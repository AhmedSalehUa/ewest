/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruchases.assets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JTable; 

/**
 *
 * @author Ahmed Al-Gazzar
 */
public class OperationsDetails {

    int id;
    int operation_id;
    int product_id;
    String operation;
    String product;
    String cost;
    String amount;
    String total_cost;

    public OperationsDetails() {
    }

    public OperationsDetails(int id, String product, String cost, String amount, String total_cost) {
        this.id = id;
        this.product = product;
        this.cost = cost;
        this.amount = amount;
        this.total_cost = total_cost;
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

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    } 

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public boolean Add() throws Exception {
        PreparedStatement st = db.get.Prepare("INSERT INTO `cli_operation_details`(`id`, `operation_id`,`product_id`,`cost` ,`amount`, `total_cost`) VALUES (?,?,?,?,?,?)");
        st.setInt(1, id);
        st.setInt(2, operation_id);
        st.setInt(3,  product_id);
        st.setString(4, cost);
        st.setString(5, amount);
        st.setString(6, total_cost); 
        st.execute();
        return true;
    }

    public boolean Edit() throws Exception {
          
        PreparedStatement st = db.get.Prepare("UPDATE `cli_operation_details` SET `operation_id`=?,`product_id`=? ,`cost`=? ,`amount`=?,`total_cost`=? WHERE `id`=?");

        st.setInt(1, operation_id);
        st.setInt(2, product_id);
        st.setString(3, cost);
        st.setString(4, amount);
        st.setString(5, total_cost);
        st.setInt(6, id); 
        st.execute();
        return true;
    }

    public boolean Delete() throws Exception {

        
        PreparedStatement st = db.get.Prepare("DELETE FROM `cli_operation_details` WHERE `id`=?");
        st.setInt(1, id);
        st.execute();
        return true;
    }

    public static ObservableList<OperationsDetails> getData(int id) throws Exception {

        ObservableList<OperationsDetails> data = FXCollections.observableArrayList();

        String SQL = "SELECT `cli_operation_details`.`id`,`st_products`.`name`,`cli_operation_details`.`cost` ,`cli_operation_details`.`amount`, `cli_operation_details`.`total_cost` FROM `st_products`,`cli_operation_details` where  `cli_operation_details`.`product_id` =`st_products`.`id`AND  `cli_operation_details`.`operation_id`='" + id + "'";
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery(SQL);

        while (rs.next()) {
            data.add(new OperationsDetails(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(max(`id`) + 1,'1') FROM `cli_operation_details`").getValueAt(0, 0).toString();
    }
}
