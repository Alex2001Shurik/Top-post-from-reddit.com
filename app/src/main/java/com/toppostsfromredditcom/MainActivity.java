package com.toppostsfromredditcom;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import com.toppostsfromredditcom.model.Feed;
import com.toppostsfromredditcom.model.children.Children;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import android.util.Log;

import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.toppostsfromredditcom.model.children.Data;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends Activity {


    Button next, previous;

    private static final String TAG = "MainActivity";

    private ListView listView;

    private ModelAdapter modelAdapter;

    private Pagination pagination;

    private int lastPage;

    private int currentPage = 0;

    private static final String BASE_URL = "https://www.reddit.com/top/";





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

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
                ArrayList<Data> dataList = new ArrayList<>();
                Log.d(TAG, "onResponse: Server Response: " + response.toString());

                ArrayList<Children> childrenArrayList = response.body().getData().getChildren();
                for (int i = 0; i < childrenArrayList.size(); i++) {
                    String title = childrenArrayList.get(i).getData().getTitle();
                    long hours = Data.convertDate(childrenArrayList.get(i).getData().getCreated());
                    String url = childrenArrayList.get(i).getData().getUrl();
                    long numComments = childrenArrayList.get(i).getData().getNumComments();
                    String author = childrenArrayList.get(i).getData().getAuthor();
                    String thumbnail = childrenArrayList.get(i).getData().getThumbnail();
                    if (!thumbnail.equals("default") && (url.contains(".jpg") || url.contains(".png") || url.contains(".jpeg"))) {
                        dataList.add(new Data(title, url, numComments, author, hours, thumbnail));
                        Log.d(TAG, "onResponse: \n" +
                                new Data(title, url, numComments, author, hours, thumbnail).toString() + "\n" +
                                "---------------------------------------------------------\n\n");
                    }
                }
                listView = findViewById(R.id.list);
                pagination = new Pagination(5, dataList);
                lastPage = pagination.getLastPage();
                updateData();
            }
            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Log.e(TAG, "onFailure: Something went wrong" + t.getMessage());
            }
        });
        next = findViewById(R.id.btn_next);
        previous = findViewById(R.id.btn_previous);
        next.setOnClickListener(view -> {
            currentPage += 1;
            updateData();
        });
        previous.setOnClickListener(view -> {
            currentPage -= 1;
            updateData();
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //outState.putString(String.valueOf((++currentPage)), page.getText().toString());
        super.onSaveInstanceState(outState);
        outState.putInt("currentPage", currentPage);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentPage = savedInstanceState.getInt("currentPage");
    }

    private void updateData() {
        modelAdapter = new ModelAdapter(MainActivity.this, pagination.generateData(currentPage));
        listView.setAdapter(modelAdapter);
        updateButtons();
    }


    private void updateButtons() {
        if (currentPage == 0) {
            next.setEnabled(true);
            previous.setEnabled(false);
        } else if (currentPage == lastPage) {
            next.setEnabled(false);
            previous.setEnabled(true);
        } else {
            next.setEnabled(true);
            previous.setEnabled(true);
        }
    }

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    public ArrayList<Data> getData() throws IOException {
        InputStream in_s = this.getAssets().open("top.json");
        Feed data;
        JsonReader jsonReader = new JsonReader( new InputStreamReader(in_s, StandardCharsets.UTF_8));
        Gson gson = new GsonBuilder().create();

        data = gson.fromJson(jsonReader, Feed.class);
        List<Children> children = data.getData().getChildren();
        ArrayList<Data> dataList = new ArrayList<>();
        for(int i = 0; i < children.size(); i++) {
            String title = children.get(i).getData().getTitle();
            long hours = Data.convertDate(children.get(i).getData().getCreated());
            String url = children.get(i).getData().getUrl();
            long numComments = children.get(i).getData().getNumComments();
            String author = children.get(i).getData().getAuthor();
            String thumbnail = children.get(i).getData().getThumbnail();
            if (!thumbnail.equals("default") && (url.contains(".jpg") || url.contains(".png") || url.contains(".jpeg"))) {
                dataList.add(new Data(title, url, numComments, author, hours, thumbnail));
            }
        }
        return dataList;
    }*/
}