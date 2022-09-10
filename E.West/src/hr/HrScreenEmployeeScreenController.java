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
import static assets.classes.statics.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class HrScreenEmployeeScreenController implements Initializable {

    Preferences prefs;
    @FXML
    private Button employee;
    @FXML
    private Button sections;
    BorderPane parent;
    @FXML
    private Button department;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(EWest.class);
        new ZoomInRight(employee).play();
        new ZoomInRight(department).play();
        new ZoomInLeft(sections).play();

    }

    public void setParent(BorderPane parent) {
        this.parent = parent;
    }

    @FXML
    private void openEmployee(ActionEvent event) {
        try {

            Parent mainMember = FXMLLoader.load(getClass().getResource(HrScreenEmployess));
            mainMember.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
            parent.setCenter(mainMember);
        } catch (IOException ex) {
            AlertDialogs.showErrors(ex);
        }

    }

    @FXML
    private void openSections(ActionEvent event) {
        try {
            Parent mainMember = FXMLLoader.load(getClass().getResource(HrScreenSections));
            mainMember.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
            parent.setCenter(mainMember);
        } catch (IOException ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void openDepartment(ActionEvent event) {
        try {
            Parent mainMember = FXMLLoader.load(getClass().getResource(HrScreenDepartment));
            mainMember.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
            parent.setCenter(mainMember);
        } catch (IOException ex) {
            AlertDialogs.showErrors(ex);
        }
    }

}
