/*import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.multiliveapp.R;
import com.example.multiliveapp.activity.MMLMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/package com.example.multiliveapp.activity;



import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.multiliveapp.R;
import com.example.multiliveapp.activity.MMLMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FollowAdapter extends BaseAdapter {
    private Context context;
    private List<String> list;
    private LayoutInflater inflate;
    private ViewHolder viewHolder;
    private String TAG = "FollowAdapter";

    private String myNick;
    private int followNum;


    public FollowAdapter(List<String> list, Context context, String myNick, int followNum) {
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
        this.myNick = myNick;
        this.followNum = followNum;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.follow_listview_item, null);

            viewHolder = new ViewHolder();
            viewHolder.label = (TextView) convertView.findViewById(R.id.label);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.label.setText(list.get(position));

        //버튼 클릭 시 리스트 뷰에 있는 데이터를 삭제한다.
        final String str = list.get(position) + "와(과) 팔로우 해제되었습니다.";

        getUserData();

        Log.d(TAG, "팔로우어댑터 : " + list);

        Button btn = (Button) convertView.findViewById(R.id.follow_releaseBtn);
        btn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference dbRef = db.collection("user_follow").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

                followNum -= 1;

                Log.d(TAG, "팔로우어댑터 : " + position);

                list.remove(position);

                ArrayList<String> listCopy = new ArrayList<String>();

                for(int i=0; i<list.size(); ++i) {
                    if(list.get(i).equals("")) {
                        for(int j=position+1; j<list.size(); ++j) {
                            listCopy.add(list.get(j));
                        }
                    }
                    else {
                        listCopy.add(list.get(i));
                    }
                }

                HashMap<String, Object> m = new HashMap<String, Object>();

                m.put("followNum", Integer.toString(followNum));

                for(int i=0; i<listCopy.size(); ++i) {
                    m.put("follow"+(i+1), listCopy.get(i));
                }
                m.put("userNick", myNick);

                dbRef.set(m).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "데이터베이스 확인해주세요 &&");
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                            }

                        });


//                ((MMLMainActivity)MMLMainActivity.mContext).getMyFollowList();
//                ((MMLMainActivity)MMLMainActivity.mContext).getData(0);
                ((MMLMainActivity)MMLMainActivity.mContext).setFollowListMML(m);
                FollowAdapter.this.notifyDataSetChanged();

            }
        });

        return convertView;
    }

    private void getUserData() {
        //user_follow 데이터베이스에서 userNick = 내 닉네임 인 문서 받아온다.
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_follow").whereEqualTo("userNick", myNick).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //데이터베이스에서 followNum 받아오기
                        followNum = Integer.parseInt(document.getData().get("followNum").toString());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }


}

class ViewHolder {
    public TextView label;
}


*/

