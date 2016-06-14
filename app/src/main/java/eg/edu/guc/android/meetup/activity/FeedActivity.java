package eg.edu.guc.android.meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;

public class FeedActivity extends BasePrivateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
    }

    public void onCommunitiesTap(View v) {
        Intent comm = new Intent(this, AllCommunitiesActivity.class);
        startActivity(comm);
    }

    public void onEventsTap(View v) {
        Intent events = new Intent(this, AllEventsActivity.class);
        startActivity(events);
    }

    public void onProfileTap(View v) {
        Intent profile = new Intent(this, ProfileActivity.class);
        startActivity(profile);
    }
}
