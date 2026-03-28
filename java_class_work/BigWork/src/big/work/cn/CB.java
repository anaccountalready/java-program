package big.work.cn;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicArrowButton;

public class CB extends JPanel{
JComboBox cb1,cb2;//cb3;
JTextField t1=new JTextField("Hello!");
JPanel pane=new JPanel();
JPanel p0=new JPanel();
JPanel p1=new JPanel();
JPanel p2=new JPanel();
JTextField cb3=new JTextField("30");
public CB(){
	Color[] colors={Color.green,Color.red,Color.gray};
	setLayout(new GridLayout(2,2));
	String []str={"10","20","30","40"};
	pane.setLayout(new GridLayout(1,2));
	JCheckBox ccb1=new JCheckBox("填充");
	ccb1.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
	});
	t1.setPreferredSize(new Dimension(200,25));
	cb1=new JComboBox(colors);
	cb2=new JComboBox(colors);
	ComboBoxRenderer renderer=new ComboBoxRenderer();
	renderer.setPreferredSize(new Dimension(200,20));
	cb1.setSelectedIndex(0);
	cb2.setSelectedIndex(1);
	Shape.shape.setBackcColor(Color.red);
	Shape.shape.setShapecolor(Color.green);
	cb1.setRenderer(renderer);
	cb1.setMaximumRowCount(2);
	cb2.setRenderer(renderer);
	cb2.setMaximumRowCount(2);
	JLabel l1=new JLabel("图形颜色");
	add(p0);
	p0.add(l1);
	p0.add(cb1);
	JLabel l2=new JLabel("文本");
	add(p1);
	p1.add(l2);
	p1.add(t1);
	JLabel l3=new JLabel("背景颜色");
	add(p2);
	p2.add(l3);
	p2.add(cb2);
	JLabel l4=new JLabel("图形尺寸");
	/*cb3=new JComboBox(str);
	cb3.setSelectedItem(1);*/
	cb3.setPreferredSize(new Dimension(200,20));
    cb3.setEditable(true);
	pane.add(l4);
	pane.add(cb3);
	cb1.addItemListener(new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if(e.getStateChange()==ItemEvent.SELECTED){
				Shape.shape.setShapecolor((Color)cb1.getSelectedItem());
			}
		}
	});
cb2.addItemListener(new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if(e.getStateChange()==ItemEvent.SELECTED){
				Shape.shape.setBackcColor((Color)cb2.getSelectedItem());
			}
					}
	});
/*cb3.addItemListener(new ItemListener() {
	
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if(e.getStateChange()==ItemEvent.SELECTED){
			Shape.shape.setSize(Integer.parseInt((String)cb3.getSelectedItem()));
		}
		
	}
});*/
cb3.addActionListener(new ActionListener() {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Shape.shape.setSize(Integer.parseInt(cb3.getText()));
	}
});
t1.addActionListener(new ActionListener() {
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Shape.shape.setText(t1.getText());
	}
});
    ccb1.addItemListener(new ItemListener() {
		
		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if(e.getStateChange()==ItemEvent.SELECTED){
				
				Shape.shape.setIffill(true);
			}
			else {
				Shape.shape.setIffill(false);
			}
		}
	});
    
	BasicArrowButton up=new BasicArrowButton(BasicArrowButton.NORTH);
	up.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int i=Integer.parseInt(cb3.getText());
			i++;
			cb3.setText(""+i);
			Shape.shape.setSize(Integer.parseInt(cb3.getText()));
		}
	});
	BasicArrowButton down=new BasicArrowButton(BasicArrowButton.SOUTH);
down.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int i=Integer.parseInt(cb3.getText());
			i--;
			cb3.setText(""+i);
			Shape.shape.setSize(Integer.parseInt(cb3.getText()));
		}
	});
	JPanel pane4= new JPanel(new GridLayout(2,1));
	pane4.add(up);
	pane4.add(down);
	pane.add(pane4);
	pane.add(ccb1);
	add(pane);
}
 
}
class ComboBoxRenderer extends JLabel //继承标签类
implements ListCellRenderer {//实现ListCellRenderer接口
	public HashMap<Color,Icon>  icons = new HashMap<>();
public ComboBoxRenderer() {//构造函数
setOpaque(true); //设置透明
setHorizontalAlignment(CENTER);//设置本标签水平居中对齐
setVerticalAlignment(CENTER);//设置本标签垂直居中对齐
setPreferredSize(new Dimension(200,130));
icons.put(Color.green, new ImageIcon("C:\\Users\\Ha ha\\Downloads\\green.png"));
icons.put(Color.red, new ImageIcon("C:\\Users\\Ha ha\\Downloads\\red.png"));
icons.put(Color.gray, new ImageIcon("C:\\Users\\Ha ha\\Downloads\\gray.png"));
}

@Override
public Component getListCellRendererComponent(JList list, Object value,
		int index, boolean isSelected, boolean cellHasFocus) {
	// TODO Auto-generated method stub
	
  Color color=(Color)value;
  setIcon(icons.get(color)); 
   return this;//返回本标签
}

}