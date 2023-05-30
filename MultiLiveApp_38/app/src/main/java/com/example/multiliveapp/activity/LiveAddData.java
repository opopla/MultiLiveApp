package com.example.multiliveapp.activity;

import androidx.appcompat.app.AppCompatActivity;

public class LiveAddData extends AppCompatActivity {

    //메인 화면의 recyclerView
    private String live_tagName;

    public LiveAddData(String live_tagName) {
        this.live_tagName = live_tagName;
    }

    public void setLive_tagName(String tagName) {
        live_tagName = tagName;
    }
    public String getLive_tagName() {
        return live_tagName;
    }
}
