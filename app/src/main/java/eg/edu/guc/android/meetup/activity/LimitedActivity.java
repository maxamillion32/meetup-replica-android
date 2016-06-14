package eg.edu.guc.android.meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.Friendship;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LimitedActivity extends BasePrivateActivity {
    private long userId;
    private boolean found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limited);

        Bundle bundle = getIntent().getExtras();

        userId = bundle.getLong("ID");

        ApiRouter.withToken(getCurrentUser().getToken()).getUser(userId,
                new Callback<User>() {
                    @Override
                    public void success(User user, Response rawResponse) {
                        TextView txtName = (TextView) findViewById(R.id.txt_limited_name);
                        txtName.setText(user.getfName() + " " + user.getlName());

                        TextView txtDesc = (TextView) findViewById(R.id.txt_limited_bio);
                        txtDesc.setText(user.getLocation() + ", " + user.getGender() + ", " + user.getDay() + "/" +
                                user.getMonth() + "/" + user.getYear());
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        displayError(e);
                    }
                });

        ApiRouter.withToken(getCurrentUser().getToken()).getUserPendingRequests(getCurrentUser().getId(), new Callback<List<Friendship>>() {
            @Override
            public void success(List<Friendship> requests, Response response) {
                for (int i = 0; i < requests.size(); i++) {
                    User uA = requests.get(i).getUserOne();
                    User uB = requests.get(i).getUserTwo();

                    if (uA.getId() == userId || uB.getId() == userId) {
                        found = true;
                        Button btnAdd = (Button) findViewById(R.id.btn_add);
                        btnAdd.setEnabled(false);
                    }
                }

                if (!found) {
                    Button btnAdd = (Button) findViewById(R.id.btn_add);
                    btnAdd.setEnabled(true);
                    btnAdd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startProgress();

                            ApiRouter.withToken(getCurrentUser().getToken()).addFriend(userId, new Callback<Response>() {
                                @Override
                                public void success(Response response, Response rawResponse) {
                                    stopProgress();

                                    finish();
                                    startActivity(getIntent());
                                }

                                @Override
                                public void failure(RetrofitError e) {
                                    displayError(e);
                                }
                            });
                        }
                    });
                }

                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
    }

}




