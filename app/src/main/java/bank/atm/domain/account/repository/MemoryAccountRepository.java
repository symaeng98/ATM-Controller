package bank.atm.domain.account.repository;

import bank.atm.database.Database;
import bank.atm.domain.account.entity.Account;

import java.util.List;

public class MemoryAccountRepository implements AccountRepository{
    private final Database database;

    public MemoryAccountRepository(Database database) {
        this.database = database;
    }

    @Override
    public void save(Account account) {
        database.createAccount(account);
    }

    @Override
    public Account findById(String id) {
        return database.getAccountById(id);
    }

    @Override
    public List<Account> findAllByCardId(String cardId) {
        return database.getAllAccountsByCardId(cardId);
    }

    @Override
    public void deposit(String accountId, int amount) {
        Account account = database.getAccountById(accountId);
        account.deposit(amount);
    }

    @Override
    public void withdraw(String accountId, int amount) {
        Account account = database.getAccountById(accountId);
        account.withdraw(amount);
    }
}
