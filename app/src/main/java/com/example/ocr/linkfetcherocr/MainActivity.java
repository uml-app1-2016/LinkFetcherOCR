package com.example.ocr.linkfetcherocr;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);
    }


}
