/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.assets;

import EWest.Logs;
import assets.classes.AlertDialogs;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JTable;

/**
 *
 * @author AHMED
 */
public class Employee {

    int id;
    String name;
    String tele1;
    String tele2;
    String email;
    String address;
    String nationalId;
    InputStream nationalIdPhoto;
    String nationalIdPhototExt;
    InputStream securityPhoto;
    String securityPhototExt;
    String accNum;
    String accBank;
    String salary;
    int sectionId;
    String section;
    String shifts;
    String role;

    public Employee() {
    }

    public Employee(int id, String name, String tele1, String tele2, String email, String address, String nationalId, String accNum, String accBank, String salary, String section, String shifts, String role) {
        this.id = id;
        this.name = name;
        this.tele1 = tele1;
        this.tele2 = tele2;
        this.email = email;
        this.address = address;
        this.nationalId = nationalId;
        this.accNum = accNum;
        this.accBank = accBank;
        this.salary = salary;
        this.section = section;
        this.shifts = shifts;
        this.role = role;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public InputStream getNationalIdPhoto() {
        return nationalIdPhoto;
    }

    public void setNationalIdPhoto(InputStream nationalIdPhoto) {
        this.nationalIdPhoto = nationalIdPhoto;
    }

    public String getNationalIdPhototExt() {
        return nationalIdPhototExt;
    }

    public void setNationalIdPhototExt(String nationalIdPhototExt) {
        this.nationalIdPhototExt = nationalIdPhototExt;
    }

    public String getAccNum() {
        return accNum;
    }

    public void setAccNum(String accNum) {
        this.accNum = accNum;
    }

    public String getAccBank() {
        return accBank;
    }

    public void setAccBank(String accBank) {
        this.accBank = accBank;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getShifts() {
        return shifts;
    }

    public void setShifts(String shifts) {
        this.shifts = shifts;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public InputStream getSecurityPhoto() {
        return securityPhoto;
    }

    public void setSecurityPhoto(InputStream securityPhoto) {
        this.securityPhoto = securityPhoto;
    }

    public String getSecurityPhototExt() {
        return securityPhototExt;
    }

    public void setSecurityPhototExt(String securityPhototExt) {
        this.securityPhototExt = securityPhototExt;
    }

    public boolean Add() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("INSERT INTO `att_employee`( `name`, `tele1`, `tele2`, `email`, `address`, `national_id`, `acc_num`, `acc_bank`, `salary`, `section_id`, `shift_ids`, `role`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
        ps.setString(i++, name);
        ps.setString(i++, tele1);
        ps.setString(i++, tele2);
        ps.setString(i++, email);
        ps.setString(i++, address);
        ps.setString(i++, nationalId);
        ps.setString(i++, accNum);
        ps.setString(i++, accBank);
        ps.setString(i++, salary);
        ps.setInt(i++, sectionId);
        ps.setString(i++, shifts);
        ps.setString(i++, role);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean AddWithPhoto() throws Exception {
        int i = 1;

        String str = "INSERT INTO `att_employee`( `name`, `tele1`, `tele2`, `email`, `address`, `national_id`, `national_photo`, `national_ext`, `securityPhoto`, `securityPhototExt`, `acc_num`, `acc_bank`, `salary`, `section_id`, `shift_ids`, `role`) VALUES (?,?,?,?,?,?,?,?,?,?";
        if (nationalIdPhoto != null) {
            str += ",?,?";
        }
        if (securityPhoto != null) {
            str += ",?,?";
        }
        str += ")";
        PreparedStatement ps = db.get.Prepare(str);
        ps.setString(i++, name);
        ps.setString(i++, tele1);
        ps.setString(i++, tele2);
        ps.setString(i++, email);
        ps.setString(i++, address);
        ps.setString(i++, nationalId);
        if (nationalIdPhoto != null) {
            ps.setBlob(i++, nationalIdPhoto);
            ps.setString(i++, nationalIdPhototExt);
        }
        if (securityPhoto != null) {
            ps.setBlob(i++, securityPhoto);
            ps.setString(i++, securityPhototExt);
        }
        ps.setString(i++, accNum);
        ps.setString(i++, accBank);
        ps.setString(i++, salary);
        ps.setInt(i++, sectionId);
        ps.setString(i++, shifts);
        ps.setString(i++, role);
        ps.execute();
        Logs.Add(ps.toString());
        return true;

    }

    public boolean Edite() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("UPDATE `att_employee` SET `name`=?,`tele1`=?,`tele2`=?,`email`=?,`address`=?,`national_id`=?,`acc_num`=?,`acc_bank`=?,`salary`=?,`section_id`=?,`shift_ids`=?,`role`=? WHERE `id`=?");
        ps.setString(i++, name);
        ps.setString(i++, tele1);
        ps.setString(i++, tele2);
        ps.setString(i++, email);
        ps.setString(i++, address);
        ps.setString(i++, nationalId);
        ps.setString(i++, accNum);
        ps.setString(i++, accBank);
        ps.setString(i++, salary);
        ps.setInt(i++, sectionId);
        ps.setString(i++, shifts);
        ps.setString(i++, role);
        ps.setInt(i++, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean EditeWithPhoto() throws Exception {
        int i = 1;
        String str = "UPDATE `att_employee` SET `name`=?,`tele1`=?,`tele2`=?,`email`=?,`address`=?,`national_id`=?,";
        if (nationalIdPhoto != null) {
            str += "`national_photo`=?,`national_ext`=?,";
        }
        if (securityPhoto != null) {
            str += "`securityPhoto`=?,`securityPhototExt`=?,";
        }

        str += "`acc_num`=?,`acc_bank`=?,`salary`=?,`section_id`=?,`shift_ids`=?,`role`=? WHERE `id`=?";
        PreparedStatement ps = db.get.Prepare(str);
        ps.setString(i++, name);
        ps.setString(i++, tele1);
        ps.setString(i++, tele2);
        ps.setString(i++, email);
        ps.setString(i++, address);
        ps.setString(i++, nationalId);
        if (nationalIdPhoto != null) {
            ps.setBlob(i++, nationalIdPhoto);
            ps.setString(i++, nationalIdPhototExt);
        }
        if (securityPhoto != null) {
            ps.setBlob(i++, securityPhoto);
            ps.setString(i++, securityPhototExt);
        }
        ps.setString(i++, accNum);
        ps.setString(i++, accBank);
        ps.setString(i++, salary);
        ps.setInt(i++, sectionId);
        ps.setString(i++, shifts);
        ps.setString(i++, role);
        ps.setInt(i++, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;

    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `att_employee` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public static ObservableList<Employee> getData() throws Exception {
        ObservableList<Employee> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `att_employee`.`id`, `att_employee`.`name`, `att_employee`.`tele1`, `att_employee`.`tele2`, `att_employee`.`email`, `att_employee`.`address`, `att_employee`.`national_id`,`att_employee`.`acc_num`, `att_employee`.`acc_bank`, `att_employee`.`salary`,`att_section`.`name`, `att_employee`.`shift_ids`, `att_employee`.`role` FROM `att_employee`,`att_section` WHERE `att_section`.`id` = `att_employee`.`section_id`");
        while (rs.next()) {
            data.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13)));
        }
        return data;
    }

    public static ObservableList<Employee> getSales() throws Exception {
        ObservableList<Employee> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `att_employee`.`id`, `att_employee`.`name`, `att_employee`.`tele1`, `att_employee`.`tele2`, `att_employee`.`email`, `att_employee`.`address`, `att_employee`.`national_id`,`att_employee`.`acc_num`, `att_employee`.`acc_bank`, `att_employee`.`salary`,`att_section`.`name`, `att_employee`.`shift_ids`, `att_employee`.`role` FROM `att_employee`,`att_section` WHERE `att_section`.`id` = `att_employee`.`section_id` AND `att_employee`.`role`='مبيعات'");
        while (rs.next()) {
            data.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13)));
        }
        return data;
    }

    public static ObservableList<Employee> getMaintaince() throws Exception {
        ObservableList<Employee> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `att_employee`.`id`, `att_employee`.`name`, `att_employee`.`tele1`, `att_employee`.`tele2`, `att_employee`.`email`, `att_employee`.`address`, `att_employee`.`national_id`,`att_employee`.`acc_num`, `att_employee`.`acc_bank`, `att_employee`.`salary`,`att_section`.`name`, `att_employee`.`shift_ids`, `att_employee`.`role` FROM `att_employee`,`att_section` WHERE `att_section`.`id` = `att_employee`.`section_id` AND `att_employee`.`role`='صيانة'");
        while (rs.next()) {
            data.add(new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `att_employee`").getValueAt(0, 0).toString();
    }

    public void showNationalPhoto() throws Exception {
        File file = null;
        String selectSQL = "SELECT `national_photo` FROM `att_employee` WHERE `id`='" + id + "'";
        JTable tableData = db.get.getTableData("SELECT `national_ext`,`name` FROM `att_employee` WHERE `id`='" + id + "'");
        if (tableData.getValueAt(0, 0) == null) {
            AlertDialogs.showError("لا يوجد مستند للموظف");
        } else {

            String ext = tableData.getValueAt(0, 0).toString();
            String name = tableData.getValueAt(0, 1).toString();
            ResultSet rs = null;

            try {
                PreparedStatement pstmt = db.get.Prepare(selectSQL);
                // set parameter;

                rs = pstmt.executeQuery();

                File directory = new File(System.getProperty("user.home") + "\\Desktop\\E-West\\employee");
                directory.mkdirs();

                file = new File(directory + "\\" + id + "-" + name + "." + ext);

                FileOutputStream output = new FileOutputStream(file);

                String payPath = file.getAbsolutePath();
                while (rs.next()) {
                    InputStream input = rs.getBinaryStream("national_photo");
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

    public void showSecurityPhoto() throws Exception {
        File file = null;
        String selectSQL = "SELECT `securityPhoto` FROM `att_employee` WHERE `id`='" + id + "'";
        JTable tableData = db.get.getTableData("SELECT `securityPhototExt`,`name` FROM `att_employee` WHERE `id`='" + id + "'");
        if (tableData.getValueAt(0, 0) == null) {
            AlertDialogs.showError("لا يوجد مستند للموظف");
        } else {

            String ext = tableData.getValueAt(0, 0).toString();
            String name = tableData.getValueAt(0, 1).toString();
            ResultSet rs = null;

            try {
                PreparedStatement pstmt = db.get.Prepare(selectSQL);
                // set parameter;

                rs = pstmt.executeQuery();

                File directory = new File(System.getProperty("user.home") + "\\Desktop\\E-West\\employee");
                directory.mkdirs();

                file = new File(directory + "\\" + id + "-" + name + "." + ext);

                FileOutputStream output = new FileOutputStream(file);

                String payPath = file.getAbsolutePath();
                while (rs.next()) {
                    InputStream input = rs.getBinaryStream("securityPhoto");
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
