package com.github.andlyticsproject;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.andlyticsproject.model.DeveloperAccount;
import com.github.andlyticsproject.sync.AutosyncHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Used for initial login and managing accounts Because of this original legacy as the launcher
 * activity, navigation is a little odd.
 * On first startup: LoginActivity -> Main
 * When managing
 * accounts: Main -> LoginActivity <- Main
 * or
 * Main -> LoginActivity -> Main
 */
public class LoginActivity extends MaterialActivity {

	private static final String TAG = LoginActivity.class.getSimpleName();

	public static final String EXTRA_MANAGE_ACCOUNTS_MODE = "com.github.andlyticsproject.manageAccounts";

	public static final String AUTH_TOKEN_TYPE_ANDROID_DEVELOPER = "androiddeveloper";

	private List<DeveloperAccount> developerAccounts;

	private boolean manageAccountsMode = false;
	private View okButton;

	private AccountManager accountManager;
	private DeveloperAccountManager developerAccountManager;
	private AutosyncHandler syncHandler;
	private View busyView;
	private AccountListAdapter adapter;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initToolbar();
		busyView = findViewById(R.id.busyView);
		RecyclerView accountList = (RecyclerView) findViewById(R.id.accountList);
		okButton = findViewById(R.id.login_ok_button);
		setBusy(true);

		accountManager = AccountManager.get(this);
		developerAccountManager = DeveloperAccountManager.getInstance(getApplicationContext());
		syncHandler = new AutosyncHandler();

