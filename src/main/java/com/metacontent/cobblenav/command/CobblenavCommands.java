package com.metacontent.cobblenav.command;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class CobblenavCommands {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(SetPlayerTitleCommand::register);
        CommandRegistrationCallback.EVENT.register(GetContactDataCommand::register);
        CommandRegistrationCallback.EVENT.register(ClearContactDataCommand::register);
        CommandRegistrationCallback.EVENT.register(AddContactCommand::register);
        CommandRegistrationCallback.EVENT.register(GiveCustomPokenavCommand::register);
        CommandRegistrationCallback.EVENT.register(CheckConfigCommand::register);
    }
}
