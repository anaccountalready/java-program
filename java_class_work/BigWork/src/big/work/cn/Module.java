package big.work.cn;

public class Module {
	
private static Module instance;
private Module (){}
	public static Module getinstance() {
		if(instance==null)instance=new Module();
		return instance;
	}
	StringBuilder operand1=new StringBuilder();
	StringBuilder operand2=new StringBuilder();
	String operator=null;
	String prefix=null;//前缀负号
	boolean o1=false;
	boolean o2=false;
	//得到操作数
	public void seton(String s){
		if(operator==null)
		{operand1.append(s);o1=true;}
		else {operand2.append(s);o2=true;}
	}
	//得到前缀负号
	public void setpO(String s){
		prefix=s;
	}
	//得到能够显示的部分
	public String geton(){
		if(operator==null&&o1==true&&prefix==null)return operand1.toString();
		else if(prefix=="+/-"){//确定正负号是加在哪个操作数上面的
			if(o2==true)//说明是在操作数2上点的前缀负号
			{double j=0-Double.parseDouble(operand2.toString());
			operand2=new StringBuilder();
			operand2.append(j);
			prefix=null;
			return operand1.toString()+operator+"("+operand2.toString()+")";}
			else {//说明是在操作数1上点的前缀负号
				double j=0-Double.parseDouble(operand1.toString());
				operand1=new StringBuilder();
				operand1.append(j);
				prefix=null;
				return "("+operand1.toString()+")";
			}
			}
			else if(operator=="√x")return operand1.toString()+"^1/2";
			else if(operator=="1/x")return operand1.toString()+"^(-1)";
			else if(operator=="x!")return operand1.toString()+"!";
			else if(operator=="%")return operand1.toString()+"×0.01";
			else if(operator=="x^2")return operand1.toString()+"^2";
			else if((operator=="+"||operator=="-"||operator=="×"||operator=="÷")&&o2==false)return operand1.toString()+operator;
		else if(o2==true){return operand1.toString()+operator+operand2.toString();}
		else return"";
	}
	//得到操作符
	public void setO(String s){
		operator=s;
	}
	//回退一个的键CE，通过char[]与string的转换实现一个一个字符的回退
	public void back(){
		if(o2==true){
			int i=operand2.length();
			char []a=new char[i];
			char []b=new char[i-1];
			a=operand2.toString().toCharArray();
		   for(int j=0;j<i-1;j++){
			b[j]=a[j];
			}
			operand2=new StringBuilder();
			operand2.append(String.valueOf(b));
			int j=operand2.length();
			if(j==0)o2=false;
		}
		else if(operator!=null){operator=null;}
		else if(o1==true){
			int i=operand1.length();
			char []a=new char[i];
			char []b=new char[i-1];
			a=operand1.toString().toCharArray();
		   for(int j=0;j<i-1;j++){
			b[j]=a[j];
			}
			operand1=new StringBuilder();
			operand1.append(String.valueOf(b));
			int j=operand1.length();
			if(j==0)o1=false;
		}
		else;
	}
	//清除键C
	public void remove(){
		operand1=new StringBuilder();
		operand2=new StringBuilder();
		operator=null;
		o1=o2=false;
	}
	//算结果
	public String result(){
		String rst = null;
		switch (operator) {
		case "√x":
			rst= ""+Math.sqrt(Double.parseDouble(operand1.toString()));
			break;
		case"%":rst=""+Double.parseDouble(operand1.toString())*0.01;
		break;
		case"x^2":rst=""+Double.parseDouble(operand1.toString())*Double.parseDouble(operand1.toString());
		break;
		case"1/x":
			double i=1;
			rst=""+(i/Double.parseDouble(operand1.toString()));
			break;
		case "+":
		rst= ""+(Double.parseDouble(operand1.toString())+Double.parseDouble(operand2.toString()));
		break;
		case "-":
		rst= ""+(Double.parseDouble(operand1.toString())-Double.parseDouble(operand2.toString()));
		break;
		case "×":
		rst= ""+(Double.parseDouble(operand1.toString())*Double.parseDouble(operand2.toString()));
		break;
		case "÷":
		rst= ""+(Double.parseDouble(operand1.toString())/Double.parseDouble(operand2.toString()));
		break;
		case "x!":
			double k=Double.parseDouble(operand1.toString());
			double m=1;
			for(double p=k;p>1;p--){
				m*=p;
			}
			rst=""+m;
			break;
		default:
			rst="ERROR!";
			break;
		}
		remove();
		if(rst!="ERROR"&&rst!=null){operand1.append(rst);o1=true;}
		return rst;
	}
}
