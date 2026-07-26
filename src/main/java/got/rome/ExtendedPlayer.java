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
    private int secondBreathCooldown;
    private double previousPosX; // New field for previous X position
    private double previousPosZ; // New field for previous Z position
    private int bounceCooldown; // New field for bounce cooldown
    
    // Tutorial fields
    private boolean isTutorialActive;
    private int tutorialStage;
    private int tutorialProgress;
    private boolean tutorialReplay;

    public ExtendedPlayer(EntityPlayer player) {
        this.player = player;
        this.attackCooldown = 0;
        this.stamina = 9900;
        this.standingStillCooldown = 0; // Initialize the new field
        this.secondBreathCooldown = 0;
        this.previousPosX = player.posX; // Initialize the previous X position
        this.previousPosZ = player.posZ; // Initialize the previous Z position
        this.bounceCooldown = 0; // Initialize bounce cooldown
        this.isTutorialActive = false;
        this.tutorialStage = 0;
        this.tutorialProgress = 0;
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
        properties.setInteger("secondBreathCooldown", this.secondBreathCooldown);
        properties.setBoolean("isTutorialActive", this.isTutorialActive);
        properties.setInteger("tutorialStage", this.tutorialStage);
        properties.setInteger("tutorialProgress", this.tutorialProgress);
        properties.setBoolean("tutorialReplay", this.tutorialReplay);
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
        this.secondBreathCooldown = properties.getInteger("secondBreathCooldown");
        this.isTutorialActive = properties.getBoolean("isTutorialActive");
        this.tutorialStage = properties.getInteger("tutorialStage");
        this.tutorialProgress = properties.getInteger("tutorialProgress");
        this.tutorialReplay = properties.getBoolean("tutorialReplay");
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

    public int getSecondBreathCooldown() {
        return secondBreathCooldown;
    }

    public void setSecondBreathCooldown(int secondBreathCooldown) {
        this.secondBreathCooldown = secondBreathCooldown;
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

    public boolean isTutorialActive() {
        return isTutorialActive;
    }

    public void setTutorialActive(boolean tutorialActive) {
        this.isTutorialActive = tutorialActive;
    }

    public int getTutorialStage() {
        return tutorialStage;
    }

    public void setTutorialStage(int tutorialStage) {
        this.tutorialStage = tutorialStage;
    }

    public int getTutorialProgress() {
        return tutorialProgress;
    }

    public void setTutorialProgress(int tutorialProgress) {
        this.tutorialProgress = tutorialProgress;
    }

    public boolean isTutorialReplay() {
        return tutorialReplay;
    }

    public void setTutorialReplay(boolean tutorialReplay) {
        this.tutorialReplay = tutorialReplay;
    }
}
