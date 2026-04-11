package noname.weapons.events;

import noname.weapons.entity.EntityStoneProjectile;
import noname.weapons.world.BlockDamageStorage;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Map;

public class BlockDamageTickHandler {

    private int tickCounter = 0;
    private static final int UPDATE_INTERVAL = 40;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        tickCounter++;

        if (tickCounter >= UPDATE_INTERVAL) {
            tickCounter = 0;
            updateAllBlockCracks();
        }
    }

    private void updateAllBlockCracks() {
        for (WorldServer world : MinecraftServer.getServer().worldServers) {
            if (world == null) continue;

            Map<String, Float> damagedBlocks = BlockDamageStorage.getAllDamagedBlocks(world);

            for (Map.Entry<String, Float> entry : damagedBlocks.entrySet()) {
                String coordKey = entry.getKey();
                float damage = entry.getValue();

                String[] parts = coordKey.split(",");
                if (parts.length != 3) continue;

                try {
                    int x = Integer.parseInt(parts[0]);
                    int y = Integer.parseInt(parts[1]);
                    int z = Integer.parseInt(parts[2]);

                    Block block = world.getBlock(x, y, z);

                    if (block == null || block.isAir(world, x, y, z)) {
                        BlockDamageStorage.removeBlockDamage(world, x, y, z);
                        continue;
                    }

                    if (EntityStoneProjectile.isProtectedSiegeBlock(world, x, y, z)) {
                        BlockDamageStorage.removeBlockDamage(world, x, y, z);
                        EntityStoneProjectile.clearCracksToBlock(world, x, y, z);
                        continue;
                    }

                    
                    
                    float storedDamage = BlockDamageStorage.getBlockDamage(world, x, y, z);

                    if (storedDamage == 0.0F) {
                        EntityStoneProjectile.clearCracksToBlock(world, x, y, z);
                        continue;
                    }

                    int metadata = world.getBlockMetadata(x, y, z);
                    int harvestLevel = 0;
                    try {
                        harvestLevel = block.getHarvestLevel(metadata);
                    } catch (Exception e) {
                        harvestLevel = 0;
                    }

                    float maxDamage = EntityStoneProjectile.getMaxDamageForBlockStatic(harvestLevel);
                    EntityStoneProjectile.applyCracksToBlock(world, x, y, z, storedDamage, maxDamage);

                    
                    world.markBlockForUpdate(x, y, z);

                } catch (NumberFormatException e) {
                    
                }
            }
        }
    }
}

