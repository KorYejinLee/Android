package com.iot.englishtestproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "Dictionary";
    public static final String _ID = "_id";
    public static final String ENGLISHWORD = "englishWord";
    public static final String ENGLISHTRANSATION = "englishTranslation";
    public static final String KOREATRANSATION = "koreanTranslation";
    public static final String PRONUNCIATION = "pronunciation";
    public static final String PARTOFSPEECH = "partOfSpeech";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ENGLISHWORD + " TEXT NOT NULL, " +
            ENGLISHTRANSATION + " TEXT NOT NULL, " +
            KOREATRANSATION + " TEXT NOT NULL, " +
            PRONUNCIATION + " TEXT NOT NULL, " +
            PARTOFSPEECH + " TEXT NOT NULL);";

    public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}

