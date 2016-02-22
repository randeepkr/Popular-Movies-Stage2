package com.randeep.myapp.popularmoviesstage2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by randeep on 2/16/16.
 */
public class GridLayoutCursorAdapter extends CursorRecyclerViewAdapter<GridLayoutCursorAdapter.ViewHolder> {

    Context mContext;

    public GridLayoutCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.movie_image)
        public ImageView movie_image;

        @Bind(R.id.year_icon)
        public TextView yearIcon;

        @Bind(R.id.year)
        public TextView yearText;

        @Bind(R.id.popularity)
        public TextView popularity;

        @Bind(R.id.popularity_icon)
        public TextView popularityIcon;

        public View container;

        public ViewHolder(View itemView) {
            super(itemView);

            container = itemView;

            ButterKnife.bind(this, itemView);

        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View container = layoutInflater.inflate(R.layout.movie_card, parent, false);


        return new ViewHolder(container);
    }

    @Override
    public void onBindViewHolder(final GridLayoutCursorAdapter.ViewHolder viewHolder, final Cursor cursor) {

        String url = cursor.getString(MainActivityFragment.COL_POSTER_PATH);

        viewHolder.yearIcon.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "icomoon.ttf"));
        viewHolder.yearIcon.setText("\ue900");
        viewHolder.itemView.setTag(cursor.getString(MainActivityFragment.COL_MOVIE_ID));
        viewHolder.yearText.setText(Utility.getYear(cursor.getString(MainActivityFragment.COL_RELEASE_DATE)));
        viewHolder.popularityIcon.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "icomoon.ttf"));
        viewHolder.popularity.setText(cursor.getDouble(MainActivityFragment.COL_VOTE_AVERAGE) + "");
        viewHolder.popularityIcon.setText("\ue902");
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.placeholder)
                .into(viewHolder.movie_image);

        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(mContext, DetailActivity.class).putExtra("MOVIE_ID",
                            v.getTag().toString());
                    mContext.startActivity(intent);
                }catch (Exception e){

                }


            }
        });

    }


}
