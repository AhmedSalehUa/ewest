/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BaseData.assets;

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
public class Clients {

    int id;
    String organization;
    String name;
    String address;
    String email;
    String accountNumber;
    String taxNumber;
    String commericalNum;
    String tele1;
    String tele2;
    String dateOfContract;
    InputStream PhotoOfContract;
    String ContractExt;

    public Clients() {
    }

    public Clients(int id, String organization, String name, String address, String email, String accountNumber, String taxNumber, String commericalNum, String tele1, String tele2, String dateOfContract) {
        this.id = id;
        this.organization = organization;
        this.name = name;
        this.address = address;
        this.email = email;
        this.accountNumber = accountNumber;
        this.taxNumber = taxNumber;
        this.commericalNum = commericalNum;
        this.tele1 = tele1;
        this.tele2 = tele2;
        this.dateOfContract = dateOfContract;
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

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getTele1() {
        return tele1;
    }

    public void setTele1(String tele1) {
        this.tele1 = tele1;
    }

    public String getTele2() {
        return tele2;
    }

    public void setTele2(String tele2) {
        this.tele2 = tele2;
    }

    public String getCommericalNum() {
        return commericalNum;
    }

    public void setCommericalNum(String commericalNum) {
        this.commericalNum = commericalNum;
    }

    public String getDateOfContract() {
        return dateOfContract;
    }

    public void setDateOfContract(String dateOfContract) {
        this.dateOfContract = dateOfContract;
    }

    public InputStream getPhotoOfContract() {
        return PhotoOfContract;
    }

    public void setPhotoOfContract(InputStream PhotoOfContract) {
        this.PhotoOfContract = PhotoOfContract;
    }

    public String getContractExt() {
        return ContractExt;
    }

    public void setContractExt(String ContractExt) {
        this.ContractExt = ContractExt;
    }

    public boolean Add() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("INSERT INTO `cli_clients`( `organization`, `name`, `address`, `email`, `account_number`, `tax_number`, `comerical_num`, `tele1`, `tele2`, `date_of_contract`) VALUES (?,?,?,?,?,?,?,?,?,?)");
        ps.setString(i++, organization);
        ps.setString(i++, name);
        ps.setString(i++, address);
        ps.setString(i++, email);
        ps.setString(i++, accountNumber);
        ps.setString(i++, taxNumber);
        ps.setString(i++, commericalNum);
        ps.setString(i++, tele1);
        ps.setString(i++, tele2);
        ps.setString(i++, dateOfContract);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean AddWithPhoto() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("INSERT INTO `cli_clients`( `organization`, `name`, `address`, `email`, `account_number`, `tax_number`, `comerical_num`, `tele1`, `tele2`, `date_of_contract`,`photo_of_doc`, `ext_of_doc`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(i++, organization);
        ps.setString(i++, name);
        ps.setString(i++, address);
        ps.setString(i++, email);
        ps.setString(i++, accountNumber);
        ps.setString(i++, taxNumber);
        ps.setString(i++, commericalNum);
        ps.setString(i++, tele1);
        ps.setString(i++, tele2);
        ps.setString(i++, dateOfContract);
        ps.setBlob(i++, PhotoOfContract);
        ps.setString(i++, ContractExt);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean Edite() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("UPDATE `cli_clients` SET `organization`=?,`name`=?,`address`=?,`email`=?,`account_number`=?,`comerical_num`=?,`tax_number`=?,`tele1`=?,`tele2`=?,`date_of_contract`=? WHERE `id`=?");
        ps.setString(i++, organization);
        ps.setString(i++, name);
        ps.setString(i++, address);
        ps.setString(i++, email);
        ps.setString(i++, accountNumber);
        ps.setString(i++, taxNumber);
        ps.setString(i++, commericalNum);
        ps.setString(i++, tele1);
        ps.setString(i++, tele2);
        ps.setString(i++, dateOfContract);
        ps.setInt(i++, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean EditeWithPhoto() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("UPDATE `cli_clients` SET `organization`=?,`name`=?,`address`=?,`email`=?,`account_number`=?,`comerical_num`=?,`tax_number`=?,`tele1`=?,`tele2`=?,`date_of_contract`=?,`photo_of_doc`=?,`ext_of_doc`=? WHERE `id`=?");
        ps.setString(i++, organization);
        ps.setString(i++, name);
        ps.setString(i++, address);
        ps.setString(i++, email);
        ps.setString(i++, accountNumber);
        ps.setString(i++, taxNumber);
        ps.setString(i++, commericalNum);
        ps.setString(i++, tele1);
        ps.setString(i++, tele2);
        ps.setString(i++, dateOfContract);
        ps.setBlob(i++, PhotoOfContract);
        ps.setString(i++, ContractExt);
        ps.setInt(i++, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `cli_clients` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public static ObservableList<Clients> getData() throws Exception {
        ObservableList<Clients> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `id`, `organization`, `name`, `address`, `email`, `account_number`, `tax_number`, `comerical_num`, `tele1`, `tele2`, `date_of_contract` FROM `cli_clients`");
        while (rs.next()) {
            data.add(new Clients(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `cli_clients`").getValueAt(0, 0).toString();
    }

    public void getContractPhoto() throws Exception {

        File file = null;
        String selectSQL = "SELECT `photo_of_doc` FROM `cli_clients` WHERE `id`='" + id + "'";
        JTable tableData = db.get.getTableData("SELECT `ext_of_doc`,`organization` FROM `cli_clients` WHERE `id`='" + id + "'");
        if (tableData.getValueAt(0, 0) == null) {
            AlertDialogs.showError("لا يوجد مستند للعميل");
        } else {

            String ext = tableData.getValueAt(0, 0).toString();
            String name = tableData.getValueAt(0, 1).toString();
            ResultSet rs = null;

            try {
                PreparedStatement pstmt = db.get.Prepare(selectSQL);
                // set parameter;

                rs = pstmt.executeQuery();

                File directory = new File(System.getProperty("user.home") + "\\Desktop\\E-West\\contracts");
                directory.mkdirs();

                file = new File(directory + "\\" + id + "-" + name + "." + ext);

                FileOutputStream output = new FileOutputStream(file);

                String payPath = file.getAbsolutePath();
                while (rs.next()) {
                    InputStream input = rs.getBinaryStream("photo_of_doc");
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
