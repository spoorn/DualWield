package org.spoorn.dualwield.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.spoorn.dualwield.config.ModConfig;

public class EnchantmentRegistry {

    public static Enchantment DUAL_WIELD_ENCHANT;


    public static void init() {
        // Do nothing but this is required because mod loader needs explicit code entrypoints to load for
        // client vs server
        if (ModConfig.get().enableDualWieldEnchantment) {
            DUAL_WIELD_ENCHANT = register(DualWieldEnchantment.DUAL_WIELD_ID, new DualWieldEnchantment());
        }
    }

    private static <T extends Enchantment> T register(Identifier identifier, T enchantment) {
        return Registry.register(Registries.ENCHANTMENT, identifier, enchantment);
    }
}
