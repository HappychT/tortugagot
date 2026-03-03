package got.common.handlers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

public class PlayerParryData implements IExtendedEntityProperties {

    public final static String EXT_PROP_NAME = "GOTPlayerParryData";

    private final EntityPlayer player;

    private boolean isTryingToBlock = false;
    private long blockStartTime = 0;
    /** World time when Syrio counter last succeeded (for +10% melee speed for 5 sec). */
    private long lastSyrioCounterTime = 0;

    public PlayerParryData(EntityPlayer player) {
        this.player = player;
    }

    public static final void register(EntityPlayer player) {
        player.registerExtendedProperties(PlayerParryData.EXT_PROP_NAME, new PlayerParryData(player));
    }

    public static final PlayerParryData get(EntityPlayer player) {
        return (PlayerParryData) player.getExtendedProperties(EXT_PROP_NAME);
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {
    }

    @Override
    public void init(Entity entity, World world) {
    }

    public void setBlockStartTime(long time) {
        this.blockStartTime = time;
    }

    public long getBlockStartTime() {
        return this.blockStartTime;
    }
    public void setTryingToBlock(boolean isTrying) {
        this.isTryingToBlock = isTrying;
    }
    public boolean isTryingToBlock() {
        return this.isTryingToBlock;
    }

    public long getLastSyrioCounterTime() {
        return lastSyrioCounterTime;
    }

    public void setLastSyrioCounterTime(long time) {
        this.lastSyrioCounterTime = time;
    }
}