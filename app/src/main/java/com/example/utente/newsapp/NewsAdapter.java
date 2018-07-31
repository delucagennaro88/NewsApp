package com.example.utente.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item, parent, false);
        }

        News newsOnView = getItem(position);
        TextView titleView = (TextView) itemView.findViewById(R.id.title_txt);
        String title = newsOnView.getmArticleTitle();
        titleView.setText(title);

        TextView categoryView = (TextView) itemView.findViewById(R.id.category_txt);
        String category = newsOnView.getmArticleCategory();
        categoryView.setText(category);

        TextView dateView = (TextView) itemView.findViewById(R.id.date_txt);
        String date = newsOnView.getmArticleDate();
        dateView.setText(date);

        TextView authorView = (TextView) itemView.findViewById(R.id.author_txt);
        String author = newsOnView.getmArticleAuthor();
        authorView.setText(author);

        return itemView;
    }
}