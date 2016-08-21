package com.smilehacker.doras;

import com.google.code.regexp.Pattern;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhouquan on 16/8/20.
 */
public class Router {

    @SerializedName("routers")
    public List<Item> items;

    public int version;

    public static class Item {
        public String uri;

        @SerializedName("android") public String compomentID;

        @SerializedName("web") public boolean focusWeb;

        public Pattern pattern;
    }
}
