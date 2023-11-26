package Registration;

import java.time.LocalDate;

public class TestRegistration {

    public static void main(String[] args) {
        Register register = new Register();

        Voter voter1 = new Voter("John Doe", "123 Main St", LocalDate.of(1980, 6, 15), "6789");
        Voter voter2 = new Voter("Jane Smith", "456 Elm St", LocalDate.of(1992, 8, 23), "4321");
        Voter voter3 = new Voter("Jim Brown", "789 Oak St", LocalDate.of(1975, 3, 5), "1011");
        Voter voter4 = new Voter("A B", "1", LocalDate.of(2001, 1, 1), "0000");
        Voter voter5 = new Voter("A A", "1", LocalDate.of(2001, 1, 1), "0000");

        System.out.println("Registering voters...");
        register.registerVoter(voter1);
        register.registerVoter(voter2);
        register.registerVoter(voter3);
        register.registerVoter(voter4);
        register.registerVoter(voter5);
    }
}
