package com.example.multiliveapp.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.multiliveapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class nickCheckDialog {

    Button nickCheckButton2;
    private Context context;
    private FirebaseAuth mAuth ;
    private static final String TAG = "nickCheckDialog";
    public nickCheckDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void nickCheckCallFunction() {

        mAuth = FirebaseAuth.getInstance();

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.nickcheck_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final Button yesBtn = (Button) dlg.findViewById(R.id.nickcheck_dialog_yes_btn);
        final Button noBtn = (Button) dlg.findViewById(R.id.nickcheck_dialog_no_btn);

        yesBtn.setOnClickListener(new View.OnClickListener() { // YES 버튼을 클릭시
            @Override
            public void onClick(View view) {
                Toast.makeText(context.getApplicationContext(), "입력하신 닉네임으로 적용되었습니다.", Toast.LENGTH_SHORT).show();
                SignUpActivity.nickCheckButton.setEnabled(false); // 닉네임 중복확인 버튼을 비활성화
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() { // NO 버튼을 클릭시
            @Override
            public void onClick(View view) {
                // 다시 회원가입 화면으로
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}
