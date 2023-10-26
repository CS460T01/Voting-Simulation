import java.io.*;
import java.util.*;

public class Register implements Serializable {
    private Map<String, Voter> registeredVoters = new HashMap<>();
    private int voterIDCounter = 0;

    public Register() {
        loadFromFile();
    }

    public String registerVoter(String name) {
        String voterID = String.format("%05d", voterIDCounter++);
        Voter voter = new Voter(voterID, name);
        registeredVoters.put(voterID, voter);
        saveToFile();
        return voterID;
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

    public void printRegisteredVoters() {
        for (Map.Entry<String, Voter> entry : registeredVoters.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getName());
        }
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
}
