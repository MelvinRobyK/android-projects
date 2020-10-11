package com.example.todoremainder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<CardViewItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onDeleteClick(int position);
        void onCheckBoxClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder{

        public CheckBox mCheckBox;
        public TextView mTextView1;
        public TextView mTextView2;
        public ImageView mDeleteImage;

        public CustomViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.checkBox);
            mTextView1 = itemView.findViewById(R.id.textViewTitle);
            mTextView2 = itemView.findViewById(R.id.textViewTime);
            mDeleteImage = itemView.findViewById(R.id.image_delete);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener !=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
            mDeleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener !=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
            mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener !=null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onCheckBoxClick(position);
                        }
                    }
                }
            });
        }
    }

    public CustomAdapter(ArrayList<CardViewItem> exampleList){
        mExampleList = exampleList;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item,parent,false);
        CustomViewHolder evh = new CustomViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        CardViewItem currentItem = mExampleList.get(position);
        holder.mCheckBox.setChecked(currentItem.isCheckBox());
        holder.mTextView1.setText(currentItem.getTitle());
        holder.mTextView2.setText(currentItem.getDate()+" / "+currentItem.getTime());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
