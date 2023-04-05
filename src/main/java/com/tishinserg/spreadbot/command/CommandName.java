package com.tishinserg.spreadbot.command;

public enum CommandName {

    START("/start"),
    STOP("/stop"),
    HELP("/help"),
    NO(" "),
    STAT("/stat"),
    UNI("/uni"),
    SUB("/subscribe"),
    UN_SUB("/unsubscribe"),
    SUBSCRIPTIONS("/grouplist");


    private final String commandName;

    CommandName(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }
}
