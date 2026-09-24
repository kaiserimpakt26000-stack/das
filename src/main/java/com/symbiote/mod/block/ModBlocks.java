package com.symbiote.mod.block;

import com.symbiote.mod.SymbioteMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, SymbioteMod.MOD_ID);
    public static final DeferredRegister<Item> BLOCK_ITEMS =
            DeferredRegister.create(Registries.ITEM, SymbioteMod.MOD_ID);

    public static final RegistryObject<Block> METEORITE_ROCK = registerWithItem("meteorite_rock",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .strength(15.0F, 200.0F)
                    .sound(SoundType.BASALT)
                    .requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> METEORITE_CORE = registerWithItem("meteorite_core",
            () -> new Block(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_PURPLE)
                    .strength(20.0F, 400.0F)
                    .sound(SoundType.AMETHYST)
                    .lightLevel(state -> 12)
                    .requiresCorrectToolForDrops()));

    private static RegistryObject<Block> registerWithItem(String name, java.util.function.Supplier<Block> block) {
        RegistryObject<Block> holder = BLOCKS.register(name, block);
        BLOCK_ITEMS.register(name, () -> new BlockItem(holder.get(), new Item.Properties()));
        return holder;
    }
}
