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
import eg.edu.guc.android.meetup.model.Community;
import eg.edu.guc.android.meetup.model.Event;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserEventsActivity extends BasePrivateActivity {
    private ArrayAdapter<Event> adpUserEvents;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);

        Bundle bundle = getIntent().getExtras();

        userId = bundle.getLong("ID");

        setUpViews();
    }

    private void setUpViews() {
        ListView lstEvents = (ListView) findViewById(R.id.lst_user_events);
        adpUserEvents = new ArrayAdapter<Event>(this, 0) {
            private LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Event event = getItem(position);

                View view;
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.view_user_event, parent, false);
                } else {
                    view = convertView;
                }

                TextView txtName = (TextView) view.findViewById(R.id.txt_user_event);
                txtName.setText(event.getEventName());

                //ImageView imgImage = (ImageView) view.findViewById(R.id.img_community);
                //Picasso.with(AllCommunitiesActivity.this).load(product.getImageUrl()).into(imgImage);

                Button btnShow = (Button) view.findViewById(R.id.btn_show);
                btnShow.setEnabled(true);
                btnShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startProgress();

                        ApiRouter.withToken(getCurrentUser().getToken()).getEvent(event.getId(),
                                new Callback<Event>() {
                                    @Override
                                    public void success(Event current_event, Response rawResponse) {
//                                        Toast.makeText(AllCommunitiesActivity.this, "" + current_community.getDescription(),
//                                                Toast.LENGTH_LONG).show();

                                        stopProgress();

//                                        product.setStock(product.getStock() - 1);
                                        adpUserEvents.notifyDataSetChanged();

                                        Intent i = new Intent(UserEventsActivity.this, EventActivity.class);

                                        Bundle bundle = new Bundle();

                                        bundle.putLong("ID", current_event.getId());

                                        i.putExtras(bundle);

                                        startActivity(i);
                                    }

                                    @Override
                                    public void failure(RetrofitError e) {
                                        displayError(e);
                                    }
                                });
                    }
                });

                return view;
            }
        };
        lstEvents.setAdapter(adpUserEvents);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshViews();
    }

    protected void refreshViews() {
        super.refreshViews();

        adpUserEvents.clear();

        startProgress();

        ApiRouter.withToken(getCurrentUser().getToken()).getUserEvents(userId, new Callback<List<Event>>() {
            @Override
            public void success(List<Event> events, Response response) {
                adpUserEvents.addAll(events);
                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
    }
}
