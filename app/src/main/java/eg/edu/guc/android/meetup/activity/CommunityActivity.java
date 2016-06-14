package eg.edu.guc.android.meetup.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.CommentUserPost;
import eg.edu.guc.android.meetup.model.Community;
import eg.edu.guc.android.meetup.model.Post;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CommunityActivity extends BasePrivateActivity {
    private ArrayAdapter<Post> adpCommunityPosts;
    private long communityId;
    private boolean member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        Bundle bundle = getIntent().getExtras();

        communityId = bundle.getLong("ID");

        ///////////////////////////////////////////////////////////////

        ApiRouter.withToken(getCurrentUser().getToken()).getCommunityMembers(communityId, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);

                    if (getCurrentUser().getId() == u.getId()) {
                        member = true;
                        Button btnJoin = (Button) findViewById(R.id.btn_join);
                        btnJoin.setEnabled(false);

                        Button btnLeave = (Button) findViewById(R.id.btn_leave);
                        btnLeave.setEnabled(true);
                        btnLeave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                ApiRouter.withToken(getCurrentUser().getToken()).leaveCommunity(communityId,
                                        new Callback<Response>() {
                                            @Override
                                            public void success(Response response, Response rawResponse) {
                                                //stopProgress();
                                                Toast.makeText(CommunityActivity.this, "Left!",
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
                }
                if (!member) {
                    Button btnLeave = (Button) findViewById(R.id.btn_leave);
                    btnLeave.setEnabled(false);

                    Button btnJoin = (Button) findViewById(R.id.btn_join);
                    btnJoin.setEnabled(true);
                    btnJoin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startProgress();

                            ApiRouter.withToken(getCurrentUser().getToken()).joinCommunity(communityId,
                                    new Callback<Response>() {
                                        @Override
                                        public void success(Response response, Response rawResponse) {
                                            //stopProgress();
                                            Toast.makeText(CommunityActivity.this, "Joined!",
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
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });

        /////////////////////////////////////////////

        ApiRouter.withToken(getCurrentUser().getToken()).getCommunity(communityId,
                new Callback<Community>() {
                    @Override
                    public void success(Community current_community, Response rawResponse) {
                        TextView txtName = (TextView) findViewById(R.id.txt_comm_name);
                        txtName.setText(current_community.getComName());

                        TextView txtDesc = (TextView) findViewById(R.id.txt_comm_desc);
                        txtDesc.setText(current_community.getDescription());

                        ApiRouter.withToken(getCurrentUser().getToken()).getUser(current_community.getUserId(),
                                new Callback<User>() {
                                    @Override
                                    public void success(User user, Response rawResponse) {
                                        TextView txtAdmin = (TextView) findViewById(R.id.txt_comm_admin);
                                        txtAdmin.setText("" + user.getfName() + " " + user.getlName());
                                    }

                                    @Override
                                    public void failure(RetrofitError e) {
                                        displayError(e);
                                    }
                                });

                        final Community communityMembers = current_community;
                        Button btnMembers = (Button) findViewById(R.id.btn_members);
                        btnMembers.setEnabled(true);
                        btnMembers.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                Intent i = new Intent(CommunityActivity.this, MembersActivity.class);

                                Bundle bundle = new Bundle();

                                bundle.putLong("ID", communityMembers.getId());

                                i.putExtras(bundle);

                                startActivity(i);
                            }
                        });

                        final Community communityEvents = current_community;
                        Button btnCommunityEvents = (Button) findViewById(R.id.btn_community_events);
                        btnCommunityEvents.setEnabled(true);
                        btnCommunityEvents.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                Intent i = new Intent(CommunityActivity.this, CommunityEventsActivity.class);

                                Bundle bundle = new Bundle();

                                bundle.putLong("ID", communityEvents.getId());

                                i.putExtras(bundle);

                                startActivity(i);
                            }
                        });


                    }

                    @Override
                    public void failure(RetrofitError e) {
                        displayError(e);
                    }
                });

        /////////////////////////////////////////////////////

        final EditText textPost = (EditText) findViewById(R.id.comm_post_field);
        Button btnPost = (Button) findViewById(R.id.btn_add_post);
        btnPost.setEnabled(true);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                String post_field = textPost.getText().toString();

                ApiRouter.withToken(getCurrentUser().getToken()).createCommunityPost(communityId, post_field,
                        new Callback<Post>() {
                            @Override
                            public void success(Post post, Response rawResponse) {
                                //stopProgress();
                                Toast.makeText(CommunityActivity.this, "Post added! ",
                                        Toast.LENGTH_LONG).show();

                                adpCommunityPosts.addAll(post);
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
        final ListView lstPosts = (ListView) findViewById(R.id.lst_comm_posts);
        adpCommunityPosts = new ArrayAdapter<Post>(this, 0) {
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

                //ImageView imgImage = (ImageView) view.findViewById(R.id.img_community);
                //Picasso.with(AllCommunitiesActivity.this).load(product.getImageUrl()).into(imgImage);

                // BTN SHOW
                lstPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        int itemPosition = position;

                        Post post = (Post) lstPosts.getItemAtPosition(position);

                        Intent i = new Intent(CommunityActivity.this, PostActivity.class);

                        Bundle bundle = new Bundle();

                        bundle.putLong("ID", post.getId());

                        i.putExtras(bundle);

                        startActivity(i);
                    }

                });

                return view;
            }
        };

        lstPosts.setAdapter(adpCommunityPosts);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshViews();
    }

    protected void refreshViews() {
        super.refreshViews();

        adpCommunityPosts.clear();

        startProgress();

        ApiRouter.withToken(getCurrentUser().getToken()).getCommunityPosts(communityId, new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                adpCommunityPosts.addAll(posts);
                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
    }
}