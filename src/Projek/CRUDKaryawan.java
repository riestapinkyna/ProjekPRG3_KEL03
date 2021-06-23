package Projek;

import connectionKel03.DBConnect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class CRUDKaryawan {
    private JPanel jp2;
    private JTextArea textArea2;
    private JButton hapusButton;
    private JButton simpanButton;
    private JRadioButton berdasarkanStatusRadioButton;
    private JRadioButton berdasarkanNamaRadioButton;
    private JTextField txtSearchID;
    private JTextField txtSearchNama;
    private JTable tblKaryawan;
    private JComboBox cmbJabatan;
    public JPanel jpUtama;
    private JTextField txtNIK;
    private JTextField txtNama;
    private JTextField txtAlamat;
    private JTextField txtNoTelp;
    private JTextField txtUsername;
    private JComboBox cmbStatus;
    private JButton editButton;
    private JButton batalButton;
    private JRadioButton semuaDataRadioButton;
    private JComboBox cmbSearchStatus;
    private JPasswordField txtpw1;
    private JPasswordField txtpw2;
    private DefaultTableModel model;

    DBConnect connection = new DBConnect();

    String idKaryawan;
    String idJabatan;
    String nik;
    String nama;
    String alamat;
    String noTelp;
    String username;
    String password;
    String status;
    String searchStatus;
    String jabatan;

    public CRUDKaryawan() {
        //membuat tabel model
        model = new DefaultTableModel();
        tampilJabatan();
        tblKaryawan.setModel(model);
        cmbJabatan.setSelectedItem(null);
        cmbStatus.setSelectedItem(null);
        cmbSearchStatus.setSelectedItem(null);
        loadData();
        berdasarkanNamaRadioButton.setEnabled(false);
        berdasarkanStatusRadioButton.setEnabled(false);
        addColumn();

        semuaDataRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadData();
                berdasarkanNamaRadioButton.setEnabled(false);
                berdasarkanStatusRadioButton.setEnabled(false);
                cmbSearchStatus.setSelectedItem(null);
                txtSearchNama.setText("");
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
                if (txtNoTelp.getText().equals("") || txtNama.getText().equals("") || txtNIK.getText().equals("") || txtAlamat.getText().equals("") || txtpw1.getText().equals("") || txtpw2.getText().equals("") || txtUsername.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Seluruh data wajib diisi!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                } else if (!txtpw1.getText().equals(txtpw2.getText()))
                {
                    JOptionPane.showMessageDialog(null, "Password belum sama!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                    txtpw2.setText("");
                }
                else {
                    autokode();
                    try {
                        connection.stat = connection.conn.createStatement();
                        String sql = "SELECT * FROM carijabatan2('"+ cmbJabatan.getSelectedItem() +"')";
                        connection.result = connection.stat.executeQuery(sql);

                        while (connection.result.next()) {
                            idJabatan = connection.result.getString("id_jabatan");
                        }

                        connection.stat.close();
                        connection.result.close();
                    } catch (SQLException ex) {
                        System.out.println("Terjadi error saat insert " + ex);
                    }
                    nik = txtNIK.getText();
                    nama = txtNama.getText();
                    alamat = txtAlamat.getText();
                    noTelp = txtNoTelp.getText();
                    username = txtUsername.getText();
                    password = txtpw1.getText();
                    if (cmbStatus.getSelectedItem() == "Aktif") {
                        status = "1";
                    } else {
                        status = "0";
                    }
                    try {
                        //INSERT ke tabel master
                        String sql2 = "EXEC sp_InputKaryawan ?,?,?,?,?,?,?,?,?";
                        connection.pstat = connection.conn.prepareStatement(sql2);
                        connection.pstat.setString(1, idKaryawan);
                        connection.pstat.setString(2, idJabatan);
                        connection.pstat.setString(3, nik);
                        connection.pstat.setString(4, nama);
                        connection.pstat.setString(5, alamat);
                        connection.pstat.setString(6, noTelp);
                        connection.pstat.setString(7, username);
                        connection.pstat.setString(8, password);
                        connection.pstat.setString(9, status);
                        connection.pstat.executeUpdate(); //insert ke tabel
                        //insert ke tabel detail, looping sebanyak row yang ada di layar
                        connection.pstat.close(); //close connection

                        JOptionPane.showMessageDialog(null, "Data berhasil disimpan dengan ID "+idKaryawan, "Insert Data",
                                JOptionPane.INFORMATION_MESSAGE);
                        clear();
                        loadData();
                    } catch (SQLException ex) {
                        System.out.println("Terjadi error saat insert " + ex);
                    }
                }
            }
        });
        berdasarkanNamaRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showByName();
            }
        });
        txtSearchNama.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                berdasarkanNamaRadioButton.setEnabled(true);
                berdasarkanStatusRadioButton.setEnabled(false);
                cmbSearchStatus.setSelectedItem(null);
            }
        });
        cmbSearchStatus.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                berdasarkanStatusRadioButton.setEnabled(true);
                berdasarkanNamaRadioButton.setEnabled(false);
                txtSearchNama.setText("");
            }
        });
        berdasarkanStatusRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showByStatus();
            }
        });
        tblKaryawan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                int i = tblKaryawan.getSelectedRow();
                if (i == -1) {
                    return;
                }

                if (model.getValueAt(i, 7).equals("1")) {
                    searchStatus = "Aktif";
                } else {
                    searchStatus = "Tidak Aktif";
                }

                cmbStatus.setEnabled(true);
                idKaryawan = (String) model.getValueAt(i, 0);
                cmbJabatan.setSelectedItem(model.getValueAt(i, 1));
                txtNIK.setText((String) model.getValueAt(i, 2));
                txtNama.setText((String) model.getValueAt(i, 3));
                txtAlamat.setText((String) model.getValueAt(i, 4));
                txtNoTelp.setText((String) model.getValueAt(i, 5));
                txtUsername.setText((String) model.getValueAt(i, 6));
                cmbStatus.setSelectedItem(searchStatus);
            }
        });
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //ambil id karyawan yang ada di detail karyawan
                try {
                    String query = "DELETE FROM tblKaryawan WHERE id_karyawan=?";
                    connection.pstat = connection.conn.prepareStatement(query);
                    connection.pstat.setString(1, idKaryawan);

                    int reply = JOptionPane.showConfirmDialog(null, "Apakah anda yakin akan menghapus?", "Hapus Data Karyawan", JOptionPane.YES_NO_OPTION);
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
                    System.out.println("Terjadi error saat menghapus data karyawan: " + ex);
                }
            }
        });
        txtNoTelp.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char karakter = e.getKeyChar();
                if(!(Character.isDigit(karakter))) {
                    e.consume();
                }
                //no telp maksimal 13 digit
                if (txtNoTelp.getText().length()>12)
                {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Digit maksimal no telepon adalah 13!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        txtNIK.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char karakter = e.getKeyChar();
                if(!(Character.isDigit(karakter))) {
                    e.consume();
                }
                //no telp maksimal 13 digit
                if (txtNIK.getText().length()>15)
                {
                    e.consume();
                    JOptionPane.showMessageDialog(null, "Digit maksimal NIK adalah 16!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtNoTelp.getText().equals("") || txtNama.getText().equals("") || txtNIK.getText().equals("") || txtAlamat.getText().equals("") || txtpw1.getText().equals("") || txtpw2.getText().equals("") ||txtUsername.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Seluruh data wajib diisi!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                }
                else if (!txtpw1.getText().equals(txtpw2.getText()))
                {
                    JOptionPane.showMessageDialog(null, "Password belum sama!", "Peringatan",
                            JOptionPane.WARNING_MESSAGE);
                    txtpw2.setText("");
                }else {
                    nik = txtNIK.getText();
                    nama = txtNama.getText();
                    alamat = txtAlamat.getText();
                    noTelp = txtNoTelp.getText();
                    username = txtUsername.getText();
                    password = txtpw1.getText();
                    status = String.valueOf(cmbStatus.getSelectedItem());

                    if (status.equals("Aktif")) {
                        status = "1";
                    } else {
                        status = "0";
                    }
                    try {
                        connection.stat = connection.conn.createStatement();
                        String sql = "SELECT * FROM carijabatan2('" + cmbJabatan.getSelectedItem().toString() + "')";
                        connection.result = connection.stat.executeQuery(sql);

                        while (connection.result.next()) {
                            idJabatan = connection.result.getString("id_jabatan");
                        }

                        connection.stat.close();
                        connection.result.close();
                    } catch (SQLException ex) {
                        System.out.println("Terjadi error saat insert " + ex);
                    }

                    try {
                        String query = "UPDATE tblKaryawan SET id_jabatan=?, nik=?, nama=?, alamat=?, no_hp=?," + "username=?, password=?, " +
                                "status=? WHERE id_karyawan=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, idJabatan);
                        connection.pstat.setString(2, nik);
                        connection.pstat.setString(3, nama);
                        connection.pstat.setString(4, alamat);
                        connection.pstat.setString(5, noTelp);
                        connection.pstat.setString(6, username);
                        connection.pstat.setString(7, password);
                        connection.pstat.setString(8, status);
                        connection.pstat.setString(9, idKaryawan);

                        int reply = JOptionPane.showConfirmDialog(null, "Apakah anda yakin akan mengupdate?", "Update Data Karyawan", JOptionPane.YES_NO_OPTION);
                        if (reply == JOptionPane.YES_OPTION) {
                            connection.pstat.executeUpdate(); //insert ke database
                            JOptionPane.showMessageDialog(null, "Update data berhasil!", "Update Data",
                                    JOptionPane.INFORMATION_MESSAGE);
                            clear();
                            loadData();
                        }
                        connection.pstat.close(); //menutup koneksi db
                    } catch (Exception ec) {
                        System.out.println("Terjadi error saat update data karyawan: " + ec);
                    }
                }
            }
        });
        txtNama.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                char c = e.getKeyChar();
                if(Character.isLetter(c) || Character.isWhitespace(c) || Character.isISOControl(c)) {
                    txtNama.setEditable(true);
                } else {
                    txtNama.setEditable(false);
                }
            }
        });
        txtNIK.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                char c = e.getKeyChar();
                if(Character.isDigit(c) || Character.isISOControl(c)) {
                    txtNIK.setEditable(true);
                } else {
                    txtNIK.setEditable(false);
                }
            }
        });
        txtNoTelp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);

                char c = e.getKeyChar();
                if(Character.isDigit(c) || Character.isISOControl(c)) {
                    txtNoTelp.setEditable(true);
                } else {
                    txtNoTelp.setEditable(false);
                }
            }
        });
    }

    public void tampilJabatan() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT deskripsi_jabatan FROM tblJabatan";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                cmbJabatan.addItem(connection.result.getString("deskripsi_jabatan"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException ex) {
            System.out.println("Terjadi error saat load data karyawan: " + ex);
        }
    }

    public void showByName() {
        model.getDataVector().removeAllElements(); //menghapus seluruh data ditampilan

        model.fireTableDataChanged(); //memberi tahu data telah kosong
        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblKaryawan WHERE nama LIKE '%" + txtSearchNama.getText() + "%'";
            connection.result = connection.stat.executeQuery(query);
            // lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[8];
                obj[0] = connection.result.getString("id_karyawan");
                obj[1] = connection.result.getString("id_jabatan");
                obj[2] = connection.result.getString("nik");
                obj[3] = connection.result.getString("nama");
                obj[4] = connection.result.getString("alamat");
                obj[5] = connection.result.getString("no_hp");
                obj[6] = connection.result.getString("username");
                String status = connection.result.getString("status");
                if(status.equals("1"))
                {
                    obj[7] = "Aktif";
                }
                else
                {
                    obj[7] = "Tidak Aktif";
                }
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
            System.out.println("Terjadi error saat load data karyawan: " + e);
        }
    }

    public void showByStatus() {
        model.getDataVector().removeAllElements(); //menghapus seluruh data ditampilan

        model.fireTableDataChanged(); //memberi tahu data telah kosong

        if (cmbSearchStatus.getSelectedItem() == "Aktif") {
            searchStatus = "1";
        } else {
            searchStatus = "0";
        }

        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT * FROM tblKaryawan WHERE status = '" + searchStatus + "'";
            connection.result = connection.stat.executeQuery(query);
            // lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[8];
                obj[0] = connection.result.getString("id_karyawan");
                obj[1] = connection.result.getString("id_jabatan");
                obj[2] = connection.result.getString("nik");
                obj[3] = connection.result.getString("nama");
                obj[4] = connection.result.getString("alamat");
                obj[5] = connection.result.getString("no_hp");
                obj[6] = connection.result.getString("username");
                String status = connection.result.getString("status");
                if(status.equals("1"))
                {
                    obj[7] = "Aktif";
                }
                else
                {
                    obj[7] = "Tidak Aktif";
                }
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
            System.out.println("Terjadi error saat load data karyawan: " + e);
        }
    }

    public void addColumn() {
        model.addColumn("ID Karyawan");
        model.addColumn("Jabatan");
        model.addColumn("NIK");
        model.addColumn("Nama");
        model.addColumn("Alamat");
        model.addColumn("No. Telepon");
        model.addColumn("Username");
        model.addColumn("Status");
    }

    public void loadData() {
        //menghapus seluruh data ditampilkan(jika ada) untuk tampilan pertama
        model.getDataVector().removeAllElements();

        //memberi tahu data telah kosong
        model.fireTableDataChanged();

        try {
            connection.stat = connection.conn.createStatement();
            String query = "SELECT k.id_karyawan, j.deskripsi_jabatan, k.nik, k.nama, k.alamat, k.no_hp, k.username, k.password, k.status FROM tblKaryawan AS k\n" +
                    "\tINNER JOIN tblJabatan j on k.id_jabatan = j.id_jabatan";
            connection.result = connection.stat.executeQuery(query);

            //lakukan perbaris data
            while (connection.result.next()) {
                Object[] obj = new Object[8];
                obj[0] = connection.result.getString("id_karyawan");
                obj[1] = connection.result.getString("deskripsi_jabatan");
                obj[2] = connection.result.getString("nik");
                obj[3] = connection.result.getString("nama");
                obj[4] = connection.result.getString("alamat");
                obj[5] = connection.result.getString("no_hp");
                obj[6] = connection.result.getString("username");
                String status = connection.result.getString("status");
                if(status.equals("1"))
                {
                    obj[7] = "Aktif";
                }
                else
                {
                    obj[7] = "Tidak Aktif";
                }
                model.addRow(obj);
            }
            if (model.getRowCount() == 0) {
                JOptionPane.showMessageDialog(null, "Data masih kosong!", "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        } catch (Exception e) {
            System.out.println("Terjadi error saat load data karyawan: " + e);
        }
    }

    public void clear() {
        txtAlamat.setText("");
        txtNama.setText("");
        txtNIK.setText("");
        txtpw1.setText("");
        txtpw2.setText("");
        txtNoTelp.setText("");
        txtUsername.setText("");
        txtSearchNama.setText("");
        berdasarkanNamaRadioButton.setEnabled(false);
        cmbJabatan.setSelectedItem(null);
        cmbStatus.setSelectedItem(null);
        cmbStatus.setEnabled(false);

        tblKaryawan.getModel();
        if (tblKaryawan.getRowCount() > 0) {
            for (int i = tblKaryawan.getRowCount() - 1; i > -1; i--) {
                model.removeRow(i);
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("CRUD Karyawan");
        frame.setContentPane(new CRUDKaryawan().jpUtama);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setSize(700, 400);
    }

    public void autokode()
    {
        try {
            String sql = "SELECT * FROM tblKaryawan ORDER BY id_karyawan desc";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(sql);
            if (connection.result.next()) {
                idKaryawan = connection.result.getString("id_karyawan");
                int a= Integer.parseInt(idKaryawan.substring(idKaryawan.length()-1))+1;
                String AN = ""+a;
                String nol = "";

                if (AN.length() == 1) {
                    nol = "00";
                } else if (AN.length() == 2) {
                    nol = "0";
                } else if (AN.length() == 3) {
                    nol = "";
                }
                idKaryawan = "KRY" + nol + AN;
            } else {
                idKaryawan = "KRY001";
            }
            connection.stat.close();
            connection.result.close();
        }catch (Exception e1) {
            System.out.println("Terjadi error pada autokode: " + e1);
        }
    }
}
