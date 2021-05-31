package com.bar.automarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class AdInfoActivity extends AppCompatActivity {

    TextView modelTextView, makeTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ad_info);

        setWindow();

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String make = extras.getString("make");
            String model = extras.getString("model");
            String image = extras.getString("image");

            modelTextView.setText(model);
            makeTextView.setText(make);
        }
    }

    private void setWindow() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * .85), (int) (height * .7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;
        getWindow().setAttributes(params);
    }
}