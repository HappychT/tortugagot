package got.rome;

import got.common.handlers.StaminaServerHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class ExtendedPlayer implements IExtendedEntityProperties {

    public static final String EXT_PROP_NAME = "ExtendedPlayer";

    private final EntityPlayer player;
    private int attackCooldown;
    private int stamina;
    private int standingStillCooldown; // New field for standing still cooldown
    private double previousPosX; // New field for previous X position
    private double previousPosZ; // New field for previous Z position
    private int bounceCooldown; // New field for bounce cooldown

    public ExtendedPlayer(EntityPlayer player) {
        this.player = player;
        this.attackCooldown = 0;
        this.stamina = 100;
        this.standingStillCooldown = 0; // Initialize the new field
        this.previousPosX = player.posX; // Initialize the previous X position
        this.previousPosZ = player.posZ; // Initialize the previous Z position
        this.bounceCooldown = 0; // Initialize bounce cooldown
    }

    public static void register(EntityPlayer player) {
        player.registerExtendedProperties(EXT_PROP_NAME, new ExtendedPlayer(player));
    }

    public static ExtendedPlayer get(EntityPlayer player) {
        return (ExtendedPlayer) player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = new NBTTagCompound();
        properties.setInteger("attackCooldown", this.attackCooldown);
        properties.setInteger("stamina", this.stamina);
        properties.setInteger("standingStillCooldown", this.standingStillCooldown); // Save the new field
        properties.setDouble("previousPosX", this.previousPosX); // Save the previous X position
        properties.setDouble("previousPosZ", this.previousPosZ); // Save the previous Z position
        properties.setInteger("bounceCooldown", this.bounceCooldown); // Save bounce cooldown
        compound.setTag(EXT_PROP_NAME, properties);
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound properties = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
        this.attackCooldown = properties.getInteger("attackCooldown");
        this.stamina = properties.getInteger("stamina");
        this.standingStillCooldown = properties.getInteger("standingStillCooldown"); // Load the new field
        this.previousPosX = properties.getDouble("previousPosX"); // Load the previous X position
        this.previousPosZ = properties.getDouble("previousPosZ"); // Load the previous Z position
        this.bounceCooldown = properties.getInteger("bounceCooldown"); // Load bounce cooldown
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public int getAttackCooldown() {
        return attackCooldown;
    }

    public void setAttackCooldown(int attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = Math.max(0, Math.min(stamina, StaminaServerHandler.INSTANCE.MAX_STAMINA));
    }


    public int getStandingStillCooldown() {
        return standingStillCooldown;
    }

    public void setStandingStillCooldown(int standingStillCooldown) {
        this.standingStillCooldown = standingStillCooldown;
    }

    public double getPreviousPosX() {
        return previousPosX;
    }

    public void setPreviousPosX(double previousPosX) {
        this.previousPosX = previousPosX;
    }

    public double getPreviousPosZ() {
        return previousPosZ;
    }

    public void setPreviousPosZ(double previousPosZ) {
        this.previousPosZ = previousPosZ;
    }

    public int getBounceCooldown() {
        return bounceCooldown;
    }

    public void setBounceCooldown(int bounceCooldown) {
        this.bounceCooldown = bounceCooldown;
    }
}