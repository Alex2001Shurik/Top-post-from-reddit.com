package com.toppostsfromredditcom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.toppostsfromredditcom.model.children.Data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class ModelAdapter extends BaseAdapter {

    Activity myActivity;
    List<Data> models;
    FileOutputStream outputStream;

    public ModelAdapter(Activity activity, List<Data> models){
        this.myActivity = activity;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Data getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"SetTextI18n", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View oneModelLine;

        LayoutInflater inflater = (LayoutInflater) myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        oneModelLine = inflater.inflate(R.layout.row, viewGroup, false);

        Data model = this.getItem(i);

        TextView author = oneModelLine.findViewById(R.id.author);
        TextView numComments = oneModelLine.findViewById(R.id.num_comments);
        TextView title = oneModelLine.findViewById(R.id.title);
        TextView created = oneModelLine.findViewById(R.id.created);
        ImageView imageView = oneModelLine.findViewById(R.id.thumbnail);

        author.setText("Posted by " + model.getAuthor());
        numComments.setText(model.getNumComments() + " comments");
        title.setText(model.getTitle());
        created.setText("Created " + (model.getCreated()) + " hours ago");
        Glide.with(oneModelLine).load(model.getThumbnail()).into(imageView);

        imageView.setOnClickListener(view1 -> {
            Intent intent = new Intent(myActivity, ImageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("url", model.getUrl());
            intent.putExtras(bundle);
            myActivity.startActivity(intent);
        });


        return oneModelLine;
    }

}
