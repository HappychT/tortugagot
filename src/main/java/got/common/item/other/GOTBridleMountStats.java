package got.common.item.other;

import java.util.HashMap;
import java.util.Map;

import got.common.database.GOTRegistry;
import got.common.entity.animal.GOTEntityBoar;
import got.common.entity.animal.GOTEntityBridleDornishSteed;
import got.common.entity.animal.GOTEntityBridleDothrakiStallion;
import got.common.entity.animal.GOTEntityBridleLordHorse;
import got.common.entity.animal.GOTEntityBridlePlotva;
import got.common.entity.animal.GOTEntityBridleSerogriv;
import got.common.entity.animal.GOTEntityCamel;
import got.common.entity.animal.GOTEntityDirewolf;
import got.common.entity.animal.GOTEntityElephant;
import got.common.entity.animal.GOTEntityHorse;
import got.common.entity.animal.GOTEntityMammoth;
import got.common.entity.animal.GOTEntityWoolyRhino;
import got.common.entity.animal.GOTEntityZebra;
import got.common.util.GOTReflection;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.item.ItemStack;

public final class GOTBridleMountStats {

    public final double hp;
    public final double speed;
    public final double jump;
    public final double attack;
    public final ItemStack armorItem;

    public GOTBridleMountStats(double hp, double speed, double jump, double attack, ItemStack armorItem) {
        this.hp = hp;
        this.speed = speed;
        this.jump = jump;
        this.attack = attack;
        this.armorItem = armorItem;
    }

    public GOTBridleMountStats(double hp, double speed, double jump, double attack) {
        this(hp, speed, jump, attack, null);
    }

    private static final Map<Class<?>, GOTBridleMountStats> REGISTRY = new HashMap<>();
    private static final GOTBridleMountStats DEFAULT = new GOTBridleMountStats(20, 0.25, 0.7, 0);

    static {
        REGISTRY.put(GOTEntityHorse.class, new GOTBridleMountStats(20, 0.25, 0.7, 0));
        REGISTRY.put(GOTEntityBridlePlotva.class, new GOTBridleMountStats(20, 0.25, 0.7, 0));
        REGISTRY.put(GOTEntityBridleDornishSteed.class, new GOTBridleMountStats(16, 0.30, 0.8, 0));
        REGISTRY.put(GOTEntityBridleDothrakiStallion.class, new GOTBridleMountStats(22, 0.34, 0.8, 0));
        REGISTRY.put(GOTEntityBridleSerogriv.class, new GOTBridleMountStats(35, 0.30, 1.0, 0, new ItemStack(GOTRegistry.dothrakiHorseArmor)));
        REGISTRY.put(GOTEntityBridleLordHorse.class, new GOTBridleMountStats(28, 0.18, 0.6, 0, new ItemStack(GOTRegistry.westerosHorseArmor)));
        REGISTRY.put(GOTEntityZebra.class, new GOTBridleMountStats(16, 0.30, 0.8, 0));
        REGISTRY.put(GOTEntityCamel.class, new GOTBridleMountStats(22, 0.34, 0.8, 0));
        REGISTRY.put(GOTEntityBoar.class, new GOTBridleMountStats(30, 0.27, 0.5, 5));
        REGISTRY.put(GOTEntityMammoth.class, new GOTBridleMountStats(35, 0.30, 1.0, 0, new ItemStack(GOTRegistry.rhinoArmor)));
        REGISTRY.put(GOTEntityWoolyRhino.class, new GOTBridleMountStats(40, 0.22, 0.6, 10, new ItemStack(GOTRegistry.rhinoArmor)));
        REGISTRY.put(GOTEntityElephant.class, new GOTBridleMountStats(80, 0.15, 0.4, 15));
        REGISTRY.put(GOTEntityDirewolf.class, new GOTBridleMountStats(25, 0.31, 0.9, 8));
    }

    public static GOTBridleMountStats getStats(Class<?> entityClass) {
        GOTBridleMountStats s = REGISTRY.get(entityClass);
        return s != null ? s : DEFAULT;
    }

    public void applyTo(EntityLiving mount) {
        mount.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(hp);
        mount.setHealth((float) hp);
        mount.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(speed);
        IAttributeInstance jumpAttr = mount.getEntityAttribute(GOTReflection.getHorseJumpStrength());
        if (jumpAttr != null) {
            jumpAttr.setBaseValue(jump);
        }
        IAttributeInstance attackAttr = mount.getEntityAttribute(SharedMonsterAttributes.attackDamage);
        if (attackAttr != null) {
            attackAttr.setBaseValue(attack);
        }
        if (mount instanceof GOTEntityHorse) {
            GOTEntityHorse horse = (GOTEntityHorse) mount;
            if (armorItem != null && horse.isMountArmorValid(armorItem)) {
                horse.setMountArmor(armorItem.copy());
            }
        }
    }
}
