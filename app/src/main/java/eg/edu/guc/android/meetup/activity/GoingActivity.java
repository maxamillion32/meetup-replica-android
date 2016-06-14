package eg.edu.guc.android.meetup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GoingActivity extends BasePrivateActivity {
    private ArrayAdapter<User> adpGoing;
    private long eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_going);

        Bundle bundle = getIntent().getExtras();

        eventId = bundle.getLong("ID");

        setUpViews();
    }

    private void setUpViews() {
        ListView lstGoing = (ListView) findViewById(R.id.lst_going);
        adpGoing = new ArrayAdapter<User>(this, 0) {
            private LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final User user = getItem(position);

                View view;
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.view_event_going, parent, false);
                } else {
                    view = convertView;
                }

                TextView txtName = (TextView) view.findViewById(R.id.txt_going);
                txtName.setText(user.getfName() + " " + user.getlName());

                //ImageView imgImage = (ImageView) view.findViewById(R.id.img_community);
                //Picasso.with(AllCommunitiesActivity.this).load(product.getImageUrl()).into(imgImage);

                Button btnShow = (Button) view.findViewById(R.id.btn_show);
                btnShow.setEnabled(true);
                btnShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startProgress();

                        Intent i = new Intent(GoingActivity.this, ProfileActivity.class);

                        Bundle bundle = new Bundle();

                        bundle.putLong("ID", user.getId());

                        i.putExtras(bundle);

                        startActivity(i);
                    }
                });

                return view;
            }
        };
        lstGoing.setAdapter(adpGoing);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshViews();
    }

    protected void refreshViews() {
        super.refreshViews();

        adpGoing.clear();

        startProgress();

        ApiRouter.withToken(getCurrentUser().getToken()).getGoing(eventId, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                adpGoing.addAll(users);
                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
    }
}
