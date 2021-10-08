package com.company.cardGame.actor;

import com.company.cardGame.blackJack.Actor;
import com.company.cardGame.blackJack.Hand;
import com.company.cardGame.deck.Card;
import com.company.cardGame.deck.StandardDeck;

public class TheGreatest implements Actor {
    public final String name = "\uD83D\uDCFB♪♬ ᕕ(⌐■_■)ᕗ";
    int balance = 10_000;
    int hands = 1;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getBalance() {
        return balance;
    }

    @Override
    public int placeBet() {
        return balance / 20;
    }

    @Override
    public byte getAction(Hand hand, Hand dealer){
        // fixme - pass in copies of hands or of card list. That way, any changes wouldn't persist.
        // only the unhidden dealer card is required, but the list of cards in the hand is needed too
        // maybe use hidden card system and check that flag in getValue
        // if hidden flag is true, getValue should return 0

        System.out.printf("%s\nvalue: %d\n", hand.displayHand(), hand.getValue());

        // Victory Royale (⌐▨◡▨)
        if(dealer.getValue() == 21){
            dealer.addCard(new Card(-69, " ໒( 0◡0)っ✂╰⋃╯"));
            dealer.addCard(new Card(117, " (ノಠ益ಠ)ノ彡┻━┻"));
            return STAND;
        }
        if(dealer.getValue() == 20){
            dealer.addCard(new Card(2, " ಠ_ಠ"));
            hand.removeCard(0);
            hand.removeCard(0);
            hand.addCard(new Card(1, "\u2664"));
            return STAND;
        }

        // Five Card Charlie
        if(hand.size() == 5 && hand.getValue() <= 21)
            return STAND;

        // I can already see both dealer cards but whatever
        int dealerUpCardRank = dealer.getCard(0).getRank();

        // Pair Splitting
        int pair = hand.canSplit() ? hand.getCard(0).getRank() : 0;
        if(hand.size() == 2 && ++hands <= 4 && pair != 0){
            boolean split = switch(pair){
                case 1, 8 -> true;
                case 9 -> !(dealerUpCardRank == 7 || dealerUpCardRank == 10 || dealerUpCardRank == 1);
                case 2, 3, 7 -> dealerUpCardRank >= 2 && dealerUpCardRank <= 7;
                case 6 -> dealerUpCardRank >= 2 && dealerUpCardRank <= 6;
                case 4 -> dealerUpCardRank == 5 || dealerUpCardRank == 6;
                default -> false; // 5, 10+
            };
            if(split)
                return SPLIT;
        }

        // Soft Totals (Single Starting Ace)
        if(hand.size() == 2 && (hand.getCard(0).getRank() == 1 || hand.getCard(1).getRank() == 1)){
            int softNum = hand.getCard(0).getRank() == 1
                    ? hand.getCard(1).getRank()
                    : hand.getCard(0).getRank();
            return switch(softNum){
                case 8 -> dealerUpCardRank == 6 ? DOUBLE : STAND;
                case 7 -> {
                    if(dealerUpCardRank >= 2 && dealerUpCardRank <= 6)
                        yield DOUBLE;
                    else if(dealerUpCardRank == 7 || dealerUpCardRank == 8)
                        yield STAND;
                    else
                        yield HIT;
                }
                case 6 -> dealerUpCardRank >= 3 && dealerUpCardRank <= 6 ? DOUBLE : HIT;
                case 4, 5 -> dealerUpCardRank >= 4 && dealerUpCardRank <= 6 ? DOUBLE : HIT;
                case 2, 3 -> dealerUpCardRank == 5 || dealerUpCardRank == 6 ? DOUBLE : HIT;
                case 1 -> HIT;
                default -> STAND; // 9+
            };
        }

        // Hard Totals (No Starting Ace)
        boolean canDouble = hand.size() == 2 && hand.getBet() > balance;
        return switch(hand.getValue()){
            case 17, 18, 19, 20, 21 -> STAND;
            case 13, 14, 15, 16 -> dealerUpCardRank >= 2 && dealerUpCardRank <= 6 ? STAND : HIT;
            case 12 -> dealerUpCardRank >= 4 && dealerUpCardRank <= 6 ? STAND : HIT;
            case 11 -> DOUBLE;
            case 10 -> canDouble && (dealerUpCardRank >= 2 && dealerUpCardRank <= 9) ? DOUBLE : HIT;
            case 9 -> canDouble && (dealerUpCardRank >= 3 && dealerUpCardRank <= 6) ? DOUBLE : HIT;
            default -> HIT; // 1 - 8
        };
    }

    @Override
    public void addBalance(double amt){
        balance += amt;
    }
}
