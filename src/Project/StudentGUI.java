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
	
	//등록된 학생데이터를 저장
	private StudentDAO dao = new StudentDAO();
	private ArrayList<Student> al;
	
	//메뉴바 생성
	private JMenuBar menubar = new JMenuBar();
		
	//메뉴 생성
	private JMenu file = new JMenu("파일");	
	private JMenu see = new JMenu("보기");
    
	private JMenuItem[] fileItem = new JMenuItem[3];
	private JMenuItem[] seeItem = new JMenuItem[2];
	private String[] itemTitle = {"저장","불러오기","종료","등록","조회"};
	
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
		this.setTitle("학생관리프로그램");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.formDesign();
		this.eventHandler();
		this.setSize(500, 300);
		this.setVisible(true);
		this.setLocationRelativeTo(null);	//GUI창을 화면의 중간으로 
		this.setResizable(false);			//창 크기 고정
	}
	
	//메뉴를 만들어 프레임에 삽입
	private void formDesign() {
		for(int i = 0; i < fileItem.length; i++) {
        	fileItem[i] = new JMenuItem(itemTitle[i]);
        	file.add(fileItem[i]);
        	if(i == fileItem.length - 1) {
        		break;
        	}
        	file.addSeparator();	//구분선 삽입
        	file.setEnabled(false);	//로그인이 되기 전에는 '파일 메뉴' 비활성화
        }
        for(int i = 0; i < seeItem.length; i++) {
        	seeItem[i] = new JMenuItem(itemTitle[i+3]);	
        	see.add(seeItem[i]);
        	if(i == seeItem.length - 1) {
        		break;
        	}	
        	see.addSeparator();		//구분선 삽입
        	see.setEnabled(false);	//로그인이 되기 전에는 '보기 메뉴' 비활성화
        }

        menubar.add(file); 			//파일 메뉴 삽입
        menubar.add(see);  			//보기 메뉴 삽입
        setJMenuBar(menubar);		//메뉴바를 프레임에 부착
        
        this.add(MainPanel);
        
        MainPanel.setVisible(true);
        MainPanel.setLayout(new CardLayout());	//CardLayout 사용
        
        //첫 화면(로그인 및 회원가입)
        MainPanel.add(MainInsertPanel);
        MainInsertPanel.setVisible(true);
        MainInsertPanel.setLayout(null);
        MainInsertPanel.add(maininsert.getMainInsertPanel());
        
        //회원가입
        MainPanel.add(UserInsertPanel);
        UserInsertPanel.setVisible(false);
        UserInsertPanel.setLayout(null);
        UserInsertPanel.add(userinsert.getUserInsertPanel());
        
        //등록
        MainPanel.add(registPanel);
        registPanel.setVisible(false);
        registPanel.setLayout(null);
        registPanel.add(regist.getRegistPanel());
        
        //조회
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
			case "등록":
				registPanel.setVisible(true);
				inquirePanel.setVisible(false);
				UserInsertPanel.setVisible(false);
				MainInsertPanel.setVisible(false);
				break;
			case "저장":
				inquire.save_table(dao);
				break;
			case "불러오기":
				inquire.call_table(dao);
				registPanel.setVisible(false);
				inquirePanel.setVisible(true);
				UserInsertPanel.setVisible(false);
				MainInsertPanel.setVisible(false);
				break;
			case "조회":
				registPanel.setVisible(false);
				inquirePanel.setVisible(true);
				UserInsertPanel.setVisible(false);
				MainInsertPanel.setVisible(false);
				break;
			case "종료" :
				System.exit(0);
				break;
			}
		}
    }
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("수정")) {
			inquire.update(dao);
			inquire.getUpdate_Button().setText("수정완료");
		} else if (e.getActionCommand().equals("수정완료")){
			checkInteger = inquire.updateEnd(dao);
			if(checkInteger == 1) {
				inquire.getUpdate_Button().setText("수정");
			}
		} else if (e.getActionCommand().equals("삭제")) {
			inquire.delete(dao);
		} else if (e.getActionCommand().equals("등록")) {
            for (int i = 0; i < regist.getTexField().length; i++) {
                if (regist.getTexField()[i].getText() == null || regist.getTexField()[i].getText().length() == 0) {
                	JOptionPane.showMessageDialog(null, "정보를 입력해주세요!","입력 에러",JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
			registState = inquire.insert(regist.getTexField()[0].getText(), regist.getTexField()[1].getText(), 
					regist.getTexField()[2].getText(), regist.getTexField()[3].getText(), regist.getTexField()[4].getText(), 
					regist.getTexField()[5].getText());
			if(registState == 1) {
				JOptionPane.showMessageDialog(null, "등록이 되었습니다.","MESSAGE",JOptionPane.INFORMATION_MESSAGE);
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
		} else if (e.getActionCommand().equals("회원가입")) {
			registPanel.setVisible(false);
			inquirePanel.setVisible(false);
			UserInsertPanel.setVisible(true);
			MainInsertPanel.setVisible(false);
			maininsert.getTextField_Id().setText("");
			maininsert.getTextField_Pw().setText("");
		} else if (e.getActionCommand().equals("취소")) {
			registPanel.setVisible(false);
			inquirePanel.setVisible(false);
			UserInsertPanel.setVisible(false);
			MainInsertPanel.setVisible(true);
			userinsert.getTextField_Id().setText("");
			userinsert.getTextField_Pw().setText("");
		} else if (e.getActionCommand().equals("가입완료")) {
			if(userinsert.getTextField_Id().getText() == null || userinsert.getTextField_Id().getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해주세요","ID ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(userinsert.getTextField_Pw().getPassword().length == 0 || userinsert.getTextField_Pw().getPassword() == null) {
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요","PW ERROR",JOptionPane.ERROR_MESSAGE);
			}
			userinsertState = dao.userInsert(userinsert);
			if(userinsertState == 1) {
				JOptionPane.showMessageDialog(null,"회원가입이 완료되었습니다.","회원가입",JOptionPane.INFORMATION_MESSAGE);
				registPanel.setVisible(false);
				inquirePanel.setVisible(false);
				UserInsertPanel.setVisible(false);
				MainInsertPanel.setVisible(true);
			}
			else if(userinsertState == 2) {
				JOptionPane.showMessageDialog(null,"중복된 아이디가 있습니다.","회원가입",JOptionPane.ERROR_MESSAGE);
				return;
			}

		} else if (e.getActionCommand().equals("로그인")) {
			if(maininsert.getTextField_Id().getText() == null || maininsert.getTextField_Id().getText().length() == 0) {
				JOptionPane.showMessageDialog(null, "아이디를 입력해주세요","ID ERROR",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(maininsert.getTextField_Pw().getPassword().length == 0 || maininsert.getTextField_Pw().getPassword() == null) {
				JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요","PW ERROR",JOptionPane.ERROR_MESSAGE);
			}
			loginState = dao.userCheck(maininsert);
			if(loginState == 1) {
				JOptionPane.showMessageDialog(null,"로그인이 완료되었습니다.","로그인",JOptionPane.INFORMATION_MESSAGE);
				see.setEnabled(true);	//로그인이 되면 '보기 메뉴' 활성화
				file.setEnabled(true);	//로그인이 되면 '파일 메뉴' 활성화
			} else if(loginState == 2) {
				JOptionPane.showMessageDialog(null, "로그인 정보가 옳지 않습니다.","로그인", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}
	
}
