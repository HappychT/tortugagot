package got.common.block.other;

import java.util.*;
import org.apache.commons.lang3.tuple.Pair;
import got.GOT;
import got.common.GOTBannerProtection;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.world.*;
import net.minecraftforge.common.util.ForgeDirection;

public class GOTBlockWildFire extends BlockFire {

	public GOTBlockWildFire() {
		setLightLevel(1.0f);
		setTickRandomly(true);
	}

	@Override
	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		world.playAuxSFXAtEntity(player, 1009, x, y, z, 0);
		world.setBlockToAir(x, y, z);
	}

	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
		entity.attackEntityFrom(DamageSource.inFire, 2.0F);

		entity.setFire(5);
	}

	@Override
	public int tickRate(World world) {
		return 20;
	}


	@Override
	public void updateTick(World world, int x, int y, int z, Random random) {

		if (world.isAirBlock(x, y, z)) return;

		int meta = world.getBlockMetadata(x, y, z);

		if (meta >= 15) {
			world.setBlockToAir(x, y, z);
			return;
		}

		if (!GOT.doFireTick(world)) return;

		if (isBannered(world, x, y, z)) {
			world.setBlockToAir(x, y, z);
			return;
		}

		boolean canBurnStone = random.nextFloat() < 0.9f;
		HashMap<Block, Pair<Integer, Integer>> infos = new HashMap<>();

		if (canBurnStone) {
			for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				Block block = world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ);
				Material material = block.getMaterial();

				if ((material != Material.rock && material != Material.clay && !(block instanceof GOTBlockGate))
						|| block.getExplosionResistance(null) >= 100.0f) {
					continue;
				}

				int enco = getEncouragement(block);
				int flam = this.getFlammability(block);
				infos.put(block, Pair.of(enco, flam));
				Blocks.fire.setFireInfo(block, 30, 30);
			}
		}

		runBaseFireUpdate(world, x, y, z, random);

		if (!infos.isEmpty()) {
			for (Map.Entry<Block, Pair<Integer, Integer>> e : infos.entrySet()) {
				Blocks.fire.setFireInfo(
						e.getKey(),
						e.getValue().getLeft(),
						e.getValue().getRight()
				);
			}
		}
	}


	public void runBaseFireUpdate(World world, int x, int y, int z, Random random) {
		if (!canPlaceBlockAt(world, x, y, z)) {
			world.setBlockToAir(x, y, z);
		}

		boolean isRaining = world.isRaining();
		if (isRaining && (world.canLightningStrikeAt(x, y, z) || world.canLightningStrikeAt(x, y+1, z))) {
			world.setBlockToAir(x, y, z);
			return;
		}

		int meta = world.getBlockMetadata(x, y, z);

		if (meta < 15) {
			world.setBlockMetadataWithNotify(x, y, z, meta + random.nextInt(2) + 1, 4);
			world.scheduleBlockUpdate(x, y, z, this, tickRate(world) + random.nextInt(10));
		} else {
			world.setBlockToAir(x, y, z);
			return;
		}

		int extraChance = 0;
		if (world.isBlockHighHumidity(x, y, z)) {
			extraChance = -50;
		}

		int hChance = 250 + extraChance;
		int vChance = 200 + extraChance;

		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			tryCatchFire(world, x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ, dir.offsetY == 0 ? hChance : vChance, random, meta, dir);
		}
	}

	public boolean canCatchFireNotBannered(World world, int i, int j, int k, ForgeDirection face) {
		if (isBannered(world, i, j, k)) {
			return false;
		}
		return canCatchFire(world, i, j, k, face);
	}

	public boolean canNeighborBurn(World world, int i, int j, int k) {
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			if (canCatchFireNotBannered(world, i + dir.offsetX, j + dir.offsetY, k + dir.offsetZ, dir)) {
				return true;
			}
		}
		return false;
	}

	public int getChanceOfNeighborsEncouragingFire(World world, int i, int j, int k) {
		if (!world.isAirBlock(i, j, k)) {
			return 0;
		}
		int chance = 0;
		for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
			chance = getChanceToEncourageFire(world, i + dir.offsetX, j + dir.offsetY, k + dir.offsetZ, chance, dir);
		}
		return chance;
	}

	@Override
	public int getChanceToEncourageFire(IBlockAccess world, int i, int j, int k, int oldChance, ForgeDirection face) {
		int chance = super.getChanceToEncourageFire(world, i, j, k, oldChance, face);
		return (int) (chance * 1.25f);
	}

	public boolean isBannered(World world, int i, int j, int k) {
		return GOTBannerProtection.isProtected(world, i, j, k, GOTBannerProtection.anyBanner(), false);
	}

	@Override
	public boolean isBurning(IBlockAccess world, int i, int j, int k) {
		return true;
	}

	public void tryCatchFire(World world, int x, int y, int z, int chance, Random random, int meta, ForgeDirection face) {
		if (isBannered(world, x, y, z)) {
			return;
		}
		int flamm = world.getBlock(x, y, z).getFlammability(world, x, y, z, face);

		if (random.nextInt(chance) < flamm) {
			boolean isTNT = world.getBlock(x, y, z) == Blocks.tnt;

			if (random.nextInt(meta + 10) < 5 && !world.canLightningStrikeAt(x, y, z)) {
				int newMeta = meta + random.nextInt(5) / 4;
				if (newMeta > 15) newMeta = 15;
				world.setBlock(x, y, z, this, newMeta, 3);
			} else {
				world.setBlockToAir(x, y, z);
			}

			if (isTNT) {
				Blocks.tnt.onBlockDestroyedByPlayer(world, x, y, z, 1);
			}
		}
	}
}