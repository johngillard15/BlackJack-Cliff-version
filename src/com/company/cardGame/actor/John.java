package com.company.cardGame.actor;

import com.company.cardGame.blackJack.Actor;
import com.company.cardGame.blackJack.Hand;

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
        // Five Card Charlie
        if(hand.size() == 5 && !(hand.getValue() <= 21))
            return Actor.STAND;

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
                return Actor.SPLIT;
            }
        }

        if(hand.size() == 2 && (hand.getCard(0).getRank() == 1 || hand.getCard(1).getRank() == 1)){
            int softNum = hand.getCard(0).getRank() == 1
                    ? hand.getCard(1).getRank()
                    : hand.getCard(0).getRank();
            return switch(softNum){
                case 1, 2, 3 -> Actor.DOUBLE;
                case 4, 5 -> Actor.HIT;
                default -> Actor.STAND;
            };
        }

        int dealerUpCardRank = dealer.getCard(0).getRank();
        boolean canDouble = hand.size() == 2 && hand.getBet() > balance;
        return switch(hand.getValue()){
            case 17, 18, 19, 20, 21 -> Actor.STAND;
            case 15, 16 -> dealerUpCardRank >= 6 ? Actor.HIT : Actor.STAND;
            case 9, 10, 11 -> canDouble ? Actor.DOUBLE : Actor.HIT;
            default -> Actor.HIT; // 1 - 8
        };
    }

    @Override
    public void addBalance(int amt) {
        balance += amt;
    }
}
