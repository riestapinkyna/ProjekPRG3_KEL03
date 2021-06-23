package Projek;

import connectionKel03.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class CRUDSatuan {
    private JPanel jp2;
    private JTextField txtsatuan;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton simpanButton;
    private JButton editButton;
    private JRadioButton berdasarkanIDRadioButton;
    private JRadioButton berdasarkanSatuanRadioButton;
    private JTextField txtidcari;
    private JTextField txtsatuancari;
    private JTable tbl;
    public JPanel jpUtama;
    private JRadioButton seluruhDataRadioButton;
    private DefaultTableModel model;
    String id;
    String deskripsi;
    DBConnect connection = new DBConnect(); //membuat objek dari class DBConnect
    public CRUDSatuan() {
        editButton.setEnabled(false);
        hapusButton.setEnabled(false);
        model = new DefaultTableModel();
        tbl.setModel(model);
        addColumn();
        berdasarkanIDRadioButton.setEnabled(false);
        berdasarkanSatuanRadioButton.setEnabled(false);
        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deskripsi=txtsatuan.getText();
                if (deskripsi.equals("")) {
                    JOptionPane.showMessageDialog(null, "Seluruh data wajib diisi!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    autokode();
                    try {
                        //INSERT ke tabel master
                        String sql2 = "EXEC sp_InputSatuan ?,?";
                        connection.pstat = connection.conn.prepareStatement(sql2);
                        connection.pstat.setString(1, id);
                        connection.pstat.setString(2, deskripsi);
                        connection.pstat.executeUpdate(); //insert ke tabel
                        //insert ke tabel detail, looping sebanyak row yang ada di layar
                        connection.pstat.close(); //close connection
                    } catch (SQLException ex) {
                        System.out.println("Terjadi error saat insert " + ex);
                    }

                    JOptionPane.showMessageDialog(null, "Data berhasil disimpan dengan ID "+id, "Insert Data",
                            JOptionPane.INFORMATION_MESSAGE);
                    clear();
                    showSemuaData();
                    editButton.setEnabled(false);
                    hapusButton.setEnabled(false);
                    simpanButton.setEnabled(true);
                }
            }
        });
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    deskripsi = txtsatuan.getText();
                    if (id.equals("") || deskripsi.equals("")) {
                        JOptionPane.showMessageDialog(null, "Seluruh data wajib diisi!", "Peringatan",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        try {
                            String query = "UPDATE tblSatuan SET deskripsi=? WHERE id_satuan =?";
                            connection.pstat = connection.conn.prepareStatement(query);
                            connection.pstat.setString(1, deskripsi);
                            connection.pstat.setString(2, id);
                            connection.pstat.executeUpdate(); //insert ke database
                            connection.pstat.close(); //menutup koneksi db
                        } catch (Exception ec) {
                            System.out.println("Terjadi error saat update data " + ec);
                        }
                        JOptionPane.showMessageDialog(null, "Update data berhasil!", "Update Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        clear();
                        showSemuaData();
                        editButton.setEnabled(false);
                        hapusButton.setEnabled(false);
                        simpanButton.setEnabled(true);
                    }
                }
            });
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog(null, "Apakah anda yakin akan menghapus data ini?", "Hapus Data", dialogButton);
                if(dialogResult == 0) {
                    try {
                        String query = "DELETE FROM tblSatuan WHERE id_satuan=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, id);
                        connection.pstat.executeUpdate(); //insert ke database
                        connection.pstat.close(); //menutup koneksi db
                    }
                    catch (Exception ec)
                    {
                        System.out.println("Terjadi error saat hapus data" + ec);
                    }
                    JOptionPane.showMessageDialog(null, "Hapus data berhasil!", "Hapus Data",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Data batal dihapus!", "Hapus Data",
                            JOptionPane.INFORMATION_MESSAGE);
                }
                clear();
                showSemuaData();
                editButton.setEnabled(false);
                hapusButton.setEnabled(false);
                simpanButton.setEnabled(true);
            }
        });
        batalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
                editButton.setEnabled(false);
                hapusButton.setEnabled(false);
                simpanButton.setEnabled(true);
            }
        });
        tbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tbl.getSelectedRow();
                if(i == -1){
                    return;
                }
                id = (String) model.getValueAt(i, 0);
                txtsatuan.setText((String) model.getValueAt(i, 1));
                editButton.setEnabled(true);
                hapusButton.setEnabled(true);
                simpanButton.setEnabled(false);
            }
        });
        seluruhDataRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showSemuaData();
            }
        });
        berdasarkanSatuanRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showByJenis();
            }
        });
        berdasarkanIDRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showByID();
            }
        });
        txtidcari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                berdasarkanIDRadioButton.setEnabled(true);
                txtsatuancari.setText("");
            }
        });
        txtsatuancari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                berdasarkanSatuanRadioButton.setEnabled(true);
            }
        });
    }
    public void showByID(){
        id=txtidcari.getText();
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM carisatuan('"+id+"')";
            connection.result = connection.stat.executeQuery(query);

            //lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[2];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                model.addRow(obj);
            }

            //jika di tabel tidak ada data yang dicari
            if(model.getRowCount() == 0)
            {
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Data tidak ditemukan!", "Informasi",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data: " + e);
        }
    }
    public void showByJenis(){
        deskripsi=txtsatuancari.getText();
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblSatuan WHERE  deskripsi LIKE '%" + deskripsi + "%'";
            connection.result = connection.stat.executeQuery(query);

            //lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[2];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                model.addRow(obj);
            }

            //jika di tabel tidak ada data yang dicari
            if(model.getRowCount() == 0)
            {
                if (model.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(null, "Data tidak ditemukan!", "Informasi",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data: " + e);
        }
    }
    public void showSemuaData () {
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblSatuan";
            connection.result = connection.stat.executeQuery(query);

            //lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[2];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                model.addRow(obj);
            }

            //jika di tabel tidak ada data yang dicari
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Data masih kosong!", "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data: " + e);
        }
    }
    public void autokode() {
        try {
            String sql = "SELECT * FROM tblSatuan ORDER BY id_satuan desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                id = connection.result.getString("id_satuan");
                int a= Integer.parseInt(id.substring(id.length()-1))+1;
                String AN = ""+a;
                String nol = "";

                if (AN.length() == 1) {
                    nol = "00";
                } else if (AN.length() == 2) {
                    nol = "0";
                } else if (AN.length() == 3) {
                    nol = "";
                }
                id = "ST" + nol + AN;

            } else {
                id = "ST001";
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e1) {
            System.out.println("Terjadi error pada autokode: " + e1);
        }
    }

    public void addColumn() {
        model.addColumn("Id Satuan");
        model.addColumn("Nama Satuan");
    }

    public void clear() {
        txtsatuan.setText("");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CRUD Satuan");
        frame.setContentPane(new CRUDSatuan().jpUtama);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700, 400);
    }
}
