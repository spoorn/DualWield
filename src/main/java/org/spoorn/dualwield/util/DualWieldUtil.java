package org.spoorn.dualwield.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spoorn.dualwield.config.ModConfig;
import org.spoorn.dualwield.enchantment.EnchantmentRegistry;

public class DualWieldUtil {

    public static boolean hasDualWieldEnchantment(ItemStack stack) {
        return EnchantmentRegistry.DUAL_WIELD_ENCHANT != null && EnchantmentHelper.get(stack).containsKey(EnchantmentRegistry.DUAL_WIELD_ENCHANT);
    }

    public static boolean isDualWieldable(ItemStack stack) {
        return ModConfig.get().creativeDualWield || hasDualWieldEnchantment(stack);
    }

    /**
     * Checks if both item stacks are dual wieldable.
     */
    public static boolean isDualWieldable(ItemStack stack1, ItemStack stack2) {
        return isDualWieldable(stack1) && isDualWieldable(stack2);
    }

    /**
     * This is needed instead of {@link Entity#isLiving()} because isLiving() is client side only.
     */
    public static boolean isLivingEntity(Entity entity) {
        return entity instanceof LivingEntity;
    }
}
