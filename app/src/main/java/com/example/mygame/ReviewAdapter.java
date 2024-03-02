package com.example.mygame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

public class ReviewAdapter extends ArrayAdapter<String> {

    private ArrayList<String> names;
    private ArrayList<Integer> ratings;
    private ArrayList<String> comments;

    public ReviewAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> names, @NonNull ArrayList<Integer> ratings, @NonNull ArrayList<String> comments) {
        super(context, resource, names);
        this.names = names;
        this.ratings = ratings;
        this.comments = comments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, parent, false);
        }

        TextView textName = convertView.findViewById(R.id.textName);
        RatingBar ratingBar = convertView.findViewById(R.id.ratingBar);
        TextView textComment = convertView.findViewById(R.id.textComment);

        textName.setText(names.get(position));
        ratingBar.setRating(ratings.get(position));
        textComment.setText(comments.get(position));

        return convertView;
    }
}

