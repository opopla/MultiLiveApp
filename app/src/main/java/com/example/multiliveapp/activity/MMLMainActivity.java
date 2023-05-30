package com.example.multiliveapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.multiliveapp.R;
import com.example.multiliveapp.followDB.DBHelper;
import com.example.multiliveapp.user_info.SettingActivity;
import com.example.multiliveapp.user_info.UserInfoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class MMLMainActivity extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    //레이아웃 관련 변수 선언
    private MainRecyclerAdapter adapter;
    private MainRecyclerData data;
    private ImageButton mainUserInfoBtn, mainSettingBtn, mainLiveAddBtn, cate_left, cate_right;
    private Button searchBtn;
    private EditText mainEditText;
    private TextView category;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String TAG = "MMLMainActivity";
    public static Context mContext;

    public Button allchatBtn;

    //변수 선언
    private String host = "", tit = "", ta = "", room = "";//라이브 생성 호스트, 라이브 제목, 라이브 태그, 라이브방번호
    private String searchDataName;//사용자가 입력한 검색단어
    private String searchedHost = "", searchedTitle = "", searchedTag = "", searchedRoom = "";//검색된 키워드들을 저장하기 위한 변수
    private List<String> userHasTag = new ArrayList<String>();
    private int cnt;
    private int REQUEST_TEST = 1;

    static String myNick = "";

    //followDB 사용
    public static DBHelper dbHelper;
    //db에서 받아온 follow list 저장
    static List<String> followList = new ArrayList<String>();

    @Override
    protected void onStart() {
        super.onStart();
        adapter.remove();

        getMyFollowList();
        if (category.getText().toString().equals("전체")) {
            getData(0);//새로 등록하기 위해 데이터를 새로 받아온다.(0은 전체)
        } else {
            getData(1);//(1은 추천)
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.remove();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.remove();
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        initLayoutSetting();//레이아웃 초기 세팅
        mContext = this;
        dbHelper = new DBHelper(mContext, "follow.db", null, 1);

        mainUserInfoBtn = (ImageButton) findViewById(R.id.mainUserInfoBtn);
        mainSettingBtn = (ImageButton) findViewById(R.id.mainSettingBtn);
        mainLiveAddBtn = (ImageButton) findViewById(R.id.mainLiveAddBtn);
        searchBtn = (Button) findViewById(R.id.mainSearchBtn);
        mainEditText = (EditText) findViewById(R.id.mainEditText);
        category = (TextView) findViewById(R.id.mainCategoryText);
        cate_left = (ImageButton) findViewById(R.id.mainCategoryLeftBtn);
        cate_right = (ImageButton) findViewById(R.id.mainCategoryRightBtn);
        //전체 채팅방 버튼
        allchatBtn = findViewById(R.id.allchatBtn);
        allchatBtn.setOnClickListener(this);

        //스와이프로 새로고침하는 레이아웃
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        init();             //리사이클러뷰 초기 설정
        getUserData();      //나의 정보 받아오는 함수
        getMyFollowList();  //나의 팔로우 목록을 받아오는 함수


        //db에서 리사이클러뷰에 들어갈 데이터 받아오기
        //getData(0);     //0은 전체데이터 , 1은 추천 데이터 (추천:태그)

        mainUserInfoBtn.setOnClickListener(this);
        mainSettingBtn.setOnClickListener(this);
        mainLiveAddBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);

        cate_left.setOnClickListener(this);
        cate_right.setOnClickListener(this);


        mainEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == event.KEYCODE_ENTER) return true;
                return false;
            }
        });

    }
    public void callRC() {
        adapter.remove();

        getMyFollowList();
        if (category.getText().toString().equals("전체")) {
            getData(0);//새로 등록하기 위해 데이터를 새로 받아온다.(0은 전체)
        } else {
            getData(1);//(1은 추천)
        }
    }

    //유저 정보를 받아오는 함수 (태그, 닉네임)
    private void getUserData() {
        final List<String> arr = new ArrayList<String>();
        String[] tags = {"_user_stag1", "_user_stag2", "_user_stag3", "_user_stag4", "_user_stag5", "_user_stag6", "_user_stag7", "_user_stag8", "_user_stag9"};

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            for (int i = 0; i < 9; ++i) {
                                //내 닉네임 받아오기
                                myNick = document.getData().get("_user_nick").toString();
                                //태그 받아오기
                                if (document.getData().get(tags[i]).toString() != null) {
                                    arr.add(document.getData().get(tags[i]).toString());
                                } else {
                                    arr.add("");
                                }
                            }
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                userHasTag.addAll(arr);
            }
        });

    }

    //나의 팔로우 목록을 받아오는 함수
    public void getMyFollowList() {
        //전에 받아온 팔로우 리스트가 있다면 리스트 초기화 후 데이터베이스에서 정보 받아오기
        if(!followList.isEmpty()) {
            followList.clear();
        }
        followList.addAll(dbHelper.getFollowUserList());

    }

    //리사이클러뷰 초기 설정
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MainRecyclerAdapter();
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //해당 액티비티 초기 설정
    private void initLayoutSetting() {
        //상단 액션바 안보이게 하는 함수
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //키패드가 레이아웃을 밀어내지않음
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    //인텐트 함수
    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }

    //카테고리의 왼쪽 오른쪽 버튼을 눌렀을 때 카테고리 변경하는 함수
    private void setCategory() {
        if (category.getText().toString().equals("전체")) {
            //현재 카테고리 텍스트가 전체라면 추천으로 바꾸고 리사이클러뷰 아이템 정렬 방식을 추천으로 바꾼다
            category.setText("추천");
            getData(1);

        } else {
            //현재 카테고리 텍스트가 추천이라면 전체로 바꾸고 리사이클러뷰 아이템 정렬 방식을 전체로 바꾼다
            category.setText("전체");
            getData(0);
        }
    }

    //눌렀을 때 이벤트 처리
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainUserInfoBtn:
                myStartActivity(UserInfoActivity.class);
                break;
            case R.id.mainSettingBtn:
                Intent i = new Intent(this, SettingActivity.class);
                startActivityForResult(i, REQUEST_TEST);
                break;
            case R.id.mainLiveAddBtn:
                myStartActivity(LiveAddActivity.class);
                break;
            case R.id.mainSearchBtn:
                //검색버튼
                searchDataName = mainEditText.getText().toString();
                searchData(searchDataName);
                break;
            case R.id.mainCategoryLeftBtn:
                //카테고리 왼쪽 버튼
                setCategory();
                break;
            case R.id.mainCategoryRightBtn:
                //카테고리 오른쪽 버튼
                setCategory();
                break;
            case R.id.allchatBtn :
                Intent intent = new Intent(MMLMainActivity.this, ChatActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    //데이터베이스에 등록 된 라이브 정보를 받아오는 함수 - 진짜 정보만 받아옴
    //해당 함수에서 정보를 받아오고 , makeRe함수를 통해 리사이클러뷰에 아이템 등록
    public void getData(int i) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        if (i == 0) {
            //전체
            adapter.remove();
            db.collection("live").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            if (document.getData().get("_live_title") == null) {
                                tit = " ";
                            } else {
                                tit = document.getData().get("_live_title").toString();
                            }
                            if (document.getData().get("_live_host") == null) {
                                host = " ";
                            } else {
                                host = document.getData().get("_live_host").toString();
                            }
                            if (document.getData().get("_live_tag") == null) {
                                ta = " ";
                            } else {
                                ta = document.getData().get("_live_tag").toString();
                            }
                            if (document.getData().get("_live_id") == null) {
                                room = " ";
                            } else {
                                room = document.getData().get("_live_id").toString();
                            }
                            if(isFollowed(host)) {
                                //라이브 호스트 유저가 팔로우 한 유저일 경우
                                //리사이클러뷰에 등록하기위해 데이터 전달
                                makeRec(host, tit, ta, room, true);
                            }
                            else {
                                //라이브 호스트 유저가 팔로우 한 유저가 아닐 경우
                                makeRec(host, tit, ta, room, false);
                            }
                            //Log.d(TAG, "mml getData 호출");

                        }
                    } else {
                    }
                }
            });
        } else if (i == 1) {
            //추천
            adapter.remove();

            db = FirebaseFirestore.getInstance();
            db.collection("live").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    cnt = 0;
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, "cnt : " + cnt);
                            Log.d(TAG, document.getId() + " asd 유저 태그 " + document.getData());
                            Log.d(TAG, "유저 태그1 : " + document.getData().get("_live_tag").toString().toLowerCase());
                            if (isTrue(document.getData().get("_live_tag").toString().toLowerCase())) {
                                searchedHost = document.getData().get("_live_host").toString();
                                searchedTitle = document.getData().get("_live_title").toString();
                                searchedTag = document.getData().get("_live_tag").toString();
                                searchedRoom = document.getData().get("_live_id").toString();
                                //등록하려는 라이브 유저가 팔로우 목록에 등록 된 사람이라면
                                if (isFollowed(searchedHost)) {
                                    makeRec(searchedHost, searchedTitle, searchedTag, searchedRoom, true);
                                } else {
                                    //등록안됐으면
                                    makeRec(searchedHost, searchedTitle, searchedTag, searchedRoom, false);
                                }

                            } else {
                            }
                            ++cnt;

                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
        }
    }

    //라이브 리스트 등록 전 해당 유저가 팔로우 된 유저인지 확인하는 함수
    private boolean isFollowed(String name) {
        //follow.db에서 받아온 데이터를 토대로 팔로우된 유저인지 판단하는 함수
        boolean flag = false;
        for(int i=0; i<followList.size(); ++i) {
            if(followList.get(i).equals("")) {
                continue;
            }
            else if(followList.get(i).equals(name)) {
                //닉네임 일치하는 경우
                flag = true;
            }
        }

        return flag;
    }


    //태그 검색
    //태그 순으로 목록 나열 할 때 유저가 해당 태그를 가지고있는지 확인하기 위한 함수
    private boolean isTrue(String dbStr) {
        boolean flag = false;
        for (int j = 0; j < userHasTag.size(); ++j) {
            if (!(userHasTag.get(j).equals(""))) {
                if (dbStr.contains(userHasTag.get(j).toLowerCase())) {
                    flag = true;
                }

            }
        }
        return flag;
    }

    //데이터를 검색하는 함수
    private void searchData(String searchDataName) {
        //리사이클러뷰에 등록 된 아이템을 전부 지워준다.
        adapter.remove();
        getMyFollowList();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("live").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        //contains함수를 이용하여 받아온 정보에 찾는 내용(searchDataName)이 있을경우 if 문 동작
                        if ((document.getData().get("_live_host").toString()).toLowerCase().contains(searchDataName.toLowerCase()) ||
                                (document.getData().get("_live_title").toString()).toLowerCase().contains(searchDataName.toLowerCase()) ||
                                (document.getData().get("_live_tag").toString()).toLowerCase().contains(searchDataName.toLowerCase())) {
                            searchedHost = document.getData().get("_live_host").toString();
                            searchedTitle = document.getData().get("_live_title").toString();
                            searchedTag = document.getData().get("_live_tag").toString();
                            searchedRoom = document.getData().get("_live_id").toString();

                            //리사이클러뷰에 받아온 정보를 토대로 아이템 등록
                            //등록하려는 라이브 유저가 팔로우 목록에 등록 된 사람이라면
                            if (isFollowed(searchedHost)) {
                                makeRec(searchedHost, searchedTitle, searchedTag, searchedRoom, true);
                            } else {
                                //등록안됐으면
                                makeRec(searchedHost, searchedTitle, searchedTag, searchedRoom, false);
                            }
                        }
                    }
                } else {
                    //Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }


    //*리사이클러뷰에 아이템 등록하는 함수
    public void makeRec(String host, String title, String tags, String room, boolean isFollow) {
        //받아온 정보를 토대로 리사이클러 데이터로 저장하여 어댑터로 데이터 전달
        //호스트유저, 라이브제목, 라이브태그, 방번호, 팔로우상태, 현 사용자명
        data = new MainRecyclerData(host, title, tags, room, isFollow, myNick);

        adapter.addItem(data);
        adapter.notifyDataSetChanged();
    }

    // 뒤로가기 버튼을 눌렀을 경우
    @Override
    public void onBackPressed() {
        // 밑 소스들은 앱 자체를 종료시킴
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    //스와이프 당겨서 새로고침
    @Override
    public void onRefresh() {
        mainEditText.setText("");
        adapter.remove();//리사이클러 아이템 지우고
        getMyFollowList();
        if (category.getText().toString().equals("전체")) {
            getData(0);//새로 등록하기 위해 데이터를 새로 받아온다.(0은 전체)
        } else {
            getData(1);//(1은 추천)
        }

        mSwipeRefreshLayout.setRefreshing(false);
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.remove();       //리사이클러 아이템 지우고

        adapter.notifyDataSetChanged();
    }
}
