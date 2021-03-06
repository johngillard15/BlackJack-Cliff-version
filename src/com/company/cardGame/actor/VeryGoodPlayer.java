package com.company.cardGame.actor;

import com.company.cardGame.blackJack.Actor;
import com.company.cardGame.blackJack.Hand;

public class VeryGoodPlayer implements Actor {
    private static final String[] SUITS = {"\u2664", "\u2665", "\u2666", "\u2667"};
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
        int bet = balance / 10;
        balance -= bet;
        return bet;
    }

    @Override
    public byte getAction(Hand hand, Hand dealer){
        // fixme - pass in copies of hands or of card list. That way, any changes wouldn't persist.
        //  only the unhidden dealer card is required, but the list of cards in the hand is needed too
        //  maybe use hidden card system and check that flag in getValue
        //  if hidden flag is true, getValue should return 0

        // Victory Royale (⌐▨◡▨)
        if(dealer.getValue() == 21){
            dealer.addCard(new com.company.cardGame.deck.Card(-69, " ໒( 0◡0)っ✂╰⋃╯"));
            dealer.addCard(new com.company.cardGame.deck.Card(117, " (ノಠ益ಠ)ノ彡┻━┻"));
            return STAND;
        }
        if(dealer.getValue() == 20){
            dealer.addCard(new com.company.cardGame.deck.Card(2, " ಠ_ಠ"));
            return STAND;
        }

        int score;
        do{
            score = hand.getValue();

            System.out.println(name + " " + hand.displayHand());

            if(hand.canSplit() && ++hands <= 4)
                return SPLIT;
            if((dealer.getValue() >= 17 && dealer.getValue() < score) || score >= 17)
                return STAND;

            hand.addCard(new com.company.cardGame.deck.Card((int)(Math.random() * (21 - score) + 1),
                    SUITS[(int)(Math.random() * SUITS.length)]));
            System.out.println("Hit");
        }while(true);
    }

    @Override
    public void addBalance(double amt) {
        balance += amt;
    }
}
