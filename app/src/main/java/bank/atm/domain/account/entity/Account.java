package bank.atm.domain.account.entity;

import java.util.UUID;

/**
 * 계좌 클래스
 * 입금, 출금, 잔액 확인이 가능합니다.
 * 계좌 생성 시 잔액은 0원으로 초기화됩니다.
 * id는 UUID를 String으로 변환한 값입니다.
 */
public class Account {
    private final String id = UUID.randomUUID().toString();
    private String cardId;
    private int balance = 0;
    private String description;

    private Account() {
    }

    public static class Builder {
        private final Account account;

        private Builder() {
            account = new Account();
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder cardId(String cardId) {
            account.cardId = cardId;
            return this;
        }

        public Builder description(String description) {
            account.description = description;
            return this;
        }

        public Account build() {
            return account;
        }
    }

    public String getId() {
        return id;
    }

    public int getBalance() {
        return balance;
    }

    public String getCardId() {
        return cardId;
    }

    public String getDescription() {
        return description;
    }

    public void deposit(int amount) {
        balance += amount;
    }

    public void withdraw(int amount) {
        if (balance < amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }
        balance -= amount;
    }
}
