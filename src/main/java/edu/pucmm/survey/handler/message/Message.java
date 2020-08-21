package edu.pucmm.survey.handler.message;

import edu.pucmm.survey.handler.command.Command;

public class Message {

    private final MessageType type;
    private final Command command;

    public Message(MessageType type, Command command) {
        this.type = type;
        this.command = command;
    }

    public MessageType getType() {
        return type;
    }

    public Command getCommand() {
        return command;
    }
}