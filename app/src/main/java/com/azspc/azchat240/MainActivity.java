package com.azspc.azchat240;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static int language;
    public static final String post_separator = "║";
    public static final String post_data_separator = "│";
    public static final String lang_separator = ";";
    static final String url = "http://azsspc.moy.su/posts.txt";
    static final String saveName = "posts.txt";
    static final String saveTo = "/storage/emulated/0/AZsSPC/AZs240";
    private RecyclerView recyclerView;
    boolean logged;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        language = Locale.getDefault().getLanguage().equals("ua") ? 1 : 0;
        logged = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy newPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(newPolicy);
        recyclerView = findViewById(R.id.tab_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updatePosts();
        setTitle(getString(R.string.tab_name).split(lang_separator)[language]);
/*      FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (logged) {
                    setContentView(R.layout.create_post);
                } else {
                    setContentView(R.layout.activity_login);
                    TextView title = findViewById(R.id.login_about);
                    title.setText(getString(R.string.login_info).split(lang_separator)[language]);

                    Button button = findViewById(R.id.login);
                    button.setText(getString(R.string.login_go).split(lang_separator)[language]);

                    EditText editText = findViewById(R.id.username);
                    editText.setHint(getString(R.string.login_name).split(lang_separator)[language]);

                    EditText editText1 = findViewById(R.id.password);
                    editText1.setHint(getString(R.string.login_id).split(lang_separator)[language]);

                }
            }
        });*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Post> getInitialData() {
        ArrayList<Post> posts = new ArrayList<>();
        for (String post : getPostsFromCloud().split(post_separator)) {
            try {
                String[] pre_p = post.split(post_data_separator);
                posts.add(0, new Post(getResources(), pre_p[0], pre_p[1], pre_p[2]));
            } catch (Exception ignored) {
                posts.add(0, new Post(getResources(), "Ошибка", "Ошибка чтения данных.", "-1"));
            }
        }
        return posts;
    }

    public File getAlbumStorageDir(String fileName) throws Exception {
        File dir = new File(saveTo);
        File file = new File(dir, fileName);
        if (!file.exists()) {
            dir.mkdirs();
            file.createNewFile();
        }
        return file;
    }

    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateCloudData(View v) {
        updatePosts();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updatePosts() {
        try {
            recyclerView.setAdapter(new DataAdapter(this, getInitialData()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String getPostsFromCloud() {
        String content = "";
        try {
            Path path = Paths.get(getAlbumStorageDir(saveName).getPath());
            Files.copy(new URL(url).openStream(), path, StandardCopyOption.REPLACE_EXISTING);
            content = Files.lines(path).reduce("", (a, b) -> a + "\n" + b).substring(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "О приложении" + post_data_separator +
                "Спасибо что установили это приложение, тепрь Вы можете следить за новостями нашей школы!\n\n" +
                "В приложении есть несколько типо постов:\n" +
                " • Серый - системный\n" +
                " • Синий - тихий\n" +
                " • Зеленый - обычный\n" +
                " • Красный - срочный\n" +
                "Тип поста ни на что не влияет кроме как быстрой классификации поста для пользователя. Проще говоря - что бы Вам было удобнее.\n\n" +
                //"Что бы добавить пост нужно подтвердить свою личность, это можно сделать через приложение (в будущем)\n\n" +
                "Приложение создано под руководством Олега Н. - нового президента школы\n\n" +
                "Хорошей учебы и просто - удачи!" + post_data_separator +
                "-1" + post_separator
                + content;
    }
}