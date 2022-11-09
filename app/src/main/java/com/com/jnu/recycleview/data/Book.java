package com.com.jnu.recycleview.data;

import java.io.Serializable;

public class Book implements Serializable{//对象序列化
    public Book(String title,int coverResourceId) {
        Title = title;
        CoverResourceId = coverResourceId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getCoverResourceId() {
        return CoverResourceId;
    }

    public void setCoverResourceId(int coverResourceId) {
        CoverResourceId = coverResourceId;
    }

    private String Title;
    private int CoverResourceId;
}
