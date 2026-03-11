package brain.factions.servers;

import brain.factions.Annot;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


 public class FreeTeleporter extends Teleporter {
	WorldServer world;
	double x, y, z;

	public FreeTeleporter(WorldServer worldIn, double x, double y, double z) {
		super(worldIn);
		world = worldIn;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean placeInExistingPortal(Entity entity, double x, double y, double z, float rotationYaw) {
		entity.posX = this.x;
		entity.posY = this.y;
		entity.posZ = this.z;
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (player.capabilities.allowFlying)
				player.capabilities.isFlying = true;
		}
		return true;
	}
	
	public static void sendToDimensionWithoutPortal(Entity entity, int dimTo, double x, double y, double z) {
		if (dimTo == entity.dimension)
			entity.setPosition(x, y, z);
		if (entity instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entity;
			WorldServer worldTo = player.mcServer.worldServerForDimension(dimTo);
			player.mcServer.getConfigurationManager().transferPlayerToDimension(player, dimTo, new FreeTeleporter(worldTo, x, y, z));
		}
	}
}