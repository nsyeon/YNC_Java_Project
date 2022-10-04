package Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class StudentDAO {
	
	private Inquire inquire = new Inquire();
	
	private Scanner scan;
	private ArrayList<Student> al;
	
	private String id;
	private String name;
	private int javaScore;
	private int pythonScore;
	private int phpScore;
	private int linuxScore;

	private int sum;
	
	private String[] insert_data = new String[6];
	Object[] newRow;
	
	private JFileChooser fc = new JFileChooser();
	
	//DB연동을 위함
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	private StringBuffer password;
	private char[] charPass;
	
	private final String USER_INSERT ="INSERT INTO user (id, pw) values (?,?)";
	private final String USER_CHECK = "SELECT * FROM user WHERE id=? AND pw=?";
	
	public int userInsert(UserInsert UserInsert) {
    	try{
	    	//db와 연결시키기 위한 커넥트
	        conn = JDBCUtil.getConnection();
	        //db에 쿼리를 전송하기 위한 준비
			stmt = conn.prepareStatement(USER_INSERT);
			//위 USER_INSERT에서 첫 번째 nayeon에 값을 넣기 위함
			charPass = UserInsert.getTextField_Pw().getPassword();
			password = new StringBuffer();
			for(int i = 0; i < charPass.length; i++) {
				password.append(charPass[i]);
			}
			//JPasswordField에서 .getPassword로 값을 읽어오는데 return 값이 char타입의 배열이기에
			//for문을 통해서 인덱스 값을 StringBuffer에 저장해서 String으로 사용
			stmt.setString(1, UserInsert.getTextField_Id().getText());
			//password에 값을 넣기 위함 
			stmt.setString(2, password.toString());
			//DB에 쿼리 전송
			stmt.executeUpdate();
			return 1;
			//에러났을 때
	    } catch (SQLException e) {
		    //finally는 에러 여부와 상관없이 실행
		    return 2;
	    } finally {
		    //DB와의 연동을 해제
			JDBCUtil.close(stmt, conn);
	    }
	}
	
	public int userCheck(MainInsert MainInsert) {
		try {
			conn = JDBCUtil.getConnection();
			stmt = conn.prepareStatement(USER_CHECK);
			stmt.setString(1, MainInsert.getTextField_Id().getText());
			charPass = MainInsert.getTextField_Pw().getPassword();
			password = new StringBuffer();
			for(int i = 0; i < charPass.length; i++) {
				password.append(charPass[i]);
			}
			stmt.setString(2, password.toString());
			rs = stmt.executeQuery();
			if(rs.next()) {
				return 1;
			} else {
				return 2;
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, e,"DB ERROR", JOptionPane.ERROR_MESSAGE);
		} finally {
			JDBCUtil.close(rs, stmt, conn);
		}
		return 0;
	}
	
	public StudentDAO() {
		al = new ArrayList<Student>();
	}

	//수정 => 수정 버튼을 누르면 수정완료 버튼으로 바뀌고 JTable 내에서 데이터를 수정한 후에 엔터를 치고 수정완료 버튼을 누르면 데이터가 수정됨
	public void update(JTable table, JButton delete_Button) {
		table.setEnabled(true);			//테이블 활성화
		delete_Button.setEnabled(true);	//삭제 버튼 활성화(수정 버튼을 눌러야 활성화됨)
	}
	//수정 완료
	public int updateEnd(JTable table, JButton delete_Button){
		try {
			for(int i = 0; i < table.getRowCount(); i++){
				sum = 0;
				for(int j = 2; j < 6; j++){
					int tableData = Integer.parseInt(table.getValueAt(i,j).toString());
					if(tableData >= 0 && tableData < 101) {
						sum += tableData;
					} else {
						JOptionPane.showMessageDialog(null, "성적을 0 ~ 100 사이로 넣어주세요.","수정 에러",JOptionPane.ERROR_MESSAGE);
						return 2;
					}
				}
				table.setValueAt(sum/4,i,6);
			}
			table.clearSelection();
			table.setEnabled(false);
			delete_Button.setEnabled(false);
			return 1;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "숫자를 입력해주세요","수정 에러",JOptionPane.ERROR_MESSAGE);
			return 2;
		}
	}

	//삭제 => 삭제하고 싶은 열을 누르고 삭제 버튼을 누르면 삭제됨 
	public void delete(DefaultTableModel model, JTable table, JButton delete_Button) {
		delete_Button.setEnabled(true);
		int[] selecteds = table.getSelectedRows();
		int selected = table.getSelectedRow();
		if(selecteds.length != 1) {
			model.removeRow(selecteds[0]);
			for(int i = 1; i < selecteds.length; i++) {
				System.out.println(Arrays.toString(selecteds));
				model.removeRow(selecteds[i]-i);
			}
			JOptionPane.showMessageDialog(null,"삭제되었습니다.","MESSAGE",JOptionPane.INFORMATION_MESSAGE);
		} else {
			if(selected != -1) {
				model.removeRow(selected);
				JOptionPane.showMessageDialog(null,"삭제되었습니다.","MESSAGE",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	//저장
	public void save(JTable table) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT text", "txt");
		fc.setFileFilter(filter);
			
		fc.setCurrentDirectory(new File(".\\"));
		int ret  = fc.showSaveDialog(null);			//파일 저장 다이얼로그를 출력하기 위해 showSaveDialog 호출
			
		if(ret != JFileChooser.APPROVE_OPTION) {
			  JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다", "경고", 
					  JOptionPane.WARNING_MESSAGE);	//파일을 선택하지 않으면 경고창을 띄움
			  return;
		}
		String filePath = fc.getSelectedFile().getPath(); 
			
		try {
			//영문만 읽을 것이 아니라 한글도 읽어야 하기에 BufferdWriter 사용 
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			for(int i = 0; i < table.getRowCount(); i++) {
				String saveStr = null;
				for(int j = 0; j < table.getColumnCount(); j++) {
					saveStr = String.valueOf(table.getValueAt(i, j)) + ",";
					bw.write(saveStr);
				}
				bw.write("\n");
			}
			bw.close();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(new StudentGUI(), "저장 오류");
		}
    }
	
	//불러오기 
	public void call(DefaultTableModel model) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT text", "txt");
        fc.setFileFilter(filter);
        
        int ret = fc.showOpenDialog(null);	  	//파일 다이어로그 출력 null -> 전체 화면을 기준으로 위치를 잡음.
        if(ret != JFileChooser.APPROVE_OPTION) {
           JOptionPane.showMessageDialog(null, "파일을 선택하지 않았습니다.", "경고", 
        		   JOptionPane.WARNING_MESSAGE); 	//파일을 선택하지 않으면 경고창을 띄움
           return;
        }
        
        //사용자가 파일을 선택하고 "열기" 버튼을 누른 경우
        String filePath = fc.getSelectedFile().getPath();
        //내가 선택한 파일을 읽어오는 코드 (try ~ catch)
        try {
      	    //영문만 읽을 것이 아니라 한글도 읽어야 하기에 BufferdWriter 사용 
            BufferedReader rd = new BufferedReader(new FileReader(filePath));
            String line;

            while((line = rd.readLine()) != null) {
                String[] textData = line.split(",");
                model.addRow(textData);
            }
            rd.close();
        } catch(Exception e1) {
           JOptionPane.showMessageDialog(new StudentGUI(), "열기 오류");
           e1.printStackTrace();
        }
    }
}



