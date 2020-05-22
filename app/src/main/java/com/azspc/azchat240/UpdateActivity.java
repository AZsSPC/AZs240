package com.azspc.azchat240;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import static com.azspc.azchat240.MainActivity.id_url_update;
import static com.azspc.azchat240.MainActivity.sp;

public class UpdateActivity extends Aztivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
    }

    @Override
    public void backToPosts(View v) {
        super.backToPosts(v);
    }

    public void updateFromCloud(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sp.getString(id_url_update, "no url"))));
    }
}
