package Pruchases.assets;

import EWest.Logs;
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

public class Operations {

    int id;
    int client_id;
    String client_name;
    int sales_id;
    String sales_name;
    String date;
    String total_cost;
    String total_spend;
    String total_yield;
    String pay_type;
    String hasTaxs;
    InputStream doc;
    String doc_ext;

    public Operations() {
    }

    public Operations(int id, String client_name, String sales_name, String date, String total_cost, String total_spend, String total_yield, String pay_type, String hasTaxs) {
        this.id = id;
        this.client_name = client_name;
        this.sales_name = sales_name;
        this.date = date;
        this.total_cost = total_cost;
        this.total_spend = total_spend;
        this.total_yield = total_yield;
        this.pay_type = pay_type;
        this.hasTaxs = hasTaxs;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public int getSales_id() {
        return sales_id;
    }

    public void setSales_id(int sales_id) {
        this.sales_id = sales_id;
    }

    public String getSales_name() {
        return sales_name;
    }

    public void setSales_name(String sales_name) {
        this.sales_name = sales_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(String total_cost) {
        this.total_cost = total_cost;
    }

    public String getTotal_spend() {
        return total_spend;
    }

    public void setTotal_spend(String total_spend) {
        this.total_spend = total_spend;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getTotal_yield() {
        return total_yield;
    }

    public void setTotal_yield(String total_yield) {
        this.total_yield = total_yield;
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

    public String getHasTaxs() {
        return hasTaxs;
    }

    public void setHasTaxs(String hasTaxs) {
        this.hasTaxs = hasTaxs;
    }

    public boolean Add() throws Exception {
        int i = 1;
        PreparedStatement st = db.get.Prepare("INSERT INTO `cli_operation`(`id`, `client_id`,`sales_id`, `date` ,`total_cost`,`total_spended`,`total_yield`, `pay_type`,`doc`,`doc_ext`,`has_taxs` ) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
        st.setInt(i++, id);
        st.setInt(i++, client_id);
        st.setInt(i++, sales_id);
        st.setString(i++, date);
        st.setString(i++, total_cost);
        st.setString(i++, total_spend);
        st.setString(i++, total_yield);
        st.setString(i++, pay_type);
        st.setBlob(i++, doc);
        st.setString(i++, doc_ext);
        st.setString(i++, hasTaxs);
        st.execute();
        Logs.Add(st.toString());
        return true;
    }

    public boolean AddWithouPhoto() throws Exception {
        int i = 1;
        PreparedStatement st = db.get.Prepare("INSERT INTO `cli_operation`(`id`, `client_id`,`sales_id`, `date` ,`total_cost`,`total_spended`,`total_yield`, `pay_type`,`has_taxs` ) VALUES (?,?,?,?,?,?,?,?,?)");
        st.setInt(i++, id);
        st.setInt(i++, client_id);
        st.setInt(i++, sales_id);
        st.setString(i++, date);
        st.setString(i++, total_cost);
        st.setString(i++, total_spend);
        st.setString(i++, total_yield);
        st.setString(i++, pay_type);
        st.setString(i++, hasTaxs);
        st.execute();
        Logs.Add(st.toString());
        return true;
    }

    public boolean Edit() throws Exception {
        int i = 1;
        PreparedStatement st = db.get.Prepare("UPDATE `cli_operation` SET `client_id`=?,`sales_id`=? ,`date`=? ,`total_cost`=?,`total_spended`=?,`total_yield`=?,`pay_type`=?,`doc`=?, `doc_ext`=?,`has_taxs`=? WHERE `id`=?");
        st.setInt(i++, client_id);
        st.setInt(i++, sales_id);
        st.setString(i++, date);
        st.setString(i++, total_cost);
        st.setString(i++, total_spend);
        st.setString(i++, total_yield);
        st.setString(i++, pay_type);
        st.setBlob(i++, doc);
        st.setString(i++, doc_ext);
        st.setString(i++, hasTaxs);
        st.setInt(i++, id);
        st.execute();
        Logs.Add(st.toString());
        return true;
    }

    public boolean EditeWithouPhoto() throws Exception {
        int i = 1;
        PreparedStatement st = db.get.Prepare("UPDATE `cli_operation` SET `client_id`=?,`sales_id`=? ,`date`=? ,`total_cost`=?,`total_spended`=?,`total_yield`=?,`pay_type`=?,`has_taxs`=? WHERE `id`=?");
        st.setInt(i++, client_id);
        st.setInt(i++, sales_id);
        st.setString(i++, date);
        st.setString(i++, total_cost);
        st.setString(i++, total_spend);
        st.setString(i++, total_yield);
        st.setString(i++, pay_type);
        st.setString(i++, hasTaxs);
        st.setInt(i++, id);
        st.execute();
        Logs.Add(st.toString());
        return true;
    }

    public static boolean updateCost(int id, String cost) throws Exception {
        PreparedStatement st = db.get.Prepare("UPDATE `cli_operation` SET `total_cost`=? WHERE `id`=?");

        st.setString(1, cost);
        st.setInt(2, id);
        st.execute();
        return true;
    }

    public static boolean updateSpended(int id, String cost) throws Exception {
        PreparedStatement st = db.get.Prepare("UPDATE `cli_operation` SET `total_spended`=? WHERE `id`=?");

        st.setString(1, cost);
        st.setInt(2, id);
        st.execute();
        return true;
    }

    public static boolean updateYields(int id, String cost) throws Exception {
        PreparedStatement st = db.get.Prepare("UPDATE `cli_operation` SET `total_yield`=? WHERE `id`=?");

        st.setString(1, cost);
        st.setInt(2, id);
        st.execute();
        return true;
    }

    public boolean Delete() throws Exception {
        PreparedStatement st = db.get.Prepare("DELETE FROM `cli_operation` WHERE `id`=?");
        st.setInt(1, id);
        st.execute();
        return true;
    }

    public static ObservableList<Operations> getData() throws Exception {

        ObservableList<Operations> data = FXCollections.observableArrayList();

        String SQL = "SELECT `cli_operation`.`id`,`cli_clients`.`organization`, `sl_members`.`name`,`cli_operation`.`date` ,`cli_operation`.`total_cost`,`cli_operation`.`total_spended`,`cli_operation`.`total_yield`, `cli_operation`.`pay_type`, `cli_operation`.`has_taxs` FROM `cli_operation`,`cli_clients`,`sl_members`where `cli_operation`.`client_id`=`cli_clients`.`id` AND `cli_operation`.`sales_id`= `sl_members`.`id`";
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery(SQL);

        while (rs.next()) {
            data.add(new Operations(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(max(`id`) + 1,'1') FROM `cli_operation`").getValueAt(0, 0).toString();
    }

    public void getDocdown() throws Exception {

        File file = null;
        String selectSQL = "SELECT `doc` FROM `cli_operation` WHERE `id`='" + id + "'";
        JTable tab = db.get.getTableData("SELECT `doc_ext` FROM `cli_operation` WHERE `id`='" + id + "'");
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
                String dateFromSql = db.get.getTableData("SELECT  `date` FROM `cli_operation` WHERE `id`='" + id + "'").getValueAt(0, 0).toString();

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
