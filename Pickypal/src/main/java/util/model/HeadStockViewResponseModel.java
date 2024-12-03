package util.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.dto.HeadStockViewResponseDto;

import java.time.LocalDateTime;

/**
 * @author Queue-ri
 */

@Data
@NoArgsConstructor
public class HeadStockViewResponseModel {
    /* 본사 재고 조회 TableView에 보여질 column들 */
    private SimpleStringProperty stockId; // UI에 보이지 않지만 내부적으로 사용할 수 있음
    private SimpleStringProperty itemId;
    private SimpleStringProperty itemName;
    private SimpleStringProperty type;
    private SimpleStringProperty tag;
    private SimpleStringProperty supplierName;
    private SimpleIntegerProperty price;
    private SimpleIntegerProperty stock; // 재고 수량
    private SimpleStringProperty incomingTime; // 원본 타입은 LocalDateTime

    public HeadStockViewResponseModel(HeadStockViewResponseDto dto) {
        this.stockId = new SimpleStringProperty(dto.getStockId());
        this.itemId = new SimpleStringProperty(dto.getItemId());
        this.itemName = new SimpleStringProperty(dto.getItemName());
        this.type = new SimpleStringProperty(dto.getType());
        this.tag = new SimpleStringProperty(dto.getTag());
        this.supplierName = new SimpleStringProperty(dto.getSupplierName());
        this.price = new SimpleIntegerProperty(dto.getPrice());
        this.stock = new SimpleIntegerProperty(dto.getStock());
        this.incomingTime = new SimpleStringProperty(dto.getIncomingTime().toString());
    }
}
