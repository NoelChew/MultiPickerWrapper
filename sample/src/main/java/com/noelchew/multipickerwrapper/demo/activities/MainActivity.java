package com.noelchew.multipickerwrapper.demo.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.noelchew.multipickerwrapper.demo.R;


/**
 * Created by noelchew on 16/08/2016.
 */
public class MainActivity extends AppCompatActivity {
    private Context context;
    private Button btnActivity, btnFragment, btnSupportFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        btnActivity = (Button) findViewById(R.id.button_activity_demo);
        btnFragment = (Button) findViewById(R.id.button_fragment_demo);
        btnSupportFragment = (Button) findViewById(R.id.button_support_fragment_demo);

        btnActivity.setOnClickListener(btnActivityOnClickListener);
        btnFragment.setOnClickListener(btnFragmentOnClickListener);
        btnSupportFragment.setOnClickListener(btnSupportFragmentOnClickListener);
    }

    private View.OnClickListener btnActivityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context, DemoActivity.class));
        }
    };

    private View.OnClickListener btnFragmentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context, DemoFragmentActivity.class));
        }
    };

    private View.OnClickListener btnSupportFragmentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context, DemoSupportFragmentActivity.class));
        }
    };
}
