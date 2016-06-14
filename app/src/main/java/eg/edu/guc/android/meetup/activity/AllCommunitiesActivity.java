package eg.edu.guc.android.meetup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.Community;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AllCommunitiesActivity extends BasePrivateActivity {
    private ArrayAdapter<Community> adpCommunities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_communities);

        setUpViews();
    }

    private void setUpViews() {
        ListView lstCommunities = (ListView) findViewById(R.id.lst_communities);
        adpCommunities = new ArrayAdapter<Community>(this, 0) {
            private LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Community community = getItem(position);

                View view;
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.view_community, parent, false);
                } else {
                    view = convertView;
                }

                TextView txtName = (TextView) view.findViewById(R.id.txt_community);
                txtName.setText(community.getComName());

                //ImageView imgImage = (ImageView) view.findViewById(R.id.img_community);
                //Picasso.with(AllCommunitiesActivity.this).load(product.getImageUrl()).into(imgImage);

                Button btnShow = (Button) view.findViewById(R.id.btn_show);
                btnShow.setEnabled(true);
                btnShow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startProgress();

                        // WILL BE MODIFIED TO ACTUALLY GO TO COMMUNITY ACTIVITY AND NOT MERELY SHOW DESCRIPTION
                        ApiRouter.withToken(getCurrentUser().getToken()).getCommunity(community.getId(),
                                new Callback<Community>() {
                                    @Override
                                    public void success(Community current_community, Response rawResponse) {
//                                        Toast.makeText(AllCommunitiesActivity.this, "" + current_community.getDescription(),
//                                                Toast.LENGTH_LONG).show();

                                        stopProgress();

//                                        product.setStock(product.getStock() - 1);
                                        adpCommunities.notifyDataSetChanged();

                                        Intent i = new Intent(AllCommunitiesActivity.this, CommunityActivity.class);

                                        Bundle bundle = new Bundle();

                                        bundle.putLong("ID", current_community.getId());

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
        lstCommunities.setAdapter(adpCommunities);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshViews();
    }

    protected void refreshViews() {
        super.refreshViews();

        adpCommunities.clear();

        startProgress();

        ApiRouter.withToken(getCurrentUser().getToken()).getCommunities(new Callback<List<Community>>() {
            @Override
            public void success(List<Community> communities, Response response) {
                adpCommunities.addAll(communities);
                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
    }
}
