package com.symbiote.mod.worldgen;

import com.symbiote.mod.SymbioteMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, SymbioteMod.MOD_ID);

    public static final RegistryObject<MeteoriteFeature> METEORITE =
            FEATURES.register("meteorite", () -> new MeteoriteFeature(NoneFeatureConfiguration.CODEC));
}
