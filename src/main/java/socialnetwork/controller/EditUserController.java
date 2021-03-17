package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import socialnetwork.service.RequestService;
import socialnetwork.service.UtilizatorService;

public class EditUserController {
    public Button buttonSendRequest;
    @FXML
    TextField textFieldEditFirstName;
    @FXML
    TextField textFieldEditLastName;

    private RequestService requestService;
    private UtilizatorService utilizatorService;
    private Long userId;
    Stage dialogStage;

    @FXML
    private void initialize() {
    }

    public void setService( RequestService requestService, UtilizatorService utilizatorService, Stage stage, Long id ) {
        this.requestService = requestService;
        this.utilizatorService = utilizatorService;
        this.dialogStage = stage;
        this.userId = id;
    }


    public void handleClickSendRequest() {
        Long user2Id = utilizatorService.getUserId(textFieldEditFirstName.getText(), textFieldEditLastName.getText());
        if (user2Id != null) {
            try {
                this.requestService.sendRequest(userId, user2Id);
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Informare", "Cererea de prietenie a fost trimisa");
            } catch (Exception ex) {
                MessageAlert.showErrorMessage(null, ex.getMessage());
            }
        } else
            MessageAlert.showErrorMessage(null, "Nu exista user cu numele dat!");
    }
}
