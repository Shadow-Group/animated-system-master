package com.osama.project34.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.osama.project34.R;
import com.osama.project34.data.Mail;
import com.osama.project34.imap.MultiPartHandler;

import java.util.ArrayList;

/**
 * Created by home on 1/29/17.
 *
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<Mail> mDataSet;
    private ArrayList<String> mColors;


    public MessagesAdapter(Context context){
        this.mContext=context;
        mColors = new ArrayList<>();
        mColors.add("#F44336");
        mColors.add("#E91E63");
        mColors.add("#9C27B0");
        mColors.add("#673AB7");
        mColors.add("#3F51B5");
        mColors.add("#2196F3");
        mColors.add("#00BCD4");
        mColors.add("#009688");
        mColors.add("#4CAF50");
        mColors.add("#CDDC39");
        mColors.add("#FFC107");
        mColors.add("#FF9800");
        mColors.add("#FF5722");
        mColors.add("#795548");
        mColors.add("#607D8B");
    }
    public void setDataSet(ArrayList<Mail> dataset){
        this.mDataSet=dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mail,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
       Mail dataModel=mDataSet.get(position);
        String name=dataModel.getSender();
        holder.mMessageTextTextView.setText(dataModel.getMessage().getText());
        holder.mMessageSenderTextView.setText(name);
        holder.mMessageSubjectTextView.setText(dataModel.getSubject());
        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable thumb=TextDrawable.builder()
                .buildRound(name.substring(0,1), generator.getColor(dataModel.getId()));
        holder.senderThumb.setImageDrawable(thumb);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView mMessageSenderTextView;
        TextView mMessageTextTextView;
        TextView mMessageSubjectTextView;
        ImageView senderThumb;

        public ViewHolder(View itemView) {
            super(itemView);

            //bind views
            mMessageSenderTextView      = (TextView)itemView.findViewById(R.id.list_title_text);
            mMessageTextTextView        = (TextView)itemView.findViewById(R.id.list_message_text);
            mMessageSubjectTextView     = (TextView)itemView.findViewById(R.id.list_subject_text);
            senderThumb                 = ((ImageView) itemView.findViewById(R.id.list_sender_thumb));
        }
    }
}
