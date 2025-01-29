package got.common.potions;

import got.common.network.GOTPacketHandler;
import got.common.network.LOTRPacketParticleFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BleedingPotion extends CustomPotion {

    private Map<EntityLivingBase, Integer> movementCooldowns = new HashMap<>();

    private static final int COOLDOWN_TICKS = 10;

    private static final float DAMAGE_AMOUNT = 1.0F;
    private static final int TICK_HISTORY_SIZE = 40; // Store the last 40 ticks of motion data

    // Map to store motionX history for each entity
    private Map<EntityLivingBase, List<Double>> motionXHistory = new HashMap<>();


    public BleedingPotion(int id, boolean isBad, int fluidColor, ResourceLocation tex, String namePot) {
        super(id, isBad, fluidColor, tex, namePot);
    }

    public boolean isJumping(EntityLivingBase entity) {
        return entity.motionY > 0;
    }

    private Map<EntityLivingBase, Double[]> lastPositions = new HashMap<>();


    public boolean isMoving(EntityLivingBase entity) {
        // Get or initialize the motionX history list for the entity
        List<Double> history = motionXHistory.computeIfAbsent(entity, k -> new ArrayList<>());

        // Add the current motionX to the history list
        history.add(entity.motionX);

        // Trim the history list to the last 40 ticks
        if (history.size() > TICK_HISTORY_SIZE) {
            history.remove(0);
        }

        // Check the last 20-40 ticks for non-zero motionX
        int startCheckIndex = Math.max(0, history.size() - 40);  // Start checking at most 40 ticks ago
        int endCheckIndex = Math.max(0, history.size() - 20);    // Check up to the last 20 ticks

        for (int i = startCheckIndex; i < endCheckIndex; i++) {
            if (history.get(i) != 0.0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        if (isMoving(entity) || isJumping(entity)) {
        	//if(entity.getRNG().nextFloat() < 0.1f) {
        		entity.attackEntityFrom(DamageSource.generic, DAMAGE_AMOUNT * (amplifier + 1));
        	//}

            if (!entity.worldObj.isRemote) {
                double offset = 0.2D;
                double randOffsetX = entity.worldObj.rand.nextDouble() * offset - offset / 2.0D;
                double randOffsetY = entity.worldObj.rand.nextDouble() * offset - offset / 2.0D;
                double randOffsetZ = entity.worldObj.rand.nextDouble() * offset - offset / 2.0D;

                double particleX = entity.posX + randOffsetX;
                double centerY = (entity.posY + entity.height / 2.0D) + randOffsetY;
                double particleZ = entity.posZ + randOffsetZ;

                String particle = "reddust";

                GOTPacketHandler.networkWrapper.sendToAllAround(new LOTRPacketParticleFX(particle, 10, particleX, centerY, particleZ, 0.0D, 0.1D, 0.0D), GOTPacketHandler.nearEntity(entity, 64.0));
            }
        }
    }

    @Override
    public int getStatusIconIndex() {
        return 0;
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

}
