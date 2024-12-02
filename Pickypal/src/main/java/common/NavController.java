package common;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

public class NavController {

    @FXML
    private VBox changePane;  // 화면을 교체할 VBox

    // 본사 재고 조회 화면 
    public void moveToHeadStock() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/head/HeadStock.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 본사 입고 관리 화면
    public void moveToIncomingManage() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/head/HeadIncomingManage.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 본사 출고 관리 화면
    public void moveToOutgoingManage() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/head/HeadOutgoingManage.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //본사 지점 정보 관리 화면
    public void moveToBranchManage() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/head/BranchManage.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //본사 납품업체 정보 관리 화면
    public void moveToSupplierManage() {
        try {
            // 다른 FXML 파일 로드
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/head/SupplierManage.fxml"));
            VBox newContent = loader.load();

            // 기존 VBox 내용을 새로운 화면으로 교체
            changePane.getChildren().clear();
            changePane.getChildren().add(newContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //end
}

