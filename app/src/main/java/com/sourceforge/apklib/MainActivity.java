package com.sourceforge.apklib;

import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sourceforge.apklib.foundation.data.ViewBinding;
import com.sourceforge.apklib.foundation.ui.widget.BaseToolbar;

public class MainActivity extends AppCompatActivity {
    private BaseToolbar baseToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        baseToolbar=(BaseToolbar)findViewById(R.id.baseToolbar);
        baseToolbar.setCompatibleElevation(20);
        baseToolbar.setTitleAlignLeft();
        baseToolbar.setTitle("这是title");
    }
}
