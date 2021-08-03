package com.pequla.bot.listener.command.meme;

import java.util.List;

public class Meme {
    private String postLink;
    private String subreddit;
    private String title;
    private String url;
    private boolean nsfw;
    private boolean spoiler;
    private String author;
    private int ups;
    private List<String> preview;

    public Meme() {
    }

    public Meme(String postLink,
                String subreddit,
                String title,
                String url,
                boolean nsfw,
                boolean spoiler,
                String author,
                int ups,
                List<String> preview) {
        this.postLink = postLink;
        this.subreddit = subreddit;
        this.title = title;
        this.url = url;
        this.nsfw = nsfw;
        this.spoiler = spoiler;
        this.author = author;
        this.ups = ups;
        this.preview = preview;
    }

    public String getPostLink() {
        return postLink;
    }

    public String getSubreddit() {
        return subreddit;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public boolean isSpoiler() {
        return spoiler;
    }

    public String getAuthor() {
        return author;
    }

    public int getUps() {
        return ups;
    }

    public List<String> getPreview() {
        return preview;
    }
}
