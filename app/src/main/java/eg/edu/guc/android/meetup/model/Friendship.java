package eg.edu.guc.android.meetup.model;

/**
 * Created by mohamedabdel-azeem on 12/6/15.
 */
public class Friendship {
    private long id;
    private long userRequestSenderId;
    private long userRequestReceiverId;
    private boolean accept;
    private User userOne;
    private User userTwo;

    public User getUserOne() {
        return userOne;
    }

    public void setUserOne(User userOne) {
        this.userOne = userOne;
    }

    public User getUserTwo() {
        return userTwo;
    }

    public void setUserTwo(User userTwo) {
        this.userTwo = userTwo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserRequestSenderId() {
        return userRequestSenderId;
    }

    public void setUserRequestSenderId(long userRequestSenderId) {
        this.userRequestSenderId = userRequestSenderId;
    }

    public long getUserRequestReceiverId() {
        return userRequestReceiverId;
    }

    public void setUserRequestReceiverId(long userRequestReceiverId) {
        this.userRequestReceiverId = userRequestReceiverId;
    }

    public boolean isAccept() {
        return accept;
    }

    public void setAccept(boolean accept) {
        this.accept = accept;
    }
}
