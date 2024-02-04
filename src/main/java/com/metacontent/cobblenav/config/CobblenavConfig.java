package com.metacontent.cobblenav.config;

import com.metacontent.cobblenav.Cobblenav;

public class CobblenavConfig {
    public static boolean DISPLAY_TEAM_WIDGET;
    public static int CHECK_SPAWNS_WIDTH;
    public static int CHECK_SPAWNS_HEIGHT;
    public static int FINDING_WIDTH;
    public static int FINDING_HEIGHT;
    public static String[] IGNORED_LABELS;

    public static void initConfig() {
        CobblenavConfigProvider provider = new CobblenavConfigProvider();
        createProvider(provider);
        SimpleConfig config = SimpleConfig.of(Cobblenav.ID + "config").provider(provider).request();
        assignParameters(config);
    }

    private static void createProvider(CobblenavConfigProvider provider) {
        provider.addParameter("displayTeamWidget", true,
                "Boolean (true/false), determines whether the team widget will be displayed on the main screen");
        provider.addParameter("checkSpawnsSizing.width", -1,
                "Integer, determines the size of the check area (-1 to use the same size as the command)");
        provider.addParameter("checkSpawnsSizing.height", -1,
                "Integer, determines the size of the check area (-1 to use the same size as the command)");
        provider.addParameter("findingSizing.width", 100,
                "Integer, Determines the size of the finding area");
        provider.addParameter("findingSizing.height", 100,
                "Integer, Determines the size of the finding area");
        provider.addParameter("ignoredPokemonLabels", "not_modeled",
                "String array, Determines which pokemon labels will be ignored when spawn checks are run");
    }

    private static void assignParameters(SimpleConfig config) {
        DISPLAY_TEAM_WIDGET = config.getOrDefault("displayTeamWidget", true);
        CHECK_SPAWNS_WIDTH = config.getOrDefault("checkSpawnsSizing.width", -1);
        CHECK_SPAWNS_HEIGHT = config.getOrDefault("checkSpawnsSizing.height", -1);
        FINDING_WIDTH = config.getOrDefault("findingSizing.width", 100);
        FINDING_HEIGHT = config.getOrDefault("findingSizing.height", 100);
        IGNORED_LABELS = config.getOrDefault("ignoredPokemonLabels", "not_modeled").replaceAll(" ", "").split(",");
    }
}
