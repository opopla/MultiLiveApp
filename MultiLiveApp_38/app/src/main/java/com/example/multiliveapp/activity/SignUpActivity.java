package com.example.multiliveapp.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiliveapp.MemberInfo;
import com.example.multiliveapp.R;
import com.example.multiliveapp.user_info.UserFollowInfo;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.regex.Pattern;
public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;

    private static final String TAG = "SignUpActivity";
    private String profilePath;
    private FirebaseUser user;

    private String[] c_name = {"대한민국", "일본", "독일", "영국", "인도", "중국", "프랑스", "이탈리아", "미국", "이집트", "벨기에"};
    private TextView m_country;
    private AlertDialog c_SelectDialog;

    private TextView birthday_Date;
    private DatePickerDialog.OnDateSetListener callbackMethod;

    Button tagsport; // 스포츠
    Button taganimal; // 동물
    Button taghumor; // 유머

    Button tagstudy; // 스터디
    Button tagsogae; // 소개팅
    Button tagsing; // 노래

    Button tagcook; // 요리
    Button tagdiet; // 다이어트
    Button tagmetting; // 모임

    String utagsport = "";
    String utaganimal = "";
    String utaghumor = "";
    String utagstudy = "";
    String utagsogae = "";
    String utagsing = "";
    String utagcook = "";
    String utagdiet = "";
    String utagmetting = "";

    String outputnick;
    String user_email;
    String user_pw;
    int signUp_check = 0; // SignUp() 메서드가 호출됐는지 안 됐는지 체크하는 변수
    int signUp_nick_check = 0; // 닉네임 중복확인 체크 변수
    int is_signUp_finished = 0; // 이메일/패스워드를 생성하고 뒤로가기(back)를 했는지 안 했는지 체크하는 변수
    int certifyByEmail = 0; // 이메일/패스워드 중복확인 전, 이메일 인증을 했는지 안 했는지 체크하는 변수
    Button sendBtn;
    static Button nickCheckButton;
    EditText emailText, nickText;

      String randomNum; // 이메일로 보내진 인증번호
       String user_answer;
     TextView time_counter; //시간을 보여주는 TextView
     EditText emailAuth_number; //사용자가 인증 번호를 입력 하는 칸
     Button emailAuth_btn; // OK버튼
     public CountDownTimer countDownTimer;
     final int MILLISINFUTURE = 180 * 1000; //총 시간 (300초 = 5분)
     final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)

    //사용자가 인증번호를 마치고서 다시 인증번호 전송을 누르면 버튼 누르라고 토스트메시지 띄움
    Boolean flag_auth = false;
    Boolean timeout_flag = false;

    //파이어베아스 연결
    FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
        //이메일 사용준비
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());




        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signUpButton1).setOnClickListener(this);
        findViewById(R.id.signUpButton2).setOnClickListener(this);
        findViewById(R.id.SignUp_gotoLoginButton).setOnClickListener(this);
        findViewById(R.id.singup_Back_Btn).setOnClickListener(this);
        findViewById(R.id.emailAuth_btn).setOnClickListener(this);
        findViewById(R.id.SignUp_nickCheckButton).setOnClickListener(this);

        nickCheckButton = findViewById(R.id.SignUp_nickCheckButton);
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(this);
        emailText = findViewById(R.id.SignUp_emailEditText);
        emailAuth_number = (EditText) findViewById(R.id.emailAuth_number);
        nickText = findViewById(R.id.SignUp_nickEditText);

        //profileImageView = findViewById(R.id.profileImageView);
        //profileImageView.setOnClickListener(onClickListener);

        //태그 추가
        this.tags();;
        this.InitializeView();
        this.InitializeListener();
        initLayoutSetting();

        //줄어드는 시간을 나타내는 TextView
        time_counter = (TextView) findViewById(R.id.emailAuth_time_counter);
        //OK 버튼
        emailAuth_btn = (Button) findViewById(R.id.emailAuth_btn);



    }



    // 뒤로가기 클릭 시 발생
    @Override
    public void onBackPressed() {
        if(is_signUp_finished == 0) { // 이메일을 생성하기 전이라면
            finish(); // 이전 화면으로 이동
        } else { // 이메일을 생성한 후라면
            revokeAccess(); // 방금 만들었던 계정을 삭제
            finish(); // 이전 화면으로 이동
            startToast("직전 생성된 이메일 계정은 삭제됩니다.\n로그인 화면으로 돌아갑니다.");
        }
    }

    // 파이어베이스 Auth 계정 삭제 메서드
    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }

    private void signUp() {
        user_email = ((EditText)findViewById(R.id.SignUp_emailEditText)).getText().toString();
        user_pw = ((EditText)findViewById(R.id.SignUp_passwordEditText)).getText().toString();
        String user_pw_check = ((EditText)findViewById(R.id.passwordCheckEditText)).getText().toString();

        // 길이제한
        if(user_email.length() > 0 && user_pw.length() > 0 && user_pw_check.length() > 0) {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user_email).matches()) {
                startToast("이메일 형식이 올바르지 않습니다.");
                return;
            }
            if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", user_pw)) {
                startToast("비밀번호는 숫자, 영어, 특수문자를 포함한 20글자 미만이어야 합니다.");
                return;
            }
            if (user_pw.equals(user_pw_check)) {
                mAuth.createUserWithEmailAndPassword(user_email, user_pw)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        startToast("이메일과 비밀번호 생성에 성공하였습니다.");
                                        signUp_check = 1;
                                        is_signUp_finished = 1;
                                        //myStartActivity(MainActivity.class);
                                    } else {
                                        if (task.getException() != null)
                                            startToast("이미 존재하는 이메일입니다.");
                                            //startToast(task.getException().toString());
                                    }
                                }
                            });
                } else {
                    startToast("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
                }
            } else {
                startToast("이메일 또는 비밀번호를 입력해 주세요.");
            }
        }


    private void profileUpdate() {
        final String user_nick = ((EditText)findViewById(R.id.SignUp_nickEditText)).getText().toString();
        final String user_name = ((EditText)findViewById(R.id.SignUp_nameEditText)).getText().toString();
        final RadioGroup rg = ((RadioGroup)findViewById(R.id.radioGroup1));
        final int id = rg.getCheckedRadioButtonId();
        final RadioButton rb = (RadioButton)findViewById(id);
        final String user_gender = rb.getText().toString();
        final String user_country = ((TextView)findViewById(R.id.country)).getText().toString();
        final String user_birth = ((TextView)findViewById(R.id.birthday)).getText().toString();
        final String user_phone = ((EditText)findViewById(R.id.phoneNumberEditText)).getText().toString();
        //String stag1 = "";

        // 이름, 전화번호, 생년월일, 주소 최소길이 설정
        if(user_nick.length() > 0 && user_name.length() > 0 && user_gender.length() > 0 && user_country.length() > 0 && user_birth.length() > 0 && user_phone.length() > 0) {
            if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", user_phone)) {
                startToast("(-)을 제외한 핸드폰 번호를 입력해주세요. \nex) 01012341234");
                return;
            }
            if(!Pattern.matches("^[가-힣]*$", user_name)) {
                startToast("이름은 한글만 가능합니다.");
                return;
            }
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/" + user.getUid() + "/profileImage.jpg");

            if(profilePath == null) {
                if (tagsport.isSelected()) {
                    utagsport = "스포츠";
                }
                if (taganimal.isSelected()) {
                    utaganimal = "동물";
                }
                if (taghumor.isSelected()) {
                    utaghumor = "유머";
                }
                if (tagstudy.isSelected()) {
                    utagstudy = "스터디";
                }
                if (tagsogae.isSelected()) {
                    utagsogae = "소개팅";
                }
                if (tagsing.isSelected()) {
                    utagsing = "노래";
                }
                if (tagcook.isSelected()) {
                    utagcook = "요리";
                }
                if (tagdiet.isSelected()) {
                    utagdiet = "다이어트";
                }
                if (tagmetting.isSelected()) {
                    utagmetting = "모임";
                }

                MemberInfo memberInfo = new MemberInfo(user_email, user_pw, user_nick, user_name, user_gender, user_country, user_birth, user_phone, utagsport, utaganimal, utaghumor, utagstudy, utagsogae, utagsing, utagcook, utagdiet, utagmetting);
                uploader(memberInfo);

                UserFollowInfo followInfo = new UserFollowInfo(user_nick, user.getUid());
                uploader(followInfo);


            } else {
                try{
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                MemberInfo memberInfo = new MemberInfo(user_email, user_pw, user_nick, user_name, user_gender, user_country, user_birth, user_phone, utagsport, utaganimal, utaghumor, utagstudy, utagsogae, utagsing, utagcook, utagdiet, utagmetting);
                                uploader(memberInfo);
                            } else {
                                startToast("회원정보를 보내는데 실패하였습니다.");
                            }
                        }
                    });
                }catch(FileNotFoundException e) {
                    Log.e("로그 : ", "에러 : " + e.toString());
                }
            }
        } else {
            startToast("빈칸을 모두 입력해 주세요.");
        }
    }

    private void uploader(MemberInfo memberInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(memberInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startToast("회원정보 등록을 성공하였습니다.");
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("회원정보 등록에 실패하였습니다.");
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    private void uploader(UserFollowInfo followInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_follow").document(user.getUid()).set(followInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "회원정보 등록을 성공하였습니다.");
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //startToast("회원정보 등록에 실패하였습니다.");
                        //Log.w(TAG, "Error writing document", e);
                    }
                });
    }



    //EX)findViewById()를 통해 화면에 표시되는 TextView의 참조 객체를 얻어오는 함수
    public void InitializeView()
    {
        //생년월일
        birthday_Date = (TextView)findViewById(R.id.birthday);

        //국적
        m_country = findViewById(R.id.country);//id가 country인 애으 값을 m_counry에 담아라
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
                //0~11까지 존재하여 현재 월을 표시할 시엔 1을 더해주어야한다
                birthday_Date.setText(year + "." + (monthOfYear+1) + "." + dayOfMonth);
            }
        };

        //국적
        m_country.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                c_SelectDialog.show();
            }
        });

        c_SelectDialog = new AlertDialog.Builder(SignUpActivity.this)
                .setItems(c_name, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        m_country.setText(c_name[i]);
                    }
                })
                .setTitle("국적 선택")
                .setNegativeButton("취소", null)
                .create();
    }

    // OnClickHandler() 함수는 버튼 이벤트 발생에 대해 처리를 담당하는 함수
    //xml에서 호출
    public void OnClickHandler(View view)
    {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);

        DatePickerDialog dialog = new DatePickerDialog(this, callbackMethod, year, month, date);

        dialog.show();
    }


    public void tags()
    {

        tagsport = (Button)findViewById(R.id.tagsport);
        tagsport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsport.setSelected(tagsport.isSelected());
                tagsport.setSelected(!tagsport.isSelected());
            }
        });

        taganimal = (Button)findViewById(R.id.taganimal);
        taganimal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taganimal.setSelected(taganimal.isSelected());
                taganimal.setSelected(!taganimal.isSelected());
            }

        });


        taghumor = (Button)findViewById(R.id.taghumor);
        taghumor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taghumor.setSelected(taghumor.isSelected());
                taghumor.setSelected(!taghumor.isSelected());
            }
        });

        tagstudy = (Button)findViewById(R.id.tagstudy);
        tagstudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagstudy.setSelected(tagstudy.isSelected());
                tagstudy.setSelected(!tagstudy.isSelected());
            }
        });

        tagsogae = (Button)findViewById(R.id.tagsogae);
        tagsogae.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsogae.setSelected(tagsogae.isSelected());
                tagsogae.setSelected(!tagsogae.isSelected());
            }
        });

        tagsing = (Button)findViewById(R.id.tagsing);
        tagsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagsing.setSelected(tagsing.isSelected());
                tagsing.setSelected(!tagsing.isSelected());
            }
        });

        tagcook = (Button)findViewById(R.id.tagcook);
        tagcook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagcook.setSelected(tagcook.isSelected());
                tagcook.setSelected(!tagcook.isSelected());
            }
        });

        tagdiet = (Button)findViewById(R.id.tagdiet);
        tagdiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagdiet.setSelected(tagdiet.isSelected());
                tagdiet.setSelected(!tagdiet.isSelected());
            }
        });

        tagmetting = (Button)findViewById(R.id.tagmetting);
        tagmetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tagmetting.setSelected(tagmetting.isSelected());
                tagmetting.setSelected(!tagmetting.isSelected());
            }
        });

    }


    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

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


    //implements
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUpButton1:
                if(certifyByEmail==1) {
                    signUp();
                } else {
                    startToast("먼저 이메일 인증을 해주세요.");
                }
                break;
            case R.id.signUpButton2:
                if(signUp_nick_check==1 && signUp_check==1) {
                    profileUpdate();
                } else {
                    startToast("먼저 이메일/패스워드 중복 확인과 닉네임 중복확인이 필요합니다.");
                }
                break;
            case R.id.SignUp_gotoLoginButton:
                myStartActivity((LoginActivity.class));
                break;
            case R.id.singup_Back_Btn :
                finish();
                break;

            case R.id.sendBtn:
                    timeout_flag = false;
                    if (flag_auth == false)//인증번호 중복클릭방지
                    {

                        user_email = ((EditText) findViewById(R.id.SignUp_emailEditText)).getText().toString();
                        SendMail mailServer = new SendMail();
                        mailServer.sendSecurityCode(getApplicationContext(), user_email);
                        //인증번호 중복클릭시 타이머 재시작(숫자깨짐방지)
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }

                        if (user_email.length() <= 0) {
                            startToast("이메일을 입력해주세요.");
                            break;
                        } else {

                            CountDownTimer();

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
                    } else {
                        //OK버튼을 누르면 값이 true로 변경
                        startToast("이메일 인증을 마쳤습니다. 남은 빈칸들을 입력해주세요");
                        //인증번호 중복클릭시 타이머 재시작(숫자깨짐방지)
                    }

                break;

            case R.id.emailAuth_btn : //OK 버튼을 눌렀을 시
                //인증번호를 입력하지 않은 경우 > null 값 처리
                final String emailAuth_num = ((EditText) findViewById(R.id.emailAuth_number)).getText().toString();
                if(emailAuth_num.equals("") || emailAuth_num.length() == 0 ){
                    Toast.makeText(this, " 인증번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    user_answer = emailAuth_number.getText().toString();
                    if(timeout_flag == false)
                    {
                        if(user_answer.equals(randomNum)){
                            Toast.makeText(this, "이메일 인증 성공", Toast.LENGTH_SHORT).show();
                            countDownTimer.cancel();//성공 시 타이머 중지
                            flag_auth = true;
                            certifyByEmail = 1;
                        }else{
                            Toast.makeText(this, "이메일 인증 실패, 입력된 이메일 또는 인증번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        startToast("제한 시간이 초과되었습니다. 인증번호를 다시 보내주세요");
                    }

                }
                break;
            case R.id.SignUp_nickCheckButton: // 닉네임 중복확인 버튼을 눌렀을 시
                nickCheckMethod();
                break;
        }
    }

    public void nickCheckMethod() {
        EditText inputnick = ((EditText)findViewById(R.id.SignUp_nickEditText));
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // 회원가입시 입력한 닉네임이 DB에 있는지 없는지 체크하기 위한 부분(중복 닉네임 방지)
        if(inputnick.length() > 0) { // 빈칸 빠짐없이 한글자 이상 입력을 받아야함
            db.collection("users")
                    .whereGreaterThanOrEqualTo("_user_nick", inputnick.getText().toString())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    outputnick = (String) document.get("_user_nick");
                                    if (inputnick.getText().toString().equals(outputnick)) { // 입력한 닉네임이 DB에 있으면
                                        Log.d(TAG, inputnick.getText().toString() + " " + outputnick);
                                        startToast("이미 존재하는 닉네임입니다.");
                                        break;
                                    } else { // 입력한 닉네임이 DB에 없으면
                                        nickCheckDialog nickCheckDialog = new nickCheckDialog(SignUpActivity.this);
                                        // 커스텀 다이얼로그를 호출한다.
                                        nickCheckDialog.nickCheckCallFunction();
                                        signUp_nick_check = 1;
                                        break;
                                    }
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                                startToast("예기치 못한 오류가 발생하였습니다.\n다시 시도해 주세요");
                            }
                        }
                    });
        } else {
            startToast("닉네임을 입력하세요.");
        }
    }
    public void CountDownTimer() { //카운트 다운 메소드

        //줄어드는 시간을 나타내는 TextView
        time_counter = (TextView) findViewById(R.id.emailAuth_time_counter);
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

                cancel();
                timeout_flag= true;
                startToast("제한시간이 다 되었습니다 인증번호를 다시 보내주세요");
            }



        }.start();
        //emailAuth_btn.setOnClickListener(this);



    }

}