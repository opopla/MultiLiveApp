package com.example.multiliveapp.user_info;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

public class TagActivity extends AppCompatActivity {
    private static final String TAG = "TagActivity";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference washingtonRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
    private List<String> list;          // 데이터를 넣은 리스트변수
    private ListView listView;          // 검색을 보여줄 리스트변수
    private EditText editSearch;        // 검색어를 입력할 Input 창
    private ArrayAdapter arrayAdapter; // 리스트뷰에 연결할 아답터

    ImageButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        initLayoutSetting();

        listView = (ListView) findViewById(R.id.tag_listView);

        findViewById(R.id.tag_Back_Btn).setOnClickListener(onClickListener);

        // 리스트를 생성한다.
        list = new ArrayList<String>();

        //다중선택모드의 AraayAdapter 생성
        arrayAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_list_item_multiple_choice, list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // 리스트뷰에 AraayAdapter 아답터를 연결한다.
        listView.setAdapter(arrayAdapter);

        // 검색에 사용할 데이터을 미리 저장한다.
        settingList();

        // input창에 검색어를 입력시 "addTextChangedListener" 이벤트 리스너를 정의한다.
        EditText editSearch = (EditText)findViewById(R.id.tag_editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                // TODO : item filtering
                String filterText = edit.toString() ;
                if (filterText.length() > 0) {
                    listView.setFilterText(filterText) ;
                } else {
                    listView.clearTextFilter() ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //몇 번째 아이템을 선택했는지 메시지를 띄운다 (0부터 시작)
                //Toast.makeText(TagActivity.this, Integer.toString(position) , Toast.LENGTH_SHORT).show();

                // 스포츠 태그 삭제/추가 시 밑에 있는 모든 태그들까지 추가한다는 내용의 토스트가 출력됨
                // 하지만 디비에선 정상적으로 스포츠 태그만 데이터가 들어가서 이를 무시하고 진행.
                //스포츠
                if(listView.isItemChecked(0) == true) {
                    // 디비에서도 삭제 ("")로 만듬
                    washingtonRef
                            .update("_user_stag1", "스포츠")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    //Log.d(TAG, "DocumentSnapshot successfully updated!");
                                    //startToast("스포츠 태그를 성공적으로 추가하였습니다.");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Log.w(TAG, "Error updating document", e);
                                    //startToast("스포츠 태그를 추가하는데 실패하였습니다.");
                                }
                            });
                }
                if(listView.isItemChecked(0) == false) {
                    // 디비에서도 삭제 ("")로 만듬
                    washingtonRef
                            .update("_user_stag1", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }

                // 동물
                if(listView.isItemChecked(1) == true) {
                    washingtonRef
                            .update("_user_stag2", "동물")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
                if(listView.isItemChecked(1) == false) {
                    washingtonRef
                            .update("_user_stag2", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }

                // 유머
                if(listView.isItemChecked(2) == true) {
                    washingtonRef
                            .update("_user_stag3", "유머")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
                if(listView.isItemChecked(2) == false) {
                    washingtonRef
                            .update("_user_stag3", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }

                // 스터디
                if(listView.isItemChecked(3) == true) {
                    washingtonRef
                            .update("_user_stag4", "스터디")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
                if(listView.isItemChecked(3) == false) {
                    washingtonRef
                            .update("_user_stag4", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }

                // 소개팅
                if(listView.isItemChecked(4) == true) {
                    washingtonRef
                            .update("_user_stag5", "소개팅")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
                if(listView.isItemChecked(4) == false) {
                    washingtonRef
                            .update("_user_stag5", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }

                // 노래
                if(listView.isItemChecked(5) == true) {
                    washingtonRef
                            .update("_user_stag6", "노래")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
                if(listView.isItemChecked(5) == false) {
                    washingtonRef
                            .update("_user_stag6", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }

                // 요리
                if(listView.isItemChecked(6) == true) {
                    washingtonRef
                            .update("_user_stag7", "요리")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
                if(listView.isItemChecked(6) == false) {
                    washingtonRef
                            .update("_user_stag7", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }

                // 다이어트
                if(listView.isItemChecked(7) == true) {
                    washingtonRef
                            .update("_user_stag8", "다이어트")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
                if(listView.isItemChecked(7) == false) {
                    washingtonRef
                            .update("_user_stag8", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }

                // 모임
                if(listView.isItemChecked(8) == true) {
                    washingtonRef
                            .update("_user_stag9", "모임")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }
                if(listView.isItemChecked(8) == false) {
                    washingtonRef
                            .update("_user_stag9", "")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                }
                            });
                }



            }
        });

        //n번쩨 아이템 상태를 바꾼다
        //listView.setItemChecked(2, true);

        TextView tag1 = findViewById(R.id.tagsport);
        TextView tag2 = findViewById(R.id.taganimal);
        TextView tag3 = findViewById(R.id.taghumor);
        TextView tag4 = findViewById(R.id.tagstudy);
        TextView tag5 = findViewById(R.id.tagsogae);
        TextView tag6 = findViewById(R.id.tagsing);
        TextView tag7 = findViewById(R.id.tagcook);
        TextView tag8 = findViewById(R.id.tagdiet);
        TextView tag9 = findViewById(R.id.tagmetting);


        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            tag1.setText((CharSequence) document.getData().get("_user_stag1"));
                            tag2.setText((CharSequence) document.getData().get("_user_stag2"));
                            tag3.setText((CharSequence) document.getData().get("_user_stag3"));
                            tag4.setText((CharSequence) document.getData().get("_user_stag4"));
                            tag5.setText((CharSequence) document.getData().get("_user_stag5"));
                            tag6.setText((CharSequence) document.getData().get("_user_stag6"));
                            tag7.setText((CharSequence) document.getData().get("_user_stag7"));
                            tag8.setText((CharSequence) document.getData().get("_user_stag8"));
                            tag9.setText((CharSequence) document.getData().get("_user_stag9"));


                            // tag1의 내용이 없으면
                            if(tag1.getText().toString().equals("") || tag1.getText().toString() == null) {
                                // 체크박스가 false이고
                                //listView.setItemChecked(0, false);
                            } else { // 내용이 있으면 체크박스 체크표시
                                listView.setItemChecked(0, true); // true 상태로
                            }

                            if(tag2.getText().toString().equals("") || tag2.getText().toString() == null) {
                                //listView.setItemChecked(1, false);
                            } else {
                                listView.setItemChecked(1, true);
                            }

                            if(tag3.getText().toString().equals("") || tag3.getText().toString() == null) {
                                //listView.setItemChecked(2, false);
                            } else {
                                listView.setItemChecked(2, true);
                            }

                            if(tag4.getText().toString().equals("") || tag4.getText().toString() == null) {
                                //listView.setItemChecked(3, false);
                            } else {
                                listView.setItemChecked(3, true);
                            }

                            if(tag5.getText().toString().equals("") || tag5.getText().toString() == null) {
                                //listView.setItemChecked(4, false);
                            } else {
                                listView.setItemChecked(4, true);
                            }

                            if(tag6.getText().toString().equals("") || tag6.getText().toString() == null) {
                                listView.setItemChecked(5, false);
                            } else {
                                listView.setItemChecked(5, true);
                            }

                            if(tag7.getText().toString().equals("") || tag7.getText().toString() == null) {
                                listView.setItemChecked(6, false);
                            } else {
                                listView.setItemChecked(6, true);
                            }

                            if(tag8.getText().toString().equals("") || tag8.getText().toString() == null) {
                                listView.setItemChecked(7, false);
                            } else {
                                listView.setItemChecked(7, true);
                            }

                            if(tag9.getText().toString().equals("") || tag9.getText().toString() == null) {
                                listView.setItemChecked(8, false);
                            } else {
                                listView.setItemChecked(8, true);
                            }

                        } else {
                            // Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    //  Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

        /* n번째 아이템의 체크상태가 true OR false라면
        if(listView.isItemChecked(3))
        {
            listView.setItemChecked(3,false);
        }*/
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tag_Back_Btn : // 뒤로가기 버튼
                    finish();
                    break;
            }
        }
    };

    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList() {
        arrayAdapter.add("스포츠") ;
        arrayAdapter.add("동물") ;
        arrayAdapter.add("유머") ;
        arrayAdapter.add("스터디") ;
        arrayAdapter.add("소개팅") ;
        arrayAdapter.add("노래") ;
        arrayAdapter.add("요리") ;
        arrayAdapter.add("다이어트") ;
        arrayAdapter.add("모임") ;
    }

    private void startToast(String msg) { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }

    private void initLayoutSetting() {
        //상단 액션바 안보이게 하는 함수
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //키패드가 레이아웃을 밀어내지않음
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
}