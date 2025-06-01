package com.kj.kjsprogressionlimiter.component;

import com.kj.kjsprogressionlimiter.KJsProgressionLimiter;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModComponents {
    protected static void initialize() {
        KJsProgressionLimiter.LOGGER.info("Registering Components for " + KJsProgressionLimiter.MOD_ID);
    }

    public static final ComponentType<Boolean> TOOL_USABLE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(KJsProgressionLimiter.MOD_ID, "tool_usable"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );
}
