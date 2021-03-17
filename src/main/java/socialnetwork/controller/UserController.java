package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import socialnetwork.domain.UserFriendDto;
import socialnetwork.domain.Utilizator;
import socialnetwork.repository.RepoException;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.RequestService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.UserChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class UserController implements Observer<UserChangeEvent> {

    RequestService requestService;
    UtilizatorService userService;
    PrietenieService friendService;
    MessageService messageService;

    ObservableList<Utilizator> modelUser = FXCollections.observableArrayList();

    ObservableList<UserFriendDto> modelUserFriends = FXCollections.observableArrayList();

    @FXML
    TableColumn<Utilizator, String> tableColumnFirstName;

    @FXML
    TableColumn<Utilizator, String> tableColumnLastName;

    @FXML
    TableView<Utilizator> tableViewUser;

    @FXML
    TextField textFieldNume;

    @FXML
    TextField textFieldPrenume;

    @FXML
    Button buttonShowFriends;

    @FXML
    TableView<UserFriendDto> tableViewFriends;

    @FXML
    TableColumn<UserFriendDto, String> tableColumnFriendFirstName;
    @FXML
    TableColumn<UserFriendDto, String> tableColumnFriendLastName;
    @FXML
    TableColumn<UserFriendDto, LocalDateTime> tableColumnFriendDate;

    @FXML
    Button buttonDeleteFriend;
    @FXML
    Button buttonAddFriend;

    @FXML
    Button buttonShowRequests;

    @FXML
    Button buttonSentRequests;

    @FXML
    Button buttonMessages;

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableViewUser.setItems(modelUser);

        tableColumnFriendFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnFriendLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnFriendDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableViewFriends.setItems(modelUserFriends);


    }

    private List<Utilizator> getUtilizatoriList() {
        return StreamSupport
                .stream(userService.getAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void setUserService( UtilizatorService userServ, PrietenieService friendServ, RequestService requestService, MessageService messageService ) {
        this.userService = userServ;
        this.friendService = friendServ;
        this.requestService = requestService;
        this.messageService = messageService;

        friendServ.addObserver(this);
        modelUser.setAll(getUtilizatoriList());
    }

    private void findFriends() {
        try {
            String nume = textFieldNume.getText();
            String prenume = textFieldPrenume.getText();
            Long id = userService.getUserId(nume, prenume);
            Utilizator us = userService.findOne(id);
            List<UserFriendDto> lista = friendService.getDtoUser(us);
            modelUserFriends.setAll(lista);

        } catch (Exception ex) {
            MessageAlert.showErrorMessage(null, "Nu exista user cu numele dat!");
        }

    }

    public void handleClickButtonShowFriends() {
        findFriends();
    }

    public void handleClickDeleteButton() {
        UserFriendDto dto = tableViewFriends.getSelectionModel().getSelectedItem();
        if (dto != null) {
            String nume = textFieldNume.getText();
            String prenume = textFieldPrenume.getText();
            Long id1 = userService.getUserId(nume, prenume);
            Long id2 = userService.getUserId(dto.getFirstName(), dto.getLastName());
            try {
                this.friendService.removePrietenie(id1, id2);

            } catch (RepoException e) {
                this.friendService.removePrietenie(id2, id1);
            }
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun user");
        }
    }

    @Override
    public void update( UserChangeEvent userChangeEvent ) {
        findFriends();
    }

    public void handleClickAddButton() {
        Long id = userService.getUserId(textFieldNume.getText(), textFieldPrenume.getText());
        if (id != null) {
            showEditUserDialog(id);
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator");
        }
    }

    public void showEditUserDialog( Long id ) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/editUserView.fxml"));
            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Adauga prieten");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditUserController editUserController = loader.getController();
            editUserController.setService(requestService, userService, dialogStage, id);

            dialogStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleClickShowRequests() {
        Long id = userService.getUserId(textFieldNume.getText(), textFieldPrenume.getText());
        if (id != null) {
            showRequests(id);
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator");
        }
    }

    public void showRequests( Long id ) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/requestsView.fxml"));
            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cereri de prietenie");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            RequestsController requestsController = loader.getController();
            requestsController.setService(requestService, userService, id);

            dialogStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleSelectUser() {
        Utilizator us = tableViewUser.getSelectionModel().getSelectedItem();
        if (us != null) {
            textFieldNume.setText(us.getFirstName());
            textFieldPrenume.setText(us.getLastName());
        }
    }


    public void handleClickSentRequests() {
        Long id = userService.getUserId(textFieldNume.getText(), textFieldPrenume.getText());
        if (id != null) {
            showSentRequests(id);
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator");
        }
    }

    private void showSentRequests( Long id ) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/sentRequestsView.fxml"));
            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cereri trimise");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            SentRequestsController sentRequestsController = loader.getController();
            sentRequestsController.setService(requestService, userService, id);

            dialogStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void handleClickMessages() {
        Long id = userService.getUserId(textFieldNume.getText(), textFieldPrenume.getText());
        if (id != null) {
            showMessages(id);
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat niciun utilizator");
        }
    }

    private void showMessages( Long id ) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/messagesView.fxml"));
            AnchorPane root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Mesaje primite");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            MessagesController messagesController = loader.getController();
            messagesController.setService(this.messageService, this.userService, id);

            dialogStage.show();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
