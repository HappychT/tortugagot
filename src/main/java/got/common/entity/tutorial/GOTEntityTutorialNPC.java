package got.common.entity.tutorial;

import got.common.entity.other.GOTEntityHumanBase;
import net.minecraft.world.World;

public class GOTEntityTutorialNPC extends GOTEntityHumanBase {
    public GOTEntityTutorialNPC(World world) {
        super(world);
        this.canBeMarried = false;
        this.setSize(0.6F, 1.8F);
        // By default, no tasks. Subclasses can override or they are statues.
    }

    @Override
    public got.common.faction.GOTFaction getFaction() {
        return got.common.faction.GOTFaction.UNALIGNED;
    }

    @Override
    public void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
        this.getEntityAttribute(net.minecraft.entity.SharedMonsterAttributes.movementSpeed).setBaseValue(0.2D);
    }

    @Override
    public String getNPCName() {
        return this.familyInfo.getName();
    }

    @Override
    public void setupNPCName() {
        this.familyInfo.setName("Tutorial NPC");
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    private boolean isTargetValid(net.minecraft.entity.EntityLivingBase target) {
        if (target instanceof net.minecraft.entity.player.EntityPlayer) {
            String owner = this.getEntityData().getString("TutorialOwner");
            if (owner != null && !owner.isEmpty()) {
                if (!target.getCommandSenderName().equals(owner)) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void setAttackTarget(net.minecraft.entity.EntityLivingBase target) {
        if (target != null && !isTargetValid(target)) {
            return;
        }
        super.setAttackTarget(target);
    }

    @Override
    public boolean attackEntityAsMob(net.minecraft.entity.Entity entity) {
        if (entity instanceof net.minecraft.entity.EntityLivingBase) {
            if (!isTargetValid((net.minecraft.entity.EntityLivingBase) entity)) {
                return false;
            }
        }
        return super.attackEntityAsMob(entity);
    }

    @Override
    public boolean canBeCollidedWith() {
        if (this.worldObj.isRemote) {
            String owner = this.getEntityData().getString("TutorialOwner");
            net.minecraft.client.entity.EntityClientPlayerMP player = net.minecraft.client.Minecraft.getMinecraft().thePlayer;
            if (player != null && owner != null && !owner.isEmpty() && !owner.equals(player.getCommandSenderName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Клиентская проверка видимости NPC.
     * Чужой NPC (TutorialOwner != localPlayer) всегда невидим.
     * NPC без владельца (owner == null/empty) видим только если туториал не активен.
     * Не использует setInvisible/DataWatcher, поэтому серверная синхронизация не сбивает результат.
     */
    @Override
    @cpw.mods.fml.relauncher.SideOnly(cpw.mods.fml.relauncher.Side.CLIENT)
    public boolean isInvisibleToPlayer(net.minecraft.entity.player.EntityPlayer viewer) {
        if (this.worldObj.isRemote) {
            String owner = this.getEntityData().getString("TutorialOwner");
            if (owner != null && !owner.isEmpty()) {
                // Есть владелец — видим только ему
                return !viewer.getCommandSenderName().equals(owner);
            } else {
                // Нет владельца (captain в Limbo и т.п.) — виден, если туториал активен
                return !brain.tutorial.client.TutorialClientState.isTutorialActive;
            }
        }
        return super.isInvisibleToPlayer(viewer);
    }
}

