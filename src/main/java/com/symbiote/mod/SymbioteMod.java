package com.symbiote.mod;

import com.symbiote.mod.block.ModBlocks;
import com.symbiote.mod.entity.ModEntities;
import com.symbiote.mod.item.ModCreativeTabs;
import com.symbiote.mod.item.ModItems;
import com.symbiote.mod.worldgen.ModFeatures;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(SymbioteMod.MOD_ID)
public class SymbioteMod {

    public static final String MOD_ID = "symbiote";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public SymbioteMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.TABS.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ITEMS.register(modEventBus);
        ModFeatures.FEATURES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        LOGGER.info("Symbiote mod loading: 'We are Venom.'");
    }

    private void commonSetup(final net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("Symbiote common setup complete");
    }
}
