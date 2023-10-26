public class TestRegistration {
    public static void main(String[] args) {
        Register voterRegistry = new Register();

        voterRegistry.registerVoter("John Doe");
        voterRegistry.registerVoter("Jane Doe");
        voterRegistry.registerVoter("John Smith");
        voterRegistry.registerVoter("Jane Smith");
        voterRegistry.registerVoter("John Doe");
        voterRegistry.printRegisteredVoters();

        boolean success = voterRegistry.removeVoter("00002");
        if (success) {
            System.out.println("Voter removed successfully.");
        } else {
            System.out.println("No voter found with the given voterID.");
        }

        voterRegistry.printRegisteredVoters();
    }
}
