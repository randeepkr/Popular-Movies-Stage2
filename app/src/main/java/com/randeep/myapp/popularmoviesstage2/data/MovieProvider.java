package com.randeep.myapp.popularmoviesstage2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by randeep on 2/15/16.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private MovieDbHelper movieDbHelper;

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;

    static final int TRAILERS=200;
    static final int TRAILERS_WITH_ID=201;

    static final int REVIEWS=300;
    static final int REVIEWS_WITH_ID=301;


    @Override
    public boolean onCreate() {

        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor mCursor;

        switch (uriMatcher.match(uri)){

            case MOVIE:
                mCursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.Movies.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case MOVIE_WITH_ID:
                mCursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.Movies.TABLE_NAME,
                        projection,
                        MovieContract.Movies.MOVIE_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRAILERS:
                mCursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.Trailers.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case TRAILERS_WITH_ID:
                mCursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.Trailers.TABLE_NAME,
                        projection,
                        MovieContract.Trailers.MOVIE_ID+" = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            case REVIEWS:
                mCursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.Reviews.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case REVIEWS_WITH_ID:
                mCursor = movieDbHelper.getReadableDatabase().query(
                        MovieContract.Reviews.TABLE_NAME,
                        projection,
                        MovieContract.Reviews.MOVIE_ID+" = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        mCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return mCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);

        switch (match){
            case MOVIE:
                return MovieContract.Movies.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.Movies.CONTENT_ITEM_TYPE;
            case TRAILERS :
                return MovieContract.Trailers.CONTENT_TYPE;
            case TRAILERS_WITH_ID:
                return MovieContract.Trailers.CONTENT_ITEM_TYPE;
            case REVIEWS :
                return MovieContract.Reviews.CONTENT_TYPE;
            case REVIEWS_WITH_ID:
                return MovieContract.Reviews.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;
        switch (match){
            case MOVIE:
                long _id = sqLiteDatabase.insert(MovieContract.Movies.TABLE_NAME,null,values);
                if (_id > 0){
                    returnUri = MovieContract.Movies.buildMoviesUri(_id);
                }else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            case TRAILERS:
                long _idTrailers = sqLiteDatabase.insert(MovieContract.Trailers.TABLE_NAME, null, values);
                if ( _idTrailers > 0 )
                    returnUri = MovieContract.Trailers.buildMoviesUri(_idTrailers);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            case REVIEWS:
                long _idReviews = sqLiteDatabase.insert(MovieContract.Reviews.TABLE_NAME, null, values);
                if ( _idReviews > 0 )
                    returnUri = MovieContract.Reviews.buildMoviesUri(_idReviews);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        int rowsDeleted;

        if ( null == selection ) selection = "1";
        switch (match){
            case MOVIE:
                rowsDeleted = sqLiteDatabase.delete(MovieContract.Movies.TABLE_NAME,selection,selectionArgs);
                break;
            case MOVIE_WITH_ID:
                rowsDeleted = sqLiteDatabase.delete(MovieContract.Movies.TABLE_NAME,
                        MovieContract.Movies.MOVIE_ID + " = ?",
                        new String[]{MovieContract.Movies.getMovieIdFromUri(uri)});
                break;
            case TRAILERS:
                rowsDeleted = sqLiteDatabase.delete(
                        MovieContract.Trailers.TABLE_NAME, selection, selectionArgs);
                break;
            case TRAILERS_WITH_ID:
                rowsDeleted = sqLiteDatabase.delete(MovieContract.Trailers.TABLE_NAME,
                        MovieContract.Trailers.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case REVIEWS:
                rowsDeleted = sqLiteDatabase.delete(
                        MovieContract.Reviews.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEWS_WITH_ID:
                rowsDeleted = sqLiteDatabase.delete(MovieContract.Reviews.TABLE_NAME,
                        MovieContract.Reviews.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase sqLiteDatabase = movieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        int rowsUpdated;

        switch (match){
            case MOVIE:
                rowsUpdated = sqLiteDatabase.update(MovieContract.Movies.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIE_WITH_ID:
                rowsUpdated = sqLiteDatabase.update(MovieContract.Movies.TABLE_NAME, values,
                        MovieContract.Movies.MOVIE_ID + " = ?",
                        new String[]{MovieContract.Movies.getMovieIdFromUri(uri)});
                break;
            case TRAILERS:
                rowsUpdated = sqLiteDatabase.update(MovieContract.Trailers.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case TRAILERS_WITH_ID:
                rowsUpdated = sqLiteDatabase.update(MovieContract.Trailers.TABLE_NAME,
                        values,
                        MovieContract.Trailers.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            case REVIEWS:
                rowsUpdated = sqLiteDatabase.update(MovieContract.Reviews.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case REVIEWS_WITH_ID:
                rowsUpdated = sqLiteDatabase.update(MovieContract.Reviews.TABLE_NAME,
                        values,
                        MovieContract.Reviews.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);

        switch (match){
            case MOVIE:
                db.beginTransaction();
                int returnCount = 0;
                try{
                    for (ContentValues value : values){
                        long _id = db.insert(MovieContract.Movies.TABLE_NAME, null, value);
                        if (_id > 0){
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                }finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case TRAILERS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MovieContract.Trailers.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            case REVIEWS:
                db.beginTransaction();
                returnCount = 0;
                try {
                    for (ContentValues value : values) {

                        long _id = db.insert(MovieContract.Reviews.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    static UriMatcher buildUriMatcher(){

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/*", MOVIE_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_TRAILERS, TRAILERS);
        matcher.addURI(authority, MovieContract.PATH_TRAILERS + "/*", TRAILERS_WITH_ID);

        matcher.addURI(authority, MovieContract.PATH_REVIEWS, REVIEWS);
        matcher.addURI(authority, MovieContract.PATH_REVIEWS + "/*", REVIEWS_WITH_ID);

        return matcher;
    }
}
