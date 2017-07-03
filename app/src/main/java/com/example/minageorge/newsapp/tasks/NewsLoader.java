package com.example.minageorge.newsapp.tasks;

/**
 * Created by mina george on 03-Jul-17.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

import com.example.minageorge.newsapp.network.CheckConnection;
import com.example.minageorge.newsapp.pojos.News;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private List<News> cachedData;
    private Bundle mBundle;
    private int status;
    private String Data;
    private List<News> datalist = new ArrayList<>();
    private News ne;
    private Context mContext;

    public NewsLoader(Context context, Bundle bundle) {
        super(context);
        this.mBundle = bundle;
        this.mContext = context;
    }

    @Override
    protected void onStartLoading() {
        if (cachedData == null) {
            forceLoad();
        } else {
            super.deliverResult(cachedData);
        }
    }

    @Override
    public List<News> loadInBackground() {
        if (new CheckConnection(mContext).isconnected()) {
            try {
                URL url = new URL(mBundle.getString("url"));
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(15000);
                httpURLConnection.connect();
                status = httpURLConnection.getResponseCode();
                if (status == 200) {
                    InputStream in = new BufferedInputStream(httpURLConnection.getInputStream());
                    Data = Stream2String(in);
                    httpURLConnection.disconnect();
                    JSONObject jsonObject = new JSONObject(Data);
                    JSONArray jsonArray = jsonObject.getJSONObject("response").getJSONArray("results");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        if (obj.getJSONObject("fields").has("thumbnail")) {
                            ne = new News();
                            ne.setType(obj.getString("type"));
                            ne.setThumbnail(obj.getJSONObject("fields").getString("thumbnail"));
                            ne.setWebPublicationDate(obj.getString("webPublicationDate"));
                            ne.setWebTitle(obj.getString("webTitle"));
                            ne.setWebUrl(obj.getString("webUrl"));
                            datalist.add(ne);
                        }
                    }
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            datalist.clear();
        }
        return datalist;
    }

    public String Stream2String(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        String Text = "";
        try {
            while ((line = bufferedReader.readLine()) != null) {
                Text += line;
            }
            inputStream.close();
        } catch (Exception e) {
        }
        return Text;
    }
}
