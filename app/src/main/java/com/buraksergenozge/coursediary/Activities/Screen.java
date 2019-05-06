package com.buraksergenozge.coursediary.Activities;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

public abstract class Screen extends AppCompatActivity {
    protected boolean hasMenu;
    protected int menuID;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(hasMenu) {
            getMenuInflater().inflate(menuID, menu);
            return true;
        }
        return false;
    }
}