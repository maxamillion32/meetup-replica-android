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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.CommentUserPost;
import eg.edu.guc.android.meetup.model.Post;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FriendActivity extends BasePrivateActivity {
    private ArrayAdapter<Post> adpFriendPosts;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Bundle bundle = getIntent().getExtras();

        userId = bundle.getLong("ID");

        ApiRouter.withToken(getCurrentUser().getToken()).getUser(userId,
                new Callback<User>() {
                    @Override
                    public void success(User user, Response rawResponse) {
                        TextView txtName = (TextView) findViewById(R.id.txt_friend_name);
                        txtName.setText(user.getfName() + " " + user.getlName());

                        TextView txtDesc = (TextView) findViewById(R.id.txt_friend_bio);
                        txtDesc.setText(user.getLocation() + ", " + user.getGender() + ", " + user.getDay() + "/" +
                                user.getMonth() + "/" + user.getYear());

                        final User friend = user;
                        Button btnFriends = (Button) findViewById(R.id.btn_friend_friends);
                        btnFriends.setEnabled(true);
                        btnFriends.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                Intent i = new Intent(FriendActivity.this, FriendsActivity.class);

                                Bundle bundle = new Bundle();

                                bundle.putLong("ID", friend.getId());

                                i.putExtras(bundle);

                                startActivity(i);
                            }
                        });

                        Button btnComms = (Button) findViewById(R.id.btn_friend_communities);
                        btnComms.setEnabled(true);
                        btnComms.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                Intent i = new Intent(FriendActivity.this, UserCommunitiesActivity.class);

                                Bundle bundle = new Bundle();

                                bundle.putLong("ID", friend.getId());

                                i.putExtras(bundle);

                                startActivity(i);
                            }
                        });

                        Button btnEvents = (Button) findViewById(R.id.btn_friend_events);
                        btnEvents.setEnabled(true);
                        btnEvents.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                Intent i = new Intent(FriendActivity.this, UserEventsActivity.class);

                                Bundle bundle = new Bundle();

                                bundle.putLong("ID", friend.getId());

                                i.putExtras(bundle);

                                startActivity(i);
                            }
                        });

                        Button btnRemove = (Button) findViewById(R.id.btn_remove);
                        btnRemove.setEnabled(true);
                        btnRemove.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                ApiRouter.withToken(getCurrentUser().getToken()).removeFriend(userId, new Callback<Response>() {
                                    @Override
                                    public void success(Response response, Response rawResponse) {
                                        stopProgress();

                                        Intent i = new Intent(FriendActivity.this, LimitedActivity.class);

                                        Bundle bundle = new Bundle();

                                        bundle.putLong("ID", friend.getId());

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
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        displayError(e);
                    }
                });

        /////////////////////////////////////////////////////

        final EditText textPost = (EditText) findViewById(R.id.friend_post_field);
        Button btnPost = (Button) findViewById(R.id.btn_add_post);
        btnPost.setEnabled(true);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                String post_field = textPost.getText().toString();

                ApiRouter.withToken(getCurrentUser().getToken()).createUserPost(userId, post_field,
                        new Callback<Post>() {
                            @Override
                            public void success(Post post, Response rawResponse) {
                                //stopProgress();
                                Toast.makeText(FriendActivity.this, "Post added! ",
                                        Toast.LENGTH_LONG).show();

                                adpFriendPosts.addAll(post);
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
        final ListView lstPosts = (ListView) findViewById(R.id.lst_friend_posts);
        adpFriendPosts = new ArrayAdapter<Post>(this, 0) {
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

                        Intent i = new Intent(FriendActivity.this, PostActivity.class);

                        Bundle bundle = new Bundle();

                        bundle.putLong("ID", post.getId());

                        i.putExtras(bundle);

                        startActivity(i);
                    }

                });

                return view;
            }
        };

        lstPosts.setAdapter(adpFriendPosts);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshViews();
    }

    protected void refreshViews() {
        super.refreshViews();

        adpFriendPosts.clear();

        startProgress();

        ApiRouter.withToken(getCurrentUser().getToken()).getUserPosts(userId, new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                adpFriendPosts.addAll(posts);
                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
    }
}
