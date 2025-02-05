package bank.atm.domain.card.controller;

import bank.atm.domain.card.dto.CardCreate;
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

    public Card getCard(String id) {
        return cardService.getCardById(id);
    }
}
