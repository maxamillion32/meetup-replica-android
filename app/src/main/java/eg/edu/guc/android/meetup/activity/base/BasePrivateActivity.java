package eg.edu.guc.android.meetup.activity.base;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.FacebookSdk;

import eg.edu.guc.android.meetup.activity.LoginActivity;

public abstract class BasePrivateActivity extends BaseActivity {
	private final static String EXTRA_REFERRER = "EXTRA_REFERRER";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!isLoggedIn()) {
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra(EXTRA_REFERRER, getClass().getName());
			startActivity(intent);

			finish();
		}
	}
}
