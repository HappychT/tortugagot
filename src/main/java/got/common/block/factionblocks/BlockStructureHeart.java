package got.common.block.factionblocks;

import brain.factions.Annot;
import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import brain.factions.network.PacketStructureGUISync;
import brain.factions.servers.CoreFaction;
import brain.factions.servers.StructureManager;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import cpw.mods.fml.relauncher.Side;
import got.GOT;
import got.common.database.GOTCreativeTabs;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.*;

public class BlockStructureHeart extends BlockContainer {

    public BlockStructureHeart() {
        super(Material.iron);
        setBlockName("structureHeart");
        setBlockTextureName("minecraft:obsidian");
        setCreativeTab(GOTCreativeTabs.tabBlock);
        setHardness(50.0F);
        setResistance(6000000.0F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileEntityStructureHeart();
    }


    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            FactionStructureSlot slot = FactionStructureManager.getStructureByCoords(x, y, z);
            if (slot == null) return true;

            Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
            boolean canOpen = false;

            if (slot.ownerFactionID == null) {
                canOpen = true;
            } else if (playerFaction != null) {
                if (playerFaction.getID().equals(slot.ownerFactionID)) {
                    canOpen = true;
                } else {
                    if (GOTFactionRelations.areFactionsHostile(GOTFaction.forName(playerFaction.getID()), GOTFaction.forName(slot.ownerFactionID))
                            && playerFaction.playerHasPermission(player.getCommandSenderName(), Faction.Permission.CAN_MANAGE_STRUCTURES)) {
                        canOpen = true;
                    }
                }
            }

            if (canOpen) {
                Map<String, List<String>> availableStructures = new HashMap<>();
                if (slot.ownerFactionID == null) {
                    for (Map.Entry<String, Map<String, String>> entry : FactionStructureManager.structureTypes.entrySet()) {
                        String category = entry.getKey();
                        boolean isFortressSlot = slot.type == FactionStructureSlot.StructureType.FORTRESS;
                        boolean isFortressCategory = category.equals("FORTRESS");

                        if (isFortressSlot == isFortressCategory) {
                            availableStructures.put(category, new ArrayList<>(entry.getValue().keySet()));
                        }
                    }
                }
                CoreFaction.brainChannel.sendTo(new PacketStructureGUISync(x, y, z, slot, availableStructures), (EntityPlayerMP) player);
            } else {
                player.addChatMessage(new ChatComponentText("§cУ вас нет доступа к этой структуре."));
            }

        } else if (!Annot.SERVER) {
            Minecraft.getMinecraft().displayGuiScreen(new got.client.gui.faction.GuiStructureBlock(x, y, z, null));
        }
        return true;
    }
    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        if (!world.isRemote) {
            FactionStructureSlot slot = FactionStructureManager.getStructureByCoords(x, y, z);
            if (slot != null) {
                slot.destructionCount--;

                if (slot.destructionCount <= 0) {
                    world.setBlockToAir(x, y, z);
                    TileEntity te = world.getTileEntity(x, y, z);
                    if (te instanceof TileEntityStructureHeart) {
                        TileEntityStructureHeart heart = (TileEntityStructureHeart) te;
                        for (ItemStack itemStack : heart.getInventory()) {
                            if (itemStack != null) {
                                float f = world.rand.nextFloat() * 0.8F + 0.1F;
                                float f1 = world.rand.nextFloat() * 0.8F + 0.1F;
                                float f2 = world.rand.nextFloat() * 0.8F + 0.1F;
                                EntityItem entityitem = new EntityItem(world, x + f, y + f1, z + f2, itemStack);
                                world.spawnEntityInWorld(entityitem);
                            }
                        }
                    }
                    FactionStructureManager.setStructureOwner(slot.id, null);
                    slot.ownerFactionID = null;
                    slot.level = 0;
                    slot.name = "Нейтральная территория";
                    slot.category = FactionStructureSlot.StructureCategory.NONE;

                    world.removeTileEntity(x, y, z);
                } else {
                    world.setBlock(x, y, z, this, meta, 3);
                    TileEntity te = world.getTileEntity(x, y, z);
                    if (te instanceof TileEntityStructureHeart) {
                        ((TileEntityStructureHeart) te).setStructureId(slot.id);
                    }
                }
            }
        }
        super.onBlockDestroyedByPlayer(world, x, y, z, meta);
    }


    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        if (!StructureManager.isRaidTimeActive()) return -1.0f;

        FactionStructureSlot slot = FactionStructureManager.getStructureByCoords(x, y, z);
        if (slot == null || slot.ownerFactionID == null) return -1.0f;

        Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
        if (playerFaction == null) return -1.0f;

        if (playerFaction.getID().equals(slot.ownerFactionID)) return -1.0f;

        if (GOTFactionRelations.areFactionsHostile(GOTFaction.forName(playerFaction.getID()), GOTFaction.forName(slot.ownerFactionID))) {
            float strength = player.getBreakSpeed(this, false, world.getBlockMetadata(x, y, z), x, y, z);
            return strength / getBlockHardness(world, x, y, z) / 30F;
        }

        return -1.0f;
    }
}