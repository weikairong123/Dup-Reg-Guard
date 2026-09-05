package com.example.dupregguard.mixin;

import com.example.dupregguard.DupRegGuardMod;
import net.fabricmc.fabric.impl.resource.ResourceLoaderImpl;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.config.MixinConfigOption;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

@Mixin(value = ResourceLoaderImpl.class, remap = false)
@MixinConfigOption(classExists = "net.fabricmc.fabric.impl.resource.ResourceLoaderImpl")
public abstract class MixinResourceLoaderImpl {

    @Shadow
    private ResourceType type;

    @Shadow
    private Set<Identifier> registeredIds;

    @Inject(
            method = "registerReloadListener",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onNewRegisterReloadListener(Identifier id, ResourceReloader listener, CallbackInfo ci) {
        if (DupRegGuardMod.DISABLED) {
            return;
        }
        if (registeredIds.contains(id)) {
            DupRegGuardMod.LOGGER.warn("[Dup‑Reg‑Guard] Skipped duplicate(new‑api) reload listener id={}, resourceType={}", id, type);
            ci.cancel();
        }
    }
}

