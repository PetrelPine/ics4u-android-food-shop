package com.example.admin.ccb.bean;

import java.util.List;


public class HomeGoodsBean {
    public List<Data> data;

    public static class Data {
        public int icon;
        public String title;
        public String content;
        public List<PicList> images;

        public static class PicList {
            public String pic;
            public Integer src;
        }
    }
}
