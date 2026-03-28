package cn.nankai.edu.cn;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Stuscore {
private  int stuid;
private int couid;
private String couname;
private float score;
public Stuscore(int stuid, int couid, float score) {
	super();
	this.stuid = stuid;
	this.couid = couid;
	this.score = score;
	
}
public Stuscore(int stuid, int couid, float score,String couname) {
	super();
	this.stuid = stuid;
	this.couid = couid;
	this.score = score;
	this.couname=couname;
}
public Stuscore() {}
public int getStuid() {
	return stuid;
}
public void setStuid(int stuid) {
	this.stuid = stuid;
}
public int getCouid() {
	return couid;
}
public void setCouid(int couid) {
	this.couid = couid;
}
public float getScore() {
	return score;
}
public void setScore(float score) {
	this.score = score;
}
public String getCouname() {
	return couname;
}
public void setcouname(String couid) {
	couname=couid;
}
public void setCouname(int couid) {
	JDBCemo.getInstence();
	JDBCemo.select("name", "course", "id="+couid);
	ResultSet re2=JDBCemo.resulSet;
	
	try {
		while(re2.next()) {
			couname=re2.getString("name");
			System.out.println("couname="+couname);
		}
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
