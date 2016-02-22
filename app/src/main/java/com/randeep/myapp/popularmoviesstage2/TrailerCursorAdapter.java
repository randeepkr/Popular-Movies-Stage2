package com.randeep.myapp.popularmoviesstage2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by randeep on 2/21/16.
 */
public class TrailerCursorAdapter extends CursorAdapter {

    Context mContext;

    public TrailerMovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    public TrailerCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        View container;

        @Bind(R.id.trailerImage)
        ImageView trailerImage;

        @Bind(R.id.trailerName)
        TextView trailerTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
    }

    @Override
    public void onBindViewHolder(TrailerCursorAdapter.ViewHolder viewHolder, Cursor cursor) {

        String urlSource = cursor.getString(DetailFragment.COL_TRAILER_SOURCE);
        final String BASE_URL="http://img.youtube.com/vi/";
        String imageUrl = BASE_URL+urlSource+"/0.jpg";
        String trailerUrl="https://www.youtube.com/watch?v="+cursor.getString(
                DetailFragment.COL_TRAILER_SOURCE);
        viewHolder.trailerImage.setTag(trailerUrl);
        Picasso.with(mContext)
                .load(imageUrl)
                .into(viewHolder.trailerImage);


        viewHolder.trailerTitle.setText(cursor.getString(DetailFragment.COL_TRAILER_NAME));


        viewHolder.trailerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(v.getTag().toString()));
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public TrailerCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View container = layoutInflater.inflate(R.layout.trailer_card, parent, false);


        return new ViewHolder(container);
    }
}
