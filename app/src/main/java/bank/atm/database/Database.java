package bank.atm.database;

import bank.atm.domain.account.entity.Account;
import bank.atm.domain.card.entity.Card;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 데이터베이스 클래스입니다.
 * Account와 Card를 id로 구분하여 저장합니다.
 */
public class Database {
    private final Map<String, Account> accountDb = new HashMap<>();
    private final Map<String, Card> cardDb = new HashMap<>();

    public void createAccount(Account account) {
        String id = account.getId();
        if (accountDb.containsKey(id)) {
            throw new IllegalArgumentException("이미 존재하는 계좌 id입니다.");
        }
        accountDb.put(id, account);
    }

    public Account getAccountById(String id) {
        if (!accountDb.containsKey(id)) {
            throw new IllegalArgumentException("id에 해당하는 계좌가 없습니다.");
        }
        return accountDb.get(id);
    }

    public List<Account> getAllAccountsByCardId(String cardId) {
        if (!cardDb.containsKey(cardId)) {
            throw new IllegalArgumentException("id에 해당하는 계좌가 없습니다.");
        }

        return accountDb.values().stream()
                .filter(account -> account.getCardId().equals(cardId))
                .toList();
    }

    public void createCard(Card card) {
        String id = card.getId();
        if (cardDb.containsKey(id)) {
            throw new IllegalArgumentException("이미 존재하는 카드 id입니다.");
        }
        cardDb.put(id, card);
    }

    public Card getCardById(String id) {
        if (!cardDb.containsKey(id)) {
            throw new IllegalArgumentException("id에 해당하는 카드가 없습니다.");
        }
        return cardDb.get(id);
    }

    public void clear() {
        accountDb.clear();
        cardDb.clear();
    }
}
