package bank.atm.domain.card.service;

import bank.atm.domain.card.dto.CardCreate;
import bank.atm.domain.card.dto.CardVerify;
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

    public Card getCardById(String cardId) {
        return cardRepository.findById(cardId);
    }

    public void verifyPinNumber(CardVerify cardVerify) {
        String cardId = cardVerify.getId();
        String pinNumber = cardVerify.getPinNumber();

        Card card = cardRepository.findById(cardId);
        verify(card, pinNumber);
    }

    private void verify(Card card, String pinNumber) {
        if (!card.verify(pinNumber)) {
            throw new IllegalArgumentException("잘못된 PIN 번호입니다.");
        }
    }
}
