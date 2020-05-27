package com.example.echo;

import android.support.v4.app.Fragment;

public class ResponseListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new ResponseListFragment();
    }
}
