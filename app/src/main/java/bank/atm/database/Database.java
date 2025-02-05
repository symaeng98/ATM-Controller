package bank.atm.database;

import bank.atm.domain.account.entity.Account;
import bank.atm.domain.card.entity.Card;

import java.util.HashMap;
import java.util.Map;

/**
 * 데이터베이스 클래스입니다.
 * Account와 Card를 id로 구분하여 저장합니다.
 */
public class Database {
    private Map<String, Account> accountDb = new HashMap<>();
    private Map<String, Card> cardDb = new HashMap<>();

    public void createCard(String id, Card card) {
        if (cardDb.containsKey(id)) {
            throw new IllegalArgumentException("이미 존재하는 카드 id입니다.");
        }
        cardDb.put(id, card);
    }

    public Card getCard(String id) {
        if (!cardDb.containsKey(id)) {
            throw new IllegalArgumentException("id에 해당하는 카드가 없습니다.");
        }
        return cardDb.get(id);
    }
}
