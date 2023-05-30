package com.example.multiliveapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.multiliveapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LiveActivity extends AppCompatActivity {

    private FirebaseAuth mAuth ;

    private static final String TAG = "LiveActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        mAuth = FirebaseAuth.getInstance();

        initLayoutSetting();//초기 설정

        start();



        findViewById(R.id.btn_live_end).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                    case (R.id.btn_live_end) :
                        Toast.makeText(getApplicationContext(), "라이브 종료됨.", Toast.LENGTH_SHORT).show();
                        endLive();
                        finish();
                        myStartActivity(MMLMainActivity.class);
                        break;
            }
        }
    };

    public void start() {
        final TextView live_title = findViewById(R.id.tv_live_title);
        final TextView live_join = findViewById(R.id.tv_live_join);
        final TextView live_tag = findViewById(R.id.tv_live_tag);
        final TextView live_host = findViewById(R.id.tv_live_host);

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("live").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            // 프로필 사진 불러오는부분, getActivity()에서 에러 뜸
                            //if(document.getData().get("photoUrl") != null){
                            //   Glide.with(getActivity()).load(document.getData().get("photoUrl")).centerCrop().override(500).into(profileImageView);
                            //}

                            live_title.setText((CharSequence)document.getData().get("_live_title"));
                            //live_join.setText((document.getData().get("_live_join").toString()));
                            live_join.setText(document.getData().get("_live_join").toString());
                            //live_join.setText((document.getData().get("_live_join").toString()));
                            live_tag.setText((CharSequence)document.getData().get("_live_tag"));
                            live_host.setText((CharSequence) document.getData().get("_live_host"));

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

    private void endLive() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("live").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //moveTaskToBack(true);
        //android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(1);
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
