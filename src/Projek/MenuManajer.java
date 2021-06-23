package Projek;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuManajer {
    private JPanel panel1;
    private JButton karyawanButton;
    private JTextArea textArea1;
    private JButton logOutButton;
    private JPanel jpmenu;
    public JPanel Menu;

    public MenuManajer() {
        karyawanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpmenu.removeAll();
                jpmenu.revalidate();
                jpmenu.repaint();
                CRUDKaryawan p = new CRUDKaryawan();
                p.jpUtama.setVisible(true);
                jpmenu.revalidate();
                jpmenu.setLayout(new java.awt.BorderLayout());
                jpmenu.add(p.jpUtama);

            }
        });
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int reply = JOptionPane.showConfirmDialog(null, "Apakah anda yakin akan keluar dari menu ini?", "Log Out Validation", JOptionPane.YES_NO_OPTION);
                if (reply == JOptionPane.YES_OPTION) {
                    Menu.removeAll();
                    Menu.revalidate();
                    Menu.repaint();
                    FormLogin p = new FormLogin();
                    p.jpUtama.setVisible(true);
                    Menu.revalidate();
                    Menu.setLayout(new java.awt.BorderLayout());
                    Menu.add(p.jpUtama);
                }
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Apotek Zapink");
        frame.setContentPane(new MenuManajer().Menu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800,400);
    }
}
