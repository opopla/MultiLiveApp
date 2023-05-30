package com.example.multiliveapp.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String TAG = "PasswordResetActivity";
    public static String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        initLayoutSetting();

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.pwdsendButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pwdsendButton:
                    send();
                    break;
            }
        }
    };

    public void send() {
        EditText pwEditText = ((EditText)findViewById(R.id.ResetPassword_passwordEditText));
        pw = pwEditText.getText().toString();

        String email = ((EditText)findViewById(R.id.ResetPassword_emailEditText)).getText().toString();
        if(email.length() > 0 && pw.length() > 0) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) { // 성공시
                            if (task.isSuccessful()) {
                                startToast("새 메일을 보냈습니다.\n이메일을 확인해주세요.");
                                myStartActivity(LoginActivity.class);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) { // 실패시
                    startToast("이메일의 형식이 올바르지 않거나 없는 이메일입니다.");
                }
            });
        } else {
            startToast("이메일 혹은 새비밀번호를 입력해 주세요.");
        }
}

    private void startToast(String msg) { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }

    private void initLayoutSetting() {
        //상단 액션바 안보이게 하는 함수
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //키패드가 레이아웃을 밀어내지않음
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}