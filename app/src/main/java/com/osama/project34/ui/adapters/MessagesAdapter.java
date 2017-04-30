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
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.messages_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView textView   = holder.mMessageToTextView;
        TextView textView1  = holder.mMessageDateTextView;
        TextView textView2  = holder.mMessageSubjectTextView;

        MessagesDataModel dataModel = mDataSet.get(position);

        //set the fields
        textView.setText(dataModel.getMessageRecipients()[0]);
        textView1.setText(dataModel.getMessageDate());
        textView2.setText(dataModel.getMessageSubject());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mMessageToTextView;
        TextView mMessageDateTextView;
        TextView mMessageSubjectTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            //bind views
            mMessageToTextView      = (TextView)itemView.findViewById(R.id.message_to_textview);
            mMessageDateTextView    = (TextView)itemView.findViewById(R.id.message_date);
            mMessageSubjectTextView = (TextView)itemView.findViewById(R.id.message_subject);
        }
    }
}
