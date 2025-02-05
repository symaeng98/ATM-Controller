package bank.atm.domain.account.dto;

import bank.atm.domain.account.entity.Account;


public class AccountCreate {
    private final String cardId;
    private final String description;

    public AccountCreate(String cardId, String description) {
        this.cardId = cardId;
        this.description = description;
    }

    public Account toAccount() {
        return Account.Builder.builder()
                .cardId(cardId)
                .description(description)
                .build();
    }
}
