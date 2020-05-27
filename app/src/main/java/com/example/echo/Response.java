package com.example.echo;

import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
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
    private Random mRandom = new Random();


    private int mImage;

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

    // TODO extract info from database
    public Response(int emotion) {
        mId = UUID.randomUUID();
        mDate = new Date();
        mImage = R.drawable.happy_2;
        int idx = -1;
        if (emotion == Response.NEUTRAL) {
            mTitle = "No special feeling? Try this out:";
            idx = mRandom.nextInt(Database.db_image_neutral.length);
            mImage = Database.db_image_neutral[idx];
            mURL = Database.db_url_neutral[idx];
        } else if (emotion == Response.ANGER) {
            mTitle = "Feeling angry? Try this out:";
            idx = mRandom.nextInt(Database.db_image_angry.length);
            mImage = Database.db_image_angry[idx];
            mURL = Database.db_url_angry[idx];
        } else if (emotion == Response.FEAR) {
            mTitle = "Feeling fearful? Try this out:";
            idx = mRandom.nextInt(Database.db_image_fear.length);
            mImage = Database.db_image_fear[idx];
            mURL = Database.db_url_fear[idx];
        } else if (emotion == Response.JOY) {
            mTitle = "Feeling happy? Try this out:";
            idx = mRandom.nextInt(Database.db_image_happy.length);
            mImage = Database.db_image_happy[idx];
            mURL = Database.db_url_happy[idx];
        } else if (emotion == Response.SADNESS) {
            mTitle = "Feeling sad? Try this out:";
            idx = mRandom.nextInt(Database.db_image_sad.length);
            mImage = Database.db_image_sad[idx];
            mURL = Database.db_url_sad[idx];
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

    public int getImage() {
        return mImage;
    }

    public void setImage(int imageID) {
        mImage = imageID;
    }
}