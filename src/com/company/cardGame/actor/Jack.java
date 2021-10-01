package com.company.cardGame.actor;

import com.company.cardGame.blackJack.Actor;
import com.company.cardGame.blackJack.Hand;
import com.company.cardGame.deck.Card;

public class Jack implements Actor {
    private final String name = "Jack";
    private int balance = 21;
    public static final String[] SUITS = {"\u2664", "\u2665", "\u2666", "\u2667"};

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
        return 21;
    }

    @Override
    public byte getAction(Hand hand, Hand dealer){
        while(hand.size() > 0)
            hand.removeCard(0);

        for(int i = 0; i < 21; i++)
            hand.addCard(new Card(1, SUITS[i % 4]));
        return Actor.STAND;
    }

    @Override
    public void addBalance(int amt) {
        balance = 21;
    }
}
