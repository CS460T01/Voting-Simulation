public class TestRegistration {
    public static void main(String[] args) {
        Register voterRegistry = new Register();

        voterRegistry.registerVoter("John Doe", "123-45-6781");
        voterRegistry.registerVoter("Jane Doe", "987-65-4322");
        voterRegistry.registerVoter("John Smith", "123-45-6783");
        voterRegistry.registerVoter("Jane Smith", "987-65-4324");
        voterRegistry.registerVoter("John Doe", "123-45-6785");
        voterRegistry.printRegisteredVoters();

    }
}
