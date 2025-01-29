package got.common.block.base;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BaseDecorBlock extends BlockDirectional implements ITileEntityProvider {

    private final float xSize, ySize, zSize; // Dimensions for the collision box

    private boolean disableCollision = false;

    private int yMove = 0;

    public BaseDecorBlock(float xSize, float ySize, float zSize) {
        super(Material.wood);
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
    }

    public BaseDecorBlock() {
        super(Material.wood);
        this.xSize = 1;
        this.ySize = 1;
        this.zSize = 1;
        this.disableCollision = true;
    }

    public BaseDecorBlock(int yMove) {
        super(Material.wood);
        this.xSize = 1;
        this.ySize = 1;
        this.zSize = 1;
        this.yMove = yMove;
        this.disableCollision = true;
    }

    public BaseDecorBlock(float xSize, float ySize, float zSize, int yMove) {
        super(Material.wood);
        this.xSize = xSize;
        this.ySize = ySize;
        this.zSize = zSize;
        this.yMove = yMove;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderType() {
        return -1; // Indicates a custom renderer is used
    }

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new DecorationTileEntity();
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z) {
        int direction = world.getBlockMetadata(x, y, z) & 3;
        float minX, minY, minZ, maxX, maxY, maxZ;

        minY = 0.0F;
        maxY = ySize;

        switch (direction) {
            case 0: // North
            case 2: // South
                minX = 0.5F - xSize / 2;
                maxX = 0.5F + xSize / 2;
                minZ = 0.5F - zSize / 2;
                maxZ = 0.5F + zSize / 2;
                break;
            case 1: // East
            case 3: // West
                minX = 0.5F - zSize / 2; // Swap xSize and zSize
                maxX = 0.5F + zSize / 2;
                minZ = 0.5F - xSize / 2;
                maxZ = 0.5F + xSize / 2;
                break;
            default:
                minX = minZ = 0.5F - xSize / 2; // Default case, unlikely to be used
                maxX = maxZ = 0.5F + xSize / 2;
                break;
        }

        this.setBlockBounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k) {
        if(disableCollision) {
            return null;
        }
        setBlockBoundsBasedOnState(world, i, j, k);
        return super.getCollisionBoundingBoxFromPool(world, i, j, k);
    }

    public boolean canCollideCheck(int p_149678_1_, boolean p_149678_2_) {
        if(disableCollision) {
            return true;
        }
        return super.canCollideCheck(p_149678_1_, p_149678_2_);
    }


    @SideOnly(value = Side.CLIENT)
    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int i, int j, int k) {
        setBlockBoundsBasedOnState(world, i, j, k);
        return super.getSelectedBoundingBoxFromPool(world, i, j, k);
    }

    @Override
    public boolean canPlaceBlockAt(World world, int x, int y, int z) {
        int newY = y + yMove;

        // Check if newY is within world bounds
        if (newY < 0 || newY >= world.getHeight()) {
            return false;
        }

        // Check if the position is free
        return world.isAirBlock(x, newY, z) && super.canPlaceBlockAt(world, x, y, z);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        int newY = y + yMove;

        // Ensure newY is within world bounds
        if (newY < 0 || newY >= world.getHeight()) {
            return; // Exit without placing the block if out of bounds
        }

        int l = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int metadata = 0;

        switch (l) {
            case 0:
                metadata = 5; // Facing North
                break;
            case 1:
                metadata = 3; // Facing East
                break;
            case 2:
                metadata = 4; // Facing South
                break;
            case 3:
                metadata = 2; // Facing West
                break;
        }

        // Remove the original block (where the player attempted to place it)
        world.setBlockToAir(x, y, z);

        // Place the block at the new adjusted position
        if (world.isAirBlock(x, newY, z)) {
            world.setBlock(x, newY, z, this, metadata, 3); // Set block with new metadata
            setBlockBoundsBasedOnState(world, x, newY, z);
        }
    }
}
