/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

   
import EWest.EWest;
import assets.classes.statics; 
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.prefs.Preferences;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AHMED
 */
public class get {

    private static String url = "";
    private static Connection con;

    private static void setURL() throws Exception {
        try {
            Preferences prefs = Preferences.userNodeForPackage(EWest.class);

            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://" + prefs.get(statics.DATABASE_IP, "127.0.0.1") + ":3306/e_west?useUnicode=true&characterEncoding=UTF-8";

        } catch (ClassNotFoundException ex) {
            throw new Exception("error in database url");
        }

    }

    public static Connection getReportCon() throws Exception {
        try {
            if (con == null) {
                setURL();
                con = DriverManager.getConnection(url, "acapytradeahmedsaleh", "as01203904426");
            }
        } catch (SQLException ex) {
            throw new Exception("Error in connection to database ERROR Code: \n" + ex.getMessage());
        }
        return con;
    }

    public static boolean canCon() throws Exception {
        try {
                setURL();
                con = DriverManager.getConnection(url, "acapytradeahmedsaleh", "as01203904426");

        } catch (SQLException ex) {
           
            throw new Exception("Error in connection to database ERROR Code: \n" + ex.getMessage());
        }
        return true;
    }

    public static void setConnection() throws Exception {

        try {
            if (con == null) {
                setURL();
                con = DriverManager.getConnection(url, "acapytradeahmedsaleh", "as01203904426");
            }
        } catch (SQLException ex) {
            throw new Exception("Error in connection to database ERROR Code: \n" + ex.getMessage());

        }
    }

    public static JTable getTableData(String statement) throws Exception {

        try {
            setConnection();
            Statement st = con.createStatement();
            ResultSet rs;
            rs = st.executeQuery(statement);
            ResultSetMetaData rsmt = rs.getMetaData();
            int c = rsmt.getColumnCount();

            JTable table = new JTable(0, c);
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            while (rs.next()) {
                Object rows[] = new Object[c];

                for (int i = 0; i < c; i++) {
                    rows[i] = rs.getString(i + 1);
                }
                model.addRow(rows);
            }
//            con.close();
            return table;
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    public static boolean runNonQuery(String statement) throws Exception {
        try {
            setConnection();
            Statement stmt = con.createStatement();
            stmt.execute(statement);
//            con.close();
            return true;
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());

        }

    }

    public static boolean runNonQueryPrepare(PreparedStatement statement) throws Exception {
        try {
            setConnection();
            statement.execute();
//            con.close();
            return true;
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());

        }

    }

    public static boolean excutePrepare(PreparedStatement statement) throws Exception {
        try {
            setConnection();
            statement.execute();
//            con.close();
            return true;
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public static PreparedStatement Prepare(String statement) throws Exception {
        try {
            setConnection();
            PreparedStatement stat = con.prepareStatement(statement);
            return stat;
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());

        }

    }

    public static Statement getBatchStatement() throws Exception {
        try {
            setConnection();
            Statement st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            con.setAutoCommit(false);
            return st;
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        }

    }

    

}
