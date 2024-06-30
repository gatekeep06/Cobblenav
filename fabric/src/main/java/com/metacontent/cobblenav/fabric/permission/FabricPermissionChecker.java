package com.metacontent.cobblenav.fabric.permission;

import com.metacontent.cobblenav.permission.PermissionChecker;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.network.ServerPlayerEntity;

public class FabricPermissionChecker implements PermissionChecker {
    @Override
    public boolean check(ServerPlayerEntity player, String permission) {
        return Permissions.check(player, permission);
    }
}
