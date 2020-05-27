package com.azspc.azchat240.menu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static com.azspc.azchat240.MainActivity.id_url_update;
import static com.azspc.azchat240.MainActivity.sp;

@SuppressLint("Registered")
public class Aztivity extends AppCompatActivity {
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public void backToPosts(View v) {
        finish();
    }

}
