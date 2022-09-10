/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Navigator;

import EWest.EWest;
import assets.classes.AlertDialogs;
import assets.classes.statics;
import static assets.classes.statics.*; 
import db.User;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * FXML Controller class
 *
 * @author ahmed
 */
public class SideNavigatorController implements Initializable {

    @FXML
    private Label mainLabel;
    @FXML
    private ImageView imgview;

    Preferences prefs; 
    @FXML
    private Button mainData;
    @FXML
    private Button Sales;
    @FXML
    private Button buying;
    @FXML
    private Button Accounts;
    @FXML
    private Button Hr;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(EWest.class);
        mainLabel.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            try {

                Parent login = FXMLLoader.load(getClass().getResource("/Navigator/Home.fxml"));login.getStylesheets().clear();
                login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(statics.THEME, statics.DEFAULT_THEME) + ".css").toExternalForm());

                Scene current = (Scene) mainLabel.getScene();
                Stage st = (Stage) mainLabel.getScene().getWindow();
                Scene sc = new Scene(login, current.getWidth(), current.getHeight());
                st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                st.setTitle("E-West");
                st.setScene(sc);
                st.show();
            } catch (IOException ex) {
                AlertDialogs.showErrors(ex);
            }
        });
        mainData.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("BaseDataScreen")) {
                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                //Background work                       
                                final CountDownLatch latch = new CountDownLatch(1);
                                Platform.runLater(() -> {
                                    try {
                                        try {
                                            Parent mainData = FXMLLoader.load(getClass().getResource(BaseData));
                                            mainData.getStylesheets().clear();
                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
                                            Scene current = ((Node) e.getSource()).getScene();
                                            Scene sc = new Scene(mainData, current.getWidth(), current.getHeight());
                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                                            st.setTitle("E-West (البيانات الرئيسية)");
                                            st.setScene(sc);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                            AlertDialogs.showErrors(ex);
                                        }
                                    } finally {
                                        latch.countDown();
                                    }
                                });
                                latch.await();

                                return null;
                            }
                        };
                    }

                    @Override
                    protected void succeeded() {

                        super.succeeded();
                    }
                };
                service.start();
            } else {
                AlertDialogs.permissionDenied();
            }
        });
        Sales.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("Sales")) {
                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                //Background work                       
                                final CountDownLatch latch = new CountDownLatch(1);
                                Platform.runLater(() -> {
                                    try {
                                        try {
                                            Parent mainData = FXMLLoader.load(getClass().getResource(PruchasesScreen));
                                            mainData.getStylesheets().clear();
                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
                                            Scene current = ((Node) e.getSource()).getScene();
                                            Scene sc = new Scene(mainData, current.getWidth(), current.getHeight());
                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                                            st.setTitle("E-West (المبيعات)");
                                            st.setScene(sc);

                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                            AlertDialogs.showErrors(ex);
                                        }
                                    } finally {
                                        latch.countDown();
                                    }
                                });
                                latch.await();

                                return null;
                            }
                        };
                    }

                    @Override
                    protected void succeeded() {

                        super.succeeded();
                    }
                };
                service.start();
            } else {
                AlertDialogs.permissionDenied();
            }
        });
         Hr.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            if (User.canAccess("Hr")) {
                Service<Void> service = new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                //Background work                       
                                final CountDownLatch latch = new CountDownLatch(1);
                                Platform.runLater(() -> {
                                    try {
                                        try {
                                            Parent mainData = FXMLLoader.load(getClass().getResource(HrScreen));
                                            mainData.getStylesheets().clear();
                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
                                            Scene current = ((Node) e.getSource()).getScene();
                                            Scene sc = new Scene(mainData, current.getWidth(), current.getHeight());
                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                                            st.setTitle("E-West (شؤون الموظفين)");
                                            st.setScene(sc);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                            AlertDialogs.showErrors(ex);
                                        }
                                    } finally {
                                        latch.countDown();
                                    }
                                });
                                latch.await();

                                return null;
                            }
                        };
                    }

                    @Override
                    protected void succeeded() {

                        super.succeeded();
                    }
                };
                service.start();
            } else {
                AlertDialogs.permissionDenied();
            }

        });
    }

}
