package got.common.world;

import net.minecraft.world.storage.*;

public class GOTWorldInfo extends DerivedWorldInfo {
	public long gotTotalTime;
	public long gotWorldTime;
	public boolean raining;
	public boolean thundering;
	public int thunderTime;
	public int rainTime;

	public GOTWorldInfo(WorldInfo worldinfo) {
		super(worldinfo);
	}

	@Override
	public long getWorldTime() {
		return gotWorldTime;
	}

	@Override
	public long getWorldTotalTime() {
		return gotTotalTime;
	}

	@Override
	public int getRainTime() {
		return rainTime;
	}

	@Override
	public int getThunderTime() {
		return thunderTime;
	}

	@Override
	public boolean isRaining()
	{
		return raining;
	}

	@Override
	public boolean isThundering() {
		return thundering;
	}

	public void got_setTotalTime(long time) {
		gotTotalTime = time;
	}

	public void got_setWorldTime(long time) {
		gotWorldTime = time;
	}

	@Override
	public void incrementTotalWorldTime(long time) {
	}

	@Override
	public void setWorldTime(long time) {
	}

	@Override
	public void setRaining(boolean rain){
		raining = rain;
	}

	@Override
	public void setRainTime(int rainTime) {
		this.rainTime = rainTime;
	}
	@Override
	public void setThundering(boolean thunder) {
		thundering = thunder;
	}
	@Override
	public void setThunderTime(int thunderTime) {
		this.thunderTime = thunderTime;
	}
}
