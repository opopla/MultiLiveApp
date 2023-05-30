
package com.example.multiliveapp.activity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiliveapp.ConnectActivity;
import com.example.multiliveapp.R;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerAdapter.ItemViewHolder> {

    //Adapter에 들어갈 데이터 리스트
    private ArrayList<MainRecyclerData> mainRecyclerList = new ArrayList<>();
    Context context;
    ArrayList<String> unFilteredlist;
    ArrayList<String> filteredList;
    private String TAG = "MainRecyclerAdapter";


    //내 닉네임
    private String myNick;

    public MainRecyclerAdapter(Context context, ArrayList<String> list) {
        super();
        this.context = context;
        this.unFilteredlist = list;
        this.filteredList = list;
    }

    public MainRecyclerAdapter() {
        super();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recyclerview_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(mainRecyclerList.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return mainRecyclerList.size();
    }

    void addItem(MainRecyclerData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        mainRecyclerList.add(data);
    }

    public void remove() {
        int size = mainRecyclerList.size();
        mainRecyclerList.clear();
        notifyItemRangeRemoved(0, size);
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {
        private MainRecyclerData data = new MainRecyclerData();
        private TextView userNickname;
        private TextView liveTitle;
        private TextView tag;
        private ImageButton followBtn;
        private LinearLayout liveBtn;
        private String roomNum;


        ItemViewHolder(View itemView) {
            super(itemView);
            liveTitle = itemView.findViewById(R.id.mainRecyclerItemLiveTitle);
            userNickname = itemView.findViewById(R.id.mainRecyclerItemUserNickname);
            tag = itemView.findViewById(R.id.mainRecyclerItemTag);
            followBtn = itemView.findViewById(R.id.mainRecyclerfollowBtn);
            liveBtn = itemView.findViewById(R.id.rec_item_linear);


            liveBtn.setOnClickListener(new View.OnClickListener() {
                //리니어레이아웃 눌렀을때 이벤트 처리
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    int position = getAdapterPosition();

                    Intent i = new Intent(itemView.getContext(), ConnectActivity.class);
                    i.putExtra("recycler_position", position);
                    i.putExtra("recycler_hostUser", mainRecyclerList.get(position).getUserNickname());
                    i.putExtra("recycler_liveTitle", mainRecyclerList.get(position).getLiveTitle());
                    i.putExtra("recycler_roomNumber", mainRecyclerList.get(position).getRoomNumber());
                    i.putExtra("type1", "Main");
                    context.startActivity(i);
                }
            });

            followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //팔로우 한 유저일경우 true가 전달되어야 함.
                    int pos = getAdapterPosition();

                    //팔로우 된 상태
                    if (mainRecyclerList.get(pos).getFollowStatus() == true) {
                        changeFollowStatus(true);
                        Log.d(TAG, "MRA 팔로우 해제버튼 클릭");
                    } else {
                        changeFollowStatus(false);
                    }

                }
            });

        }


        //팔로우 상태 변경하는 함수
        private void changeFollowStatus(boolean flag) {
            //선택한 유저 닉네임 받아오기
            String selectUserNick = userNickname.getText().toString();
            //선택한 리사이클러 아이템의 포지션 받아오기
            int position = getAdapterPosition();


            if (flag == true) {
                //팔로우 상태에서 -> 팔로우 해제하는 경우

                //db에서 삭제
                MMLMainActivity.dbHelper.delete(selectUserNick);
                //팔로우 상태 false로 바꿔주기
                mainRecyclerList.get(position).setFollowStatus(false);
                //버튼 이미지 언팔로우 이미지로 바꿔주기
                followBtn.setImageResource(R.drawable.un_follow);

            } else if (flag == false) {
                //팔로우 X -> 팔로우 O
                //db에 추가
                MMLMainActivity.dbHelper.insert(myNick, selectUserNick);
                //팔로우 상태 true로 바꿔주기
                mainRecyclerList.get(position).setFollowStatus(true);
                //버튼 이미지 팔로우 이미지로 바꿔주기
                followBtn.setImageResource(R.drawable.follow);
            }
            //리사이클러에 데이터 변경됐다고 알려주기
            MainRecyclerAdapter.this.notifyDataSetChanged();
            ((MMLMainActivity)MMLMainActivity.mContext).callRC();

        }


        void onBind(MainRecyclerData data) {
            userNickname.setText(data.getUserNickname());
            liveTitle.setText(data.getLiveTitle());
            tag.setText(data.getLiveTag());
            roomNum = data.getRoomNumber();
            int position = getAdapterPosition();

            //현 리사이클러 아이템의 팔로우 상태 받아오기
            boolean flags = data.getFollowStatus();

            //내 닉네임 받아오기
            myNick = mainRecyclerList.get(position).getMyNick();

            //팔로우 상태에 맞춰 버튼 이미지 바꿔주기
            if (flags == true) {
                //팔로우
                followBtn.setImageResource(R.drawable.follow);
            } else {
                //언팔로우
                followBtn.setImageResource(R.drawable.un_follow);
            }


        }
    }

    public void removeItem(int position) {
        mainRecyclerList.remove(position);
        notifyItemRemoved(position);
    }

    public void updateData(ArrayList<MainRecyclerData> data) {
        mainRecyclerList.clear();
        mainRecyclerList.addAll(data);
        notifyDataSetChanged();
    }

}