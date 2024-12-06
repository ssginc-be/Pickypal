package head;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.stock.HeadStockViewResponseDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.ApiKit;
import util.ApiResponse;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HeadStockController {
    // API utils
    ApiKit apiKit;
    ObjectMapper mapper;

    @FXML private TableView<HeadStockViewResponseDto> tableView;
    @FXML private TableColumn<HeadStockViewResponseDto, String> stockId;
    @FXML private TableColumn<HeadStockViewResponseDto, String> itemId;
    @FXML private TableColumn<HeadStockViewResponseDto, String> itemName;
    @FXML private TableColumn<HeadStockViewResponseDto, String> type;
    @FXML private TableColumn<HeadStockViewResponseDto, String> tag;
    @FXML private TableColumn<HeadStockViewResponseDto, String> supplierName;
    @FXML private TableColumn<HeadStockViewResponseDto, Integer> price;
    @FXML private TableColumn<HeadStockViewResponseDto, Integer> stock;
    @FXML private TableColumn<HeadStockViewResponseDto, LocalDateTime> incomingTime;

    private ObservableList<HeadStockViewResponseDto> dtoList;

    @FXML
    public void initialize() throws JsonProcessingException {
        // Instantiate API utils
        apiKit = new ApiKit();
        mapper = new ObjectMapper();

        // Create a list of ResponseDto objects
        dtoList = FXCollections.observableArrayList(getData(0));

        // Bind the columns to the ResponseDto properties
        //stockId.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        stockId.setCellValueFactory(new PropertyValueFactory<>("stockId"));
        itemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        tag.setCellValueFactory(new PropertyValueFactory<>("tag"));
        supplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        incomingTime.setCellValueFactory(new PropertyValueFactory<>("incomingTime"));

        // Set the data to the TableView
        tableView.setItems(dtoList);
    }

    public List<HeadStockViewResponseDto> getData(int pageIdx) throws JsonProcessingException {
        final String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyb290Iiwicm9sZSI6IkhFQUQiLCJleHAiOjE3MzYwNTU3MzF9.lCD6TzLa0b8bcQW5cslAfGjIcCGUA7mPMzBDpWMzVJ4";
        String endpoint = "http://localhost:8080/head/stock/" + pageIdx;
        System.out.println("* * * HeadStockController: GET " + endpoint);
        ApiResponse response = apiKit.getRequestWithAuth(endpoint, accessToken);

        if (response.getStatusCode() == 200) {
            // json string을 dto 객체의 List로 파싱
            mapper.registerModule(new JavaTimeModule()); // LocalDateTime 파싱을 위해 필요
            String jsonStr = response.getJsonStr();
            List<HeadStockViewResponseDto> dtoList = mapper.readValue(jsonStr, TypeFactory.defaultInstance().constructCollectionType(List.class, HeadStockViewResponseDto.class));
//            List<HeadStockViewModel> modelList = new ArrayList<>();
//            for (HeadStockViewResponseDto dto : dtoList) {
//                modelList.add(HeadStockViewModel.of(dto)); // TableView에 들어가도록 dto --> model 변환
//            }
//            return modelList;
            return dtoList;
        }
        else {
            System.out.println("* * * HeadStockController: API request failed.");
        }

        return new ArrayList<>();
    }
}
