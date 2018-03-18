package trust.entity;

/**
 * Created by inst1 on 2017/8/6.
 * 用来存储统计后的结果
 */
public class Result {
    private int round;
    private float trust;

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public float getTrust() {
        return trust;
    }

    public void setTrust(float trust) {
        this.trust = trust;
    }
}
