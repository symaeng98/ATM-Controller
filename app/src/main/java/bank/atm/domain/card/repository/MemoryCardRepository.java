package bank.atm.domain.card.repository;

import bank.atm.database.Database;
import bank.atm.domain.card.entity.Card;

public class MemoryCardRepository implements CardRepository{
    private final Database database;
    public MemoryCardRepository(Database database) {
        this.database = database;
    }

    @Override
    public void save(Card card) {
        database.createCard(card);
    }

    @Override
    public Card findById(String id) {
        return database.getCardById(id);
    }
}
