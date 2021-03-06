package com.osama.project34.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.Gson;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.osama.project34.MailApplication;
import com.osama.project34.R;
import com.osama.project34.data.Mail;
import com.osama.project34.ui.activities.MailViewActivity;
import com.osama.project34.utils.CommonConstants;
import com.osama.project34.utils.ConfigManager;

import java.util.ArrayList;



public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Mail> mDataSet;
    private ArrayList<String> mColors;
    private boolean isLoading = true;
    private int messageNumber;

    private ArrayList<Integer> shownPosition;


    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
        Log.d("bullhead", "setMessageNumber: " + messageNumber);
        if (mDataSet != null && messageNumber == mDataSet.size()) {
            Log.d("bullhead", "setMessageNumber: loading is set to false");
            isLoading = false;
            notifyDataSetChanged();
        }
    }

    public MessagesAdapter(Context context) {
        this.mContext = context;
        shownPosition = new ArrayList<>();
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

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public void setDataSet(ArrayList<Mail> dataset, int messageCount) {
        this.mDataSet = dataset;
        //to check if we reached at end
        setMessageNumber(messageCount);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_mail, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position >= mDataSet.size() && isLoading) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.mailItem.setVisibility(View.GONE);
            return;
        } else {
            holder.progressBar.setVisibility(View.GONE);
            holder.mailItem.setVisibility(View.VISIBLE);
        }
        Mail dataModel = mDataSet.get(position);
        String name = dataModel.getSender().getName() == null ? dataModel.getSender().getMail() : dataModel.getSender().getName();
        holder.mMessageTextTextView.setText(dataModel.getMessage().getText()[0]);
        holder.mMessageSenderTextView.setText(name);
        if (dataModel.isEncrypted()) {
            holder.encryptionStatusImage.setVisibility(View.VISIBLE);
        } else {
            holder.encryptionStatusImage.setVisibility(View.GONE);
        }
        holder.mMessageSubjectTextView.setText(dataModel.getSubject());
        ColorGenerator generator = ColorGenerator.MATERIAL;
        TextDrawable thumb = TextDrawable.builder()
                .buildRound(name.substring(0, 1), generator.getColor(dataModel.getId()));
        holder.senderThumb.setImageDrawable(thumb);


        //hide the mail text
        if (shownPosition.contains(position)) {
            holder.mailTextContent.setVisibility(View.VISIBLE);
        } else if (!ConfigManager.showMailEnabled()) {
            holder.mailTextContent.setVisibility(View.GONE);
        }

        //check if mail is favorite or not and set view according to that
        if (dataModel.isFavorite()) {
            holder.likeButton.setLiked(true);
        } else {
            holder.likeButton.setLiked(false);
        }
    }

    public void showMailText(int position) {
        shownPosition.add(position);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return isLoading ? mDataSet.size() + 1 : mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView mMessageSenderTextView;
        TextView mMessageTextTextView;
        TextView mMessageSubjectTextView;
        ImageView senderThumb;
        ImageView encryptionStatusImage;
        ProgressBar progressBar;
        LinearLayout mailItem;
        LinearLayout mailTextContent;
        LikeButton likeButton;

        public ViewHolder(final View itemView) {
            super(itemView);

            //bind views
            mMessageSenderTextView = (TextView) itemView.findViewById(R.id.list_title_text);
            mMessageTextTextView = (TextView) itemView.findViewById(R.id.list_message_text);
            mMessageSubjectTextView = (TextView) itemView.findViewById(R.id.list_subject_text);
            senderThumb = ((ImageView) itemView.findViewById(R.id.list_sender_thumb));
            encryptionStatusImage = (ImageView) itemView.findViewById(R.id.encryptedStatusImage);
            progressBar = (ProgressBar) itemView.findViewById(R.id.last_progress_bar);
            mailItem = (LinearLayout) itemView.findViewById(R.id.mail_item);
            mailTextContent = (LinearLayout) itemView.findViewById(R.id.mail_text_content);
            likeButton = (LikeButton) itemView.findViewById(R.id.mail_like_button);

            likeButton.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    markMessageFavorite(getAdapterPosition(), mDataSet.get(getAdapterPosition()), likeButton);
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    markMessageFavorite(getAdapterPosition(), mDataSet.get(getAdapterPosition()), likeButton);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getAdapterPosition() >= mDataSet.size()) {
                        return;
                    }
                    Intent intent = new Intent(mContext, MailViewActivity.class);
                    intent.putExtra(CommonConstants.MAIL_VIEW_INTENT, new Gson().toJson(mDataSet.get(getAdapterPosition())));
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private void markMessageFavorite(final int position, final Mail message, final LikeButton itemView) {
        if (message.isFavorite()) {
            //message is already favorite
            message.setFavorite(false);
            itemView.setLiked(false);
            MailApplication.getDb().removeFromFavorite(message); //remove message from favorites
            Snackbar.make(itemView, "Mail removed from favorite", Snackbar.LENGTH_LONG).
                    setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            message.setFavorite(false);
                            markMessageFavorite(position, message, itemView);
                        }
                    }).show();
        } else {
            MailApplication.getDb().insertFavorite(message);
            message.setFavorite(true);
            itemView.setLiked(true);
        }
        mDataSet.remove(position);
        mDataSet.add(position, message);
    }
}
