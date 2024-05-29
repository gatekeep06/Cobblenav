package com.metacontent.cobblenav.config.util;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Badges {
    private final List<Badge> badges;

    public Badges(List<Badge> badges) {
        this.badges = badges;
    }

    public Badges() {
        this.badges = new ArrayList<>();
    }

    @Nullable
    public Badge getByType(String type) {
        return badges.stream().filter(badge -> badge.type().equals(type)).findFirst().orElse(null);
    }

    @Nullable
    public Badge getByPermission(String permission) {
        return badges.stream().filter(badge -> badge.permissionToGrant().equals(permission)).findFirst().orElse(null);
    }

    public List<String> getTypes() {
        return badges.stream().map(Badge::type).toList();
    }

    public List<String> getPermissions() {
        return badges.stream().map(Badge::permissionToGrant).toList();
    }
}
