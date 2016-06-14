package eg.edu.guc.android.meetup.activity;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.Friendship;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RequestsActivity extends BasePrivateActivity {
    private ArrayAdapter<Friendship> adpRequests;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        Bundle bundle = getIntent().getExtras();

        userId = bundle.getLong("ID");

        setUpViews();
    }

    private void setUpViews() {
        ListView lstRequests = (ListView) findViewById(R.id.lst_requests);
        adpRequests = new ArrayAdapter<Friendship>(this, 0) {
            private LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Friendship request = getItem(position);

                final View view;
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.view_request, parent, false);
                } else {
                    view = convertView;
                }

                ApiRouter.withToken(getCurrentUser().getToken()).getUser(request.getUserRequestSenderId(),
                        new Callback<User>() {
                            @Override
                            public void success(User user, Response rawResponse) {
                                TextView txtName = (TextView) view.findViewById(R.id.txt_request);
                                txtName.setText(user.getfName() + " " + user.getlName());

                                Button btnAccept = (Button) view.findViewById(R.id.btn_accept);
                                btnAccept.setEnabled(true);
                                // On click listener
                                btnAccept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startProgress();

                                        ApiRouter.withToken(getCurrentUser().getToken()).acceptRequest(userId, request.getId(),
                                                new Callback<Response>() {
                                                    @Override
                                                    public void success(Response response, Response rawResponse) {
                                                        //stopProgress();
                                                        Toast.makeText(RequestsActivity.this, "Accepted!",
                                                                Toast.LENGTH_LONG).show();
                                                        //refreshViews();
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

                                Button btnReject = (Button) view.findViewById(R.id.btn_reject);
                                btnReject.setEnabled(true);
                                // On click listener
                                btnReject.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startProgress();

                                        ApiRouter.withToken(getCurrentUser().getToken()).rejectRequest(userId, request.getId(),
                                                new Callback<Response>() {
                                                    @Override
                                                    public void success(Response response, Response rawResponse) {
                                                        //stopProgress();
                                                        Toast.makeText(RequestsActivity.this, "Rejected!",
                                                                Toast.LENGTH_LONG).show();
                                                        //refreshViews();
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

                            @Override
                            public void failure(RetrofitError e) {
                                displayError(e);
                            }
                        });



                //ImageView imgImage = (ImageView) view.findViewById(R.id.img_community);
                //Picasso.with(AllCommunitiesActivity.this).load(product.getImageUrl()).into(imgImage);

                return view;
            }
        };
        lstRequests.setAdapter(adpRequests);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshViews();
    }

    protected void refreshViews() {
        super.refreshViews();

        adpRequests.clear();

        startProgress();

        ApiRouter.withToken(getCurrentUser().getToken()).getUserRequests(userId, new Callback<List<Friendship>>() {
            @Override
            public void success(List<Friendship> requests, Response response) {
                adpRequests.addAll(requests);
                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
    }
}
