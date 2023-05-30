package com.example.multiliveapp;

import java.util.HashMap;
import java.util.Map;

public class TagInfo {

    /* 태그 테이블 */
   private String tag_id;
   private String tag_name;
   private String user_nick;
   private int tag_num;//음


    public TagInfo() {

    }
    public TagInfo(String user_nick, String tag_name) {
        this.user_nick = user_nick;
        this.tag_name = tag_name;

    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_nick", user_nick);
        result.put("tagName", tag_name);

        return result;
    }

    public String get_user_nick() { return this.user_nick; }
    public void set_user_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String get_tag_name() {
        return this.tag_name;
    }
    public void set_tag_name(String user_name) {
        this.tag_name = tag_name;
    }
}

