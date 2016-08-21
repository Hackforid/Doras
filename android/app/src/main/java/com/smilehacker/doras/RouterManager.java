package com.smilehacker.doras;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhouquan on 16/8/20.
 */
public class RouterManager {

    private final static String TAG = RouterManager.class.getName();

    private static RouterManager mInstance;
    private Context mContext;

    private Router mRouter;

    public static void init(Context context) {
        mInstance = new RouterManager(context);
        mInstance.load();
    }

    public static RouterManager inst() {
        if (mInstance == null) {
            throw new IllegalStateException("router manger should init first");
        }

        return mInstance;
    }

    private RouterManager(Context context) {
        mContext = context.getApplicationContext();
    }


    public RouterResult matchUrl(String url) {
        Router.Item matchItem = null;
        Matcher matcher = null;
        if (mRouter.items == null) {
            return null;
        }
        for (Router.Item item : mRouter.items) {
            if (item.pattern == null) {
                item.pattern = Pattern.compile(item.uri);
            }
            Matcher m = item.pattern.matcher(url);
            if (!m.find()) {
                continue;
            } else {
                matchItem = item;
                matcher = m;
                break;
            }
        }

        if (matchItem == null) {
            return null;
        }

        Bundle bundle = new Bundle();
        for (Map.Entry<String, String> entry: matcher.namedGroups().entrySet()) {
            String key = entry.getKey();
            String val = entry.getValue();

            insertBundleVal(bundle, key, val);
            if (val == null) {
                continue;
            }
        }

        parseUrlParams(url, bundle);

        RouterResult result = new RouterResult();
        result.item = matchItem;
        result.url = url;
        result.data = bundle;

        return result;
    }

    private void parseUrlParams(String url, Bundle bundle) {
        Uri uri;
        try {
            uri = Uri.parse(url);
        } catch (Exception e) {
            return;
        }

        for (String key : uri.getQueryParameterNames()) {
            String val = uri.getQueryParameter(key);
            insertBundleVal(bundle, key, val);
        }

    }

    private void insertBundleVal(Bundle bundle, String key, String val) {
        if (val == null) {
            return;
        }
        if (DataUtil.isInt(val)) {
            bundle.putInt(key, Integer.parseInt(val));
        } else if (DataUtil.isFloat(val)) {
            bundle.putFloat(key, Float.parseFloat(val));
        } else {
            bundle.putString(key, val);
        }

    }

    private void load() {
        loadFromPrefs();
        loadFromServer();
    }

    private void loadFromPrefs() {
        SharedPreferences preferences = mContext.getSharedPreferences("router", 0);
        String val = preferences.getString("router", null);
        if (val == null) {
            return;
        }
        Gson gson = new Gson();
        try {
           mRouter = gson.fromJson(val, Router.class);
        } catch (Exception e) {
            Log.e(TAG, "err parse router from pref", e);
        }
    }

    private void loadFromServer() {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... voids) {
                return loadRouter();
            }

            @Override
            protected void onPostExecute(String str) {
                super.onPostExecute(str);
                if (TextUtils.isEmpty(str)) {
                    return;
                }
                Log.i(TAG, "get server = " + str);

                Gson gson = new Gson();
                try {
                    mRouter = gson.fromJson(str, Router.class);
                } catch (Exception e) {
                    Log.e(TAG, "err parse router from pref", e);
                }

                if (mRouter == null) {
                    Log.i(TAG, "...");
                }

            }
        }.execute();
    }

    private String loadRouter() {
        String url = "http://192.168.1.12:9999/router";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            Log.e(TAG, "err load router from server", e);
            return null;
        }
    }

}
