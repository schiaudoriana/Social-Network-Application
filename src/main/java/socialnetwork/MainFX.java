package socialnetwork;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import socialnetwork.controller.UserController;
import socialnetwork.domain.*;
import socialnetwork.domain.validators.MessageDTOValidator;
import socialnetwork.domain.validators.PrietenieValidator;
import socialnetwork.domain.validators.RequestValidator;
import socialnetwork.domain.validators.UtilizatorValidator;
import socialnetwork.repository.Repository;
import socialnetwork.repository.file.MessageDTOFile;
import socialnetwork.repository.file.PrietenieFile;
import socialnetwork.repository.file.RequestFile;
import socialnetwork.repository.file.UtilizatorFile;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.RequestService;
import socialnetwork.service.UtilizatorService;

import java.io.IOException;

public class MainFX extends Application {

    UtilizatorService userServ;
    PrietenieService friendServ;
    RequestService requestService;
    MessageService messageService;

    @Override
    public void start( Stage stage ) throws IOException {
        //String fileName= ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.users");
        String fileName = "data/users.csv";
        Repository<Long, Utilizator> userFileRepository = new UtilizatorFile(fileName, new UtilizatorValidator());

        //String fileFriends=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.friends");
        String fileFriends = "data/friends.csv";
        Repository<Tuple<Long, Long>, Prietenie> friendsFileRepository = new PrietenieFile(fileFriends, new PrietenieValidator());

        userServ = new UtilizatorService(userFileRepository, friendsFileRepository);
        friendServ = new PrietenieService(friendsFileRepository, userFileRepository);


        //String msgFile=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.messages");
        String msgFile = "data/messages.csv";
        Repository<Long, MessageDTO> msgsFileRepository = new MessageDTOFile(msgFile, new MessageDTOValidator());

        messageService = new MessageService(userFileRepository, msgsFileRepository);

        //String reqFile=ApplicationContext.getPROPERTIES().getProperty("data.socialnetwork.requests");
        String reqFile = "data/requests.csv";
        Repository<Tuple<Long, Long>, Request> reqFileRepository = new RequestFile(reqFile, new RequestValidator());
        requestService = new RequestService(reqFileRepository, userFileRepository, friendsFileRepository);

        initView(stage);
        stage.setWidth(855);
        stage.show();
    }

    private void initView( Stage primaryStage ) throws IOException {
        FXMLLoader userLoader = new FXMLLoader();
        userLoader.setLocation(Main.class.getResource("/view/usersView.fxml"));
        AnchorPane userLayout = userLoader.load();
        primaryStage.setScene(new Scene(userLayout));
        primaryStage.setTitle("Social network");

        UserController userController = userLoader.getController();
        userController.setUserService(userServ, friendServ, requestService, messageService);
    }

    public static void main( String[] args ) {
        launch();
    }


}
