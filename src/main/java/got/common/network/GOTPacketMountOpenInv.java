package got.common.network;

import got.GOT;
import got.common.entity.animal.GOTEntityDirewolf;
import cpw.mods.fml.common.network.simpleimpl.*;
import got.common.entity.other.GOTEntityNPCRideable;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class GOTPacketMountOpenInv implements IMessage {
	@Override
	public void fromBytes(ByteBuf data) {
	}

	@Override
	public void toBytes(ByteBuf data) {
	}

	public static class Handler implements IMessageHandler<GOTPacketMountOpenInv, IMessage> {
		@Override
		public IMessage onMessage(GOTPacketMountOpenInv packet, MessageContext context) {
			EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
			if (entityplayer.ridingEntity instanceof GOTEntityNPCRideable) {
				((GOTEntityNPCRideable) entityplayer.ridingEntity).openGUI(entityplayer);
			} else if (entityplayer.ridingEntity instanceof GOTEntityDirewolf) {
				entityplayer.openGui(GOT.instance, 29, entityplayer.worldObj, entityplayer.ridingEntity.getEntityId(), 0, 0);
			}
			return null;
		}
	}

}