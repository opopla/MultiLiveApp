package com.example.multiliveapp.user_info;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiliveapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "UserInfoFragment";

    public UserInfoActivity() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        start();

        initLayoutSetting();

        findViewById(R.id.userInfo_Back_Btn).setOnClickListener(this);
        findViewById(R.id.userInfo_checkBtn).setOnClickListener(this);

    }

    public void start() {
        final TextView emailTextView = findViewById(R.id.userInfo_emailText);
        final TextView pwTextView = findViewById(R.id.userInfo_pwText);
        final TextView nickTextView = findViewById(R.id.userInfo_nickText);
        final TextView nameTextView = findViewById(R.id.userInfo_nameText);
        final TextView genderTextView = findViewById(R.id.userInfo_genderText);
        final TextView countryTextView = findViewById(R.id.userInfo_countryText);
        final TextView birthDayTextView = findViewById(R.id.userInfo_birthText);
        final TextView phoneNumberTextView = findViewById(R.id.userInfo_phoneText);

        final TextView tag1 = findViewById(R.id.tagsport);
        final TextView tag2 = findViewById(R.id.taganimal);
        final TextView tag3 = findViewById(R.id.taghumor);
        final TextView tag4 = findViewById(R.id.tagstudy);
        final TextView tag5 = findViewById(R.id.tagsogae);
        final TextView tag6 = findViewById(R.id.tagsing);
        final TextView tag7 = findViewById(R.id.tagcook);
        final TextView tag8 = findViewById(R.id.tagdiet);
        final TextView tag9 = findViewById(R.id.tagmetting);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());

                            emailTextView.setText((CharSequence)document.getData().get("_user_email"));
                            pwTextView.setText((CharSequence) document.getData().get("_user_pw"));
                            nickTextView.setText((CharSequence)document.getData().get("_user_nick"));
                            nameTextView.setText((CharSequence) document.getData().get("_user_name"));
                            genderTextView.setText((CharSequence) document.getData().get("_user_gender"));
                            countryTextView.setText((CharSequence) document.getData().get("_user_country"));
                            phoneNumberTextView.setText((CharSequence) document.getData().get("_user_phone"));
                            birthDayTextView.setText((CharSequence) document.getData().get("_user_birth"));
                            tag1.setText((CharSequence) document.getData().get("_user_stag1"));
                            tag2.setText((CharSequence) document.getData().get("_user_stag2"));
                            tag3.setText((CharSequence) document.getData().get("_user_stag3"));
                            tag4.setText((CharSequence) document.getData().get("_user_stag4"));
                            tag5.setText((CharSequence) document.getData().get("_user_stag5"));
                            tag6.setText((CharSequence) document.getData().get("_user_stag6"));
                            tag7.setText((CharSequence) document.getData().get("_user_stag7"));
                            tag8.setText((CharSequence) document.getData().get("_user_stag8"));
                            tag9.setText((CharSequence) document.getData().get("_user_stag9"));

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

    private void initLayoutSetting() {
        //상단 액션바 안보이게 하는 함수
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //키패드가 레이아웃을 밀어내지않음
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userInfo_Back_Btn :
                finish();
                break;
            case R.id.userInfo_checkBtn :
                finish();
                break;
        }
    }
}
