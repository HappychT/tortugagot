package got.common.item.other;

import java.lang.reflect.InvocationTargetException;

import got.common.GOTLevelData;
import got.common.GOTPlayerData;
import got.common.database.GOTCreativeTabs;
import got.common.entity.animal.GOTEntityHorse;
import got.common.util.GOTReflection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class GOTItemBridle extends Item {
    private Class<? extends GOTEntityHorse> entity;

    public GOTItemBridle(Class<? extends GOTEntityHorse> entityClass) {
        super();
        setMaxStackSize(1);
        setCreativeTab(GOTCreativeTabs.tabMisc);
        this.entity = entityClass;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
        if(!world.isRemote) {
            GOTPlayerData data = GOTLevelData.getData(entityplayer);
            if(!entityplayer.isRiding()) {
                if(data.getBridleMount() == -1) {
                    GOTEntityHorse mount = new GOTEntityHorse(world);
                    try {
                        mount = this.entity.getConstructor(World.class).newInstance(world);
                    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                        e.printStackTrace();
                    }
                    mount.setTamedBy(entityplayer);
                    mount.setHorseSaddled(true);
                    mount.setLocationAndAngles(entityplayer.posX, entityplayer.posY, entityplayer.posZ, entityplayer.rotationYaw, entityplayer.rotationPitch);
                    mount.setCustomNameTag(StatCollector.translateToLocal("got.bridle_mount"));
                    setupAttrib(mount);
                    world.spawnEntityInWorld(mount);
                    entityplayer.mountEntity(mount);
                    data.setBridleMount(mount.getEntityId());
                    return itemstack;
                }
                else {
                    Entity entity = world.getEntityByID(data.getBridleMount());
                    if(!(entity instanceof EntityLivingBase)) return itemstack;
                    EntityLivingBase mount = (EntityLivingBase)entity;
                    mount.setDead();
                    data.setBridleMount(-1);
                    return itemstack;
                }
            }
            if (entityplayer.ridingEntity != null && entityplayer.ridingEntity.getEntityId() == data.getBridleMount()) {
                entityplayer.dismountEntity(entityplayer.ridingEntity);
                entityplayer.ridingEntity.setDead();
                entityplayer.ridingEntity = null;
                data.setBridleMount(-1);
                return itemstack;
            }
        }
        return super.onItemRightClick(itemstack, world, entityplayer);
    }

    public static void setupAttrib(GOTEntityHorse entity) {
        float healthBoost = 1.5f;
        float speedBoost = 1.3f;
        float jumpAdd = 0.2f;

        double maxHealth = entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).getAttributeValue();
        entity.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(maxHealth *= healthBoost);
        entity.setHealth(entity.getMaxHealth());

        double movementSpeed = entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
        entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(movementSpeed *= speedBoost);

        double jumpStrength = entity.getEntityAttribute(GOTReflection.getHorseJumpStrength()).getAttributeValue();
        double jumpLimit = Math.max(jumpStrength, 1.0);
        jumpStrength += jumpAdd;
        jumpStrength = Math.min(jumpStrength, jumpLimit);
        entity.getEntityAttribute(GOTReflection.getHorseJumpStrength()).setBaseValue(jumpStrength);
    }
}
