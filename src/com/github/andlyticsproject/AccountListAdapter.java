package com.github.andlyticsproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.andlyticsproject.model.DeveloperAccount;

import java.util.ArrayList;
import java.util.List;

public class AccountListAdapter extends ArrayAdapter<DeveloperAccount> {

    private final Context context;
    private final int resource;
    private final AccountSelectedListener listener;
    private List<DeveloperAccount> developerAccounts = new ArrayList<>();

    public AccountListAdapter(Context context, int resource, List<DeveloperAccount> developerAccounts, AccountSelectedListener listener) {
        super(context, resource, developerAccounts);
        this.resource = resource;
        this.context = context;
        this.listener = listener;
        this.developerAccounts.addAll(developerAccounts);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(resource, parent, false);
        final TextView developerEmail = (TextView) rowView.findViewById(R.id.login_list_item_text);
        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.login_list_item_enabled);
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.accountSelected(checkBox.isChecked(), developerAccounts.get(position));
            }
        });
        developerEmail.setText(developerAccounts.get(position).getName());
        checkBox.setChecked(!developerAccounts.get(position).isHidden());
        return rowView;
    }
}
