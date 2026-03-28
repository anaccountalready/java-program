package cn.edu.nankai.cn;
class Dogdog{
	void bark(String s){
		System.out.println("barking");
	}
	void bark(Integer s){
		System.out.println("howling");
	}
	void bark(){
		System.out.println("screaming");
	}

}
public class DogClass {
  public static void main(String []args){
	  Dogdog dog=new Dogdog();
	  dog.bark(1);
	  dog.bark("dog");
	  dog.bark();
  }
}
