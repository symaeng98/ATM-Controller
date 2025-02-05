package bank.atm.domain.account.repository;

import bank.atm.domain.account.entity.Account;

import java.util.List;

public interface AccountRepository {
    void save(Account account);

    Account findById(String id);

    List<Account> findAllByCardId(String cardId);

    void deposit(String accountId, int amount);

    void withdraw(String accountId, int amount);
}
