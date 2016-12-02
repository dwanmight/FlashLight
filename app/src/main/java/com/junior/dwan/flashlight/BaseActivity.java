package com.junior.dwan.flashlight;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.WindowManager;
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

    public float getBrightness(){
        float curBrightnessValue = 0;
        try {
            curBrightnessValue=android.provider.Settings.System.getInt(
                    getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return curBrightnessValue;
    }

    public void setBrightness(float brightness){
        WindowManager.LayoutParams layout = getWindow().getAttributes();
        layout.screenBrightness = brightness;
        getWindow().setAttributes(layout);
    }

}
