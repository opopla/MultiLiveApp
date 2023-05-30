package com.example.multiliveapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.multiliveapp.R;
import com.example.multiliveapp.followDB.DBHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class revokeAccessDialog {
    private Context context;
    private FirebaseAuth mAuth ;
    private static final String TAG = "revokeAccessDialog";
    public static DBHelper dbHelper;
    public revokeAccessDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void revokeAccessCallFunction() {
        dbHelper = new DBHelper(context, "follow.db", null, 1);
        mAuth = FirebaseAuth.getInstance();

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.revokeaccess_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button yesBtn = (Button) dlg.findViewById(R.id.revokeaccess_dialog_yes_btn);
        final Button noBtn = (Button) dlg.findViewById(R.id.revokeaccess_dialog_no_btn);

        yesBtn.setOnClickListener(new View.OnClickListener() { // 탈퇴하기 버튼을 클릭시
            @Override
            public void onClick(View view) {
                // 사용자 Auth 삭제
                revokeAccess();
                // 사용자 DB 삭제
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
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
                dbHelper.deleteUSER(MMLMainActivity.myNick);

                Toast.makeText(context.getApplicationContext(), "회원탈퇴가 정상적으로 처리되었습니다.", Toast.LENGTH_SHORT).show();
                myStartActivity(LoginActivity.class);
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() { // 취소 버튼을 클릭시
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소했습니다.", Toast.LENGTH_SHORT).show();

                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void nickCheckCallFunction() {
        mAuth = FirebaseAuth.getInstance();

    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(context, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    private void revokeAccess() {
        mAuth.getCurrentUser().delete();
    }
}
