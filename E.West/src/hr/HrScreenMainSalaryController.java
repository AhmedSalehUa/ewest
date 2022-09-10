/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr;
 
import EWest.EWest;
import assets.animation.ZoomInLeft;
import assets.animation.ZoomInRight;
import assets.classes.AlertDialogs;
import assets.classes.statics;
import static assets.classes.statics.* ;  
import hr.assets.Devices;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.prefs.Preferences;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage; 

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class HrScreenMainSalaryController implements Initializable {
 Preferences prefs;
    @FXML
    private Button individualReport;
    @FXML
    private Button allSalaryxReport;
    @FXML
    private Button clacAttend;
    @FXML
    private Button downloadAttendane;
 BorderPane parent;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) { prefs = Preferences.userNodeForPackage(EWest.class);
       new ZoomInRight(individualReport).play();
       new ZoomInRight(downloadAttendane).play();
        new ZoomInRight(allSalaryxReport).play();
        new ZoomInLeft(clacAttend).play();
       
    }     public void setParent(BorderPane parent) {
        this.parent = parent;
    }

    @FXML
    private void individualReport(ActionEvent event) {
        try {
            Parent mainMember = FXMLLoader.load(getClass().getResource(HrScreenIndividualReport));
            mainMember.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
            parent.setCenter(mainMember);
        } catch (IOException ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void allSalaryxReport(ActionEvent event) {
        try {
            Parent mainMember = FXMLLoader.load(getClass().getResource(HrScreenReports));
            mainMember.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
           parent.setCenter(mainMember);
        } catch (IOException ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void clacAttend(ActionEvent event) {
         try {
            Parent mainMember = FXMLLoader.load(getClass().getResource(HrScreenCalcAttend));
            mainMember.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
            parent.setCenter(mainMember);
        } catch (IOException ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void downloadAttendane(ActionEvent event) {
         try {
            ObservableList<Devices> data = Devices.getData();
            List<String> choices = new ArrayList<>();
            for (Devices a : data) {
                choices.add(a.getName());
            }

            ChoiceDialog<String> dialog = new ChoiceDialog<>(data.get(0).getName(), choices);
            dialog.setTitle("Choice Device");
            dialog.setHeaderText("اختار الجهاز");
            dialog.setContentText("اختار اسم الجهاز");
            dialog.setGraphic(new ImageView(this.getClass().getResource("/assets/icons/icons8_hdd_80px.png").toString()));
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) { 
                db.get.getReportCon().createStatement().execute("REPLACE INTO `att_target_devices`(`device_id`) select `att_machines`.`id` from `att_machines` where `att_machines`.`name`='" + result.get() + "'");
                AlertDialogs.showmessage("سيتم تنزيل البيانات قد يتسبب فى توقف مؤقت للبرنامج");
                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                final CountDownLatch latch = new CountDownLatch(1);

                                try {

                                    try {
                                        System.out.println(prefs.get(statics.COMMAND, COMMAND_DEFAULT) + prefs.get(statics.PYTHON_PATH, statics.PYTHON_PATH_DEFAULT) + statics.downloadAttendance + prefs.get(statics.EXTEND, EXTEND_DEFAULT));
                                        Process p = Runtime.getRuntime().exec(prefs.get(statics.COMMAND, COMMAND_DEFAULT) + prefs.get(statics.PYTHON_PATH, statics.PYTHON_PATH_DEFAULT) + statics.downloadAttendance + prefs.get(statics.EXTEND, EXTEND_DEFAULT));
                                        p.waitFor();
                                        BufferedReader bri = new BufferedReader(new InputStreamReader(p.getInputStream()));
                                        BufferedReader bre = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                                        String line;
                                        while ((line = bri.readLine()) != null) {
                                            a += "\n " + line;
                                        }
                                        bri.close();
                                        while ((line = bre.readLine()) != null) {

                                            b += "\n " + line;
                                        }
                                        bre.close();
                                        p.waitFor();

                                        p.destroy();

                                    } catch (Exception ex) {
                                        AlertDialogs.showErrors(ex);
                                    } finally {
                                        latch.countDown();
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                } finally {
                                    latch.countDown();
                                }

                                latch.await();

                                return null;
                            }

                        };

                    }

                    @Override
                    protected void succeeded() {
                        showMessage(a, b); 
                        super.succeeded();
                    }

                };
                service.start();

            }
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    } String a = "";
    String b = "";
     private void showMessage(String a, String b) {
        if (a.contains("cannt connect to db") || b.contains("cannt connect to db")) {
            AlertDialogs.showError("check database ip and running (192.168.1.90)");
        } else if (a.contains("Process terminate :") || b.contains("Process terminate :")) {
            AlertDialogs.showError("An Error Accured In Progress \n " +a+b);
        } else {
            AlertDialogs.showmessage("تم");
        }

    }
}
