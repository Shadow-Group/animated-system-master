package com.osama.project34.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.osama.project34.imap.MessagesDataModel;
import com.osama.project34.R;

import java.util.ArrayList;

/**
 * Created by home on 1/29/17.
 *
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<MessagesDataModel> mDataSet;

    public MessagesAdapter(Context context){
        this.mContext=context;
    }
    public void setDataSet(ArrayList<MessagesDataModel> dataset){
        this.mDataSet=dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mail,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       MessagesDataModel dataModel=mDataSet.get(position);
        holder.mMessageTextTextView.setText(dataModel.getMessageText());
        holder.mMessageSenderTextView.setText(dataModel.getMessageSender());
        holder.mMessageSubjectTextView.setText(dataModel.getMessageSubject());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mMessageSenderTextView;
        TextView mMessageTextTextView;
        TextView mMessageSubjectTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            //bind views
            mMessageSenderTextView      = (TextView)itemView.findViewById(R.id.list_title_text);
            mMessageTextTextView        = (TextView)itemView.findViewById(R.id.list_message_text);
            mMessageSubjectTextView     = (TextView)itemView.findViewById(R.id.list_subject_text);
        }
    }
}
