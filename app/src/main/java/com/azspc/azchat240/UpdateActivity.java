package com.azspc.azchat240;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import static com.azspc.azchat240.MainActivity.id_url_update;
import static com.azspc.azchat240.MainActivity.sp;

public class UpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
    }

    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public void backToPosts(View v) {
        finish();
    }

    public void updateFromCloud(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sp.getString(id_url_update, "no url"))));
    }
}
