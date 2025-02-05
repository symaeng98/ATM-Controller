package bank.atm.domain.card.controller;

import bank.atm.domain.card.dto.CardCreate;
import bank.atm.domain.card.dto.CardVerify;
import bank.atm.domain.card.entity.Card;
import bank.atm.domain.card.service.CardService;

public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    public void createCard(CardCreate cardCreate) {
        cardService.createCard(cardCreate);
    }

    public Card getCard(String cardId) {
        return cardService.getCardById(cardId);
    }

    public void verifyPinNumber(CardVerify cardVerify) {
        cardService.verifyPinNumber(cardVerify);
    }
}
