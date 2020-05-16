package com.azspc.azchat240;

import android.Manifest;
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


import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //public static int language;
    public static final String sep_post = "║";
    public static final String sep_part = "│";
    public static final String lang_separator = ";";
    static final String url = "https://raw.githubusercontent.com/AZsSPC/test/master/README.md";
    static final String saveName = "posts.txt";
    static final String saveTo = "/storage/emulated/0/AZsSPC/AZs240";
    private RecyclerView recyclerView;
    boolean isModerator = false;
    boolean isMenuVisible = false;

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        reloadPosts(null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //language = Locale.getDefault().getLanguage().equals("ua") ? 1 : 0;
        isModerator = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy pol = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(pol);
        recyclerView = findViewById(R.id.tab_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Post> getInitialData() {
        ArrayList<Post> posts = new ArrayList<>();
        for (String post : getPostsFromCloud().split(sep_post)) {
            try {
                String[] pre_p = post.split(sep_part);
                posts.add(0, new Post(getResources(), pre_p[0], pre_p[1], pre_p[2]));
            } catch (Exception ignored) {
                if (isModerator)
                    posts.add(0, new Post(getResources(), "Помилка читання поста", "Весь вміст:\n\n" + post, "-1"));
            }
        }
        return posts;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reloadPosts(View v) {
        closeMenu();
        recyclerView.setAdapter(new DataAdapter(this, getInitialData()));
    }

    public void menuFab(View v) {
        findViewById(R.id.fab_men).setVisibility((isMenuVisible = !isMenuVisible) ? View.VISIBLE : View.INVISIBLE);
    }

    public void infoScreen(View v) {
        closeMenu();
    }

    public void createPost(View v) {
        closeMenu();
    }

    void closeMenu() {
        findViewById(R.id.fab_men).setVisibility((isMenuVisible = false) ? View.VISIBLE : View.INVISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    String getPostsFromCloud() {
        String anotherPosts = "";
        try {
            Path path = Paths.get(getPostFile(saveName).getPath());
            try {
                Files.copy(new URL(url).openStream(), path, StandardCopyOption.REPLACE_EXISTING);
                Toast.makeText(getBaseContext(),
                        "Розпочато оновлення постів, це може зайняти час.",
                        Toast.LENGTH_LONG).show();
            } catch (Exception ignored) {
                Toast.makeText(getBaseContext(),
                        "Погане підключення до інтернету, " +
                                "будуть показані тільки раніше завантажені пости.",
                        Toast.LENGTH_LONG).show();
            }
            anotherPosts = Files.lines(path).findFirst().orElse(null);
        } catch (Exception ignored) {
        }
        return ("Про програму" + sep_part +
                "Дякую що встановили цей додаток, " +
                "теперь Ви можете слідкувати за новинами нашої школи!\n\n" +
                "У додатку є кілька типів постів:\n" +
                " • Сірий - системний\n" +
                " • Синій - тихий\n" +
                " • Зелений - звичайний\n" +
                " • Червоний - терміновий\n\n" +
                "Тип поста ні на що не впливає крім як швидкої класифікації" +
                " поста для користувача. Простіше кажучи - що б Вам було зручніше.\n\n" +
                "Додаток створено під керівництвом Олега Н. - нового президента школи.\n\n" +
                "Гарного навчання та просто - удачі!" + sep_part +
                "-1" + sep_post
                + anotherPosts).replaceAll("\\\\n", "\n");
    }

    public File getPostFile(String fileName) throws Exception {
        File dir = new File(saveTo);
        File file = new File(dir, fileName);
        if (!file.exists()) if (dir.mkdirs() && file.createNewFile())
            Log.i("created new file", "the post file has been recreated");
        return file;
    }


}