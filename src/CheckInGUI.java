import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CheckInGUI extends Application {

    private Register register;
    private TextField firstNameTextField;
    private TextField lastNameTextField;
    private TextField addressTextField;
    private TextField ssnTextField;
    private TextField dobTextField;
    private Text actionTarget;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/DD/YYYY");

    @Override
    public void start(Stage primaryStage) {
        register = new Register();
        primaryStage.setTitle("Voter Check-In");
        primaryStage.setResizable(false);

        GridPane grid = createGridPane();
        addUIControls(grid);

        Button btnCheckIn = createCheckInButton();
        grid.add(btnCheckIn, 0, 6);
        btnCheckIn.setOnAction(e -> handleCheckInAction());

        Scene scene = new Scene(grid, 500, 475);
        primaryStage.setScene(scene);
        primaryStage.show();
        register.printVoters();
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setStyle("-fx-background-color: white; -fx-font-size: 14;");
        return grid;
    }

    private void addUIControls(GridPane grid) {
        Text header = new Text("Voter Check-In");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setFill(Color.DARKBLUE);
        GridPane.setColumnSpan(header, 2);
        GridPane.setHalignment(header, HPos.CENTER);
        grid.add(header, 0, 0);

        Label firstNameLabel = new Label("First Name:");
        grid.add(firstNameLabel, 0, 1);
        firstNameTextField = new TextField();
        grid.add(firstNameTextField, 1, 1);

        Label lastNameLabel = new Label("Last Name:");
        grid.add(lastNameLabel, 0, 2);
        lastNameTextField = new TextField();
        grid.add(lastNameTextField, 1, 2);

        Label addressLabel = new Label("Address:");
        grid.add(addressLabel, 0, 3);
        addressTextField = new TextField();
        grid.add(addressTextField, 1, 3);

        Label ssnLabel = new Label("SSN (Last 4 digits):");
        grid.add(ssnLabel, 0, 4);
        ssnTextField = new TextField();
        grid.add(ssnTextField, 1, 4);

        Label dobLabel = new Label("Date of Birth (MM/DD/YYYY):");
        grid.add(dobLabel, 0, 5);
        dobTextField = new TextField();
        dobTextField.setPromptText("MM/DD/YYYY");
        grid.add(dobTextField, 1, 5);

        actionTarget = new Text();
        GridPane.setColumnSpan(actionTarget, 2);
        GridPane.setHalignment(actionTarget, HPos.CENTER);
        actionTarget.setFill(Color.FIREBRICK);
        grid.add(actionTarget, 0, 7);
    }

    private Button createCheckInButton() {
        Button btnCheckIn = new Button("Check In");
        btnCheckIn.setStyle("-fx-font-weight: bold; -fx-background-color: #09a30f; -fx-text-fill: white;");
        GridPane.setHalignment(btnCheckIn, HPos.CENTER);
        GridPane.setColumnSpan(btnCheckIn, 2);
        return btnCheckIn;
    }

    private void handleCheckInAction() {
        actionTarget.setFill(Color.FIREBRICK);
        String firstName = firstNameTextField.getText().trim();
        String lastName = lastNameTextField.getText().trim();
        String address = addressTextField.getText().trim();
        String ssnLast4 = ssnTextField.getText().trim();
        String dobString = dobTextField.getText().trim();

        if (areFieldsValid(firstName, lastName, address, ssnLast4, dobString)) {
            LocalDate dob = parseDateOfBirth(dobString);
            if (dob != null) {
                processCheckIn(firstName, lastName, address, ssnLast4, dob);
            }
        }
    }

    private boolean areFieldsValid(String firstName, String lastName, String address, String ssnLast4, String dob) {
        if (firstName.isEmpty() || lastName.isEmpty() || address.isEmpty() || ssnLast4.isEmpty() || dob.isEmpty()) {
            actionTarget.setText("Please fill out all fields.");
            return false;
        }
        return true;
    }

    private LocalDate parseDateOfBirth(String dobString) {
        try {
            return LocalDate.parse(dobString, formatter);
        } catch (DateTimeParseException dtpe) {
            actionTarget.setText("Invalid date of birth. Please enter a valid date in the format MM/DD/YYYY.");
            return null;
        }
    }

    private void processCheckIn(String firstName, String lastName, String address, String ssnLast4, LocalDate dob) {
        String voterID = createVoterID(firstName, lastName, ssnLast4, dob);
        if (register.isRegistered(voterID)) {
            if (register.checkInVoter(firstName, lastName, ssnLast4, address, dob)) {
                actionTarget.setFill(Color.GREEN);
                actionTarget.setText("Check-in successful!");
            } else {
                actionTarget.setText("Check-in failed: Voter has already voted.");
            }
        } else {
            actionTarget.setText("Check-in failed: Voter not found.");
        }
    }

    private String createVoterID(String firstName, String lastName, String ssnLast4, LocalDate dob) {
        return firstName + " " + lastName + dob + ssnLast4;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
