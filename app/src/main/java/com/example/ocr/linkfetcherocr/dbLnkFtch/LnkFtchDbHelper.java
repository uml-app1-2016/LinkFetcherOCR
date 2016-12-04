/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.ocr.linkfetcherocr.dbLnkFtch;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.example.ocr.linkfetcherocr.Link;
import com.example.ocr.linkfetcherocr.R;
import com.example.ocr.linkfetcherocr.dbLnkFtch.LnkContract.LinkEntry;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

public class LnkFtchDbHelper extends SQLiteOpenHelper {

    /*private mdbhelper*/
    private LnkFtchDbHelper mDbHelper;
    private Context mCtx;
    private SQLiteDatabase mDb;

    public  final String LOG_TAG = LnkFtchDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "lnkFtchr.db";
    private static final int DATABASE_VERSION = 4;
    public static final String SQL_CREATE_LINKS_TABLE = "CREATE TABLE " + LnkContract.LinkEntry.TABLE_NAME + "("
        + LnkContract.LinkEntry._ID + " INTEGER PRIMARY KEY autoincrement, "
        + LnkContract.LinkEntry.COLUMN_FETCHED_NAME + " TEXT, "
        + LnkContract.LinkEntry.COLUMN_FETCHED_ADDRESS + " TEXT, "
        + LnkContract.LinkEntry.COLUMN_IMAGE + " TEXT, "
        + LnkContract.LinkEntry.COLUMN_FETCHED_URL + " TEXT, "
        + LnkContract.LinkEntry.COLUMN_SEARCHED_TIME + " TEXT);";


    public LnkFtchDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mCtx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_LINKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion>oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + LinkEntry.TABLE_NAME);
            onCreate(db);
        }
    }

    public LnkFtchDbHelper open() throws SQLException{
        mDbHelper = new LnkFtchDbHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if(mDbHelper != null){
            mDbHelper.close();
        }
    }

    public long createEntry(String name, String address, String url, String photoUrl){
        ContentValues initVals = new ContentValues();
        //Ref: @http://stackoverflow.com/questions/6341776/how-to-save-bitmap-in-database
        initVals.put(LinkEntry.COLUMN_FETCHED_NAME, name);
        initVals.put(LinkEntry.COLUMN_IMAGE, photoUrl);
        initVals.put(LinkEntry.COLUMN_FETCHED_ADDRESS, address);
        initVals.put(LinkEntry.COLUMN_FETCHED_URL, url);
        return mDb.insert(LinkEntry.TABLE_NAME, null, initVals);
    }
    /* Jwydo
     * to insert our new data to database
     * Takes a list of "Link"*/
    public void insertAllLinks(List<Link> allLinks) {
        Iterator<Link> iterLinks = allLinks.iterator();
        while (iterLinks.hasNext()) {
            createEntry(iterLinks.next().getName(),
                    iterLinks.next().getName(),
                    iterLinks.next().getUrl(),
                    iterLinks.next().getFavicon().toString());
        }
    }


    public boolean deleteAllEntries(){
        int doneDeed = 0;
        doneDeed = mDb.delete(LinkEntry.TABLE_NAME, null, null);
        return doneDeed > 0;
    }
    /*Jwydo
     *Select statements where column is like inpurl
     */
    public Cursor fetchEntryByUrl(String inpUrl) throws SQLException{
        Cursor nmCursor = null;
        if (inpUrl == null || inpUrl.length() == 0) {
            nmCursor = mDb.query(LinkEntry.TABLE_NAME, new String[]{
                    LinkEntry._ID, LinkEntry.COLUMN_FETCHED_NAME, LinkEntry.COLUMN_FETCHED_ADDRESS, LinkEntry.COLUMN_FETCHED_URL, LinkEntry.COLUMN_IMAGE
            }, null, null, null, null, null);
        }
        else{
            nmCursor = mDb.query(true, LinkEntry.TABLE_NAME, new String[] {
                    LinkEntry._ID, LinkEntry.COLUMN_FETCHED_NAME, LinkEntry.COLUMN_FETCHED_ADDRESS, LinkEntry.COLUMN_FETCHED_URL, LinkEntry.COLUMN_IMAGE
            }, LinkEntry.COLUMN_FETCHED_URL + "like '%'" + inpUrl + "'%'", null, null, null, null, null);
        }
        if (nmCursor != null){
            nmCursor.moveToFirst();
        }
        return nmCursor;
    }
    public Cursor fetchEntryByName(String inpUrl) throws SQLException {
        Cursor nmCursor = null;
        if (inpUrl == null || inpUrl.length() == 0) {
            nmCursor = mDb.query(LinkEntry.TABLE_NAME, new String[]{
                    LinkEntry._ID, LinkEntry.COLUMN_FETCHED_NAME, LinkEntry.COLUMN_FETCHED_ADDRESS, LinkEntry.COLUMN_FETCHED_URL, LinkEntry.COLUMN_IMAGE
            }, null, null, null, null, null);
        } else {
            nmCursor = mDb.query(true, LinkEntry.TABLE_NAME, new String[] {
                    LinkEntry._ID, LinkEntry.COLUMN_FETCHED_NAME, LinkEntry.COLUMN_FETCHED_ADDRESS, LinkEntry.COLUMN_FETCHED_URL, LinkEntry.COLUMN_IMAGE
            }, LinkEntry.COLUMN_FETCHED_NAME + "like '%'" + inpUrl + "'%'", null, null, null, null, null);

        }
        return nmCursor;
    }
    public Cursor fetchAllInfo(){
        Cursor nmCursor = mDb.query(LinkEntry.TABLE_NAME, new String[] {
                LinkEntry._ID, LinkEntry.COLUMN_FETCHED_NAME, LinkEntry.COLUMN_FETCHED_ADDRESS, LinkEntry.COLUMN_FETCHED_URL, LinkEntry.COLUMN_IMAGE
        },null, null, null, null, null);
        if(nmCursor != null){
            nmCursor.moveToFirst();
        }
        return nmCursor;
    }

    public void insertSomeFakeEntries(){
        createEntry("Jonathan", "jwydola@hotmail.com", "http://www.facebook.com","something");
        createEntry("Katherine", "kObert@obert.net", "http://www.reddit.com", "something");
        createEntry("csDepartment", "csDepot", "http://www.cs.uml.edu", "some");
    }

    private void copyDataBase() {

        try {
            InputStream myInput = mCtx.getAssets().open(DATABASE_NAME);
            String outFileName = this.mCtx.getDatabasePath(DATABASE_NAME).getAbsolutePath();
            ;

            OutputStream myOutput = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024 * 3];

            int length = 0;

            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            return;
        }
    }

    }