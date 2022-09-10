package Pruchases;

import BaseData.assets.Clients;
import Pruchases.assets.Contracts; 
import assets.classes.AlertDialogs;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.ObservableList;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator; 
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author amran
 */
public class ContractController implements Initializable {

    @FXML
    private JFXTextField search;
    @FXML
    private TableView<Contracts> tab;
    @FXML
    private TableColumn<Contracts, Date> tab_dueto;
    @FXML
    private TableColumn<Contracts, String> tab_cost;
    @FXML
    private TableColumn<Contracts, String> tab_noVisits;
    @FXML
    private TableColumn<Contracts, String> tab_dateto;
    @FXML
    private TableColumn<Contracts, String> tab_datefrom;
    @FXML
    private TableColumn<Contracts, String> tab_clientName;
    @FXML
    private TableColumn<Contracts, String> tab_id;
    @FXML
    private Label contrctId;
    @FXML
    private ComboBox<Clients> clientName;
    @FXML
    private TextField noVisits;
    @FXML
    private TextField cost;
    @FXML
    private ComboBox<String> due_to;
    @FXML
    private DatePicker date_from;
    @FXML
    private ProgressIndicator progress;
    @FXML
    private Button formNew;
    @FXML
    private Button formDelete;
    @FXML
    private Button formEdite;
    @FXML
    private Button formAdd;
    @FXML
    private DatePicker date_to;

    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @FXML
    private AnchorPane VisitsPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private CheckBox addTaxes;
    @FXML
    private CheckBox withTaxs;
    @FXML
    private ImageView docdown;
    @FXML
    private TextField docpath;
    @FXML
    private Button DocShow_btn;
    @FXML
    private CheckBox noTaxs;
    @FXML
    private AnchorPane detailsPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        date_from.setConverter(new StringConverter<LocalDate>() {
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
        date_to.setConverter(new StringConverter<LocalDate>() {
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
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        final CountDownLatch latch = new CountDownLatch(1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    clear();
                                    intialColumn();
                                    getData();
                                    fillCombo();
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
                formNew.setDisable(false);

                formDelete.setDisable(false);

                formEdite.setDisable(false);

                formAdd.setDisable(true);

                Contracts selected = tab.getSelectionModel().getSelectedItem();
                contrctId.setText(Integer.toString(selected.getId()));
                date_from.setValue(LocalDate.parse(selected.getDate_from()));
                date_to.setValue(LocalDate.parse(selected.getDate_to()));
                noVisits.setText(selected.getNoVisits());
                cost.setText(selected.getCost());
                due_to.getSelectionModel().select(selected.getDue_after());

                ObservableList<Clients> items1 = clientName.getItems();
                for (Clients a : items1) {
                    if (a.getOrganization().equals(selected.getName())) {
                        clientName.getSelectionModel().select(a);
                    }
                }
                tabPane.setVisible(true);
                visitsController.setId(selected.getId());
                detailsController.setId(selected.getId());
            }
        });
    }
    ContractVisitsController visitsController;
    ContractDetailsController detailsController;

    public void configPanels() {

        try {
            VisitsPane.getChildren().clear();
            FXMLLoader fxShow = new FXMLLoader(getClass().getResource("ContractVisits.fxml"));
            VisitsPane.getChildren().add(fxShow.load());
            visitsController = fxShow.getController();
            visitsController.setParentController(ContractController.this);

            detailsPane.getChildren().clear();
            FXMLLoader fxCost = new FXMLLoader(getClass().getResource("ContractDetails.fxml"));
            detailsPane.getChildren().add(fxCost.load());
            detailsController = fxCost.getController();
            detailsController.setParentController(ContractController.this);

        } catch (IOException ex) {
            AlertDialogs.showErrors(ex);
        }
    }

    private void intialColumn() {
        tab_id.setCellValueFactory(new PropertyValueFactory<>("id"));

        tab_clientName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tab_datefrom.setCellValueFactory(new PropertyValueFactory<>("date_from"));

        tab_dateto.setCellValueFactory(new PropertyValueFactory<>("date_to"));

        tab_noVisits.setCellValueFactory(new PropertyValueFactory<>("noVisits"));

        tab_cost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        tab_dueto.setCellValueFactory(new PropertyValueFactory<>("due_after"));

    }

    private void clear() {
        getAutoNum();
        formNew.setDisable(true);

        formDelete.setDisable(true);

        formEdite.setDisable(true);

        formAdd.setDisable(false);

        clientName.getSelectionModel().clearSelection();
        date_from.setValue(null);
        date_to.setValue(null);
        noVisits.setText("");
        cost.setText("0");
        due_to.getSelectionModel().clearSelection();
        tabPane.setVisible(false);

        noTaxs.setSelected(false);
        withTaxs.setSelected(false);
        addTaxes.setSelected(false);
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
                            autoNum = Contracts.getAutoNum();

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
                contrctId.setText(autoNum);
                super.succeeded();
            }
        };
        service.start();

    }

    private void getData() {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<Contracts> data;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {

                        try {
                            data = Contracts.getData();

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
                tab.setItems(data);
                super.succeeded();
            }
        };
        service.start();
    }

    private void fillCombo() {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            ObservableList<Clients> data;

            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            data = Clients.getData();

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
                clientName.setItems(data);
                clientName.setConverter(new StringConverter<Clients>() {
                    @Override
                    public String toString(Clients patient) {
                        return patient.getOrganization();
                    }

                    @Override
                    public Clients fromString(String string) {
                        return null;
                    }
                });
                clientName.setCellFactory(cell -> new ListCell<Clients>() {

                    // Create our layout here to be reused for each ListCell
                    GridPane gridPane = new GridPane();
                    Label lblid = new Label();
                    Label lblName = new Label();
                    Label lblOrg = new Label();

                    // Static block to configure our layout
                    {
                        // Ensure all our column widths are constant
                        gridPane.getColumnConstraints().addAll(
                                new ColumnConstraints(100, 100, 100),
                                new ColumnConstraints(100, 100, 100),
                                new ColumnConstraints(100, 100, 100)
                        );

                        gridPane.add(lblid, 0, 1);
                        gridPane.add(lblName, 1, 1);
                        gridPane.add(lblOrg, 2, 1);

                    }
                    // We override the updateItem() method in order to provide our own layout for this Cell's graphicProperty

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
                super.succeeded();
            }
        };
        service.start();
        due_to.getItems().addAll("سنوي", "نص سنوي", "ربع سنوي", "شهري");

    }

    @FXML
    private void search(KeyEvent event) {
    }

    @FXML
    private void New(ActionEvent event) {
        clear();
    }

     public void setAccount(int id, String amount) {
        try {

            String total = cost.getText().isEmpty() ? "0" : cost.getText();
            cost.setText(Double.toString(Double.parseDouble(total) + Double.parseDouble(amount)));
            Contracts.updateCost(id, cost.getText()); 
            getData();
            ObservableList<Contracts> values = tab.getItems();
            for (Contracts a : values) {
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
            String total = cost.getText().isEmpty() ? "0" : cost.getText();
            cost.setText(Double.toString(Double.parseDouble(total) - Double.parseDouble(amount)));
            Contracts.updateCost(id, cost.getText());
           
            getData();
            ObservableList<Contracts> values = tab.getItems();
            for (Contracts a : values) {
                if (a.getId() == id) {
                    tab.getSelectionModel().select(a);
                }
            }
        } catch (Exception e) {
            AlertDialogs.showErrors(e);
        }
    }
    @FXML
    private void Delete(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
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
                                    alert.setTitle("Deleting  Contract");
                                    alert.setHeaderText("سيتم حذف العقد ");
                                    alert.setContentText("هل انتالعقد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        Contracts pr = new Contracts();
                                        pr.setId(Integer.parseInt(contrctId.getText()));
                                        pr.Delete();
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
                    getData();
                }
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void Edite(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            boolean ok = true;
            Contracts pr = new Contracts();

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
                                    alert.setTitle("Editting  Contract");
                                    alert.setHeaderText("سيتم تعديل العقد ");
                                    alert.setContentText("هل انتالعقد؟");

                                    Optional<ButtonType> result = alert.showAndWait();
                                    if (result.get() == ButtonType.OK) {
                                        if (docpath.getText().isEmpty() || docpath.getText().length() == 0) {
                                            pr.setId(Integer.parseInt(contrctId.getText()));
                                            pr.setCli_id(clientName.getSelectionModel().getSelectedItem().getId());
                                            pr.setDate_from(date_from.getValue().format(format));
                                            pr.setDate_to(date_to.getValue().format(format));
                                            pr.setNoVisits(noVisits.getText());
                                            pr.setCost(cost.getText());
                                            pr.setHasTaxs(noTaxs.isSelected() ? "false" : "true");
                                            pr.setDue_after(due_to.getSelectionModel().getSelectedItem());
                                            pr.EditeWithouPhoto();
                                        } else {

                                            pr.setId(Integer.parseInt(contrctId.getText()));
                                            pr.setCli_id(clientName.getSelectionModel().getSelectedItem().getId());
                                            pr.setDate_from(date_from.getValue().format(format));
                                            pr.setDate_to(date_to.getValue().format(format));
                                            pr.setNoVisits(noVisits.getText());
                                            pr.setCost(cost.getText());
                                            pr.setHasTaxs(noTaxs.isSelected() ? "false" : "true");
                                            pr.setDue_after(due_to.getSelectionModel().getSelectedItem());
                                            InputStream in = new FileInputStream(new File(docpath.getText()));
                                            pr.setDoc(in);

                                            String[] st = docpath.getText().split(Pattern.quote("."));
                                            pr.setDoc_path(st[st.length - 1]);
                                            pr.Edite();
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
                    getData();
                }
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void Add(ActionEvent event) {
        progress.setVisible(true);
        Service<Void> service = new Service<Void>() {
            boolean ok = true;
            Contracts pr = new Contracts();

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
                                        pr.setId(Integer.parseInt(contrctId.getText()));
                                        pr.setCli_id(clientName.getSelectionModel().getSelectedItem().getId());
                                        pr.setDate_from(date_from.getValue().format(format));
                                        pr.setDate_to(date_to.getValue().format(format));
                                        pr.setNoVisits(noVisits.getText());
                                        pr.setHasTaxs(noTaxs.isSelected() ? "false" : "true");
                                        pr.setCost(cost.getText());
                                        pr.setDue_after(due_to.getSelectionModel().getSelectedItem());
                                        pr.AddWithouPhoto();
                                    } else {
                                        pr.setId(Integer.parseInt(contrctId.getText()));
                                        pr.setCli_id(clientName.getSelectionModel().getSelectedItem().getId());
                                        pr.setDate_from(date_from.getValue().format(format));
                                        pr.setDate_to(date_to.getValue().format(format));
                                        pr.setNoVisits(noVisits.getText());
                                        pr.setCost(cost.getText());
                                        pr.setHasTaxs(noTaxs.isSelected() ? "false" : "true");
                                        pr.setDue_after(due_to.getSelectionModel().getSelectedItem());
                                        InputStream in = new FileInputStream(new File(docpath.getText()));
                                        pr.setDoc(in);

                                        String[] st = docpath.getText().split(Pattern.quote("."));
                                        pr.setDoc_path(st[st.length - 1]);
                                        pr.Add();
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
                    getData();
                }
                super.succeeded();
            }
        };
        service.start();
    }

    @FXML
    private void addTaxes(ActionEvent event) {
        if (withTaxs.isSelected()) {
            withTaxs.setSelected(false);
            noTaxs.setSelected(false);

            String total = cost.getText();
            String afterTaxes = Double.toString((Double.parseDouble(total) * 0.14) + Double.parseDouble(total));

            cost.setText(afterTaxes);
        } else {
            withTaxs.setSelected(true);
            String total = cost.getText();
            String afterTaxes = Double.toString((Double.parseDouble(total) * 100) / 114);

            cost.setText(afterTaxes);
        }

    }

    @FXML
    private void hasTaxes(ActionEvent event) {

        addTaxes.setSelected(false);

        noTaxs.setSelected(false);
    }

    @FXML
    private void noTaxs(ActionEvent event) {
        addTaxes.setSelected(false);
        withTaxs.setSelected(false);

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

    @FXML
    private void getDocument(ActionEvent event) {
        try {
            if (tab.getSelectionModel().getSelectedIndex() == -1) {
                AlertDialogs.showmessage("اختار من الجدول اولا");
            } else {
                Contracts cv = new Contracts();
                cv.setId(tab.getSelectionModel().getSelectedItem().getId());
                cv.getDocdown();
            }
        } catch (Exception ex) {
            AlertDialogs.showErrors(ex);
        }
    }

}
