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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.Community;
import eg.edu.guc.android.meetup.model.Event;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateEventActivity extends BasePrivateActivity {
    List<Community> myCommunities;
    long commId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        final EditText newEv = (EditText) findViewById(R.id.new_event_name);

        final EditText newDesc = (EditText) findViewById(R.id.new_event_desc);

        setUpViews();

        Button btnCreate = (Button) findViewById(R.id.btn_create_ev);
        btnCreate.setEnabled(true);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                String event_name = newEv.getText().toString();

                String event_desc = newDesc.getText().toString();

                Spinner spinner = (Spinner) findViewById(R.id.spinner_comm);
                String choice = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

                for (int i = 0; i < myCommunities.size(); i++) {
                    if (choice.equals(myCommunities.get(i).getComName())) {
                        commId = myCommunities.get(i).getId();
                    }
                    else if (choice.equals("Select a community")) {
                        commId = 0;
                    }
                }

                ApiRouter.withToken(getCurrentUser().getToken()).createEvent(event_name, event_desc, commId,
                        new Callback<Event>() {
                            @Override
                            public void success(Event event, Response rawResponse) {
                                //stopProgress();
                                Toast.makeText(CreateEventActivity.this, "Event created!",
                                        Toast.LENGTH_LONG).show();

                                Intent i = new Intent(CreateEventActivity.this, EventActivity.class);

                                Bundle bundle = new Bundle();

                                bundle.putLong("ID", event.getId());

                                i.putExtras(bundle);

                                startActivity(i);

                                stopProgress();
                            }

                            @Override
                            public void failure(RetrofitError e) {
                                displayError(e);
                            }
                        });
            }
        });
    }

    private void setUpViews() {
        Spinner lstCommunities = (Spinner) findViewById(R.id.spinner_comm);
        //adpCommunities = new ArrayAdapter<Community>(this, 0);

        final ArrayList<String> options=new ArrayList<String>();

        options.add("Select a community");

        ApiRouter.withToken(getCurrentUser().getToken()).getCommunities(new Callback<List<Community>>() {
            @Override
            public void success(List<Community> communities, Response response) {
                for (int i = 0; i < communities.size(); i++) {
                    options.add(communities.get(i).getComName());
                }

                myCommunities = communities;

                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        lstCommunities.setAdapter(adapter);
    }

}



