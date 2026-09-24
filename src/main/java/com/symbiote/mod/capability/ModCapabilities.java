package com.symbiote.mod.capability;

import net.minecraftforge.fml.common.Mod;
import com.symbiote.mod.SymbioteMod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

@Mod.EventBusSubscriber(modid = SymbioteMod.MOD_ID)
public class ModCapabilities {

    public static final Capability<SymbioteData> SYMBIOTE_DATA =
            CapabilityManager.get(new CapabilityToken<>() {});

    // NOTE: on some NeoForge 1.20.1 builds capability registration happens
    // automatically via CapabilityManager.get(); no explicit RegisterCapabilitiesEvent
    // call is required for the classic Capability<T> system used here.
}
