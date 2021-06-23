package Projek;

import javax.swing.*;

public class TransaksiPembelian {
    public JPanel jpUtama;
    private JPanel jp2;
    private JTextField txtid;
    private JTextArea textArea2;
    private JComboBox comboBox1;
    private JButton tambahKeKeranjangButton;
    private JTable table1;
    private JTextField txtobatcari;
    private JButton cariNamaObatButton;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pembelian Buku");
        frame.setContentPane(new TransaksiPembelian().jpUtama);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500, 600);
    }
}
