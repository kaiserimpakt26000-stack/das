package com.symbiote.mod.item;

import com.symbiote.mod.SymbioteMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

/**
 * Living symbiote armor. Slightly tougher than diamond, lighter (cheaper
 * enchantability) and it slowly heals itself back onto the wearer — see
 * com.symbiote.mod.event.SymbioteArmorEvents for the self-repair / bonus logic.
 */
public enum ModArmorMaterials implements ArmorMaterial {

    SYMBIOTE("symbiote", 40, buildHealthMap(), 12,
            SoundEvents.ARMOR_EQUIP_NETHERITE, 3.5F, 0.15F,
            () -> Ingredient.of(ModItems.SYMBIOTE_GOO.get()));

    private static final EnumMap<ArmorItem.Type, Integer> buildHealthMap() {
        EnumMap<ArmorItem.Type, Integer> map = new EnumMap<>(ArmorItem.Type.class);
        map.put(ArmorItem.Type.BOOTS, 3);
        map.put(ArmorItem.Type.LEGGINGS, 6);
        map.put(ArmorItem.Type.CHESTPLATE, 9);
        map.put(ArmorItem.Type.HELMET, 3);
        return map;
    }

    private static final EnumMap<ArmorItem.Type, Integer> DEFENSE_PER_TYPE = new EnumMap<>(ArmorItem.Type.class);
    static {
        DEFENSE_PER_TYPE.put(ArmorItem.Type.BOOTS, 3);
        DEFENSE_PER_TYPE.put(ArmorItem.Type.LEGGINGS, 6);
        DEFENSE_PER_TYPE.put(ArmorItem.Type.CHESTPLATE, 9);
        DEFENSE_PER_TYPE.put(ArmorItem.Type.HELMET, 4);
    }

    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> durabilityPerType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    ModArmorMaterials(String name, int durabilityMultiplier, EnumMap<ArmorItem.Type, Integer> durabilityPerType,
                       int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance,
                       Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.durabilityPerType = durabilityPerType;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient;
    }

    @Override
    public int getDurabilityForType(ArmorItem.Type type) {
        return this.durabilityPerType.get(type) * this.durabilityMultiplier;
    }

    @Override
    public int getDefenseForType(ArmorItem.Type type) {
        return DEFENSE_PER_TYPE.get(type);
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.sound;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }

    @Override
    public String getName() {
        return SymbioteMod.MOD_ID + ":" + this.name;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
