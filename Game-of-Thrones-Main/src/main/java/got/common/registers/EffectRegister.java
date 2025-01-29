package got.common.registers;

import got.common.potions.BleedingPotion;
import got.common.potions.CombatlogPotion;
import got.common.potions.CustomPotion;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class EffectRegister {

    public static Potion bleeding;

    public static Potion rest;
    public static Potion dexterity;
    public static Potion secondBreath;
    public static Potion exhaustion;
    private static int idCounter = 102;
    public static int COMBATLOG_POTIONID = 101;

    public static void registerPotions() {
        CombatlogPotion combatlogPotion = new CombatlogPotion( COMBATLOG_POTIONID, false, 9388224);
        Potion.potionTypes[COMBATLOG_POTIONID] = combatlogPotion;

        bleeding = new BleedingPotion(idCounter, true, 8171463, new ResourceLocation("got", "textures/potions/bleeding.png"), "got.potion.bleeding");
        registerPotion(bleeding);

        rest = new CustomPotion(idCounter, false, 8171463, new ResourceLocation("got", "textures/potions/rest.png"), "got.potion.rest");
        registerPotion(rest);

        dexterity = new CustomPotion(idCounter, false, 8171463, new ResourceLocation("got", "textures/potions/dexterity.png"), "got.potion.dexterity");
        registerPotion(dexterity);

        secondBreath = new CustomPotion(idCounter, false, 8171463, new ResourceLocation("got", "textures/potions/secondBreath.png"), "got.potion.secondBreath");
        registerPotion(secondBreath);

        exhaustion = new CustomPotion(idCounter, true, 8171463, new ResourceLocation("got", "textures/potions/exhaustion.png"), "got.potion.exhaustion");
        registerPotion(exhaustion);



    }

    private static void registerPotion(Potion pot) {
        Potion.potionTypes[idCounter] = pot;
        idCounter++;
    }
}
