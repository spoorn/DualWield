package org.spoorn.dualwield;

import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.ModInitializer;
import org.spoorn.dualwield.config.ModConfig;
import org.spoorn.dualwield.enchantment.EnchantmentRegistry;

@Log4j2
public class DualWield implements ModInitializer {

    public static final String MODID = "dualwield";

    @Override
    public void onInitialize() {
        log.info("Hello from Spoorn's DualWield!");

        // Config
        ModConfig.init();

        // Enchantments
        EnchantmentRegistry.init();
    }
}
