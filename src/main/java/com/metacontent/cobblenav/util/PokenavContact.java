package com.metacontent.cobblenav.util;

import com.google.gson.annotations.JsonAdapter;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public class PokenavContact {
    private final String key;
    @JsonAdapter(GameProfile.Serializer.class)
    private GameProfile profile;
    private String title;
    private int winnings;
    private int losses;
    private final List<ContactTeamMember> team;
    private final boolean trainer;

    public PokenavContact(String key, GameProfile profile, String title, int winnings, int losses, List<ContactTeamMember> team, boolean trainer) {
        this.key = key;
        this.profile = profile;
        this.title = title;
        this.winnings = winnings;
        this.losses = losses;
        this.team = team;
        this.trainer = trainer;
    }

    public PokenavContact(String key, GameProfile profile, boolean trainer) {
        this(key, profile, "", 0, 0, new ArrayList<>(), trainer);
    }

    public String getTitleOrElseName() {
        return title.isEmpty() ? profile.getName() : title;
    }

    public String getKey() {
        return key;
    }

    public GameProfile getProfile() {
        return profile;
    }

    public void setProfile(GameProfile profile) {
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
        buf.writeGameProfile(profile);
        buf.writeString(title);
        buf.writeInt(winnings);
        buf.writeInt(losses);
        buf.writeCollection(team, (buf1, member) -> member.saveToBuf(buf1));
        buf.writeBoolean(trainer);
    }

    public static PokenavContact fromBuf(PacketByteBuf buf) {
        String key = buf.readString();
        GameProfile profile = buf.readGameProfile();
        String title = buf.readString();
        int winnings = buf.readInt();
        int losses = buf.readInt();
        List<ContactTeamMember> team = buf.readList(ContactTeamMember::fromBuf);
        boolean trainer = buf.readBoolean();
        return new PokenavContact(key, profile, title, winnings, losses, team, trainer);
    }

    @Override
    public String toString() {
        return "PokenavContact{" +
                "key='" + key + '\'' +
                ", profile=" + profile +
                ", title='" + title + '\'' +
                ", winnings=" + winnings +
                ", losses=" + losses +
                ", team=" + team +
                ", trainer=" + trainer +
                '}';
    }
}
