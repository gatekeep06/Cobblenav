package com.metacontent.cobblenav.util;

import java.util.List;

public class PokenavContact {
    private final String uuid;
    private final String name;
    private final String title;
    private final int winnings;
    private final int losses;
    private final List<ContactTeamMember> team;

    public PokenavContact(String uuid, String name, String title, int winnings, int losses, List<ContactTeamMember> team) {
        this.uuid = uuid;
        this.name = name;
        this.title = title;
        this.winnings = winnings;
        this.losses = losses;
        this.team = team;
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

    public String getTitle() {
        return title;
    }

    public int getWinnings() {
        return winnings;
    }

    public int getLosses() {
        return losses;
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
