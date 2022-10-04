package Project;

import java.awt.BorderLayout;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

//조회
public class Inquire{
	
	private Regist regist = new Regist();

	private JPanel InquirePanel = new JPanel();
	private JPanel content = new JPanel();
	
	private JTextArea inquire_Area = new JTextArea();
	
	private JButton update_Button = new JButton("수정");
	private JButton delete_Button = new JButton("삭제");
	
	private String[] table_title = {"학번","이름","JAVA","PYTHON","PHP","LINUX","평균"};
	
	DefaultTableModel inquire_model = new DefaultTableModel(table_title, 0);
	JTable inquire_table = new JTable(inquire_model);
	JScrollPane table_scroll = new JScrollPane(inquire_table);
	
	String[] insert_data = new String[7];
	
	//수정
	public void update(StudentDAO dao){
		dao.update(inquire_table, delete_Button);
	}

	//수정 완료 
	public int updateEnd(StudentDAO dao){
		int checkInteger = 0;
		checkInteger = dao.updateEnd(inquire_table, delete_Button);
		return checkInteger;
	}
	
	//삭제
	public void delete(StudentDAO dao) {
		dao.delete(inquire_model, inquire_table, delete_Button);
	}
	
	public String handling(int subjectCode) {
		if(subjectCode == 0) {
			return "java";
		} else if(subjectCode == 1) {
			return "python";
		} else if(subjectCode == 2) {
			return "php";
		} else if(subjectCode == 3) {
			return "linux";
		}
		return "";
	}
	
	public int insert(String id, String name, String javaScore, String pythonScore, String phpScore, String linuxScore) {
		for(int i = 0; i < inquire_model.getRowCount(); i++){ 
			if(id.equals(inquire_model.getValueAt(i,0))){
	       		//아이디 중복 시
				JOptionPane.showMessageDialog(null, "사용 중인 아이디입니다","MESSAGE",JOptionPane.ERROR_MESSAGE);
				return 3; //insert 나감 
			}
		}
		try {
			int[] intScore = {Integer.parseInt(javaScore), Integer.parseInt(pythonScore), Integer.parseInt(phpScore), Integer.parseInt(linuxScore)};
			int sum = 0;
			for(int i = 0; i < intScore.length; i++) {
				if(intScore[i] >= 0 && intScore[i] < 101) {
					insert_data[i+2] = Integer.toString(intScore[i]);
					sum += intScore[i];
				} else {
					JOptionPane.showMessageDialog(null, handling(i) + "성적을 0 ~ 100 사이로 넣어주세요", "입력 에러", JOptionPane.INFORMATION_MESSAGE);
					return 2;
				}
			}
			insert_data[0] = id;
			insert_data[1] = name;
			insert_data[6] = Integer.toString(sum/4);
			inquire_model.addRow(insert_data);
		} catch(Exception e) {
			JOptionPane.showMessageDialog(null, "성적에 숫자만 입력해주세요.","입력 에러",JOptionPane.ERROR_MESSAGE);
			return 2;
		}
		return 1;
	}
	
	
	
	private void InquireDesign() {
		
		InquirePanel.setLayout(null);				//절대 위치(setBounds)를 위함
		InquirePanel.setBounds(0, 0, 500, 300);		//조회 패널의 절대 위치 지정

		inquire_table.setEnabled(false);			//조회테이블 비활성화
		
		inquire_table.setAutoCreateRowSorter(true);	//자동 행 정렬 => 속성을 꼭 true로 설정해줘야 함 (컬럼 헤더를 클릭했을 때, 행을 자동으로 정렬해주는 정렬기 정의)
		table_scroll.setBounds(20, 15, 450, 170);	//스크롤의 절대 위치 지정
		update_Button.setBounds(135, 200, 100, 25);	//수정 버튼의 절대 위치 지정
		delete_Button.setBounds(250, 200, 100, 25);	//삭제 버튼의 절대 위치 지정 
		delete_Button.setEnabled(false);			//삭제 버튼 비활성화
		
		InquirePanel.add(table_scroll);				//조회 패널에 스크롤 추가
		InquirePanel.add(update_Button);			//조회 패널에 수정 버튼 추가
		InquirePanel.add(delete_Button);			//조회 패널에 삭제 버튼 추가
	}

	public JButton getUpdate_Button() {
		return update_Button;
	}

	public JButton getDelete_Button() {
		return delete_Button;
	}
	
	public JPanel getInquirePanel() {
		InquireDesign();
   	 	return InquirePanel;
    }

	public DefaultTableModel getInquire_model() {
		return inquire_model;
	}

	public void save_table(StudentDAO dao) {
		dao.save(inquire_table);
	}
	
	public void call_table(StudentDAO dao) {
		dao.call(inquire_model);
	}

}
