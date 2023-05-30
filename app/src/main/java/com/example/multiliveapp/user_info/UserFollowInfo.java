package com.example.multiliveapp.user_info;

import java.util.List;

public class UserFollowInfo {

    //팔로우 db에 추가 해야하는 데이터
    private String userNick;//사용자 닉네임
    private String userUID;//사용자 uid
    private int followNum;//사용자의 팔로우 수
    private List<String> userFollowList;//팔로우 한 유저 리스트

    public UserFollowInfo(String userNick, String userUID) {
        this.userNick  = userNick;
        this.userUID   = userUID;
        this.followNum = 0;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public int getFollowNum() {
        return followNum;
    }

    public void setFollowNum(int followNum) {
        this.followNum = followNum;
    }

    public void getUserFollowList(List<String> userFollowList) {
        this.userFollowList = userFollowList;
    }

    public List<String> setUserFollowList() {
        return userFollowList;
    }



}
