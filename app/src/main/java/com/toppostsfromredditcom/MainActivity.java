package com.toppostsfromredditcom;

import android.os.Build;
import android.os.Bundle;

import com.toppostsfromredditcom.model.Feed;
import com.toppostsfromredditcom.model.children.Children;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.toppostsfromredditcom.model.children.Data;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Button next, previous;

    private static final String TAG = "MainActivity";

    private ListView listView;

    private ModelAdapter modelAdapter;

    private Pagination pagination;

    private int lastPage;

    private int currentPage = 0;

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
                listView = findViewById(R.id.list_view);

                pagination = new Pagination(5, dataList);
                lastPage = pagination.getLastPage();
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Something went wrong" + t.getMessage());
            }
        });

        next = findViewById(R.id.btn_next);
        previous = findViewById(R.id.btn_previous);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage += 1;
                updateData();
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage -= 1;
                updateData();
            }
        });


    }
    private void updateData(){
        modelAdapter = new ModelAdapter(MainActivity.this, pagination.generateData(currentPage));
        listView.setAdapter(modelAdapter);
        updateButtons();
    }

    private void updateButtons() {
        if(currentPage == 0){
            next.setEnabled(true);
            previous.setEnabled(false);
        } else if(currentPage == lastPage){
            next.setEnabled(false);
            previous.setEnabled(true);
        } else {
            next.setEnabled(true);
            previous.setEnabled(true);
        }
    }
}