package Projek;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPegawai {
    private JPanel panel1;
    private JButton btnantrian;
    private JButton obatButton;
    private JTextArea textArea1;
    private JButton logOutButton;
    private JPanel jpmenu;
    public JPanel Menu;
    private JButton btnDistributor;
    private JButton btnDokter;
    private JButton jenisbtn;
    private JButton satuanbtn;
    private JButton transbelbtn;
    private JButton transjuabtn;

    public MenuPegawai() {
        obatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpmenu.removeAll();
                jpmenu.revalidate();
                jpmenu.repaint();
                CRUDobt p = new CRUDobt();
                p.jpUtama.setVisible(true);
                jpmenu.revalidate();
                jpmenu.setLayout(new java.awt.BorderLayout());
                jpmenu.add(p.jpUtama);
            }
        });
        btnantrian.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpmenu.removeAll();
                jpmenu.revalidate();
                jpmenu.repaint();
                CRUDProdusen  p = new CRUDProdusen();
                p.jpUtama.setVisible(true);
                jpmenu.revalidate();
                jpmenu.setLayout(new java.awt.BorderLayout());
                jpmenu.add(p.jpUtama);
            }
        });
        btnDistributor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpmenu.removeAll();
                jpmenu.revalidate();
                jpmenu.repaint();
                CRUDDistributor p = new CRUDDistributor();
                p.jpUtama.setVisible(true);
                jpmenu.revalidate();
                jpmenu.setLayout(new java.awt.BorderLayout());
                jpmenu.add(p.jpUtama);
            }
        });
        btnDokter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpmenu.removeAll();
                jpmenu.revalidate();
                jpmenu.repaint();
                CRUDDokter p = new CRUDDokter();
                p.jpUtama.setVisible(true);
                jpmenu.revalidate();
                jpmenu.setLayout(new java.awt.BorderLayout());
                jpmenu.add(p.jpUtama);
            }
        });
        jenisbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpmenu.removeAll();
                jpmenu.revalidate();
                jpmenu.repaint();
                CRUDJenis p = new CRUDJenis();
                p.jpUtama.setVisible(true);
                jpmenu.revalidate();
                jpmenu.setLayout(new java.awt.BorderLayout());
                jpmenu.add(p.jpUtama);
            }
        });
        satuanbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpmenu.removeAll();
                jpmenu.revalidate();
                jpmenu.repaint();
                CRUDSatuan p = new CRUDSatuan();
                p.jpUtama.setVisible(true);
                jpmenu.revalidate();
                jpmenu.setLayout(new java.awt.BorderLayout());
                jpmenu.add(p.jpUtama);
            }
        });
        transbelbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpmenu.removeAll();
                jpmenu.revalidate();
                jpmenu.repaint();
                TransaksiPembelian p = new TransaksiPembelian();
                p.jpUtama.setVisible(true);
                jpmenu.revalidate();
                jpmenu.setLayout(new java.awt.BorderLayout());
                jpmenu.add(p.jpUtama);
            }
        });
        transjuabtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jpmenu.removeAll();
                jpmenu.revalidate();
                jpmenu.repaint();
                TransaksiPenjualan p = new TransaksiPenjualan();
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
        frame.setContentPane(new MenuPegawai().Menu);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(800,400);
    }
}
