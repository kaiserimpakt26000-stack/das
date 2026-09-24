package com.symbiote.mod.entity;

import com.symbiote.mod.SymbioteMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, SymbioteMod.MOD_ID);

    public static final RegistryObject<EntityType<SymbioteEntity>> SYMBIOTE =
            ENTITIES.register("symbiote", () -> EntityType.Builder.of(SymbioteEntity::new, MobCategory.MONSTER)
                    .sized(0.7F, 1.9F)
                    .clientTrackingRange(8)
                    .build(SymbioteMod.MOD_ID + ":symbiote"));

    public static final RegistryObject<EntityType<SymbioteTendrilProjectile>> SYMBIOTE_TENDRIL_PROJECTILE =
            ENTITIES.register("symbiote_tendril_projectile", () -> EntityType.Builder.<SymbioteTendrilProjectile>of(SymbioteTendrilProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(SymbioteMod.MOD_ID + ":symbiote_tendril_projectile"));
}
