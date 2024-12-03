package head;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.extern.slf4j.Slf4j;
import util.ApiKit;
import util.ApiResponse;
import util.dto.HeadStockViewResponseDto;
import util.model.HeadStockViewResponseModel;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HeadStockController {
    private ApiKit apiKit = new ApiKit();
    private final String SERVER = "http://localhost:8080";

    @FXML
    private TableView<HeadStockViewResponseModel> headStockTable;

    private ObservableList<HeadStockViewResponseModel> tableRow;

    @FXML
    public void setTableContent() {
        tableRow = FXCollections.observableArrayList();
        headStockTable.setEditable(true);
        
        headStockTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        headStockTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("itemName"));
        headStockTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("type"));
        headStockTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("tag"));
        headStockTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        headStockTable.getColumns().get(5).setCellValueFactory(new PropertyValueFactory<>("price"));
        headStockTable.getColumns().get(6).setCellValueFactory(new PropertyValueFactory<>("stock"));
        headStockTable.getColumns().get(7).setCellValueFactory(new PropertyValueFactory<>("incomingTime"));

        // dto --> api 호출해서 받아온 원본 타입의 데이터
        // model --> JavaFX에 사용 가능하도록 타입 변환된 데이터
        List<HeadStockViewResponseDto> dtoList = null;
        try {
            // api 호출해서 dto list로 받아옴
            dtoList = fetchHeadStock(0);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // 테이블 row 데이터 생성
        for (HeadStockViewResponseDto dto : dtoList) {
            tableRow.add(new HeadStockViewResponseModel(dto));
        }
        // 테이블에 row 값 세팅
        headStockTable.setItems(tableRow);
    }


    public List<HeadStockViewResponseDto> fetchHeadStock(int pageIdx) throws JsonProcessingException {
        ApiResponse res = apiKit.getRequestWithAuth(SERVER + "/head/stock/" + pageIdx);
        int statusCode = res.getStatusCode();
        String jsonStr = res.getJsonStr();

        List<HeadStockViewResponseDto> dtoList = new ArrayList<>();
        if (statusCode == 200) { // 정상 응답
            // json string을 dto 객체로 파싱
            ObjectMapper mapper = new ObjectMapper();
            dtoList = mapper.readValue(jsonStr, TypeFactory.defaultInstance().constructCollectionType(List.class, HeadStockViewResponseDto.class));
        }
        else { // api error
            log.error("* * * HeadStockController: server returned " + statusCode);
        }

        return dtoList;
    }

}
