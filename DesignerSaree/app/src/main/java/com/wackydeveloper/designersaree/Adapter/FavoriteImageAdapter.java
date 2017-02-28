package com.wackydeveloper.designersaree.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wackydeveloper.designersaree.R;

import java.util.ArrayList;

/**
 * Created by hp on 1/3/17.
 */

public class FavoriteImageAdapter extends RecyclerView.Adapter<FavoriteImageAdapter.UrlHolder> {
    private static String LOG_TAG = "FavoriteImageAdapter";
    private ArrayList<String> urlList;
    private static UrlHolder.ImageClickListener imgClickListener;


    public static class UrlHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        TextView dateTime;

        public UrlHolder(View itemView) {
            super(itemView);
//            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
//            imgClickListener.onItemClick(getPosition(), v);
        }
        public void setOnItemClickListener(ImageClickListener) {
//            this.myClickListener = myClickListener;
        }

//        public MyRecyclerViewAdapter(ArrayList<String> urlList) {
////            this.url = myDataset;
//        }

      //  @Override
        public UrlHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout., parent, false);

            UrlHolder holder = new UrlHolder(view);
            return holder;
        }

//        @Override
        public void onBindViewHolder(UrlHolder holder, int position) {
//            holder.label.setText(mDataset.get(position).getmText1());
//            holder.dateTime.setText(mDataset.get(position).getmText2());
        }

        public void addItem(String url, int index) {
//            mDataset.add(dataObj);
//            notifyItemInserted(index);
        }

        public void deleteItem(int index) {
//            mDataset.remove(index);
//            notifyItemRemoved(index);
        }

//        @Override
        public int getItemCount() {
//            return mDataset.size();
        }

        public interface ImageClickListener {
            public void onItemClick(int position, View v);
        }

    }
}
