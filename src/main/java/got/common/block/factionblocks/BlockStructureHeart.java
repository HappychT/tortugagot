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
import got.common.database.GOTCreativeTabs;
import got.common.faction.GOTFaction;
import got.common.faction.GOTFactionRelations;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            FactionStructureSlot slot = FactionStructureManager.getStructureNearby(x, y, z);
            if (slot == null) {
                return false;
            }

            Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
            Map<String, List<String>> availableStructures = new HashMap<>();

            if (slot.ownerFactionID == null) {
                boolean canCapture = false;
                if (playerFaction != null && (slot.for_fraction.equalsIgnoreCase("ALL") || slot.for_fraction.equalsIgnoreCase(playerFaction.getID()))) {
                    canCapture = true;
                } else if (playerFaction == null && slot.for_fraction.equalsIgnoreCase("ALL")) {

                }

                if (canCapture) {
                    Map<String, Map<String, String>> allStructureTypes = FactionStructureManager.structureTypes;
                    if (slot.type == FactionStructureSlot.StructureType.FORTRESS && allStructureTypes.containsKey("FORTRESS")) {
                        availableStructures.put("FORTRESS", new ArrayList<>(allStructureTypes.get("FORTRESS").keySet()));
                    } else if (slot.type == FactionStructureSlot.StructureType.RESOURCE_POINT) {
                        if (allStructureTypes.containsKey("FARMS")) availableStructures.put("FARMS", new ArrayList<>(allStructureTypes.get("FARMS").keySet()));
                        if (allStructureTypes.containsKey("INDUSTRY")) availableStructures.put("INDUSTRY", new ArrayList<>(allStructureTypes.get("INDUSTRY").keySet()));
                    } else if (slot.type == FactionStructureSlot.StructureType.BARN && allStructureTypes.containsKey("BARN")) {
                        availableStructures.put("BARN", new ArrayList<>(allStructureTypes.get("BARN").keySet()));
                    } else if (slot.type == FactionStructureSlot.StructureType.ENGINEERING_WORKSHOP && allStructureTypes.containsKey("ENGINEERING_WORKSHOP")) {
                        availableStructures.put("ENGINEERING_WORKSHOP", new ArrayList<>(allStructureTypes.get("ENGINEERING_WORKSHOP").keySet()));
                    }
                } else {
                    player.addChatMessage(new ChatComponentText("§cЭта точка не предназначена для вашей фракции или вы не состоите во фракции."));
                    CoreFaction.brainChannel.sendTo(new PacketStructureGUISync(x, y, z, slot, availableStructures), (EntityPlayerMP) player);
                    return true;
                }
            } else {
                boolean canOpen = false;
                if (playerFaction != null) {
                    if (playerFaction.getID().equals(slot.ownerFactionID)) {
                        canOpen = true;
                    } else {
                        if (GOTFactionRelations.areFactionsHostile(GOTFaction.forName(playerFaction.getID()), GOTFaction.forName(slot.ownerFactionID))) {
                            player.addChatMessage(new ChatComponentText("§cЭто территория враждебной фракции!"));
                        } else {
                            canOpen = true;
                            player.addChatMessage(new ChatComponentText("§eЭто территория другой фракции."));
                        }
                    }
                }
                if (!canOpen) {
                    player.addChatMessage(new ChatComponentText("§cУ вас нет доступа к этой структуре."));
                    CoreFaction.brainChannel.sendTo(new PacketStructureGUISync(x, y, z, slot, availableStructures), (EntityPlayerMP) player);
                    return true;
                }
            }

            CoreFaction.brainChannel.sendTo(new PacketStructureGUISync(x, y, z, slot, availableStructures), (EntityPlayerMP) player);

        } else if (!Annot.SERVER) {
            Minecraft.getMinecraft().displayGuiScreen(new got.client.gui.faction.GuiStructureBlock(x, y, z, null));
        }
        return true;
    }

    @Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        if (!world.isRemote) {
            FactionStructureSlot slot = FactionStructureManager.getStructureNearby(x, y, z);
            EntityPlayer player = world.getClosestPlayer((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, -1.0D);

            if (slot != null && slot.ownerFactionID != null) {
                if (player == null) {
                    restoreBlock(world, x, y, z, meta, slot.id);
                    return;
                }

                Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());

                if (playerFaction == null || playerFaction.getID().equals(slot.ownerFactionID)) {
                    restoreBlock(world, x, y, z, meta, slot.id);
                    player.addChatMessage(new ChatComponentText("§cВы не можете разрушить свою структуру!"));
                    return;
                }

                boolean raidTimeActive = StructureManager.isRaidTimeNow(slot);

                if (!raidTimeActive) {
                    restoreBlock(world, x, y, z, meta, slot.id);
                    player.addChatMessage(new ChatComponentText("§cСтруктуру можно разрушать только во время рейд-тайма!"));
                    return;
                }

                if (GOTFactionRelations.areFactionsHostile(GOTFaction.forName(playerFaction.getID()), GOTFaction.forName(slot.ownerFactionID))) {
                    slot.destructionCount--;
                    player.addChatMessage(new ChatComponentText("§eПрочность структуры: " + slot.destructionCount));

                    if (slot.destructionCount <= 0) {
                        boolean isFortress = slot.type == FactionStructureSlot.StructureType.FORTRESS;
                        if (isFortress) {
                            FactionStructureManager.setStructureOwner(slot.id, playerFaction.getID());
                            slot.ownerFactionID = playerFaction.getID();
                            slot.destructionCount = StructureManager.config.structureBreakCounts.getOrDefault(slot.level, slot.level);

                            restoreBlock(world, x, y, z, meta, slot.id);
                            TileEntity te = world.getTileEntity(x, y, z);
                            if (te instanceof TileEntityStructureHeart) {
                                Arrays.fill(((TileEntityStructureHeart) te).getInventory(), null);
                                te.markDirty();
                            }
                            world.markBlockForUpdate(x, y, z);
                            slot.provisions = 0;
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
                                    CoreFaction.logger().severe("Error removing structure blocks: " + e.getMessage());
                                    e.printStackTrace();
                                }
                            }

                            world.setBlock(slot.xCoord, slot.yCoord, slot.zCoord, this, 0, 3);
                            TileEntity newTe = world.getTileEntity(slot.xCoord, slot.yCoord, slot.zCoord);
                            if (newTe instanceof TileEntityStructureHeart) {
                                ((TileEntityStructureHeart) newTe).setStructureId(slot.id);
                                Arrays.fill(((TileEntityStructureHeart) newTe).getInventory(), null);
                                newTe.markDirty();
                            }
                            world.markBlockForUpdate(slot.xCoord, slot.yCoord, slot.zCoord);

                            FactionStructureManager.setStructureOwner(slot.id, null);
                            slot.ownerFactionID = null;
                            slot.level = 0;
                            slot.name = "Нейтральная территория";
                            slot.category = FactionStructureSlot.StructureCategory.NONE;
                            slot.structureFile = null;
                            slot.destructionCount = 1;

                            player.addChatMessage(new ChatComponentText("§cВы разрушили ресурсную точку!"));
                        }
                    } else {
                        restoreBlock(world, x, y, z, meta, slot.id);
                    }
                    FactionStructureManager.saveStructureOwnership();
                    CoreFaction.sendAllGui();
                } else {
                    restoreBlock(world, x, y, z, meta, slot.id);
                    player.addChatMessage(new ChatComponentText("§cВы не можете атаковать структуры союзных или нейтральных фракций!"));
                }
            }
        }
    }

    private void restoreBlock(World world, int x, int y, int z, int meta, String structureId) {
        world.setBlock(x, y, z, this, meta, 3);
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityStructureHeart) {
            ((TileEntityStructureHeart) te).setStructureId(structureId);
            te.markDirty();
        }
        world.markBlockForUpdate(x, y, z);
    }

    @Override
    public float getPlayerRelativeBlockHardness(EntityPlayer player, World world, int x, int y, int z) {
        float hardness = this.getBlockHardness(world, x, y, z);
        if (hardness < 0.0F) return 0.0F;
        return player.getBreakSpeed(this, true, world.getBlockMetadata(x, y, z), x, y, z) / hardness / 30F;
    }
}