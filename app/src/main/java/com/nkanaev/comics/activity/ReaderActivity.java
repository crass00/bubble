package com.nkanaev.comics.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import com.nkanaev.comics.R;
import com.nkanaev.comics.fragment.ReaderFragment;
import android.view.KeyEvent;

import java.io.File;


public class ReaderActivity extends AppCompatActivity {
    private ReaderFragment reader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_reader);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_reader);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            if (Intent.ACTION_VIEW.equals(getIntent().getAction())) {
                ReaderFragment fragment = ReaderFragment.create(new File(getIntent().getData().getPath()));
                setFragment(fragment);
            }
            else {
                Bundle extras = getIntent().getExtras();
                ReaderFragment fragment = null;
                ReaderFragment.Mode mode = (ReaderFragment.Mode) extras.getSerializable(ReaderFragment.PARAM_MODE);

                if (mode == ReaderFragment.Mode.MODE_LIBRARY)
                    fragment = ReaderFragment.create(extras.getInt(ReaderFragment.PARAM_HANDLER));
                else
                    fragment = ReaderFragment.create((File) extras.getSerializable(ReaderFragment.PARAM_HANDLER));
                setFragment(fragment);
            }
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void setFragment(ReaderFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame_reader, fragment)
                .commit();
        reader = fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_PAGE_UP:
            if (action == KeyEvent.ACTION_DOWN) {
                reader.pageLeft(false);
            }
            return true;
        case KeyEvent.KEYCODE_VOLUME_DOWN:
        case KeyEvent.KEYCODE_PAGE_DOWN:
            if (action == KeyEvent.ACTION_DOWN) {
                reader.pageRight(false);
            }
            return true;
        default:
            return super.dispatchKeyEvent(event);
        }
    }
}
