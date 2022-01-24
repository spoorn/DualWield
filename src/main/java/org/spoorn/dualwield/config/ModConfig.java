package org.spoorn.dualwield.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import org.spoorn.dualwield.DualWield;

@Config(name = DualWield.MODID)
public class ModConfig implements ConfigData {

    @Comment("Set to false to disable Dual Wield enchantment book [default = true]")
    public boolean enableDualWieldEnchantment = true;

    @Comment("Control how much of the dual wielded offhand base weapon damage will apply per hit [0.0 for 0%, 1.0 for 100%, etc.] [default = 0.7]")
    public double dualWieldedOffHandDamageMultiplier = 0.7;

    @Comment("True to enable bonus damage from enchantments to dual wielded offhand weapon [default = true]")
    public boolean includeOffhandEnchantmentDamage = true;

    @Comment("Set to true to enable Dual Wielding for all weapons, regardless of enchantment [default = false]")
    public boolean creativeDualWield = false;

    public static void init() {
        AutoConfig.register(ModConfig.class, JanksonConfigSerializer::new);
    }

    public static ModConfig get() {
        return AutoConfig.getConfigHolder(ModConfig.class).getConfig();
    }
}
