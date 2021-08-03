package com.pequla.bot.listener.command.joke;

public class Joke {
    private int id;
    private String type;
    private String setup;
    private String punchline;

    public Joke() {
    }

    public Joke(int id, String type, String setup, String punchline) {
        this.id = id;
        this.type = type;
        this.setup = setup;
        this.punchline = punchline;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSetup() {
        return setup;
    }

    public String getPunchline() {
        return punchline;
    }
}
