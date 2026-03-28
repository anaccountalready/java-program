package cn.nankai.edu.cn;

public class Avg_score {
private int stu_id;
private float avg_score;
public int getStu_id() {
	return stu_id;
}
public void setStu_id(int stu_id) {
	this.stu_id = stu_id;
}
public float getAvg_score() {
	return avg_score;
}
public void setAvg_score(float avg_score) {
	this.avg_score = avg_score;
}
public Avg_score(int stu_id, float avg_score) {
	super();
	this.stu_id = stu_id;
	this.avg_score = avg_score;
}
public Avg_score() {
}

}
