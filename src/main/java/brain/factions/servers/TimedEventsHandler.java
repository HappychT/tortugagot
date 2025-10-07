package brain.factions.servers;

import brain.factions.Faction;
import brain.factions.network.PacketMessage;
import brain.factions.structures.FactionStructureManager;
import brain.factions.structures.FactionStructureSlot;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

public class TimedEventsHandler {
    private long lastTaxCheckTime = 0;
    private long lastRaidCheckTime = 0;

    private static final long CHECK_INTERVAL_TICKS = 20 * 60;
    private static final long ONE_DAY_TICKS = 20 * 60 * 60 * 24;


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
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();

        boolean shouldBeRaidTime = (hour >= 18 && hour < 20);

        if (shouldBeRaidTime && !StructureManager.isRaidTime) {
            StructureManager.isRaidTime = true;
            MinecraftServer.getServer().getConfigurationManager().sendChatMsg(new ChatComponentText("§4[!] Началось время рейдов! Структуры врагов уязвимы!"));
        } else if (!shouldBeRaidTime && StructureManager.isRaidTime) {
            StructureManager.isRaidTime = false;
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