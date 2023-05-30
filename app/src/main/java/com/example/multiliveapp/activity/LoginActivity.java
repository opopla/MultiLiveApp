package com.example.multiliveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiliveapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    final static PasswordResetActivity passwordresetactivity = new PasswordResetActivity();

    private FirebaseAuth mAuth;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.loginButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoSignUpButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoFindIdButton).setOnClickListener((onClickListener));
        findViewById(R.id.gotoFindPasswordButton).setOnClickListener(onClickListener);
        //findViewById(R.id.gotoPasswordResetButton).setOnClickListener(onClickListener);

        initLayoutSetting();
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loginButton:
                    login();
                    break;
                case R.id.gotoSignUpButton:
                    myStartActivity(SignUpActivity.class);
                    break;
                case R.id.gotoFindIdButton:
                    myStartActivity(FindIdActivity.class);
                    break;
                case R.id.gotoFindPasswordButton:
                    myStartActivity(FindPwActivity.class);
                    break;

            }
        }
    };

    public void login() {
        String email = ((EditText)findViewById(R.id.Login_emailEditText)).getText().toString();
        String password = ((EditText)findViewById(R.id.Login_passwordEditText)).getText().toString();

        // 로그인 과정
        if(email.length() > 0 && password.length() > 0) { // 이메일 또는 비밀번호를 입력했다면
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();
                                startToast("로그인에 성공하였습니다.");
                                myStartActivity(MMLMainActivity.class);

                                String pw2 = passwordresetactivity.pw;
                                Log.d(TAG, "pw2의 값은 : " + pw2);
                                if(pw2 == null || pw2.length() == 0) { // passwordresetactivity.pw에 값이 넘어온게 없다면 패스
                                    // ...
                                } else {
                                    // Authentication의 비밀번호도 변경했다면 DB의 비밀번호도 변경해야함
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference washingtonRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    washingtonRef
                                            .update("_user_pw", pw2)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Log.d(TAG, "DocumentSnapshot successfully updated!");
                                                    startToast("비밀번호 변경 성공");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error updating document", e);
                                                    startToast("비밀번호 변경 실패");
                                                }
                                            });
                                }
                            } else {
                                if (task.getException() != null) {
                                    startToast("이메일 또는 비밀번호가 틀립니다.");
                                    //startToast(task.getException().toString());
                                }
                            }
                        }
                    });
        } else {
            startToast("이메일 또는 비밀번호를 입력해 주세요.");
        }
}

    private void startToast(String msg) { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
    }

    private void initLayoutSetting() {
        //상단 액션바 안보이게 하는 함수
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //키패드가 레이아웃을 밀어내지않음
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

}