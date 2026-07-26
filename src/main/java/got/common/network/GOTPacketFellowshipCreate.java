package got.common.network;

import com.google.common.base.Charsets;

import cpw.mods.fml.common.network.simpleimpl.*;
import got.common.*;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class GOTPacketFellowshipCreate implements IMessage {
	public String fellowshipName;

	public GOTPacketFellowshipCreate() {
	}

	public GOTPacketFellowshipCreate(String name) {
		fellowshipName = name;
	}

	@Override
	public void fromBytes(ByteBuf data) {
		byte nameLength = data.readByte();
		ByteBuf nameBytes = data.readBytes(nameLength);
		fellowshipName = nameBytes.toString(Charsets.UTF_8);
	}

	@Override
	public void toBytes(ByteBuf data) {
		byte[] nameBytes = fellowshipName.getBytes(Charsets.UTF_8);
		data.writeByte(nameBytes.length);
		data.writeBytes(nameBytes);
	}

	public static class Handler implements IMessageHandler<GOTPacketFellowshipCreate, IMessage> {
		@Override
		public IMessage onMessage(GOTPacketFellowshipCreate packet, MessageContext context) {
			EntityPlayerMP entityplayer = context.getServerHandler().playerEntity;
			GOTPlayerData playerData = GOTLevelData.getData(entityplayer);

			// Фикс туториала: если игрок на этапе создания братства (stage 4),
			// принудительно расформировываем его старые братства-владельца,
			// чтобы не упереться в лимит (например, если создал братство в очереди).
			got.rome.ExtendedPlayer ext = got.rome.ExtendedPlayer.get(entityplayer);
			if (ext != null && ext.isTutorialActive() && ext.getTutorialStage() == 4) {
				java.util.List<java.util.UUID> fsIDs = new java.util.ArrayList<>(playerData.getFellowshipIDs());
				for (java.util.UUID fsID : fsIDs) {
					got.common.fellowship.GOTFellowship fs = got.common.fellowship.GOTFellowshipData.getActiveFellowship(fsID);
					if (fs != null && !fs.isDisbanded() && fs.isOwner(entityplayer.getUniqueID())) {
						fs.setDisbandedAndRemoveAllMembers();
					}
				}
			}

			playerData.createFellowship(packet.fellowshipName, true);
			brain.tutorial.TutorialManager.getInstance().advanceStage4(entityplayer, 5);
			return null;
		}
	}


}
