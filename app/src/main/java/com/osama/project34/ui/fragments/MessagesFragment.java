package com.osama.project34.ui.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.osama.project34.MailApplication;
import com.osama.project34.R;
import com.osama.project34.data.Folder;
import com.osama.project34.data.Mail;
import com.osama.project34.imap.EmailTasks;
import com.osama.project34.imap.FolderNames;
import com.osama.project34.ui.adapters.MessagesAdapter;
import com.osama.project34.ui.widgets.MailListTouchHelper;
import com.osama.project34.utils.ConfigManager;

import java.util.ArrayList;

/**
 * Created by bullhead on 8/18/17.
 */

public class MessagesFragment extends Fragment implements MailListTouchHelper.OnSwiped {
    private View mView;
    private RecyclerView mDataListView;
    private ArrayList<Mail> mMessages;
    private MessagesAdapter messagesAdapter;
    private int messageCount = -1;
    private Folder associatedFolder;
    private View noMialView;
    private Context context;

    private boolean favorite;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static MessagesFragment newInstance(final Folder folder) {

        Bundle args = new Bundle();

        MessagesFragment fragment = new MessagesFragment();
        fragment.setArguments(args);
        fragment.associatedFolder = folder;
        fragment.favorite = folder.getId() == FolderNames.ID_FAVORITE;
        return fragment;
    }

    public void setMessagesNumber(int number) {
        if (noMialView != null && number == 0) {
            noMialView.setVisibility(View.VISIBLE);
            mView.findViewById(R.id.loading_inbox_bar).setVisibility(View.GONE);
            if (messagesAdapter != null) {
                messagesAdapter.setLoading(false);
            }
        }
        if (messagesAdapter != null) {
            messagesAdapter.setMessageNumber(number);
        }
        this.messageCount = number;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.data_content_layout, container, false);
        if (mMessages == null) {
            mMessages = MailApplication.getDb().getAllMessages(associatedFolder.getId());
        }
        if (favorite) {
            mMessages = MailApplication.getDb().getAllFavoriteMails();
            setMessagesNumber(mMessages.size());
        }
        noMialView = mView.findViewById(R.id.no_mail_view);
        noMialView.setVisibility(View.GONE);
        return mView;
    }

    public void setMessages(ArrayList<Mail> mMessages) {
        this.mMessages = mMessages;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        if (messageCount == 0) {
            noMialView.setVisibility(View.VISIBLE);
            mView.findViewById(R.id.loading_inbox_bar).setVisibility(View.GONE);
            return;
        }
        setUpList();
    }

    private void setUpList() {
        mDataListView = (RecyclerView) mView.findViewById(R.id.messages_recycler_view);
        MailListTouchHelper touchHelper = new MailListTouchHelper(context, this);
        ItemTouchHelper helper = new ItemTouchHelper(touchHelper);
        helper.attachToRecyclerView(mDataListView);
        mDataListView.setLayoutManager(new LinearLayoutManager(context));
        messagesAdapter = new MessagesAdapter(context);
        if (mMessages == null) {
            mMessages = new ArrayList<>();
        } else {
            mView.findViewById(R.id.loading_inbox_bar).setVisibility(View.GONE);
        }
        messagesAdapter.setDataSet(mMessages, messageCount);
        mDataListView.setAdapter(messagesAdapter);
    }

    public void setLoading(boolean loading) {
        if (messagesAdapter != null) {
            messagesAdapter.setLoading(loading);
        }
    }

    public void updateMessages(ArrayList<Mail> message) {
        if (mMessages != null) {
            if (mMessages.size() == 0) {
                mView.findViewById(R.id.loading_inbox_bar).setVisibility(View.GONE);
            }
            mMessages = message;
            messagesAdapter.setDataSet(mMessages, messageCount);
            messagesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void toLeft(int position) {
        if (position >= mMessages.size()) {
            return;
        }
        showDeletionDialog(position);

    }

    private void showDeletionDialog(final int position) {
        new AlertDialog.Builder(context, ConfigManager.isDarkTheme() ? R.style.DialogStyleDark : R.style.DialogStyleLight)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        messagesAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .setTitle("Deleting mail")
                .setMessage("Do you really want to delete this message")
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        messagesAdapter.notifyItemChanged(position);
                    }
                })
                .show();
    }

    private void deleteMessage(final int position) {
        if (position >= mMessages.size()) {
            return;
        }
        Log.d("bullhead", "toLeft: hello world");
        //delete mail
        try {
            Mail mail = mMessages.get(position);
            mMessages.remove(mail);
            messagesAdapter.notifyDataSetChanged();
            EmailTasks.deleteMail(mail);
            Log.d("bullhead", "toLeft: message deleted");
            Snackbar.make(mView, "Message deleted.", Snackbar.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("bullhead", "toLeft: ");
        }
    }

    @Override
    public void toRight(int position) {
        //show message content
        try {
            messagesAdapter.showMailText(position);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
