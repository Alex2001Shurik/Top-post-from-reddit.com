package com.toppostsfromredditcom;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.toppostsfromredditcom.model.Feed;
import com.toppostsfromredditcom.model.children.Children;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ListAdapter;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.toppostsfromredditcom.model.children.Data;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ListView listView;

    ModelAdapter modelAdapter;

    private static final String BASE_URL = "https://www.reddit.com/top/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RedditApi redditApi = retrofit.create(RedditApi.class);
        Call<Feed> call = redditApi.getData();

        call.enqueue(new Callback<Feed>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> response) {
                ArrayList<Model> dataList = new ArrayList<>();
                Log.d(TAG, "onResponse: Server Response: " + response.toString());

                ArrayList<Children> childrenArrayList = response.body().getData().getChildren();
                for(int i = 0; i < childrenArrayList.size(); i++){
                    String title = childrenArrayList.get(i).getData().getTitle();
                    long hours = Data.convertDate(childrenArrayList.get(i).getData().getCreated());
                    String url = childrenArrayList.get(i).getData().getUrl();
                    long numComments = childrenArrayList.get(i).getData().getNumComments();
                    String author = childrenArrayList.get(i).getData().getAuthor();
                    String thumbnail = childrenArrayList.get(i).getData().getThumbnail();
                    if(!thumbnail.equals("default") && (url.contains(".jpg") || url.contains(".png") || url.contains(".jpeg"))) {
                        dataList.add(new Model(title, url, numComments, author, hours, thumbnail));
                        Log.d(TAG, "onResponse: \n" +
                                new Model(title, url, numComments, author, hours, thumbnail).toString() +"\n" +
                                "---------------------------------------------------------\n\n");
                    }

                }
                listView = (ListView) findViewById(R.id.list_view);

                modelAdapter = new ModelAdapter(MainActivity.this, dataList);

                listView.setAdapter(modelAdapter);
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Something went wrong" + t.getMessage());
            }
        });


    }
}