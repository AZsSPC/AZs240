package com.azspc.azchat240;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

import static com.azspc.azchat240.MainActivity.sep_part;
import static com.azspc.azchat240.MainActivity.sep_post;

public class CreatePostActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {
    int type = 0;
    EditText et_tt, et_tx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        et_tt = findViewById(R.id.c_title);
        et_tx = findViewById(R.id.c_text);
        SeekBar sb = findViewById(R.id.c_type);
        sb.setOnSeekBarChangeListener(this);
    }

    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    public void copyToBuffer(View v) {
        String copiedText = et_tt.getText() + sep_part + et_tx.getText() + sep_part + type + sep_post;
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(android.content.ClipData.newPlainText("Скопійовано в буфер обміну", copiedText));
    }

    public void backToPosts(View v) {
        finish();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        type = progress;
        et_tt.setBackgroundColor(getResources().getColor(getTypeColor(type)));
    }

    int getTypeColor(int type) {
        switch (type) {
            default:
                return R.color.t_normal;
            case 1:
                return R.color.t_spy;
            case 2:
                return R.color.t_alarm;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }
}
