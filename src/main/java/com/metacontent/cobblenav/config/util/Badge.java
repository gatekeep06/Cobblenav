package com.metacontent.cobblenav.config.util;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Objects;

public record Badge(
        String type,
        String permissionToGrant,
        String trainer
) {
    public boolean hasPermissionToGrant(ServerPlayerEntity player) {
        if (permissionToGrant == null || permissionToGrant.isEmpty()) {
            return player.hasPermissionLevel(2);
        }
        return Permissions.check(player, permissionToGrant);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Badge badge)) return false;
        return Objects.equals(type, badge.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
