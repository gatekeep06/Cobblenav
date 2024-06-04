package com.metacontent.cobblenav.util;

import com.google.gson.annotations.JsonAdapter;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PokenavContact {
    private final String key;
    @Nullable
    @JsonAdapter(GameProfile.Serializer.class)
    private GameProfile profile;
    @NotNull
    private String name;
    private String title;
    private int winnings;
    private int losses;
    private final List<ContactTeamMember> team;
    private final boolean trainer;

    public PokenavContact(String key, @Nullable GameProfile profile, @NotNull String name, String title, int winnings, int losses, List<ContactTeamMember> team, boolean trainer) {
        this.key = key;
        this.profile = profile;
        this.name = name;
        this.title = title;
        this.winnings = winnings;
        this.losses = losses;
        this.team = team;
        this.trainer = trainer;
    }

    public PokenavContact(String key, GameProfile profile, boolean trainer) {
        this(key, profile, profile.getName(), "", 0, 0, new ArrayList<>(), trainer);
    }

    public PokenavContact(String key, String name, boolean trainer) {
        this(key, null, name, "", 0, 0, new ArrayList<>(), trainer);
    }

    public String getTitleOrElseName() {
        return title.isEmpty() ? getName() : title;
    }

    public String getName() {
        return isTrainer() || profile == null ? name : profile.getName();
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public @Nullable GameProfile getProfile() {
        return profile;
    }

    public void setProfile(@Nullable GameProfile profile) {
        this.profile = profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWinnings() {
        return winnings;
    }

    public void updateWinnings() {
        winnings++;
    }

    public int getLosses() {
        return losses;
    }

    public void updateLosses() {
        losses++;
    }

    public List<ContactTeamMember> getTeam() {
        return team;
    }

    public boolean isTrainer() {
        return trainer;
    }

    public void saveToBuf(PacketByteBuf buf) {
        buf.writeString(key);
        buf.writeBoolean(trainer);
        if (trainer) {
            buf.writeString(name);
        }
        else {
            buf.writeGameProfile(profile);
        }
        buf.writeString(title);
        buf.writeInt(winnings);
        buf.writeInt(losses);
        buf.writeCollection(team, (buf1, member) -> member.saveToBuf(buf1));
    }

    public static PokenavContact fromBuf(PacketByteBuf buf) {
        String key = buf.readString();
        boolean trainer = buf.readBoolean();
        GameProfile profile = trainer ? null : buf.readGameProfile();
        String name = trainer ? buf.readString() : profile.getName();
        String title = buf.readString();
        int winnings = buf.readInt();
        int losses = buf.readInt();
        List<ContactTeamMember> team = buf.readList(ContactTeamMember::fromBuf);
        return new PokenavContact(key, profile, name, title, winnings, losses, team, trainer);
    }

    @Override
    public String toString() {
        return "PokenavContact{" +
                "key='" + key + '\'' +
                ", profile=" + profile +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", winnings=" + winnings +
                ", losses=" + losses +
                ", team=" + team +
                ", trainer=" + trainer +
                '}';
    }
}
