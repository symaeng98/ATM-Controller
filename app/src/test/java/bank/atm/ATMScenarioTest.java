package bank.atm;

import bank.atm.database.Database;
import bank.atm.domain.account.controller.AccountController;
import bank.atm.domain.account.dto.AccountCreate;
import bank.atm.domain.account.entity.Account;
import bank.atm.domain.account.repository.AccountRepository;
import bank.atm.domain.account.repository.MemoryAccountRepository;
import bank.atm.domain.account.service.AccountService;
import bank.atm.domain.card.controller.CardController;
import bank.atm.domain.card.dto.CardCreate;
import bank.atm.domain.card.dto.CardVerify;
import bank.atm.domain.card.entity.Card;
import bank.atm.domain.card.repository.CardRepository;
import bank.atm.domain.card.repository.MemoryCardRepository;
import bank.atm.domain.card.service.CardService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ATMScenarioTest {
    private Database database;

    private AccountController accountController;
    private CardController cardController;

    @BeforeEach
    void setUp() {
        database = new Database();

        // 카드 의존성 주입
        CardRepository cardRepository = new MemoryCardRepository(database);
        CardService cardService = new CardService(cardRepository);
        cardController = new CardController(cardService);

        // 계좌 의존성 주입
        AccountRepository accountRepository = new MemoryAccountRepository(database);
        AccountService accountService = new AccountService(accountRepository);
        accountController = new AccountController(accountService);

        // 카드와 계좌 3개 생성
        String cardId = "mycard1";
        String cardDescription = "내 첫 카드";
        String pinNumber = "1234";
        CardCreate cardCreate = new CardCreate(cardId, cardDescription, pinNumber);
        database.createCard(cardCreate.toCard());

        String accountDescription = "월급 계좌";
        AccountCreate accountCreate = new AccountCreate(cardId, accountDescription);
        database.createAccount(accountCreate.toAccount());

        String accountDescription2 = "데이트 통장";
        AccountCreate accountCreate2 = new AccountCreate(cardId, accountDescription2);
        database.createAccount(accountCreate2.toAccount());

        String accountDescription3 = "적금 통장";
        AccountCreate accountCreate3 = new AccountCreate(cardId, accountDescription3);
        database.createAccount(accountCreate3.toAccount());
    }

    @AfterEach
    public void clear() {
        database.clear();
    }

    /**
     * 해당 테스트는 과제에서 주어진 기본 시나리오를 성공하는 테스트입니다.
     * PIN 번호 오입력, 출금 실패 테스트는 AccountControllerTest, CardControllerTest에서 확인할 수 있습니다.
     * [기본 시나리오]
     * 1. 카드를 삽입합니다. (Insert Card)
     * 2. 카드의 PIN 번호를 입력합니다. (PIN Number)
     * 3. 계좌들 중 원하는 계좌를 선택합니다. (Select Account)
     *   3-1) 등록된 카드의 모든 계좌를 확인합니다.
     *   3-2) 특정 계좌를 불러옵니다.
     * 4. 잔액을 확인합니다.
     * 5. 일정 금액을 입금합니다.
     * 6. 일정 금액을 출금합니다.
     */
    @Test
    void default_Scenario_Test_Success() {
        String cardId = "mycard1";

        // 1. 카드를 삽입합니다.
        Card card = cardController.getCard(cardId);
        System.out.println("-----");
        System.out.println("카드를 삽입합니다...");
        assertEquals("내 첫 카드", card.getDescription());

        // 2. PIN 번호를 입력합니다. (PIN 번호가 일치하지 않으면 예외를 발생시킵니다.)
        System.out.println("-----");
        System.out.println("PIN 번호를 검증합니다...");
        cardController.verifyPinNumber(new CardVerify(cardId, "1234"));

        // 3-1) 등록된 카드의 모든 계좌를 확인합니다.
        System.out.println("-----");
        System.out.println("카드의 모든 계좌를 불러옵니다...");
        List<Account> accounts = accountController.getAccountsByCardId(cardId);
        assertEquals(3, accounts.size());

        // 3-2) 특정 계좌(e.g. 데이트 통장)를 불러옵니다.
        // 클라이언트 UI 상 id의 리스트로 나열될 것으로 예상되어 선택된 id를 불러옵니다.
        System.out.println("-----");
        String selectedAccountId = accounts.get(1).getId();
        System.out.println("계좌를 선택합니다...");
        Account account = accountController.getAccount(selectedAccountId);

        // 4. 잔액을 확인합니다.
        System.out.println("-----");
        System.out.println("잔액을 확인합니다...");
        int balance = account.getBalance();
        System.out.println("계좌에 남은 잔액: " + balance);

        // 5. 일정 금액을 입금합니다.
        System.out.println("-----");
        int beforeDeposit = account.getBalance();
        System.out.println("입금 전 잔액: " + beforeDeposit);
        System.out.println("2000달러를 입금합니다...");
        accountController.deposit(account.getId(), 2000);
        int afterDeposit = account.getBalance();
        System.out.println("입금 후 잔액: " + afterDeposit);
        assertEquals(2000, afterDeposit);

        // 6. 일정 금액을 출금합니다.
        System.out.println("-----");
        int beforeWithdraw = account.getBalance();
        System.out.println("출금 전 잔액: " + beforeWithdraw);
        System.out.println("500달러를 출금합니다...");
        accountController.withdraw(account.getId(), 500);
        int afterWithdraw = account.getBalance();
        System.out.println("출금 후 잔액: " + afterWithdraw);
        assertEquals(1500, afterWithdraw);
    }
}
