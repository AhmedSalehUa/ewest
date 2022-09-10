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

/**
 *
 * @author Ahmed Al-Gazzar
 */
public class OperationsMembers {
    int id;
    int operation_id;
    String operation;
    int member_id;
    String member;

    public OperationsMembers() {
    }

    public OperationsMembers(int id, String member) {
        this.id = id;
        this.member = member;
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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
     public boolean Add() throws Exception {
        PreparedStatement st = db.get.Prepare("INSERT INTO `cli_operation_members`(`id`, `operation_id`,`member_id`) VALUES (?,?,?)");
        st.setInt(1, id);
        st.setInt(2, operation_id);
        st.setInt(3, member_id);
       
      

        st.execute();
        return true;
    }
      public boolean Edit() throws Exception {
        PreparedStatement st = db.get.Prepare("UPDATE `cli_operation_members` SET `operation_id`=?,`member_id`=? WHERE `id`=?");

        st.setInt(1, operation_id);
        st.setInt(2, member_id);
      
        st.setInt(3, id);
        st.execute();
        return true;
    }
       public boolean Delete() throws Exception {
        PreparedStatement st = db.get.Prepare("DELETE FROM `cli_operation_members` WHERE `id`=?");
        st.setInt(1, id);
        st.execute();
        return true;
    }
       public static ObservableList<OperationsMembers> getData(int id) throws Exception {

        ObservableList<OperationsMembers> data = FXCollections.observableArrayList();

        String SQL = "SELECT `cli_operation_members`.`id`,`mem_acapy_members`.`name` FROM `cli_operation_members`,`mem_acapy_members`where `cli_operation_members`.`member_id`=`mem_acapy_members`.`id` AND `cli_operation_members`.`operation_id`='"+id+"'";
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery(SQL);

        while (rs.next()) {
            data.add(new OperationsMembers(rs.getInt(1),rs.getString(2)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(max(`id`) + 1,'1') FROM `cli_operation_members`").getValueAt(0, 0).toString();
    }
}
