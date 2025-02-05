package bank.atm;

import bank.atm.database.Database;
import bank.atm.domain.account.controller.AccountController;
import bank.atm.domain.account.dto.AccountCreate;
import bank.atm.domain.account.entity.Account;
import bank.atm.domain.account.repository.AccountRepository;
import bank.atm.domain.account.repository.MemoryAccountRepository;
import bank.atm.domain.account.service.AccountService;
import bank.atm.domain.card.dto.CardCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AccountControllerTest {
    private Database database;

    private AccountController accountController;

    @BeforeEach
    void setUp() {
        database = new Database();

        // 계좌 의존성 주입
        AccountRepository accountRepository = new MemoryAccountRepository(database);
        AccountService accountService = new AccountService(accountRepository);
        accountController = new AccountController(accountService);

        CardCreate cardCreate = new CardCreate("mycard1", "내 첫 카드", "1234");
        database.createCard(cardCreate.toCard());
    }

    @Test
    void create_Account_Success() {
        AccountCreate accountCreate = new AccountCreate("mycard1", "월급 계좌");

        accountController.createAccount(accountCreate);
        List<Account> accounts = database.getAllAccountsByCardId("mycard1");

        Account account = accounts.get(0);
        assertEquals("mycard1", account.getCardId());
        assertEquals("월급 계좌", account.getDescription());
        assertEquals(0, account.getBalance()); //생성 직후 잔액은 0이어야 합니다.
    }

    @Test
    void get_Accounts_By_Card_Id() {
        AccountCreate accountCreate1 = new AccountCreate("mycard1", "월급 계좌");
        AccountCreate accountCreate2 = new AccountCreate("mycard1", "월급 계좌2");
        database.createAccount(accountCreate1.toAccount());
        database.createAccount(accountCreate2.toAccount());

        List<Account> accounts = accountController.getAccountsByCardId("mycard1");

        assertEquals(2, accounts.size());
        assertEquals("mycard1", accounts.get(0).getCardId());
        assertEquals("월급 계좌", accounts.get(0).getDescription());
        assertEquals(0, accounts.get(0).getBalance());
        assertEquals("mycard1", accounts.get(1).getCardId());
        assertEquals("월급 계좌2", accounts.get(1).getDescription());
        assertEquals(0, accounts.get(1).getBalance());
    }

    @Test
    void deposit_Success() {
        AccountCreate accountCreate = new AccountCreate("mycard1", "월급 계좌");
        database.createAccount(accountCreate.toAccount());
        List<Account> accounts = database.getAllAccountsByCardId("mycard1");
        Account account = accounts.get(0);
        String accountId = account.getId();

        accountController.deposit(accountId, 1000);

        assertEquals(1000, account.getBalance());
    }

    @Test
    void withdraw_Success() {
        AccountCreate accountCreate = new AccountCreate("mycard1", "월급 계좌");
        database.createAccount(accountCreate.toAccount());
        List<Account> accounts = database.getAllAccountsByCardId("mycard1");
        Account account = accounts.get(0);
        String accountId = account.getId();

        accountController.deposit(accountId, 10000);
        accountController.withdraw(accountId, 3000);

        assertEquals(7000, account.getBalance());
    }

    @Test
    void withdraw_Fail_NotEnoughBalance() {
        AccountCreate accountCreate = new AccountCreate("mycard1", "월급 계좌");
        database.createAccount(accountCreate.toAccount());
        List<Account> accounts = database.getAllAccountsByCardId("mycard1");
        Account account = accounts.get(0);
        String accountId = account.getId();

        assertThrows(IllegalStateException.class, () -> accountController.withdraw(accountId, 1000));
    }
}
