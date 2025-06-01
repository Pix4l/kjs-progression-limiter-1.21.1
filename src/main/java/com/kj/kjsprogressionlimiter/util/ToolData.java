package com.kj.kjsprogressionlimiter.util;

import net.minecraft.nbt.NbtCompound;

public class ToolData {
    public static int addThirst(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.kjs_progression_limiter_1_21_1$getPersistentData();
        int thirst = nbt.getInt("thirst");
        thirst += amount;
        nbt.putInt("thirst", thirst);

        return thirst;
    }

    public static int removeThirst(IEntityDataSaver player, int amount) {
        NbtCompound nbt = player.kjs_progression_limiter_1_21_1$getPersistentData();
        int thirst = nbt.getInt("thirst");
        thirst -= amount;
        nbt.putInt("thirst", thirst);

        return thirst;
    }
}
