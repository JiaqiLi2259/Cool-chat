package com.example.jiaqili.coolchat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jiaqi Li on 2018/4/1.
 */

public class MessageHandleAdapter extends RecyclerView.Adapter<MessageHandleAdapter.ViewHolder> {
    private List<MessageHandle> myMessageList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout,rightLayout,leftParentLayout;
        RelativeLayout rightParentLayout;
        TextView leftMessage,rightMessage;
        ImageView leftImage,rightImage;
        public ViewHolder(View view){
            super(view);
            leftLayout=view.findViewById(R.id.left_layout);
            leftParentLayout=view.findViewById(R.id.left_parent_layout);
            rightLayout=view.findViewById(R.id.right_layout);
            rightParentLayout=view.findViewById(R.id.right_parent_layout);
            leftMessage=view.findViewById(R.id.tvLeftMessage);
            rightMessage=view.findViewById(R.id.tvRightMessage);
            leftImage=view.findViewById(R.id.ivLeftUserImage);
            rightImage=view.findViewById(R.id.ivRightUserImage);
        }
    }
    public MessageHandleAdapter(List<MessageHandle> messageList){
        this.myMessageList=messageList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        MessageHandle msg=myMessageList.get(position);
        switch (msg.getType()){
            case MessageHandle.TYPE_RECEIVE:
                holder.leftParentLayout.setVisibility(View.VISIBLE);//注意控件的初始化
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.leftMessage.setText(msg.getContent());
                holder.leftImage.setVisibility(View.VISIBLE);
                holder.rightParentLayout.setVisibility(View.GONE);
                //holder.rightLayout.setVisibility(View.GONE);
                //holder.rightImage.setVisibility(View.GONE);
                break;
            case MessageHandle.TYPE_SEND:
                holder.rightParentLayout.setVisibility(View.VISIBLE);//注意控件的初始化
                holder.rightLayout.setVisibility(View.VISIBLE);
                holder.rightMessage.setText(msg.getContent());
                holder.rightImage.setVisibility(View.VISIBLE);
                holder.leftParentLayout.setVisibility(View.GONE);
                //holder.leftLayout.setVisibility(View.GONE);
                //holder.leftImage.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }
    @Override
    public int getItemCount(){
        return myMessageList.size();
    }
}
