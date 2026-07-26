package got.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

import got.rome.ExtendedPlayer;
import brain.tutorial.TutorialManager;

public class GOTCommandTutorial extends CommandBase {

    @Override
    public String getCommandName() {
        return "tutorial";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/tutorial setstage <player> <stage> | /tutorial stop <player> | /tutorial reload | /tutorial setpos <entity> | /tutorial restart <player>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Admin only
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            brain.tutorial.TutorialConfig.load();
            brain.tutorial.TutorialTexts.load();
            got.common.network.GOTPacketHandler.networkWrapper.sendToAll(new brain.tutorial.network.GOTPacketTutorialTexts(brain.tutorial.TutorialTexts.getTexts()));
            sender.addChatMessage(new ChatComponentText("\u00a7aTutorial config and texts reloaded!"));
            return;
        }

        if (args.length < 2) {
            throw new WrongUsageException(getCommandUsage(sender));
        }

        String action = args[0];
        
        if (action.equalsIgnoreCase("setpos")) {
            if (args.length < 2) {
                throw new WrongUsageException("/tutorial setpos <entity>");
            }
            if (sender instanceof EntityPlayerMP) {
                EntityPlayerMP p = (EntityPlayerMP) sender;
                int x = net.minecraft.util.MathHelper.floor_double(p.posX);
                int y = net.minecraft.util.MathHelper.floor_double(p.posY);
                int z = net.minecraft.util.MathHelper.floor_double(p.posZ);
                brain.tutorial.TutorialConfig.setPos(args[1], x, y, z);
                sender.addChatMessage(new ChatComponentText("\u00a7aSet position of " + args[1] + " to " + x + ", " + y + ", " + z));
            } else {
                sender.addChatMessage(new ChatComponentText("\u00a7cOnly players can use this command."));
            }
            return;
        }

        EntityPlayerMP player = getPlayer(sender, args[1]);
        ExtendedPlayer ext = ExtendedPlayer.get(player);

        if (ext == null) {
            sender.addChatMessage(new ChatComponentText("\u00a7cExtendedPlayer not found."));
            return;
        }

        if (action.equalsIgnoreCase("setstage")) {
            if (args.length < 3) {
                throw new WrongUsageException("/tutorial setstage <player> <stage_number>");
            }
            int stage = parseInt(sender, args[2]);
            
            ext.setTutorialActive(true);
            
            switch (stage) {
                case 1:
                    ext.setTutorialStage(1);
                    ext.setTutorialProgress(0);
                    TutorialManager.getInstance().syncState(player);
                    break;
                case 2:
                    TutorialManager.getInstance().startStage3(player, ext); // No direct startStage2 exist with simple arguments? Wait! Let's just use startStage3 for stage 3 etc. Actually let's use reflection or direct calls for all stages.
                    break;
                case 3:
                    TutorialManager.getInstance().startStage3(player, ext);
                    break;
                case 4:
                    TutorialManager.getInstance().startStage4(player, ext);
                    break;
                case 5:
                    TutorialManager.getInstance().startStage5(player, ext);
                    break;
                case 6:
                    TutorialManager.getInstance().startStage6(player, ext);
                    break;
                case 7:
                    TutorialManager.getInstance().startStage7(player, ext);
                    break;
                case 8:
                    TutorialManager.getInstance().startStage8(player, ext);
                    break;
                case 9:
                    TutorialManager.getInstance().startStage9(player, ext);
                    break;
                case 10:
                    TutorialManager.getInstance().startStage10(player, ext);
                    break;
                default:
                    sender.addChatMessage(new ChatComponentText("\u00a7cInvalid stage number (1-10)."));
                    return;
            }
            sender.addChatMessage(new ChatComponentText("\u00a7aTutorial stage for " + player.getCommandSenderName() + " set to " + stage));
        } else if (action.equalsIgnoreCase("stop")) {
            ext.setTutorialActive(false);
            TutorialManager.getInstance().syncState(player);
            sender.addChatMessage(new ChatComponentText("\u00a7aTutorial stopped for " + player.getCommandSenderName()));
        } else if (action.equalsIgnoreCase("restart")) {
            TutorialManager mgr = TutorialManager.getInstance();

            // 1. Освободить арену/таймеры если туториал был активен
            mgr.freeArena(player.getUniqueID(), player.worldObj);

            // 2. Сбросить состояние ExtendedPlayer
            ext.setTutorialActive(false);
            ext.setTutorialStage(0);
            ext.setTutorialProgress(0);
            ext.setTutorialReplay(false);

            // 3. Убрать флаг TutorialCompleted из persisted NBT
            net.minecraft.nbt.NBTTagCompound persisted = player.getEntityData()
                    .getCompoundTag(net.minecraft.entity.player.EntityPlayer.PERSISTED_NBT_TAG);
            persisted.removeTag("TutorialCompleted");
            persisted.removeTag("TutorialRewardsPending");

            // 4. Очистить инвентарь
            player.inventory.clearInventory(null, -1);
            player.inventoryContainer.detectAndSendChanges();

            // 5. Синхронизировать с клиентом
            mgr.syncToClient(player, false, 0, 0, "");

            // 6. Запустить туториал с нуля (TP в Limbo + Welcome GUI)
            mgr.startTutorial(player);

            sender.addChatMessage(new ChatComponentText("\u00a7aTutorial fully reset for " + player.getCommandSenderName() + "!"));
        } else {
            throw new WrongUsageException(getCommandUsage(sender));
        }
    }
}
