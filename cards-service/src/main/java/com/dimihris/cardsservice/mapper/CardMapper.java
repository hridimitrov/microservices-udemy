package com.dimihris.cardsservice.mapper;

import com.dimihris.cardsservice.dto.CardDto;
import com.dimihris.cardsservice.entity.Card;

public class CardMapper {

    public static CardDto mapToCardsDto(Card card, CardDto cardsDto) {
        cardsDto.setCardNumber(card.getCardNumber());
        cardsDto.setCardType(card.getCardType());
        cardsDto.setMobileNumber(card.getMobileNumber());
        cardsDto.setTotalLimit(card.getTotalLimit());
        cardsDto.setAvailableAmount(card.getAvailableAmount());
        cardsDto.setAmountUsed(card.getAmountUsed());

        return cardsDto;
    }

    public static Card mapToCards(CardDto cardDto, Card cards) {
        cards.setCardNumber(cardDto.getCardNumber());
        cards.setCardType(cardDto.getCardType());
        cards.setMobileNumber(cardDto.getMobileNumber());
        cards.setTotalLimit(cardDto.getTotalLimit());
        cards.setAvailableAmount(cardDto.getAvailableAmount());
        cards.setAmountUsed(cardDto.getAmountUsed());

        return cards;
    }
}
