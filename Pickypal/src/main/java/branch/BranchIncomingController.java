package branch;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

public class BranchIncomingController {

    @FXML
    private TableView<Product> branchIncomingTable;

    @FXML
    private TableColumn<Product, Boolean> selectColumn;

    @FXML
    private TableColumn<Product, String> productIdColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, String> incomingQuantityColumn;

    @FXML
    private TableColumn<Product, String> incomingDateColumn;

    public ComboBox<String> productCategoryComboBox;

    public Label loginInfoLabel;

    public void initialize() {
        // CheckBox 컬럼 설정
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));

        // Product 카테고리 설정
        ObservableList<String> categories = FXCollections.observableArrayList(
                "신선식품 상품", "차별화 상품", "행사 상품"
        );
        productCategoryComboBox.setItems(categories);
        productCategoryComboBox.getSelectionModel().select(0);

        // 다른 컬럼 데이터 바인딩
        productIdColumn.setCellValueFactory(cellData -> cellData.getValue().productIdProperty());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        incomingQuantityColumn.setCellValueFactory(cellData -> cellData.getValue().incomingQuantityProperty());
        incomingDateColumn.setCellValueFactory(cellData -> cellData.getValue().incomingDateProperty());


        // 데이터 초기화
        ObservableList<Product> products = FXCollections.observableArrayList(
                new Product(false, "PRD001", "김밥", "100", "2024-11-28"),
                new Product(false, "PRD002", "사탕", "150", "2024-11-28"),
                new Product(false, "PRD003", "우유", "200", "2024-11-28")
        );

        branchIncomingTable.setItems(products);
    }

    public void handleLogout(ActionEvent actionEvent) {
    }

    public static class Product {
        @FXML
        private TableView<Product> orderTable;
        private final SimpleBooleanProperty selected;
        private final SimpleStringProperty productId;
        private final SimpleStringProperty productName;
        private final SimpleStringProperty incomingQuantity;
        private final SimpleStringProperty incomingDate;

        public Product(boolean selected, String productId, String productName, String incomingQuantity, String incomingDate) {
            this.selected = new SimpleBooleanProperty(selected);
            this.productId = new SimpleStringProperty(productId);
            this.productName = new SimpleStringProperty(productName);
            this.incomingQuantity = new SimpleStringProperty(incomingQuantity);
            this.incomingDate = new SimpleStringProperty(incomingDate);
        }

        public ObservableValue<Boolean> selectedProperty() {
            return selected;
        }

        public ObservableValue<String> productIdProperty() {
            return productId;
        }

        public ObservableValue<String> productNameProperty() {
            return productName;
        }

        public ObservableValue<String> incomingQuantityProperty() {
            return incomingQuantity;
        }

        public ObservableValue<String> incomingDateProperty() {
            return incomingDate;
        }


    }
}
