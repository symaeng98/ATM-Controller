package bank.atm.domain.account.service;

import bank.atm.domain.account.dto.AccountCreate;
import bank.atm.domain.account.entity.Account;
import bank.atm.domain.account.repository.AccountRepository;

import java.util.List;

public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void createAccount(AccountCreate accountCreate) {
        Account account = accountCreate.toAccount();
        accountRepository.save(account);
    }

    public Account getAccountById(String accountId) {
        return accountRepository.findById(accountId);
    }

    public List<Account> getAllAccountsByCardId(String cardId) {
        return accountRepository.findAllByCardId(cardId);
    }

    public void deposit(String accountId, int amount) {
        accountRepository.deposit(accountId, amount);
    }

    public void withdraw(String accountId, int amount) {
        Account account = accountRepository.findById(accountId);
        if (account.getBalance() < amount) {
            throw new IllegalStateException("잔액이 부족합니다.");
        }

        accountRepository.withdraw(accountId, amount);
    }
}
