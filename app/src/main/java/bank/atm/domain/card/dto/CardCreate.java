package bank.atm.domain.card.dto;

import bank.atm.domain.card.entity.Card;

public class CardCreate {
    private String id;
    private String description;
    private String pinNumber;

    public CardCreate(String id, String description, String pinNumber) {
        this.id = id;
        this.description = description;
        if (!pinNumber.matches("\\d{4}")) {
            throw new IllegalArgumentException("잘못된 PIN 번호입니다.");
        }
        this.pinNumber = pinNumber;
    }

    public Card toCard() {
        return Card.Builder.builder()
                .id(id)
                .description(description)
                .pinNumber(pinNumber)
                .build();
    }
}
