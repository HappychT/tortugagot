package got.common.entity.tutorial;

import got.common.entity.GOTEnchaldBlacksmith;
import got.common.faction.GOTFaction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Нейтральный NPC-бронник для туториала (stage 6).
 * Наследует GOTEnchaldBlacksmith (ARM-тип, 23 слота), не привязан ни к какой фракции.
 * Скрывается от чужих игроков через TutorialOwner NBT.
 */
public class GOTEntityTutorialArmorsmith extends GOTEnchaldBlacksmith {

    public GOTEntityTutorialArmorsmith(World world) {
        super(world);
    }

    @Override
    public GOTFaction getFaction() {
        return GOTFaction.UNALIGNED;
    }

    @Override
    public BlacksmithType getBlacksmithType() {
        return BlacksmithType.ARM;
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public boolean isEntityInvulnerable() {
        return true;
    }

    /**
     * Бронник доступен только владельцу туториала.
     */
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
            player.openGui(got.GOT.instance, 89, this.worldObj, this.getEntityId(), 0, 0);
            brain.tutorial.TutorialManager.getInstance().advanceStage6(player, 1);
        }
        return true;
    }

    @Override
    public void applyUnlockSlot(EntityPlayer player, int slot) {
        // Use the parent's mechanism (slots[], sendClientPacket)
        super.applyUnlockSlot(player, slot);

        // Tutorial progression hook
        got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(player);
        if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 6 && ext.getTutorialProgress() == 1) {
             brain.tutorial.TutorialManager.getInstance().advanceStage6(player, 2);
        }
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
