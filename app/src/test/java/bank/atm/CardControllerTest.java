package bank.atm;

import bank.atm.database.Database;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
