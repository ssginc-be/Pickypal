package stock;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.VBox;

public class StockReadController {
    @FXML
    private TableView<Stock> stockTable;

    @FXML
    private TableColumn<Stock, Boolean> selectColumn;

    @FXML
    private TableColumn<Stock, String> stockIdColumn;

    @FXML
    private TableColumn<Stock, String> itemIdColumn;

    @FXML
    private TableColumn<Stock, String> categoryColumn;

    @FXML
    private TableColumn<Stock, String> supplierColumn;

    @FXML
    private TableColumn<Stock, String> nameColumn;

    @FXML
    private TableColumn<Stock, String> priceColumn;

    @FXML
    private TableColumn<Stock, String> tagColumn;

    @FXML
    private TableColumn<Stock, String> stockColumn;

    @FXML
    private TableColumn<Stock, String> createAtColumn;

    @FXML
    private TableColumn<Stock, String> updatedAtColumn;

    private ObservableList<Stock> stocks = FXCollections.observableArrayList();

    public void initialize() {
        // CheckBox 컬럼 설정
        selectColumn.setCellValueFactory(cellData -> cellData.getValue().selectedProperty());
        selectColumn.setCellFactory(CheckBoxTableCell.forTableColumn(selectColumn));
        selectColumn.setEditable(true);

        // 테이블 자체를 수정 가능하도록 설정
        stockTable.setEditable(true);

        // 다른 컬럼 데이터 바인딩
        stockIdColumn.setCellValueFactory(cellData -> cellData.getValue().stockIdProperty());
        itemIdColumn.setCellValueFactory(cellData -> cellData.getValue().itemIdProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        supplierColumn.setCellValueFactory(cellData -> cellData.getValue().supplierProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
        tagColumn.setCellValueFactory(cellData -> cellData.getValue().tagProperty());
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty());
        createAtColumn.setCellValueFactory(cellData -> cellData.getValue().createdAtProperty());
        updatedAtColumn.setCellValueFactory(cellData -> cellData.getValue().updatedAtProperty());

        // 데이터 초기화
        stocks = FXCollections.observableArrayList(
                new Stock(false, "stockId", "itemId", "category", "supplier", "name", "price", "tag", "stock", "createdAt", "updatedAt"),
                new Stock(false, "stockId2", "itemId2", "category2", "supplier2", "2024-10-15", "12", "13", "12", "12", "14")
        );

        stockTable.setItems(stocks);
    }

    // 다른 클래스에서 호출 가능한 데이터 추가 메서드
    public void addStock(String stockId, String itemId, String category, String supplier, String name, String price, String tag, String stock, String createdAt, String updatedAt) {
        // 새 Order 객체를 리스트에 추가
        stocks.add(new Stock(false, stockId, itemId, category, supplier, name, price, tag, stock, createdAt, updatedAt));
    }

    public static class Stock {
        @FXML
        private final SimpleBooleanProperty selected;
        private final SimpleStringProperty stockId;
        private final SimpleStringProperty itemId;
        private final SimpleStringProperty category;
        private final SimpleStringProperty supplier;
        private final SimpleStringProperty name;
        private final SimpleStringProperty price;
        private final SimpleStringProperty tag;
        private final SimpleStringProperty stock;
        private final SimpleStringProperty created_at;
        private final SimpleStringProperty updated_at;

        public Stock(boolean selected, String stockId, String itemId, String category, String supplier, String name, String price, String tag, String stock, String created_at, String updated_at) {
            this.selected = new SimpleBooleanProperty(selected);
            this.stockId = new SimpleStringProperty(stockId);
            this.itemId = new SimpleStringProperty(itemId);
            this.category = new SimpleStringProperty(category);
            this.supplier = new SimpleStringProperty(supplier);
            this.name = new SimpleStringProperty(name);
            this.price = new SimpleStringProperty(price);
            this.tag = new SimpleStringProperty(tag);
            this.stock = new SimpleStringProperty(stock);
            this.created_at = new SimpleStringProperty(created_at);
            this.updated_at = new SimpleStringProperty(updated_at);
        }

        public ObservableValue<Boolean> selectedProperty() {
            return selected;
        }

        public ObservableValue<String> stockIdProperty() {
            return stockId;
        }

        public ObservableValue<String> itemIdProperty() {
            return itemId;
        }

        public ObservableValue<String> categoryProperty() {
            return category;
        }

        public ObservableValue<String> supplierProperty() {
            return supplier;
        }

        public ObservableValue<String> nameProperty() {
            return name;
        }

        public ObservableValue<String> priceProperty() {
            return price;
        }

        public ObservableValue<String> tagProperty() {
            return tag;
        }

        public ObservableValue<String> stockProperty() {
            return stock;
        }

        public ObservableValue<String> createdAtProperty() {
            return created_at;
        }

        public ObservableValue<String> updatedAtProperty() {
            return updated_at;
        }
    }

    @FXML
    private VBox changePane;

    public void readStockButtonClick() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/readStock.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void incomingRegistButtonClick() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/incomingRegist.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void outgoingRegistButtonClick() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/outgoingRegist.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void branchManageButtonClick() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/branchManage.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void supplierManageButtonClick() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/supplierManage.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}