package eg.edu.guc.android.meetup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.Community;
import eg.edu.guc.android.meetup.model.Post;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CreateCommunityActivity extends BasePrivateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);

        final EditText newComm = (EditText) findViewById(R.id.new_comm_name);

        final EditText newDesc = (EditText) findViewById(R.id.new_comm_desc);

        Button btnCreate = (Button) findViewById(R.id.btn_create_comm);
        btnCreate.setEnabled(true);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                String comm_name = newComm.getText().toString();

                String comm_desc = newDesc.getText().toString();

                ApiRouter.withToken(getCurrentUser().getToken()).createCommunity(comm_name, comm_desc,
                        new Callback<Community>() {
                            @Override
                            public void success(Community community, Response rawResponse) {
                                //stopProgress();
                                Toast.makeText(CreateCommunityActivity.this, "Community created!",
                                        Toast.LENGTH_LONG).show();

                                Intent i = new Intent(CreateCommunityActivity.this, CommunityActivity.class);

                                Bundle bundle = new Bundle();

                                bundle.putLong("ID", community.getId());

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

}
