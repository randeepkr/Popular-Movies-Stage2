package com.randeep.myapp.popularmoviesstage2;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by randeep on 2/22/16.
 */
public class ReviewsCursorAdapter extends CursorRecyclerViewAdapter<ReviewsCursorAdapter.ViewHolder> {

    Context mContext;

    public ReviewsCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.author_name)
        public TextView author;

        @Bind(R.id.content)
        public TextView txtContent;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public void onBindViewHolder(ReviewsCursorAdapter.ViewHolder viewHolder, Cursor cursor) {

        viewHolder.author.setText(cursor.getString(DetailFragment.COL_REVIEWS_AUTHOR));
        viewHolder.txtContent.setText(cursor.getString(DetailFragment.COL_REVIEWS_CONTENT));

    }

    @Override
    public ReviewsCursorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View container = layoutInflater.inflate(R.layout.review_card, parent, false);
        return new ViewHolder(container);
    }
}
