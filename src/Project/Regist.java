package Project;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//���
public class Regist {
	
	private JPanel RegistPanel = new JPanel();
	private JPanel content = new JPanel();
	
    private JButton regist_Button = new JButton("���");
    private JTextField[] regist_data = new JTextField[6];
    private JLabel[] regist_text = new JLabel[6];
    private String[] text_label = {"ID", "NAME", "JAVA", "PYTHON", "PHP", "LINUX"};

    //line_spacing�� 15�� ����
    private int line_spacing = 15;

    private void RegistDesign() {
    	RegistPanel.setBounds(0, 0, 500, 300);		//��� �г��� ���� ��ġ ����
		RegistPanel.setLayout(null);				//���� ��ġ(setBounds)�� ���� 
		for(int i = 0; i < regist_data.length; i++) {
			regist_text[i] = new JLabel(text_label[i]);
			regist_data[i] = new JTextField(10);
			regist_text[i].setBounds(150, line_spacing, 100, 25);
			regist_data[i].setBounds(250, line_spacing, 100, 25);
			line_spacing += 30;
			RegistPanel.add(regist_text[i]);
			RegistPanel.add(regist_data[i]);
		}
		regist_Button.setBounds(150, 195, 200, 25);	//��� ��ư�� ���� ��ġ ���� 
		RegistPanel.add(regist_Button);				//��� �гο� ��� ��ư �߰�
     }
     
     public JPanel getRegistPanel() {
    	 RegistDesign();
    	 return RegistPanel;
     }
     
     public JButton getRegist_Button() {
    	 return regist_Button;
     }
     
     public JTextField[] getTexField() {
    	 return regist_data;
     }
}