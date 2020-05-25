package com.example.echo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {
    TextView txTitle;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        txTitle = (TextView)findViewById(R.id.note_title);
       editText = (EditText)findViewById(R.id.note_text);
        Typeface titleFont = Typeface.createFromAsset(getAssets(), "font/title.ttf");
        Typeface textFont = Typeface.createFromAsset(getAssets(), "font/text.ttf");
        txTitle.setTypeface(titleFont);
        editText.setTypeface(textFont);
    }

    public void onEchoClicked(View v) {
        String text = editText.getText().toString();
    }
}
