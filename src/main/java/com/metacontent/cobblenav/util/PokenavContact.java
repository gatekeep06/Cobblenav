package com.metacontent.cobblenav.util;

import java.util.ArrayList;
import java.util.List;

public class PokenavContact {
    private final String uuid;
    private String name;
    private String title;
    private int winnings;
    private int losses;
    private final List<ContactTeamMember> team;

    public PokenavContact(String uuid, String name, String title, int winnings, int losses, List<ContactTeamMember> team) {
        this.uuid = uuid;
        this.name = name;
        this.title = title;
        this.winnings = winnings;
        this.losses = losses;
        this.team = team;
    }

    public PokenavContact(String uuid, String name) {
        this(uuid, name, "", 0, 0, new ArrayList<>());
    }

    public String getTitleOrElseName() {
        return title.isEmpty() ? name : title;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "PokenavContact{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", winnings=" + winnings +
                ", losses=" + losses +
                ", team=" + team +
                '}';
    }
}
