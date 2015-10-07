package com.github.andlyticsproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.andlyticsproject.model.DeveloperAccount;

import java.util.ArrayList;
import java.util.List;

public class AccountListAdapter extends RecyclerView.Adapter<AccountListAdapter.ViewHolder> {
    private final AccountSelectedListener listener;
    private List<DeveloperAccount> developerAccounts = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView emailDeveloper;
        public CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);

            emailDeveloper = (TextView) itemView.findViewById(R.id.login_list_item_text);
            checkBox = (CheckBox) itemView.findViewById(R.id.login_list_item_enabled);
        }
    }

    public AccountListAdapter(List<DeveloperAccount> developerAccounts, AccountSelectedListener listener) {
        this.listener = listener;
        this.developerAccounts.addAll(developerAccounts);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.login_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DeveloperAccount developerAccount = developerAccounts.get(position);
        holder.emailDeveloper.setText(developerAccount.getName());
        holder.checkBox.setChecked(!developerAccount.isHidden());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.accountSelected(holder.checkBox.isChecked(), developerAccounts.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return developerAccounts.size();
    }
}
