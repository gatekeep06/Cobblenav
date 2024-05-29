package com.metacontent.cobblenav.config.util;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.network.ServerPlayerEntity;

public record Badge(
        String type,
        String permissionToGrant
) {
    public boolean hasPermissionToGrant(ServerPlayerEntity player) {
        return Permissions.check(player, permissionToGrant);
    }
}
