package Navigator;

import EWest.EWest;
import assets.animation.ZoomInLeft;
import assets.animation.ZoomInRight;
import assets.classes.AlertDialogs;
import static assets.classes.statics.*; 
import clients.assets.ClientsStatics;
import com.jfoenix.controls.JFXComboBox;
import db.User;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Pair;

public class HomeController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Button Accounts; 

    Preferences prefs;
    @FXML
    private MenuButton userNode;
    @FXML
    private Label userName;
    @FXML
    private Button Hr;
    @FXML
    private Button Sales;  
    @FXML
    private BorderPane borderpane;
    @FXML
    private VBox statisticsPane;
    @FXML
    private HBox Static;
    @FXML
    private Label newReturned;
    @FXML
    private HBox Static2;
    @FXML
    private Label newOrders;
    @FXML
    private HBox Static3;
    @FXML
    private Label newClients;
    @FXML
    private HBox Static4;
    @FXML
    private Label allClients;
    @FXML
    private VBox panel;
    @FXML
    private HBox pieCharts;
    @FXML
    private JFXComboBox<String> colorSelection;
    @FXML
    private Button mainData;
    @FXML
    private Button buying;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(EWest.class); setStatics();
        new ZoomInRight(Hr).play();
//        new ZoomInRight(provider).play();
        new ZoomInLeft(Accounts).play();
        new ZoomInRight(mainData).play();
        new ZoomInLeft(buying).play();
        new ZoomInRight(Sales).play();
        colorSelection.getItems().add("blueOrange");
        colorSelection.getItems().add("lightMode");
        colorSelection.getItems().add("darkMode");
        colorSelection.getSelectionModel().select(prefs.get(THEME, DEFAULT_THEME));
        userName.setText(prefs.get(USER_NAME, USERNAME_DEFAULT).toUpperCase());
        MenuItem logout = new MenuItem("Log Out");
        MenuItem changePassword = new MenuItem("change password");
        MenuItem controlPanel = new MenuItem("Control Panel");
        userNode.getItems().add(logout);
        userNode.getItems().add(changePassword);
        if (!prefs.get(USER_ROLE, "user").equals("user")) {
            userNode.getItems().add(controlPanel);
        }
       
        logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Parent login = FXMLLoader.load(getClass().getResource(Login));
                    System.err.println(prefs.get(THEME, DEFAULT_THEME));
                    login.getStylesheets().clear();
                    login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());

                    Stage st = (Stage) mainData.getScene().getWindow();
                    st.close();
                    st = new Stage();
                    st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                    st.setTitle("E-West");

                    double width = 350;
                    double height = 530;

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    st.setX((screenBounds.getWidth() - width) / 2);
                    st.setY((screenBounds.getHeight() - height) / 2);

                    Scene scene = new Scene(login, width, height);
                    st.setScene(scene);
                    st.show();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Alert dialod = new Alert(AlertType.ERROR);
                    dialod.setContentText(ex.getMessage());
                    dialod.show();
                }
            }
        });

        changePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Dialog<Pair<String, String>> dialog = new Dialog<>();
                dialog.setTitle("Change Password");
                dialog.setHeaderText("You Are About Changing Your Password");

                dialog.setGraphic(new ImageView(this.getClass().getResource("/assets/icons/icons8_password_200px_3.png").toString()));

                ButtonType loginButtonType = new ButtonType("Change", ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20, 150, 10, 10));

                PasswordField username = new PasswordField();
                username.setPromptText("Password");
                PasswordField password = new PasswordField();
                password.setPromptText("Confirm Password");

                grid.add(new Label("Password:"), 0, 0);
                grid.add(username, 1, 0);
                grid.add(new Label("Confirm Password:"), 0, 1);
                grid.add(password, 1, 1);

                Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
                loginButton.setDisable(true);

                password.textProperty().addListener((observable, oldValue, newValue) -> {
                    loginButton.setDisable(!password.getText().equals(username.getText()));
                });
                username.textProperty().addListener((observable, oldValue, newValue) -> {
                    loginButton.setDisable(!password.getText().equals(username.getText()));
                });

                dialog.getDialogPane().setContent(grid);

                Platform.runLater(() -> username.requestFocus());

                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == loginButtonType) {
                        return new Pair<>(username.getText(), password.getText());
                    }
                    return null;
                });

                Optional<Pair<String, String>> result = dialog.showAndWait();

                result.ifPresent(usernamePassword -> {
                    User us = new User();
                    us.setId(Integer.parseInt(prefs.get(USER_ID, "0")));
                    us.setPassword(usernamePassword.getKey());

                    if (us.changePassword()) {
                        AlertDialogs.showmessage("تم");
                    } else {
                        AlertDialogs.showError("حدث خطا");
                    }
                });
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
//        Store.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
//            if (User.canAccess("Store")) {
//                Service<Void> service = new Service<Void>() {
//                    @Override
//                    protected Task<Void> createTask() {
//                        return new Task<Void>() {
//                            @Override
//                            protected Void call() throws Exception {
//                                //Background work                       
//                                final CountDownLatch latch = new CountDownLatch(1);
//                                Platform.runLater(() -> {
//                                    try {
//                                        try {
//                                            Parent mainData = FXMLLoader.load(getClass().getResource(StoreScreen));
//                                            mainData.getStylesheets().clear();
//                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
//                                            Scene current = ((Node) e.getSource()).getScene();
//                                            Scene sc = new Scene(mainData, current.getWidth(), current.getHeight());
//                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
//                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
//                                            st.setTitle("E-West (المخازن)");
//                                            st.setScene(sc);
//                                        } catch (IOException ex) {
//                                            ex.printStackTrace();
//                                            AlertDialogs.showErrors(ex);
//                                        }
//                                    } finally {
//                                        latch.countDown();
//                                    }
//                                });
//                                latch.await();
//
//                                return null;
//                            }
//                        };
//                    }
//
//                    @Override
//                    protected void succeeded() {
//
//                        super.succeeded();
//                    }
//                };
//                service.start();
//            } else {
//                AlertDialogs.permissionDenied();
//            }
//
//        });
//        Accounts.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
//            if (User.canAccess("Accounts")) {
//                Service<Void> service = new Service<Void>() {
//                    @Override
//                    protected Task<Void> createTask() {
//                        return new Task<Void>() {
//                            @Override
//                            protected Void call() throws Exception {
//                                //Background work                       
//                                final CountDownLatch latch = new CountDownLatch(1);
//                                Platform.runLater(() -> {
//                                    try {
//                                        try {
//                                            Parent mainData = FXMLLoader.load(getClass().getResource(AccountsScreen));
//                                            mainData.getStylesheets().clear();
//                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
//                                            Scene current = ((Node) e.getSource()).getScene();
//                                            Scene sc = new Scene(mainData, current.getWidth(), current.getHeight());
//                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
//                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
//                                            st.setTitle("E-West (الحسابات)");
//                                            st.setScene(sc);
//                                        } catch (IOException ex) {
//                                            ex.printStackTrace();
//                                            AlertDialogs.showErrors(ex);
//                                        }
//                                    } finally {
//                                        latch.countDown();
//                                    }
//                                });
//                                latch.await();
//
//                                return null;
//                            }
//                        };
//                    }
//
//                    @Override
//                    protected void succeeded() {
//
//                        super.succeeded();
//                    }
//                };
//                service.start();
//            } else {
//                AlertDialogs.permissionDenied();
//            }
//
//        });
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
//        provider.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
//            if (User.canAccess("provider")) {
//                Service<Void> service = new Service<Void>() {
//                    @Override
//                    protected Task<Void> createTask() {
//                        return new Task<Void>() {
//                            @Override
//                            protected Void call() throws Exception {
//                                //Background work                       
//                                final CountDownLatch latch = new CountDownLatch(1);
//                                Platform.runLater(() -> {
//                                    try {
//                                        try {
//                                            Parent mainData = FXMLLoader.load(getClass().getResource(ProviderScreen));
//                                            mainData.getStylesheets().clear();
//                                            mainData.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
//                                            Scene current = ((Node) e.getSource()).getScene();
//                                            Scene sc = new Scene(mainData, current.getWidth(), current.getHeight());
//                                            Stage st = (Stage) ((Node) e.getSource()).getScene().getWindow();
//                                            st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
//                                            st.setTitle("E-West (الموردين)");
//                                            st.setScene(sc);
//                                        } catch (IOException ex) {
//                                            ex.printStackTrace();
//                                            AlertDialogs.showErrors(ex);
//                                        }
//                                    } finally {
//                                        latch.countDown();
//                                    }
//                                });
//                                latch.await();
//
//                                return null;
//                            }
//                        };
//                    }
//
//                    @Override
//                    protected void succeeded() {
//
//                        super.succeeded();
//                    }
//                };
//                service.start();
//            } else {
//                AlertDialogs.permissionDenied();
//            }
//
//        });

        controlPanel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Parent login = FXMLLoader.load(getClass().getResource(controlPanelScreen));
                    login.getStylesheets().clear();
                    login.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());

                    Stage st = (Stage) mainData.getScene().getWindow();
                    st.getIcons().add(new Image(getClass().getResourceAsStream("/assets/icons/logo.png")));
                    st.setTitle("E-West (Control panel)");

                    double width = 1360;
                    double height = 768;

                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    st.setX((screenBounds.getWidth() - width) / 2);
                    st.setY((screenBounds.getHeight() - height) / 2);

                    Scene scene = new Scene(login, width, height);
                    st.setScene(scene);
                    st.show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Alert dialod = new Alert(AlertType.ERROR);
                    dialod.setContentText(ex.getMessage());
                    dialod.show();
                }
            }
        });

    }

    private void setStatics() {
        try {
             allClients.setText(ClientsStatics.getAllClient());
        newClients.setText(ClientsStatics.getNewClient());
        newOrders.setText(ClientsStatics.getAllInvoice());
        newReturned.setText(ClientsStatics.getAllReturnes());
            LineChart clientLineChart = HomeStatics.getClientLineChart();
            statisticsPane.getChildren().add(clientLineChart);
            statisticsPane.setVgrow(clientLineChart, Priority.ALWAYS);

            LineChart providerLineChart = HomeStatics.getProviersLineChart();
            statisticsPane.getChildren().add(providerLineChart);
            statisticsPane.setVgrow(providerLineChart, Priority.ALWAYS);

            PieChart productCatPie = HomeStatics.getProductCatPie();
            pieCharts.getChildren().add(productCatPie);

            BarChart accountBalance = HomeStatics.getAccountBalance();
            pieCharts.getChildren().add(accountBalance);
            new ZoomInRight(Static).play();
            new ZoomInRight(Static2).play();
            new ZoomInRight(Static3).play();
            new ZoomInRight(Static4).play();
            new ZoomInRight(pieCharts).play();
            new ZoomInRight(statisticsPane).play();
        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void saveColor(ActionEvent e) {
        prefs.put(THEME, colorSelection.getSelectionModel().getSelectedItem());
        ((Node) e.getSource()).getScene().getStylesheets().clear();

        ((Node) e.getSource()).getScene().getStylesheets().add(getClass().getResource("/assets/styles/" + colorSelection.getSelectionModel().getSelectedItem() + ".css").toExternalForm());
    }

}
