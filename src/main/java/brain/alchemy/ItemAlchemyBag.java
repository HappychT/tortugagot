package brain.alchemy;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class ItemAlchemyBag extends Item {

    private static final int MAX_POTIONS = 9;

    public ItemAlchemyBag() {
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            if (player.isSneaking()) {

                player.openGui(
                    AlchemyBagMod.instance,
                    GuiHandler.GUI_ALCHEMY_BAG,
                    world,
                    (int) player.posX,
                    (int) player.posY,
                    (int) player.posZ);
                return stack;
            }

            NBTTagCompound nbt = getOrCreateNBT(stack);
            int activeSlot = nbt.getInteger("ActiveSlot");
            NBTTagList potions = nbt.getTagList("Potions", 10);

            if (potions.tagCount() == 0) {
                player.addChatMessage(new ChatComponentText("Мешок пуст."));
                return stack;
            }

            NBTTagCompound activePotion = null;
            int activePotionIndex = -1;

            for (int i = 0; i < potions.tagCount(); i++) {
                NBTTagCompound potionTag = potions.getCompoundTagAt(i);
                if (potionTag.getInteger("Slot") == activeSlot) {
                    activePotion = potionTag;
                    activePotionIndex = i;
                    break;
                }
            }

            if (activePotion == null) {
                player.addChatMessage(new ChatComponentText("Мешок пуст."));
                return stack;
            }

            ItemStack potionStack = ItemStack.loadItemStackFromNBT(activePotion);
            int count = activePotion.getInteger("Count");

            EntityPotion entityPotion = new EntityPotion(world, player, potionStack);
            entityPotion.rotationPitch -= 20.0F;
            world.spawnEntityInWorld(entityPotion);
            world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

            count--;
            if (count <= 0) {
                potions.removeTag(activePotionIndex);

                if (potions.tagCount() > 0) {
                    activeSlot = potions.getCompoundTagAt(0)
                        .getInteger("Slot");
                    nbt.setInteger("ActiveSlot", activeSlot);
                }
            } else {
                activePotion.setInteger("Count", count);
            }

            nbt.setTag("Potions", potions);
            stack.setTagCompound(nbt);
        }

        return stack;
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        return false;
    }

    public void cycleActivePotion(ItemStack stack, EntityPlayer player) {
        NBTTagCompound nbt = getOrCreateNBT(stack);
        NBTTagList potions = nbt.getTagList("Potions", 10);

        if (potions.tagCount() == 0) {
            if (!player.worldObj.isRemote) {
                player.addChatMessage(new ChatComponentText("§cМешок пуст!"));
            }
            return;
        }

        int activeSlot = nbt.getInteger("ActiveSlot");
        int currentIndex = -1;

        for (int i = 0; i < potions.tagCount(); i++) {
            if (potions.getCompoundTagAt(i)
                .getInteger("Slot") == activeSlot) {
                currentIndex = i;
                break;
            }
        }

        int nextIndex = (currentIndex + 1) % potions.tagCount();
        int nextSlot = potions.getCompoundTagAt(nextIndex)
            .getInteger("Slot");

        nbt.setInteger("ActiveSlot", nextSlot);
        stack.setTagCompound(nbt);

        if (!player.worldObj.isRemote) {
            NBTTagCompound potionTag = potions.getCompoundTagAt(nextIndex);
            ItemStack potionStack = ItemStack.loadItemStackFromNBT(potionTag);
            int count = potionTag.getInteger("Count");

            if (potionStack != null) {
                player.addChatMessage(new ChatComponentText("§a► §f" + potionStack.getDisplayName() + " §7x" + count));
            }
        }

        player.worldObj.playSoundAtEntity(player, "random.click", 0.3F, 1.5F);
    }

    public boolean addPotion(ItemStack bag, ItemStack potion, EntityPlayer player) {
        return addPotionInternal(bag, potion, player, true);
    }

    public boolean addPotionSilent(ItemStack bag, ItemStack potion, EntityPlayer player) {
        return addPotionInternal(bag, potion, player, false);
    }

    private boolean addPotionInternal(ItemStack bag, ItemStack potion, EntityPlayer player, boolean showMessages) {
        if (potion == null || potion.getItem() != Items.potionitem) {
            if (showMessages) {
                player.addChatMessage(new ChatComponentText("Этот предмет нельзя положить в мешок."));
            }
            return false;
        }

        if (!ItemPotion.isSplash(potion.getItemDamage())) {
            if (showMessages) {
                player.addChatMessage(new ChatComponentText("Этот предмет нельзя положить в мешок."));
            }
            return false;
        }

        NBTTagCompound nbt = getOrCreateNBT(bag);
        NBTTagList potions = nbt.getTagList("Potions", 10);

        if (potions.tagCount() >= MAX_POTIONS) {

            boolean added = false;
            for (int i = 0; i < potions.tagCount(); i++) {
                NBTTagCompound existing = potions.getCompoundTagAt(i);
                ItemStack existingStack = ItemStack.loadItemStackFromNBT(existing);

                if (existingStack != null && existingStack.getItem() == potion.getItem()
                    && existingStack.getItemDamage() == potion.getItemDamage()
                    && ItemStack.areItemStackTagsEqual(existingStack, potion)) {

                    int currentCount = existing.getInteger("Count");
                    existing.setInteger("Count", currentCount + potion.stackSize);
                    added = true;
                    break;
                }
            }

            if (!added) {
                if (showMessages) {
                    player.addChatMessage(new ChatComponentText("Мешок заполнен."));
                }
                return false;
            }
        } else {

            boolean found = false;
            for (int i = 0; i < potions.tagCount(); i++) {
                NBTTagCompound existing = potions.getCompoundTagAt(i);
                ItemStack existingStack = ItemStack.loadItemStackFromNBT(existing);

                if (existingStack != null && existingStack.getItem() == potion.getItem()
                    && existingStack.getItemDamage() == potion.getItemDamage()
                    && ItemStack.areItemStackTagsEqual(existingStack, potion)) {

                    int currentCount = existing.getInteger("Count");
                    existing.setInteger("Count", currentCount + potion.stackSize);
                    found = true;
                    break;
                }
            }

            if (!found) {

                NBTTagCompound potionTag = new NBTTagCompound();
                potion.writeToNBT(potionTag);
                potionTag.setInteger("Count", potion.stackSize);
                potionTag.setInteger("Slot", potions.tagCount());
                potions.appendTag(potionTag);

                if (potions.tagCount() == 1) {
                    nbt.setInteger("ActiveSlot", 0);
                }
            }
        }

        nbt.setTag("Potions", potions);
        bag.setTagCompound(nbt);
        return true;
    }

    private NBTTagCompound getOrCreateNBT(ItemStack stack) {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        NBTTagCompound nbt = stack.getTagCompound();

        if (!nbt.hasKey("Potions")) {
            nbt.setTag("Potions", new NBTTagList());
            nbt.setInteger("ActiveSlot", 0);
        }

        return nbt;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, java.util.List list, boolean advanced) {
        NBTTagCompound nbt = getOrCreateNBT(stack);
        NBTTagList potions = nbt.getTagList("Potions", 10);
        int activeSlot = nbt.getInteger("ActiveSlot");

        if (potions.tagCount() == 0) {
            list.add("§7Пусто (0/" + MAX_POTIONS + ")");
            list.add("§8Подбирайте взрывные зелья!");
        } else {
            list.add("§7Зелий: §f" + potions.tagCount() + "§7/" + MAX_POTIONS);
            list.add("§8ЛКМ - выбор, ПКМ - бросок");
            list.add("");

            for (int i = 0; i < potions.tagCount(); i++) {
                NBTTagCompound potionTag = potions.getCompoundTagAt(i);
                ItemStack potionStack = ItemStack.loadItemStackFromNBT(potionTag);
                int count = potionTag.getInteger("Count");
                int slot = potionTag.getInteger("Slot");

                if (potionStack != null) {
                    String prefix = (slot == activeSlot) ? "§a► §f" : "§7  ";
                    list.add(prefix + potionStack.getDisplayName() + " §7x" + count);
                }
            }
        }
    }
}
