package com.toppostsfromredditcom;


import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

///title - название публикации
///url - картинка в полный рост
///num_comments - количество комментариев
///author - автор публикации
///created(_utc) - создание поста

public class ParseJson {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertData(Long date) {
        Long created = Long.parseLong(date.toString() + "000");
        Long epoch = Instant.now().toEpochMilli();
        long diff = epoch - created;
        //int diffDays = (int) (diff / (24 * 60 * 60 * 1000));
        //System.out.println("days:" + diffDays);
        int diffHours = (int) (diff / (60 * 60 * 1000));
        String s = "";
        if (diffHours / 24 == 1) {
            s += diffHours % 24 + " day";
        } else if (diffHours / 24 >= 1) {
            s += diffHours / 24 + " days";
        } else if (diffHours == 0) {
            s += "now";
        } else if (diffHours == 1) {
            s += diffHours + " hour";
        } else if (diffHours > 1) {
            s += diffHours + " hours";
        }

        return s + " ago";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Model> getData() throws IOException, JSONException, URISyntaxException {
        //URL url1 = new URL("https://www.reddit.com/top.json?limit=8");
        //URLConnection con = url1.openConnection();
        //Scanner in = new Scanner((InputStream) con.getContent());
        //String result = "";
        //while (in.hasNext()) {
        //    result += in.nextLine();
        //}
        FileReader reader = new FileReader("C:\\Users\\Alex\\AndroidStudioProjects\\Toppostsfromredditcom\\app\\src\\main\\java\\com\\toppostsfromredditcom\\top.json");
        JSONTokener tokener = new JSONTokener(reader.toString());
        List<Model> models = new LinkedList<>();
        JSONObject object = new JSONObject(tokener);
        JSONObject data = object.getJSONObject("data");
        JSONArray children = data.getJSONArray("children");
        for (int i = 0; i < children.length(); i++) {
            JSONObject childrenData = children.getJSONObject(i).getJSONObject("data");
            String title = childrenData.getString("title");
            String hours = ParseJson.convertData(childrenData.getLong("created_utc"));
            String url = childrenData.getString("url");
            long numComments = childrenData.getLong("num_comments");
            String author = childrenData.getString("author");
            String thumbnail = childrenData.getString("thumbnail");
            if(!thumbnail.equals("default") && (url.contains(".jpg") || url.contains(".png") || url.contains(".jpeg")))
                models.add(new Model(title, url, numComments, author, hours, thumbnail));

        }
        return models;
    }
}
