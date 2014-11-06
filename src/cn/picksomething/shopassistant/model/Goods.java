package cn.picksomething.shopassistant.model;

public class Goods {

	private int goodId;
	private String goodName;
	private String goodPrice;

	public Goods() {
		// TODO Auto-generated constructor stub
		super();
	}

	public Goods(int goodId, String goodName, String goodPrice) {
		super();
		this.goodId = goodId;
		this.goodName = goodName;
		this.goodPrice = goodPrice;
	}

	public void setGoodId(int goodId) {
		this.goodId = goodId;
	}

	public int getGoodId() {
		return goodId;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodPrice(String goodPrice) {
		this.goodPrice = goodPrice;
	}

	public String getGoodPrice() {
		return goodPrice;
	}

	public String toString() {
		return "goodID = " + goodId + " goodName = " + goodName + "goodPrice = " + goodPrice;
	}

}
