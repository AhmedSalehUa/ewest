package Pruchases;

import BaseData.assets.Clients;
import Pruchases.assets.Operations;
import assets.classes.AlertDialogs;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import hr.assets.Employee;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ListCell;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javafx.stage.Stage;
import sales.assets.SalesMembers;

/**
 * FXML Controller class
 *
 * @author Ahmed Al-Gazzar
 */
public class OperationsController implements Initializable {

    @FXML
    private JFXTextField search;
    @FXML
    private TableView<Operations> tab;
    @FXML
    private TableColumn<Operations, String> tabDate;
    @FXML
    private TableColumn<Operations, String> tabPay_type;
    @FXML
    private TableColumn<Operations, String> tabTotalcost;
    @FXML
    private TableColumn<Operations, String> tabClient_name;
    @FXML
    private TableColumn<Operations, String> tabSales_name;
    @FXML
    private TableColumn<Operations, String> tabId;

    @FXML
    private Label id;
    @FXML
    private ComboBox<Clients> client;
    @FXML
    private ComboBox<Employee> sales;
    @FXML
    private JFXDatePicker date;
    @FXML
    private TextField totalcost;

    @FXML
    private ProgressIndicator progress;
    @FXML
    private Button New;
    @FXML
    private Button Delete;
    @FXML
    private Button Edite;
    @FXML
    private Button Doc;
    @FXML
    private Button Add;
    @FXML
    private ComboBox<String> pay_type;
    @FXML
    private ImageView docdown;
    @FXML
    private TextField docpath;
    @FXML
    private AnchorPane detailsPane;
    @FXML
    private TabPane tabs;
    @FXML
    private AnchorPane costPane;
    @FXML
    private TableColumn<Operations, String> tabTotalSpended;
    @FXML
    private TextField totalSpended;
    @FXML
    private CheckBox addTaxes;
    @FXML
    private CheckBox withTaxs;
    @FXML
    private CheckBox noTaxs;
    @FXML
    private Button BuyOrder;
    @FXML
    private TextField yields;
    @FXML
    private TableColumn<Operations, String> tabTotalYield;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        date.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null) {
                    return "";
                }
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });
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
                                    clear();
                                    intialColumn();
                                    getData();
                                    fillCombos();
                                    configPanels();
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                } finally {
                                    latch.countDown();
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

                progress.setVisible(false);
                super.succeeded();
            }
        };
        service.start();

        tab.setOnMouseClicked((e) -> {
            if (tab.getSelectionModel().getSelectedIndex() == -1) {

            } else {
                Add.setDisable(true);
                Edite.setDisable(false);
                Delete.setDisable(false);
                New.setDisable(false);

                Operations selected = tab.getSelectionModel().getSelectedItem();
                id.setText(Integer.toString(selected.getId()));
                ObservableList<Clients> items1 = client.getItems();
                for (Clients a : items1) {
                    if (a.getOrganization().equals(selected.getClient_name())) {
                        client.getSelectionModel().select(a);
                    }
                }
                ObservableList<Employee> items2 = sales.getItems();
                for (Employee a : items2) {
                    if (a.getName().equals(selected.getSales_name())) {
                        sales.getSelectionModel().select(a);
                    }
                }
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                date.setValue(LocalDate.parse(selected.getDate()));

                totalcost.setText(selected.getTotal_cost());
                totalSpended.setText(selected.getTotal_spend());
                yields.setText(selected.getTotal_yield());
                pay_type.getSelectionModel().select(selected.getPay_type());
                tabs.setVisible(true);
                detailsController.setId(selected.getId());

//                memberController.setId(selected.getId());
                costControll.setId(selected.getId());
            }
        });
    }
    OperationsDetailsController detailsController;
    OperationsMembersController memberController;
    OperationsCostsController costControll;

    public void configPanels() {

        try {
            detailsPane.getChildren().clear();
            FXMLLoader fxShow = new FXMLLoader(getClass().getResource("OperationsDetails.fxml"));
            detailsPane.getChildren().add(fxShow.load());
            detailsController = fxShow.getController();
            detailsController.setParentController(OperationsController.this);

//            memberPane.getChildren().clear();
//            FXMLLoader fxEdite = new FXMLLoader(getClass().getResource("OperationsMembers.fxml"));
//            memberPane.getChildren().add(fxEdite.load());
//            memberController = fxEdite.getController();
//            memberController.setParentController(OperationsController.this);

            costPane.getChildren().clear();
            FXMLLoader fxCost = new FXMLLoader(getClass().getResource("OperationsCosts.fxml"));
            costPane.getChildren().add(fxCost.load());
            costControll = fxCost.getController();
            costControll.setParentController(OperationsController.this);
        } catch (IOException ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void attachFile(MouseEvent event) {
        FileChooser fil_chooser = new FileChooser();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = fil_chooser.showOpenDialog(st);

        if (file != null) {
            docpath.setText(file.getAbsolutePath());
        }

    }

    private void fillCombos() {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<Employee> salesData;
            ObservableList<Clients> clientData;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            clientData = Clients.getData();
                            salesData = Employee.getSales();
                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                        return null;
                    }
                };

            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);

                pay_type.getItems().add("كاش");
                pay_type.getItems().add("دفعات");
                client.setItems(clientData);
                client.setConverter(new StringConverter<Clients>() {
                    @Override
                    public String toString(Clients patient) {
                        return patient.getOrganization();
                    }

                    @Override
                    public Clients fromString(String string) {
                        return null;
                    }
                });
                client.setCellFactory(cell -> new ListCell<Clients>() {

                    // Create our layout here to be reused for each ListCell
                    GridPane gridPane = new GridPane();
                    Label lblid = new Label();
                    Label lblName = new Label();
                    Label lblOrg = new Label();

                    {
                        gridPane.getColumnConstraints().addAll(
                                new ColumnConstraints(100, 100, 100),
                                new ColumnConstraints(100, 100, 100),
                                new ColumnConstraints(100, 100, 100)
                        );

                        gridPane.add(lblid, 0, 1);
                        gridPane.add(lblName, 1, 1);
                        gridPane.add(lblOrg, 2, 1);

                    }

                    @Override
                    protected void updateItem(Clients person, boolean empty) {
                        super.updateItem(person, empty);

                        if (!empty && person != null) {

                            // Update our Labels
                            lblid.setText("م: " + Integer.toString(person.getId()));
                            lblName.setText("الاسم: " + person.getName());
                            lblOrg.setText("المؤسسة: " + person.getOrganization());

                            setGraphic(gridPane);
                        } else {
                            // Nothing to display here
                            setGraphic(null);
                        }
                    }
                });

                sales.setItems(salesData);
                sales.setConverter(new StringConverter<Employee>() {
                    @Override
                    public String toString(Employee patient) {
                        return patient.getName();
                    }

                    @Override
                    public Employee fromString(String string) {
                        return null;
                    }
                });
                sales.setCellFactory(cell -> new ListCell<Employee>() {

                    GridPane gridPane = new GridPane();
                    Label lblid = new Label();
                    Label lblName = new Label();

                    {
                        gridPane.getColumnConstraints().addAll(
                                new ColumnConstraints(100, 100, 100),
                                new ColumnConstraints(100, 100, 100)
                        );

                        gridPane.add(lblid, 0, 1);
                        gridPane.add(lblName, 1, 1);

                    }

                    @Override
                    protected void updateItem(Employee person, boolean empty) {
                        super.updateItem(person, empty);

                        if (!empty && person != null) {

                            lblid.setText("م: " + Integer.toString(person.getId()));
                            lblName.setText("الاسم: " + person.getName());

                            setGraphic(gridPane);
                        } else {
                            setGraphic(null);
                        }
                    }
                });
                super.succeeded();
            }
        };
        service.start();

    }

    public void setAccount(int id, String amount) {
        try {

            String total = totalcost.getText().isEmpty() ? "0" : totalcost.getText();
            totalcost.setText(Double.toString(Double.parseDouble(total) + Double.parseDouble(amount)));
            Operations.updateCost(id, totalcost.getText());
            setYields(id);
            getData();
            ObservableList<Operations> values = tab.getItems();
            for (Operations a : values) {
                if (a.getId() == id) {
                    tab.getSelectionModel().select(a);
                }
            }
        } catch (Exception e) {
            AlertDialogs.showErrors(e);
        }
    }

    public void reduceAccount(int id, String amount) {
        try {
            String total = totalcost.getText().isEmpty() ? "0" : totalcost.getText();
            totalcost.setText(Double.toString(Double.parseDouble(total) - Double.parseDouble(amount)));
            Operations.updateCost(id, totalcost.getText());
            setYields(id);
            getData(); ObservableList<Operations> values = tab.getItems();
            for (Operations a : values) {
                if (a.getId() == id) {
                    tab.getSelectionModel().select(a);
                }
            }
        } catch (Exception e) {
            AlertDialogs.showErrors(e);
        }
    }

    public void setSpended(int id, String amount) {
        try {

            String total = totalSpended.getText().isEmpty() ? "0" : totalSpended.getText();
            totalSpended.setText(Double.toString(Double.parseDouble(total) + Double.parseDouble(amount)));
            Operations.updateSpended(id, totalSpended.getText());
            setYields(id);
            getData();
            ObservableList<Operations> values = tab.getItems();
            for (Operations a : values) {
                if (a.getId() == id) {
                    tab.getSelectionModel().select(a);
                }
            }
        } catch (Exception e) {
            AlertDialogs.showErrors(e);
        }
    }

    public void reduceSpended(int id, String amount) {
        try {
            String total = totalSpended.getText().isEmpty() ? "0" : totalSpended.getText();
            totalSpended.setText(Double.toString(Double.parseDouble(total) - Double.parseDouble(amount)));
            Operations.updateSpended(id, totalSpended.getText());
            setYields(id);
            getData(); ObservableList<Operations> values = tab.getItems();
            for (Operations a : values) {
                if (a.getId() == id) {
                    tab.getSelectionModel().select(a);
                }
            }
        } catch (Exception e) {
            AlertDialogs.showErrors(e);
        }
    }

    public void setYields(int id) {
        try {
            String totalCost = totalcost.getText().isEmpty() ? "0" : totalcost.getText();
            String totalSpend = totalSpended.getText().isEmpty() ? "0" : totalSpended.getText();
            yields.setText(Double.toString(Double.parseDouble(totalCost) - Double.parseDouble(totalSpend)));
            Operations.updateYields(id, yields.getText());
            getData();
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }

    }

    private void getAutoNum() {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            String autoNum;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            autoNum = Operations.getAutoNum();
                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }
                        return null;
                    }
                };
            }

            @Override
            protected void succeeded() {
                progress.setVisible(false);
                id.setText(autoNum);
                super.succeeded();
            }
        };
        service.start();
    }

    private void clear() {
        getAutoNum();
        client.getSelectionModel().clearSelection();
        sales.getSelectionModel().clearSelection();
        totalcost.setText("0");
        totalSpended.setText("0");
        pay_type.getSelectionModel().clearSelection();
        date.setValue(null);
        docpath.setText("");
        yields.setText("");
        Add.setDisable(false);
        Edite.setDisable(true);
        Delete.setDisable(true);
        New.setDisable(true);
        tabs.setVisible(false);

        noTaxs.setSelected(false);
        withTaxs.setSelected(false);
        addTaxes.setSelected(false);
    }

    private void intialColumn() {
        tabId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tabClient_name.setCellValueFactory(new PropertyValueFactory<>("client_name"));
        tabSales_name.setCellValueFactory(new PropertyValueFactory<>("sales_name"));
        tabDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tabTotalcost.setCellValueFactory(new PropertyValueFactory<>("total_cost"));
        tabPay_type.setCellValueFactory(new PropertyValueFactory<>("pay_type"));
        tabTotalSpended.setCellValueFactory(new PropertyValueFactory<>("total_spend"));
        tabTotalYield.setCellValueFactory(new PropertyValueFactory<>("total_yield"));
    }

    private void getData() {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<Operations> data;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        try {
                            data = Operations.getData();

                        } catch (Exception ex) {
                            AlertDialogs.showErrors(ex);
                        }

                        return null;
                    }
                };
            }

            @Override
            protected void succeeded() {
                tab.setItems(data);
                items = data;
                progress.setVisible(false);
                super.succeeded();
            }
        };
        service.start();
    }
    ObservableList<Operations> items;

    @FXML
    private void search(KeyEvent event) {

        FilteredList<Operations> filteredData = new FilteredList<>(items, p -> true);

        filteredData.setPredicate(pa -> {

            if (search.getText() == null || search.getText().isEmpty()) {
                return true;
            }

            String lowerCaseFilter = search.getText().toLowerCase();

            if (pa.getClient_name().contains(lowerCaseFilter)
                    || pa.getSales_name().contains(lowerCaseFilter)
                    || pa.getDate().contains(lowerCaseFilter)
                    || pa.getPay_type().contains(lowerCaseFilter)) {
                return true;
            } else {
                return false;
            }

        });

        SortedList< Operations> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tab.comparatorProperty());
        tab.setItems(sortedData);
    }

    @FXML
    private void New(ActionEvent event) {
        clear();
    }

    @FXML
    private void Delete(ActionEvent event) {
        progress.setVisible(true);
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
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Deleting  operation");
                                    alert.setHeaderText("سيتم حذف العملية ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        Operations op = new Operations();
                                        op.setId(Integer.parseInt(id.getText()));
                                        op.Delete();
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);

                                } finally {
                                    latch.countDown();
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
                progress.setVisible(false);
                clear();
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void Edite(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            Operations op = new Operations();
            boolean ok = true;

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
                                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                    alert.setTitle("Editting  operation");
                                    alert.setHeaderText("سيتم تعديل العملية ");
                                    alert.setContentText("هل انت متاكد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        if (docpath.getText().isEmpty() || docpath.getText().length() == 0) {
                                            op.setId(Integer.parseInt(id.getText()));
                                            op.setClient_id(client.getSelectionModel().getSelectedItem().getId());
                                            op.setSales_id(sales.getSelectionModel().getSelectedItem().getId());
                                            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                            op.setDate(date.getValue().format(format));
                                            op.setTotal_cost(totalcost.getText());
                                            op.setTotal_spend(totalSpended.getText());
                                            op.setTotal_yield(yields.getText());
                                            op.setPay_type(pay_type.getSelectionModel().getSelectedItem());

                                            op.setHasTaxs(noTaxs.isSelected() ? "false" : "true");
                                            op.EditeWithouPhoto();
                                        } else {
                                            op.setId(Integer.parseInt(id.getText()));
                                            op.setClient_id(client.getSelectionModel().getSelectedItem().getId());
                                            op.setSales_id(sales.getSelectionModel().getSelectedItem().getId());
                                            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                            op.setDate(date.getValue().format(format));
                                            op.setTotal_cost(totalcost.getText());
                                            op.setTotal_spend(totalSpended.getText());
                                            op.setTotal_yield(yields.getText());
                                            op.setPay_type(pay_type.getSelectionModel().getSelectedItem());

                                            op.setHasTaxs(noTaxs.isSelected() ? "false" : "true");
                                            InputStream in = new FileInputStream(new File(docpath.getText()));
                                            op.setDoc(in);

                                            String[] st = docpath.getText().split(Pattern.quote("."));
                                            op.setDoc_ext(st[st.length - 1]);

                                            op.Edit();
                                        }
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                    ok = false;
                                } finally {
                                    latch.countDown();
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
                progress.setVisible(false);
                if (ok) {
                    clear();

                }
                getData();

                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void Add(ActionEvent event) {

        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            Operations op = new Operations();
            boolean ok = true;

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
                                    if (docpath.getText().isEmpty() || docpath.getText().length() == 0) {
                                        op.setId(Integer.parseInt(id.getText()));
                                        op.setClient_id(client.getSelectionModel().getSelectedItem().getId());
                                        op.setSales_id(sales.getSelectionModel().getSelectedItem().getId());
                                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        op.setDate(date.getValue().format(format));

                                        op.setHasTaxs(noTaxs.isSelected() ? "false" : "true");
                                        op.setTotal_cost(totalcost.getText());
                                        op.setTotal_spend(totalSpended.getText());
                                        op.setTotal_yield(yields.getText());
                                        op.setPay_type(pay_type.getSelectionModel().getSelectedItem());

                                        op.AddWithouPhoto();
                                    } else {
                                        op.setId(Integer.parseInt(id.getText()));
                                        op.setClient_id(client.getSelectionModel().getSelectedItem().getId());
                                        op.setSales_id(sales.getSelectionModel().getSelectedItem().getId());
                                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        op.setDate(date.getValue().format(format));

                                        op.setHasTaxs(noTaxs.isSelected() ? "false" : "true");
                                        InputStream in = new FileInputStream(new File(docpath.getText()));
                                        op.setDoc(in);

                                        String[] st = docpath.getText().split(Pattern.quote("."));
                                        op.setDoc_ext(st[st.length - 1]);

                                        op.setTotal_cost(totalcost.getText());
                                        op.setTotal_spend(totalSpended.getText());
                                        op.setTotal_yield(yields.getText());
                                        op.setPay_type(pay_type.getSelectionModel().getSelectedItem());

                                        op.Add();
                                    }
                                } catch (Exception ex) {
                                    AlertDialogs.showErrors(ex);
                                    ok = false;
                                } finally {
                                    latch.countDown();
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
                progress.setVisible(false);
                if (ok) {
                    clear();

                }
                getData();
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    public void getdoc(ActionEvent event) {

        try {
            if (tab.getSelectionModel().getSelectedIndex() == -1) {
                AlertDialogs.showmessage("اختار من الجدول اولا");
            } else {
                Operations op = new Operations();
                op.setId(tab.getSelectionModel().getSelectedItem().getId());
                op.getDocdown();
            }
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    @FXML
    private void addTaxes(ActionEvent event) {
        if (!addTaxes.isSelected()) {
            withTaxs.setSelected(false);
            noTaxs.setSelected(false);
            String total = totalcost.getText();
            String afterTaxes = Double.toString((Double.parseDouble(total) * 0.14) + Double.parseDouble(total));

            totalcost.setText(afterTaxes);
        } else {

            String total = totalcost.getText();
            String afterTaxes = Double.toString((Double.parseDouble(total) * 100) / 114);

            totalcost.setText(afterTaxes);
        }
    }

    @FXML
    private void hasTaxes(ActionEvent event) {

        if (addTaxes.isSelected()) {

            String total = totalcost.getText();
            String afterTaxes = Double.toString((Double.parseDouble(total) * 100) / 114);

            totalcost.setText(afterTaxes);
        }
        addTaxes.setSelected(false);

        noTaxs.setSelected(false);
    }

    @FXML
    private void noTaxs(ActionEvent event) {
        if (addTaxes.isSelected()) {

            String total = totalcost.getText();
            String afterTaxes = Double.toString((Double.parseDouble(total) * 100) / 114);

            totalcost.setText(afterTaxes);
        }
        addTaxes.setSelected(false);
        withTaxs.setSelected(false);
    }

    @FXML
    private void createBuyOrder(ActionEvent event) {
    }

}
