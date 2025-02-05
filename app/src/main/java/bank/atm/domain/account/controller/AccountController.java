package bank.atm.domain.account.controller;

import bank.atm.domain.account.dto.AccountCreate;
import bank.atm.domain.account.service.AccountService;
import bank.atm.domain.account.entity.Account;

import java.util.List;

public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    public void createAccount(AccountCreate accountCreate) {
        accountService.createAccount(accountCreate);
    }

    public Account getAccount(String accountId) {
        return accountService.getAccountById(accountId);
    }

    public List<Account> getAccountsByCardId(String cardId) {
        return accountService.getAllAccountsByCardId(cardId);
    }

    public void deposit(String accountId, int amount) {
        accountService.deposit(accountId, amount);
    }

    public void withdraw(String accountId, int amount) {
        accountService.withdraw(accountId, amount);
    }
}
