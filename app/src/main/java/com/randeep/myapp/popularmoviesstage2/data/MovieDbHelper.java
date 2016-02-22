package com.randeep.myapp.popularmoviesstage2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.randeep.myapp.popularmoviesstage2.data.MovieContract.Movies;
import com.randeep.myapp.popularmoviesstage2.data.MovieContract.Trailers;
import com.randeep.myapp.popularmoviesstage2.data.MovieContract.Reviews;
/**
 * Created by randeep on 2/14/16.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "movie.db";


    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE "+ Movies.TABLE_NAME +
                " (" + Movies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Movies.POSTER_PATH + " TEXT, "+
                Movies.ADULT + " TEXT, "+
                Movies.OVERVIEW + " TEXT, "+
                Movies.RELEASE_DATE + " TEXT, "+
                Movies.MOVIE_ID + " TEXT, "+
                Movies.ORIGINAL_TITLE + " TEXT, "+
                Movies.ORIGINAL_LANGUAGE + " TEXT, "+
                Movies.TITLE + " TEXT, "+
                Movies.BACKDROP_PATH + " TEXT, "+
                Movies.POPULARITY + " REAL, "+
                Movies.VOTE_COUNT + " INTEGER, "+
                Movies.VOTE_AVERAGE+ " REAL, "+
                Movies.DOWNLOADED+ " INTEGER NOT NULL DEFAULT 0, "+
                Movies.FAVOURITE+ " INTEGER NOT NULL DEFAULT 0," +
                " UNIQUE (" + Movies.MOVIE_ID + ") ON CONFLICT REPLACE)";
        
        final String SQL_CREATE_TRAILERS_TABLE = "CREATE TABLE "+ Trailers.TABLE_NAME +
                " (" + Trailers._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Trailers.NAME + " TEXT, "+
                Trailers.SIZE + " TEXT, "+
                Trailers.SOURCE+ " TEXT, "+
                Trailers.TYPE+ " TEXT, "+
                Trailers.MOVIE_ID+ " INTEGER NOT NULL, "+
                "UNIQUE (" + Trailers.SOURCE + ") ON CONFLICT REPLACE)";

        final String SQL_CREATE_REVIEWS_TABLE = "CREATE TABLE "+ Reviews.TABLE_NAME +
                " (" + Reviews._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                Reviews.ID_REVIEWS + " TEXT, "+
                Reviews.AUTHOR + " TEXT, "+
                Reviews.CONTENT + " TEXT, "+
                Reviews.MOVIE_ID + " TEXT, "+
                "UNIQUE (" + Reviews.ID_REVIEWS + ") ON CONFLICT REPLACE)";;


        db.execSQL(SQL_CREATE_MOVIES_TABLE);
        db.execSQL(SQL_CREATE_TRAILERS_TABLE);
        db.execSQL(SQL_CREATE_REVIEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+Movies.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Trailers.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+Reviews.TABLE_NAME);
        onCreate(db);
    }
}
