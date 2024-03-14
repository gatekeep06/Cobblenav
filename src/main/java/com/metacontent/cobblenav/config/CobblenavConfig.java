package com.metacontent.cobblenav.config;

import com.metacontent.cobblenav.Cobblenav;

public class CobblenavConfig {
    public static int MAIN_MENU_WIDGET;
    public static int CHECK_SPAWNS_WIDTH;
    public static int CHECK_SPAWNS_HEIGHT;
    public static int FINDING_WIDTH;
    public static int FINDING_HEIGHT;
    public static String[] IGNORED_LABELS;
    public static int TITLE_COMMANDS_PERMISSION_LEVEL;
    public static int TRACK_ARROW_VERTICAL_POSITION;

    public static void initConfig() {
        CobblenavConfigProvider provider = new CobblenavConfigProvider();
        createProvider(provider);
        SimpleConfig config = SimpleConfig.of("cobblenav/" + Cobblenav.ID + "-config").provider(provider).request();
        assignParameters(config);
    }

    private static void createProvider(CobblenavConfigProvider provider) {
        provider.addParameter("mainMenuWidget", 1,
                "Integer between 0 and 2, determines which widget will be on the main screen (so far from the options only none - 0 - and player's team - 1)");
        provider.addParameter("checkSpawnsSizing.width", -1,
                "Integer, determines the size of the check area (-1 to use the same size as the command)");
        provider.addParameter("checkSpawnsSizing.height", -1,
                "Integer, determines the size of the check area (-1 to use the same size as the command)");
        provider.addParameter("findingSizing.width", 100,
                "Integer, Determines the size of the finding area");
        provider.addParameter("findingSizing.height", 100,
                "Integer, Determines the size of the finding area");
        provider.addParameter("ignoredPokemonLabels", "not_modeled",
                "String array, Determines which pokemon labels will be ignored when spawn checks are run, list separated by commas");
        provider.addParameter("titleCommandsPermissionLevel", 2,
                "Integer between 0 and 4, Determines the permission level for title commands, google for more info");
        provider.addParameter("trackArrowVerticalPosition", 70,
                "Integer, Determines the height at which the track arrow will be positioned");
    }

    private static void assignParameters(SimpleConfig config) {
        MAIN_MENU_WIDGET = config.getOrDefault("mainMenuWidget", 1);
        CHECK_SPAWNS_WIDTH = config.getOrDefault("checkSpawnsSizing.width", -1);
        CHECK_SPAWNS_HEIGHT = config.getOrDefault("checkSpawnsSizing.height", -1);
        FINDING_WIDTH = config.getOrDefault("findingSizing.width", 100);
        FINDING_HEIGHT = config.getOrDefault("findingSizing.height", 100);
        IGNORED_LABELS = config.getOrDefault("ignoredPokemonLabels", "not_modeled").replaceAll(" ", "").split(",");
        TITLE_COMMANDS_PERMISSION_LEVEL = config.getOrDefault("titleCommandsPermissionLevel", 2);
        TRACK_ARROW_VERTICAL_POSITION = config.getOrDefault("trackArrowVerticalPosition", 70);
    }
}
