package noname.weapons.item;

import got.common.GOTBannerProtection;
import got.common.database.GOTCreativeTabs;
import noname.weapons.entity.EntityTribushet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemTribushetSpawner extends Item {

    public ItemTribushetSpawner() {
        this.setUnlocalizedName("tribushet_spawner");
        this.setTextureName("got:tribushet_spawner");
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
        EntityTribushet tribushet = new EntityTribushet(world);
        tribushet.setPosition(spawnX, spawnY, spawnZ);
        tribushet.rotationYaw = player.rotationYaw;
        tribushet.prevRotationYaw = player.rotationYaw;
        world.spawnEntityInWorld(tribushet);
        if (!player.capabilities.isCreativeMode) stack.stackSize--;
        return true;
    }
}
