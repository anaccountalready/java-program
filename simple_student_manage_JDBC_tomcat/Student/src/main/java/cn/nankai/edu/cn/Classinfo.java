package cn.nankai.edu.cn;

import java.sql.SQLException;

public class Classinfo {
private String name;
private int num;
private int teaid;
private String teaname;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getNum() {
	return num;
}
public void setNum(int num) {
	this.num = num;
}
public int getTeaid() {
	return teaid;
}
public void setTeaid(int teaid) {
	this.teaid = teaid;
}
public String getTeaname() {
	return teaname;
}
public void setTeaname(int teaid) {
	JDBCemo.getInstence();
	JDBCemo.select("name", "teacher", " id="+teaid);
	try {
		if(JDBCemo.resulSet.next()) {
			this.teaname = JDBCemo.resulSet.getString("name");
		}
	} catch (SQLException e) {
		e.printStackTrace();
	}
}

public void setTeanameFromResult(String teaname) {
	this.teaname = teaname;
}
public Classinfo() {
	super();
	
}
public Classinfo(String name, int num, int teaid, String teaname) {
	super();
	this.name = name;
	this.num = num;
	this.teaid = teaid;
	this.teaname = teaname;
}
public Classinfo(String name, int num, int teaid) {
	super();
	this.name = name;
	this.num = num;
	this.teaid = teaid;
}

}
