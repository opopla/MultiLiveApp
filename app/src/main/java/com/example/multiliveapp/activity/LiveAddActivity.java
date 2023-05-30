package com.example.multiliveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiliveapp.ConnectActivity;
import com.example.multiliveapp.LiveInfo;
import com.example.multiliveapp.R;
import com.example.multiliveapp.user_info.SettingActivity;
import com.example.multiliveapp.user_info.UserInfoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LiveAddActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private ArrayAdapter arrayAdapter; // 리스트뷰에 연결할 아답터

    private static final String TAG = "LiveAddActivity";

    EditText liveTitle_ET, liveAdd_searchTag_ET;
    Button liveAddBtn, liveAdd_addTag_btn;
    ImageButton liveAddBackBtn, liveAddSettingBtn, liveAdd_userInfo_Btn;

    String live_guest;
    //유저 정보
    String userNickname = "", s = "default", t = "default";

    String[] tagArray = {"", "", "", "", "", "", "", "", ""};
    String defaultTag = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_add);

        mAuth = FirebaseAuth.getInstance();

        //초기 레이아웃 설정 + 변수 선언
        initLayoutSetting();

        listView = (ListView) findViewById(R.id.liveAdd_ListView);
        // 리스트를 생성한다.
        list = new ArrayList<String>();
        //다중선택모드의 AraayAdapter 생성
        arrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_multiple_choice, list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // 리스트뷰에 AraayAdapter 아답터를 연결한다.
        listView.setAdapter(arrayAdapter);
        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        EditText editSearch = (EditText)findViewById(R.id.liveAdd_searchTag_ET);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                // TODO : item filtering
                String filterText = edit.toString() ;
                if (filterText.length() > 0) {
                    listView.setFilterText(filterText) ;
                } else {
                    listView.clearTextFilter() ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //스포츠
                if(listView.isItemChecked(0) == true) {
                    tagArray[0] = "스포츠";
                }
                if(listView.isItemChecked(0) == false) {
                    tagArray[0] = "";
                }

                // 동물
                if(listView.isItemChecked(1) == true) {
                    tagArray[1] = "동물";
                }
                if(listView.isItemChecked(1) == false) {
                    tagArray[1] = "";
                }

                // 유머
                if(listView.isItemChecked(2) == true) {
                    tagArray[2] = "유머";
                }
                if(listView.isItemChecked(2) == false) {
                    tagArray[2] = "";
                }

                // 스터디
                if(listView.isItemChecked(3) == true) {
                    tagArray[3] = "스터디";
                }
                if(listView.isItemChecked(3) == false) {
                    tagArray[3] = "";
                }

                // 소개팅
                if(listView.isItemChecked(4) == true) {
                    tagArray[4] = "소개팅";
                }
                if(listView.isItemChecked(4) == false) {
                    tagArray[4] = "";
                }

                // 노래
                if(listView.isItemChecked(5) == true) {
                    tagArray[5] = "노래";
                }
                if(listView.isItemChecked(5) == false) {
                    tagArray[5] = "";
                }

                // 요리
                if(listView.isItemChecked(6) == true) {
                    tagArray[6] = "요리";
                }
                if(listView.isItemChecked(6) == false) {
                    tagArray[6] = "";
                }

                // 다이어트
                if(listView.isItemChecked(7) == true) {
                    tagArray[7] = "다이어트";
                }
                if(listView.isItemChecked(7) == false) {
                    tagArray[7] = "";
                }

                // 모임
                if(listView.isItemChecked(8) == true) {
                    tagArray[8] = "모임";
                }
                if(listView.isItemChecked(8) == false) {
                    tagArray[8] = "";
                }
            }
        });

        liveTitle_ET        = (EditText)findViewById(R.id.liveAdd_liveTitle_ET);
        liveAddBackBtn = (ImageButton) findViewById(R.id.liveAdd_Back_Btn);
        liveAddSettingBtn = (ImageButton) findViewById(R.id.liveAdd_setting_Btn);
        liveAddBtn = (Button) findViewById(R.id.liveAdd_liveAdd_Btn);
        liveAdd_userInfo_Btn = (ImageButton) findViewById(R.id.liveAdd_userInfo_Btn);

        liveAddBackBtn.setOnClickListener(this);
        liveAddSettingBtn.setOnClickListener(this);
        liveAddBtn.setOnClickListener(this);
        liveAdd_userInfo_Btn.setOnClickListener(this);
        initUserData();//유저 정보 받아오는 함수
        TagData();//전체 태그 받아오는 함수

        //Toast.makeText(getApplicationContext(),userNickname, Toast.LENGTH_SHORT).show();

    }

    private void settingList() {
        arrayAdapter.add("스포츠") ;
        arrayAdapter.add("동물") ;
        arrayAdapter.add("유머") ;
        arrayAdapter.add("스터디") ;
        arrayAdapter.add("소개팅") ;
        arrayAdapter.add("노래") ;
        arrayAdapter.add("요리") ;
        arrayAdapter.add("다이어트") ;
        arrayAdapter.add("모임") ;
    }

    private void TagData() {
        String[] t = {"스포츠", "동물", "유머", "스터디", "소개팅",
                "노래", "요리","다이어트", "모임"};

    }

    private void initUserData() {
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            s = (document.getData().get("_user_nick")).toString();

                            setUserNickname(s);
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }

            }
        });
    }

    private void setUserNickname(String s) {
        userNickname = s;
    }

    // 뒤로가기 버튼을 눌렀을 경우
    @Override
    public void onBackPressed() {
        finish();
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("type1", "LiveAdd");
        finish();
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.liveAdd_setting_Btn) :
                //Toast.makeText(getApplicationContext(), "설정창", Toast.LENGTH_SHORT).show();
                myStartActivity(SettingActivity.class);
                break;
            case (R.id.liveAdd_Back_Btn) :
                //Toast.makeText(getApplicationContext(), "뒤로가기", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case (R.id.liveAdd_liveAdd_Btn) :
                createLive();
                break;
            case (R.id.liveAdd_userInfo_Btn) :
                //Toast.makeText(getApplicationContext(), "사용자 정보창", Toast.LENGTH_SHORT).show();
                myStartActivity(UserInfoActivity.class);
                break;

        }
    }

    private void createLive() {
        final String live_title = ((EditText)findViewById(R.id.liveAdd_liveTitle_ET)).getText().toString();
        final String live_host = userNickname;
        Random rnd = new Random();
        final int live_id = rnd.nextInt(152238) + 1000000;

        if(live_title.length() > 0) {
            if(live_title.length() > 20) {
                startToast("라이브 제목은 20글자 미만이어야 합니다.");
                return;
            }
            else {
                for (int i = 0; i < tagArray.length; i++) {
                    if (tagArray[i] == "") {

                    } else {
                        defaultTag = defaultTag + tagArray[i] + "|";
                    }
                }
                if (defaultTag == "") {

                } else {
                    defaultTag = defaultTag.substring(0, defaultTag.length() - 1);
                }

                final String live_tag = defaultTag;

                Toast.makeText(getApplicationContext(), "라이브 생성됨", Toast.LENGTH_SHORT).show();
                //myStartActivity(LiveActivity.class);
                user = FirebaseAuth.getInstance().getCurrentUser();

                LiveInfo liveInfo = new LiveInfo(live_host, live_title, 2, live_tag, live_guest, live_id);
                uploader(liveInfo);
            }
        } else {
            startToast("라이브 제목을 입력해주세요.");
            return;
        }
    }

    private void uploader(LiveInfo liveInfo) {
        db = FirebaseFirestore.getInstance();
        db.collection("live").document(user.getUid()).set(liveInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("라이브가 생성되었습니다.");
                        myStartActivity(ConnectActivity.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("라이브 생성이 실패했습니다.");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void initLayoutSetting() {
        //상단 액션바 안보이게 하는 함수
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //키패드가 레이아웃을 밀어내지않음
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
