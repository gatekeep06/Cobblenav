package com.metacontent.cobblenav.util;

import java.util.List;

public class PokenavContact {
    private final String name;
    private final String title;
    private final int winnings;
    private final int losses;
    private final List<String> team;

    public PokenavContact(String name, String title, int winnings, int losses, List<String> team) {
        this.name = name;
        this.title = title;
        this.winnings = winnings;
        this.losses = losses;
        this.team = team;
    }

    public int getWinnings() {
        return winnings;
    }

    public int getLosses() {
        return losses;
    }

    public List<String> getTeam() {
        return team;
    }

    @Override
    public String toString() {
        return "PokenavContact{" +
                "name='" + name + '\'' +
                ", winnings=" + winnings +
                ", losses=" + losses +
                ", team=" + team +
                '}';
    }
}
