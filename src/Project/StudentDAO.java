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
	
	//DB������ ����
	private Connection conn = null;
	private PreparedStatement stmt = null;
	private ResultSet rs = null;
	
	private StringBuffer password;
	private char[] charPass;
	
	private final String USER_INSERT ="INSERT INTO user (id, pw) values (?,?)";
	private final String USER_CHECK = "SELECT * FROM user WHERE id=? AND pw=?";
	
	public int userInsert(UserInsert UserInsert) {
    	try{
	    	//db�� �����Ű�� ���� Ŀ��Ʈ
	        conn = JDBCUtil.getConnection();
	        //db�� ������ �����ϱ� ���� �غ�
			stmt = conn.prepareStatement(USER_INSERT);
			//�� USER_INSERT���� ù ��° nayeon�� ���� �ֱ� ����
			charPass = UserInsert.getTextField_Pw().getPassword();
			password = new StringBuffer();
			for(int i = 0; i < charPass.length; i++) {
				password.append(charPass[i]);
			}
			//JPasswordField���� .getPassword�� ���� �о���µ� return ���� charŸ���� �迭�̱⿡
			//for���� ���ؼ� �ε��� ���� StringBuffer�� �����ؼ� String���� ���
			stmt.setString(1, UserInsert.getTextField_Id().getText());
			//password�� ���� �ֱ� ���� 
			stmt.setString(2, password.toString());
			//DB�� ���� ����
			stmt.executeUpdate();
			return 1;
			//�������� ��
	    } catch (SQLException e) {
		    //finally�� ���� ���ο� ������� ����
		    return 2;
	    } finally {
		    //DB���� ������ ����
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

	//���� => ���� ��ư�� ������ �����Ϸ� ��ư���� �ٲ�� JTable ������ �����͸� ������ �Ŀ� ���͸� ġ�� �����Ϸ� ��ư�� ������ �����Ͱ� ������
	public void update(JTable table, JButton delete_Button) {
		table.setEnabled(true);			//���̺� Ȱ��ȭ
		delete_Button.setEnabled(true);	//���� ��ư Ȱ��ȭ(���� ��ư�� ������ Ȱ��ȭ��)
	}
	//���� �Ϸ�
	public int updateEnd(JTable table, JButton delete_Button){
		try {
			for(int i = 0; i < table.getRowCount(); i++){
				sum = 0;
				for(int j = 2; j < 6; j++){
					int tableData = Integer.parseInt(table.getValueAt(i,j).toString());
					if(tableData >= 0 && tableData < 101) {
						sum += tableData;
					} else {
						JOptionPane.showMessageDialog(null, "������ 0 ~ 100 ���̷� �־��ּ���.","���� ����",JOptionPane.ERROR_MESSAGE);
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
			JOptionPane.showMessageDialog(null, "���ڸ� �Է����ּ���","���� ����",JOptionPane.ERROR_MESSAGE);
			return 2;
		}
	}

	//���� => �����ϰ� ���� ���� ������ ���� ��ư�� ������ ������ 
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
			JOptionPane.showMessageDialog(null,"�����Ǿ����ϴ�.","MESSAGE",JOptionPane.INFORMATION_MESSAGE);
		} else {
			if(selected != -1) {
				model.removeRow(selected);
				JOptionPane.showMessageDialog(null,"�����Ǿ����ϴ�.","MESSAGE",JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
	
	//����
	public void save(JTable table) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT text", "txt");
		fc.setFileFilter(filter);
			
		fc.setCurrentDirectory(new File(".\\"));
		int ret  = fc.showSaveDialog(null);			//���� ���� ���̾�α׸� ����ϱ� ���� showSaveDialog ȣ��
			
		if(ret != JFileChooser.APPROVE_OPTION) {
			  JOptionPane.showMessageDialog(null, "������ �������� �ʾҽ��ϴ�", "���", 
					  JOptionPane.WARNING_MESSAGE);	//������ �������� ������ ���â�� ���
			  return;
		}
		String filePath = fc.getSelectedFile().getPath(); 
			
		try {
			//������ ���� ���� �ƴ϶� �ѱ۵� �о�� �ϱ⿡ BufferdWriter ��� 
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
			JOptionPane.showMessageDialog(new StudentGUI(), "���� ����");
		}
    }
	
	//�ҷ����� 
	public void call(DefaultTableModel model) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT text", "txt");
        fc.setFileFilter(filter);
        
        int ret = fc.showOpenDialog(null);	  	//���� ���̾�α� ��� null -> ��ü ȭ���� �������� ��ġ�� ����.
        if(ret != JFileChooser.APPROVE_OPTION) {
           JOptionPane.showMessageDialog(null, "������ �������� �ʾҽ��ϴ�.", "���", 
        		   JOptionPane.WARNING_MESSAGE); 	//������ �������� ������ ���â�� ���
           return;
        }
        
        //����ڰ� ������ �����ϰ� "����" ��ư�� ���� ���
        String filePath = fc.getSelectedFile().getPath();
        //���� ������ ������ �о���� �ڵ� (try ~ catch)
        try {
      	    //������ ���� ���� �ƴ϶� �ѱ۵� �о�� �ϱ⿡ BufferdWriter ��� 
            BufferedReader rd = new BufferedReader(new FileReader(filePath));
            String line;

            while((line = rd.readLine()) != null) {
                String[] textData = line.split(",");
                model.addRow(textData);
            }
            rd.close();
        } catch(Exception e1) {
           JOptionPane.showMessageDialog(new StudentGUI(), "���� ����");
           e1.printStackTrace();
        }
    }
}



