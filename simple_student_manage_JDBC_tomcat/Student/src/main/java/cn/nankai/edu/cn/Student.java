package cn.nankai.edu.cn;

import java.sql.Date;

import javax.swing.border.TitledBorder;

public class Student {
private int id;
private String name;
private String sex;
private java.sql.Date indate;
private String claname;
private String major;
private String teacher;
public Student() {
	super();
	this.id = 1;
	this.name = "name";
	this.sex = "sex";
	this.indate = Date.valueOf("2020-10-10");
	this.claname = "claname";
	this.major = "major";
	this.teacher="tea";}
public Student(int string, String name, String sex, Date indate, String claname, int i, int j) {
	super();
	this.id = string;
	this.name = name;
	this.sex = sex;
	this.indate = indate;
	this.claname = claname;
	this.major = String.valueOf(i);
	this.teacher =String.valueOf(j);
}
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
public String getSex() {
	return sex;
}
public void setSex(String sex) {
	this.sex = sex;
}
public java.sql.Date getIndate() {
	return indate;
}
public void setIndate(java.sql.Date indate) {
	this.indate = indate;
}
public String getClaname() {
	return claname;
}
public void setClaname(String claname) {
	this.claname = claname;
}
public String getMajor() {
	return major;
}
public void setMajor(String major) {
	this.major = major;
}
public String getTeacher() {
	return teacher;
}
public void setTeacher(String teacher) {
	this.teacher = teacher;
}



}
