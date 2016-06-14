package eg.edu.guc.android.meetup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.CommentUserPost;
import eg.edu.guc.android.meetup.model.Community;
import eg.edu.guc.android.meetup.model.Event;
import eg.edu.guc.android.meetup.model.Post;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfileActivity extends BasePrivateActivity {
    private ArrayAdapter<Post> adpUserPosts;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();

            userId = bundle.getLong("ID");

            ApiRouter.withToken(getCurrentUser().getToken()).getFriends(getCurrentUser().getId(), new Callback<List<User>>() {
                @Override
                public void success(List<User> users, Response response) {
                    for (int i = 0; i < users.size(); i++) {
                        User u = users.get(i);
                        if (u.getId() == userId) {
                            Intent intent = new Intent(ProfileActivity.this, FriendActivity.class);

                            Bundle bundle = new Bundle();

                            bundle.putLong("ID", userId);

                            intent.putExtras(bundle);

                            startActivity(intent);
                        }
                    }
                    stopProgress();
                }

                @Override
                public void failure(RetrofitError e) {
                    displayError(e);
                }
            });

            if (userId != getCurrentUser().getId()) {
                Intent intent = new Intent(ProfileActivity.this, LimitedActivity.class);

                Bundle newBundle = new Bundle();

                newBundle.putLong("ID", userId);

                intent.putExtras(newBundle);

                startActivity(intent);
            }
        }

        final User user = getCurrentUser();

        //ImageView imgImage = (ImageView) findViewById(R.id.img_user);
        //Picasso.with(ProfileActivity.this).load(user.getProfilePicture()).into(imgImage);

        TextView txtName = (TextView) findViewById(R.id.txt_user_name);
        txtName.setText(user.getfName() + " " + user.getlName());

        TextView txtDesc = (TextView) findViewById(R.id.txt_user_bio);
        txtDesc.setText(user.getLocation() + ", " + user.getGender() + ", " + user.getDay() + "/" +
                user.getMonth() + "/" + user.getYear());

        Button btnRequests = (Button) findViewById(R.id.btn_requests);
        btnRequests.setEnabled(true);
        btnRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                Intent i = new Intent(ProfileActivity.this, RequestsActivity.class);

                Bundle bundle = new Bundle();

                bundle.putLong("ID", user.getId());

                i.putExtras(bundle);

                startActivity(i);
            }
        });

        Button btnFriends = (Button) findViewById(R.id.btn_friends);
        btnFriends.setEnabled(true);
        btnFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                Intent i = new Intent(ProfileActivity.this, FriendsActivity.class);

                Bundle bundle = new Bundle();

                bundle.putLong("ID", user.getId());

                i.putExtras(bundle);

                startActivity(i);
            }
        });

        Button btnComms = (Button) findViewById(R.id.btn_user_communities);
        btnComms.setEnabled(true);
        btnComms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                Intent i = new Intent(ProfileActivity.this, UserCommunitiesActivity.class);

                Bundle bundle = new Bundle();

                bundle.putLong("ID", user.getId());

                i.putExtras(bundle);

                startActivity(i);
            }
        });

        Button btnEvents = (Button) findViewById(R.id.btn_user_events);
        btnEvents.setEnabled(true);
        btnEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                Intent i = new Intent(ProfileActivity.this, UserEventsActivity.class);

                Bundle bundle = new Bundle();

                bundle.putLong("ID", user.getId());

                i.putExtras(bundle);

                startActivity(i);
            }
        });

        Button btnCreateComm = (Button) findViewById(R.id.btn_create_community);
        btnCreateComm.setEnabled(true);
        btnCreateComm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                Intent i = new Intent(ProfileActivity.this, CreateCommunityActivity.class);

                startActivity(i);
            }
        });

        Button btnCreateEv = (Button) findViewById(R.id.btn_create_event);
        btnCreateEv.setEnabled(true);
        btnCreateEv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                Intent i = new Intent(ProfileActivity.this, CreateEventActivity.class);

                startActivity(i);
            }
        });

        /////////////////////////////////////////////////////

        final EditText textPost = (EditText) findViewById(R.id.user_post_field);
        Button btnPost = (Button) findViewById(R.id.btn_add_post);
        btnPost.setEnabled(true);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                String post_field = textPost.getText().toString();

                ApiRouter.withToken(getCurrentUser().getToken()).createUserPost(getCurrentUser().getId(), post_field,
                        new Callback<Post>() {
                            @Override
                            public void success(Post post, Response rawResponse) {
                                //stopProgress();
                                Toast.makeText(ProfileActivity.this, "Post added! ",
                                        Toast.LENGTH_LONG).show();

                                adpUserPosts.addAll(post);
                                stopProgress();
                                refreshViews();
                                //adpCommentUserPost.notifyDataSetChanged(); kda kda da called ma3 addAll
                            }

                            @Override
                            public void failure(RetrofitError e) {
                                displayError(e);
                            }
                        });
            }
        });

        setUpViews();
    }

    private void setUpViews() {
        final ListView lstPosts = (ListView) findViewById(R.id.lst_user_posts);
        adpUserPosts = new ArrayAdapter<Post>(this, 0) {
            private LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Post post = getItem(position);

                final View view;
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.view_post, parent, false);
                } else {
                    view = convertView;
                }

                TextView txtComment = (TextView) view.findViewById(R.id.txt_post);
                txtComment.setText(post.getContent());

                TextView txtCommenter = (TextView) view.findViewById(R.id.txt_poster);
                txtCommenter.setText(post.getUser().getName());

                // BTN SHOW
                lstPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int itemPosition = position;

                        Post post = (Post) lstPosts.getItemAtPosition(position);

                        Intent i = new Intent(ProfileActivity.this, PostActivity.class);

                        Bundle bundle = new Bundle();

                        bundle.putLong("ID", post.getId());

                        i.putExtras(bundle);

                        startActivity(i);
                    }

                });

                return view;
            }
        };

        lstPosts.setAdapter(adpUserPosts);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshViews();
    }

    protected void refreshViews() {
        super.refreshViews();

        adpUserPosts.clear();

        startProgress();

        ApiRouter.withToken(getCurrentUser().getToken()).getUserPosts(getCurrentUser().getId(), new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                adpUserPosts.addAll(posts);
                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
    }
}
