package com.example.dupregguard.mixin;

import com.example.dupregguard.DupRegGuardMod;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ResourceManagerHelper.class, remap = false)
public abstract class MixinResourceManagerHelper {

    @Inject(
            method = "registerReloader(Lnet/minecraft/resource/ResourceReloader;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void onStaticRegisterReloader(ResourceReloader reloader, CallbackInfo ci) {
        if (DupRegGuardMod.DISABLED) {
            return;
        }
        if (reloader == null) return;
        ResourceType targetType = ResourceType.CLIENT_RESOURCES;

        if (DupRegGuardMod.isAlreadyRegistered(reloader, targetType)) {
            DupRegGuardMod.LOGGER.warn("[Dup‑Reg‑Guard] Skipped duplicate(static) ResourceReloader type={}, class={}",
                    targetType, reloader.getClass().getName());
            ci.cancel();
            return;
        }
        DupRegGuardMod.markRegistered(reloader, targetType);
    }
}

