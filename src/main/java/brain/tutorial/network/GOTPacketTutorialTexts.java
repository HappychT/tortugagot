package brain.tutorial.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import got.common.network.base.AbstractPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import brain.tutorial.client.TutorialTextsClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GOTPacketTutorialTexts extends AbstractPacket.AbstractClientMessage<GOTPacketTutorialTexts> {

    private Map<String, String> texts;

    public GOTPacketTutorialTexts() {
        this.texts = new HashMap<>();
    }

    public GOTPacketTutorialTexts(Map<String, String> texts) {
        this.texts = texts != null ? new HashMap<>(texts) : new HashMap<>();
    }

    @Override
    protected void read(PacketBuffer buffer) throws IOException {
        int size = buffer.readInt();
        for (int i = 0; i < size; i++) {
            String key = ByteBufUtils.readUTF8String(buffer);
            String value = ByteBufUtils.readUTF8String(buffer);
            texts.put(key, value);
        }
    }

    @Override
    protected void write(PacketBuffer buffer) throws IOException {
        buffer.writeInt(texts.size());
        for (Map.Entry<String, String> entry : texts.entrySet()) {
            ByteBufUtils.writeUTF8String(buffer, entry.getKey());
            ByteBufUtils.writeUTF8String(buffer, entry.getValue());
        }
    }

    @Override
    public void process(EntityPlayer player, Side side) {
        if (side == Side.CLIENT) {
            TutorialTextsClient.setTexts(texts);
        }
    }
}
