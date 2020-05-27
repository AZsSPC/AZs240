package com.azspc.azchat240.cheat;


import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azspc.azchat240.Post;
import com.azspc.azchat240.R;
import com.azspc.azchat240.menu.Aztivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import static com.azspc.azchat240.MainActivity.id_url_posts;
import static com.azspc.azchat240.MainActivity.isCheater;
import static com.azspc.azchat240.MainActivity.isModer;
import static com.azspc.azchat240.MainActivity.savePost;
import static com.azspc.azchat240.MainActivity.separator;
import static com.azspc.azchat240.MainActivity.sp;
import static com.azspc.azchat240.MainActivity.splitter;

public class CheatActivity extends Aztivity {
    private RecyclerView cheatRecView;
    boolean isVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);
        cheatRecView = findViewById(R.id.cheat_list);
        cheatRecView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void hideAll(View v) {
        setVisible(isVisible = !isVisible);
    }

    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
        cheatRecView.setAdapter(new CheatAdapter(getBaseContext(), getInitialData()));
    }


    public String[] getFromCloud(String url, String dName) {
        try {
            ReadableByteChannel rbc = Channels.newChannel(new URL(url).openStream());
            FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), dName));
            fos.getChannel().transferFrom(rbc, 0, Build.VERSION.SDK_INT > 23 ? Long.MAX_VALUE : 8 * 1024);
            fos.close();
            rbc.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),
                    "Плохое подключение к и-нету, будут показаны лишь загруженные посты.",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader bread = new BufferedReader(new InputStreamReader(openFileInput(dName)));
            while ((line = bread.readLine()) != null) sb.append(line).append(separator);
            return sb.toString().split(separator);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{"no data"};
    }

    private ArrayList<Cheat> getInitialData() {
        ArrayList<Cheat> ret = new ArrayList<>();
        String[] posts = getFromCloud(sp.getString(id_url_posts, "no url"), savePost);
        for (String post : posts)
            try {
                String[] d = post.replaceAll("\\\\n", "\n").split(splitter);
                ret.add(0, new Cheat(d[0], d[1]));
            } catch (Exception e) {
                e.printStackTrace();
            }

        return ret;
    }
}
