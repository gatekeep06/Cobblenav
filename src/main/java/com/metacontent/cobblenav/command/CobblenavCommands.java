package com.metacontent.cobblenav.command;

import com.metacontent.cobblenav.Cobblenav;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CobblenavCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(SetPlayerTitleCommand::register);
        CommandRegistrationCallback.EVENT.register(GetContactDataCommand::register);
        CommandRegistrationCallback.EVENT.register(ClearContactDataCommand::register);
        CommandRegistrationCallback.EVENT.register(AddContactCommand::register);
        CommandRegistrationCallback.EVENT.register(GiveCustomPokenavCommand::register);
        CommandRegistrationCallback.EVENT.register(CheckConfigCommand::register);
        CommandRegistrationCallback.EVENT.register(GrantBadgeCommand::register);
        CommandRegistrationCallback.EVENT.register(OpenAllPokemonScreenCommand::register);
        if (Cobblenav.CONFIG.enableDebugMode) {
            CommandRegistrationCallback.EVENT.register(AddDebugingContactsCommand::register);
        }
    }
}
