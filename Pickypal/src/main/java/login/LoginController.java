package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    private double x = 0,y = 0;

    @FXML
    private AnchorPane sideBar;

    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sideBar.setOnMousePressed(mouseEvent -> {
            x = mouseEvent.getSceneX();
            y = mouseEvent.getSceneY();
        });

        sideBar.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - x);
            stage.setY(mouseEvent.getScreenY() - y);
        });
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    void closeProgram(ActionEvent event) {
        stage.close();
    }
}
