import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CheckinGUI extends Application {

    private Register register;

    public static void main(String[] args) {
        launch(args);
    }

    public CheckinGUI() {
        register = new Register();
    }

    @Override
    public void start(Stage primaryStage) {
        register.printRegisteredVoters();
        TextField firstNameField = new TextField();
        TextField lastNameField = new TextField();
        Button checkInButton = new Button("Check In");
        Label messageLabel = new Label();

        firstNameField.setPromptText("First Name");
        lastNameField.setPromptText("Last Name");
        TextField ssnField = new TextField();
        ssnField.setPromptText("Social Security Number");

        checkInButton.setOnAction(event -> {
            String fullName = firstNameField.getText() + " " + lastNameField.getText();
            String ssn = ssnField.getText();
            String voterID = register.getVoterIDByNameAndSSN(fullName, ssn);
            if (voterID != null) {
                messageLabel.setText("Your Voter ID is: " + voterID);
            } else {
                messageLabel.setText("You are not registered. Please register to vote.");
            }
        });

        VBox vbox = new VBox(10, firstNameField, lastNameField, ssnField, checkInButton, messageLabel);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Voter Check-In");
        primaryStage.show();
    }
}
