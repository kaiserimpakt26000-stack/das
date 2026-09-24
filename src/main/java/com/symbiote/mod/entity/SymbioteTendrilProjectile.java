package com.symbiote.mod.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class SymbioteTendrilProjectile extends ThrowableItemProjectile {

    public SymbioteTendrilProjectile(EntityType<? extends SymbioteTendrilProjectile> type, Level level) {
        super(type, level);
    }

    public SymbioteTendrilProjectile(Level level, LivingEntity owner) {
        super(ModEntities.SYMBIOTE_TENDRIL_PROJECTILE.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return Items.STRING;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            if (result.getEntity() instanceof LivingEntity target) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3));
                target.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 3));
                if (this.getOwner() instanceof LivingEntity owner) {
                    target.hurt(this.damageSources().mobAttack(owner), 3.0F);
                }
            }
            ((ServerLevel) this.level()).sendParticles(ParticleTypes.ITEM_SLIME,
                    this.getX(), this.getY(), this.getZ(), 12, 0.2, 0.2, 0.2, 0.05);
            this.discard();
        }
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide && result.getType() == HitResult.Type.BLOCK) {
            this.discard();
        }
    }
}
