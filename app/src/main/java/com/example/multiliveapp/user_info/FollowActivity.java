package com.example.multiliveapp.user_info;

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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiliveapp.R;
import com.example.multiliveapp.activity.MMLMainActivity;

import java.util.ArrayList;
import java.util.List;

public class FollowActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ImageButton backBtn;
    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private String TAG = "FollowActivity";

    private ArrayAdapter arrayAdapter; // 리스트뷰에 연결할 아답터
    Button releaseBtn;//팔로우 해제 버튼

    //db에서 받아온 follow list 저장
    static List<String> followList22 = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);
        backBtn     = findViewById(R.id.follow_Back_Btn);
        editSearch  = (EditText) findViewById(R.id.follow_editSearch);
        listView    = (ListView) findViewById(R.id.follow_listView);
        releaseBtn = (Button) findViewById(R.id.follow_releaseBtn);

        initLayoutSetting();

        // 리스트를 생성한다.
        list    = new ArrayList<String>();

        // 검색에 사용할 데이터을 미리 저장한다.
        getData();
        Log.d(TAG, "팔로우 리스트 잘 받아왔나용 : " + followList22);


        // 리스트에 연동될 아답터를 생성한다.

        arrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_single_choice, list);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(this);

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
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

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FollowActivity.this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }


    //팔로우 데이터베이스 정보 받아오는 함수
    private void getData() {
        //follow.db에서 정보 받아오기
        followList22.clear();
        followList22.addAll(MMLMainActivity.dbHelper.getFollowUserList());

        //만약 팔로우 유저에 공백이 들어있다면 (회원가입당시 공백으로 하나 만들어둠)
        //무시하고 공백 아닌 것들로만 팔로우 리스트에 등록 및 출력
        for(int i=0; i<followList22.size(); ++i) {
            if(followList22.get(i).equals(""))
            {
                continue;
            }
            else
            {
                list.add(followList22.get(i));
            }
        }
        //arrayAdapter.notifyDataSetChanged();
    }

    //레이아웃 초기 세팅
    private void initLayoutSetting() {
        //상단 액션바 안보이게 하는 함수
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //키패드가 레이아웃을 밀어내지않음
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final String str = list.get(position) + "와(과) 팔로우 해제되었습니다.";

        releaseBtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //버튼 클릭 시 리스트 뷰에 있는 데이터를 삭제한다.

                Toast.makeText(getApplication(), str, Toast.LENGTH_SHORT).show();
                String unfollowUser = list.remove(position);
                MMLMainActivity.dbHelper.delete(unfollowUser);
                arrayAdapter.notifyDataSetChanged();
            }
        });
    }
}
