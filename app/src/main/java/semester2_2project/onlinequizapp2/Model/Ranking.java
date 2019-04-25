package semester2_2project.onlinequizapp2.Model;

/**
 * Created by dipta10 on 13-Feb-18.
 */

public class Ranking {
    private String userName;
    private long score;

    public Ranking() {
    }

    public Ranking(String userName, long score) {
        this.userName = userName;
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
