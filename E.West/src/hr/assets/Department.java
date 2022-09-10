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
public class Department {

    int id;
    String name; 

    public Department() {
    }

    public Department(int id, String name ) {
        this.id = id;
        this.name = name; 
    }

    public Department(int id, String name, int org_id) {
        this.id = id;
        this.name = name; 
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
 

    public boolean Add() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `att_department`(`id`, `name`) VALUES (?,?)");
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.execute();
        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `att_department` SET `name`=? WHERE `id`=?");
        ps.setInt(2, id);
        ps.setString(1, name);
        ps.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `att_department` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static ObservableList<Department> getData() throws Exception {
        ObservableList<Department> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `id`, `name` FROM `att_department`");
        while (rs.next()) {
            data.add(new Department(rs.getInt(1), rs.getString(2)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `att_department`").getValueAt(0, 0).toString();
    }
}
