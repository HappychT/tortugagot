package noname.weapons.item;

import got.common.GOTBannerProtection;
import got.common.database.GOTCreativeTabs;
import noname.weapons.entity.EntityCatapult;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCatapultSpawner extends Item {

    public ItemCatapultSpawner() {
        this.setUnlocalizedName("catapult_spawner");
        this.setTextureName("got:catapult_spawner");
        this.setCreativeTab(GOTCreativeTabs.tabsWaepons);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        if (GOTBannerProtection.isProtected(world, x, y + 1, z, GOTBannerProtection.forPlayer(player, GOTBannerProtection.Permission.FULL), true))
            return true;
        double spawnX = x + 0.5;
        double spawnY = y + 1;
        double spawnZ = z + 0.5;
        EntityCatapult catapult = new EntityCatapult(world);
        catapult.setPosition(spawnX, spawnY, spawnZ);
        catapult.rotationYaw = player.rotationYaw;
        catapult.prevRotationYaw = player.rotationYaw;
        world.spawnEntityInWorld(catapult);
        if (!player.capabilities.isCreativeMode) stack.stackSize--;
        return true;
    }
}

