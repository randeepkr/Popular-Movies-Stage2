package com.randeep.myapp.popularmoviesstage2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.randeep.myapp.popularmoviesstage2.apiCall.FetchMovieTask;
import com.randeep.myapp.popularmoviesstage2.bean.MovieDetail;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by randeep on 2/7/16.ArrayList<MovieDetail> movieDetailArrayList;
 */
public class MainActivityFragment extends Fragment {

    View rootView;
    GridLayoutAdapter gridLayoutAdapter;
    @Bind(R.id.movies_list)
    RecyclerView moviesListView;
    ArrayList<MovieDetail> movieDetailArrayList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        movieDetailArrayList = new ArrayList<>();

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutAdapter = new GridLayoutAdapter(getActivity(),movieDetailArrayList);
        moviesListView.setLayoutManager(gridLayoutManager);
        moviesListView.setAdapter(gridLayoutAdapter);

        new FetchMovieTask(gridLayoutAdapter);
        return rootView;
    }
}
