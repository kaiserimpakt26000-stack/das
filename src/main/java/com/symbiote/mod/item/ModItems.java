package com.symbiote.mod.item;

import com.symbiote.mod.SymbioteMod;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, SymbioteMod.MOD_ID);

    // --- Raw materials ---
    public static final RegistryObject<Item> SYMBIOTE_GOO = ITEMS.register("symbiote_goo",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));

    public static final RegistryObject<Item> KLYNTAR_SHARD = ITEMS.register("klyntar_shard",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    // --- Weapon ---
    public static final RegistryObject<Item> SYMBIOTE_WHIP = ITEMS.register("symbiote_whip",
            () -> new SymbioteWhipItem(Tiers.NETHERITE, 4, -2.2F, new Item.Properties()));

    // --- Armor ---
    public static final RegistryObject<Item> SYMBIOTE_HELMET = ITEMS.register("symbiote_helmet",
            () -> new SymbioteArmorItem(ModArmorMaterials.SYMBIOTE, ArmorItem.Type.HELMET,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SYMBIOTE_CHESTPLATE = ITEMS.register("symbiote_chestplate",
            () -> new SymbioteArmorItem(ModArmorMaterials.SYMBIOTE, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SYMBIOTE_LEGGINGS = ITEMS.register("symbiote_leggings",
            () -> new SymbioteArmorItem(ModArmorMaterials.SYMBIOTE, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    public static final RegistryObject<Item> SYMBIOTE_BOOTS = ITEMS.register("symbiote_boots",
            () -> new SymbioteArmorItem(ModArmorMaterials.SYMBIOTE, ArmorItem.Type.BOOTS,
                    new Item.Properties().rarity(Rarity.EPIC)));

    // --- Ability item ---
    public static final RegistryObject<Item> SYMBIOTE_CORE = ITEMS.register("symbiote_core",
            () -> new SymbioteCoreItem(new Item.Properties().rarity(Rarity.EPIC).stacksTo(1)));

    // --- Spawn egg ---
    public static final RegistryObject<Item> SYMBIOTE_SPAWN_EGG = ITEMS.register("symbiote_spawn_egg",
            () -> new net.minecraft.world.item.SpawnEggItem(
                    com.symbiote.mod.entity.ModEntities.SYMBIOTE.get(),
                    0x0b0b0f, 0xa30f0f,
                    new Item.Properties()));
}
