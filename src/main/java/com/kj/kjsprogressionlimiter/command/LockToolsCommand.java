package com.kj.kjsprogressionlimiter.command;

import com.kj.kjsprogressionlimiter.util.IEntityDataSaver;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class LockToolsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("tools")
                .then(CommandManager.literal("lock")
                        .then(CommandManager.argument("tool", StringArgumentType.string())
                                .executes(LockToolsCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        ServerPlayerEntity player = source.getPlayer();

        String tool = StringArgumentType.getString(context, "tool");

        assert player != null;
        NbtCompound persistentData = ((IEntityDataSaver) player).getPersistentData();

        persistentData.putBoolean(tool, true);

        //player.sendMessage(Text.literal(((IEntityDataSaver) player).getPersistentData().toString()));

        return 1;
    }
}
