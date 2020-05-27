package com.azspc.azchat240.menu;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.azspc.azchat240.R;

import static com.azspc.azchat240.MainActivity.id_cheat;
import static com.azspc.azchat240.MainActivity.id_moder;
import static com.azspc.azchat240.MainActivity.isCheater;
import static com.azspc.azchat240.MainActivity.isModer;
import static com.azspc.azchat240.MainActivity.sp;

public class SettingsActivity extends Aztivity {
    final String code_moder = "D49I-M0D3-R4T0-R",
            code_cheater = "D49I-CH34-T3R4-S";

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
        if (input_code.equals(code_moder)) isModer = true;
        else if (input_code.equals(code_cheater)) isCheater = true;
        sp.edit().putBoolean(id_moder, isModer).apply();
        sp.edit().putBoolean(id_cheat, isCheater).apply();
    }

}
