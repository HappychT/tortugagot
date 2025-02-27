package got.common.block.other;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import got.GOT;
import got.common.database.GOTCreativeTabs;
import got.common.database.GOTRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class GOTBlockCampfire extends Block{
    public GOTBlockCampfire() {
        super(Material.wood);
        setCreativeTab(GOTCreativeTabs.tabUtil);
        setBlockBounds(0.0f, 0.0f, 0.0f, 1.0f, 1f, 1f);
        setHardness(0.0f);
        setResistance(5.0f);
        setStepSound(Block.soundTypeWood);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public IIcon getIcon(int i, int j) {
        return Blocks.planks.getIcon(i, 0);
    }

    @SideOnly(value = Side.CLIENT)
    @Override
    public void registerBlockIcons(IIconRegister iconregister) {
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean canBlockStay(World world, int i, int j, int k) {
        return world.getBlock(i, j - 1, k).isSideSolid(world, i, j - 1, k, ForgeDirection.UP);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int i, int j, int k) {
        return canBlockStay(world, i, j, k);
    }
}
