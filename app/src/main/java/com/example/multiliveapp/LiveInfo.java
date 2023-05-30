package com.example.multiliveapp;

public class LiveInfo {

    /* 태그 테이블 */
    private String live_host;
    private String live_title;
    private int live_join;
    private String live_tag;
    private String live_guest;
    private int live_id;



    public LiveInfo() {

    }
    public LiveInfo(String live_host, String live_title, int live_join, String live_tag, String live_guest, int live_id) {
        this.live_host=live_host;
        this.live_title=live_title;
        this.live_join=live_join;
        this.live_tag=live_tag;
        this.live_guest=live_guest;
        this.live_id=live_id;
    }

    public String get_live_host(){
        return this.live_host;
    }
    public void set_live_host(String live_host){
        this.live_host = live_host;
    }

    public String get_live_title(){
        return this.live_title;
    }
    public void set_live_title(String live_title){
        this.live_title = live_title;
    }

    public int get_live_join(){
        return this.live_join;
    }
    public void set_live_join(int live_join){
        this.live_join = live_join;
    }

    public String get_live_tag(){
        return this.live_tag;
    }
    public void set_live_tag(String live_tag){
        this.live_tag = live_tag;
    }

    public String get_live_guest(){
        return this.live_guest;
    }
    public void set_live_guest(String live_guest){
        this.live_guest = live_guest;
    }

    public int get_live_id(){
        return this.live_id;
    }
    public void set_live_id(int live_id){
        this.live_id = live_id;
    }
}

