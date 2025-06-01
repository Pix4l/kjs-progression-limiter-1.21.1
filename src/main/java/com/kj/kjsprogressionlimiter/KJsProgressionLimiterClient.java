package com.kj.kjsprogressionlimiter;

import com.kj.kjsprogressionlimiter.networking.DirtBrokenPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class KJsProgressionLimiterClient implements ClientModInitializer {

    private static void handleDirtBrokenPayload(DirtBrokenPayload payload, ClientPlayNetworking.Context context) {
        ClientPlayerEntity player = context.client().player;
        assert player != null;
        player.sendMessage(Text.literal("Total dirt blocks broken: " + payload.totalDirtBlocksBroken()), false);
    }

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(KJsProgressionLimiter.DIRT_BROKEN, (client, handler, buf, responseSender) -> {
            int totalDirtBlocksBroken = buf.readInt();
            int playerSpecificDirtBlocksBroken = buf.readInt();

            client.execute(() -> {
                client.player.sendMessage(Text.literal("Total dirt blocks broken: " + totalDirtBlocksBroken));
                client.player.sendMessage(Text.literal("Player specific dirt blocks broken: " + playerSpecificDirtBlocksBroken));
            });
        });
    }
}
