package techkids.vn.chinesechessonline.models;

public class Game {
    private String user1;
    private String user2;
    private String winner;

    public Game(){

    }

    public Game(String user1, String user2, String winner) {
        this.user1 = user1;
        this.user2 = user2;
        this.winner = winner;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }
}
