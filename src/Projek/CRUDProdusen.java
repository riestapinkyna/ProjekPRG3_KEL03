package Projek;

import connectionKel03.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class CRUDProdusen {
    private JPanel jp2;
    private JTextField txtnama;
    private JTextArea textArea1;
    private JTextArea textArea2;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton simpanButton;
    private JButton editButton;
    private JRadioButton berdasarkanIDRadioButton;
    private JRadioButton berdasarkanNamaRadioButton;
    private JTextField txtidcari;
    private JTextField txtnamacari;
    private JTable tbl;
    private JRadioButton seluruhDataRadioButton;
    private JTextField txtalamat;
    private JTextField txtno;
    public JPanel jpUtama;
    String id;
    String nama;
    String alamat;
    String no;
    DBConnect connection = new DBConnect(); //membuat objek dari class DBConnect
    private DefaultTableModel model;
    public CRUDProdusen()
    {
        editButton.setEnabled(false);
        hapusButton.setEnabled(false);
        model = new DefaultTableModel();
        tbl.setModel(model);
        addColumn();
        berdasarkanIDRadioButton.setEnabled(false);
        berdasarkanNamaRadioButton.setEnabled(false);
        simpanButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nama = txtnama.getText();
                    alamat = txtalamat.getText();
                    no = txtno.getText();
                    if (nama.equals("") || alamat.equals("") || no.equals("")) {
                        JOptionPane.showMessageDialog(null, "Seluruh data wajib diisi!", "Peringatan",
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        autokode();
                        try {
                            //INSERT ke tabel master
                            String sql2 = "EXEC sp_InputProdusen ?,?,?,?";
                            connection.pstat = connection.conn.prepareStatement(sql2);
                            connection.pstat.setString(1, id);
                            connection.pstat.setString(2, nama);
                            connection.pstat.setString(3, alamat);
                            connection.pstat.setString(4, no);
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
                nama = txtnama.getText();
                alamat = txtalamat.getText();
                no = txtno.getText();
                if (id.equals("") || nama.equals("") || alamat.equals("") || no.equals("")) {
                    JOptionPane.showMessageDialog(null, "Seluruh data wajib diisi!");
                } else {
                    try {
                        String query = "UPDATE tblProdusen SET nama=?, alamat=?, no_telp = ? WHERE id_produsen =?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, nama);
                        connection.pstat.setString(2, alamat);
                        connection.pstat.setString(3, no);
                        connection.pstat.setString(4, id);
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
                        String query = "DELETE FROM tblProdusen WHERE id_produsen=?";
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
        txtno.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char karakter = e.getKeyChar();
                if(!(Character.isDigit(karakter))) {
                    e.consume();
                }
                //no telp maksimal 13 digit
                if (txtno.getText().length()>12)
                {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Digit maksimal no telepon adalah 13!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }
                else
                {
                    no = txtno.getText();
                }
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
                txtnama.setText((String) model.getValueAt(i, 1));
                txtalamat.setText((String) model.getValueAt(i, 2));
                txtno.setText((String) model.getValueAt(i,3));
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
        berdasarkanNamaRadioButton.addActionListener(new ActionListener() {
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
                txtnamacari.setText("");
            }
        });
        txtnamacari.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                berdasarkanNamaRadioButton.setEnabled(true);
                txtidcari.setText("");
            }
        });
    }
    public void showByID(){
        id= txtidcari.getText();
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM cariprodusen('"+id+"')";
            connection.result = connection.stat.executeQuery(query);

            //lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                model.addRow(obj);
            }

            //jika di tabel tidak ada data yang dicari
            if(model.getRowCount() == 0)
            {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan!", "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data: " + e);
        }
    }
    public void showByJenis(){
        nama=txtnamacari.getText();
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblProdusen WHERE  nama LIKE '%" + nama + "%'";
            connection.result = connection.stat.executeQuery(query);

            //lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                model.addRow(obj);
            }

            //jika di tabel tidak ada data yang dicari
            if(model.getRowCount() == 0)
            {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan!", "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data: " + e);
        }
    }
    public void showSemuaData(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblProdusen";
            connection.result = connection.stat.executeQuery(query);

            //lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[4];
                obj[0] = connection.result.getString(1);
                obj[1] = connection.result.getString(2);
                obj[2] = connection.result.getString(3);
                obj[3] = connection.result.getString(4);
                model.addRow(obj);
            }
            //jika di tabel tidak ada data yang dicari
            if(model.getRowCount() == 0)
            {
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
            String sql = "SELECT * FROM tblProdusen ORDER BY id_produsen desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                id = connection.result.getString("id_produsen");
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
                id = "PD" + nol + AN;

            } else {
                id = "PD001";
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e1) {
            System.out.println("Terjadi error pada autokode: " + e1);
        }
    }
    public void addColumn() {
        model.addColumn("Id Produsen");
        model.addColumn("Nama");
        model.addColumn("Alamat");
        model.addColumn("No Telepon");
    }

    public void clear() {
        txtnama.setText("");
        txtalamat.setText("");
        txtno.setText("");
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CRUD Produsen");
        frame.setContentPane(new CRUDProdusen().jpUtama);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700, 400);
    }
}
