package com.kj.kjsprogressionlimiter.util;

import com.kj.kjsprogressionlimiter.command.AddToolsCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;


public class ModRegistries {
    public static void registerModStuffs() {
        registerCommands();
    }

    private static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(AddToolsCommand::register);
    }
}
