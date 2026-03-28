package cn.edu.nankai.cn;
import java.util.*;
public class T {
	public static void  main(String []args){
		for(int i=10;i<100;i++){
			for(int j=i+1;j<100;j++){//为了避免重复
				int m=i*j;
				if(m>9999||m<1000)continue;
				else{
					String[] str1=String.valueOf(m).split("");//将表示保存指定的int对象的String对象拆分后存在字符串数组中
					String[] str2=(String.valueOf(i)+String.valueOf(j)).split("");
					Arrays.sort(str1);//将数组内元素排序，便于比较
					Arrays.sort(str2);
					if(Arrays.equals(str1,str2))System.out.println(i+"*"+j+"="+m);
				}
			}
		}
	}
}