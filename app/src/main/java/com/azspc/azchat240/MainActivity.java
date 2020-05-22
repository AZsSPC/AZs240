package com.azspc.azchat240;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    public static String
            savePost = "posts",           //CHANGEABLE
            saveData = "data",            //CHANGEABLE
            version = "4.3",              //CHANGEABLE
            separator = "║",              //FINAL
            splitter = "│",               //FINAL
            id_moder = "isModer",         //FINAL
            id_url_posts = "urlPost",     //FINAL
            id_url_update = "urlUp",      //FINAL
            id_version = "version",       //FINAL
            id_v_info = "vInfo",          //FINAL
            data_load_url =               //FINAL
                    "https://raw.githubusercontent.com/AZsSPC/AZs240/master/data_az.txt";
    public static boolean
            isModerator = false,
            isMenuVisible = false;
    public static SharedPreferences sp;
    private RecyclerView recyclerView;

    protected void onResume() {
        super.onResume();
        isModerator = sp.getBoolean(id_moder, false);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (isModerator) Toast.makeText(getBaseContext(),
                "Режим модератора активирован",
                Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        closeMenu();
        recyclerView = findViewById(R.id.tab_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
                startActivity(new Intent(this, UpdateActivity.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Post> getInitialData() {
        ArrayList<Post> ret = new ArrayList<>();
        String[] posts = getFromCloud(sp.getString(id_url_posts, "no url"), savePost);
        for (String post : posts)
            try {
                String[] d = post.replaceAll("\\\\n", "\n").split(splitter);
                ret.add(0, new Post(getResources(), d[0], d[1], d[2]));
            } catch (Exception e) {
                e.printStackTrace();
                if (isModerator)
                    ret.add(0, new Post(getResources(), "! Ошибка чтения !", post, "0"));
            }

        return ret;
    }

    public void menuFab(View v) {
        findViewById(R.id.fab_men).setVisibility((isMenuVisible = !isMenuVisible) ? View.VISIBLE : View.INVISIBLE);
    }

    public void reloadPosts(View v) {
        closeMenu();
        recyclerView.setAdapter(new DataAdapter(getBaseContext(), getInitialData()));
    }

    public void infoScreen(View v) {
        closeMenu();
        startActivity(new Intent(this, InfoActivity.class));
    }

    public void createPost(View v) {
        closeMenu();
        startActivity(new Intent(this, CreatePostActivity.class));
    }

    public void settingsOpen(View v) {
        closeMenu();
        startActivity(new Intent(this, SettingsActivity.class));
    }

    void closeMenu() {
        findViewById(R.id.fab_men).setVisibility((isMenuVisible = false) ? View.VISIBLE : View.INVISIBLE);
    }

    String[] getFromCloud(String url, String dName) {
        try {
            ReadableByteChannel rbc = Channels.newChannel(new URL(url).openStream());
            FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), dName));
            fos.getChannel().transferFrom(rbc, 0, Build.VERSION.SDK_INT > 23 ? Long.MAX_VALUE : 8 * 1024);
            fos.close();
            rbc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),
                    "Файл не знайдено",
                    Toast.LENGTH_SHORT).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),
                    e + "",
                    Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),
                    "Погане підключення до інтернету",
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
}