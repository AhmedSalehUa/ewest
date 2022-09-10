/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Pruchases;

import EWest.EWest;
import assets.animation.BounceInRight;
import assets.animation.BounceInUp;
import assets.animation.ZoomInLeft;
import assets.animation.ZoomInRight;
import assets.classes.AlertDialogs;
import static assets.classes.statics.DEFAULT_THEME;
import static assets.classes.statics.NoPermission;
import static assets.classes.statics.THEME;
import clients.assets.ClientsStatics;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import db.User;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author AHMED
 */
public class PruchasesMainScreenController implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private JFXHamburger hamburg;
    Preferences prefs;
    @FXML
    private Label newReturned;
    @FXML
    private HBox chart;
    @FXML
    private VBox panel;
    @FXML
    private Button statistic;
    @FXML
    private VBox statisticsPane;
    @FXML
    private Label newOrders;
    @FXML
    private Label newClients;
    @FXML
    private Label allClients;
    @FXML
    private HBox Static;
    @FXML
    private HBox Static2;
    @FXML
    private HBox Static3;
    @FXML
    private HBox Static4;
    @FXML
    private Button projects;
    @FXML
    private Button contracts;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        prefs = Preferences.userNodeForPackage(EWest.class);
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        //Background work                       
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    try {
                                        setStatics();

                                        configDrawer();
                                        projects.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                                            try {
                                                new BounceInRight(panel).play();
                                                projects.setId("selectedNavBtn");
                                                contracts.setId("mainBtnEsp");
                                                statistic.setId("navBtn");

                                                Parent node = FXMLLoader.load(getClass().getResource(NoPermission));
                                                if (User.canAccess("PruchasesProjects")) {
                                                    node = FXMLLoader.load(getClass().getResource("/Pruchases/Operations.fxml"));
                                                    node.getStylesheets().clear();
                                                    node.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
                                                }
                                                borderpane.setCenter(node);

                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                                AlertDialogs.showErrors(ex);
                                            }
                                        });
                                        contracts.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                                            try {
                                                new BounceInRight(panel).play();
                                                contracts.setId("selectedNavBtnSpe");
                                                projects.setId("navBtn");
                                                statistic.setId("navBtn");
                                                Parent node = FXMLLoader.load(getClass().getResource(NoPermission));
                                                if (User.canAccess("PruchasesContracts")) {
                                                    node = FXMLLoader.load(getClass().getResource("/Pruchases/Contract.fxml"));
                                                    node.getStylesheets().clear();
                                                    node.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());
                                                }
                                                borderpane.setCenter(node);

                                            } catch (IOException ex) {
                                                ex.printStackTrace();
                                                AlertDialogs.showErrors(ex);
                                            }
                                        });

                                        statistic.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                                            try {
                                                new BounceInRight(panel).play();
                                                statistic.setId("selectedNavBtn");
                                                projects.setId("navBtn");
                                                contracts.setId("mainBtnEsp");
                                                borderpane.setCenter(statisticsPane);
                                                setStatics();
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                                AlertDialogs.showErrors(ex);
                                            }
                                        });
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                        AlertDialogs.showErrors(ex);

                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                } finally {
                                    latch.countDown();
                                }
                            }

                            private void configDrawer() {
                                try {

                                    AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/Navigator/SideNavigator.fxml"));
                                    anchorPane.getStylesheets().clear();
                                    anchorPane.getStylesheets().add(getClass().getResource("/assets/styles/" + prefs.get(THEME, DEFAULT_THEME) + ".css").toExternalForm());

                                    drawer.setSidePane(anchorPane);

                                    double drawerx = drawer.getLayoutX();
                                    double drawery = drawer.getLayoutY();
                                    drawer.setLayoutX(drawerx + 250);
                                    drawer.setLayoutY(drawery);
                                    drawer.setVisible(false);
                                    drawer.setMaxWidth(0);

                                    drawer.setOnDrawerOpening(event
                                            -> {
                                        drawer.setLayoutX(drawerx);
                                        drawer.setLayoutY(drawery);
                                        drawer.setMaxWidth(250);
                                    });

                                    drawer.setOnDrawerClosed(event
                                            -> {
                                        drawer.setLayoutX(drawerx + 250);
                                        drawer.setLayoutY(drawery);
                                        drawer.setVisible(false);
                                        drawer.setMaxWidth(0);
                                    });

                                    HamburgerBackArrowBasicTransition nav = new HamburgerBackArrowBasicTransition(hamburg);
                                    nav.setRate(nav.getRate() * -1);
                                    nav.play();
                                    hamburg.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
                                        nav.setRate(nav.getRate() * -1);
                                        nav.play();
                                        drawer.setVisible(true);
                                        if (drawer.isOpened()) {
                                            drawer.close();
                                        } else {
                                            drawer.open();
                                        }
                                    });
                                } catch (Exception e) {
                                    AlertDialogs.showErrors(e);
                                }
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
        new ZoomInLeft(projects).play();
        new ZoomInRight(contracts).play();
        new ZoomInRight(statistic).play();

    }

    private void setStatics() throws Exception {
        allClients.setText(ClientsStatics.getAllClient());
        newClients.setText(ClientsStatics.getNewClient());
        newOrders.setText(ClientsStatics.getAllInvoice());
        newReturned.setText(ClientsStatics.getAllReturnes());
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        final LineChart<String, Number> lineChart
                = new LineChart<String, Number>(xAxis, yAxis);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy");
        lineChart.setTitle("الاوردرات خلال  " + LocalDate.now().format(format));

        XYChart.Series series1 = ClientsStatics.getAllInvoiceChart();
        series1.setName("الاوردرات");

        XYChart.Series series2 = ClientsStatics.getAllReturnesChart();
        series2.setName("المرتجعات");

        XYChart.Series series3 = ClientsStatics.getAllClientChart();
        series3.setName("العملاء");

        lineChart.getData().addAll(series1, series2, series3);
        chart.getChildren().clear();
        chart.getChildren().add(lineChart);
        chart.setHgrow(lineChart, Priority.ALWAYS);

        new BounceInUp(Static).play();
        new BounceInUp(Static2).play();
        new BounceInUp(Static3).play();
        new BounceInUp(Static4).play();
    }
}
