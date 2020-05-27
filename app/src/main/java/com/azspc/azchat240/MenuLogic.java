package com.azspc.azchat240;

import android.content.Intent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.azspc.azchat240.cheat.CheatActivity;
import com.azspc.azchat240.menu.CreatePostActivity;
import com.azspc.azchat240.menu.InfoActivity;
import com.azspc.azchat240.menu.SettingsActivity;
import com.azspc.azchat240.menu.UpdateActivity;

import static com.azspc.azchat240.MainActivity.*;

public class MenuLogic extends AppCompatActivity {

    public void menuFab(View v) {
        findViewById(R.id.fab_menu_0).setVisibility((isMenuVisible = !isMenuVisible) ? View.VISIBLE : View.INVISIBLE);
    }

    public void infoScreen(View v) {
        startActivity(new Intent(this, InfoActivity.class));
    }

    public void createPost(View v) {
        startActivity(new Intent(this, CreatePostActivity.class));
    }

    public void settingsOpen(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void versionInfo(View v) {
        startActivity(new Intent(this, UpdateActivity.class));
    }

    public void openCheat(View v) {
        startActivity(new Intent(this, CheatActivity.class));
    }

    public void closeMenu(View v) {
        hideMenu(findViewById(R.id.fab_menu_0));
    }

    public static void hideMenu(ConstraintLayout layout) {
        layout.setVisibility((isMenuVisible = false) ? View.VISIBLE : View.INVISIBLE);
    }

}
