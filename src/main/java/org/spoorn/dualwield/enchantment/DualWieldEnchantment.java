package org.spoorn.dualwield.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.util.Identifier;
import org.spoorn.dualwield.DualWield;

/**
 * Allows dual wielding of weapons.
 */
public class DualWieldEnchantment extends Enchantment {

    public static final Identifier DUAL_WIELD_ID = new Identifier(DualWield.MODID, "dualwield");

    public DualWieldEnchantment() {
        this(Rarity.VERY_RARE, EnchantmentTarget.WEAPON, new EquipmentSlot[] {EquipmentSlot.MAINHAND});
    }

    public DualWieldEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot[] slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public int getMinPower(int level) {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
