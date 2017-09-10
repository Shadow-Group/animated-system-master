package com.osama.project34.ui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.osama.project34.R;

/**
 * Created by home on 1/27/17.
 */

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.ViewHolder> {
    private String[] mDataSet;
    private Context mContext;

    public AccountsAdapter(Context context, String[] mDataSet) {
        this.mDataSet = mDataSet;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.accounts_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView view = holder.emailTextView;
        ImageView imageView = holder.accountThumb;

        //in the last add the 'add new account' option
        if (position >= mDataSet.length) {
            view.setText("Add new account");
            imageView.setImageDrawable(mContext.getDrawable(R.drawable.ic_add_account));
        } else {
            view.setText(mDataSet[position]);
            imageView.setImageDrawable(holder.accountThumb.getDrawable());
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.length + 1; //+1 because I have to show add account item too
    }

    public void setData(String[] mUserAccountsAddress) {
        this.mDataSet = mUserAccountsAddress;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView emailTextView;
        ImageView accountThumb;
        private AdapterCallbacks mCallbacks = (AdapterCallbacks) mContext;

        public ViewHolder(View itemView) {
            super(itemView);

            //inflate views
            emailTextView = (TextView) itemView.findViewById(R.id.account_email);
            accountThumb = (ImageView) itemView.findViewById(R.id.account_thumb);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallbacks.viewClicked(getAdapterPosition());
                }
            });
        }
    }


}
