package com.randeep.myapp.popularmoviesstage2;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.florent37.picassopalette.PicassoPalette;
import com.randeep.myapp.popularmoviesstage2.apiCall.FetchMovieTrailersAndReviewsTask;
import com.randeep.myapp.popularmoviesstage2.bean.MovieDetail;
import com.randeep.myapp.popularmoviesstage2.data.MovieContract;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by randeep on 2/14/16.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    @Bind(R.id.backgroundImage)
    ImageView backgroundImage;

    @Bind(R.id.poster)
    ImageView posterImage;

    @Bind(R.id.share)
    FloatingActionButton shareFloatButton;

    @Bind(R.id.favourite)
    FloatingActionButton favouriteFloatButton;

    @Bind(R.id.title)
    TextView movieTitle;

    @Bind(R.id.release_date)
    TextView releaseDateText;

    @Bind(R.id.rating)
    TextView averageRating;

    @Bind(R.id.overview)
    TextView description;

    @Bind(R.id.trailersList)
    RecyclerView trailerList;

    @Bind(R.id.reviewList)
    RecyclerView reviewList;

    private static final int DETAIL_LOADER = 0;
    private static final int TRAILER_LOADER = 1;
    private static final int REVIEW_LOADER = 2;


    private static final String[] MOVIE_COLUMNS = {
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies._ID,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.POSTER_PATH,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ADULT,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.OVERVIEW,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.RELEASE_DATE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.MOVIE_ID,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ORIGINAL_TITLE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.ORIGINAL_LANGUAGE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.TITLE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.BACKDROP_PATH,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.POPULARITY,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.VOTE_COUNT,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.VOTE_AVERAGE,
            MovieContract.Movies.TABLE_NAME + "." + MovieContract.Movies.DOWNLOADED
    };

    private static final String[] TRAILER_COLUMNS = {
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers._ID,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.NAME,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.SIZE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.SOURCE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.TYPE,
            MovieContract.Trailers.TABLE_NAME + "." + MovieContract.Trailers.MOVIE_ID
    };

    private static final String[] REVIEW_COLUMNS = {
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews._ID,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.ID_REVIEWS,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.AUTHOR,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.CONTENT,
            MovieContract.Reviews.TABLE_NAME + "." + MovieContract.Reviews.MOVIE_ID
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

    public static int COL_TRAILER_ID = 0;
    public static int COL_TRAILER_NAME = 1;
    public static int COL_TRAILER_SIZE = 2;
    public static int COL_TRAILER_SOURCE = 3;
    public static int COL_TRAILER_TYPE = 4;
    public static int COL_TRAILER_MOVIE_ID = 5;

    public static int COL_REVIEWS_ID = 0;
    public static int COL_REVIEWS_ID_REVIEWS = 1;
    public static int COL_REVIEWS_AUTHOR = 2;
    public static int COL_REVIEWS_CONTENT = 3;
    public static int COL_REVIEWS_MOVIE_ID = 4;


    String movie_Id;
    LinearLayoutManager linearLayoutManager;
    TrailerCursorAdapter trailerCursorAdapter;
    ReviewsCursorAdapter reviewsCursorAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        movie_Id = getActivity().getIntent().getStringExtra("MOVIE_ID");


//        new FetchMovieTrailersAndReviewsTask(getActivity(),movie_Id);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        trailerList.setLayoutManager(linearLayoutManager);
        trailerCursorAdapter = new TrailerCursorAdapter(getActivity(), null);
        trailerList.setAdapter(trailerCursorAdapter);

        LinearLayoutManager reviewLinearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        reviewList.setLayoutManager(reviewLinearLayoutManager);
        reviewsCursorAdapter = new ReviewsCursorAdapter(getActivity(), null);
        reviewList.setAdapter(reviewsCursorAdapter);


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        getLoaderManager().initLoader(TRAILER_LOADER, null, this);
        getLoaderManager().initLoader(REVIEW_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (null != movie_Id) {
            switch (id) {
                case DETAIL_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Movies.buildMoviesUriWithMovieId(movie_Id),
                            MOVIE_COLUMNS,
                            null,
                            null,
                            null
                    );
                case TRAILER_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Trailers.buildMoviesUriWithMovieId(movie_Id),
                            TRAILER_COLUMNS,
                            null,
                            null,
                            null
                    );
                case REVIEW_LOADER:
                    return new CursorLoader(
                            getActivity(),
                            MovieContract.Reviews.buildMoviesUriWithMovieId(movie_Id),
                            REVIEW_COLUMNS,
                            null,
                            null,
                            null
                    );
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, final Cursor data) {
        final AppBarLayout appBarLayout = (AppBarLayout) getActivity().findViewById(R.id.appBarLayout);
        if (!data.moveToFirst())
            return;
        switch (loader.getId()) {
            case DETAIL_LOADER:

                Picasso.with(getActivity())
                        .load(data.getString(COL_BACKDROP_PATH))
                        .into(backgroundImage, PicassoPalette.with(data.getString(COL_BACKDROP_PATH), backgroundImage)
                                .use(PicassoPalette.Profile.VIBRANT)
                                .intoCallBack(new PicassoPalette.CallBack() {
                                    @Override
                                    public void onPaletteLoaded(Palette palette) {
                                        int color = palette.getDarkMutedColor(0);
                                        if (color == 0) {
                                            color = palette.getDarkVibrantColor(0);
                                        }

                                        appBarLayout.setBackgroundColor(color);
                                        try {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                                getActivity().getWindow().setStatusBarColor(color);
                                        } catch (NullPointerException e) {

                                        }

                                        shareFloatButton.setBackgroundTintList(new ColorStateList(new int[][]{
                                                new int[]{}}, new int[]{color}));
                                        favouriteFloatButton.setBackgroundTintList(new ColorStateList(new int[][]{
                                                new int[]{}}, new int[]{color}));

                                    }
                                }));

                Picasso.with(getActivity())
                        .load(data.getString(COL_POSTER_PATH))
                        .into(posterImage);

                if (data.getString(COL_DOWNLOADED).equalsIgnoreCase("0")) {
                    if (Utility.hasNetworkConnection(getActivity())) {
                        new FetchMovieTrailersAndReviewsTask(getActivity(), movie_Id);
                    }
                }


                movieTitle.setText(data.getString(COL_TITLE));
                releaseDateText.setText("Release Date: " + Utility.getYear(data.getString(COL_RELEASE_DATE)));
                averageRating.setText("Average Rating: " + data.getString(COL_VOTE_AVERAGE));
                description.setText(data.getString(COL_OVERVIEW));
                break;
            case TRAILER_LOADER:
                if (!data.moveToFirst())
                    return;
                trailerCursorAdapter.swapCursor(data);

                shareFloatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String trailerUrl = "https://www.youtube.com/watch?v=" + data.getString(
                                DetailFragment.COL_TRAILER_SOURCE);
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                        getActivity().startActivity(intent);
                    }
                });
                break;
            case REVIEW_LOADER:
                if (!data.moveToFirst())
                    return;
                reviewsCursorAdapter.swapCursor(data);

        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
        trailerCursorAdapter.swapCursor(null);
        reviewsCursorAdapter.swapCursor(null);
    }
}
