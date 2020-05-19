package com.azspc.azchat240;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.azspc.azchat240.MainActivity.isModerator;
import static com.azspc.azchat240.MainActivity.moder_id;
import static com.azspc.azchat240.MainActivity.sp;

public class SettingsActivity extends AppCompatActivity {
    final String code = "CE4RF-NS-6KZNM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public void saveModerCode(View v) {
        String input_code = ((EditText) findViewById(R.id.et_moder_code)).getText().toString();
        isModerator = input_code.equals(code);
        sp.edit().putBoolean(moder_id, isModerator).apply();
        if (isModerator) Toast.makeText(v.getContext(),
                "Режим модератора активирован",
                Toast.LENGTH_LONG).show();
        backToPosts(v);
    }

    public void backToPosts(View v) {
        finish();
    }
}
