package big.work.cn;

public class Control {
	private static Control instance;
	private Control (){}
 public static Control getinstance() {
		if(instance==null)instance=new Control();
		return instance;
	}
public void press(String s) {
	// TODO Auto-generated method stub
	if(s=="+"||s=="-"||s=="¡Á"||s=="¡Â"||s=="1/x"||s=="x^2"||s=="¡Ìx"||s=="x!"||s=="%"){
		Module.getinstance().setO(s);
		 MainC.getinstance().show(Module.getinstance().geton());
		}
	else if(s=="="){
		 MainC.getinstance().show(Module.getinstance().result());
	}
	else if(s=="C"){MainC.getinstance().show("");Module.getinstance().remove() ;}
	else if(s=="CE"){Module.getinstance().back() ;MainC.getinstance().show(Module.getinstance().geton());}
	else if(s=="+/-"){Module.getinstance().setpO(s);MainC.getinstance().show(Module.getinstance().geton());}
	else {
			Module.getinstance().seton(s);
			 MainC.getinstance().show(Module.getinstance().geton());

			}
	
}
}
