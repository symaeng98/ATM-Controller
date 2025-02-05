package bank.atm.domain.account.entity;

import java.util.UUID;

/**
 * 계좌 클래스
 * 입금, 출금, 잔액 확인이 가능합니다.
 * id는 계좌 생성 시 랜덤 UUID값으로 자동 생성됩니다.
 */
public class Account {
    private final String id = UUID.randomUUID().toString();
    private int balance;

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

        public Account build() {
            return account;
        }
    }

    public int getBalance() {
        return balance;
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
