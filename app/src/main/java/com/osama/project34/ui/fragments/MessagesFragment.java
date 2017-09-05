package com.osama.project34.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osama.project34.R;
import com.osama.project34.data.Mail;
import com.osama.project34.imap.MessagesDataModel;
import com.osama.project34.ui.adapters.MessagesAdapter;

import java.util.ArrayList;

/**
 * Created by bullhead on 8/18/17.
 *
 */

public class MessagesFragment extends Fragment {
    private View mView;
     private RecyclerView                    mDataListView;
    private ArrayList<Mail>     mMessages;
    private MessagesAdapter                  messagesAdapter;

    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.data_content_layout,container,false);
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context=getActivity();
        setUpList();
    }

    private void setUpList() {
        mDataListView=(RecyclerView)mView.findViewById(R.id.messages_recycler_view);
        mDataListView.setLayoutManager(new LinearLayoutManager(context));
        messagesAdapter=new MessagesAdapter(context);
        mMessages=new ArrayList<>();
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
