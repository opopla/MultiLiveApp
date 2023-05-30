package com.example.multiliveapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainRecyclerData extends AppCompatActivity {

    //메인 화면의 recyclerView
    private String userNickname;
    private String liveTitle;
    private String tag;
    private String roomNumber;
    private boolean isFollowed;
    private int followNumber;
    private String myNick;

    private HashMap<String, Object> followList = new HashMap<String, Object>();


    public MainRecyclerData() {
        return;
    }

    //팔로우추가
    public MainRecyclerData(String userNickname, String liveTitle, String tag, String roomNumber, boolean isFollowed) {
        this.userNickname = userNickname;
        this.liveTitle = liveTitle;
        this.tag = tag;
        this.roomNumber = roomNumber;
        this.isFollowed = isFollowed;
    }

    public MainRecyclerData(String userNickname, String liveTitle, String tag, String roomNumber, boolean isFollowed, String myNick) {
        this.userNickname = userNickname;
        this.liveTitle = liveTitle;
        this.tag = tag;
        this.roomNumber = roomNumber;
        this.isFollowed = isFollowed;
        this.myNick = myNick;
    }

    public MainRecyclerData(String userNickname, String liveTitle, String tag, String roomNumber, int followNumber, boolean isFollowed, String myNick) {
        this.userNickname = userNickname;
        this.liveTitle = liveTitle;
        this.tag = tag;
        this.roomNumber = roomNumber;
        this.isFollowed = isFollowed;
        this.followNumber = followNumber;
        this.myNick = myNick;
    }

    //유저 닉네임을 받아옴
    public void setUserNickname(String nickname) {
        userNickname = nickname;
    }

    //유저 닉네임 출력 할 수 있게 반환
    public String getUserNickname() {
        return userNickname;
    }

    public void setLiveTitle(String title) {
        liveTitle = title;
    }

    public String getLiveTitle() {
        return liveTitle;
    }

    public void setLiveTag(String t) {
        tag = t;
    }

    public String getLiveTag() {
        return tag;
    }

    public void setRoomNumber(String r) {
        roomNumber = r;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setFollowStatus(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public boolean getFollowStatus() {
        return isFollowed;
    }

    public void setFollowNumber(int followNumber) {
        this.followNumber = followNumber;
    }

    public int getFollowNumber() {
        return followNumber;
    }

    public void setMyNick() {
        this.myNick = myNick;
    }

    public String getMyNick() {
        return myNick;
    }

    public void setMyFollowList(HashMap<String, Object> list) {
        this.followList = list;
    }

    public HashMap<String, Object> getMyFollowList() {
        return this.followList;
    }

}
