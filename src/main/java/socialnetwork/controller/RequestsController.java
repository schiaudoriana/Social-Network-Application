package socialnetwork.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import socialnetwork.domain.RequestUserDto;
import socialnetwork.service.RequestService;
import socialnetwork.service.UtilizatorService;
import socialnetwork.utils.events.RequestChangeEvent;
import socialnetwork.utils.observer.Observer;

import java.time.LocalDateTime;

public class RequestsController implements Observer<RequestChangeEvent> {

    @FXML
    Button buttonRejectRequest;
    @FXML
    Button buttonAcceptRequest;
    @FXML
    TableView<RequestUserDto> tableViewRequests;
    @FXML
    Label labelUser;
    @FXML
    TableColumn<RequestUserDto, String> tableColumnFirstName;
    @FXML
    TableColumn<RequestUserDto, String> tableColumnLastName;
    @FXML
    TableColumn<RequestUserDto, String> tableColumnStatus;
    @FXML
    TableColumn<RequestUserDto, LocalDateTime> tableColumnDate;


    private UtilizatorService utilizatorService;
    private RequestService requestService;
    private Long userId;

    ObservableList<RequestUserDto> modelRequests = FXCollections.observableArrayList();


    @FXML
    private void initialize() {

        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableViewRequests.setItems(modelRequests);

    }

    public void setService( RequestService requestService, UtilizatorService utilizatorService, Long id ) {
        this.requestService = requestService;
        this.utilizatorService = utilizatorService;
        this.userId = id;
        requestService.addObserver(this);
        modelRequests.setAll(this.requestService.getUserRequests(this.userId));

        labelUser.setText("Cereri de prietenie pentru " +
                utilizatorService.findOne(userId).getFirstName() + " " +
                utilizatorService.findOne(userId).getLastName());
    }


    public void handleClickAcceptButton() {
        RequestUserDto dto = tableViewRequests.getSelectionModel().getSelectedItem();
        if (dto != null) {
            Long id2 = utilizatorService.getUserId(dto.getFirstName(), dto.getLastName());
            this.requestService.respondRequest(id2, userId, "approved");
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nicio cerere!");
        }
    }

    public void handleClickRejectButton() {
        RequestUserDto dto = tableViewRequests.getSelectionModel().getSelectedItem();
        if (dto != null) {
            Long id2 = utilizatorService.getUserId(dto.getFirstName(), dto.getLastName());
            this.requestService.respondRequest(id2, userId, "rejected");
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nicio cerere!");
        }
    }

    public void initModel() {
        modelRequests.setAll(this.requestService.getUserRequests(this.userId));
    }

    @Override
    public void update( RequestChangeEvent requestChangeEvent ) {
        initModel();
    }
}
