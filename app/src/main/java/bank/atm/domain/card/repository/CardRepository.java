package bank.atm.domain.card.repository;

import bank.atm.domain.card.entity.Card;

public interface CardRepository {
    void save(Card card);

    Card findById(String id);
}
