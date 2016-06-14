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
import eg.edu.guc.android.meetup.model.Community;
import eg.edu.guc.android.meetup.model.Event;
import eg.edu.guc.android.meetup.model.Post;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventActivity extends BasePrivateActivity {
    private ArrayAdapter<Post> adpEventPosts;
    private long eventId;
    private boolean going;
    private boolean found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Bundle bundle = getIntent().getExtras();

        eventId = bundle.getLong("ID");

        ////////////////////////////////////////////////////////

        ApiRouter.withToken(getCurrentUser().getToken()).getGoing(eventId, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);

                    if (getCurrentUser().getId() == u.getId()) {
                        going = true;
                        found = true;

                        Button btnGoing = (Button) findViewById(R.id.btn_rsvp_going);
                        btnGoing.setEnabled(false);

                        Button btnNotGoing = (Button) findViewById(R.id.btn_rsvp_not_going);
                        btnNotGoing.setEnabled(true);
                        btnNotGoing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                ApiRouter.withToken(getCurrentUser().getToken()).rsvpNotGoing(eventId,
                                        new Callback<Response>() {
                                            @Override
                                            public void success(Response response, Response rawResponse) {
                                                //stopProgress();
                                                Toast.makeText(EventActivity.this, "RSVPed: Not Going!",
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

            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });

        ApiRouter.withToken(getCurrentUser().getToken()).getNotGoing(eventId, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);

                    if (getCurrentUser().getId() == u.getId()) {
                        going = false;
                        found = true;
                        Button btnNotGoing = (Button) findViewById(R.id.btn_rsvp_not_going);
                        btnNotGoing.setEnabled(false);

                        Button btnGoing = (Button) findViewById(R.id.btn_rsvp_going);
                        btnGoing.setEnabled(true);
                        btnGoing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                ApiRouter.withToken(getCurrentUser().getToken()).rsvpGoing(eventId,
                                        new Callback<Response>() {
                                            @Override
                                            public void success(Response response, Response rawResponse) {
                                                //stopProgress();
                                                Toast.makeText(EventActivity.this, "RSVPed: Going!",
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

                if (!found) {
                    Button btnGoing = (Button) findViewById(R.id.btn_rsvp_going);
                    btnGoing.setEnabled(true);
                    btnGoing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startProgress();

                            ApiRouter.withToken(getCurrentUser().getToken()).rsvpGoing(eventId,
                                    new Callback<Response>() {
                                        @Override
                                        public void success(Response response, Response rawResponse) {
                                            //stopProgress();
                                            Toast.makeText(EventActivity.this, "RSVPed: Going!",
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

                    Button btnNotGoing = (Button) findViewById(R.id.btn_rsvp_not_going);
                    btnNotGoing.setEnabled(true);
                    btnNotGoing.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startProgress();

                            ApiRouter.withToken(getCurrentUser().getToken()).rsvpNotGoing(eventId,
                                    new Callback<Response>() {
                                        @Override
                                        public void success(Response response, Response rawResponse) {
                                            //stopProgress();
                                            Toast.makeText(EventActivity.this, "RSVPed: Not Going!",
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

        ////////////////////////////////////////////////////////

        ApiRouter.withToken(getCurrentUser().getToken()).getEvent(eventId,
                new Callback<Event>() {
                    @Override
                    public void success(Event current_event, Response rawResponse) {
                        TextView txtName = (TextView) findViewById(R.id.txt_event_name);
                        txtName.setText(current_event.getEventName());

                        TextView txtDesc = (TextView) findViewById(R.id.txt_event_desc);
                        txtDesc.setText(current_event.getDescription());

                        final Event eventGoing = current_event;
                        Button btnGoing = (Button) findViewById(R.id.btn_view_going);
                        btnGoing.setEnabled(true);
                        btnGoing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                Intent i = new Intent(EventActivity.this, GoingActivity.class);

                                Bundle bundle = new Bundle();

                                bundle.putLong("ID", eventGoing.getId());

                                i.putExtras(bundle);

                                startActivity(i);
                            }
                        });

                        final Event eventNotGoing = current_event;
                        Button btnNotGoing = (Button) findViewById(R.id.btn_view_not_going);
                        btnNotGoing.setEnabled(true);
                        btnNotGoing.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startProgress();

                                Intent i = new Intent(EventActivity.this, NotGoingActivity.class);

                                Bundle bundle = new Bundle();

                                bundle.putLong("ID", eventGoing.getId());

                                i.putExtras(bundle);

                                startActivity(i);
                            }
                        });

                        ApiRouter.withToken(getCurrentUser().getToken()).getUser(current_event.getUserId(),
                                new Callback<User>() {
                                    @Override
                                    public void success(User user, Response rawResponse) {
                                        TextView txtAdmin = (TextView) findViewById(R.id.txt_event_host);
                                        txtAdmin.setText("" + user.getfName() + " " + user.getlName());
                                    }

                                    @Override
                                    public void failure(RetrofitError e) {
                                        displayError(e);
                                    }
                                });

                        ApiRouter.withToken(getCurrentUser().getToken()).getCommunity(current_event.getCommunityId(),
                                new Callback<Community>() {
                                    @Override
                                    public void success(Community community, Response rawResponse) {
                                        TextView txtAdmin = (TextView) findViewById(R.id.txt_event_comm);
                                        txtAdmin.setText("" + community.getComName());
                                    }

                                    @Override
                                    public void failure(RetrofitError e) {
                                        displayError(e);
                                    }
                                });
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        displayError(e);
                    }
                });

        /////////////////////////////////////////////////////

        final EditText textPost = (EditText) findViewById(R.id.event_post_field);
        Button btnPost = (Button) findViewById(R.id.btn_add_post);
        btnPost.setEnabled(true);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                String post_field = textPost.getText().toString();

                ApiRouter.withToken(getCurrentUser().getToken()).createEventPost(eventId, post_field,
                        new Callback<Post>() {
                            @Override
                            public void success(Post post, Response rawResponse) {
                                //stopProgress();
                                Toast.makeText(EventActivity.this, "Post added! ",
                                        Toast.LENGTH_LONG).show();

                                adpEventPosts.addAll(post);
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
        final ListView lstPosts = (ListView) findViewById(R.id.lst_event_posts);
        adpEventPosts = new ArrayAdapter<Post>(this, 0) {
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

                        Intent i = new Intent(EventActivity.this, PostActivity.class);

                        Bundle bundle = new Bundle();

                        bundle.putLong("ID", post.getId());

                        i.putExtras(bundle);

                        startActivity(i);
                    }

                });

                return view;
            }
        };

        lstPosts.setAdapter(adpEventPosts);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshViews();
    }

    protected void refreshViews() {
        super.refreshViews();

        adpEventPosts.clear();

        startProgress();

        ApiRouter.withToken(getCurrentUser().getToken()).getEventPosts(eventId, new Callback<List<Post>>() {
            @Override
            public void success(List<Post> posts, Response response) {
                adpEventPosts.addAll(posts);
                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
    }
}



