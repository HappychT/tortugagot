package got.common.block.factionblocks;

import brain.factions.Annot;
import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import brain.factions.network.PacketStructureGUISync;
import brain.factions.servers.CoreFaction;
import brain.factions.servers.StructureManager;
import brain.factions.structures.BlockData;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import brain.factions.structures.StructureData;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
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
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.io.File;
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
            FactionStructureSlot slot = FactionStructureManager.getStructureByCoords(x, z);
            if (slot == null) return true;

            Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
            boolean canOpen = false;

            if (slot.ownerFactionID == null) {
                if (playerFaction != null && (slot.for_fraction.equalsIgnoreCase("ALL") || slot.for_fraction.equalsIgnoreCase(playerFaction.getID()))) {
                    canOpen = true;
                } else if (playerFaction == null && slot.for_fraction.equalsIgnoreCase("ALL")) {
                    // если надо чтобы без фракции тоже могли взаимодействовать
                    //canOpen = true;
                }
                else {
                    player.addChatMessage(new ChatComponentText("§cЭта точка не предназначена для вашей фракции."));
                    return true;
                }
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
            FactionStructureSlot slot = FactionStructureManager.getStructureByCoords(x, z);

            if (slot != null && slot.ownerFactionID != null) {
                EntityPlayer player = world.getClosestPlayer((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, -1.0D);
                if (player == null) return;

                Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
                if (playerFaction == null || playerFaction.getID().equals(slot.ownerFactionID)) return;

                slot.destructionCount--;

                if (slot.destructionCount <= 0) {
                    if (slot.type == FactionStructureSlot.StructureType.FORTRESS) {
                        FactionStructureManager.setStructureOwner(slot.id, playerFaction.getID());
                        slot.ownerFactionID = playerFaction.getID();
                        slot.destructionCount = StructureManager.config.structureBreakCounts.getOrDefault(slot.level, slot.level);
                        world.setBlock(x, y, z, this, meta, 3);
                        player.addChatMessage(new ChatComponentText("§aВы захватили крепость!"));
                    } else {
                        if (slot.structureFile != null && !slot.structureFile.isEmpty()) {
                            try {
                                File structureFile = new File(new File(CoreFaction.configFolder, "structures"), slot.structureFile);
                                if (structureFile.exists()) {
                                    String jsonContent = Files.toString(structureFile, Charsets.UTF_8);
                                    StructureData structureData = new Gson().fromJson(jsonContent, StructureData.class);

                                    int xOffset = structureData.getWidth() / 2;
                                    int zOffset = structureData.getDepth() / 2;

                                    for (BlockData blockData : structureData.getBlocks()) {
                                        int blockX = slot.xCoord + blockData.getX() - xOffset;
                                        int blockY = slot.yCoord + blockData.getY() - 3;
                                        int blockZ = slot.zCoord + blockData.getZ() - zOffset;

                                        if(world.getBlock(blockX, blockY, blockZ) != Blocks.air) {
                                            world.setBlockToAir(blockX, blockY, blockZ);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.err.println("Не могу убрать структуру слот " + slot.id);
                                e.printStackTrace();
                            }
                        }

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

                        world.setBlock(slot.xCoord, slot.yCoord, slot.zCoord, this, 0, 3);

                        FactionStructureManager.setStructureOwner(slot.id, null);
                        slot.ownerFactionID = null;
                        slot.level = 0;
                        slot.name = "Нейтральная территория";
                        slot.category = FactionStructureSlot.StructureCategory.NONE;

                        player.addChatMessage(new ChatComponentText("§cВы разрушили ресурсную точку!"));
                    }
                } else {
                    world.setBlock(x, y, z, this, meta, 3);
                    TileEntity te = world.getTileEntity(x, y, z);
                    if (te instanceof TileEntityStructureHeart) {
                        ((TileEntityStructureHeart) te).setStructureId(slot.id);
                    }
                    player.addChatMessage(new ChatComponentText("§eПрочность структуры: " + slot.destructionCount));
                }
                FactionStructureManager.saveStructureOwnership();
                CoreFaction.sendAllGui();
            }
        }
        super.onBlockDestroyedByPlayer(world, x, y, z, meta);
    }
    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        if (!StructureManager.isRaidTimeActive()) return -1.0f;

        FactionStructureSlot slot = FactionStructureManager.getStructureByCoords(x, z);
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