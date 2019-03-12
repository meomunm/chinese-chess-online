package techkids.vn.chinesechessonline.models;

public class Room {
    private User user1;
    private User user2;
    private User userWinner;

    public Room() {
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public User getUserWinner() {
        return userWinner;
    }

    public void setUserWinner(User userWinner) {
        this.userWinner = userWinner;
    }
}
