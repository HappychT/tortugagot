package got.common.itemreg;

import cpw.mods.fml.common.registry.GameRegistry;
import got.common.block.base.BaseDecorBlock;
import got.common.database.GOTCreativeTabs;
import got.common.decorations.DecorationsRegister;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

import java.lang.reflect.Field;
import java.util.Objects;

public class GOTItems {
    public static Block WOWBarricadeBlock = new BaseDecorBlock(1, 2, 1).setBlockName("got:WOWBarricadeBlock").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWHangingCage = new BaseDecorBlock(1, 2, 1, -1).setBlockName("got:WOWHangingCage").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWBigCactus = new BaseDecorBlock(1F, 1.5F, 1F).setBlockName("got:WOWBigCactus").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWHangingPothos = new BaseDecorBlock(1F, 1F, 1F).setBlockName("got:WOWHangingPothos").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWVinesFlowering = new BaseDecorBlock(-1).setBlockName("got:WOWVinesFlowering").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWSmallFir = new BaseDecorBlock(0.4F, 0.4F, 0.4F).setBlockName("got:WOWSmallFir").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDungeonShackles = new BaseDecorBlock().setBlockName("got:WOWDungeonShackles").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDungeonPillory = new BaseDecorBlock(0.3F, 1F, 0.3F).setBlockName("got:WOWDungeonPillory").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWGoblintekcoil = new BaseDecorBlock(0.5F, 2F, 0.5F).setBlockName("got:WOWGoblinTekCoil").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWSkibidiToilet = new BaseDecorBlock(1F, 1F, 1F).setBlockName("got:WOWSkibidiToilet").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block BarrelDecor = new BaseDecorBlock(1F, 1F, 1F).setBlockName("got:BarrelDecor").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block leatherStand = new BaseDecorBlock(1F, 1F, 1F).setBlockName("got:leatherStand").setCreativeTab(GOTCreativeTabs.tabUtil);


    public static void registerItem(Item item) {
        String prefixUnlocal = "item.got:";
        String textureName = item.getUnlocalizedName().substring(prefixUnlocal.length());
        item.setTextureName("got:" + textureName);
        GameRegistry.registerItem(item, "item." + textureName);
    }


    public static void registerBlock(Block block) {
        String prefixUnlocal = "tile:got.";
        String textureName = block.getUnlocalizedName().substring(prefixUnlocal.length());
        block.setBlockTextureName("got:" + textureName);
        GameRegistry.registerBlock(block, "tile." + textureName);
    }

    public static void registerBlock(Block block, boolean tempTex) {
        String prefixUnlocal = "tile:got.";
        String textureName = "";
        String blockName = "";
        if(tempTex) {
            textureName = String.valueOf(Objects.requireNonNull(DecorationsRegister.findDecorationByBlock(block)).getTex());
            textureName = (textureName.substring(0, textureName.lastIndexOf('.')));

            textureName = textureName.substring("got:textures/blocks/".length());
            textureName = "got:" + textureName;

            block.setBlockTextureName(textureName);
        } else {
            textureName = block.getUnlocalizedName().substring(prefixUnlocal.length());
            block.setBlockTextureName("got:" + textureName);
        }


        blockName = block.getUnlocalizedName().substring(prefixUnlocal.length());
        GameRegistry.registerBlock(block, "tile." + blockName);
    }

    public static void autoRegisterItems() {

        for (Field field : GOTItems.class.getDeclaredFields()) {
            try {
                Object fieldValue = field.get(GOTItems.class);

                if (fieldValue instanceof Item) {
                    registerItem((Item) fieldValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        for (Field field : GOTItems.class.getDeclaredFields()) {
            try {
                Object fieldValue = field.get(GOTItems.class);

                if (fieldValue instanceof BaseDecorBlock) {
                    registerBlock((BaseDecorBlock) fieldValue, true);
                }

                if (fieldValue instanceof Block && !(fieldValue instanceof BaseDecorBlock) ) {
                    registerBlock((Block) fieldValue);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

}
