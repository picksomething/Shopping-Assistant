package cn.picksomething.shopassistant.model;

public class Good {

    private String jid;
    private String name;
    private String price;
    private String imageUrl;

    public Good() {
        super();
    }

    public Good(String goodId) {
        super();
        this.jid = goodId;
    }

    public Good(String goodId, String goodName, String goodPrice) {
        super();
        this.jid = goodId;
        this.name = goodName;
        this.price = goodPrice;
    }


    public String getJid() {
        return jid;
    }

    public void setJid(String id) {
        this.jid = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String toString() {
        return "goodID = " + jid + " goodName = " + name + "goodPrice = " + price + "imageUrl =" + imageUrl;
    }

}
