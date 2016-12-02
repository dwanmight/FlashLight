package com.junior.dwan.flashlight;

import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Might on 01.12.2016.
 */

public class BaseActivity extends AppCompatActivity {

    public void setBackgroundColor(ViewGroup view, int color) {
        view.setBackgroundColor(color);
    }


    public void showToast(String message){
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

}
