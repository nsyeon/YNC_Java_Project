package Project;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class StudentGUI extends JFrame implements ActionListener{
	
	//��ϵ� �л������͸� ����
	private StudentDAO dao = new StudentDAO();
	private ArrayList<Student> al;
	
	//�޴��� ����
	private JMenuBar menubar = new JMenuBar();
		
	//�޴� ����
	private JMenu file = new JMenu("����");	
	private JMenu see = new JMenu("����");
    
	private JMenuItem[] fileItem = new JMenuItem[3];
	private JMenuItem[] seeItem = new JMenuItem[2];
	private String[] itemTitle = {"����","�ҷ�����","����","���","��ȸ"};
	
	private JPanel MainPanel = new JPanel();
	private JPanel registPanel = new JPanel();
	private JPanel inquirePanel = new JPanel();
	private JPanel UserInsertPanel = new JPanel();
	private JPanel MainInsertPanel = new JPanel();
	
	private Regist regist = new Regist();
	private Inquire inquire = new Inquire();
	private UserInsert userinsert = new UserInsert();
	private MainInsert maininsert = new MainInsert();
	private int registState;
	
	private int loginState;
	private int userinsertState;
	private int checkInteger;

	
	StudentGUI(){
		this.setTitle("�л��������α׷�");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.formDesign();
		this.eventHandler();
		this.setSize(500, 300);
		this.setVisible(true);
		this.setLocationRelativeTo(null);	//GUIâ�� ȭ���� �߰����� 
		this.setResizable(false);			//â ũ�� ����
	}
	
	//�޴��� ����� �����ӿ� ����
	private void formDesign() {
		for(int i = 0; i < fileItem.length; i++) {
        	fileItem[i] = new JMenuItem(itemTitle[i]);
        	file.add(fileItem[i]);
        	if(i == fileItem.length - 1) {
        		break;
        	}
        	file.addSeparator();	//���м� ����
        	file.setEnabled(false);	//�α����� �Ǳ� ������ '���� �޴�' ��Ȱ��ȭ
        }
        for(int i = 0; i < seeItem.length; i++) {
        	seeItem[i] = new JMenuItem(itemTitle[i+3]);	
        	see.add(seeItem[i]);
        	if(i == seeItem.length - 1) {
        		break;
        	}	
        	see.addSeparator();		//���м� ����
        	see.setEnabled(false);	//�α����� �Ǳ� ������ '���� �޴�' ��Ȱ��ȭ
        }

        menubar.add(file); 			//���� �޴� ����
        menubar.add(see);  			//���� �޴� ����
        setJMenuBar(menubar);		//�޴��ٸ� �����ӿ� ����
        
        this.add(MainPanel);
        
        MainPanel.setVisible(true);
        MainPanel.setLayout(new CardLayout());	//CardLayout ���
        
        //ù ȭ��(�α��� �� ȸ������)
        MainPanel.add(MainInsertPanel);
        MainInsertPanel.setVisible(true);
        MainInsertPanel.setLayout(null);
        MainInsertPanel.add(maininsert.getMainInsertPanel());
        
        //ȸ������
        MainPanel.add(UserInsertPanel);
        UserInsertPanel.setVisible(false);
        UserInsertPanel.setLayout(null);
        UserInsertPanel.add(userinsert.getUserInsertPanel());
        
        //���
        MainPanel.add(registPanel);
        registPanel.setVisible(false);
        registPanel.setLayout(null);
        registPanel.add(regist.getRegistPanel());
        
        //��ȸ
        MainPanel.add(inquirePanel);
        inquirePanel.setVisible(false);
        inquirePanel.setLayout(null);
        inquirePanel.add(inquire.getInquirePanel());
	}
	
	private void eventHandler() {
		for (int i = 0; i < fileItem.length; i++) {
			fileItem[i].addActionListener(new MenuActionListener());
		}
		for (int i = 0; i < seeItem.length; i++) {
			seeItem[i].addActionListener(new MenuActionListener());
		}
		inquire.getUpdate_Button().addActionListener(this);
		inquire.getDelete_Button().addActionListener(this);
		regist.getRegist_Button().addActionListener(this);
		maininsert.getUserInsert_Button().addActionListener(this);
		userinsert.getCancel_Button().addActionListener(this);
		userinsert.getUserInsert_Button().addActionListener(this);
		maininsert.getLogin_Button().addActionListener(this);
	}
	
	public static void main(String[] args) {
		new StudentGUI();
	}
	
	class MenuActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			MainPanel.setVisible(true);
			String string = e.getActionCommand();
			switch(string) {
			case "���":
				registPanel.setVisible(true);
				inquirePanel.setVisible(false);
				UserInsertPanel.setVisible(false);
				MainInsertPanel.setVisible(false);
				break;
			case "����":
				inquire.save_table(dao);
				break;
			case "�ҷ�����":
				inquire.call_table(dao);
				registPanel.setVisible(false);
				inquirePanel.setVisible(true);
				UserInsertPanel.setVisible(false);
				MainInsertPanel.setVisible(false);
				break;
			case "��ȸ":
				registPanel.setVisible(false);
				inquirePanel.setVisible(true);
				UserInsertPanel.setVisible(false);
				MainInsertPanel.setVisible(false);
				break;
			case "����" :
				System.exit(0);
				break;
			}
		}
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("����")) {
			inquire.update(dao);
			inquire.getUpdate_Button().setText("�����Ϸ�");
		} else if (e.getActionCommand().equals("�����Ϸ�")){
			checkInteger = inquire.updateEnd(dao);
			if(checkInteger == 1) {
				inquire.getUpdate_Button().setText("����");
			}
		} else if (e.getActionCommand().equals("����")) {
			inquire.delete(dao);
		} else if (e.getActionCommand().equals("���")) {
            for (int i = 0; i < regist.getTexField().length; i++) {
                if (regist.getTexField()[i].getText() == null || regist.getTexField()[i].getText().length() == 0) {
                	JOptionPane.showMessageDialog(null, "������ �Է����ּ���!","�Է� ����",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
			registState = inquire.insert(regist.getTexField()[0].getText(), regist.getTexField()[1].getText(), 
					regist.getTexField()[2].getText(), regist.getTexField()[3].getText(), regist.getTexField()[4].getText(), 
					regist.getTexField()[5].getText());
			if(registState == 1) {
				JOptionPane.showMessageDialog(null, "����� �Ǿ����ϴ�.","MESSAGE",JOptionPane.INFORMATION_MESSAGE);
				registPanel.setVisible(false);
				inquirePanel.setVisible(true);
				for(int i = 0; i < regist.getTexField().length; i++) {
					regist.getTexField()[i].setText("");
				}
			} else if(registState == 2) {
				for(int i = 2; i < regist.getTexField().length; i++) {
					regist.getTexField()[i].setText("");
				}
			} else if(registState == 3) {
				for(int i = 0; i < regist.getTexField().length; i++) {
					regist.getTexField()[i].setText("");
				}
			}
		} else if (e.getActionCommand().equals("ȸ������")) {
			registPanel.setVisible(false);
			inquirePanel.setVisible(false);
			UserInsertPanel.setVisible(true);
			MainInsertPanel.setVisible(false);
			maininsert.getTextField_Id().setText("");
			maininsert.getTextField_Pw().setText("");
		} else if (e.getActionCommand().equals("���")) {
			registPanel.setVisible(false);
			inquirePanel.setVisible(false);
			UserInsertPanel.setVisible(false);
			MainInsertPanel.setVisible(true);
			userinsert.getTextField_Id().setText("");
			userinsert.getTextField_Pw().setText("");
		} else if (e.getActionCommand().equals("���ԿϷ�")) {
			if(userinsert.getTextField_Id().getText() == null || userinsert.getTextField_Id().getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "���̵� �Է����ּ���","ID ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(userinsert.getTextField_Pw().getPassword().length == 0 || userinsert.getTextField_Pw().getPassword() == null) {
				JOptionPane.showMessageDialog(null, "��й�ȣ�� �Է����ּ���","PW ERROR",JOptionPane.ERROR_MESSAGE);
			}
			userinsertState = dao.userInsert(userinsert);
			if(userinsertState == 1) {
				JOptionPane.showMessageDialog(null,"ȸ�������� �Ϸ�Ǿ����ϴ�.","ȸ������",JOptionPane.INFORMATION_MESSAGE);
				registPanel.setVisible(false);
				inquirePanel.setVisible(false);
				UserInsertPanel.setVisible(false);
				MainInsertPanel.setVisible(true);
			}
			else if(userinsertState == 2) {
				JOptionPane.showMessageDialog(null,"�ߺ��� ���̵� �ֽ��ϴ�.","ȸ������",JOptionPane.ERROR_MESSAGE);
				return;
			}

		} else if (e.getActionCommand().equals("�α���")) {
			if(maininsert.getTextField_Id().getText() == null || maininsert.getTextField_Id().getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "���̵� �Է����ּ���","ID ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(maininsert.getTextField_Pw().getPassword().length == 0 || maininsert.getTextField_Pw().getPassword() == null) {
				JOptionPane.showMessageDialog(null, "��й�ȣ�� �Է����ּ���","PW ERROR",JOptionPane.ERROR_MESSAGE);
			}
			loginState = dao.userCheck(maininsert);
			if(loginState == 1) {
				JOptionPane.showMessageDialog(null,"�α����� �Ϸ�Ǿ����ϴ�.","�α���",JOptionPane.INFORMATION_MESSAGE);
				see.setEnabled(true);	//�α����� �Ǹ� '���� �޴�' Ȱ��ȭ
				file.setEnabled(true);	//�α����� �Ǹ� '���� �޴�' Ȱ��ȭ
			} else if(loginState == 2) {
				JOptionPane.showMessageDialog(null, "�α��� ������ ���� �ʽ��ϴ�.","�α���", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}
	
}
