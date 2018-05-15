package trust.entity;

import java.util.Date;

/**
 * Created by inst1 on 2017/6/21.
 * 可信反馈记录
 */
public class Record {
    private Integer id;
    private Integer usr;
    private Integer se;
    private Date time;
    private double trust;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsr() {
        return usr;
    }

    public void setUsr(Integer usr) {
        this.usr = usr;
    }

    public Integer getSe() {
        return se;
    }

    public void setSe(Integer se) {
        this.se = se;
    }

    public double getTrust() {
        return trust;
    }

    public void setTrust(double trust) {
        this.trust = trust;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
