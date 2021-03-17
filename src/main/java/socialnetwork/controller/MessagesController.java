package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.MessageUserDto;
import socialnetwork.service.MessageService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.MessageChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;


public class MessagesController implements Observer<MessageChangeEvent> {

    public Button buttonStartConversation;
    public Button buttonReplyMessage;
    public Button buttonReplyAll;
    @FXML
    TableView<MessageUserDto> tableViewMessages;

    @FXML
    TableColumn<MessageUserDto, Long> tableColumnId;

    @FXML
    TableColumn<MessageUserDto, String> tableColumnFrom;

    @FXML
    TableColumn<MessageUserDto, String> tableColumnMessage;

    @FXML
    TableColumn<MessageUserDto, LocalDateTime> tableColumnDate;

    @FXML
    TableColumn<MessageUserDto, Long> tableColumnReply;

    @FXML
    Label labelUser;

    private MessageService messageService;
    private UtilizatorService utilizatorService;
    private Long userId;

    ObservableList<MessageUserDto> modelMessages = FXCollections.observableArrayList();

    public void setService( MessageService messageService, UtilizatorService userService, Long id ) {
        this.messageService = messageService;
        this.utilizatorService = userService;
        this.userId = id;

        modelMessages.setAll(this.messageService.showUserMessages(this.userId));

        messageService.addObserver(this);

        labelUser.setText("Inbox: " + utilizatorService.findOne(userId).getFirstName() + " " +
                utilizatorService.findOne(userId).getLastName());

    }

    @FXML
    public void initialize() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnFrom.setCellValueFactory(new PropertyValueFactory<>("from"));
        tableColumnMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableColumnReply.setCellValueFactory(new PropertyValueFactory<>("replyId"));

        tableViewMessages.setItems(modelMessages);

    }


    private void showEditMessageDialog( SendMessageType sendMessageType, Long messId ) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/editMessageView.fxml"));
            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Trimite mesaj");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditMessageController editMessageController = loader.getController();
            editMessageController.setService(messageService, utilizatorService, this.userId, sendMessageType, messId);

            dialogStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleClickStartConversation() {
        showEditMessageDialog(SendMessageType.START_CONVERSATION, null);
    }

    public void handleClickReplyMessage() {
        MessageUserDto dto = tableViewMessages.getSelectionModel().getSelectedItem();
        if (dto != null)
            showEditMessageDialog(SendMessageType.REPLY, dto.getId());
        else
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun mesaj!");
    }

    public void handleClickReplyAll() {
        MessageUserDto dto = tableViewMessages.getSelectionModel().getSelectedItem();
        if (dto != null)
            showEditMessageDialog(SendMessageType.REPLY_ALL, dto.getId());
        else
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun mesaj!");
    }

    private void initModel() {
        modelMessages.setAll(this.messageService.showUserMessages(this.userId));
    }


    @Override
    public void update( MessageChangeEvent messageChangeEvent ) {
        initModel();
    }
}
