package got.common.item.weapon.cswords;

import com.google.common.collect.Multimap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.common.database.GOTCreativeTabs;
import got.common.item.weapon.GOTItemLegendaryHammer;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.UUID;

public class ItemBaratheonHammer extends GOTItemLegendaryHammer {
    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("d8a0a184-9b32-443a-9097-9e79786311a2");

    public ItemBaratheonHammer(ToolMaterial material) {
        super(material);
        setCreativeTab(GOTCreativeTabs.tabCombat);
    }

    @Override
    public Multimap getItemAttributeModifiers() {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();

        multimap.put(SharedMonsterAttributes.movementSpeed.getAttributeUnlocalizedName(),
                new AttributeModifier(SPEED_MODIFIER_UUID, "Warhammer slowness", -0.15D, 2));

        return multimap;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean advanced) {
        super.addInformation(stack, player, list, advanced);
        list.add("");
        list.add(EnumChatFormatting.RED + "Критическая Атака" + EnumChatFormatting.WHITE + " – с прыжка сносит 25 прочности нагруднику.");
        list.add(EnumChatFormatting.DARK_RED + "Ярость" + EnumChatFormatting.WHITE + " – при убийстве игрока накладывает Силу и Скорость на 5 секунд.");
        list.add("");
        list.add(EnumChatFormatting.GRAY + "Тот самый молот, которым Роберт разбил");
        list.add(EnumChatFormatting.GRAY + "грудь принцу Рейегару.");
    }

}