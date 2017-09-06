package com.osama.project34.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osama.project34.MailApplication;
import com.osama.project34.R;
import com.osama.project34.data.Folder;
import com.osama.project34.data.Mail;
import com.osama.project34.ui.adapters.MessagesAdapter;

import java.util.ArrayList;

/**
 * Created by bullhead on 8/18/17.
 *
 */

public class MessagesFragment extends Fragment {
    private View mView;
     private RecyclerView                    mDataListView;
    private ArrayList<Mail>                  mMessages;
    private MessagesAdapter                  messagesAdapter;
    private int messageCount=-1;
    private Folder associatedFolder;
    private View noMialView;

    public static MessagesFragment newInstance(final Folder folder) {

        Bundle args = new Bundle();

        MessagesFragment fragment = new MessagesFragment();
        fragment.setArguments(args);
        fragment.associatedFolder=folder;
        return fragment;
    }
    public void setMessagesNumber(int number){
        Log.d("bullhead", "setMessagesNumber: setting message number: "+number);
        if (noMialView!=null && number==0){
            noMialView.setVisibility(View.VISIBLE);
            mView.findViewById(R.id.loading_inbox_bar).setVisibility(View.GONE);
        }
        this.messageCount=number;
    }

    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.data_content_layout,container,false);
        mMessages= MailApplication.getDb().getAllMessages(associatedFolder.getId());
        noMialView=mView.findViewById(R.id.no_mail_view);
        noMialView.setVisibility(View.GONE);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getActivity();
        Log.d("bullhead", "onActivityCreated: activity created");
        if (messageCount==0){
            noMialView.setVisibility(View.VISIBLE);
            mView.findViewById(R.id.loading_inbox_bar).setVisibility(View.GONE);
            return;
        }
        setUpList();
    }

    private void setUpList() {
        mDataListView=(RecyclerView)mView.findViewById(R.id.messages_recycler_view);
        mDataListView.setLayoutManager(new LinearLayoutManager(context));
        messagesAdapter=new MessagesAdapter(context);
        if (mMessages==null){
            mMessages=new ArrayList<>();
        }else{
            mView.findViewById(R.id.loading_inbox_bar).setVisibility(View.GONE);
        }
        messagesAdapter.setDataSet(mMessages);
        mDataListView.setAdapter(messagesAdapter);
    }

    public void updateMessages(ArrayList<Mail> message){
        if (mMessages!=null){
            if (mMessages.size()==0){
                mView.findViewById(R.id.loading_inbox_bar).setVisibility(View.GONE);
            }
            mMessages=message;
            messagesAdapter.setDataSet(mMessages);
            messagesAdapter.notifyItemInserted(mMessages.size()-1);
        }
    }
}
