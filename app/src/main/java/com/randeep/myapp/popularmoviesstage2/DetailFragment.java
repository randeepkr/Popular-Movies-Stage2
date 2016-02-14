package com.randeep.myapp.popularmoviesstage2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.randeep.myapp.popularmoviesstage2.bean.MovieDetail;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by randeep on 2/14/16.
 */
public class DetailFragment extends Fragment {

    @Bind(R.id.backgroundImage)
    ImageView backgroundImage;

    @Bind(R.id.poster)
    ImageView posterImage;

    @Bind(R.id.overview)
    TextView overView;

    MovieDetail movieDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail_fragment, container, false);
        ButterKnife.bind(this, rootView);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            movieDetail = bundle.getParcelable("MOVIE_DETAIL");
        }

        Picasso.with(getActivity())
                .load(movieDetail.getPosterPath())
                .into(posterImage);

        Picasso.with(getActivity())
                .load(movieDetail.getBackdropPath())
                .into(backgroundImage);

        overView.setText(movieDetail.getOverview());

        return rootView;
    }
}
