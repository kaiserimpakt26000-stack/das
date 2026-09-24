package com.symbiote.mod.ability;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * All active symbiote abilities. Each has:
 *  - unlockLevel: minimum symbiote level required
 *  - cooldownTicks: cooldown after use
 *  - execute(): the actual effect, run server-side only
 *
 * Passive bonuses (permanent while bonded) are handled separately in
 * PlayerSymbioteEvents#applyPassives, scaling with level.
 */
public enum SymbioteAbility {

    REGENERATION_PULSE(1, 100, "Regeneration Pulse", "Мгновенно лечит и снимает негативные эффекты") {
        @Override
        public void execute(ServerPlayer player) {
            player.heal(6.0F);
            player.removeEffect(MobEffects.POISON);
            player.removeEffect(MobEffects.WITHER);
            player.level().playSound(null, player.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 0.4F, 1.6F);
        }
    },

    VENOM_DASH(2, 60, "Venom Dash", "Резкий рывок вперёд по направлению взгляда") {
        @Override
        public void execute(ServerPlayer player) {
            Vec3 look = player.getLookAngle();
            player.setDeltaMovement(player.getDeltaMovement().add(look.x * 1.8, 0.35, look.z * 1.8));
            player.fallDistance = 0;
            player.hurtMarked = true;
            spawnTrail(player);
        }
    },

    TENDRIL_GRAPPLE(3, 100, "Tendril Grapple", "Притягивает ближайшего врага перед игроком") {
        @Override
        public void execute(ServerPlayer player) {
            for (Entity e : player.level().getEntities(player, player.getBoundingBox().inflate(16.0),
                    ent -> ent instanceof LivingEntity && ent != player)) {
                Vec3 toPlayer = player.position().subtract(e.position());
                double dist = toPlayer.length();
                if (dist < 16 && player.hasLineOfSight(e)) {
                    Vec3 pull = toPlayer.normalize().scale(1.1);
                    e.setDeltaMovement(e.getDeltaMovement().add(pull.x, 0.25, pull.z));
                    e.hurtMarked = true;
                    break;
                }
            }
            player.level().playSound(null, player.blockPosition(), SoundEvents.TRIDENT_RETURN, SoundSource.PLAYERS, 1.0F, 0.7F);
        }
    },

    WEB_SHOT(4, 80, "Web Shot", "Выстреливает липким сгустком, замедляющим цель") {
        @Override
        public void execute(ServerPlayer player) {
            com.symbiote.mod.entity.SymbioteTendrilProjectile proj =
                    new com.symbiote.mod.entity.SymbioteTendrilProjectile(player.level(), player);
            Vec3 look = player.getLookAngle();
            proj.shoot(look.x, look.y, look.z, 1.6F, 1.0F);
            player.level().addFreshEntity(proj);
            player.level().playSound(null, player.blockPosition(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.PLAYERS, 0.8F, 1.4F);
        }
    },

    CAMOUFLAGE(5, 400, "Camouflage", "Кратковременная невидимость") {
        @Override
        public void execute(ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 200, 0, false, true));
        }
    },

    VENOM_STRIKE(6, 300, "Venom Strike", "Следующие удары отравляют врагов (15 сек)") {
        @Override
        public void execute(ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 300, 0, false, false)); // placeholder buff marker handled via capability flag by caller
        }
    },

    WALL_CRAWL(7, 200, "Wall Crawl", "Медленное падение и цепкость на 20 сек") {
        @Override
        public void execute(ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 400, 0, false, true));
        }
    },

    SYMBIOTE_ROAR(8, 240, "Symbiote Roar", "Волна, отбрасывающая и ослабляющая врагов рядом") {
        @Override
        public void execute(ServerPlayer player) {
            for (Entity e : player.level().getEntities(player, player.getBoundingBox().inflate(6.0),
                    ent -> ent instanceof LivingEntity && ent != player)) {
                Vec3 push = e.position().subtract(player.position()).normalize().scale(1.4);
                e.setDeltaMovement(e.getDeltaMovement().add(push.x, 0.4, push.z));
                e.hurtMarked = true;
                if (e instanceof LivingEntity le) {
                    le.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1));
                }
            }
            ((ServerLevel) player.level()).sendParticles(ParticleTypes.SONIC_BOOM, player.getX(), player.getY() + 1, player.getZ(), 1, 0, 0, 0, 0);
            player.level().playSound(null, player.blockPosition(), SoundEvents.WARDEN_ROAR, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    },

    ADRENALINE_SURGE(9, 600, "Adrenaline Surge", "Ярость: сила и стойкость на 10 сек") {
        @Override
        public void execute(ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200, 1, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1, false, true));
        }
    },

    FULL_SYMBIOTE_FORM(10, 1200, "Full Symbiote Form", "Полная форма: максимум всех бонусов на 30 сек") {
        @Override
        public void execute(ServerPlayer player) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600, 3, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 2, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 2, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 600, 2, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 600, 1, false, true));
            player.level().playSound(null, player.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.PLAYERS, 0.6F, 1.8F);
        }
    };

    private final int unlockLevel;
    private final int cooldownTicks;
    private final String displayName;
    private final String description;

    SymbioteAbility(int unlockLevel, int cooldownTicks, String displayName, String description) {
        this.unlockLevel = unlockLevel;
        this.cooldownTicks = cooldownTicks;
        this.displayName = displayName;
        this.description = description;
    }

    public abstract void execute(ServerPlayer player);

    public int getUnlockLevel() { return unlockLevel; }
    public int getCooldownTicks() { return cooldownTicks; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }

    public static List<SymbioteAbility> unlockedFor(int level) {
        return java.util.Arrays.stream(values()).filter(a -> a.unlockLevel <= level).toList();
    }

    private static void spawnTrail(ServerPlayer player) {
        ((ServerLevel) player.level()).sendParticles(ParticleTypes.WITCH, player.getX(), player.getY() + 1, player.getZ(), 8, 0.3, 0.3, 0.3, 0.02);
    }
}
