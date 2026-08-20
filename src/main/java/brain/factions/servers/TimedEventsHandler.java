package brain.factions.servers;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import got.common.block.other.GOTBlockWildFire;
import got.common.database.GOTRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.Comparator;
import java.util.Optional;

public class TimedEventsHandler {
    private long lastTaxCheckTime = 0;
    private long lastRaidCheckTime = 0;

    private static final long CHECK_INTERVAL_TICKS = 20 * 60;
    private static final long ONE_DAY_TICKS = 20 * 60 * 60 * 24;

    @SubscribeEvent
    public void onMobDrops(LivingDropsEvent event) {
        if (event.entityLiving.worldObj.isRemote) return;
        if (event.entityLiving instanceof EntityBat) {
            if (!event.recentlyHit) return;
            if (event.entityLiving.worldObj.rand.nextFloat() < 0.8f) {
                ItemStack itemStack = new ItemStack(GOTRegistry.batWings, 1);
                EntityItem drop = new EntityItem(
                        event.entityLiving.worldObj,
                        event.entityLiving.posX,
                        event.entityLiving.posY,
                        event.entityLiving.posZ,
                        itemStack
                );
                drop.posY += 0.5;
                drop.delayBeforeCanPickup = 10;
                event.drops.add(drop);
            }
        }
    }

    @SubscribeEvent
    public void onLeftClick(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) return;

        World world = event.world;
        int x = event.x;
        int y = event.y;
        int z = event.z;

        Block directBlock = world.getBlock(x, y, z);

        if (directBlock instanceof GOTBlockWildFire) {
            extinguishFire(world, x, y, z, event);
            return;
        }

        ForgeDirection dir = ForgeDirection.getOrientation(event.face);
        int offX = x + dir.offsetX;
        int offY = y + dir.offsetY;
        int offZ = z + dir.offsetZ;

        Block offsetBlock = world.getBlock(offX, offY, offZ);

        if (offsetBlock instanceof GOTBlockWildFire) {
            extinguishFire(world, offX, offY, offZ, event);
        }
    }

    private void extinguishFire(World world, int x, int y, int z, PlayerInteractEvent event) {
        if (!world.isRemote) {
            world.playAuxSFX(1009, x, y, z, 0);
            world.setBlockToAir(x, y, z);
        } else {
            event.entityPlayer.swingItem();
        }

        if (event.isCancelable()) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        long currentTime = MinecraftServer.getServer().getTickCounter();

        if (currentTime - lastRaidCheckTime >= CHECK_INTERVAL_TICKS) {
            lastRaidCheckTime = currentTime;
            checkRaidTime();
        }

        if (currentTime - lastTaxCheckTime >= ONE_DAY_TICKS) {
            lastTaxCheckTime = currentTime;
            processDailyWarTaxes();
        }
    }

    private void checkRaidTime() {
        boolean anyRaidActive = false;

        for (FactionStructureSlot slot : FactionStructureManager.structureSlots) {
            if (StructureManager.isRaidTimeNow(slot) || StructureManager.activeRaidPoints.contains(slot.id)) {
                anyRaidActive = true;
                break;
            }
        }

        if (anyRaidActive && !StructureManager.isRaidTime) {
            StructureManager.isRaidTime = true;
            SiegeActivationManager.getInstance().rebuildRaidZones(0);
            MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("§4[!] Началось время рейдов! Структуры врагов уязвимы!"));
        } else if (!anyRaidActive && StructureManager.isRaidTime) {
            StructureManager.isRaidTime = false;
            SiegeActivationManager.getInstance().clearRaidZones();
            MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("§a[!] Время рейдов окончено! Структуры в безопасности."));
        }
    }

    private void processDailyWarTaxes() {
        ServerTaskExecutor.addScheduledTask(() -> {
            for (Faction faction : CoreFaction.factions.values()) {
                if (faction.isInWarState()) {
                    long taxAmount = (long) faction.getPlayers().size() * StructureManager.config.warTaxPerMember;
                    faction.setWarTaxOwed(faction.getWarTaxOwed() + taxAmount);

                    if (faction.getTreasury() >= faction.getWarTaxOwed()) {
                        faction.setTreasury(faction.getTreasury() - faction.getWarTaxOwed());
                        faction.setWarTaxOwed(0);
                        faction.setUnpaidWarTaxDays(0);
                    } else {
                        faction.setUnpaidWarTaxDays(faction.getUnpaidWarTaxDays() + 1);
                        if (faction.getUnpaidWarTaxDays() >= 3) {
                            Optional<FactionStructureSlot> slotToDowngrade = FactionStructureManager.structureSlots.stream()
                                    .filter(s -> faction.getID().equals(s.ownerFactionID) && s.level > 1)
                                    .max(Comparator.comparingInt(s -> s.level));

                            if (slotToDowngrade.isPresent()) {
                                FactionStructureSlot slot = slotToDowngrade.get();
                                slot.level--;

                                EntityPlayerMP leader = PacketMessage.getPlayer(faction.getLeaderName());
                                if (leader != null) {
                                    leader.addChatMessage(new ChatComponentText("§c[НАЛОГ] За неуплату налога ваша структура '" + slot.name + "' была понижена до уровня " + slot.level + "!"));
                                }
                            }

                            faction.setUnpaidWarTaxDays(0);
                        }
                    }
                }
            }
            CoreFaction.saveFactions();
            FactionStructureManager.saveStructureOwnership();
        });
    }
}
