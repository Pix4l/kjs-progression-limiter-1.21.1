package com.kj.kjsprogressionlimiter;


import com.kj.kjsprogressionlimiter.util.IEntityDataSaver;
import com.kj.kjsprogressionlimiter.util.ModRegistries;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KJsProgressionLimiter implements ModInitializer {
	public static final String MOD_ID = "kjs-progression-limiter";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);


	@Override
	public void onInitialize() {
		ModRegistries.registerModStuffs();

		PlayerBlockBreakEvents.BEFORE.register(((world, player, pos, state, blockEntity) -> {
			NbtCompound persistentData = ((IEntityDataSaver) player).getPersistentData();
			String currentTool = player.getMainHandStack().getItem().toString();
			if(persistentData.getBoolean(currentTool)) {
				player.sendMessage(Text.literal("You do not have the dexterity to mine with this!"));
				return false;
			}
			return true;
		}));

		AttackEntityCallback.EVENT.register(((player, world, hand, entity, hitResult) -> {
			NbtCompound persistentData = ((IEntityDataSaver) player).getPersistentData();
			String currentTool = player.getMainHandStack().getItem().toString();
			if(persistentData.getBoolean(currentTool)) {
				player.sendMessage(Text.literal("You are not strong enough to wield this item!"));
				return ActionResult.FAIL;
			}
			return ActionResult.PASS;
		}));

		UseBlockCallback.EVENT.register(((player, world, hand, hitResult) -> {
			NbtCompound persistentData = ((IEntityDataSaver) player).getPersistentData();
			String currentTool = player.getMainHandStack().getItem().toString();
			if(persistentData.getBoolean(currentTool)) {
				player.sendMessage(Text.literal("You do not have the expertise to use this item!"));
				return ActionResult.FAIL;
			}
			return ActionResult.PASS;
		}));
	}
}