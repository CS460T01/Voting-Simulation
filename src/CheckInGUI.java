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
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    @Override
    public void start(Stage primaryStage) {
        register = new Register();

        primaryStage.setTitle("Voter Check-In");
        primaryStage.setResizable(false);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        grid.setStyle("-fx-background-color: white; -fx-font-size: 14;");

        Text header = new Text("Voter Check-In");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        header.setFill(Color.DARKBLUE);
        GridPane.setColumnSpan(header, 2);
        GridPane.setHalignment(header, HPos.CENTER);
        grid.add(header, 0, 0);

        Label firstNameLabel = new Label("First Name:");
        Label lastNameLabel = new Label("Last Name:");
        Label addressLabel = new Label("Address:");
        Label ssnLabel = new Label("SSN (Last 4 digits):");
        Label dobLabel = new Label("Date of Birth (MM/DD/YYYY):");


        final Text actiontarget = new Text();
        GridPane.setColumnSpan(actiontarget, 2);
        GridPane.setHalignment(actiontarget, HPos.CENTER);
        actiontarget.setFill(Color.FIREBRICK);

        TextField firstNameTextField = new TextField();
        TextField lastNameTextField = new TextField();
        TextField addressTextField = new TextField();
        TextField ssnTextField = new TextField();
        TextField dobTextField = new TextField();
        dobTextField.setPromptText("MM/DD/YYYY");

        Button btnCheckIn = new Button("Check In");
        btnCheckIn.setStyle("-fx-font-weight: bold; -fx-background-color: #09a30f; -fx-text-fill: white;");
        GridPane.setHalignment(btnCheckIn, HPos.CENTER);
        GridPane.setColumnSpan(btnCheckIn, 2);

        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameTextField, 1, 1);
        grid.add(lastNameLabel, 0, 2);
        grid.add(lastNameTextField, 1, 2);
        grid.add(addressLabel, 0, 3);
        grid.add(addressTextField, 1, 3);
        grid.add(ssnLabel, 0, 4);
        grid.add(ssnTextField, 1, 4);
        grid.add(dobLabel, 0, 5);
        grid.add(dobTextField, 1, 5);
        grid.add(btnCheckIn, 0, 6);
        grid.add(actiontarget, 0, 7);

        btnCheckIn.setOnAction(e -> {
            actiontarget.setFill(Color.FIREBRICK);
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            String address = addressTextField.getText();
            String ssnLast4 = ssnTextField.getText();
            String dobString = dobTextField.getText();
            LocalDate dob = null;
            try {
                dob = LocalDate.parse(dobString, formatter);
            } catch (DateTimeParseException dtpe) {
                actiontarget.setText("Invalid date of birth. Please enter a valid date in the format MM/DD/YYYY.");
            }
            if (dob != null) {
                if (register.checkInVoter(firstName, lastName, ssnLast4, address, dob)) {
                    actiontarget.setFill(Color.GREEN);
                    actiontarget.setText("Check-in successful!");
                } else {
                    if (!register.isRegistered(firstName + " " + lastName + dob + ssnLast4))
                        actiontarget.setText("Check-in failed: Voter not found.");
                    else {
                        actiontarget.setText("Check-in failed: Voter has already voted.");
                    }
                }
            }
        });

        Scene scene = new Scene(grid, 500, 475);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
