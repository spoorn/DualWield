package org.spoorn.dualwield.mixin;

import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spoorn.dualwield.util.DualWieldUtil;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> {

    private int counter = 0;
    private boolean firstInvocation = true;

    @Shadow protected abstract void method_29353(T livingEntity, float f);

    @Shadow protected abstract Arm getPreferredArm(T entity);

    /**
     * Hijack this method_29353() that renders the arm swings in Third person.  Render both arm swings if the player is
     * holding 2 weapons with Dual Wielding.
     */
    @Inject(method = "method_29353", at = @At(value = "HEAD"), cancellable = true)
    public void renderThirdPersonModelForBothArms(T livingEntity, float f, CallbackInfo ci) {
        if (firstInvocation && shouldRenderBothArmSwing(livingEntity)) {
            firstInvocation = false;
            method_29353(livingEntity, f);
            method_29353(livingEntity, f);
            firstInvocation = true;
            ci.cancel();
        }
    }

    @Redirect(method = "method_29353", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;getPreferredArm(Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/util/Arm;"))
    public Arm overrideArm(BipedEntityModel bipedEntityModel, T entity) {
        if (shouldRenderBothArmSwing(entity)) {
            if (this.counter == 0) {
                this.counter++;
                return Arm.LEFT;
            } else {
                this.counter = 0;
                return Arm.RIGHT;
            }
        } else {
            return this.getPreferredArm(entity);
        }
    }

    // Only render both arms swings if this is for a player and we're swinging
    private boolean shouldRenderBothArmSwing(T entity) {
        if (entity instanceof PlayerEntity && !(((BipedEntityModel)(Object)this).handSwingProgress <= 0.0F)) {
            ItemStack mainStack = entity.getMainHandStack();
            ItemStack offStack = entity.getOffHandStack();
            return DualWieldUtil.isDualWieldable(mainStack, offStack);
        }

        return false;
    }
}
