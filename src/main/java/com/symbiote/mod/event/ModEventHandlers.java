package com.symbiote.mod.event;

import net.minecraftforge.fml.common.Mod;
import com.symbiote.mod.SymbioteMod;
import com.symbiote.mod.entity.ModEntities;
import com.symbiote.mod.entity.SymbioteEntity;
import com.symbiote.mod.item.ModItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.TickEvent;

/**
 * Handles:
 *  - registering the symbiote mob's attributes
 *  - the "full symbiote suit" passive bonus (speed, jump, no fall damage,
 *    fire resistance) applied every tick a player wears the full set.
 */
@Mod.EventBusSubscriber(modid = SymbioteMod.MOD_ID)
public class ModEventHandlers {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.SYMBIOTE.get(), SymbioteEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        var player = event.player;
        if (player.level().isClientSide) return;

        boolean fullSet =
                isSymbiote(player.getItemBySlot(EquipmentSlot.HEAD), ModItems.SYMBIOTE_HELMET) &&
                isSymbiote(player.getItemBySlot(EquipmentSlot.CHEST), ModItems.SYMBIOTE_CHESTPLATE) &&
                isSymbiote(player.getItemBySlot(EquipmentSlot.LEGS), ModItems.SYMBIOTE_LEGGINGS) &&
                isSymbiote(player.getItemBySlot(EquipmentSlot.FEET), ModItems.SYMBIOTE_BOOTS);

        if (fullSet) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 40, 1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 40, 1, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 40, 0, false, false, false));
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 0, false, false, false));
            player.fallDistance = 0.0F;
        }
    }

    private static boolean isSymbiote(ItemStack stack, net.minecraftforge.registries.RegistryObject<net.minecraft.world.item.Item> expected) {
        return !stack.isEmpty() && stack.is(expected.get());
    }
}
