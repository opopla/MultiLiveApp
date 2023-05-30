package com.example.multiliveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiliveapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FindPwActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "FindPwActivity";

    EditText inputemail;
    int emailAuth_auth = 0; // 이메일 인증을 확인하기 위한 변수
    String randomNum;
    String user_email;
    CountDownTimer countDownTimer;
    Button emailAuth_btn2; // OK버튼
    TextView time_counter; //시간을 보여주는 TextView
    final int MILLISINFUTURE = 180 * 1000; //총 시간 (300초 = 5분)
    final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)
    EditText emailAuth_number2; //사용자가 인증 번호를 입력 하는 칸

    //사용자가 인증번호를 마치고서 다시 인증번호 전송을 누르면 비밀번호변경 누르라고 토스트메시지 띄움
    Boolean flag_auth = false;
    Boolean timeout_flag = false;
    public FindPwActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        initLayoutSetting();//초기 레이아웃 설정

        findViewById(R.id.FindPw_gotoLoginButton).setOnClickListener(this);
        findViewById(R.id.findPW_Back_Btn).setOnClickListener(this);
        emailAuth_number2 = (EditText) findViewById(R.id.emailAuth_number2);
        findViewById(R.id.authEmailReceiveButton).setOnClickListener(this);
        findViewById(R.id.FindPwButton).setOnClickListener(this);
        findViewById(R.id.emailAuth_btn2).setOnClickListener(this);
        findViewById(R.id.findPW_Back_Btn).setOnClickListener(this);

        //이메일 사용준비
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());
    }
    @Override
        public void onClick(View v) {
        switch (v.getId()) {
            case R.id.FindPw_gotoLoginButton:
                myStartActivity(LoginActivity.class);
                break;

            case R.id.authEmailReceiveButton:
                timeout_flag = false;
                if(flag_auth == false)//인증번호 중복클릭방지
                {
                    user_email = ((EditText) findViewById(R.id.editText)).getText().toString();
                    SendMail mailServer = new SendMail();
                    mailServer.sendSecurityCode(getApplicationContext(), user_email);

                    //인증번호 중복클릭시 타이머 재시작
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    CountDownTimer();

                    if (user_email.length() <= 0) {
                        startToast("이메일을 입력해주세요.");
                        break;
//                        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
//                            startToast("이메일 형식이 올바르지 않습니다.");
//                        }
                    } else {

                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        DocumentReference docRef = db.collection("AuthNumber").document("random");

                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                        randomNum = (String) document.getData().get("random");
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }
                        });
                    }
                }
                else
                {
                    //OK버튼을 누르면 값이 true로 변경
                    startToast("이메일 인증을 마쳤습니다. 비밀번호 찾기를 눌러주세요");
                }
                break;
                    case R.id.emailAuth_btn2: //OK 버튼을 눌렀을 시
                        // 인증번호를 입력하지 않은 경우 > null 값 처리
                        final String emailAuth_num = ((EditText) findViewById(R.id.emailAuth_number2)).getText().toString();
                        if (emailAuth_num.equals("") || emailAuth_num.length() == 0 ) {
                            startToast("인증번호를 입력하세요.");
                        } else {
                            String user_answer = emailAuth_number2.getText().toString();
                            if(timeout_flag == false)
                            {
                                if (user_answer.equals(randomNum)) {
                                    startToast("이메일 인증 성공");
                                    countDownTimer.cancel();//성공 시 타이머 중지
                                    emailAuth_auth = emailAuth_auth + 1;
                                    flag_auth = true;
                                } else {
                                    Toast.makeText(this, "이메일 인증 실패, 입력된 이메일 또는 인증번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                startToast("제한 시간이 초과되었습니다. 인증번호를 다시 보내주세요");
                            }
                        }
                        break;
            case R.id.FindPwButton:
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                inputemail = ((EditText) findViewById(R.id.editText));

                if (emailAuth_auth == 1) {
                    db.collection("users")
                            .whereGreaterThanOrEqualTo("_user_email", inputemail.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Log.d(TAG, document.getId() + " => " + document.getData());

                                            // DB에 담긴 이메일, 비밀번호를 가져와서 출력
                                            String outputemail = (String) document.getData().get("_user_email");
                                            String outputpw = (String) document.getData().get("_user_pw");

                                            // 사용자가 입력한 이메일과 DB의 이메일이 일치하면 패스워드를 알려줌
                                            if (inputemail.getText().toString().equals(outputemail)) {
                                                outputemail = (String) document.get("_user_email");
                                                startToast("사용자 비밀번호는 " + outputpw + " 입니다.");
                                                break;
                                            } else {
                                                Log.d(TAG, inputemail.getText().toString() + " " + outputemail);
                                                startToast("입력한 이메일이 존재하지 않습니다.");
                                                break;

                                            }
                                        }
                                    }
                                }
                            });
                } else {
                    startToast("먼저 이메일 인증을 해주세요.");
                    return;
                }
                break;
            case R.id.findPW_Back_Btn :
                finish();
                break;
            }
        };


    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void initLayoutSetting() {
        //상단 액션바 안보이게 하는 함수
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //키패드가 레이아웃을 밀어내지않음
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void CountDownTimer() { //카운트 다운 메소드
            //줄어드는 시간을 나타내는 TextView
            time_counter = (TextView) findViewById(R.id.emailAuth_time_counter2);


            countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
                @Override
                public void onTick(long millisUntilFinished) {
                    //(300초에서 1초 마다 계속 줄어듬)

                    long emailAuthCount = millisUntilFinished / 1000;
                    Log.d("Alex", emailAuthCount + "");

                    if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                        time_counter.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                    } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                        time_counter.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                    }
                    //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                    // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                    // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.
                }
                @Override
                public void onFinish() { //시간이 다 되면 다이얼로그 종료
                    countDownTimer.cancel();
                    timeout_flag= true;
                    startToast("제한시간이 다 되었습니다 인증번호를 다시 보내주세요");
                }
            }.start();
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}