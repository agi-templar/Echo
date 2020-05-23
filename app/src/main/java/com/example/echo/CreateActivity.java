package com.example.echo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class CreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        TextView txTitle = (TextView)findViewById(R.id.note_title);
        TextView txText = (TextView)findViewById(R.id.note_text);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "font/title.ttf");
        Typeface textFont = Typeface.createFromAsset(getAssets(), "font/text.ttf");
        txTitle.setTypeface(titleFont);
        txText.setTypeface(textFont);
    }
}
