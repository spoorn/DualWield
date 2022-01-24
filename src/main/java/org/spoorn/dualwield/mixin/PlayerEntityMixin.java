package org.spoorn.dualwield.mixin;

import com.google.common.collect.Multimap;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spoorn.dualwield.config.ModConfig;
import org.spoorn.dualwield.util.DualWieldUtil;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    private static final Logger log = LogManager.getLogger("DualWieldPlayerEntityMixin");

    // Fetch data from NBT and apply damage modifiers
    @ModifyVariable(method = "attack", ordinal = 0, at = @At(value = "STORE", ordinal = 0))
    public float modifyDualWieldDamage(float f, Entity target) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        float modifiedDamage = f;
        if (ModConfig.get().dualWieldedOffHandDamageMultiplier != 0.0 && player instanceof ServerPlayerEntity) {
            World world = player.getEntityWorld();
            if (!world.isClient()) {
                boolean isLivingTarget = DualWieldUtil.isLivingEntity(target);
                if (isLivingTarget) {
                    // Dual Wield damage
                    Item offHandItem = player.getOffHandStack().getItem();
                    if (DualWieldUtil.isDualWieldable(player.getMainHandStack(), player.getOffHandStack())) {
                        // At minimum, add base damage of offhand weapon
                        float offHandDamage = getBaseDamage(player, player.getOffHandStack(), offHandItem, false);
                        //log.info("### off hand damage: " + offHandDamage);
                        modifiedDamage += (offHandDamage * ModConfig.get().dualWieldedOffHandDamageMultiplier);
                    }
                }
            }
        }
        //log.info("final modified damage: " + modifiedDamage);
        return modifiedDamage;
    }

    // Gets base damage
    private static float getBaseDamage(PlayerEntity player, ItemStack stack, Item item, boolean includePlayerDamage) {
        AtomicReference<Float> damage = new AtomicReference<>(0.0f);
        Multimap<EntityAttribute, EntityAttributeModifier> attributeModifiers =
                        item.getAttributeModifiers(EquipmentSlot.MAINHAND);
        if (attributeModifiers.containsKey(EntityAttributes.GENERIC_ATTACK_DAMAGE)) {
                attributeModifiers.get(EntityAttributes.GENERIC_ATTACK_DAMAGE)
                                .forEach((entityAttributeModifier -> {
                                damage.updateAndGet(v -> new Float((float)(v + entityAttributeModifier.getValue())));
                                //log.error("### updating with={}", entityAttributeModifier.getValue());
                                    }));
            }

        float updatedDamage = damage.get();
        // Damage shown on tooltip is player + item + enchantments damage.  Got this from ItemStack.java
        if (includePlayerDamage) {
            float playerDamage = (float) player.getAttributeBaseValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            updatedDamage += playerDamage;
        }

        if (ModConfig.get().includeOffhandEnchantmentDamage) {
            float enchantmentDamage = EnchantmentHelper.getAttackDamage(stack, EntityGroup.DEFAULT);
            //log.info("### enchantment damage: " + enchantmentDamage);
            updatedDamage += enchantmentDamage;
        }
        return updatedDamage;
    }
}