		// When called from accounts action item in Main, this flag is passed to
		// indicate
		// that LoginActivity should not auto login as we are managing the
		// accounts,
		// rather than performing the initial login
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			manageAccountsMode = extras.getBoolean(LoginActivity.EXTRA_MANAGE_ACCOUNTS_MODE);
		}

		if (manageAccountsMode) {
			getSupportActionBar().setTitle(R.string.manage_accounts);
		}


		setBusy(false);

		okButton.setClickable(true);
		okButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AsyncTask<Void, Void, Void>() {

					@Override
					protected void onPreExecute() {
						setBusy(true);
						okButton.setEnabled(false);
					}

					@Override
					protected Void doInBackground(Void... args) {
						saveDeveloperAccounts();
						return null;
					}

					@Override
					protected void onPostExecute(Void arg) {
						setBusy(false);
						okButton.setEnabled(true);
						for (DeveloperAccount account : developerAccounts) {
							if (account.isVisible()) {
								redirectToMain(account.getName(), account.getDeveloperId());
								return;
							}
						}
						throw new UnsupportedOperationException("no account was selected");
					}
				}.execute();
			}
		});

		adapter = new AccountListAdapter();
		accountList.setLayoutManager(new LinearLayoutManager(this));
		accountList.setAdapter(adapter);
	}


	@Override
	protected void onResume() {
		super.onResume();

		boolean skipAutoLogin = Preferences.getSkipAutologin(this);
		DeveloperAccount selectedAccount = developerAccountManager.getSelectedDeveloperAccount();
		if (!manageAccountsMode & !skipAutoLogin & selectedAccount != null) {
			redirectToMain(selectedAccount.getName(), selectedAccount.getDeveloperId());
		} else {
			showAccountList();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login_menu, menu);
		return true;
	}

	/**
	 * Called if item in option menu is selected.
	 *
	 * @param item The chosen menu item
	 * @return boolean true/false
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.itemLoginmenuAdd:
				addNewGoogleAccount();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void showAccountList() {
		Account[] googleAccounts = accountManager.getAccountsByType(AutosyncHandler.ACCOUNT_TYPE_GOOGLE);
		List<DeveloperAccount> dbAccounts = developerAccountManager.getAllDeveloperAccounts();
		developerAccounts = new ArrayList<>();
		for (Account googleAccount : googleAccounts) {
			DeveloperAccount developerAccount = DeveloperAccount
					.createHidden(googleAccount.name);
			int idx = dbAccounts.indexOf(developerAccount);
			// use persistent object if exists
			if (idx != -1) {
				developerAccount = dbAccounts.get(idx);
			}
			developerAccounts.add(developerAccount);

			// Setup auto sync
			// only do this when managing accounts, otherwise sync may start
			// in the background before accounts are actually configured
			if (manageAccountsMode) {
				// Ensure it matches the sync period (excluding disabled state)
				syncHandler.setAutosyncPeriod(googleAccount.name,
						Preferences.getLastNonZeroAutosyncPeriod(this));
				// Now make it match the master sync (including disabled state)
				syncHandler.setAutosyncPeriod(googleAccount.name,
						Preferences.getAutosyncPeriod(this));
			}
		}

		adapter.setAccounts(developerAccounts);

		// Update ok button
		updateOkButton();
	}

	private void saveDeveloperAccounts() {
		for (DeveloperAccount account : developerAccounts) {
			if (account.isHidden()) {
				// They are removing the account from Andlytics, disable
				// syncing
				syncHandler.setAutosyncEnabled(account.getName(), false);
			} else {
				// Make it match the master sync period (including
				// disabled state)
				syncHandler.setAutosyncPeriod(account.getName(),
						Preferences.getAutosyncPeriod(LoginActivity.this));
			}
			developerAccountManager.addOrUpdateDeveloperAccount(account);
		}
	}

	private void updateOkButton() {
		okButton.setEnabled(isAtLeastOneAccountEnabled());
	}

	private boolean isAtLeastOneAccountEnabled() {
		for (DeveloperAccount acc : developerAccounts) {
			if (acc.isVisible()) {
				return true;
			}
		}
		return false;
	}

	private void addNewGoogleAccount() {
		AccountManagerCallback<Bundle> callback = new AccountManagerCallback<Bundle>() {
			public void run(AccountManagerFuture<Bundle> future) {
				try {
					Bundle bundle = future.getResult();
					bundle.keySet();
					Log.d(TAG, "account added: " + bundle);

					showAccountList();

				} catch (OperationCanceledException e) {
					Log.d(TAG, "addAccount was canceled");
				} catch (IOException e) {
					Log.d(TAG, "addAccount failed: " + e);
				} catch (AuthenticatorException e) {
					Log.d(TAG, "addAccount failed: " + e);
				}
				// gotAccount(false);
			}
		};

		// TODO request a weblogin: token here, so we have it cached?
		accountManager.addAccount(AutosyncHandler.ACCOUNT_TYPE_GOOGLE,
				LoginActivity.AUTH_TOKEN_TYPE_ANDROID_DEVELOPER, null, null /* options */,
				LoginActivity.this, callback, null /* handler */);
	}

	private void redirectToMain(String selectedAccount, String developerId) {
		Preferences.saveSkipAutoLogin(this, false);
		Intent intent = new Intent(LoginActivity.this, Main.class);
		intent.putExtra(BaseActivity.EXTRA_AUTH_ACCOUNT_NAME, selectedAccount);
		intent.putExtra(BaseActivity.EXTRA_DEVELOPER_ID, developerId);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		overridePendingTransition(R.anim.activity_fade_in, R.anim.activity_fade_out);
		finish();
	}

	private void setBusy(boolean isBusy) {
		busyView.setVisibility(isBusy ? View.VISIBLE : View.GONE);
	}

	private class AccountListAdapter extends RecyclerView.Adapter<AccountViewHolder> {

		private List<DeveloperAccount> accounts;

		@Override
		public AccountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			return new AccountViewHolder(inflater.inflate(R.layout.login_list_item, parent, false));
		}

		@Override
		public void onBindViewHolder(AccountViewHolder holder, int position) {
			holder.setAccount(accounts.get(position));
		}

		@Override
		public int getItemCount() {
			return accounts != null ? accounts.size() : 0;
		}

		public void setAccounts(List<DeveloperAccount> accounts) {
			this.accounts = accounts;
			notifyDataSetChanged();
		}
	}

	private class AccountViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

		private final TextView nameView;
		private final CheckBox checkBoxView;
		private DeveloperAccount account;

		public AccountViewHolder(View itemView) {
			super(itemView);
			nameView = (TextView) itemView.findViewById(R.id.login_list_item_text);
			checkBoxView = (CheckBox) itemView.findViewById(R.id.login_list_item_enabled);
			itemView.setOnClickListener(this);
		}

		public void setAccount(DeveloperAccount developerAccount) {
			this.account = developerAccount;
			nameView.setText(developerAccount.getName());
			checkBoxView.setChecked(!developerAccount.isHidden());
		}

		@Override
		public void onClick(View v) {
			checkBoxView.toggle();
			if (checkBoxView.isChecked())
				account.activate();
			else
				account.hide();

			updateOkButton();
		}
	}


}
