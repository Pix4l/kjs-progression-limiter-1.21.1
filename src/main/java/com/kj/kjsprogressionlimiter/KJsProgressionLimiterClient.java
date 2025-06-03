package com.kj.kjsprogressionlimiter;

import com.kj.kjsprogressionlimiter.util.IEntityDataSaver;
import com.kj.kjsprogressionlimiter.util.InitialToolBlacklist;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class KJsProgressionLimiterClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ServerPlayConnectionEvents.JOIN.register(((serverPlayNetworkHandler, packetSender, minecraftServer) -> {
            PlayerEntity player = serverPlayNetworkHandler.player;
            assert player != null;
            NbtCompound persistentData = ((IEntityDataSaver) player).getPersistentData();

            if(!persistentData.getBoolean("joined")) {
                //player.sendMessage(Text.literal("Initializing Tool Blacklist"));
                String[] toolList = InitialToolBlacklist.initialToolBlacklist;

                for (String tool : toolList) {
                    persistentData.putBoolean(tool, true);
                }
                //player.sendMessage(Text.literal("Joined already!"));
                persistentData.putBoolean("joined", true);
            }
        }));




    }
}
