package com.metacontent.cobblenav.permission;

import net.minecraft.server.network.ServerPlayerEntity;

public interface PermissionChecker {
    boolean check(ServerPlayerEntity player, String permission);
}
