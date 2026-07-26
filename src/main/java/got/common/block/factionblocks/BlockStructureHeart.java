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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.Explosion;
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
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public Item getItemDropped(int meta, Random random, int fortune) {
        return null;
    }
    @Override
    public void onBlockExploded(World world, int x, int y, int z, Explosion explosion) {
    }
    @Override
    protected boolean canSilkHarvest() {
        return false;
    }
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
        if (got.GOT.proxy.isTutorialStage9(player)) {
            if (world.isRemote) {
                got.GOT.proxy.openGOTGuiStructureHeart();
            }
            return true;
        }

        if (!world.isRemote) {
            FactionStructureSlot slot = FactionStructureManager.getStructureNearby(x, y, z);
            if (slot == null) {
                return false;
            }

            Faction playerFaction = PacketMessage.getCurrentFaction(player.getCommandSenderName());
            Map<String, List<String>> availableStructures = new HashMap<>();

            if (slot.ownerFactionID == null) {
                boolean canCapture = false;
                String forFrac = slot.for_fraction;

                if (playerFaction != null && (forFrac == null || forFrac.isEmpty() || forFrac.equalsIgnoreCase("ALL") || forFrac.equalsIgnoreCase(playerFaction.getID()))) {
                    canCapture = true;
                }

                if (canCapture) {
                    Map<String, Map<String, String>> allStructureCategorys = FactionStructureManager.structureTypes;

                    if (slot.category == FactionStructureSlot.StructureCategory.NONE) {
                        if (allStructureCategorys.containsKey("FARMS")) availableStructures.put("FARMS", new ArrayList<>(allStructureCategorys.get("FARMS").keySet()));
                        if (allStructureCategorys.containsKey("INDUSTRY")) availableStructures.put("INDUSTRY", new ArrayList<>(allStructureCategorys.get("INDUSTRY").keySet()));
                        if (allStructureCategorys.containsKey("BARN")) availableStructures.put("BARN", new ArrayList<>(allStructureCategorys.get("BARN").keySet()));
                        if (allStructureCategorys.containsKey("ENGINEERING_WORKSHOP")) availableStructures.put("ENGINEERING_WORKSHOP", new ArrayList<>(allStructureCategorys.get("ENGINEERING_WORKSHOP").keySet()));
                        if (allStructureCategorys.containsKey("STABLE")) availableStructures.put("STABLE", new ArrayList<>(allStructureCategorys.get("STABLE").keySet()));
                    } else {
                        String catName = slot.category.name();
                        if (allStructureCategorys.containsKey(catName)) {
                            availableStructures.put(catName, new ArrayList<>(allStructureCategorys.get(catName).keySet()));
                        }
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
            got.GOT.proxy.openGuiStructureBlock(x, y, z);
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

                boolean isFortress = slot.category == FactionStructureSlot.StructureCategory.FORTRESS;
                boolean canDestroy = false;

                if (isFortress) {
                    if (brain.factions.servers.StructureManager.activeWarFortresses.contains(slot.id)) {
                        double distanceSq = player.getDistanceSq(slot.xCoord + 0.5D, slot.yCoord + 0.5D, slot.zCoord + 0.5D);
                        if (distanceSq <= 250000) { // 500^2
                            canDestroy = true;
                        } else {
                            player.addChatMessage(new ChatComponentText("§cВы слишком далеко от центра крепости для атаки (нужно < 500 блоков)!"));
                        }
                    }
                } else {
                    boolean raidTimeActive = brain.factions.servers.StructureManager.isRaidTimeNow(slot) || brain.factions.servers.StructureManager.activeRaidPoints.contains(slot.id);
                    if (raidTimeActive) {
                        double distanceSq = player.getDistanceSq(slot.xCoord + 0.5D, slot.yCoord + 0.5D, slot.zCoord + 0.5D);
                        if (distanceSq <= 62500) { // 250^2
                            canDestroy = true;
                        } else {
                            player.addChatMessage(new ChatComponentText("§cВы слишком далеко от сердца точки для атаки (нужно < 250 блоков)!"));
                        }
                    }
                }

                if (!canDestroy) {
                    restoreBlock(world, x, y, z, meta, slot.id);
                    if (isFortress) {
                        player.addChatMessage(new ChatComponentText("§cЭта крепость сейчас не находится в состоянии войны!"));
                    } else {
                        player.addChatMessage(new ChatComponentText("§cЭту точку можно атаковать только во время рейда!"));
                    }
                    return;
                }

                if (got.common.faction.GOTFactionRelations.areFactionsHostile(got.common.faction.GOTFaction.forName(playerFaction.getID()), got.common.faction.GOTFaction.forName(slot.ownerFactionID))) {
                    slot.destructionCount--;
                    player.addChatMessage(new ChatComponentText("§eПрочность структуры: " + slot.destructionCount));

                    if (!isFortress && slot.securityLevel > 0) {
                        if (slot.destructionCount > 0 && slot.destructionCount % 25 == 0) {
                            brain.factions.servers.StructureManager.spawnSecurityNPCs(world, slot.xCoord, slot.yCoord, slot.zCoord, slot.ownerFactionID, slot.securityLevel);
                            player.addChatMessage(new ChatComponentText("§cГарнизон точки поднят по тревоге!"));
                        }
                    }

                    if (slot.destructionCount <= 0) {
                        if (isFortress) {
                            FactionStructureManager.setStructureOwner(slot.id, playerFaction.getID());
                            slot.ownerFactionID = playerFaction.getID();
                            slot.destructionCount = 100 + ((slot.level > 0 ? slot.level : 1) - 1) * 10;
                            slot.barracksCapacity = (slot.level > 0 ? slot.level : 1) * 10;

                            restoreBlock(world, x, y, z, meta, slot.id);
                            TileEntity te = world.getTileEntity(x, y, z);
                            if (te instanceof TileEntityStructureHeart) {
                                java.util.Arrays.fill(((TileEntityStructureHeart) te).getInventory(), null);
                                te.markDirty();
                            }
                            world.markBlockForUpdate(x, y, z);
                            slot.provisions = 0;
                            player.addChatMessage(new ChatComponentText("§aВаша фракция захватила крепость!"));
                        } else {
                            if (slot.structureFile != null && !slot.structureFile.isEmpty()) {
                                try {
                                    java.io.File structureFile = new java.io.File(new java.io.File(brain.factions.servers.CoreFaction.configFolder, "structures"), slot.structureFile);
                                    if (structureFile.exists()) {
                                        String jsonContent = com.google.common.io.Files.toString(structureFile, com.google.common.base.Charsets.UTF_8);
                                        brain.factions.structures.StructureData structureData = new com.google.gson.Gson().fromJson(jsonContent, brain.factions.structures.StructureData.class);
                                        int xOffset = structureData.getWidth() / 2;
                                        int zOffset = structureData.getDepth() / 2;
                                        for (brain.factions.structures.BlockData blockData : structureData.getBlocks()) {
                                            int blockX = slot.xCoord + blockData.getX() - xOffset;
                                            int blockY = slot.yCoord + blockData.getY() - 3;
                                            int blockZ = slot.zCoord + blockData.getZ() - zOffset;
                                            if(world.getBlock(blockX, blockY, blockZ) != net.minecraft.init.Blocks.air) {
                                                world.setBlockToAir(blockX, blockY, blockZ);
                                            }
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            world.setBlock(slot.xCoord, slot.yCoord, slot.zCoord, this, 0, 3);
                            TileEntity newTe = world.getTileEntity(slot.xCoord, slot.yCoord, slot.zCoord);
                            if (newTe instanceof TileEntityStructureHeart) {
                                ((TileEntityStructureHeart) newTe).setStructureId(slot.id);
                                java.util.Arrays.fill(((TileEntityStructureHeart) newTe).getInventory(), null);
                                newTe.markDirty();
                            }
                            world.markBlockForUpdate(slot.xCoord, slot.yCoord, slot.zCoord);

                            FactionStructureManager.setStructureOwner(slot.id, null);
                            slot.ownerFactionID = null;
                            slot.level = 0;
                            slot.securityLevel = 0;
                            slot.name = "Нейтральная территория";
                            slot.category = brain.factions.structures.FactionStructureSlot.StructureCategory.NONE;
                            slot.structureFile = null;
                            slot.destructionCount = 100;

                            player.addChatMessage(new ChatComponentText("§cРесурсная точка была полностью разрушена!"));
                        }
                    } else {
                        restoreBlock(world, x, y, z, meta, slot.id);
                    }
                    FactionStructureManager.saveStructureOwnership();
                    brain.factions.servers.CoreFaction.sendAllGui();
                } else {
                    restoreBlock(world, x, y, z, meta, slot.id);
                    player.addChatMessage(new ChatComponentText("§cВы не можете атаковать союзников!"));
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