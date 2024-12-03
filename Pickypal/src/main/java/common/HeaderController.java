package common;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HeaderController {

    @FXML
    private Label titleLabel; // FXML에서 지정한 ID와 연결됩니다.

    /**
     * 제목 텍스트를 변경하는 메서드
     * @param title 변경할 제목 텍스트
     */
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}

