package got.common.database;

import got.common.potions.GOTCustomPotion;
import got.common.potions.GOTPotionAntidote;
import got.common.potions.GOTPotionBleeding;
import got.common.potions.GOTPotionCombatlog;
import got.common.potions.GOTPotionFreeze;
import got.common.potions.GOTPotionNauseaResistance;
import got.common.potions.GOTPotionPoisonKilling;
import got.common.potions.GOTPotionRage;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;

public class GOTEffects {

    public static Potion killingPoison;
    public static GOTPotionCombatlog combatLog;
    public static Potion bleeding;
    public static Potion antidote;
    public static Potion nauseaResistance;
    public static Potion frostResistance;
    public static Potion rage;
    public static Potion catEye;
    public static Potion combatSpirit;
    public static Potion inversion;
    public static Potion freeze;

    public static Potion rest;
    public static Potion dexterity;
    public static Potion secondBreath;
    public static Potion exhaustion;

    private static int idCounter = 101;

    public static void registerPotions() {
        killingPoison = new GOTPotionPoisonKilling();

        combatLog = new GOTPotionCombatlog(idCounter);
        registerPotion(combatLog);

        bleeding = new GOTPotionBleeding(idCounter);
        registerPotion(bleeding);

        antidote = new GOTPotionAntidote(idCounter);
        registerPotion(antidote);

        nauseaResistance = new GOTPotionNauseaResistance(idCounter);
        registerPotion(nauseaResistance);

        frostResistance = new GOTCustomPotion(idCounter, false, 0, "frostResistance");
        registerPotion(frostResistance);

        rage = new GOTPotionRage(idCounter);
        registerPotion(rage);

        catEye = new GOTCustomPotion(idCounter, false, 0, "catEye");
        registerPotion(catEye);

        combatSpirit = new GOTCustomPotion(idCounter, false, 0, "combatSpirit").func_111184_a(SharedMonsterAttributes.movementSpeed, "91AEAA56-376B-4498-935B-2F7F68070635", 0.05D, 2);
        registerPotion(combatSpirit);

        inversion = new GOTCustomPotion(idCounter, false, 0, "inversion");
        registerPotion(inversion);

        freeze = new GOTPotionFreeze(idCounter);
        registerPotion(freeze);

        rest = new GOTCustomPotion(idCounter, false, 8171463, new ResourceLocation("got", "textures/potions/rest.png"), "got.potion.rest");
        registerPotion(rest);

        dexterity = new GOTCustomPotion(idCounter, false, 8171463, new ResourceLocation("got", "textures/potions/dexterity.png"), "got.potion.dexterity");
        registerPotion(dexterity);

        secondBreath = new GOTCustomPotion(idCounter, false, 8171463, new ResourceLocation("got", "textures/potions/secondBreath.png"), "got.potion.secondBreath");
        registerPotion(secondBreath);

        exhaustion = new GOTCustomPotion(idCounter, true, 8171463, new ResourceLocation("got", "textures/potions/exhaustion.png"), "got.potion.exhaustion");
        registerPotion(exhaustion);
    }

    private static void registerPotion(Potion pot) {
        Potion.potionTypes[idCounter] = pot;
        idCounter++;
    }
}
