package com.metacontent.cobblenav.permission;

import net.minecraft.server.network.ServerPlayerEntity;

public class PermissionHandler {
    private PermissionChecker checker;

    public boolean check(ServerPlayerEntity player, String permission) {
        if (checker != null) {
            return checker.check(player, permission);
        }
        return false;
    }

    public void setChecker(PermissionChecker checker) {
        this.checker = checker;
    }
}
