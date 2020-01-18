package users;

import java.util.ArrayList;

public class CareReceiver extends User {
    // === Instance variables ===
    private ArrayList<CareTaker> careTakers;

    // === Methods ===
    public void addCaretaker(CareTaker caretaker){
        careTakers.add(caretaker);
    }

    public void removeCareTaker(CareTaker careTaker){
        careTakers.remove(careTaker);
    }
}
