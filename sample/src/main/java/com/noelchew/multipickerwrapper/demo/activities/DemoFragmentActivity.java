package com.noelchew.multipickerwrapper.demo.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.greysonparrelli.permiso.PermisoActivity;
import com.noelchew.multipickerwrapper.demo.R;
import com.noelchew.multipickerwrapper.demo.fragments.DemoFragment;

/**
 * Created by noelchew on 16/08/2016.
 */
    // Note that this activity extends PermisoActivity
    // If you don't want to extend PermisoActivity, please refer to the example in DemoSupportFragmentActivity
public class DemoFragmentActivity extends PermisoActivity {

    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_fragment_demo);
        DemoFragment fragment = new DemoFragment();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
