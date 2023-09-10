package com.example.lab4;

import android.annotation.SuppressLint;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    GridLayout buttons;
    ImageButton nextBtn;
    ImageView image;
    TextView questionText;
    TextView resultText;
    String[] images;
    AlertDialog.Builder alertBuilder;
    TextView textLevel;
    int level;
    int wrong = 0;
    int levels = 1;
    int count;
    int num;
    int random;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextBtn = findViewById(R.id.nextBtn);
        image = findViewById(R.id.image);
        questionText = findViewById(R.id.questionText);
        textLevel = findViewById(R.id.level);
        resultText = findViewById(R.id.resultText);
        questionText.setText("Question " + levels + " Out of 10");
        resultText.setText("");
        nextBtn.setEnabled(false);
        buttons = findViewById(R.id.g);
        startingAlert();
        enabled(true);
    }

    private void enabled(boolean x) {
        for (int i = 0; i < buttons.getChildCount(); i++) {
            Button b = (Button) buttons.getChildAt(i);
            b.setEnabled(x);
            if (x) {
                b.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.purple_700));
            }
        }
    }

    @SuppressLint("SetTextI18n")
    public void clicking() {
        for (int i = 0; i < buttons.getChildCount(); i++) {
            Button b = (Button) buttons.getChildAt(i);
            b.setOnClickListener(v -> {
                String imageName = "png/" + images[random];
                if (b.getText().equals(getCountryName(imageName))) {
                    levels++;
                    resultText.setText(getCountryName(imageName) + "!");
                    b.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.green));
                    enabled(false);
                    b.setEnabled(true);
                    nextBtn.setEnabled(true);
                    nextBtn.setImageResource(R.mipmap.ic_next1);
                } else {
                    count--;
                    resultText.setText("InCorrect!");
                    wrong++;
                    b.setEnabled(false);
                    b.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.red));
                }
                if (levels > 10) {
                    nextBtn.setEnabled(false);
                    nextBtn.setImageResource(R.mipmap.ic_next);
                    endingAlert();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    public void next(View v) {
        enabled(true);
        questionText.setText("Question " + levels + " Out of 10");
        resultText.setText("");
        enabled(true);
        nextBtn.setEnabled(false);
        nextBtn.setImageResource(R.mipmap.ic_next);
        game();
    }

    @SuppressLint("SetTextI18n")
    public void game() {
        AssetManager assets = this.getAssets();
        try {
            images = assets.list("png");
            random = (int) Math.floor(Math.random() * images.length);
            String imageName = "png/" + images[random];
            Drawable im = Drawable.createFromStream(assets.open(imageName), imageName);
            image.setImageDrawable(im);
            int randomNumBtn = (int) Math.floor(Math.random() * buttons.getChildCount());
            Button btn = (Button) buttons.getChildAt(randomNumBtn);
            String countryName = getCountryName(imageName);
            btn.setText(countryName);

            for (int i = 0; i < buttons.getChildCount(); i++) {
                int r = (int) Math.floor(Math.random() * images.length);
                if (i != randomNumBtn) {
                    String n = "png/" + images[r];
                    Button btn1 = (Button) buttons.getChildAt(i);
                    btn1.setText(getCountryName(n));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        clicking();
    }

    @SuppressLint("SetTextI18n")
    public void endingAlert() {
        int percent = (count * 100) / num;
        String message = wrong + " wrong clicks, " + percent + "% are correct";
        alertBuilder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle(message);
        alertBuilder.setNegativeButton("Play Again", (dialog, which) -> {
            levels = 1;
            resultText.setText("");
            questionText.setText("Question " + levels + " Out of 10");
            wrong = 0;
            enabled(true);
            startingAlert();
        });
        alertBuilder.create().show();
    }

    public String getCountryName(@NonNull String x) {
        String c = x.split("/")[1];
        String country = c.split("-")[1];
        return "" + country.substring(0, country.length() - 4);
    }

    public void startingAlert() {
        alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(false)
                .setTitle("Choose a level")
                .setItems(new String[]{"level 1", "level 2", "level 3"}, (dialog, which) -> {
                    if (which == 0) {
                        level = 1;
                        count = 20;
                        num = 20;
                    } else if (which == 1) {
                        level = 2;
                        count = 50;
                        num = 50;
                    } else {
                        level = 3;
                        count = 80;
                        num = 80;
                    }
                    dialog.dismiss();
                    initialize();
                    game();
                });
        alertBuilder.create().show();
    }

    @SuppressLint("SetTextI18n")
    public void initialize() {
        buttons.removeAllViews();
        for (int i = 0; i < level * 3; i++) {
            Button btn = new Button(this);
            btn.setText("");
            btn.setBackgroundTintList(AppCompatResources.getColorStateList(getApplicationContext(), R.color.purple_700));
            btn.setTextSize(12);
            buttons.addView(btn);
            textLevel.setText("Level " + level);
        }
    }
}