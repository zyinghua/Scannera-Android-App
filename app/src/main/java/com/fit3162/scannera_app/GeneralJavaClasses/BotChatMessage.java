package com.fit3162.scannera_app.GeneralJavaClasses;

public class BotChatMessage {
    private String message;
    private boolean isFromBot;

    public BotChatMessage(String message, boolean isFromBot) {
        this.message = message;
        this.isFromBot = isFromBot;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isFromBot() {
        return isFromBot;
    }

    public void setFromBot(boolean fromBot) {
        isFromBot = fromBot;
    }
}
