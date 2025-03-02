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
    public static Block BarrelDecor1 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:BarrelDecor").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block BarrelDecor2 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:BarrelDecor2").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block BarrelDecor3 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:BarrelDecor3").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block leatherStand = new BaseDecorBlock(1F,1F,1F).setBlockName("got:leatherStand").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWWineBottle = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWWineBottle").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDeer = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWDeer").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWGrass = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWGrass").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDead1 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWDead1").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDead2 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWDead2").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDead3 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:Dead3").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDead4 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:Dead4").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDead5 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:Dead5").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDead6 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:Dead6").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDead7 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:Dead7").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDead8 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:Dead8").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWCow = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWCow").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWMixtures1 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWMixtures").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWMixtures2 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:MixturesDouble").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWMixtures3 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:MixturesTriple").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWMixtures4 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:MixturesQuad").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWLeatherLighter = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWLeatherLighter").setLightLevel(0.9375f).setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWLighter = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWLighter").setLightLevel(0.9375f).setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWDeerDeco = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWDeerDeco").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWSpearHead1 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWSpearHead1").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWSpearHead2 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWSpearHead2").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWHandWheel = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWHandWheel").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWChains1 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:WOWChains").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWChains2 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:ChainsDouble").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWChains3 = new BaseDecorBlock(1F,1F,1F).setBlockName("got:ChainsTriple").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWCandle1 = new BaseDecorBlock(0.2F,0.4F,0.2F).setBlockName("got:WOWCandle").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWCandle2 = new BaseDecorBlock(0.3F,0.4F,0.3F).setBlockName("got:CandleDouble").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWCandle3 = new BaseDecorBlock(0.4F,0.4F,0.4F).setBlockName("got:CandleTriple").setCreativeTab(GOTCreativeTabs.tabUtil);
    public static Block WOWCandle4 = new BaseDecorBlock(0.5F,0.4F,0.5F).setBlockName("got:CandleQuad").setCreativeTab(GOTCreativeTabs.tabUtil);



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
