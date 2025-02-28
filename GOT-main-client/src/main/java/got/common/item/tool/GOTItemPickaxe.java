package got.common.item.tool;

import got.common.database.GOTCreativeTabs;
import got.common.enchant.GOTEnchantment;
import got.common.enchant.GOTEnchantmentHelper;
import got.common.item.GOTMaterialFinder;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;

public class GOTItemPickaxe extends ItemPickaxe implements GOTMaterialFinder {
	public ToolMaterial gotMaterial;

	public GOTItemPickaxe(ToolMaterial material) {
		super(material);
		setCreativeTab(GOTCreativeTabs.tabTools);
		gotMaterial = material;
	}
	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, Block block, int x, int y, int z, EntityLivingBase entityliving) {

		if (GOTEnchantmentHelper.hasEnchant(itemstack, GOTEnchantment.extraMining)) {
			if (!(entityliving instanceof EntityPlayer) || world.isRemote) {
				return super.onBlockDestroyed(itemstack, world, block, x, y, z, entityliving);
			}

			EntityPlayer entityplayer = (EntityPlayer) entityliving;
			Vec3 vec3 = Vec3.createVectorHelper(entityplayer.posX, entityplayer.posY + entityplayer.getEyeHeight(), entityplayer.posZ);
			Vec3 vec31 = entityplayer.getLook(1f);
			double distance = 5d;
			Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);

			if (entityplayer.worldObj.rayTraceBlocks(vec3, vec32, true) != null) {

				int side;
				side = entityplayer.worldObj.rayTraceBlocks(vec3, vec32, true).sideHit;

				int i = side == 4 || side == 5 ? 0 : 1;
				int j = side == 0 || side == 1 ? 0 : 1;
				int k = side == 2 || side == 3 ? 0 : 1;


				for (int i1 = x - i; i1 <= x + i; i1++) {
					for (int j1 = y - j; j1 <= y + j; j1++) {
						for (int k1 = z - k; k1 <= z + k; k1++) {
							if (world.isAirBlock(i1, j1, k1))
								continue;

							Block block1 = world.getBlock(i1, j1, k1);
							int meta = world.getBlockMetadata(i1, j1, k1);

							if (gotMaterial.getHarvestLevel() >= block1.getHarvestLevel(meta)) {

								if (block1.getBlockHardness(world, i1, j1, k1) == -1f)
									continue;

								if(block1.getMaterial() != Material.rock)
									continue;

								BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(i1, j1, k1, world, block1, meta, entityplayer);
								MinecraftForge.EVENT_BUS.post(event);
								if (event.isCanceled())
									continue;

								block1.harvestBlock(world, entityplayer, i1, j1, k1, meta);
								world.setBlockToAir(i1, j1, k1);
							}
						}
					}
				}
			}
		}
		return super.onBlockDestroyed(itemstack, world, block, x, y, z, entityliving);
	}

	@Override
	public ToolMaterial getMaterial() {
		return gotMaterial;
	}
}
