package com.azspc.azchat240;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static int language;
    public static final String post_separator = ";;;";
    public static final String post_data_separator = ";;";
    public static final String lang_separator = ";";
    public static final String base_date = "•";
    public static final String base_name = "Разработчик";
    private RecyclerView recyclerView;
    boolean logged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        language = Locale.getDefault().getLanguage().equals("ua") ? 1 : 0;
        logged = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.tab_post);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        try {
            recyclerView.setAdapter(new DataAdapter(this, getInitialData()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView title = findViewById(R.id.main_title);
        title.setText(getString(R.string.tab_name).split(lang_separator)[language]);

        FloatingActionButton fab = findViewById(R.id.fab);
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
        });
    }


    private ArrayList<Post> getInitialData() throws IOException {
        ArrayList<Post> posts = new ArrayList<>();
        for (String post : getPostsFromCloud().split(post_separator)) {
            try {
                String[] post_data = post.split(post_data_separator);
                posts.add(0, new Post(getResources(),
                        post_data[0], post_data[1], post_data[2], post_data[3],
                        Integer.parseInt(post_data[4])));
            } catch (Exception ignored) {
                posts.add(0, new Post(getResources(), base_name, "Ошибка", "Ошибка чтения данных.", base_date, -1));
            }
        }
        return posts;
    }

    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

    }

    String getPostsFromCloud() throws IOException {
        String another_posts = "Сергей;;Люблю физику!;;Ура, физичка опять влепила кол!;;сегодня;;0;;;" +
                "Миша;;Пропал учебник;;Помогите найти учебник по математике, 5-й класс;;сегодня;;2;;;" +
                "Саша;;Шторы;;Надо бы постирать шторы, я могу это сделать!!;;вчера;;1;;;" +
                "Арсений;;Хорошие новости;;Я нашел твой учебник по математике, Миша;;сегодня;;0;;;";
        return base_name + post_data_separator +
                "О приложении" + post_data_separator +
                "Спасибо что установили это приложение, тепрь Вы можете следить за новостями нашей школы!\n\n" +
                "В приложении есть несколько типо постов:\n" +
                " • Серый - системный\n" +
                " • Синий - тихий\n" +
                " • Зеленый - обычный\n" +
                " • Красный - срочный\n" +
                "Тип поста ни на что не влияет кроме как быстрой классификации поста для пользователя. Проще говоря - что бы Вам было удобнее.\n\n" +
                "Что бы добавить пост нужно подтвердить свою личность, это можно сделать через приложение (в будущем)\n\n" +
                "Приложение создано под руководством Олега Н. - нового президента школы\n\n" +
                "Хорошей учебы и просто - удачи!" + post_data_separator +
                base_date + post_data_separator +
                "-1" + post_separator + another_posts;
    }
}