package bank.atm.domain.card.service;

import bank.atm.domain.card.dto.CardCreate;
import bank.atm.domain.card.entity.Card;
import bank.atm.domain.card.repository.CardRepository;

public class CardService {
    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public void createCard(CardCreate cardCreate) {
        Card card = cardCreate.toCard();
        cardRepository.save(card);
    }

    public Card getCardById(String id) {
        return cardRepository.findById(id);
    }
}
