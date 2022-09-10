package Pruchases.assets;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javax.swing.JTable; 

public class AcapyMembers {

    int id;
    String name;
    String nationalId;
    InputStream nationalPhoto;
    String photo_ext;
    String acc_num;
    String app_token;

    public AcapyMembers() {
    }

    public AcapyMembers(int id, String name, String nationalId, String acc_num, String app_token) {
        this.id = id;
        this.name = name;
        this.nationalId = nationalId;
        this.acc_num = acc_num;
        this.app_token = app_token;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcc_num() {
        return acc_num;
    }

    public void setAcc_num(String acc_num) {
        this.acc_num = acc_num;
    }

    public String getApp_token() {
        return app_token;
    }

    public void setApp_token(String app_token) {
        this.app_token = app_token;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public InputStream getNationalPhoto() {
        return nationalPhoto;
    }

    public void setNationalPhoto(InputStream nationalPhoto) {
        this.nationalPhoto = nationalPhoto;
    }

    public String getPhoto_ext() {
        return photo_ext;
    }

    public void setPhoto_ext(String photo_ext) {
        this.photo_ext = photo_ext;
    }

    public boolean Add() throws Exception {
        int i = 0;
        PreparedStatement ps = db.get.Prepare("INSERT INTO `mem_acapy_members`(`id`, `name`,`nationalId`,`nationalPhoto`,`photo_ext`, `acc_num`, `app_token`) VALUES (?,?,?,?,?,?,? )");
        ps.setInt(++i, id);
        ps.setString(++i, name);
        ps.setString(++i, nationalId);
        ps.setBlob(++i, nationalPhoto);
        ps.setString(++i, photo_ext);
        ps.setString(++i, acc_num);
        ps.setString(++i, app_token);
        ps.execute();
        return true;
    }

    public boolean AddWithoutPhoto() throws Exception {
        int i = 0;
        PreparedStatement ps = db.get.Prepare("INSERT INTO `mem_acapy_members`(`id`, `name`,`nationalId`, `acc_num`, `app_token`) VALUES (?,?,?,?,?)");
        ps.setInt(++i, id);
        ps.setString(++i, name);
        ps.setString(++i, nationalId);
        ps.setString(++i, acc_num);
        ps.setString(++i, app_token);
        ps.execute();
        return true;
    }

    public boolean Edite() throws Exception {
        int i = 0;
        PreparedStatement ps = db.get.Prepare("UPDATE `mem_acapy_members` SET `name`=?,`acc_num`=?,`app_token`=?,`nationalId`=?,`nationalPhoto`=?,`photo_ext`=? WHERE `id`=?");
        ps.setString(++i, name);
        ps.setString(++i, acc_num);
        ps.setString(++i, app_token);
        ps.setString(++i, nationalId);
        ps.setBlob(++i, nationalPhoto);
        ps.setString(++i, photo_ext);
        ps.setInt(++i, id);
        ps.execute();
        return true;
    }

    public boolean EditeWithoutPhoto() throws Exception {
        int i = 0;
        PreparedStatement ps = db.get.Prepare("UPDATE `mem_acapy_members` SET `name`=?,`acc_num`=?,`app_token`=?,`nationalId`=? WHERE `id`=?");
        ps.setString(++i, name);
        ps.setString(++i, acc_num);
        ps.setString(++i, app_token);
        ps.setString(++i, nationalId);
        ps.setInt(++i, id);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `mem_acapy_members` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static ObservableList<AcapyMembers> getData() throws Exception {
        ObservableList<AcapyMembers> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `id`, `name`, `nationalId`, `acc_num`, `app_token` FROM `mem_acapy_members` ");
        while (rs.next()) {
            data.add(new AcapyMembers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `mem_acapy_members`").getValueAt(0, 0).toString();
    }

    public void getDocdown() throws Exception {

        File file = null;
        String selectSQL = "SELECT `nationalPhoto` FROM `mem_acapy_members` WHERE `id`='" + id + "'";
        JTable tab = db.get.getTableData("SELECT `photo_ext` FROM `mem_acapy_members` WHERE `id`='" + id + "'");
        if (tab.getRowCount() <= 0 || tab.getValueAt(0, 0) == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("لا يوجد صورة متوفرة");
            alert.show();
        } else {
            String ext = tab.getValueAt(0, 0).toString();
            ResultSet rs = null;

            try {
                PreparedStatement pstmt = db.get.Prepare(selectSQL);

                rs = pstmt.executeQuery();

                File directory = new File(System.getProperty("user.home") + "\\Desktop\\Acapy Trade\\documentation");
                directory.mkdirs();
                String dateFromSql = db.get.getTableData("SELECT  `name` FROM `mem_acapy_members` WHERE `id`='" + id + "'").getValueAt(0, 0).toString();

                file = new File(directory + "\\" + id + "-" + dateFromSql + "." + ext);

                FileOutputStream output = new FileOutputStream(file);

                String payPath = file.getAbsolutePath();
                while (rs.next()) {
                    InputStream input = rs.getBinaryStream("nationalPhoto");
                    byte[] buffer = new byte[1024];
                    while (input.read(buffer) > 0) {
                        output.write(buffer);
                    }
                }
                Desktop d = Desktop.getDesktop();
                d.open(file);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
