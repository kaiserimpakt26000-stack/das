package com.symbiote.mod.item;

import com.symbiote.mod.ability.SymbioteAbility;
import com.symbiote.mod.capability.ModCapabilities;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

/**
 * The "Symbiote Will" — the interface through which a bonded player
 * triggers their unlocked active abilities.
 *  - Sneak + right click: cycle through unlocked abilities.
 *  - Right click: use the currently selected ability (if off cooldown).
 */
public class SymbioteCoreItem extends Item {

    public SymbioteCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level.isClientSide || !(player instanceof ServerPlayer sp)) {
            return InteractionResultHolder.success(stack);
        }

        player.getCapability(ModCapabilities.SYMBIOTE_DATA).ifPresent(data -> {
            if (!data.isBonded()) {
                player.displayClientMessage(Component.literal("Симбиот ещё не связан с тобой.").withStyle(ChatFormatting.GRAY), true);
                return;
            }

            List<SymbioteAbility> unlocked = SymbioteAbility.unlockedFor(data.getLevel());
            if (unlocked.isEmpty()) return;

            if (player.isShiftKeyDown()) {
                int next = (data.getSelectedAbilityIndex() + 1) % unlocked.size();
                data.setSelectedAbilityIndex(next);
                SymbioteAbility ability = unlocked.get(next);
                player.displayClientMessage(Component.literal(
                        "Способность: " + ability.getDisplayName() + " — " + ability.getDescription())
                        .withStyle(ChatFormatting.LIGHT_PURPLE), true);
                return;
            }

            int idx = Math.min(data.getSelectedAbilityIndex(), unlocked.size() - 1);
            SymbioteAbility ability = unlocked.get(idx);

            if (sp.getCooldowns().isOnCooldown(this)) {
                player.displayClientMessage(Component.literal("Симбиот ещё восстанавливается...")
                        .withStyle(ChatFormatting.RED), true);
                return;
            }

            ability.execute(sp);
            sp.getCooldowns().addCooldown(this, ability.getCooldownTicks());
            player.displayClientMessage(Component.literal("Активировано: " + ability.getDisplayName())
                    .withStyle(ChatFormatting.DARK_PURPLE), true);
        });

        return InteractionResultHolder.success(stack);
    }
}
