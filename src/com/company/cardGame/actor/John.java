package com.company.cardGame.actor;

import com.company.cardGame.blackJack.Actor;
import com.company.cardGame.blackJack.Hand;
import com.company.cardGame.deck.Card;

public class John implements Actor {
    public final String name = "xXx_john_xXx";
    int balance = 1_001;
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
        return balance / 10;
    }

    @Override
    public byte getAction(Hand hand, Hand dealer){
        System.out.printf("%s\n%s\nvalue: %d\n", name, hand.displayHand(), hand.getValue());

        // Five Card Charlie
        if(hand.size() == 5 && hand.getValue() <= 21)
            return STAND;

        // Pair Splitting
        int pair = hand.getCard(0).getRank() == hand.getCard(1).getRank()
                ? hand.getCard(0).getRank()
                : 0;
        if(hand.size() == 2 && hands < 4 && pair != 0){
            boolean split = switch(pair){
                case 10, 11, 12, 13 -> false;
                default -> true;
            };
            if(split){
                ++hands;
                return SPLIT;
            }
        }

        // Soft Totals (Single Starting Ace)
        if(hand.size() == 2 && (hand.getCard(0).getRank() == 1 || hand.getCard(1).getRank() == 1)){
            int softNum = hand.getCard(0).getRank() == 1
                    ? hand.getCard(1).getRank()
                    : hand.getCard(0).getRank();
            return switch(softNum){
                case 1, 2, 3 -> DOUBLE;
                case 4, 5 -> HIT;
                default -> STAND;
            };
        }

        // Hard Totals (No Starting Ace)
        int dealerUpCardRank = dealer.getCard(0).getRank();
        boolean canDouble = hand.size() == 2 && hand.getBet() > balance;
        return switch(hand.getValue()){
            case 17, 18, 19, 20, 21 -> STAND;
            case 15, 16 -> dealerUpCardRank >= 6 ? HIT : STAND;
            case 9, 10, 11 -> canDouble ? DOUBLE : HIT;
            default -> HIT; // 1 - 8
        };
    }

    @Override
    public void addBalance(double amt) {
        balance += amt;
    }
}
