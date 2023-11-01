import java.io.*;
import java.util.*;

public class Register implements Serializable {
    private Map<String, Voter> registeredVoters = new HashMap<>();
    private int voterIDCounter = 0;

    public Register() {
        loadFromFile();
    }

    public void registerVoter(String name, String ssn) {
        if (isSSNRegistered(ssn)) {
            System.out.println("[" + ssn + " is already registered]");
            return;
        }

        String voterID = String.format("%05d", voterIDCounter++);
        Voter voter = new Voter(voterID, name, ssn);
        registeredVoters.put(voterID, voter);
        saveToFile();
    }

    public boolean removeVoter(String voterID) {
        if (registeredVoters.containsKey(voterID)) {
            registeredVoters.remove(voterID);
            saveToFile();
            return true;
        } else {
            return false;
        }
    }

    public String getVoterIDByNameAndSSN(String name, String ssn) {
        for (Voter voter : registeredVoters.values()) {
            if (voter.getName().equalsIgnoreCase(name) && voter.getSSN().equals(ssn)) {
                return voter.getVoterID();
            }
        }
        return null;
    }

    public void printRegisteredVoters() {
        for (Map.Entry<String, Voter> entry : registeredVoters.entrySet()) {
            System.out.println(entry.getKey() + ": NAME: " + entry.getValue().getName() + ", SSN: " + entry.getValue().getSSN());
        }
    }

    private boolean isSSNRegistered(String ssn) {
        for (Voter voter : registeredVoters.values()) {
            if (voter.getSSN().equals(ssn)) {
                return true;
            }
        }
        return false;
    }

    public Voter getVoter(String voterID) {
        return registeredVoters.get(voterID);
    }

    private void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data/registered_voters.dat"))) {
            oos.writeObject(registeredVoters);
            oos.writeInt(voterIDCounter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile() {
        File file = new File("data/registered_voters.dat");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                registeredVoters = (Map<String, Voter>) ois.readObject();
                voterIDCounter = ois.readInt();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    // Register.java
    // Register.java
    public boolean hasVoterVoted(String voterID) {
        Voter voter = registeredVoters.get(voterID);
        return voter != null && voter.getHasVoted();
    }

    public void markVoterAsVoted(String voterID) {
        Voter voter = registeredVoters.get(voterID);
        if (voter != null) {
            voter.markAsVoted();
            saveToFile();
        }
    }

}
