public class TestRegistration {
    public static void main(String[] args) {
        Register voterRegistry = new Register();

        String generatedVoterID = voterRegistry.registerVoter("John Doe");
        System.out.println("Generated Voter ID: " + generatedVoterID);
        String generatedVoterID2 = voterRegistry.registerVoter("John Doe");
        System.out.println("Generated Voter ID: " + generatedVoterID2);
    }
}
