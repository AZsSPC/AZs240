package com.azspc.azchat240;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import static android.provider.Telephony.Mms.Part.FILENAME;

public class MainActivity extends AppCompatActivity {
    public static final String sep_post = "║";
    public static final String sep_part = "│";
    final String url = "https://raw.githubusercontent.com/AZsSPC/test/master/README.md";
    final String saveName = "posts";
    private RecyclerView recyclerView;
    boolean isModerator = false;
    boolean isMenuVisible = false;

    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isModerator = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        closeMenu();
        recyclerView = findViewById(R.id.tab_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
        reloadPosts(null);
    }

    private ArrayList<Post> getInitialData() {
        ArrayList<Post> posts = new ArrayList<>();
        for (String post : getStrDataPosts().split(sep_post)) {
            try {
                String[] pre_p = post.split(sep_part);
                posts.add(0, new Post(getResources(), pre_p[0], pre_p[1], pre_p[2]));
            } catch (Exception e) {
                e.printStackTrace();
                if (isModerator)
                    posts.add(0, new Post(getResources(), "Помилка читання поста", "Весь вміст:\n\n" + post, "-1"));
            }
        }
        return posts;
    }

    public void menuFab(View v) {
        findViewById(R.id.fab_men).setVisibility((isMenuVisible = !isMenuVisible) ? View.VISIBLE : View.INVISIBLE);
    }

    public void reloadPosts(View v) {
        closeMenu();
        Toast.makeText(getBaseContext(),
                "Розпочато оновлення постів, це може зайняти час.",
                Toast.LENGTH_LONG).show();
        recyclerView.setAdapter(new DataAdapter(getBaseContext(), getInitialData()));
    }

    public void infoScreen(View v) {
        closeMenu();
        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
    }

    public void createPost(View v) {
        closeMenu();
        Intent intent = new Intent(this, CreatePostActivity.class);
        startActivity(intent);
    }

    void closeMenu() {
        findViewById(R.id.fab_men).setVisibility((isMenuVisible = false) ? View.VISIBLE : View.INVISIBLE);
    }

    String getStrDataPosts() {
        return ("Першо-пост" + sep_part +
                "Це - просто найперший пост.\n\n" +
                "Я раджу Вам подивитися інформацію про цей додаток, я думаю Ви зрозуміете де її знайти.\n\n" +
                "   - Ратмир Мирошниченко (AZ_218)" + sep_part +
                "-1" + sep_post + getPostsFromCloud()
        ).replaceAll("\\\\n", "\n");
    }

    String getPostsFromCloud() {
        try {
            ReadableByteChannel rbc = Channels.newChannel(new URL(this.url).openStream());
            FileOutputStream fos = new FileOutputStream(new File(getFilesDir(), saveName));
            fos.getChannel().transferFrom(rbc, 0, Build.VERSION.SDK_INT > 23 ? Long.MAX_VALUE : 8 * 1024);
            fos.close();
            rbc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),
                    "Файл не найден",
                    Toast.LENGTH_LONG).show();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),
                    "Погане підключення до інтернету, " +
                            "будуть показані тільки раніше завантажені пости.",
                    Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),
                    e + "",
                    Toast.LENGTH_LONG).show();
        }
        try {
            return new BufferedReader(new InputStreamReader(openFileInput(saveName))).readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Files.lines(path.toPath()).findFirst().orElse(null);

        return "";
    }
/*
    public File getPostFile(String fileName) throws Exception {
        File dir = new File(getFilesDir(), saveName);
        File file = new File(dir, fileName);
        if (!file.exists()) if (dir.mkdirs() && file.createNewFile())
            Log.i("created new file", "the post file has been recreated");
        return file;
    }
*/

}