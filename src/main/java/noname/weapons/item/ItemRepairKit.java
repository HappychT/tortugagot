package noname.weapons.item;

import got.common.database.GOTCreativeTabs;
import com.tortugagot.togcore.technology.TOGEngineeringTechnology;
import noname.weapons.world.BlockDamageStorage;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ItemRepairKit extends Item {

    private static final int REQUIRED_BLOCK_COUNT = 10;
    private static final int USE_DURATION = 40;
    private static final Map<String, RepairSession> ACTIVE_REPAIRS = new ConcurrentHashMap<String, RepairSession>();

    public ItemRepairKit() {
        this.setUnlocalizedName("repair_kit");
        this.setTextureName("got:repair_kit");
        this.setCreativeTab(GOTCreativeTabs.tabsWaepons);
        this.setMaxStackSize(1);
        this.setMaxDamage(50);
        this.setNoRepair();
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote && !TOGEngineeringTechnology.canUseRepairKit(player)) {
            TOGEngineeringTechnology.notifyRepairKitBlocked(player);
            return false;
        }

        if (!player.canPlayerEdit(x, y, z, side, stack)) {
            return false;
        }

        Block block = world.getBlock(x, y, z);
        if (block == null || world.isAirBlock(x, y, z)) {
            if (!world.isRemote) {
                sendStatusMessage(player, "Блок не найден.");
            }
            return false;
        }

        float storedDamage = BlockDamageStorage.getBlockDamage(world, x, y, z);
        if (storedDamage <= 0.0F) {
            if (!world.isRemote) {
                sendStatusMessage(player, "Блок не повреждён.");
            }
            return false;
        }

        int metadata = world.getBlockMetadata(x, y, z);
        Item blockItem = Item.getItemFromBlock(block);
        if (blockItem == null && !player.capabilities.isCreativeMode) {
            if (!world.isRemote) {
                sendStatusMessage(player, "Невозможно подобрать подходящий материал.");
            }
            return false;
        }

        if (!player.capabilities.isCreativeMode && !hasRequiredMaterials(player, blockItem, metadata)) {
            if (!world.isRemote) {
                sendStatusMessage(player, "Недостаточно блоков для ремонта.");
            }
            return false;
        }

        if (world.isRemote) {
            return true;
        }

        String playerKey = player.getUniqueID().toString();
        RepairSession existing = ACTIVE_REPAIRS.remove(playerKey);
        if (existing != null) {
            sendStatusMessage(player, "Ремонт прерван.");
        }

        int slotIndex = findStackSlot(player, stack);
        RepairSession session = new RepairSession(world.provider.dimensionId, x, y, z,
                Block.blockRegistry.getNameForObject(block), metadata,
                world.getTotalWorldTime(), slotIndex, blockItem);
        ACTIVE_REPAIRS.put(playerKey, session);
        sendStatusMessage(player, "Ремонт начат. Держите набор, пока работа не завершится.");
        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isHeld) {
        super.onUpdate(stack, world, entity, slotIndex, isHeld);

        if (world.isRemote || !(entity instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;
        String playerKey = player.getUniqueID().toString();
        RepairSession session = ACTIVE_REPAIRS.get(playerKey);
        if (session == null) {
            return;
        }

        if (session.slotIndex != slotIndex) {
            if (player.inventory.currentItem != session.slotIndex) {
                abortRepair(player, session, "Ремонт прерван.");
            }
            return;
        }

        if (player.inventory.currentItem != session.slotIndex || player.getCurrentEquippedItem() != stack) {
            abortRepair(player, session, "Ремонт прерван.");
            return;
        }

        if (world.provider.dimensionId != session.dimension) {
            abortRepair(player, session, "Нужно быть в том же измерении для ремонта.");
            return;
        }

        Block block = world.getBlock(session.x, session.y, session.z);
        if (block == null || world.isAirBlock(session.x, session.y, session.z)) {
            abortRepair(player, session, "Блок изменён, ремонт отменён.");
            return;
        }

        String currentBlockId = Block.blockRegistry.getNameForObject(block);
        int currentMeta = world.getBlockMetadata(session.x, session.y, session.z);
        if (!session.blockId.equals(currentBlockId) || currentMeta != session.metadata) {
            abortRepair(player, session, "Блок изменён, ремонт отменён.");
            return;
        }

        float damage = BlockDamageStorage.getBlockDamage(world, session.x, session.y, session.z);
        if (damage <= 0.0F) {
            if (!world.isRemote) {
                sendStatusMessage(player, "Блок уже восстановлен.");
            }
            ACTIVE_REPAIRS.remove(playerKey);
            return;
        }

        long elapsed = world.getTotalWorldTime() - session.startTick;
        if (elapsed < USE_DURATION) {
            return;
        }

        if (!player.capabilities.isCreativeMode) {
            if (session.blockItem == null || !consumeMaterials(player, session.blockItem, session.metadata)) {
                sendStatusMessage(player, "Недостаточно блоков для ремонта.");
                ACTIVE_REPAIRS.remove(playerKey);
                return;
            }
            stack.damageItem(1, player);
        }

        BlockDamageStorage.removeBlockDamage(world, session.x, session.y, session.z);
        clearVisualCracks(world, session.x, session.y, session.z);
        world.markBlockForUpdate(session.x, session.y, session.z);
        sendStatusMessage(player, "Блок восстановлен.");
        ACTIVE_REPAIRS.remove(playerKey);
    }

    private boolean hasRequiredMaterials(EntityPlayer player, Item blockItem, int metadata) {
        int total = 0;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack slot = player.inventory.getStackInSlot(i);
            if (slot != null && slot.getItem() == blockItem && slot.getItemDamage() == metadata) {
                total += slot.stackSize;
                if (total >= REQUIRED_BLOCK_COUNT) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean consumeMaterials(EntityPlayer player, Item blockItem, int metadata) {
        if (player.capabilities.isCreativeMode) {
            return true;
        }

        if (!hasRequiredMaterials(player, blockItem, metadata)) {
            return false;
        }

        int remaining = REQUIRED_BLOCK_COUNT;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack slot = player.inventory.getStackInSlot(i);
            if (slot != null && slot.getItem() == blockItem && slot.getItemDamage() == metadata) {
                int take = Math.min(slot.stackSize, remaining);
                slot.stackSize -= take;
                if (slot.stackSize <= 0) {
                    player.inventory.setInventorySlotContents(i, null);
                }
                remaining -= take;
                if (remaining <= 0) {
                    player.inventoryContainer.detectAndSendChanges();
                    return true;
                }
            }
        }
        player.inventoryContainer.detectAndSendChanges();
        return false;
    }

    private void clearVisualCracks(World world, int x, int y, int z) {
        if (world instanceof WorldServer) {
            long hash = ((long) x << 20) ^ ((long) y << 10) ^ (long) z;
            int entityId = -(int) (hash & 0x7FFFFFFF);
            if (entityId == 0 || entityId == -1) {
                entityId = -(Math.abs(x) % 10000 * 10000 + Math.abs(y) % 256 * 256 + Math.abs(z) % 10000) - 2;
            }
            world.destroyBlockInWorldPartially(entityId, x, y, z, -1);
        }
    }

    private void sendStatusMessage(EntityPlayer player, String message) {
        player.addChatComponentMessage(new ChatComponentText(message));
    }

    private void abortRepair(EntityPlayer player, RepairSession session, String message) {
        if (message != null && !message.isEmpty()) {
            sendStatusMessage(player, message);
        }
        ACTIVE_REPAIRS.remove(player.getUniqueID().toString());
    }

    private int findStackSlot(EntityPlayer player, ItemStack stack) {
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            if (player.inventory.getStackInSlot(i) == stack) {
                return i;
            }
        }
        return player.inventory.currentItem;
    }

    private static final class RepairSession {
        final int dimension;
        final int x;
        final int y;
        final int z;
        final String blockId;
        final int metadata;
        final long startTick;
        final int slotIndex;
        final Item blockItem;

        RepairSession(int dimension, int x, int y, int z, String blockId, int metadata,
                      long startTick, int slotIndex, Item blockItem) {
            this.dimension = dimension;
            this.x = x;
            this.y = y;
            this.z = z;
            this.blockId = blockId;
            this.metadata = metadata;
            this.startTick = startTick;
            this.slotIndex = slotIndex;
            this.blockItem = blockItem;
        }
    }
}

