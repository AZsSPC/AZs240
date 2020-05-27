package com.azspc.azchat240;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends MenuLogic {
    public static String
            savePost = "posts",           //CHANGEABLE
            saveData = "data",            //CHANGEABLE
            saveCheat = "cheat",            //CHANGEABLE
            version = "4.3",              //CHANGEABLE
            separator = "║",              //FINAL
            splitter = "│",               //FINAL
            id_moder = "isModer",         //FINAL
            id_url_posts = "urlPost",     //FINAL
            id_url_update = "urlUp",      //FINAL
            id_version = "version",       //FINAL
            id_v_info = "vInfo",          //FINAL
            id_cheat = "isCheat",         //FINAL
            data_load_url =               //FINAL
                    "https://raw.githubusercontent.com/AZsSPC/AZs240/master/usable/data_az.txt";
    public static boolean
            isModer = false,
            isCheater = false,
            isMenuVisible = false;
    public static SharedPreferences sp;
    private RecyclerView postRecView;

    protected void onResume() {
        super.onResume();
        findViewById(R.id.cheat_fab).setVisibility((isCheater =
                sp.getBoolean(id_cheat, false)) ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.version_fab).setVisibility((isModer =
                sp.getBoolean(id_moder, false)) ? View.VISIBLE : View.INVISIBLE);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        reloadPosts(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        postRecView = findViewById(R.id.post_list);
        postRecView.setLayoutManager(new LinearLayoutManager(this));
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        firstRead();
    }

    public void firstRead() {
        try {
            ArrayList<String> arr = new ArrayList<>(Arrays.asList(getFromCloud(data_load_url, saveData)));
            SharedPreferences.Editor ed = sp.edit();
            for (String s : arr)
                ed.putString(s.split(splitter)[0], s.split(splitter)[1]);
            ed.apply();
            reloadPosts(null);
            if (!version.equals(sp.getString(id_version, version))) {
                Toast.makeText(getBaseContext(), "" +
                                "Установленная версия устарела.\n" +
                                "Обновите приложение до новой версии.\n" +
                                "С (" + version + ") до (" + sp.getString(id_version, "error") + ")",
                        Toast.LENGTH_LONG).show();
                versionInfo(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                    "Погане підключення до інтернету, неможливо оновити пости.",
                    Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),
                    e + "",
                    Toast.LENGTH_SHORT).show();
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


    public void reloadPosts(View v) {
        hideMenu(findViewById(R.id.fab_menu_0));
        postRecView.setAdapter(new PostAdapter(getBaseContext(), getInitialData()));
    }

    private ArrayList<Post> getInitialData() {
        ArrayList<Post> ret = new ArrayList<>();
        String[] posts = getFromCloud(sp.getString(id_url_posts, "no url"), savePost);
        int i = 0;
        int i1 = 0;
        int i2 = 0;
        for (String post : posts) {
            try {
                String[] d = post.split(splitter);
                ret.add(0, new Post(getResources(), d[0], d[1].replaceAll("%n", "\n"), d[2]));
                i1++;
            } catch (Exception e) {
                e.printStackTrace();
                if (isModer)
                    ret.add(0, new Post(getResources(), "! Ошибка чтения !", post, "0"));
                i2++;
            }
            i++;
        }
        ret.add(0, new Post(getResources(), "# Статистика", i1 + "/" + i2 + "/" + posts.length + "-" + i, "0"));
        return ret;
    }
}