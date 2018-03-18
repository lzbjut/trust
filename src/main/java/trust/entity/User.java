package trust.entity;

/**
 * Created by inst1 on 2017/6/21.
 * 用户
 */
public class User {
    private int id;
    private String name;
    private int count;
    private float usability;
    private float reliability;
    private float responseTime;
    private float throughPut;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getUsability() {
        return usability;
    }

    public void setUsability(float usability) {
        this.usability = usability;
    }

    public float getReliability() {
        return reliability;
    }

    public void setReliability(float reliability) {
        this.reliability = reliability;
    }

    public float getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(float responseTime) {
        this.responseTime = responseTime;
    }

    public float getThroughPut() {
        return throughPut;
    }

    public void setThroughPut(float throughPut) {
        this.throughPut = throughPut;
    }
}
