package com.azspc.azchat240;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import static com.azspc.azchat240.MainActivity.isModerator;
import static com.azspc.azchat240.MainActivity.splitter;
import static com.azspc.azchat240.MainActivity.separator;

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
        String copiedText = et_tt.getText() + splitter + et_tx.getText() + splitter + type + separator;
        ((android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE))
                .setPrimaryClip(android.content.ClipData.newPlainText(
                        "Скопійовано в буфер обміну", copiedText.replaceAll("\n", "\\\\n")));
        if (isModerator) Toast.makeText(getBaseContext(),
                "Скопійовано в буфер обміну",
                Toast.LENGTH_LONG).show();
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
            case 0:
                if (isModerator) return R.color.t_system;
            default:
                return R.color.t_normal;
            case 2:
                return R.color.t_spy;
            case 3:
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
