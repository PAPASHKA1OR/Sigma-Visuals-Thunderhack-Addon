package com.example.mixins;

import com.example.modules.ExampleModule;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    long time = System.currentTimeMillis();
    @Inject(method = "jump", at = @At("RETURN"), cancellable = true)
    public void jump(CallbackInfo ci) {
        if ((Object) this == MinecraftClient.getInstance().player && System.currentTimeMillis() - time > 100) {
            ExampleModule.circles.add(new ExampleModule.Circle(MinecraftClient.getInstance().player.getPos()));
        }
    }
}
