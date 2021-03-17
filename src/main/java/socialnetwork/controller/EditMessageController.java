package socialnetwork.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import socialnetwork.service.MessageService;
import socialnetwork.service.UtilizatorService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EditMessageController {

    @FXML
    TextField textFieldTo;

    @FXML
    TextField textFieldMesaj;

    @FXML
    Button buttonSendMessage;


    private MessageService messageService;
    private UtilizatorService utilizatorService;
    private Long userId;
    private SendMessageType sendMessageType;
    private Long messId;


    public void setService( MessageService messageService, UtilizatorService utilizatorService, Long userId, SendMessageType sendMessageType, Long messId ) {
        this.messageService = messageService;
        this.utilizatorService = utilizatorService;
        this.userId = userId;
        this.sendMessageType = sendMessageType;
        this.messId = messId;

    }


    public void handleClickSendMessage() {
        try {
            switch (this.sendMessageType) {
                case REPLY:
                    Long idTo = this.messageService.findOne(messId).getFrom_id();
                    this.messageService.replyToOne(this.userId, idTo, textFieldMesaj.getText(), LocalDateTime.now(), messId);
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Informare", "Mesajul a fost trimis cu succes!");
                    break;

                case START_CONVERSATION:
                    List<String> usersTo = Arrays.asList(textFieldTo.getText().split(";"));
                    List<Long> usersIds = usersTo.stream()
                            .map(string -> this.utilizatorService.getUserId(Arrays.asList(string.split(" ")).get(0),
                                    Arrays.asList(string.split(" ")).get(1)))
                            .collect(Collectors.toList());
                    this.messageService.startConversation(this.userId, usersIds, textFieldMesaj.getText());
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Informare", "Mesajul a fost trimis cu succes!");
                    break;

                case REPLY_ALL:
                    messageService.replyToAll(this.userId, textFieldMesaj.getText(), LocalDateTime.now(), messId);
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Informare", "Mesajul a fost trimis cu succes!");
                    break;
            }
        } catch (Exception ex) {
            MessageAlert.showErrorMessage(null, ex.getMessage());
        }
    }
}
