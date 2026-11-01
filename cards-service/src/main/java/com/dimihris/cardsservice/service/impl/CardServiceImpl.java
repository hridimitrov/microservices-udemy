package com.dimihris.cardsservice.service.impl;

import com.dimihris.cardsservice.constants.CardConstants;
import com.dimihris.cardsservice.dto.CardDto;
import com.dimihris.cardsservice.entity.Card;
import com.dimihris.cardsservice.exception.CardAlreadyExistsException;
import com.dimihris.cardsservice.exception.ResourceNotFoundException;
import com.dimihris.cardsservice.mapper.CardMapper;
import com.dimihris.cardsservice.repository.CardRepository;
import com.dimihris.cardsservice.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private CardRepository cardRepository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Card> optionalCards= cardRepository.findByMobileNumber(mobileNumber);

        if(optionalCards.isPresent()){
            throw new CardAlreadyExistsException("Card already registered with given mobileNumber "+mobileNumber);
        }

        cardRepository.save(createNewCard(mobileNumber));
    }

    @Override
    public CardDto getCardDetails(String mobileNumber) {

        Card card = cardRepository.findByMobileNumber(mobileNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));

        return CardMapper.mapToCardsDto(card, new CardDto());
    }

    @Override
    public boolean updateCard(CardDto cardsDto) {
        Card cards = cardRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "CardNumber", cardsDto.getCardNumber()));
        CardMapper.mapToCards(cardsDto, cards);
        cardRepository.save(cards);
        return  true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {

        Card cards = cardRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        cardRepository.deleteById(cards.getCardId());

        return true;
    }

    private Card createNewCard(String mobileNumber) {
        Card newCard = new Card();
        long randomCardNumber = 100000000000L + new Random().nextInt(900000000);
        newCard.setCardNumber(Long.toString(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardConstants.CREDIT_CARD);
        newCard.setTotalLimit(CardConstants.NEW_CARD_LIMIT);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(CardConstants.NEW_CARD_LIMIT);

        return newCard;
    }
}
