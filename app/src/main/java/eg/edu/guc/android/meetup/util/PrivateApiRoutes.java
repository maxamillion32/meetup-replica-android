package eg.edu.guc.android.meetup.util;


import java.util.List;

import eg.edu.guc.android.meetup.model.Comment;
import eg.edu.guc.android.meetup.model.CommentUserPost;
import eg.edu.guc.android.meetup.model.Community;
import eg.edu.guc.android.meetup.model.Event;
import eg.edu.guc.android.meetup.model.Friendship;
import eg.edu.guc.android.meetup.model.Post;
import eg.edu.guc.android.meetup.model.User;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.Path;

public interface PrivateApiRoutes {
    // =======================================================
    @PATCH("/products/{product_id}/buy")
    void patchProductBuy(@Path("product_id") long productId,
                         Callback<Response> callback);
    // =======================================================


    // INDEX
    @GET("/communities")
    void getCommunities(Callback<List<Community>> callback);

    @GET("/events")
    void getEvents(Callback<List<Event>> callback);


    // EVENTS
    @GET("/events/{event_id}")
    void getEvent(@Path("event_id") long eventId,
            Callback<Event> callback);

    @GET("/events/{event_id}/going")
    void getGoing(@Path("event_id") long eventId,
            Callback<List<User>> callback);

    @GET("/events/{event_id}/not_going")
    void getNotGoing(@Path("event_id") long eventId,
            Callback<List<User>> callback);

    @GET("/events/{event_id}/posts")
    void getEventPosts(@Path("event_id") long eventId,
            Callback<List<Post>> callback);

    @POST("/events/{event_id}/new_post")
    @FormUrlEncoded
    void createEventPost(@Path("event_id") long eventId,
                        @Field("post[content]") String content,
                        Callback<Post> callback);

    @PATCH("/events/{event_id}/going")
    void rsvpGoing(@Path("event_id") long eventId,
                       Callback<Response> callback); // Callback<Event>

    @PATCH("/events/{event_id}/not_going")
    void rsvpNotGoing(@Path("event_id") long eventId,
                   Callback<Response> callback); // Callback<Event>



    // USERS
    @GET("/users/{user_id}")
    void getUser(@Path("user_id") long userId,
            Callback<User> callback);

    @GET("/users/{user_id}/my_friends")
    void getFriends(@Path("user_id") long userId,
            Callback<List<User>> callback);

    @GET("/users/{user_id}/posts")
    void getUserPosts(@Path("user_id") long userId,
            Callback<List<Post>> callback);

    @GET("/users/{user_id}/my_comms")
    void getUserCommunities(@Path("user_id") long userId,
            Callback<List<Community>> callback);

    @GET("/users/{user_id}/my_events")
    void getUserEvents(@Path("user_id") long userId,
            Callback<List<Event>> callback);

    @GET("/users/{user_id}/requests")
    void getUserRequests(@Path("user_id") long userId,
            Callback<List<Friendship>> callback);

    @GET("/users/{user_id}/pending_requests")
    void getUserPendingRequests(@Path("user_id") long userId,
                         Callback<List<Friendship>> callback);

    @POST("/communities")
    @FormUrlEncoded
    void createCommunity(@Field("community[com_name]") String comName,
               @Field("community[description]") String description,
               Callback<Community> callback);

    @POST("/events")
    @FormUrlEncoded
    void createEvent(@Field("event[event_name]") String eventName,
                     @Field("event[description]") String description,
                     @Field("event[community_id") long community_id,
                         Callback<Event> callback);

    @POST("/users/{user_id}/new_post")
    @FormUrlEncoded
    void createUserPost(@Path("user_id") long userId,
                        @Field("post[content]") String content,
                        Callback<Post> callback);

    @PATCH("/users/{user_id}/remove_friend")
    void removeFriend(@Path("user_id") long userId,
                      Callback<Response> callback); // Callback<User>

    @PATCH("/users/{user_id}/add_friend")
    void addFriend(@Path("user_id") long userId,
                      Callback<Response> callback); // Callback<User>

    @PATCH("/users/{user_id}/accept_request/{request_id}")
    void acceptRequest(@Path("user_id") long userId,
                       @Path("request_id") long requestId,
                   Callback<Response> callback); // Callback<Friendship>

    @PATCH("/users/{user_id}/reject_request/{request_id}")
    void rejectRequest(@Path("user_id") long userId,
                       @Path("request_id") long requestId,
                       Callback<Response> callback); // Callback<Friendship>




    // COMMUNITIES
    @GET("/communities/{community_id}")
    void getCommunity(@Path("community_id") long communityId,
            Callback<Community> callback);

    @GET("/communities/{community_id}/posts")
    void getCommunityPosts(@Path("community_id") long communityId,
            Callback<List<Post>> callback);

    @GET("/communities/{community_id}/events")
    void getCommunityEvents(@Path("community_id") long communityId,
            Callback<List<Event>> callback);

    @GET("/communities/{community_id}/members")
    void getCommunityMembers(@Path("community_id") long communityId,
            Callback<List<User>> callback);

    @PATCH("/communities/{community_id}/join")
    void joinCommunity(@Path("community_id") long communityId,
                       Callback<Response> callback); // Callback<Community>

    @PATCH("/communities/{community_id}/leave")
    void leaveCommunity(@Path("community_id") long communityId,
                       Callback<Response> callback); // Callback<Community>

    @POST("/communities/{community_id}/new_post")
    @FormUrlEncoded
    void createCommunityPost(@Path("community_id") long communityId,
                        @Field("post[content]") String content,
                        Callback<Post> callback);


    // POSTS
    @GET("/posts/{post_id}")
    void getPost(@Path("post_id") long postId,
            Callback<Post> callback);

    @GET("/posts/{post_id}/comments")
    void getPostComments(@Path("post_id") long postId,
            Callback<List<CommentUserPost>> callback);

    @GET("/posts/{post_id}/sender")
    void getPostSender(@Path("post_id") long postId,
            Callback <User> callback);

    @POST("/posts/{post_id}")
    @FormUrlEncoded
    void createComment(@Path("post_id") long postId,
                       @Field("comment[content]") String content,
                         Callback<List<CommentUserPost>> callback);


    // COMMENTS
    @GET("/posts/{id}/comments/{comment_id}")
    void getComment(@Path("id") long postId,
                    @Path("comment_id") long commentId,
                    Callback <Comment> callback);
}
