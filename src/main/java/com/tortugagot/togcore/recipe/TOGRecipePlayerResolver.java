package com.tortugagot.togcore.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;

import java.lang.reflect.Field;

public final class TOGRecipePlayerResolver {
    private TOGRecipePlayerResolver() {
    }

    public static EntityPlayer findPlayer(InventoryCrafting inventory) {
        Container container = findContainer(inventory);
        return container != null ? findPlayerInContainer(container) : null;
    }

    private static Container findContainer(InventoryCrafting inventory) {
        Class<?> type = InventoryCrafting.class;
        while (type != null) {
            for (Field field : type.getDeclaredFields()) {
                if (!Container.class.isAssignableFrom(field.getType())) {
                    continue;
                }
                try {
                    field.setAccessible(true);
                    Object value = field.get(inventory);
                    if (value instanceof Container) {
                        return (Container) value;
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    private static EntityPlayer findPlayerInContainer(Container container) {
        EntityPlayer player = findPlayerInSlots(container);
        if (player != null) {
            return player;
        }

        Class<?> type = container.getClass();
        while (type != null) {
            for (Field field : type.getDeclaredFields()) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(container);
                    if (value instanceof EntityPlayer) {
                        return (EntityPlayer) value;
                    }
                    if (value instanceof InventoryPlayer) {
                        return ((InventoryPlayer) value).player;
                    }
                } catch (IllegalAccessException ignored) {
                }
            }
            type = type.getSuperclass();
        }
        return null;
    }

    private static EntityPlayer findPlayerInSlots(Container container) {
        for (Object obj : container.inventorySlots) {
            if (!(obj instanceof Slot)) {
                continue;
            }
            Slot slot = (Slot) obj;
            if (slot.inventory instanceof InventoryPlayer) {
                return ((InventoryPlayer) slot.inventory).player;
            }
        }
        return null;
    }
}
