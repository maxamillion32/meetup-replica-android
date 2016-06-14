package eg.edu.guc.android.meetup.model;

/**
 * Created by mohamedabdel-azeem on 12/12/15.
 */
public class EventMember {
    private long id;
    private long eventId;
    private long userId;
    private boolean rsvp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public boolean isRsvp() {
        return rsvp;
    }

    public void setRsvp(boolean rsvp) {
        this.rsvp = rsvp;
    }
}
