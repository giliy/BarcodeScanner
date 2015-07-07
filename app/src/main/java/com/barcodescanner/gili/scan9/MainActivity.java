package com.barcodescanner.gili.scan9;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements LoaderCallbacks<Cursor> {

    private View progressView;
    private AutoCompleteTextView usernameTextView;
    private EditText passwordTextView;
    private Button signUpButton;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().getBackground().setDither(true);
        getWindow().setFormat(PixelFormat.RGBA_8888);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap gradient = BitmapFactory.decodeResource(getResources(), R.drawable.supermarket, options);

        findViewById(R.id.loginView).setBackgroundDrawable(new BitmapDrawable(gradient));

        usernameTextView = (AutoCompleteTextView)findViewById(R.id.user);
        loadAutoComplete();

        passwordTextView = (EditText) findViewById(R.id.pwd);
        passwordTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });
    }

    private void loadAutoComplete() {
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}