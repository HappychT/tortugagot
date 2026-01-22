package brain.alchemy;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class PacketCyclePotion implements IMessage {

    public PacketCyclePotion() {}

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler implements IMessageHandler<PacketCyclePotion, IMessage> {
        @Override
        public IMessage onMessage(PacketCyclePotion message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if (player != null) {
                ItemStack stack = player.getCurrentEquippedItem();
                if (stack != null && stack.getItem() instanceof ItemAlchemyBag) {
                    ((ItemAlchemyBag) stack.getItem()).cycleActivePotion(stack, player);
                }
            }
            return null;
        }
    }
}