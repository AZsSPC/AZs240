package com.azspc.azchat240;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.azspc.azchat240.MainActivity.id_moder;
import static com.azspc.azchat240.MainActivity.isModerator;
import static com.azspc.azchat240.MainActivity.sp;

public class SettingsActivity extends Aztivity {
    final String code = "CE4RF-NS-6KZNM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public void backToPosts(View v) {
        super.backToPosts(v);
    }

    public void saveModerCode(View v) {
        String input_code = ((EditText) findViewById(R.id.et_moder_code)).getText().toString();
        isModerator = input_code.equals(code);
        sp.edit().putBoolean(id_moder, isModerator).apply();
        if (isModerator) Toast.makeText(v.getContext(),
                "Режим модератора активирован",
                Toast.LENGTH_LONG).show();
        backToPosts(v);
    }

}
