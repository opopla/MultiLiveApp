package com.example.multiliveapp.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiliveapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FindIdActivity extends AppCompatActivity {
    private static final String TAG = "FindIdActivity";
public int a = 0;

    EditText findidinputname;  // 사용자가 입력한 이름을 저장하는 변수
    TextView findidinputbirth; // 사용자가 입력한 생년월일을 저장하는 변수
    EditText findidinputnick; // 사용자가 입력한 닉네임을 저장하는 변수
    String findidoutputname; // DB에 저장된 이름을 불러와 저장하는 변수
    String findidoutputbirth; // DB에 저장된 생년월일을 불러와 저장하는 변수
    String findidoutputnick; // DB에 저장된 닉네임을 불러와 저장하는 변수
    String outputemail;

    private TextView birthday_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_find_id);
    initLayoutSetting();//초기 레이아웃 설정

    findViewById(R.id.FindId_gotoLoginButton).setOnClickListener(onClickListener);
    findViewById(R.id.findID_Back_Btn).setOnClickListener(onClickListener);
    findViewById(R.id.FindId_FindIdcheckButton).setOnClickListener(onClickListener);

    this.InitializeView();
    this.InitializeListener();
    initLayoutSetting();
}

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.FindId_gotoLoginButton: // 로그인 화면으로 버튼
                    myStartActivity(LoginActivity.class);
                    break;
                case R.id.findID_Back_Btn: // 뒤로가기 버튼
                    finish();
                    break;
                case R.id.FindId_FindIdcheckButton: // 이메일 찾기 버튼(사용자 아이디 찾기)
                    findId();
                    break;
            }
        }
    };


    public void findId() {
        findidinputname = ((EditText) findViewById(R.id.FindId_nameEditText));
        findidinputbirth = ((TextView) findViewById(R.id.FindId_birthText));
        findidinputnick = ((EditText) findViewById(R.id.FindId_nickEditText));
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 회원가입시 입력한 닉네임이 DB에 있는지 없는지 체크하기 위한 부분(중복 닉네임 방지)
        if (findidinputname.length() > 0 && findidinputbirth.length() > 0 && findidinputnick.length() > 0) { // 빈칸 빠짐 없이 한글자 이상 입력 받아야함
            db.collection("users")
                    .whereGreaterThanOrEqualTo("_user_nick", findidinputnick.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());

                                    // DB의 각 요소들을 각 String 변수에 저장
                                    findidoutputname = (String) document.getData().get("_user_name");
                                    findidoutputbirth = (String) document.getData().get("_user_birth");
                                    findidoutputnick = (String) document.getData().get("_user_nick");

                                    // 사용자가 입력한 닉네임, 생년월일, 이름과 DB에 저장된 닉네임, 생년월일, 이름이 같으면 참
                                    if (findidinputnick.getText().toString().equals(findidoutputnick) && findidinputbirth.getText().toString().equals(findidoutputbirth) && findidinputname.getText().toString().equals(findidoutputname)) {
                                        outputemail = (String) document.get("_user_email");
                                        startToast("사용자 이메일은 " + getMaskedEmail(outputemail) + " 입니다.");
                                        break;
                                    } else {
                                        Log.d(TAG, findidinputbirth.getText().toString() + " " + findidoutputbirth);
                                        startToast("입력한 정보가 존재하지 않습니다.");
                                        break;
                                    }
                                }
                            }
                        }
                    });
        } else {
            startToast("빈칸을 모두 입력해주세요.");
        }
    }


    private static String getMaskedEmail(String email) {
        /*
         * 요구되는 메일 포맷
         * {userId}@domain.com
         * */
        String regex = "\\b(\\S+)+@(\\S+.\\S+)";
        Matcher matcher = Pattern.compile(regex).matcher(email);
        if (matcher.find()) {
            String id = matcher.group(1); // 마스킹 처리할 부분인 userId
            /*
             * userId의 길이를 기준으로 세글자 초과인 경우 뒤 세자리를 마스킹 처리하고,
             * 세글자인 경우 뒤 두글자만 마스킹,
             * 세글자 미만인 경우 모두 마스킹 처리
             */
            int length = id.length();
            if (length < 2) {
                char[] c = new char[length];
                Arrays.fill(c, '*');
                return email.replace(id, String.valueOf(c));
            } else if (length == 4) {
                return email.replaceAll("\\b(\\S+)[^@][^@]+@(\\S+)", "$1**@$2");
            } else {
                return email.replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
            }
        }
        return email;
    }


    private void startToast(String msg) { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }

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

    //EX)findViewById()를 통해 화면에 표시되는 TextView의 참조 객체를 얻어오는 함수
    public void InitializeView()
    {
        //생년월일
        birthday_Date = (TextView)findViewById(R.id.FindId_birthText);
    }

    // OnDateSetListener와 selectDialog를 구현하는 함수
    public void InitializeListener()
    {
        //생년월일
        callbackMethod = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                birthday_Date.setText(year + "." + (monthOfYear+1) + "." + dayOfMonth);
            }
        };
    }

    // OnClickHandler() 함수는 버튼 이벤트 발생에 대해 처리를 담당하는 함수
    //xml에서 호출
    public void OnClickHandler(View view)
    {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; //0~11까지 존재하여 현재 월을 표시할 시엔 1을 더해주어야한다
        int date = cal.get(Calendar.DATE);

        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year, month, date);

        dialog.show();
    }

}