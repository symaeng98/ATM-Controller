package bank.atm.domain.card.entity;

/**
 * 카드 클래스
 * id와 pinNumber를 포함합니다.
 * id는 사용자 지정 값입니다.
 * pinNumber는 숫자로만 이루어져 있고, 길이가 4여야 합니다.
 */
public class Card {
    private String id;
    private String description;
    private String pinNumber;

    private Card() {
    }

    public static class Builder {
        private final Card card;

        private Builder() {
            card = new Card();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(String id) {
            card.id = id;
            return this;
        }

        public Builder description(String description) {
            card.description = description;
            return this;
        }

        public Builder pinNumber(String pinNumber) {
            card.pinNumber = pinNumber;
            return this;
        }

        public Card build() {
            return card;
        }
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
