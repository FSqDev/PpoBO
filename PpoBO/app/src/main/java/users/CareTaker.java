package users;

import java.util.ArrayList;

public class CareTaker extends User {
    // === Instance Variables ===
    private ArrayList<users.CareReceiver> careReceivers;

    // === Methods ===
    public void addCareReceiver(CareReceiver careReceiver){
        careReceivers.add(careReceiver);
    }

    public void removeCareReceiver(CareReceiver careReceiver){
        careReceivers.remove(careReceiver);
    }
}
