/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruchases.assets;

import EWest.Logs;
import assets.classes.AlertDialogs;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JTable;

/**
 *
 * @author AHMED
 */
public class OperationsCosts {

    int id;
    int operation_id;
    String amount;
    String payFor;
    String date;
    InputStream photo;
    String photo_ext;
    int user_id;
    String user;
    String SysTime;

    public OperationsCosts() {
    }

    public OperationsCosts(int id, int operation_id, String amount, String date, String payFor, String user, String SysTime) {
        this.id = id;
        this.operation_id = operation_id;
        this.amount = amount;
        this.payFor = payFor;
        this.date = date;
        this.user = user;     this.SysTime = SysTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(int operation_id) {
        this.operation_id = operation_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayFor() {
        return payFor;
    }

    public void setPayFor(String payFor) {
        this.payFor = payFor;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public InputStream getPhoto() {
        return photo;
    }

    public void setPhoto(InputStream photo) {
        this.photo = photo;
    }

    public String getPhoto_ext() {
        return photo_ext;
    }

    public void setPhoto_ext(String photo_ext) {
        this.photo_ext = photo_ext;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSysTime() {
        return SysTime;
    }

    public void setSysTime(String SysTime) {
        this.SysTime = SysTime;
    }
    

    public boolean Add() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("INSERT INTO `cli_operation_costs`(`id`, `operation_id`, `amount`, `date`, `reason`,`user_id`) VALUES (?,?,?,?,?,?)");
        ps.setInt(i++, id);
        ps.setInt(i++, operation_id);
        ps.setString(i++, amount);
        ps.setString(i++, date);
        ps.setString(i++, payFor);
        ps.setInt(i++, user_id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean AddWithPhoto() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("INSERT INTO `cli_operation_costs`(`id`, `operation_id`, `amount`, `date`, `reason`, `doc_photo`, `doc_ext`,`user_id`) VALUES (?,?,?,?,?,?,?,?)");
        ps.setInt(i++, id);
        ps.setInt(i++, operation_id);
        ps.setString(i++, amount);
        ps.setString(i++, date);
        ps.setString(i++, payFor);
        ps.setBlob(i++, photo);
        ps.setString(i++, photo_ext);
        ps.setInt(i++, user_id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean Edite() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("UPDATE `cli_operation_costs` SET `operation_id`=?,`amount`=?,`date`=?,`reason`=?,`user_id`=? WHERE `id`=?");
        ps.setInt(i++, operation_id);
        ps.setString(i++, amount);
        ps.setString(i++, date);
        ps.setString(i++, payFor);
        ps.setInt(i++, user_id);
        ps.setInt(i++, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean EditeWithPhoto() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("UPDATE `cli_operation_costs` SET `operation_id`=?,`amount`=?,`date`=?,`reason`=?,`doc_photo`=?,`doc_ext`=?,`user_id`=? WHERE `id`=?");
        ps.setInt(i++, operation_id);
        ps.setString(i++, amount);
        ps.setString(i++, date);
        ps.setString(i++, payFor);
        ps.setBlob(i++, photo);
        ps.setString(i++, photo_ext);
        ps.setInt(i++, user_id);
        ps.setInt(i++, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `cli_operation_costs` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static ObservableList<OperationsCosts> getData(int id) throws Exception {
        ObservableList<OperationsCosts> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT  `cli_operation_costs`.`id`,  `cli_operation_costs`.`operation_id`,  `cli_operation_costs`.`amount`,  `cli_operation_costs`.`date`,  `cli_operation_costs`.`reason`, `sys_users`.`username` ,  `cli_operation_costs`.`date_time` FROM `cli_operation_costs`,`sys_users` WHERE `cli_operation_costs`.`user_id` =`sys_users`.`id` AND `operation_id`='" + id + "'");
        while (rs.next()) {
            data.add(new OperationsCosts(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `cli_operation_costs`").getValueAt(0, 0).toString();
    }

    public void getCostPhoto() throws Exception {
        File file = null;
        String selectSQL = "SELECT `doc_photo` FROM `cli_operation_costs` WHERE `id`='" + id + "'";
        JTable tableData = db.get.getTableData("SELECT `doc_ext`,`operation_id` FROM `cli_operation_costs` WHERE `id`='" + id + "'");
        if (tableData.getValueAt(0, 0) == null) {
            AlertDialogs.showError("لا يوجد مستند للصرف");
        } else {

            String ext = tableData.getValueAt(0, 0).toString();
            String name = tableData.getValueAt(0, 1).toString();
            ResultSet rs = null;

            try {
                PreparedStatement pstmt = db.get.Prepare(selectSQL);
                // set parameter;

                rs = pstmt.executeQuery();

                File directory = new File(System.getProperty("user.home") + "\\Desktop\\E-West\\operations");
                directory.mkdirs();

                file = new File(directory + "\\" + id + "-" + name + "." + ext);

                FileOutputStream output = new FileOutputStream(file);

                String payPath = file.getAbsolutePath();
                while (rs.next()) {
                    InputStream input = rs.getBinaryStream("doc_photo");
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
