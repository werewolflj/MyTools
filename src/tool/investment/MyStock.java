package tool.investment;

/**
 * Created by penet on 15/7/12.
 */
public class MyStock {

    //id varchar(20) primary key, zfprice double, zfdate date, cq double, todayprice double, discount double



    private String id;
    private float zfprice;
    private String zfdate;
    private float cq;
    private float todayprice;
    private float discount;
    private String ssdate;

    public MyStock() {
    }

    public String getSsdate() {
        return ssdate;
    }

    public void setSsdate(String ssdate) {
        this.ssdate = ssdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getZfprice() {
        return zfprice;
    }

    public void setZfprice(float zfprice) {
        this.zfprice = zfprice;
    }

    public String getZfdate() {
        return zfdate;
    }

    public void setZfdate(String zfdate) {
        this.zfdate = zfdate;
    }

    public float getCq() {
        return cq;
    }

    public void setCq(float cq) {
        this.cq = cq;
    }

    public float getTodayprice() {
        return todayprice;
    }

    public void setTodayprice(float todayprice) {
        this.todayprice = todayprice;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }
}
