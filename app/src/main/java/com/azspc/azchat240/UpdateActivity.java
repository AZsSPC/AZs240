package com.azspc.azchat240;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

import static com.azspc.azchat240.MainActivity.id_url_update;
import static com.azspc.azchat240.MainActivity.id_v_info;
import static com.azspc.azchat240.MainActivity.id_version;
import static com.azspc.azchat240.MainActivity.sp;
import static com.azspc.azchat240.MainActivity.version;

public class UpdateActivity extends Aztivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        TextView labe = findViewById(R.id.info_labe);
        labe.setText(getText(R.string.update).toString()
                .replaceAll("%vInf%", sp.getString(id_v_info, "/lost data/"))
                .replaceAll("%vSec%", version)
                .replaceAll("%vNew%", sp.getString(id_version, "/lost version/"))
                .replaceAll("%n", "\n"));
    }

    @Override
    public void backToPosts(View v) {
        super.backToPosts(v);
    }

    public void updateFromCloud(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sp.getString(id_url_update, "no url"))));
    }

    public void copyUrlToLoad(View v) {
        ((android.content.ClipboardManager) Objects.requireNonNull(getSystemService(Context.CLIPBOARD_SERVICE)))
                .setPrimaryClip(android.content.ClipData.newPlainText(
                        "Скопійовано в буфер обміну", sp.getString(id_url_update, "no url")));
    }
}
