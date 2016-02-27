package com.randeep.myapp.popularmoviesstage2;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.randeep.myapp.popularmoviesstage2.apiCall.FetchMovieTask;
import com.randeep.myapp.popularmoviesstage2.bean.MovieDetail;
import com.randeep.myapp.popularmoviesstage2.data.MovieContract;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by randeep on 2/7/16.ArrayList<MovieDetail> movieDetailArrayList;
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    View rootView;
    GridLayoutCursorAdapter gridLayoutCursorAdapter;
    @Bind(R.id.movies_list)
    RecyclerView moviesListView;
    public int mPosition = RecyclerView.NO_POSITION;
    private static final String SELECTED_KEY = "selected_position";
    GridLayoutManager gridLayoutManager;


    private static final int MOVIE_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies._ID,
            MovieContract.Movies.POSTER_PATH,
            MovieContract.Movies.ADULT,
            MovieContract.Movies.OVERVIEW,
            MovieContract.Movies.RELEASE_DATE,
            MovieContract.Movies.MOVIE_ID,
            MovieContract.Movies.ORIGINAL_TITLE,
            MovieContract.Movies.ORIGINAL_LANGUAGE,
            MovieContract.Movies.TITLE,
            MovieContract.Movies.BACKDROP_PATH,
            MovieContract.Movies.POPULARITY,
            MovieContract.Movies.VOTE_COUNT,
            MovieContract.Movies.VOTE_AVERAGE,
            MovieContract.Movies.DOWNLOADED,
            MovieContract.Movies.SORT_BY
    };

    private static final String[] FAVOURITE_COLUMNS = {
            MovieContract.Favourite.TABLE_NAME + "." + MovieContract.Favourite._ID,
            MovieContract.Favourite.POSTER_PATH,
            MovieContract.Favourite.ADULT,
            MovieContract.Favourite.OVERVIEW,
            MovieContract.Favourite.RELEASE_DATE,
            MovieContract.Favourite.MOVIE_ID,
            MovieContract.Favourite.ORIGINAL_TITLE,
            MovieContract.Favourite.ORIGINAL_LANGUAGE,
            MovieContract.Favourite.TITLE,
            MovieContract.Favourite.BACKDROP_PATH,
            MovieContract.Favourite.POPULARITY,
            MovieContract.Favourite.VOTE_COUNT,
            MovieContract.Favourite.VOTE_AVERAGE,
            MovieContract.Movies.DOWNLOADED,
    };

    public static int COL_ID = 0;
    public static int COL_POSTER_PATH = 1;
    public static int COL_ADULT = 2;
    public static int COL_OVERVIEW = 3;
    public static int COL_RELEASE_DATE = 4;
    public static int COL_MOVIE_ID = 5;
    public static int COL_ORIGINAL_TITLE = 6;
    public static int COL_ORIGINAL_LANGUAGE = 7;
    public static int COL_TITLE = 8;
    public static int COL_BACKDROP_PATH = 9;
    public static int COL_POPULARITY = 10;
    public static int COL_VOTE_COUNT = 11;
    public static int COL_VOTE_AVERAGE = 12;
    public static int COL_DOWNLOADED = 13;
    public static int COL_SORT_BY = 14;

    public interface Callback {

        void onItemSelected(String movieUri);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Utility.hasNetworkConnection(getActivity())) {
            Toast.makeText(getContext(), "Network Not Available!", Toast.LENGTH_LONG).show();
        }
        updateMovieList();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutCursorAdapter = new GridLayoutCursorAdapter(getActivity(), null);
        moviesListView.setLayoutManager(gridLayoutManager);
        moviesListView.setAdapter(gridLayoutCursorAdapter);

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {

            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        return rootView;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortBy = Utility.getPreferredSorting(getActivity());
        Uri movie = MovieContract.Movies.buildMovieUri();
        Uri favourite = MovieContract.Favourite.buildMovieUri();
        if (sortBy.equalsIgnoreCase(getString(R.string.pref_sorting_favourite_value))) {
            return new CursorLoader(getActivity(),
                    favourite,
                    FAVOURITE_COLUMNS,
                    null,
                    null,
                    null);
        }
        return new CursorLoader(getActivity(),
                movie,
                MOVIE_COLUMNS,
                MovieContract.Movies.SORT_BY + " = ?",
                new String[]{sortBy},
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() == 0 && Utility.getPreferredSorting(getActivity()).equalsIgnoreCase("favourite")) {
            Toast.makeText(getActivity(), getString(R.string.nothing_fav), Toast.LENGTH_SHORT).show();
        }
        if (getActivity() instanceof MainActivity) {

            mPosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
        }
        if(mPosition != RecyclerView.NO_POSITION) {

            moviesListView.smoothScrollToPosition(mPosition);

        }
        gridLayoutCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        gridLayoutCursorAdapter.swapCursor(null);
    }

    private void updateMovieList() {

        String sortBy = Utility.getPreferredSorting(getContext());
        if (!sortBy.equalsIgnoreCase(getString(R.string.pref_sorting_favourite_value)))
            new FetchMovieTask(getActivity(), sortBy);
    }

    public void onSortingChanged() {
        updateMovieList();
        getLoaderManager().restartLoader(MOVIE_LOADER, null, this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        mPosition = gridLayoutManager.findFirstCompletelyVisibleItemPosition();

        if (mPosition != RecyclerView.NO_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }
}
