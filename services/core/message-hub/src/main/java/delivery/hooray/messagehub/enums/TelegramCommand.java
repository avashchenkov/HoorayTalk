package delivery.hooray.messagehub.enums;

import java.util.Optional;

public enum TelegramCommand {
    START("/start"),
    NEWORDER("/neworder");

    private final String command;

    TelegramCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static Optional<TelegramCommand> fromString(String text) {
        for (TelegramCommand telegramCommand : TelegramCommand.values()) {
            if (telegramCommand.command.equals(text)) {
                return Optional.of(telegramCommand);
            }
        }

        return Optional.empty();
    }
}
