package com.example.utente.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;
        if (listView == null) {
            listView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
        }

        News currentArticle = getItem(position);

        ImageView illustrationView = listView.findViewById(R.id.illustration);

        Picasso.get().load(currentArticle.getArticleImage()).into(illustrationView);

        TextView articleTitleView = listView.findViewById(R.id.article_title);

        articleTitleView.setText(currentArticle.getArticleTitle());

        TextView articleSectionView = listView.findViewById(R.id.article_section);

        articleSectionView.setText(currentArticle.getArticleSection());

        TextView articleAuthorView = listView.findViewById(R.id.article_author);

        if (currentArticle.getArticleAuthor() != "") {
            articleAuthorView.setText(currentArticle.getArticleAuthor());

            articleAuthorView.setVisibility(View.VISIBLE);
        } else {

            articleAuthorView.setVisibility(View.GONE);
        }

        TextView articleDateView = null;
        TextView articleTimeView = null;
        if (currentArticle.getArticleDate() != null) {
            articleDateView = listView.findViewById(R.id.article_date);

            String date_formatted = formatDate(currentArticle.getArticleDate()).concat(",");

            articleDateView.setText(date_formatted);

            articleTimeView = listView.findViewById(R.id.time);

            String formattedTime = formatTime(currentArticle.getArticleDate());

            articleTimeView.setText(formattedTime);

            articleDateView.setVisibility(View.VISIBLE);
            articleTimeView.setVisibility(View.VISIBLE);
        } else {

            articleDateView.setVisibility(View.GONE);
            articleTimeView.setVisibility(View.GONE);
        }

        return listView;
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}