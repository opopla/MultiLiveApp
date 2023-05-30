package com.example.multiliveapp.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiliveapp.R;

import java.util.ArrayList;

public class LiveAddRecyclerAdapter extends RecyclerView.Adapter<LiveAddRecyclerAdapter.ItemViewHolder>{

    //Adapter에 들어갈 데이터 리스트
    private ArrayList<LiveAddData> liveRecyclerList = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_add_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(liveRecyclerList.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return liveRecyclerList.size();
    }
    void addItem(LiveAddData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        //if(!liveRecyclerList.contains(data)) {
            liveRecyclerList.add(data);
        //}
    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView live_tagName;
        private ImageButton tagdeleteBtn;
        ItemViewHolder(View itemView) {
            super(itemView);
            live_tagName = itemView.findViewById(R.id.live_tagName);
            tagdeleteBtn = itemView.findViewById(R.id.tagdeleteBtn);


            tagdeleteBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "태그 삭제", Toast.LENGTH_SHORT).show();
                    liveRecyclerList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    notifyItemRangeChanged(getAdapterPosition(), liveRecyclerList.size());
                }
            });
        }

        void onBind(LiveAddData data) {
            live_tagName.setText(data.getLive_tagName());
        }
    }


}
