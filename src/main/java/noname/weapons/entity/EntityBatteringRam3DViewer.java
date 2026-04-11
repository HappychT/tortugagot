package noname.weapons.entity;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;

public class EntityBatteringRam3DViewer {

	public static String[] ENTITYRENDERER_THIRDPERSONDISTANCE = { "thirdPersonDistance", "field_78490_B" };
	public Minecraft mc = Minecraft.getMinecraft();
	public float defaultThirdPersonDistance;
	public boolean ridingRam;
	public boolean ridingRamPrev;

	public EntityBatteringRam3DViewer() {
		defaultThirdPersonDistance = getThirdPersonDistance();
	}

	public float getThirdPersonDistance() {
		return ReflectionHelper.getPrivateValue(EntityRenderer.class, mc.entityRenderer, ENTITYRENDERER_THIRDPERSONDISTANCE);
	}

	@SubscribeEvent
	public void onTick(ClientTickEvent evt) {
		if (evt.phase != TickEvent.Phase.START || mc.thePlayer == null) {
			return;
		}
		ridingRam = mc.thePlayer.ridingEntity instanceof EntityBatteringRam;

		if (ridingRam && !ridingRamPrev) {
			setThirdPersonDistance(8);
		} else if (!ridingRam && ridingRamPrev) {
			setThirdPersonDistance(defaultThirdPersonDistance);
		}

		ridingRamPrev = ridingRam;
	}

	public void setThirdPersonDistance(float thirdPersonDistance) {
		ReflectionHelper.setPrivateValue(EntityRenderer.class, mc.entityRenderer, thirdPersonDistance, ENTITYRENDERER_THIRDPERSONDISTANCE);
	}
}
