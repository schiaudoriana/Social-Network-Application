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

public class SentRequestsController implements Observer<RequestChangeEvent> {

    public TableView<RequestUserDto> tableViewSentRequests;
    public TableColumn<RequestUserDto, String> tableColumnFirstName;
    public TableColumn<RequestUserDto, String> tableColumnLastName;
    public TableColumn<RequestUserDto, String> tableColumnStatus;
    public TableColumn<RequestUserDto, LocalDateTime> tableColumnDate;
    public Label labelRequests;
    @FXML
    Button buttonCancelRequest;

    private RequestService requestService;
    private UtilizatorService utilizatorService;
    private Long userId;

    ObservableList<RequestUserDto> modelSentRequests = FXCollections.observableArrayList();

    public void setService( RequestService requestService, UtilizatorService utilizatorService, Long id ) {
        this.requestService = requestService;
        this.utilizatorService = utilizatorService;
        this.userId = id;
        requestService.addObserver(this);
        modelSentRequests.setAll(this.requestService.getUserSentRequests(this.userId));

        labelRequests.setText("Cereri trimise de " + this.utilizatorService.findOne(this.userId).getFirstName() + " " +
                this.utilizatorService.findOne(this.userId).getLastName());
    }

    @FXML
    public void initialize() {
        tableColumnFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        tableColumnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableColumnDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tableViewSentRequests.setItems(modelSentRequests);
    }

    @Override
    public void update( RequestChangeEvent requestChangeEvent ) {
        initModel();
    }

    private void initModel() {
        modelSentRequests.setAll(this.requestService.getUserSentRequests(this.userId));
    }

    public void handleClickCancelRequest() {
        RequestUserDto dto = tableViewSentRequests.getSelectionModel().getSelectedItem();
        if (dto != null) {
            if (dto.getStatus().equals("pending")) {
                Long id = utilizatorService.getUserId(dto.getFirstName(), dto.getLastName());
                this.requestService.cancelRequest(userId, id);
            } else MessageAlert.showErrorMessage(null, "Nu se poate sterge cererea!");
        } else {
            MessageAlert.showErrorMessage(null, "Nu ati selectat nicio cerere!");
        }
    }
}
