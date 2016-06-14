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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eg.edu.guc.android.meetup.R;
import eg.edu.guc.android.meetup.activity.base.BasePrivateActivity;
import eg.edu.guc.android.meetup.model.Comment;
import eg.edu.guc.android.meetup.model.CommentUserPost;
import eg.edu.guc.android.meetup.model.Community;
import eg.edu.guc.android.meetup.model.Post;
import eg.edu.guc.android.meetup.model.User;
import eg.edu.guc.android.meetup.util.ApiRouter;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PostActivity extends BasePrivateActivity {
    private ArrayAdapter<CommentUserPost> adpCommentUserPost;
    private long postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        Bundle bundle = getIntent().getExtras();

        postId = bundle.getLong("ID");
        //postId = 27;

        /////////////////////////////////////
        ApiRouter.withToken(getCurrentUser().getToken()).getPost(postId,
                new Callback<Post>() {
                    @Override
                    public void success(Post current_post, Response rawResponse) {
                        TextView txtContent = (TextView) findViewById(R.id.txt_post_content);
                        txtContent.setText(current_post.getContent());

                        ApiRouter.withToken(getCurrentUser().getToken()).getUser(current_post.getUserSenderId(),
                                new Callback<User>() {
                                    @Override
                                    public void success(User user, Response rawResponse) {
                                        TextView txtName = (TextView) findViewById(R.id.txt_post_sender);
                                        txtName.setText(user.getfName() + " " + user.getlName());

                                        // Avatar
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
        ///////////////////////////////////////

        final EditText textComment = (EditText) findViewById(R.id.comment_field);
        Button btnPost = (Button) findViewById(R.id.btn_post);
        btnPost.setEnabled(true);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgress();

                String comment_field = textComment.getText().toString();

                ApiRouter.withToken(getCurrentUser().getToken()).createComment(postId, comment_field,
                        new Callback<List<CommentUserPost>>() {
                            @Override
                            public void success(List<CommentUserPost> current_comments, Response rawResponse) {
                                //stopProgress();
                                Toast.makeText(PostActivity.this, "Comment posted! ",
                                        Toast.LENGTH_LONG).show();

                                adpCommentUserPost.addAll(current_comments);
                                stopProgress();
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
        ListView lstComments = (ListView) findViewById(R.id.lst_comments);
        adpCommentUserPost = new ArrayAdapter<CommentUserPost>(this, 0) {
            private LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final CommentUserPost commentUserPost = getItem(position);

                final View view;
                if (convertView == null) {
                    view = mInflater.inflate(R.layout.view_comment, parent, false);
                } else {
                    view = convertView;
                }

                TextView txtComment = (TextView) view.findViewById(R.id.txt_comment);
                txtComment.setText(commentUserPost.getComment().getContent());

                TextView txtCommenter = (TextView) view.findViewById(R.id.txt_commenter);
                txtCommenter.setText(commentUserPost.getUser().getName());

                //ImageView imgImage = (ImageView) view.findViewById(R.id.img_community);
                //Picasso.with(AllCommunitiesActivity.this).load(product.getImageUrl()).into(imgImage);

                return view;
            }
        };

        lstComments.setAdapter(adpCommentUserPost);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshViews();
    }

    protected void refreshViews() {
        super.refreshViews();

        adpCommentUserPost.clear();

        startProgress();

        ApiRouter.withToken(getCurrentUser().getToken()).getPostComments(postId, new Callback<List<CommentUserPost>>() {
            @Override
            public void success(List<CommentUserPost> commentsUsersPosts, Response response) {
                adpCommentUserPost.addAll(commentsUsersPosts);
                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });
    }
}
