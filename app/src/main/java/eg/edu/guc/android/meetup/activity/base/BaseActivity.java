package eg.edu.guc.android.meetup.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.FeedActivity;
import eg.edu.guc.android.meetup.activity.LoginActivity;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.model.UserOld;

public abstract class BaseActivity extends Activity {
	private static final String PREF_USER_ID = "PREF_USER_ID";
	private static final String PREF_USER_F_NAME = "PREF_USER_F_NAME";
	private static final String PREF_USER_L_NAME = "PREF_USER_L_NAME";
	private static final String PREF_USER_LOCATION = "PREF_USER_LOCATION";
	private static final String PREF_USER_GENDER = "PREF_USER_GENDER";
	private static final String PREF_USER_PROFILE_PICTURE = "PREF_USER_PROFILE_PICTURE";
	private static final String PREF_USER_PROVIDER = "PREF_USER_PROVIDER";
	private static final String PREF_USER_UID = "PREF_USER_UID";
	private static final String PREF_USER_DAY = "PREF_USER_DAY";
	private static final String PREF_USER_MONTH = "PREF_USER_MONTH";
	private static final String PREF_USER_YEAR = "PREF_USER_YEAR";
	private static final String PREF_USER_FB_TOKEN = "PREF_USER_FB_TOKEN";
	private static final String PREF_USER_EMAIL = "PREF_USER_EMAIL";
	private static final String PREF_USER_TOKEN = "PREF_USER_TOKEN";

	private User currentUser;
	private int inProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	}

	@Override
	protected void onResume() {
		super.onResume();

		invalidateOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();

		if (isLoggedIn()) {
			inflater.inflate(R.menu.menu_eshop, menu);
		}
		else {
			inflater.inflate(R.menu.menu_eshop_guest, menu);
		}

		return isRefreshable();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_refresh:
			refreshViews();
			return true;
		case R.id.action_logout:
			setCurrentUser(null);

//			Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
//			startActivity(intent);

			recreate();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	protected boolean isRefreshable() {
		return true;
	}

	protected void refreshViews() {
	}

	protected void startProgress() {
		setProgressBarIndeterminateVisibility(true);
		inProgress++;
	}

	protected void stopProgress() {
		if (--inProgress == 0) {
			setProgressBarIndeterminateVisibility(false);
		}
	}

	protected void displayError(Exception e) {
		setProgressBarIndeterminateVisibility(false);
		Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT)
				.show();
	}

	protected boolean isLoggedIn() {
		return getCurrentUser() != null;
	}

	protected User getCurrentUser() {
		if (currentUser == null) {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

			if (sharedPreferences.contains(PREF_USER_TOKEN)) {
				currentUser = new User();
				currentUser.setId(sharedPreferences.getLong(PREF_USER_ID, 0));
				currentUser.setfName(sharedPreferences.getString(PREF_USER_F_NAME, null));
				currentUser.setlName(sharedPreferences.getString(PREF_USER_L_NAME, null));
				currentUser.setLocation(sharedPreferences.getString(PREF_USER_LOCATION, null));
				currentUser.setGender(sharedPreferences.getString(PREF_USER_GENDER, null));
				currentUser.setProfilePicture(sharedPreferences.getString(PREF_USER_PROFILE_PICTURE, null));
				currentUser.setProvider(sharedPreferences.getString(PREF_USER_PROVIDER, null));
				currentUser.setUid(sharedPreferences.getLong(PREF_USER_UID, 0));
				currentUser.setDay(sharedPreferences.getInt(PREF_USER_DAY, 0));
				currentUser.setMonth(sharedPreferences.getInt(PREF_USER_MONTH, 0));
				currentUser.setYear(sharedPreferences.getInt(PREF_USER_YEAR, 0));
				currentUser.setFbToken(sharedPreferences.getString(PREF_USER_FB_TOKEN, null));
				currentUser.setEmail(sharedPreferences.getString(PREF_USER_EMAIL, null));
				currentUser.setToken(sharedPreferences.getString(PREF_USER_TOKEN, null));
			}
		}

		return currentUser;
	}

	protected void setCurrentUser(User user) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor p = sharedPreferences.edit();

		if ((currentUser = user) != null) {
			p.putLong(PREF_USER_ID, currentUser.getId());
			p.putString(PREF_USER_F_NAME, currentUser.getfName());
			p.putString(PREF_USER_L_NAME, currentUser.getlName());
			p.putString(PREF_USER_LOCATION, currentUser.getLocation());
			p.putString(PREF_USER_GENDER, currentUser.getGender());
			p.putString(PREF_USER_PROFILE_PICTURE, currentUser.getProfilePicture());
			p.putString(PREF_USER_PROVIDER, currentUser.getProvider());
			p.putLong(PREF_USER_UID, currentUser.getUid());
			p.putInt(PREF_USER_DAY, currentUser.getDay());
			p.putInt(PREF_USER_MONTH, currentUser.getMonth());
			p.putInt(PREF_USER_YEAR, currentUser.getYear());
			p.putString(PREF_USER_FB_TOKEN, currentUser.getFbToken());
			p.putString(PREF_USER_EMAIL, currentUser.getEmail());
			p.putString(PREF_USER_TOKEN, currentUser.getToken());
		}
		else {
			p.clear();
		}

		p.commit();
	}
}
