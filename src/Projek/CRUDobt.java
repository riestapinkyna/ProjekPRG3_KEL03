package Projek;

import com.toedter.calendar.JDateChooser;
import connectionKel03.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CRUDobt {
    private JPanel jp2;
    public JPanel jpUtama;
    private JButton hapusButton;
    private JButton batalButton;
    private JButton simpanButton;
    private JButton editButton;
    private JRadioButton berdasarkanJenisRadioButton;
    private JRadioButton berdasarkanNamaRadioButton;
    private JTextField txtSearchNama;
    private JTable tblObat;
    private JComboBox cmbKategori;
    private JPanel JpTglKadaluarsa;
    private JRadioButton semuaDataRadioButton;
    private JComboBox cmbSearchJenis;
    private JComboBox cmbJenis;
    private JComboBox cmbSatuan;
    private JTextField txtNama;
    private JTextField txtHargaBeli;
    private JTextField txtHargaJual;
    private JTextField txtStock;
    private JTextField txtEfekSamping;
    private JTextField txtKemasan;
    private JComboBox cbProdusen;
    private JTextArea textArea1;
    private DefaultTableModel model;

    JDateChooser datechos = new JDateChooser();
    DBConnect connection = new DBConnect();

    String idObat;
    String jenis;
    String satuan;
    String namaObat;
    String kategori;
    String tglKadaluarsa;
    String hargaBeli;
    String hargaJual;
    String stock;
    String efekSamping;
    String kemasan;
    String cariJenis;
    String produsen;

    public CRUDobt() {
        //membuat tabel model
        model = new DefaultTableModel();
        tampilProdusen();
        tampilJenis();
        tampilSatuan();
        tblObat.setModel(model);
        JpTglKadaluarsa.add(datechos);
        cmbKategori.setSelectedItem(null);
        berdasarkanNamaRadioButton.setEnabled(false);
        berdasarkanJenisRadioButton.setEnabled(false);
        addColumn();

        semuaDataRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
                berdasarkanNamaRadioButton.setEnabled(false);
                berdasarkanJenisRadioButton.setEnabled(false);
            }
        });
        batalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (datechos == null || txtNama.getText().equals("") || txtKemasan.getText().equals("") || txtEfekSamping.getText().equals("")
                        || txtStock.getText().equals("") || txtHargaBeli.getText().equals("") || txtHargaJual.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Seluruh data wajib diisi!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    autokode();
                    try {
                        connection.stat = connection.conn.createStatement();
                        String sql = "SELECT * FROM cariJenis('"+ cmbJenis.getSelectedItem() +"')";
                        connection.result = connection.stat.executeQuery(sql);

                        while (connection.result.next()) {
                            jenis = connection.result.getString("id_jenis");
                        }

                        connection.stat = connection.conn.createStatement();
                        String sql2 = "SELECT * FROM Produsen('"+ cbProdusen.getSelectedItem() +"')";
                        connection.result = connection.stat.executeQuery(sql2);

                        while (connection.result.next()) {
                            produsen = connection.result.getString("id_produsen");
                        }

                        connection.stat = connection.conn.createStatement();
                        String sql3 = "SELECT * FROM cariSatuan('"+ cmbSatuan.getSelectedItem() +"')";
                        connection.result = connection.stat.executeQuery(sql3);

                        while (connection.result.next()) {
                            satuan = connection.result.getString("id_satuan");
                        }
                        connection.stat.close();
                        connection.result.close();
                    } catch (SQLException ex) {
                        System.out.println("Terjadi error saat insert3 " + ex);
                    }

                    if (cmbKategori.getSelectedItem() == "Resep") {
                        kategori = "1";
                    } else {
                        kategori = "2";
                    }

                    Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                    autokode();
                    namaObat = txtNama.getText();
                    tglKadaluarsa = formatter.format(datechos.getDate());
                    hargaBeli = txtHargaBeli.getText();
                    hargaJual = txtHargaJual.getText();
                    stock = txtStock.getText();
                    efekSamping = txtEfekSamping.getText();
                    kemasan = txtKemasan.getText();

                    try {
                        //INSERT ke tabel master
                        String sql2 = "EXEC sp_InputObat ?,?,?,?,?,?,?,?,?,?,?";
                        connection.pstat = connection.conn.prepareStatement(sql2);
                        connection.pstat.setString(1, idObat);
                        connection.pstat.setString(2, jenis);
                        connection.pstat.setString(3, satuan);
                        connection.pstat.setString(4, namaObat);
                        connection.pstat.setString(5, kategori);
                        connection.pstat.setString(6, tglKadaluarsa);
                        connection.pstat.setString(7, hargaBeli);
                        connection.pstat.setString(8, hargaJual);
                        connection.pstat.setString(9, stock);
                        connection.pstat.setString(10, efekSamping);
                        connection.pstat.setString(11, kemasan);
                        connection.pstat.setString(12, produsen);
                        connection.pstat.executeUpdate(); //insert ke tabel
                        //insert ke tabel detail, looping sebanyak row yang ada di layar
                        connection.pstat.close(); //close connection

                        JOptionPane.showMessageDialog(null, "Data berhasil disimpan dengan ID "+idObat, "Insert Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        clear();
                    } catch (SQLException ex) {
                        System.out.println("Terjadi error saat insert: " + ex);
                    }
                }
            }
        });
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //ambil id obat yang ada di detail obat
                try {
                    String query = "DELETE FROM tblObat WHERE id_obat=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, idObat);

                    int reply = JOptionPane.showConfirmDialog(null, "Apakah anda yakin akan menghapus?", "Hapus Data Obat", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        connection.pstat.executeUpdate(); //insert ke database
                        JOptionPane.showMessageDialog(null, "Hapus data berhasil!", "Hapus Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        clear();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Data batal dihapus!", "Hapus Data",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    connection.pstat.close();
                } catch (Exception ex) {
                    System.out.println("Terjadi error saat menghapus data obat: " + ex);
                }
            }
        });
        tblObat.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
                Date dateValue;

                int i = tblObat.getSelectedRow();
                if (i == -1) {
                    return;
                }

                if (model.getValueAt(i, 4).equals("1")) {
                    kategori = "Resep";
                } else {
                    kategori = "Non Resep";
                }

                try {
                    dateValue = dt.parse((String)model.getValueAt(i,5));
                    datechos.setDate(dateValue);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }

                idObat = (String) model.getValueAt(i, 0);
                cmbJenis.setSelectedItem(model.getValueAt(i, 1));
                cmbSatuan.setSelectedItem(model.getValueAt(i, 2));
                txtNama.setText((String) model.getValueAt(i, 3));
                cmbKategori.setSelectedItem(kategori);
                txtHargaBeli.setText((String) model.getValueAt(i, 6));
                txtHargaJual.setText((String) model.getValueAt(i, 7));
                txtStock.setText((String) model.getValueAt(i, 8));
                txtEfekSamping.setText((String) model.getValueAt(i, 9));
                txtKemasan.setText((String) model.getValueAt(i, 10));
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Format formatter = new SimpleDateFormat("yyyy-MM-dd");
                namaObat = txtNama.getText();
                tglKadaluarsa = formatter.format(datechos.getDate());
                hargaBeli = txtHargaBeli.getText();
                hargaJual = txtHargaJual.getText();
                stock = txtStock.getText();
                kategori = String.valueOf(cmbKategori.getSelectedItem());

                if (kategori.equals("Resep")) {
                    kategori = "1";
                } else {
                    kategori = "0";
                }

                try {
                    connection.stat = connection.conn.createStatement();
                    String sql = "SELECT * FROM cariJenis('"+ cmbJenis.getSelectedItem() +"')";
                    connection.result = connection.stat.executeQuery(sql);

                    while (connection.result.next()) {
                        jenis = connection.result.getString("id_jenis");
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (SQLException ex) {
                    System.out.println("Terjadi error saat insert " + ex);
                }

                try {
                    connection.stat = connection.conn.createStatement();
                    String sql = "SELECT * FROM cariSatuan('"+ cmbSatuan.getSelectedItem() +"')";
                    connection.result = connection.stat.executeQuery(sql);

                    while (connection.result.next()) {
                        jenis = connection.result.getString("id_satuan");
                    }

                    connection.stat.close();
                    connection.result.close();
                } catch (SQLException ex) {
                    System.out.println("Terjadi error saat insert " + ex);
                }

                try {
                    String query = "UPDATE tblObat SET id_jenis=?, id_satuan=?, nama=?, kategori=?," + "tgl_kadaluarsa=?, harga_beli=?, " +
                            "harga_jual=?, stock=?, efek_samping=?, kemasan=? WHERE id_obat=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, jenis);
                    connection.pstat.setString(2, satuan);
                    connection.pstat.setString(3, namaObat);
                    connection.pstat.setString(4, kategori);
                    connection.pstat.setString(5, tglKadaluarsa);
                    connection.pstat.setString(6, hargaBeli);
                    connection.pstat.setString(7, hargaJual);
                    connection.pstat.setString(8, stock);
                    connection.pstat.setString(9, efekSamping);
                    connection.pstat.setString(10, kemasan);
                    connection.pstat.setString(11, idObat);

                    int reply = JOptionPane.showConfirmDialog(null, "Apakah anda yakin akan mengupdate?", "Update Data Obat", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        connection.pstat.executeUpdate(); //insert ke database
                        JOptionPane.showMessageDialog(null, "Update data berhasil!", "Update Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        clear();
                    }
                    connection.pstat.close(); //menutup koneksi db
                } catch (Exception ec) {
                    System.out.println("Terjadi error saat update data obat: " + ec);
                }
            }
        });
        txtSearchNama.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                berdasarkanNamaRadioButton.setEnabled(true);
                berdasarkanJenisRadioButton.setEnabled(false);
                cmbSearchJenis.setSelectedItem(null);
            }
        });
        berdasarkanJenisRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showByJenis();
            }
        });
        berdasarkanNamaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showByName();
            }
        });
        cmbSearchJenis.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                berdasarkanJenisRadioButton.setEnabled(true);
                berdasarkanNamaRadioButton.setEnabled(false);
                txtSearchNama.setText("");
            }
        });
    }

    public void autokode()
    {
        try {
            String sql = "SELECT * FROM tblObat ORDER BY id_obat desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                idObat = connection.result.getString("id_obat");
                int a= Integer.parseInt(idObat.substring(idObat.length()-1))+1;
                String AN = ""+a;
                String nol = "";

                if (AN.length() == 1) {
                    nol = "00";
                } else if (AN.length() == 2) {
                    nol = "0";
                } else if (AN.length() == 3) {
                    nol = "";
                }
                idObat = "OBT" + nol + AN;
            } else {
                idObat = "OBT001";
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e1) {
            System.out.println("Terjadi error pada autokode: " + e1);
        }
    }
    public void tampilJenis() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT deskripsi FROM tblJenis";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                cmbJenis.addItem(connection.result.getString("deskripsi"));
                cmbSearchJenis.addItem(connection.result.getString("deskripsi"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data jenis obat: " + ex);
        }
    }

    public void tampilProdusen() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT nama FROM tblProdusen";
            connection.result = connection.stat.executeQuery(sql);
            while (connection.result.next()) {
                cbProdusen.addItem(connection.result.getString("nama"));
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data produsen obat: " + ex);
        }
    }

    public void tampilSatuan() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT deskripsi FROM tblSatuan";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                cmbSatuan.addItem(connection.result.getString("deskripsi"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data satuan obat: " + ex);
        }
    }

    public void addColumn() {
        model.addColumn("ID Obat");
        model.addColumn("Jenis Obat");
        model.addColumn("Satuan Obat");
        model.addColumn("Nama Obat");
        model.addColumn("Kategori");
        model.addColumn("Tgl Kadaluarsa");
        model.addColumn("Harga Beli");
        model.addColumn("Harga Jual");
        model.addColumn("Stock");
        model.addColumn("Efek Samping");
        model.addColumn("Kemasan");
    }

    public void loadData() {
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();

        //memberi tahu data telah kosong
        model.fireTableDataChanged();

        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT o.id_obat, j.deskripsi, s.deskripsi, o.nama, o.kategori, o.tgl_kadaluarsa, o.harga_beli, o.harga_jual, o.stock, o.efek_samping," +
                    "o.kemasan FROM tblObat AS o\n" +
                    "\tINNER JOIN tblJenis j on o.id_jenis = j.id_jenis" +
                    " INNER JOIN tblSatuan s on o.id_satuan = s.id_satuan";
            connection.result = connection.stat.executeQuery(query);

            //lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[11];
                obj[0] = connection.result.getString("id_obat");
                obj[1] = connection.result.getString("deskripsi");
                obj[2] = connection.result.getString("deskripsi");
                obj[3] = connection.result.getString("nama");
                obj[4] = connection.result.getString("kategori");
                obj[5] = connection.result.getString("tgl_kadaluarsa");
                obj[6] = connection.result.getString("harga_beli");
                obj[7] = connection.result.getString("harga_jual");
                obj[8] = connection.result.getString("stock");
                obj[9] = connection.result.getString("efek_samping");
                obj[10] = connection.result.getString("kemasan");
                model.addRow(obj);
            }
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Data masih kosong!", "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }

    public void showByJenis() {
        model.getDataVector().removeAllElements(); //menghapus seluruh data ditampilan

        model.fireTableDataChanged(); //memberi tahu data telah kosong

        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT * FROM cariJenis('"+ cmbSearchJenis.getSelectedItem() +"')";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                cariJenis = connection.result.getString("id_jenis");
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load: " + ex);
        }

        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT o.id_obat, j.deskripsi, s.deskripsi, o.nama, o.kategori, o.tgl_kadaluarsa, o.harga_beli, o.harga_jual, o.stock, o.efek_samping," +
                    "o.kemasan FROM tblObat AS o\n" +
                    "\tINNER JOIN tblJenis j on o.id_jenis = j.id_jenis" +
                    " INNER JOIN tblSatuan s on o.id_satuan = s.id_satuan WHERE o.id_jenis = '"+ cariJenis + "'";
            connection.result = connection.stat.executeQuery(query);
            // lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[11];
                obj[0] = connection.result.getString("id_obat");
                obj[1] = connection.result.getString("deskripsi");
                obj[2] = connection.result.getString("deskripsi");
                obj[3] = connection.result.getString("nama");
                obj[4] = connection.result.getString("kategori");
                obj[5] = connection.result.getString("tgl_kadaluarsa");
                obj[6] = connection.result.getString("harga_beli");
                obj[7] = connection.result.getString("harga_jual");
                obj[8] = connection.result.getString("stock");
                obj[9] = connection.result.getString("efek_samping");
                obj[10] = connection.result.getString("kemasan");
                model.addRow(obj);
            }
            // jika di tabel tidak ada daya yang di cari
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Data obat tidak ditemukan");
            }

            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }

    public void showByName() {
        model.getDataVector().removeAllElements(); //menghapus seluruh data ditampilan

        model.fireTableDataChanged(); //memberi tahu data telah kosong
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT o.id_obat, j.deskripsi, s.deskripsi, o.nama, o.kategori, o.tgl_kadaluarsa, o.harga_beli, o.harga_jual, o.stock, o.efek_samping," +
                    "o.kemasan FROM tblObat AS o\n" +
                    "\tINNER JOIN tblJenis j on o.id_jenis = j.id_jenis" +
                    " INNER JOIN tblSatuan s on o.id_satuan = s.id_satuan WHERE o.nama LIKE '%"+ txtSearchNama.getText() + "%'";
            connection.result = connection.stat.executeQuery(query);
            // lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[11];
                obj[0] = connection.result.getString("id_obat");
                obj[1] = connection.result.getString("deskripsi");
                obj[2] = connection.result.getString("deskripsi");
                obj[3] = connection.result.getString("nama");
                obj[4] = connection.result.getString("kategori");
                obj[5] = connection.result.getString("tgl_kadaluarsa");
                obj[6] = connection.result.getString("harga_beli");
                obj[7] = connection.result.getString("harga_jual");
                obj[8] = connection.result.getString("stock");
                obj[9] = connection.result.getString("efek_samping");
                obj[10] = connection.result.getString("kemasan");
                model.addRow(obj);
            }
            // jika di tabel tidak ada daya yang di cari
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Data tidak ditemukan!", "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data obat: " + e);
        }
    }

    public void clear() {
        txtEfekSamping.setText("");
        txtHargaBeli.setText("");
        txtKemasan.setText("");
        txtHargaJual.setText("");
        txtNama.setText("");
        txtSearchNama.setText("");
        txtStock.setText("");
        cmbJenis.setSelectedItem(null);
        cmbKategori.setSelectedItem(null);
        cmbSatuan.setSelectedItem(null);
        cmbSearchJenis.setSelectedItem(null);
        berdasarkanNamaRadioButton.setEnabled(false);
        berdasarkanJenisRadioButton.setEnabled(false);
        datechos.setCalendar(null);

        tblObat.getModel();
        if (tblObat.getRowCount() > 0) {
            for (int i = tblObat.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CRUD Obat");
        frame.setContentPane(new CRUDobt().jpUtama);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700, 400);
    }
}
