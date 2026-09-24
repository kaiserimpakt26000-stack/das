package com.symbiote.mod.item;

import com.symbiote.mod.SymbioteMod;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, SymbioteMod.MOD_ID);

    public static final net.minecraftforge.registries.RegistryObject<CreativeModeTab> SYMBIOTE_TAB =
            TABS.register("symbiote_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.symbiote"))
                    .icon(() -> new ItemStack(ModItems.SYMBIOTE_CHESTPLATE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.SYMBIOTE_GOO.get());
                        output.accept(ModItems.KLYNTAR_SHARD.get());
                        output.accept(ModItems.SYMBIOTE_WHIP.get());
                        output.accept(ModItems.SYMBIOTE_CORE.get());
                        output.accept(ModItems.SYMBIOTE_HELMET.get());
                        output.accept(ModItems.SYMBIOTE_CHESTPLATE.get());
                        output.accept(ModItems.SYMBIOTE_LEGGINGS.get());
                        output.accept(ModItems.SYMBIOTE_BOOTS.get());
                        output.accept(ModItems.SYMBIOTE_SPAWN_EGG.get());
                        output.accept(com.symbiote.mod.block.ModBlocks.METEORITE_ROCK.get());
                        output.accept(com.symbiote.mod.block.ModBlocks.METEORITE_CORE.get());
                    })
                    .build());
}
