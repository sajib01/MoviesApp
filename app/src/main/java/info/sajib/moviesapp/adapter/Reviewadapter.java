package info.sajib.moviesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import info.sajib.moviesapp.R;
import info.sajib.moviesapp.pojo.Review;

/**
 * Created by sajib on 09-04-2016.
 */
public class Reviewadapter extends RecyclerView.Adapter<Reviewadapter.Viewholder> {
    Context context;
    List<Review> reviews = Collections.emptyList();

    public Reviewadapter(Context context, List<Review> reviews) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Viewholder(LayoutInflater.from(context).inflate(R.layout.activity_description_review_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(Reviewadapter.Viewholder holder, int position) {
        Review data = reviews.get(position);
        holder.authorname.setText(data.getAuthor());
        holder.content.setText(data.getContent());

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        TextView authorname;
        TextView content;

        public Viewholder(View itemView) {
            super(itemView);
            authorname = (TextView) itemView.findViewById(R.id.activity_description_review_layout_authorname);
            content = (TextView) itemView.findViewById(R.id.activity_description_review_layout_content);
        }
    }
}