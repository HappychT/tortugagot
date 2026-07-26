package got.common.entity;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.registry.GameRegistry;
import got.common.entity.other.GOTBlacksmithOffer;
import got.common.faction.GOTFaction;
import got.common.network.GOTPacketHandler;
import got.common.network.serverToClient.GOTPacketBlacksmithInfo;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Arrays;

public class GOTEnchaldBlacksmith extends EntityCreature {
    GOTEnchaldBlacksmith.BlacksmithType type;
    private int[] slots;
    private float coinMultiplier = 0.0f;
    private float blocksCounter = 0.0f;

    public GOTEnchaldBlacksmith(World world) {
        super(world);
        this.type = this.getBlacksmithType();
        slots = new int[type.getSlots()];
        Arrays.fill(slots, -1);
        this.setSize(0.6F, 1.8F);
        this.isImmuneToFire = true;
        this.experienceValue = 0;
        this.preventEntitySpawning = true;
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(Double.MAX_VALUE);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.0D); 
        this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(0.0D);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.rotationYaw = this.prevRotationYaw;
        this.rotationPitch = this.prevRotationPitch;
    }

    @Override
    public void onLivingUpdate() {
        this.updateArmSwingProgress();

        if (!this.onGround && this.motionY < 0.0D)
            this.motionY = 0.0D;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    protected boolean isMovementBlocked() {
        return true;
    }

    @Override
    public boolean isEntityInvulnerable() {
        return true;
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
    }

    @Override
    public boolean canDespawn() {
        return false;
    }

    @Override
    public boolean isAIEnabled() {
        return false;
    }

    @Override
    protected void updateAITasks() {
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public void addVelocity(double x, double y, double z) {
    }

    public GOTFaction getFaction() {
        return null;
    }
//    @Override
//    public void knockBack(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
//    }

    public enum BlacksmithType {
        MW(10), RW(7), ARM(23), TOOL(10);
        private int slots;

        BlacksmithType(int slots) {
            this.slots = slots;
        }

        public int getSlots() {
            return slots;
        }
    }

    public void costsIncrease() {
        coinMultiplier += 0.35f;
        blocksCounter += 5.0f;
    }

    public void sendClientPacket(EntityPlayer entityplayer) {
        NBTTagCompound nbt = new NBTTagCompound();
        writeEntityToNBT(nbt);
        GOTPacketHandler.networkWrapper.sendTo((IMessage) new GOTPacketBlacksmithInfo(nbt), (EntityPlayerMP) entityplayer);
    }

    public void receiveClientPacket(GOTPacketBlacksmithInfo packet) {
        NBTTagCompound nbt = packet.blacksmithData;
        readEntityFromNBT(nbt);
    }

    public void applyUnlockSlot(EntityPlayer entityplayer, int slot) {
        costsIncrease();
        setSlotUnlocked(slot);
        sendClientPacket(entityplayer);
    }

    public void setSlotUnlocked(int slot) {
        slots[slot] = 0;
    }

    public boolean isSlotUnlocked(int slot) {
        return slots[slot] == 0;
    }

    public float getCoinMultiplier() {
        return coinMultiplier;
    }

    public float getBlocksCounter() {
        return blocksCounter;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setFloat("CoinM", coinMultiplier);
        nbt.setFloat("BlocksC", blocksCounter);
        nbt.setIntArray("Slots", slots);

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        coinMultiplier = nbt.getFloat("CoinM");
        blocksCounter = nbt.getFloat("BlocksC");
        slots = nbt.getIntArray("Slots");
    }

    public BlacksmithType getBlacksmithType() {
        return null;
    }
}
