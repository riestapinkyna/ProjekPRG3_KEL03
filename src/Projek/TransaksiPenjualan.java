package Projek;

import connectionKel03.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TransaksiPenjualan {
    private JTextField txtid;
    private JButton cariNamaObatButton;
    private JTextField txtobatcari;
    private JTable tbl;
    private JButton tambahKeKeranjangButton;
    private JRadioButton yaRadioButton;
    private JRadioButton tidakRadioButton;
    private JTextField txtidpas;
    private JTextField txthargatotal;
    private JTextField txtuang;
    private JTextField txtkembali;
    public JPanel jpUtama;
    private JTextField txttanggal;
    private JTextField txtnamapas;
    private JComboBox cbnamdok;
    private JTextField txtidobat;
    private JTextField txtnamaobat;
    private JTextField txthargaobat;
    private JButton simpanbtn;
    private JButton totalbelanja;
    private JTextField txtkuantitas;
    private DefaultTableModel model;
    String idtrans, idpas, namapas, dokter, idobat, idkar, tanggal;
    double totalbel;
    int stock, stock2;
    String tgl;
    DBConnect connection = new DBConnect();
    public TransaksiPenjualan()
    {
        tampildokter();
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        txttanggal.setText(formatter.format(calendar.getTime()));
        yaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtnamapas.setEnabled(true);
                cbnamdok.setEnabled(true);
            }
        });
        cariNamaObatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                namapas = txtobatcari.getText();
                try {
                    connection.stat = connection.conn.createStatement();
                    String query = "SELECT * FROM tblObat WHERE  deskripsi LIKE '%" + namapas + "%'";
                    connection.result = connection.stat.executeQuery(query);
                    //lakukan perbaris data
                    if (connection.result.next()) {
                        txtidpas.setText(connection.result.getString("id_obat"));
                        txtnamapas.setText(connection.result.getString("nama"));
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Data obat tidak ditemukan!");
                    }
                } catch (Exception ex) {
                    System.out.println("Terjadi error saat load data: " + ex);
                }
            }
        });
        tambahKeKeranjangButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] obj = new Object[3];
                obj[0] = txtidobat.getText();
                obj[1] = txtnamaobat.getText();
                obj[2] = txthargaobat.getText();
                obj[3] = txtkuantitas.getText();
                model.addRow(obj);
            }
        });
        totalbelanja.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double temp= 0.0;
                int i = tbl.getSelectedRow();
                if(i == -1) { //jika tidak ada baris terseleksi
                    return;
                }
                //mengetahui berapa banyak baris tabelBuku di layar
                int j = tbl.getModel().getRowCount();

                //menghitung Total = sum of (harga*jumlah)
                for(int k = 0; k < j; k++) {
                    //menghitung nilai harga*jumlah setiap baris
                    temp = (Double.parseDouble((String) model.getValueAt(k, 3))) * (Double.parseDouble((String) model.getValueAt(k, 4)));
                    totalbel = totalbel + temp;
                }
                txthargatotal.setText(String.valueOf(totalbel));
            }
        });
        txtuang.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char karakter = e.getKeyChar();
                if(!(Character.isDigit(karakter))) {
                    e.consume();
                }
            }
        });
        txtkembali.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                double ttl1, kembali = 0;
                if (txtuang.getText().equals("")) {
                    return;
                } else {
                    if(Double.parseDouble(txtuang.getText()) < totalbel)
                    {
                        JOptionPane.showMessageDialog(null, "Jumlah uang kurang !", "Warning", JOptionPane.WARNING_MESSAGE);
                        txtuang.setText("");
                    }
                    else {
                        kembali = Double.parseDouble(txtuang.getText()) - totalbel;
                        txtkembali.setText(Double.toString(kembali));
                    }
                }
            }
        });
        simpanbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    connection.stat = connection.conn.createStatement();
                    String sql = "SELECT id_karyawan FROM peg_aktif";
                    connection.result = connection.stat.executeQuery(sql);
                    while (connection.result.next()) {
                        idkar = (String) connection.result.getString("id_karyawan");
                    }
                } catch (SQLException ex) {
                    System.out.println("Terjadi error saat insert sp1" + ex);
                }
                idtrans = txtid.getText();
                totalbel = Double.parseDouble(txthargatotal.getText());
                tgl = txttanggal.getText();
                idpas = txtidpas.getText();
                namapas = txtnamapas.getText();
                if (tidakRadioButton.isSelected()) {
                    try {
                        //INSERT ke tabel master
                        String sql2 = "EXEC sp_InputTransaksi2 ?,?,?,?,?,?";
                        connection.pstat = connection.conn.prepareStatement(sql2);
                        connection.pstat.setString(1, idtrans);
                        connection.pstat.setString(2, idkar);
                        connection.pstat.setInt(3, 0);
                        connection.pstat.setDouble(4, totalbel);
                        connection.pstat.setString(5, tgl);
                        connection.pstat.setInt(3, 1);
                        connection.pstat.executeUpdate(); //insert ke tabel master
                    } catch (SQLException ex) {
                        System.out.println("Terjadi error saat insert sp2" + ex);
                    }
                } else {
                    try {
                        connection.stat = connection.conn.createStatement();
                        String sql = "SELECT id_dokter FROM tblDokter WHERE nama = '" + cbnamdok.getSelectedItem() + "'";
                        connection.result = connection.stat.executeQuery(sql);

                        while (connection.result.next()) {
                            dokter = (String) connection.result.getString("id_dokter");
                        }

                        //INSERT ke tabel master
                        String sql2 = "EXEC sp_InputTransaksi ?,?,?,?,?,?,?,?";
                        connection.pstat = connection.conn.prepareStatement(sql2);
                        connection.pstat.setString(1, idtrans);
                        connection.pstat.setString(2, dokter);
                        connection.pstat.setString(3, idkar);
                        connection.pstat.setString(4, idpas);
                        connection.pstat.setString(5, namapas);
                        connection.pstat.setInt(6, 0);
                        connection.pstat.setDouble(7, totalbel);
                        connection.pstat.setString(8, tgl);
                        connection.pstat.setInt(9, 1);
                        connection.pstat.executeUpdate(); //insert ke tabel master
                    }
                    //insert ke tabel detail, looping sebanyak row yang ada di layar
                    catch (SQLException ex) {
                        System.out.println("Terjadi error saat insert sp" + ex);
                    }
                }
                try {
                    int j = tbl.getModel().getRowCount();
                    for (int k = 0; k < j; k++) {
                        Double harga = (Double.parseDouble((String) model.getValueAt(k, 3))) * (Double.parseDouble((String) model.getValueAt(k, 4)));
                        String sql3 = "INSERT INTO tblDetilTransaksiPenjualan VALUES (?, ?, ?, ?)";
                        connection.pstat = connection.conn.prepareStatement(sql3);
                        connection.pstat.setString(1, idtrans);
                        connection.pstat.setString(2, (String) model.getValueAt(k, 0)); //kodebuku
                        connection.pstat.setString(3, (String) model.getValueAt(k, 4)); //jumlah beli
                        connection.pstat.setString(4, (String.valueOf(harga))); //jumlah * harga
                        connection.pstat.executeUpdate(); //insert tabel detilBeli

                        //mencari nilai stock ditabel buku saat ini dan menabmahkan dengan nilai di inputan
                        String sql4 = "SELECT stock FROM tblObat WHERE id_obat = '" + (String) model.getValueAt(k, 0) + "'";
                        connection.result = connection.stat.executeQuery(sql4);
                        while (connection.result.next()) {
                            stock = connection.result.getInt("stock");
                            stock2 = Integer.parseInt((String) model.getValueAt(k, 3)) - stock;
                        }

                        //update stack di tabel buku
                        String sql5 = "UPDATE tblObat SET stock = ? WHERE id_obat =?";
                        connection.pstat = connection.conn.prepareStatement(sql5);
                        connection.pstat.setString(1, String.valueOf(stock2));
                        connection.pstat.setString(2, (String) model.getValueAt(k, 0));
                        connection.pstat.executeUpdate(); //update tabel buku
                    }

                    connection.pstat.close(); //close connection
                }// catch (SQLException ex) {
                catch (SQLException ex) {
                    System.out.println("Terjadi error saat insert sp" + ex);
                }
                JOptionPane.showMessageDialog(null, "Insert data Buku Berhasil");
            }
        });
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Transaksi Obat");
        frame.setContentPane(new TransaksiPenjualan().jpUtama);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700, 400);
    }
    public void tampildokter() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql1 = "SELECT nama FROM tblDokter";
            connection.result = connection.stat.executeQuery(sql1);

            while (connection.result.next()) {
                cbnamdok.addItem(connection.result.getString("nama"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load nama dokter" + ex);
        }
    }
    public void autokode1(){
        try {
            String sql = "SELECT * FROM tblPasien ORDER BY id_pasien desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                idpas = connection.result.getString("id_pasien").substring(4);
                String AN = "" + (Integer.parseInt(idpas) + 1);
                String nol = "";

                if (AN.length() == 1) {
                    nol = "00";
                } else if (AN.length() == 2) {
                    nol = "0";
                } else if (AN.length() == 3) {
                    nol = "";
                }
                txtidpas.setText("PS" + nol + AN);
                txtidpas.setEnabled(false);

            } else {
                txtidpas.setText("PS001");
                txtidpas.setEnabled(false);
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e1) {
            System.out.println("Terjadi error pada autokode1: " + e1);
        }
    }
    public void autokode2() {
        try {
            String sql = "SELECT * FROM tblTransaksiPenjualan ORDER BY id_transaksi desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                idtrans = connection.result.getString("id_transaksi").substring(4);
                String AN = "" + (Integer.parseInt(idtrans) + 1);
                String nol = "";

                if (AN.length() == 1) {
                    nol = "00";
                } else if (AN.length() == 2) {
                    nol = "0";
                } else if (AN.length() == 3) {
                    nol = "";
                }
                txtid.setText("TRS" + nol + AN);
                txtid.setEnabled(false);

            } else {
                txtid.setText("TRS001");
                txtid.setEnabled(false);
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e1) {
            System.out.println("Terjadi error pada autokode2: " + e1);
        }
    }
}
