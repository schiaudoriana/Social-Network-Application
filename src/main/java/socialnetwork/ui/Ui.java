package socialnetwork.ui;


import socialnetwork.domain.*;
import socialnetwork.domain.validators.ValidationException;
import socialnetwork.repository.RepoException;
import socialnetwork.service.MessageService;
import socialnetwork.service.PrietenieService;
import socialnetwork.service.RequestService;
import socialnetwork.service.UtilizatorService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Ui {
    private final UtilizatorService userService;
    private final PrietenieService friendService;
    private final MessageService messageService;
    private final RequestService requestService;

    public Ui( UtilizatorService userService, PrietenieService friendService, MessageService messageService, RequestService requestService ) {
        this.userService = userService;
        this.friendService = friendService;
        this.messageService = messageService;
        this.requestService = requestService;
    }

    public void run() {
        label:
        while (true) {
            System.out.println("-MENIU-");
            System.out.println("1.Adauga utilizator");
            System.out.println("2.Afiseaza utilizatori");
            System.out.println("3.Sterge utilizator");
            System.out.println("4.Modifica utilizator");
            System.out.println("5.Adauga prietenie");
            System.out.println("6.Afiseaza prietenii");
            System.out.println("7.Sterge prietenie");
            System.out.println("8.Afiseaza nr de comunitati");
            System.out.println("9.Afiseaza prietenii unui utilizator");
            System.out.println("10.Afiseaza prietenii unui utilizator dupa luna");
            System.out.println("11.Incepe conversatie noua");
            System.out.println("12.Raspunde unui singur utilizator");
            System.out.println("13.Raspunde tuturor");
            System.out.println("14.Afiseaza conversatie 2 utilizatori");
            System.out.println("15.Trimite cerere de prietenie");
            System.out.println("16.Raspunde cerere de prietenie");
            System.out.println("0.Exit");
            System.out.print("Command=");
            Scanner in = new Scanner(System.in);
            String cmd = in.nextLine();
            switch (cmd) {
                case "0":
                    break label;
                case "1":
                    addUtilizator();
                    break;
                case "2":
                    userService.getAll().forEach(System.out::println);
                    break;
                case "3":
                    removeUtilizator();
                    break;
                case "4":
                    updateUtilizator();
                    break;
                case "5":
                    addPrietenie();
                    break;
                case "6":
                    friendService.getAll().forEach(System.out::println);
                    break;
                case "7":
                    removePrietenie();
                    break;
                case "8":
                    printComunitati();
                    break;
                case "9":
                    printPrieteni();
                    break;
                case "10":
                    printMonth();
                    break;
                case "11":
                    startConversation();
                    break;
                case "12":
                    replyMessage();
                    break;
                case "13":
                    replyToAll();
                    break;
                case "14":
                    showConversation();
                    break;
                case "15":
                    sendRequest();
                    break;
                case "16":
                    respondRequest();
                    break;
                default:
                    System.out.println("Comanda invalida!");
                    System.out.println();
                    break;
            }

        }
    }


    private void addUtilizator() {
        try {

            Scanner in = new Scanner(System.in);
            System.out.print("Id=");
            Long id = Long.parseLong(in.nextLine());
            System.out.print("Nume=");
            String nume = in.nextLine();
            System.out.print("Prenume=");
            String prenume = in.nextLine();
            userService.addUtilizator(id, nume, prenume);

            System.out.println("Utilizator adaugat...");
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
            System.out.println();
        } catch (NumberFormatException ex) {
            System.out.println("Id ul trebuie sa fie numar!");
        } catch (RepoException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void removeUtilizator() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("Id=");
            Long id = Long.parseLong(in.nextLine());
            userService.removeUtilizator(id);
            System.out.println("Utilizatorul a fost sters...");

        } catch (NumberFormatException ex) {
            System.out.println("Id-ul trebuie sa fie numar!");
        } catch (RepoException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void updateUtilizator() {
        try {

            Scanner in = new Scanner(System.in);
            System.out.print("Id=");
            Long id = Long.parseLong(in.nextLine());
            System.out.print("Nume nou=");
            String nume = in.nextLine();
            System.out.print("Prenume nou=");
            String prenume = in.nextLine();
            userService.updateUtilizator(id, nume, prenume);
            System.out.println("Utilizator modificat...");
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
            System.out.println();
        } catch (NumberFormatException ex) {
            System.out.println("Id ul trebuie sa fie numar!");
        } catch (RepoException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void addPrietenie() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("Id 1=");
            String id1 = in.nextLine();
            System.out.print("Id 2=");
            String id2 = in.nextLine();
            friendService.addPrietenie(Long.parseLong(id1), Long.parseLong(id2));
            System.out.println("Prietenie adaugata...");

        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
            System.out.println();
        } catch (NumberFormatException ex1) {
            System.out.println("Id urile trebuie sa fie numere");
        } catch (RepoException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void removePrietenie() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("Id 1=");
            String id1 = in.nextLine();
            System.out.print("Id 2=");
            String id2 = in.nextLine();
            friendService.removePrietenie(Long.parseLong(id1), Long.parseLong(id2));
            System.out.println("Prietenia a fost stearsa");

        } catch (NumberFormatException ex) {
            System.out.println("Id urile trebuie sa fie numere");
        } catch (RepoException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void printComunitati() {
        Retea retea = new Retea(friendService.getAll());
        System.out.println("Numarul de comunitati:" + retea.ConnectedComponents());
    }

    private void printPrieteni() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("Id=");
            String id = in.nextLine();
            Utilizator u = userService.findOne(Long.parseLong(id));
            System.out.println("Prietenii utilizatorului '" + u.getFirstName() + " " + u.getLastName() + "' sunt:");
            List<String> l = friendService.getFriends(u);
            l.forEach(System.out::println);
        } catch (NumberFormatException ex) {
            System.out.println("Id-ul trebuie sa fie numar");
        } catch (RepoException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void printMonth() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("Id=");
            String id = in.nextLine();
            System.out.print("Luna=");
            String luna = in.nextLine();
            Utilizator u = userService.findOne(Long.parseLong(id));
            System.out.println("Prietenii utilizatorului '" + u.getFirstName() + " " + u.getLastName() + "' din luna " + luna + " sunt:");
            List<String> l = friendService.getFriendsMonth(u, luna);
            l.forEach(System.out::println);
        } catch (NumberFormatException ex) {
            System.out.println("ID-ul trebuie sa fie numar");
        } catch (RepoException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void replyMessage() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("Id mesaj=");
            String idMsg = in.nextLine();
            System.out.print("Id from=");
            String idFrom = in.nextLine();
            System.out.print("Id to=");
            String idTo = in.nextLine();
            System.out.print("Mesaj=");
            String mesaj = in.nextLine();
            MessageDTO m = messageService.replyToOne(Long.parseLong(idFrom), Long.parseLong(idTo), mesaj, LocalDateTime.now(), Long.parseLong(idMsg));
            if (m != null)
                System.out.println("Raspunsul a fost trimis...");
            else
                System.out.println("Nu se poate trimite raspuns mesajului!");

        } catch (NumberFormatException ex) {
            System.out.println("Id-urile trebuie sa fie numere");
        } catch (RepoException | ValidationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void replyToAll() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("Id mesaj=");
            Long idMsg = Long.parseLong(in.nextLine());
            System.out.print("Id from=");
            Long idFrom = Long.parseLong(in.nextLine());
            System.out.print("Mesaj=");
            String mesaj = in.nextLine();
            messageService.replyToAll(idFrom, mesaj, LocalDateTime.now(), idMsg);
            System.out.println("Raspunsul a fost trimis tuturor");
        } catch (NumberFormatException ex) {
            System.out.println("Id-urile trebuie sa fie numere");
        } catch (RepoException | ValidationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void showConversation() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("Id1=");
            Long id1 = Long.parseLong(in.nextLine());
            System.out.print("Id2=");
            Long id2 = Long.parseLong(in.nextLine());
            List<Message> msgs = messageService.showConversation(id1, id2);
            msgs.forEach(x -> System.out.println("FROM:" + x.getFrom().getFirstName() + " " + x.getFrom().getLastName() + " MESSAGE:" + x.getMessage() + " DATE:" + x.getData()));

        } catch (NumberFormatException ex) {
            System.out.println("Id-urile trebuie sa fie numere");
        } catch (RepoException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void startConversation() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("Id from=");
            Long id1 = Long.parseLong(in.nextLine());
            System.out.print("Id to sau stop pentru oprire=");
            String id2 = in.nextLine();
            List<Long> idTo = new ArrayList<>();
            while (!id2.equals("stop")) {
                idTo.add(Long.parseLong(id2));
                System.out.print("Id to sau stop pentru oprire=");
                id2 = in.nextLine();
            }
            System.out.print("Mesaj=");
            String mesaj = in.nextLine();
            messageService.startConversation(id1, idTo, mesaj);
            System.out.println("Mesajul a fost adaugat...");

        } catch (NumberFormatException ex) {
            System.out.println("Id-urile trebuie sa fie numere");
        } catch (RepoException | ValidationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void sendRequest() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("From id=");
            Long id1 = Long.parseLong(in.nextLine());
            System.out.print("To id=");
            Long id2 = Long.parseLong(in.nextLine());
            requestService.sendRequest(id1, id2);
            System.out.println("Cerea de prietenie a fost trimisa...");
        } catch (NumberFormatException ex) {
            System.out.println("Id-urile trebuie sa fie numere");
        } catch (RepoException | ValidationException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void respondRequest() {
        try {
            Scanner in = new Scanner(System.in);
            System.out.print("From id=");
            Long id1 = Long.parseLong(in.nextLine());
            System.out.print("To id=");
            Long id2 = Long.parseLong(in.nextLine());
            System.out.print("Response(approved,rejected)=");
            String response = in.next();
            requestService.respondRequest(id1, id2, response);
            if (response.equals("approved"))
                System.out.println("Cerere de prietenie acceptata...");
            else
                System.out.println("Cerere de prietenie respinsa...");
        } catch (NumberFormatException ex) {
            System.out.println("Id-urile trebuie sa fie numere");
        } catch (RepoException | ValidationException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
