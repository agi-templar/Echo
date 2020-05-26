package com.example.echo;

import java.util.Date;
import java.util.UUID;

/**
 * Model: Response
 * attributes and necessary methods for Response class
 */
public class Response {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private String mURL;

    // Emotions
    public static int NEUTRAL = 0;
    public static int ANGER = 1;
    public static int FEAR = 2;
    public static int JOY = 3;
    public static int SADNESS = 4;

    public Response() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Response(int emotion) {
        mId = UUID.randomUUID();
        mDate = new Date();
        if (emotion == Response.NEUTRAL) {
            mTitle = "No special feeling? Try this out:";
            mURL = "https://web.cs.dartmouth.edu/";
        } else if (emotion == Response.ANGER) {
            mTitle = "Feeling angry? Try this out:";
            mURL = "https://web.cs.dartmouth.edu/";
        } else if (emotion == Response.FEAR) {
            mTitle = "Feeling fearful? Try this out:";
            mURL = "https://web.cs.dartmouth.edu/";
        } else if (emotion == Response.JOY) {
            mTitle = "Feeling happy? Try this out:";
            mURL = "https://web.cs.dartmouth.edu/";
        } else if (emotion == Response.SADNESS) {
            mTitle = "Feeling sad? Try this out:";
            mURL = "https://web.cs.dartmouth.edu/";
        }
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getURL() {
        return mURL;
    }

    public void setURL(String text) {
        mURL = text;
    }
}

