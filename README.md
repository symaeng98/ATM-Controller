# ATM Controller 구현
## Git Clone 방법
아래의 명령어를 실행하여 로컬에 프로젝트를 클론합니다.
```
git clone https://github.com/symaeng98/ATM-Controller.git && cd ATM-Controller
```  

## 테스트 방법
테스트는 app/src/test/java/bank/atm 하위에 있으며 총 3개의 파일로 구성됩니다.
1. **ATMScenarioTest.java**: 기본 시나리오 테스트입니다.
2. CardControllerTest.java: Card 엔티티 관련 테스트입니다.
3. AccountControllerTest.java: Account 엔티티 관련 테스트입니다.

### 1번
프로그램의 플로우를 보여주기 위해 System.out.println으로 출력합니다.
이때 한 번 진행한 테스트는 up-to-date 처리되어 출력이 되지 않기 때문에 "--rerun-tasks" 옵션을 사용합니다.
```
./gradlew test --tests ATMScenarioTest --rerun-tasks
```

### 2번
```
./gradlew test --tests CardControllerTest
```   

### 3번
```
./gradlew test --tests AccountControllerTest
``` 


### 전체 테스트
한 번에 모든 테스트를 확인하고 싶다면 아래의 명령어를 사용합니다.
```
./gradlew test
```



## ATMScenarioTest.java
```java
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
```
## CardControllerTest.java
```java
public class CardControllerTest {
    private Database database;
    private CardController cardController;

    @BeforeEach
    void setUp() {
        database = new Database();

        // 카드 의존성 주입
        CardRepository cardRepository = new MemoryCardRepository(database);
        CardService cardService = new CardService(cardRepository);
        cardController = new CardController(cardService);
    }

    @AfterEach
    public void clear() {
        database.clear();
    }

    @Test
    void create_Card_Success() {
        CardCreate cardCreate = new CardCreate("mycard1", "내 첫 카드", "1234");

        cardController.createCard(cardCreate);
        Card foundCard = database.getCardById("mycard1");

        assertEquals("mycard1", foundCard.getId());
        assertEquals("내 첫 카드", foundCard.getDescription());
    }

    @Test
    void create_Card_Fail_Already_Exists() {
        CardCreate cardCreate = new CardCreate("mycard1", "내 첫 카드", "1234");
        database.createCard(cardCreate.toCard());

        assertThrows(IllegalArgumentException.class,
                () -> cardController.createCard(new CardCreate("mycard1", "잘못된 카드 id", "4521")));
    }

    @Test
    void create_Card_Fail_Wrong_PinNumber() {
        assertThrows(IllegalArgumentException.class,
                () -> new CardCreate("mycard1", "내 첫 카드", "125789"));
    }

    @Test
    void get_Card_Success() {
        CardCreate cardCreate = new CardCreate("mycard1", "내 첫 카드", "1234");
        database.createCard(cardCreate.toCard());

        Card foundCard = cardController.getCard("mycard1");

        assertEquals("mycard1", foundCard.getId());
        assertEquals("내 첫 카드", foundCard.getDescription());
    }

    @Test
    void verify_PinNumber_Fail() {
        CardCreate cardCreate = new CardCreate("mycard1", "내 첫 카드", "1234");
        database.createCard(cardCreate.toCard());
        CardVerify verify = new CardVerify("mycard1", "4321");

        assertThrows(IllegalArgumentException.class, () -> cardController.verifyPinNumber(verify));
    }
}
```


## AccountControllerTest.java
```java
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

    @AfterEach
    public void clear() {
        database.clear();
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
        assertEquals("mycard1", accounts.get(1).getCardId());
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
```