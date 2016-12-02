package com.junior.dwan.flashlight;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import yuku.ambilwarna.AmbilWarnaDialog;

public class MainActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    @BindView(R.id.btnTurnFlashLight)
    ToggleButton mTurnFlashLightBtn;
    @BindView(R.id.btnSetting)
    ToggleButton mSettingBtn;
    @BindView(R.id.btn_color_white)
    CheckedTextView mColorWhite;
    @BindView(R.id.btn_color_other)
    CheckedTextView mColorOther;
    @BindView(R.id.settingLayout)
    RelativeLayout mSettingLayout;
    @BindView(R.id.econom_chTv)
    CheckedTextView mEconomChTV;
    @BindView(R.id.display_chTv)
    CheckedTextView mDisplayChTV;
    @BindView(R.id.flash_chTv)
    CheckedTextView mFlashChTV;
    @BindView(R.id.seekBar)
    SeekBar mSeekBar;
    @BindView(R.id.color_linLayout)
    LinearLayout mColorLinLayout;
    //    @BindView(R.id.background_ImageView)
//    ImageView mBGImgView;
    @BindView(R.id.main_layout)
    ViewGroup mainLayout;
    private Camera mCam;
    private Camera.Parameters mCamParameters;
    private SurfaceTexture mPreviewTexture;
    int mColor;
    private AmbilWarnaDialog mAmbilWarnaDialog;
    private boolean isFlashEnable;
    private float mBrightnessDefault;
    private boolean isBlinkFlash;
    private Handler mHandler;
    private int mblinkDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBrightnessDefault = getBrightness();
        isFlashEnable = false;
        isBlinkFlash = false;
        mblinkDefault=100;
        mHandler = new Handler();
        ButterKnife.bind(this);
        initializeCamera();
        setFlashLightOn();
        mSettingBtn.setOnCheckedChangeListener(this);
        mTurnFlashLightBtn.setOnCheckedChangeListener(this);
        mColorWhite.setOnClickListener(this);
        mColorOther.setOnClickListener(this);
        mEconomChTV.setOnClickListener(this);
        mDisplayChTV.setOnClickListener(this);
        mFlashChTV.setOnClickListener(this);


    }

    private void initializeCamera() {
        mCam = Camera.open();
        mCamParameters = mCam.getParameters();
    }

    private void setFlashLightOn() {
        if (!isFlashEnable) {
            mCamParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCam.setParameters(mCamParameters);
            mPreviewTexture = new SurfaceTexture(0);
            try {
                mCam.setPreviewTexture(mPreviewTexture);
            } catch (IOException ex) {
                // Ignore
            }
            mCam.startPreview();
            isFlashEnable = true;
        } else {
            mCamParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            mCam.setParameters(mCamParameters);
            mPreviewTexture = new SurfaceTexture(0);
            try {
                mCam.setPreviewTexture(mPreviewTexture);
            } catch (IOException ex) {
                // Ignore
            }
            mCam.startPreview();
            isFlashEnable = false;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton btn, boolean isChecked) {
        switch (btn.getId()) {
            case R.id.btnTurnFlashLight:
                if (!isChecked) {
                    if (mDisplayChTV.isChecked() && mColor == 0) {
                        showToast("Pick the color!");
                        btn.setChecked(!isChecked);
                        switchBackground();
                    }
                    if (mTurnFlashLightBtn.isChecked())
                        showToast("on");
                    else showToast("off");

                    if (!mTurnFlashLightBtn.isChecked()) {
                        mainLayout.setBackground(getResources().getDrawable(R.drawable.bg_stardust));
                        mCamParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        mCam.setParameters(mCamParameters);
                        mPreviewTexture = new SurfaceTexture(0);
                        try {
                            mCam.setPreviewTexture(mPreviewTexture);
                        } catch (IOException ex) {
                            // Ignore
                        }
                        mCam.startPreview();

                    }
                }
                turnFlash();
                break;
            case R.id.btnSetting:
                if (isChecked)
                    mSettingLayout.setVisibility(View.VISIBLE);
                else
                    mSettingLayout.setVisibility(View.GONE);
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_color_white:
                ((CheckedTextView) v).toggle();
                swiitchToogleColor(mColorWhite);
                setColor(mColor);
                mColorOther.setTextColor(getResources().getColor(R.color.colorText));
                if (!mTurnFlashLightBtn.isChecked()) {
                    turnFlash();
                }
                break;
            case R.id.btn_color_other:
                initPickColor();
                mAmbilWarnaDialog.show();
                break;
            case R.id.econom_chTv:
                ((CheckedTextView) v).toggle();
                showOptions(mEconomChTV);

                break;
            case R.id.display_chTv:
                ((CheckedTextView) v).toggle();
                showOptions(mDisplayChTV);
                break;
            case R.id.flash_chTv:
                ((CheckedTextView) v).toggle();
                showOptions(mFlashChTV);
                break;
        }
    }

    private void initPickColor() {
        mAmbilWarnaDialog = new AmbilWarnaDialog(this, mColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                // color is the color selected by the user.
                Log.i("TAGTAG", color + "ok");
                if (!mColorOther.isChecked()) {
                    mColorOther.toggle();
                }
                mColor = color;
                swiitchToogleColor(mColorOther);
                mColorOther.setTextColor(color);

                if (!mTurnFlashLightBtn.isChecked()) {
                    mColor = color;
                    turnFlash();
                }
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                // cancel was selected by the user
                Log.i("TAGTAG", "cancel");
                if (mColorOther.isChecked()) {
                    mColorOther.toggle();
                }
                setColor(mColor);
            }

        });

    }

    private void showOptions(CheckedTextView v) {
        switch (v.getId()) {
            case R.id.econom_chTv:
                if (v.isChecked()) {
                    mSeekBar.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(toggleFlash, mblinkDefault);
                } else {
                    mSeekBar.setVisibility(View.GONE);
                    mHandler.removeCallbacks(toggleFlash);
                    if(isFlashEnable){
                        turnFlash();
                    }
                }
                break;
            case R.id.display_chTv:
                if (v.isChecked()) {
                    mColorLinLayout.setVisibility(View.VISIBLE);
                } else {
                    mColorLinLayout.setVisibility(View.GONE);
                }
        }
    }

    private void turnFlash() {
        if (mDisplayChTV.isChecked() && !mFlashChTV.isChecked()) {
            if (mColor != 0) {
                switchBackground();
            } else {
                showToast("Pick the color!");
            }

        } else if (mFlashChTV.isChecked() && !mDisplayChTV.isChecked()) {
            setFlashLightOn();
        } else if (mDisplayChTV.isChecked() && mFlashChTV.isChecked()) {
            if (mColor != 0) {
                switchBackground();
                setFlashLightOn();
            } else {
                showToast("Pick the color!");
            }

        }
    }

    private void switchBackground() {
        if (!mTurnFlashLightBtn.isChecked()) {
            setBrightness(1);
            mainLayout.setBackgroundColor(mColor);

        } else {
            setBrightness(mBrightnessDefault);
            mainLayout.setBackground(getResources().getDrawable(R.drawable.bg_stardust));
        }
    }

    private void swiitchToogleColor(CheckedTextView tvColor) {
        switch (tvColor.getId()) {
            case R.id.btn_color_white:
                if (mColorWhite.isChecked()) {
                    if (mColorOther.isChecked()) {
                        mColorOther.toggle();
                    }
                }
                break;
            case R.id.btn_color_other:
                if (mColorOther.isChecked()) {
                    if (mColorWhite.isChecked()) {
                        mColorWhite.toggle();
                    }
                }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCam.stopPreview();
        mCam = null;
    }

    private void setColor(int color) {
        if (mColorWhite.isChecked()) {
            mColor = getResources().getColor(R.color.colorWhite);
        } else if (mColorOther.isChecked()) {
            mColor = color;
        } else if (!mColorWhite.isChecked() && !mColorOther.isChecked()) {
            mColor = 0;
        }
    }

    private Runnable toggleFlash = new Runnable() {
        public void run() {
            if (isBlinkFlash) {
                //do Flash off
                setFlashLightOn();
                isBlinkFlash = false;
            } else {
                //do Flash on
                setFlashLightOn();
                isBlinkFlash = true;
            }
            mHandler.postDelayed(this, 100);
        }
    };
}
