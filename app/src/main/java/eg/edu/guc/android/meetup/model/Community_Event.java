package eg.edu.guc.android.meetup.model;

/**
 * Created by mohamedabdel-azeem on 12/6/15.
 */
public class Community_Event {
    private long id;
    private long communityId;
    private long eventId;

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

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }
}
