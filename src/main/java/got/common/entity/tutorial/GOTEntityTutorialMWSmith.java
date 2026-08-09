package got.common.entity.tutorial;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Нейтральный кузнец-оружейник (MW, меч) для туториала (stage 6, фаза 2).
 * Открывает GOTGuiFactionBlacksmith с MW-типом.
 */
public class GOTEntityTutorialMWSmith extends GOTEnchaldBlacksmith {

    public GOTEntityTutorialMWSmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.UNALIGNED;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.MW;
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public boolean isEntityInvulnerable() {
        return true;
    }

    @Override
    public boolean interact(EntityPlayer player) {
        if (!player.worldObj.isRemote) {
            String owner = this.getEntityData().getString("TutorialOwner");
            if (owner != null && !owner.isEmpty() && !owner.equals(player.getCommandSenderName())) {
                return false;
            }
            got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
            if (ext == null || !ext.isTutorialActive() || ext.getTutorialStage() != 6) {
                return false;
            }
            // Доступен только когда бронник уже сделан (prog >= 4)
            if (ext.getTutorialProgress() < 4) {
                return false;
            }
            player.openGui(got.GOT.instance, 89, this.worldObj, this.getEntityId(), 0, 0);
            brain.tutorial.TutorialManager.getInstance().advanceStage6(player, 5);
        }
        return true;
    }

    @Override
    public boolean isSlotUnlocked(int slot) {
        return true;
    }

    @Override
    @cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
    public boolean isInvisibleToPlayer(net.minecraft.entity.player.EntityPlayer viewer) {
        if (this.worldObj.isRemote) {
            String owner = this.getEntityData().getString("TutorialOwner");
            if (owner != null && !owner.isEmpty()) {
                return !viewer.getCommandSenderName().equals(owner);
            }
            return !brain.tutorial.client.TutorialClientState.isTutorialActive;
        }
        return super.isInvisibleToPlayer(viewer);
    }
}
