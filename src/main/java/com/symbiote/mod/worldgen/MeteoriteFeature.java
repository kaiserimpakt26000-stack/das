package com.symbiote.mod.worldgen;

import com.mojang.serialization.Codec;
import com.symbiote.mod.block.ModBlocks;
import com.symbiote.mod.entity.ModEntities;
import com.symbiote.mod.entity.SymbioteEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Random;

/**
 * A rare fallen-meteorite crater: a scorched crater lined with meteorite
 * rock, a glowing core at the center, and a single bondable Symbiote
 * waiting for a host. Placement rarity is controlled entirely by the
 * placed feature / biome modifier JSON (data/symbiote/worldgen/...),
 * not by this class.
 */
public class MeteoriteFeature extends Feature<NoneFeatureConfiguration> {

    public MeteoriteFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = findSurface(level, context.origin());
        Random random = new Random(context.random().nextLong());

        int craterRadius = 6 + random.nextInt(3);

        // Carve the crater bowl and line it with meteorite rock.
        for (int dx = -craterRadius; dx <= craterRadius; dx++) {
            for (int dz = -craterRadius; dz <= craterRadius; dz++) {
                double dist = Math.sqrt(dx * dx + dz * dz);
                if (dist > craterRadius) continue;
                int depth = (int) (Math.sqrt(craterRadius * craterRadius - dist * dist) * 0.6);
                BlockPos top = origin.offset(dx, 0, dz);
                for (int dy = 0; dy <= depth; dy++) {
                    BlockPos pos = top.below(dy);
                    if (dy == depth) {
                        level.setBlock(pos, ModBlocks.METEORITE_ROCK.get().defaultBlockState(), 3);
                    } else {
                        level.setBlock(pos, net.minecraft.world.level.block.Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }
        }

        // Core cluster in the middle of the crater.
        BlockPos center = origin.below(2);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dz = -1; dz <= 1; dz++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (random.nextFloat() < 0.6F) {
                        level.setBlock(center.offset(dx, dy, dz), ModBlocks.METEORITE_CORE.get().defaultBlockState(), 3);
                    }
                }
            }
        }
        level.setBlock(center, ModBlocks.METEORITE_CORE.get().defaultBlockState(), 3);

        // Scattered meteorite rock debris around the rim.
        for (int i = 0; i < craterRadius * 3; i++) {
            int dx = random.nextInt(craterRadius * 2 + 1) - craterRadius;
            int dz = random.nextInt(craterRadius * 2 + 1) - craterRadius;
            BlockPos pos = findSurface(level, origin.offset(dx, 0, dz)).above();
            if (random.nextFloat() < 0.3F) {
                level.setBlock(pos, ModBlocks.METEORITE_ROCK.get().defaultBlockState(), 3);
            }
        }

        // Spawn the bondable symbiote at the crater center.
        if (level instanceof ServerLevel serverLevel) {
            SymbioteEntity symbiote = ModEntities.SYMBIOTE.get().create(serverLevel);
            if (symbiote != null) {
                symbiote.setBondable(true);
                symbiote.moveTo(center.getX() + 0.5, center.getY() + 1.5, center.getZ() + 0.5, 0, 0);
                symbiote.setPersistenceRequired();
                serverLevel.addFreshEntity(symbiote);
            }
        }

        return true;
    }

    private static BlockPos findSurface(WorldGenLevel level, BlockPos columnPos) {
        BlockPos.MutableBlockPos pos = columnPos.mutable().setY(level.getMaxBuildHeight() - 1);
        while (pos.getY() > level.getMinBuildHeight() && level.isEmptyBlock(pos)) {
            pos.move(0, -1, 0);
        }
        return pos.immutable();
    }
}
