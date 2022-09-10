/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruchases.assets;

import EWest.Logs;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.InputStream;
import javafx.scene.control.Alert;
import javax.swing.JTable;

/**
 *
 * @author amran
 */
public class ContractsVisits {

    int id;
    int contractID;
    int memID;
    int deviceID;
    String device;
    String memName;
    String date;
    String report;
    String statue;
    InputStream doc;
    String doc_ext;

    public ContractsVisits() {
    }

    public ContractsVisits(int id, String device, String memName, String date, String report, String statue) {
        this.id = id;
        this.device = device;
        this.memName = memName;
        this.date = date;
        this.report = report;
        this.statue = statue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContractID() {
        return contractID;
    }

    public void setContractID(int contractID) {
        this.contractID = contractID;
    }

    public int getMemID() {
        return memID;
    }

    public void setMemID(int memID) {
        this.memID = memID;
    }

    public String getMemName() {
        return memName;
    }

    public void setMemName(String memName) {
        this.memName = memName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public InputStream getDoc() {
        return doc;
    }

    public void setDoc(InputStream doc) {
        this.doc = doc;
    }

    public String getDoc_ext() {
        return doc_ext;
    }

    public void setDoc_ext(String doc_ext) {
        this.doc_ext = doc_ext;
    }

    public int getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }

    public boolean Add() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("INSERT INTO `cli_contract_visits`(`id`, `contract_id`, `device_id`, `member_id`, `date`, `report`, `statue`, `doc`, `doc_ext`) VALUES (?,?,?,?,?,?,?,?,?)");
        ps.setInt(i++, id);
        ps.setInt(i++, contractID);
        ps.setInt(i++, deviceID);
        ps.setInt(i++, memID);
        ps.setString(i++, date);
        ps.setString(i++, report);
        ps.setString(i++, statue);
        ps.setBlob(i++, doc);
        ps.setString(i++, doc_ext);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean AddWithouPhoto() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("INSERT INTO `cli_contract_visits`(`id`, `contract_id`, `device_id`, `member_id`, `date`, `report`, `statue`) VALUES (?,?,?,?,?,?,?)");
        ps.setInt(i++, id);
        ps.setInt(i++, contractID);
        ps.setInt(i++, deviceID);
        ps.setInt(i++, memID);
        ps.setString(i++, date);
        ps.setString(i++, report);
        ps.setString(i++, statue);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean Edite() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("UPDATE `cli_contract_visits` SET `contract_id`=?,`device_id`=?,`member_id`=?,`date`=?,`report`=?,`statue`=?,`doc`=?,`doc_ext`=? WHERE `id`=?");
        ps.setInt(i++, contractID);
        ps.setInt(i++, deviceID);
        ps.setInt(i++, memID);
        ps.setString(i++, date);
        ps.setString(i++, report);
        ps.setString(i++, statue);
        ps.setBlob(i++, doc);
        ps.setString(i++, doc_ext);
        ps.setInt(i++, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean EditeWithouPhoto() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("UPDATE `cli_contract_visits` SET `contract_id`=?,`device_id`=?,`member_id`=?,`date`=?,`report`=?,`statue`=? WHERE `id`=?");
        ps.setInt(i++, contractID);
        ps.setInt(i++, deviceID);
        ps.setInt(i++, memID);
        ps.setString(i++, date);
        ps.setString(i++, report);
        ps.setString(i++, statue);
        ps.setInt(i++, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `cli_contract_visits` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public static ObservableList<ContractsVisits> getData(int id) throws Exception {
        ObservableList<ContractsVisits> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `cli_contract_visits`.`id`,`cli_contract_details`.`name`,`att_employee`.`name`, `cli_contract_visits`.`date`, `cli_contract_visits`.`report`, `cli_contract_visits`.`statue` FROM `att_employee`,`cli_contract_visits`,`cli_contract_details` WHERE `cli_contract_visits`.`member_id`= `att_employee`.`id` AND `cli_contract_details`.`id`=`cli_contract_visits`.`device_id` AND cli_contract_visits.`contract_id`='" + id + "' ");
        while (rs.next()) {
            data.add(new ContractsVisits(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `cli_contract_visits`").getValueAt(0, 0).toString();
    }

    public void getDocdown() throws Exception {

        File file = null;
        String selectSQL = "SELECT `doc` FROM `cli_contract_visits` WHERE `id`='" + id + "'";
        JTable tab = db.get.getTableData("SELECT `doc_ext` FROM `cli_contract_visits` WHERE `id`='" + id + "'");
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

                File directory = new File(System.getProperty("user.home") + "\\Desktop\\E-West\\documentation");
                directory.mkdirs();
                String dateFromSql = db.get.getTableData("SELECT  `date` FROM `cli_contract_visits` WHERE `id`='" + id + "'").getValueAt(0, 0).toString();

                file = new File(directory + "\\" + id + "-" + dateFromSql + "." + ext);

                FileOutputStream output = new FileOutputStream(file);

                String payPath = file.getAbsolutePath();
                while (rs.next()) {
                    InputStream input = rs.getBinaryStream("doc");
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
