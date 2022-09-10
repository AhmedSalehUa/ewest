/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruchases.assets;

import EWest.Logs;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author MTC
 */
public class ContractsDetails {

    int id;
    int contract_id;
    String name;
    String model;
    String cost;
    String details;

    public ContractsDetails() {
    }

    public ContractsDetails(String name, String model, String cost, String details, int id) {
        this.name = name;
        this.model = model;
        this.cost = cost;
        this.details = details;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContract_id() {
        return contract_id;
    }

    public void setContract_id(int contract_id) {
        this.contract_id = contract_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean Add() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("INSERT INTO `cli_contract_details`(`contract_id`, `name`, `model`, `cost`, `details`) VALUES (?,?,?,?,?)");
        ps.setInt(i++, contract_id);
        ps.setString(i++, name);
        ps.setString(i++, model);
        ps.setString(i++, cost);
        ps.setString(i++, details);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean Edite() throws Exception {
        int i = 1;
        PreparedStatement ps = db.get.Prepare("UPDATE `cli_contract_details` SET `name`=?,`model`=?,`cost`=?,`details`=? WHERE `id`=?");
        ps.setString(i++, name);
        ps.setString(i++, model);
        ps.setString(i++, cost);
        ps.setString(i++, details);
        ps.setInt(i++, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `cli_contract_details` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        Logs.Add(ps.toString());
        return true;
    }

    public static ObservableList<ContractsDetails> getData(int id) throws Exception {
        ObservableList<ContractsDetails> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `name`, `model`, `cost`, `details`,`id` FROM `cli_contract_details` WHERE  `contract_id`='" + id + "'");
         while (rs.next()) {
            data.add(new ContractsDetails(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `cli_contract_details`").getValueAt(0, 0).toString();
    }
}
