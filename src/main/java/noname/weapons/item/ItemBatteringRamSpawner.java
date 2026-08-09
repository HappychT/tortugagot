package noname.weapons.item;

import got.common.GOTBannerProtection;
import got.common.database.GOTCreativeTabs;
import noname.weapons.entity.EntityBatteringRam;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBatteringRamSpawner extends Item {

    public ItemBatteringRamSpawner() {
        this.setUnlocalizedName("battering_ram_spawner");
        this.setTextureName("got:battering_ram_spawner");
        this.setCreativeTab(GOTCreativeTabs.tabsWaepons);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (world.isRemote) return true;
        if (GOTBannerProtection.isProtected(world, x, y + 1, z, GOTBannerProtection.forPlayer(player, GOTBannerProtection.Permission.FULL), true))
            return true;
        double spawnX = x + 0.5D;
        double spawnY = y + 1.0D;
        double spawnZ = z + 0.5D;
        EntityBatteringRam ram = new EntityBatteringRam(world);
        ram.setPosition(spawnX, spawnY, spawnZ);
        world.spawnEntityInWorld(ram);
        if (!player.capabilities.isCreativeMode) stack.stackSize--;
        return true;
    }
}

