package Project;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MainInsert {
	private JPanel MainInsertPanel = new JPanel();
	
	private JButton UserInsert_Button = new JButton("회원가입");
	private JButton Login_Button = new JButton("로그인");

	private JTextField UserInsert_Id = new JTextField(10);
	private JPasswordField UserInsert_Pw = new JPasswordField(10);
    private JLabel[] UserInsert_text = new JLabel[2];
    private String[] UserInsert_label = {"아이디", "비밀번호"};
    
    private int line_spacing = 70;
    
    private void MainInsertDesign() {
    	MainInsertPanel.setBounds(0, 0, 500, 300);			//등록 패널의 절대 위치 지정
    	MainInsertPanel.setLayout(null);					//절대 위치(setBounds)를 위함 
    	for(int i = 0; i < UserInsert_text.length; i++) {
    		UserInsert_text[i] = new JLabel(UserInsert_label[i]);
			UserInsert_text[i].setBounds(150, line_spacing, 100, 25);
			line_spacing += 30;
			MainInsertPanel.add(UserInsert_text[i]);
    	}
    	UserInsert_Id.setBounds(250, 70, 100, 25);
    	UserInsert_Pw.setBounds(250, 100, 100, 25);
    	MainInsertPanel.add(UserInsert_Id);
    	MainInsertPanel.add(UserInsert_Pw);
    	Login_Button.setBounds(150, 130, 90, 25);	
    	UserInsert_Button.setBounds(260, 130, 90, 25);	
    	MainInsertPanel.add(UserInsert_Button);		
    	MainInsertPanel.add(Login_Button);		
    }
    
    public JPanel getMainInsertPanel() {
    	MainInsertDesign();
    	return MainInsertPanel;
    }
    
    public JButton getUserInsert_Button() {
    	return UserInsert_Button;
    }
    
    public JTextField getTextField_Id() {
    	return UserInsert_Id;
    }
    
    public JPasswordField getTextField_Pw() {
    	return UserInsert_Pw;
    }
    
    public JButton getLogin_Button() {
    	return Login_Button;
    }
		

}
