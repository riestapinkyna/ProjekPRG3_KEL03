package Projek;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FormLogin {
    public JPanel jpUtama;
    private JTextField txtusername;
    private JTextArea textArea1;
    private JButton LOGINButton;
    private JTextArea textArea2;
    private JPasswordField txtpw;
    public FormLogin(){
        LOGINButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    String url = "jdbc:sqlserver://localhost;database=PRG3_KEL03;user=sa;password=polman";
                    Connection con = DriverManager.getConnection(url);
                    String sql = "Select * from tblKaryawan where username = ? and [password] = ?";
                    PreparedStatement pst = con.prepareStatement(sql);
                    pst.setString(1, txtusername.getText());
                    pst.setString(2, txtpw.getText());
                    ResultSet rs = pst.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(null, "Selamat datang Bapak/Ibu Pegawai!", "Login Pegawai",
                                JOptionPane.INFORMATION_MESSAGE);
                        jpUtama.removeAll();
                        jpUtama.revalidate();
                        jpUtama.repaint();
                        MenuPegawai c = new MenuPegawai();
                        c.Menu.setVisible(true);
                        jpUtama.setLayout(new BorderLayout());
                        jpUtama.add(c.Menu);
                        jpUtama.validate();
                    }
                    else if (txtusername.getText().equals("Manager") && txtpw.getText().equals("MGR123")) {
                        JOptionPane.showMessageDialog(null, "Selamat datang Bapak/Ibu Manager!", "Login Manager",
                                JOptionPane.INFORMATION_MESSAGE);
                        jpUtama.removeAll();
                        jpUtama.revalidate();
                        jpUtama.repaint();
                        MenuManajer c = new MenuManajer();
                        c.Menu.setVisible(true);
                        jpUtama.setLayout(new BorderLayout());
                        jpUtama.add(c.Menu);
                        jpUtama.validate();
                    }else
                    {
                        JOptionPane.showMessageDialog(null, "Username atau Password Salah!","Login Pegawai",
                                JOptionPane.ERROR_MESSAGE);
                        txtusername.setText("");
                        txtpw.setText("");
                    }
                    con.close();
                }
                catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
            }
        });
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Apotek Zapink");
        frame.setContentPane(new FormLogin().jpUtama);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
}
