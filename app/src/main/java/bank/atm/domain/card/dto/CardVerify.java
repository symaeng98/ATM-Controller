package bank.atm.domain.card.dto;

public class CardVerify {
    private final String id;
    private final String pinNumber;

    public CardVerify(String id, String pinNumber) {
        this.id = id;
        this.pinNumber = pinNumber;
    }

    public String getId() {
        return id;
    }

    public String getPinNumber() {
        return pinNumber;
    }
}
