package com.symbiote.mod.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

/**
 * A living tentacle-whip. On hit it yanks the target toward the wielder,
 * just like Venom pulling enemies in — implemented in SymbioteWhipItem#hurtEnemy.
 */
public class SymbioteWhipItem extends SwordItem {

    public SymbioteWhipItem(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    @Override
    public boolean hurtEnemy(net.minecraft.world.item.ItemStack stack, LivingEntity target, LivingEntity attacker) {
        boolean result = super.hurtEnemy(stack, target, attacker);
        if (!attacker.level().isClientSide) {
            double dx = attacker.getX() - target.getX();
            double dz = attacker.getZ() - target.getZ();
            double dist = Math.max(0.1, Math.sqrt(dx * dx + dz * dz));
            target.setDeltaMovement(target.getDeltaMovement().add(
                    (dx / dist) * 0.6,
                    0.25,
                    (dz / dist) * 0.6));
            target.hurtMarked = true;
        }
        return result;
    }
}
