package big.work.cn;

public class Userbook {
 User data[]=new User[1000];
 int usernum=0;
 Userbook(){
	 for(User m:data){
		 if(m!=null)usernum++;
	 }
 }
public void adduser(User user){
	if(data[user.id]==null)data[user.id]=user;
	else {data[user.id]=user;}
	usernum++;
}
public void removeuser(User user){
	if(data[user.id]!=null){ data[user.id]=null;
	usernum--;}
	else;
}
public User finduser(int id){if(data[id]!=null)return data[id];
else {System.out.println("没有id为"+id+"的用户");return null;}}
public void list(){
	System.out.println("当前共有用户"+usernum+"个");
	if(usernum!=0){
	System.out.println("用户名单如下:");
	for(User m:data){
		if(m!=null)System.out.println(m);
	}
	}
	else System.out.println("店里已经没有任何用户了");
}

}
