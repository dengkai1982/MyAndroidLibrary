package com.sourceforge.apklib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sourceforge.apklib.foundation.data.ViewBinding;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewBinding.binding(this,R.class,ViewBinding.getActivityViewFinder(this));
        textView.setText("愉快的绑定起啦");
    }
}
