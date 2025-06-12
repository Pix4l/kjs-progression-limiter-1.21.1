package com.kj.kjsprogressionlimiter;

import com.kj.kjsprogressionlimiter.util.IEntityDataSaver;
import com.kj.kjsprogressionlimiter.util.ModRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class KJsProgressionLimiter implements ModInitializer {
	public static final String MOD_ID = "kjs-progression-limiter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
        ModRegistries.registerModStuffs();

		ServerPlayerEvents.AFTER_RESPAWN.register(((serverPlayerEntity, serverPlayerEntity1, b) -> {
			NbtCompound oldPersistentData = ((IEntityDataSaver) serverPlayerEntity).getPersistentData();
			NbtCompound newPersistentData = ((IEntityDataSaver) serverPlayerEntity1).getPersistentData();
			newPersistentData.copyFrom(oldPersistentData);
		}));

		PlayerBlockBreakEvents.BEFORE.register(((world, player, pos, state, blockEntity) -> {
			NbtCompound persistentData = ((IEntityDataSaver) player).getPersistentData();
			String currentTool = player.getMainHandStack().getItem().toString();
			if(persistentData.getBoolean(currentTool) && !player.isCreative()) {
				player.sendMessage(Text.literal("You do not have the dexterity to mine with this!"));
				return false;
			}
			return true;
		}));

		

		AttackEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
			NbtCompound persistentData = ((IEntityDataSaver) player).getPersistentData();
			String currentTool = player.getMainHandStack().getItem().toString();
			if(persistentData.getBoolean(currentTool) && !player.isCreative()) {
				player.sendMessage(Text.literal("You are not strong enough to wield this item!"));
				return ActionResult.FAIL;
			}
			return ActionResult.PASS;
		}));

		UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
			NbtCompound persistentData = ((IEntityDataSaver) player).getPersistentData();
			String currentTool = player.getMainHandStack().getItem().toString();
			if(persistentData.getBoolean(currentTool) && !player.isCreative()) {
				player.sendMessage(Text.literal("You do not have the expertise to use this item!"));
				return ActionResult.FAIL;
			}
			return ActionResult.PASS;
		}));

		ServerEntityEvents.EQUIPMENT_CHANGE.register((livingEntity, equipmentSlot, itemStack, itemStack1) -> {
			if (livingEntity.isPlayer() && equipmentSlot.isArmorSlot()) {
				NbtCompound persistentData = ((IEntityDataSaver) livingEntity).getPersistentData();
				if (persistentData.getBoolean(itemStack1.getItem().toString())) {
					PlayerInventory playerInventory = ((PlayerEntity) livingEntity).getInventory();
					livingEntity.sendMessage(Text.literal("This armour is too heavy for you to wear!"));
					playerInventory.offerOrDrop(itemStack1);
					playerInventory.removeOne(itemStack1);
				}

			}
		});
	}
}