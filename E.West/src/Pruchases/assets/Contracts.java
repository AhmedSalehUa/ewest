/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruchases.assets;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javax.swing.JTable;

/**
 *
 * @author amran
 */
public class Contracts {

    int id;
    int cli_id;
    String name;
    String date_from;
    String date_to;
    InputStream doc;
    String hasTaxs;
    String doc_path;
    String noVisits;
    String cost;
    String due_after;

    public Contracts() {
    }

    public Contracts(int id, String name, String date_from, String date_to, String noVisits, String cost, String due_after,String hasTaxs) {
        this.id = id;
        this.name = name;
        this.date_from = date_from;
        this.date_to = date_to;
        this.noVisits = noVisits;
        this.cost = cost;
        this.due_after = due_after; this.hasTaxs = hasTaxs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCli_id() {
        return cli_id;
    }

    public void setCli_id(int cli_id) {
        this.cli_id = cli_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public String getHasTaxs() {
        return hasTaxs;
    }

    public void setHasTaxs(String hasTaxs) {
        this.hasTaxs = hasTaxs;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getNoVisits() {
        return noVisits;
    }

    public void setNoVisits(String noVisits) {
        this.noVisits = noVisits;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDue_after() {
        return due_after;
    }

    public void setDue_after(String due_after) {
        this.due_after = due_after;
    }

    public InputStream getDoc() {
        return doc;
    }

    public void setDoc(InputStream doc) {
        this.doc = doc;
    }

    public String getDoc_path() {
        return doc_path;
    }

    public void setDoc_path(String doc_path) {
        this.doc_path = doc_path;
    }

    public boolean Add() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `cli_contracts`(`id`, `client_id`, `date_from`, `date_to`, `doc`, `doc_ext`, `num_of_visits`, `cost`, `due_after`,`has_taxs`) VALUES (?,?,?,?,?,?,?,?,?,?)");
        ps.setInt(1, id);
        ps.setInt(2, cli_id);
        ps.setString(3, date_from);
        ps.setString(4, date_to);
        ps.setBlob(5, doc);
        ps.setString(6, doc_path);
        ps.setString(7, noVisits);
        ps.setString(8, cost);
        ps.setString(9, due_after);  ps.setString(10,hasTaxs);
        ps.execute();
        return true;
    }

    public boolean AddWithouPhoto() throws Exception {
        PreparedStatement ps = db.get.Prepare("INSERT INTO `cli_contracts`(`id`, `client_id`, `date_from`, `date_to`, `num_of_visits`, `cost`, `due_after`,`has_taxs`) VALUES (?,?,?,?,?,?,?,?)");
        ps.setInt(1, id);
        ps.setInt(2, cli_id);
        ps.setString(3, date_from);
        ps.setString(4, date_to);
        ps.setString(5, noVisits);
        ps.setString(6, cost);
        ps.setString(7, due_after);ps.setString(8,hasTaxs);
        ps.execute();
        return true;
    }

    public boolean Edite() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `cli_contracts` SET `client_id`=?,`date_from`=?,`date_to`=?,`doc`=?, `doc_ext`=?,`num_of_visits`=?,`cost`=?,`due_after`=?,`has_taxs`=? WHERE `id`=?");
        ps.setInt(1, cli_id);
        ps.setString(2, date_from);
        ps.setString(3, date_to);
        ps.setBlob(4, doc);
        ps.setString(5, doc_path);
        ps.setString(6, noVisits);
        ps.setString(7, cost);
        ps.setString(8, due_after);ps.setString(9,hasTaxs);
        ps.setInt(10, id);
        ps.execute();
        return true;
    }

    public boolean EditeWithouPhoto() throws Exception {
        PreparedStatement ps = db.get.Prepare("UPDATE `cli_contracts` SET `client_id`=?,`date_from`=?,`date_to`=?,`num_of_visits`=?,`cost`=?,`due_after`=?,`has_taxs`=? WHERE `id`=?");
        ps.setInt(1, cli_id);
        ps.setString(2, date_from);
        ps.setString(3, date_to);
        ps.setString(4, noVisits);
        ps.setString(5, cost);
        ps.setString(6, due_after);ps.setString(7,hasTaxs);
        ps.setInt(8, id);
        ps.execute();
        return true;
    }
 public static boolean updateCost(int id, String cost) throws Exception {
        PreparedStatement st = db.get.Prepare("UPDATE `cli_contracts` SET `cost`=? WHERE `id`=?"); 
        st.setString(1, cost);
        st.setInt(2, id);
        st.execute();
        return true;
    }
    public boolean Delete() throws Exception {
        PreparedStatement ps = db.get.Prepare("DELETE FROM `cli_contracts` WHERE `id`=?");
        ps.setInt(1, id);
        ps.execute();
        return true;
    }

    public static ObservableList<Contracts> getData() throws Exception {
        ObservableList<Contracts> data = FXCollections.observableArrayList();
        ResultSet rs = db.get.getReportCon().createStatement().executeQuery("SELECT `cli_contracts`.`id`,`cli_clients`.`organization`, `cli_contracts`.`date_from`, `cli_contracts`.`date_to`, `cli_contracts`.`num_of_visits`, `cli_contracts`.`cost`, `cli_contracts`.`due_after`, `cli_contracts`.`has_taxs` FROM `cli_contracts`,`cli_clients` WHERE `cli_clients`.`id`=`cli_contracts`.`client_id`");
        while (rs.next()) {
            data.add(new Contracts(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8)));
        }
        return data;
    }

    public static String getAutoNum() throws Exception {
        return db.get.getTableData("SELECT IFNULL(MAX(`id`)+1,1) FROM `cli_contracts`").getValueAt(0, 0).toString();
    }

    public void getDocdown() throws Exception {

        File file = null;
        String selectSQL = "SELECT `doc` FROM `cli_contracts` WHERE `id`='" + id + "'";
        JTable tab = db.get.getTableData("SELECT `doc_ext` FROM `cli_contracts` WHERE `id`='" + id + "'");
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

                File directory = new File(System.getProperty("user.home") + "\\Desktop\\E-West\\contracts");
                directory.mkdirs();
                String dateFromSql = db.get.getTableData("SELECT  `date_from` FROM `cli_contracts` WHERE `id`='" + id + "'").getValueAt(0, 0).toString();

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
