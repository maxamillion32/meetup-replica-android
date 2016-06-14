package eg.edu.guc.android.meetup.model;

/**
 * Created by mohamedabdel-azeem on 12/6/15.
 */
public class Member {
    private long id;
    private long communityId;
    private long userId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(long communityId) {
        this.communityId = communityId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
