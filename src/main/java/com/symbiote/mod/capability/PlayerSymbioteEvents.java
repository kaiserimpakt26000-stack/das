package com.symbiote.mod.capability;

import net.minecraftforge.fml.common.Mod;
import com.symbiote.mod.SymbioteMod;
import com.symbiote.mod.entity.SymbioteEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Mod.EventBusSubscriber(modid = SymbioteMod.MOD_ID)
public class PlayerSymbioteEvents {

    // --- Capability attachment ---

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<net.minecraft.world.entity.Entity> event) {
        if (event.getObject() instanceof Player) {
            SymbioteDataProvider provider = new SymbioteDataProvider();
            event.addCapability(SymbioteDataProvider.IDENTIFIER, provider);
            event.addListener(provider::invalidate);
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) return;
        event.getOriginal().getCapability(ModCapabilities.SYMBIOTE_DATA).ifPresent(oldData -> {
            event.getEntity().getCapability(ModCapabilities.SYMBIOTE_DATA).ifPresent(newData -> {
                newData.setBonded(oldData.isBonded());
                newData.setLevel(oldData.getLevel());
                newData.setXp(oldData.getXp());
                newData.setSelectedAbilityIndex(oldData.getSelectedAbilityIndex());
            });
        });
    }

    // --- Bonding: right-click a "wild" bondable symbiote to bond it to you ---

    @SubscribeEvent
    public static void onInteractWithSymbiote(PlayerInteractEvent.EntityInteract event) {
        if (!(event.getTarget() instanceof SymbioteEntity symbiote)) return;
        if (!symbiote.isBondable()) return;
        if (event.getLevel().isClientSide) return;
        Player player = event.getEntity();
        if (!player.isShiftKeyDown()) {
            player.displayClientMessage(Component.literal("Присядь (Shift) и нажми ПКМ, чтобы связаться с симбиотом.").withStyle(ChatFormatting.GRAY), true);
            return;
        }

        player.getCapability(ModCapabilities.SYMBIOTE_DATA).ifPresent(data -> {
            if (data.isBonded()) {
                player.displayClientMessage(Component.literal("Симбиот уже привязан к тебе.").withStyle(ChatFormatting.GRAY), true);
                return;
            }
            data.setBonded(true);
            data.setLevel(1);
            data.setXp(0);
            data.setSelectedAbilityIndex(0);
            symbiote.discard();
            player.level().playSound(null, player.blockPosition(),
                    net.minecraft.sounds.SoundEvents.WARDEN_AGITATED_ROAR, net.minecraft.sounds.SoundSource.PLAYERS, 0.5F, 1.6F);
            player.displayClientMessage(Component.literal("Симбиот связался с тобой! Мы теперь одно целое.").withStyle(ChatFormatting.DARK_PURPLE), false);
        });
    }

    // --- XP gain: bonded players get XP for kills ---

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof ServerPlayer player) {
            player.getCapability(ModCapabilities.SYMBIOTE_DATA).ifPresent(data -> {
                if (!data.isBonded()) return;
                int xpGain = (int) Math.max(5, event.getEntity().getMaxHealth());
                boolean leveledUp = data.addXp(xpGain);
                if (leveledUp) {
                    player.displayClientMessage(Component.literal(
                            "Симбиот эволюционировал! Новый уровень: " + data.getLevel()).withStyle(ChatFormatting.LIGHT_PURPLE), false);
                    player.level().playSound(null, player.blockPosition(),
                            net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP, net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 0.7F);
                }
            });
        }
    }

    // --- Passive per-tick bonuses, scaling with level ---

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide) return;

        player.getCapability(ModCapabilities.SYMBIOTE_DATA).ifPresent(data -> {
            if (!data.isBonded()) return;
            int lvl = data.getLevel();

            // Base bond bonuses (always active once bonded)
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 0, false, false, false));
            player.fallDistance = 0.0F;

            if (lvl >= 3) player.addEffect(new MobEffectInstance(MobEffects.JUMP, 30, 0, false, false, false));
            if (lvl >= 4) player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 30, 0, false, false, false));
            if (lvl >= 5) player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, false, false, false));
            if (lvl >= 6) player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 30, 1, false, false, false));
            if (lvl >= 7) player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30, 0, false, false, false));
            if (lvl >= 8) player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 30, 0, false, false, false));
            if (lvl >= 9) player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30, 0, false, false, false));
            if (lvl >= 10) player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 30, 0, false, false, false));
        });
    }
}
