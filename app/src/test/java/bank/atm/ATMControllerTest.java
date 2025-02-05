package bank.atm;

import bank.atm.database.Database;
import bank.atm.domain.card.controller.CardController;
import bank.atm.domain.card.dto.CardCreate;
import bank.atm.domain.card.dto.CardVerify;
import bank.atm.domain.card.entity.Card;
import bank.atm.domain.card.repository.CardRepository;
import bank.atm.domain.card.repository.MemoryCardRepository;
import bank.atm.domain.card.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ATMControllerTest {
    private Database database;
    private CardController cardController;
    private CardService cardService;
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() {
        database = new Database();

        // 카드 초기화
        cardRepository = new MemoryCardRepository(database);
        cardService = new CardService(cardRepository);
        cardController = new CardController(cardService);

        // 계좌 초기화
//        cardRepository = new MemoryAccountRepository(database);
//        cardService = new CardService(cardRepository);
//        cardController = new CardController(cardService);
    }


    /**
     * 카드 관련 테스트 (Controller, Service, Repository)
     */
    @Test
    void createCardSuccess() {
        CardCreate card = new CardCreate("mycard1", "내 첫 카드", "1234");
        cardController.createCard(card);

        Card foundCard = database.getCard("mycard1");

        assertEquals("mycard1", foundCard.getId());
        assertEquals("내 첫 카드", foundCard.getDescription());
    }

    @Test
    void createCardFail_AlreadyExists() {
        CardCreate cardCreate = new CardCreate("mycard1", "내 첫 카드", "1234");
        cardController.createCard(cardCreate);

        assertThrows(
                IllegalArgumentException.class,
                () -> cardController.createCard(new CardCreate("mycard1", "잘못된 카드 id", "4521"))
        );
    }

    @Test
    void createCardFail_WrongPINNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new CardCreate("mycard1", "내 첫 카드", "125789")
        );
    }

    @Test
    void getCardSuccess() {
        CardCreate card = new CardCreate("mycard1", "내 첫 카드", "1234");
        cardController.createCard(card);

        Card foundCard = cardController.getCard("mycard1");

        assertEquals("mycard1", foundCard.getId());
        assertEquals("내 첫 카드", foundCard.getDescription());
    }

    @Test
    void verifyPinFail() {
        CardCreate card = new CardCreate("mycard1", "내 첫 카드", "1234");
        cardController.createCard(card);

        CardVerify verify = new CardVerify("mycard1", "4321");

        assertThrows(
                IllegalArgumentException.class,
                () -> cardController.verifyPinNumber(verify)
        );
    }
}
