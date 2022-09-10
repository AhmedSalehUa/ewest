/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.assets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author AHMED
 */
public class Sections {
    
    
    int id;
    String name;
    String department;
    int dept_id;

    public Sections() {
    }

    public Sections(int id, String name, String department) {
        this.id = id;
        this.name = name;
        this.department = department;
    }

    public Sections(int id, String name, int dept_id) {
        this.id = id;
        this.name = name;
        this.dept_id = dept_id;
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
 
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getDept_id() {
        return dept_id;
    }

    public void setDept_id(int dept_id) {
        this.dept_id = dept_id;
    }

    
    public boolean Add() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `att_section`(`id`, `name`, `department_id`) VALUES (?,?,?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setInt(3, dept_id);
        ps.execute();
        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `att_section` SET `name`=?,`department_id`=? WHERE `id`=?");
        ps.setInt(3, id);
        ps.setString(1, name);
        ps.setInt(2, dept_id);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `att_section` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static ObservableList<Sections> getData() throws Exception {
        ObservableList<Sections> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `att_section`.`id`, `att_section`.`name`, `att_department`.`name` FROM `att_section`,`att_department` WHERE `att_department`.`id`= `att_section`.`department_id`");
        while (rs.next()) {
            data.add(new Sections(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `att_section`").getValueAt(0, 0).toString();
    }
}
