package com.example.multiliveapp.user_info;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiliveapp.R;
import com.example.multiliveapp.activity.MainActivity;
import com.example.multiliveapp.activity.revokeAccessDialog;
import com.google.firebase.auth.FirebaseAuth;

public class SettingActivity extends AppCompatActivity {
    private FirebaseAuth mAuth ;
    private static final String TAG = "SettingActivity";

    // Button intent_btn1,intent_btn2, intent_btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();


        findViewById(R.id.UserInfo).setOnClickListener(onClickListener);
        findViewById(R.id.Follow).setOnClickListener(onClickListener);
        findViewById(R.id.Tag).setOnClickListener(onClickListener);
        findViewById(R.id.logout).setOnClickListener(onClickListener);
        findViewById(R.id.gotoUserDelete).setOnClickListener(onClickListener);
        findViewById(R.id.setting_backBtn).setOnClickListener(onClickListener);

        initLayoutSetting();
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.setting_backBtn :
                    finish();
                    break;
                case R.id.UserInfo:
                    // 사용자 정보
                    myStartActivity(UserInfoActivity.class);
                    break;
                case R.id.Follow:
                    // 팔로우
                    myStartActivity(FollowActivity.class);
                    break;
                case R.id.Tag:
                    // 태그
                    myStartActivity(TagActivity.class);
                    break;
                case R.id.logout:
                    // 로그아웃
                    signOut();
                    startToast("사용자 계정이 로그아웃 되었습니다.");
                    myStartActivity(MainActivity.class);
                    break;
                case R.id.gotoUserDelete:
                    // 회원탈퇴
                    // 커스텀 다이얼로그를 생성한다. 사용자가 만든 클래스이다.
                    revokeAccessDialog revokeAccessDialog = new revokeAccessDialog(SettingActivity.this);

                    // 커스텀 다이얼로그를 호출한다.
                    // 커스텀 다이얼로그의 결과를 출력할 TextView를 매개변수로 같이 넘겨준다.
                    revokeAccessDialog.revokeAccessCallFunction();
                    break;
            }
        }
    };

    // 로그아웃
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
    }

    // 회원탈퇴 컬렉션 삭제가 안됨
    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }

    private void startToast(String msg) { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initLayoutSetting() {
        //상단 액션바 안보이게 하는 함수
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //키패드가 레이아웃을 밀어내지않음
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

}
