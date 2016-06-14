package eg.edu.guc.android.meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BaseActivity;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.model.UserOld;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends BaseActivity {
	private static final String EXTRA_REFERRER = "EXTRA_REFERRER";
	private CallbackManager mCallbackManager;
	private ProfileTracker mProfileTracker;
	private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
		@Override
		public void onSuccess(LoginResult loginResult) {
			final AccessToken accessToken = loginResult.getAccessToken();
			Log.d(getPackageName(), accessToken.getToken());

//			Profile profile = Profile.getCurrentProfile();
//
//			if (profile != null) {
//				Log.d(getPackageName(), profile.getFirstName());
//			}

			if(Profile.getCurrentProfile() == null) {
				mProfileTracker = new ProfileTracker() {
					@Override
					protected void onCurrentProfileChanged(Profile profile, Profile profile2) {
						//Log.v("facebook - profile", profile2.getFirstName());
						//HERE

						String uid = accessToken.getUserId();

						String fName = profile2.getFirstName();
						String lName = profile2.getLastName();
						//String profile_picture = profile2.getProfilePictureUri(64, 64).toString();

						ApiRouter.withoutToken().facebookLogin(uid, fName, lName, new Callback<User>() {
							@Override
							public void success(User user, Response response) {
								setCurrentUser(user);

								Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
								startActivity(intent);

								//if (user.getName() == null)
									//Log.d(getPackageName(), "NO USER");
								//Log.d(getPackageName(), "NO USER");
								//setCurrentUser(user);
							}

							@Override
							public void failure(RetrofitError e) {
								displayError(e);
							}
						});

						mProfileTracker.stopTracking();
					}
				};
				mProfileTracker.startTracking();
			}
			else {
				Profile profile = Profile.getCurrentProfile();
				//Log.v("facebook - profile", profile.getFirstName());
			}
		}

		@Override
		public void onCancel() {

		}

		@Override
		public void onError(FacebookException error) {

		}
	};

	private Class<?> referrer;
	private Button btnLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		FacebookSdk.sdkInitialize(getApplicationContext());
		mCallbackManager = CallbackManager.Factory.create();

		setContentView(R.layout.activity_login);

		Intent intent = getIntent();
		if (intent != null && intent.hasExtra(EXTRA_REFERRER)) {
			String referrerString = intent.getStringExtra(EXTRA_REFERRER);
			if (referrerString != null) {
				try {
					referrer = Class.forName(referrerString);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
		loginButton.setReadPermissions("public_profile");
		loginButton.setReadPermissions("email");
		loginButton.setReadPermissions("user_friends");
		loginButton.registerCallback(mCallbackManager, mCallback);

		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				EditText txtEmail = (EditText) findViewById(R.id.txt_email);
				String email = txtEmail.getText().toString();

				EditText txtPassword = (EditText) findViewById(R.id.txt_password);
				String password = txtPassword.getText().toString();

				Log.d(getPackageName(), "Login with '" + email + "' and '" + password + "'");

                ApiRouter.withoutToken().login(email, password, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        setCurrentUser(user);
						Log.d(getPackageName(), "" + user.getfName());

                        stopProgress();

                        if (referrer != null) {
                            Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                            startActivity(intent);

                            finish();
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        displayError(e);

                        btnLogin.setEnabled(true);
                    }
                });

				if (referrer != null) {
					Intent intent = new Intent(LoginActivity.this, referrer);
					startActivity(intent);

					finish();
				}
			}
		});
	}

	@Override
	protected boolean isRefreshable() {
		return false;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
