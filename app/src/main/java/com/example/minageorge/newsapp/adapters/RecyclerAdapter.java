package com.example.minageorge.newsapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.minageorge.newsapp.R;
import com.example.minageorge.newsapp.pojos.News;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mina george on 03-Jul-17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<News> mArrayList = new ArrayList<News>();
    private Context mContext;
    private Intent mIntent;
    private Activity mActivity;

    public RecyclerAdapter(Context context, Activity activity) {
        this.mContext = context;
        this.mActivity = activity;
    }

    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, final int position) {
        try {
            Picasso.with(mContext).load(mArrayList.get(position).getThumbnail()).into(holder.image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.title.setText(mArrayList.get(position).getWebTitle());
        holder.date.setText(mContext.getString(R.string.date_header) + mArrayList.get(position).getWebPublicationDate().substring(0,10));
        holder.type.setText(mContext.getString(R.string.type_header)+ mArrayList.get(position).getType());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(mArrayList.get(position).getWebUrl()));
                mActivity.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mArrayList.size();
    }

    public void swapdata(Collection<News> data) {
        this.mArrayList.clear();
        this.mArrayList.addAll(data);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_image)
        ImageView image;

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.type)
        TextView type;

        @BindView(R.id.date)
        TextView date;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
