package com.kj.kjsprogressionlimiter.command;

import com.kj.kjsprogressionlimiter.util.IEntityDataSaver;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class UnlockToolsCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess commandRegistryAccess, CommandManager.RegistrationEnvironment registrationEnvironment) {
        dispatcher.register(CommandManager.literal("tools")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .then(CommandManager.literal("unlock")
                        .then(CommandManager.argument("tool", StringArgumentType.string())
                                .executes(UnlockToolsCommand::run))));
    }

    private static int run(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();
        PlayerEntity player = source.getServer().getOverworld().getPlayerByUuid(context.getSource().getPlayer().getUuid());

        String tool = StringArgumentType.getString(context, "tool");

        assert player != null;
        NbtCompound persistentData = ((IEntityDataSaver) player).getPersistentData();

        persistentData.putBoolean(tool, false);

        //player.sendMessage(Text.literal(((IEntityDataSaver) player).getPersistentData().toString()));

        return 1;
    }
}
