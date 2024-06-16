package com.metacontent.cobblenav.util;

import net.minecraft.network.PacketByteBuf;

import java.util.Date;
import java.util.Objects;

public record GrantedBadge(String type, String grantedBy, Date grantDate) {
    public void saveToBuf(PacketByteBuf buf) {
        buf.writeString(type);
        buf.writeString(grantedBy);
        buf.writeDate(grantDate);
    }

    public static GrantedBadge fromBuf(PacketByteBuf buf) {
        String type = buf.readString();
        String grantedBy = buf.readString();
        Date grantDate = buf.readDate();
        return new GrantedBadge(type, grantedBy, grantDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GrantedBadge that)) return false;
        return Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
