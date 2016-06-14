package eg.edu.guc.android.meetup.model;

/**
 * Created by mohamedabdel-azeem on 12/6/15.
 */
public class Post {
    private long id;
    private long userSenderId;
    private long userReceiverId;
    private String content;
    private long communityId;
    private long eventId;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserSenderId() {
        return userSenderId;
    }

    public void setUserSenderId(long userSenderId) {
        this.userSenderId = userSenderId;
    }

    public long getUserReceiverId() {
        return userReceiverId;
    }

    public void setUserReceiverId(long userReceiverId) {
        this.userReceiverId = userReceiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(long communityId) {
        this.communityId = communityId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}
