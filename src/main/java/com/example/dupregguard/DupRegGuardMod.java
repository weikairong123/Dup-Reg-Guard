package com.example.dupregguard;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.resource.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DupRegGuardMod implements ModInitializer {
    public static final String MOD_ID = "dup‑reg‑guard";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // 系统属性：-Ddupregguard.disable=true 完全关闭本模组拦截逻辑
    public static final boolean DISABLED = Boolean.getBoolean("dupregguard.disable");

    // key: Reloader实例，value：已经注册过的ResourceType集合
    public static final Map<ResourceReloader, Set<ResourceType>> REGISTER_MAP =
            Collections.synchronizedMap(new HashMap<>());

    public static boolean isAlreadyRegistered(ResourceReloader reloader, ResourceType type) {
        if (DISABLED) return false;
        Set<ResourceType> typeSet = REGISTER_MAP.get(reloader);
        if (typeSet == null) return false;
        return typeSet.contains(type);
    }

    public static void markRegistered(ResourceReloader reloader, ResourceType type) {
        if (DISABLED) return;
        REGISTER_MAP.computeIfAbsent(reloader, k -> Collections.synchronizedSet(new HashSet<>())).add(type);
    }

    @Override
    public void onInitialize() {
        if (DISABLED) {
            LOGGER.info("Dup‑Reg‑Guard is DISABLED by system property");
            return;
        }
        LOGGER.info("Dup‑Reg‑Guard v1.2.1 loaded: guard static & instance ResourceReloader register");
    }
}

