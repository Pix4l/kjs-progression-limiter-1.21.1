package com.kj.kjsprogressionlimiter;


import com.kj.kjsprogressionlimiter.networking.DirtBrokenPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KJsProgressionLimiter implements ModInitializer {
	public static final String MOD_ID = "kjs-progression-limiter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final Identifier DIRT_BROKEN = Identifier.of(MOD_ID, "dirt_broken");
	public static final Identifier INITIAL_SYNC = Identifier.of(MOD_ID, "initial_sync");

	@Override
	public void onInitialize() {
		ServerPlayConnectionEvents.JOIN.register(((serverPlayNetworkHandler, packetSender, minecraftServer) -> {
			PlayerData playerState = StateSaverAndLoader.getPlayerState(serverPlayNetworkHandler.getPlayer());
			PacketByteBuf data = PacketByteBufs.create();
			data.writeInt(playerState.dirtBlocksBroken);
			minecraftServer.execute(() -> {
				ServerPlayNetworking.send(serverPlayNetworkHandler.getPlayer(), INITIAL_SYNC, data);
			});
		}));

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
			if (state.getBlock() == Blocks.GRASS_BLOCK || state.getBlock() == Blocks.DIRT) {
				// Send a packet to the client
				MinecraftServer server = world.getServer();
				assert server != null;

				// Increment the amount of dirt blocks that have been broken
				StateSaverAndLoader serverState = StateSaverAndLoader.getServerState(server);
				serverState.totalDirtBlocksBroken += 1;

				PlayerData playerState = StateSaverAndLoader.getPlayerState(player);
				playerState.dirtBlocksBroken += 1;

				PacketByteBuf data = PacketByteBufs.create();
				data.writeInt(serverState.totalDirtBlocksBroken);
				data.writeInt(playerState.dirtBlocksBroken);

				ServerPlayerEntity playerEntity = server.getPlayerManager().getPlayer(player.getUuid());
				server.execute(() -> {
					assert playerEntity != null;
					ServerPlayNetworking.send(playerEntity, DIRT_BROKEN, data);
				});
			}
		});
	}
}