package cn.edu.nankai.cn;
class Dog{
	String name;
	String says;
	Dog(String name,String says){
		this.name=name;
		this.says=says;
	}
}
public class T3 {
public static void main(String []args){
	Dog spot=new Dog("spot","Ruff!");
	Dog scruffy=new Dog("scruffy", "Wurf!");
	System.out.println(spot.name+" "+spot.says);
	System.out.println(scruffy.name+" "+scruffy.says);
	
}
}
