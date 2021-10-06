package com.company.cardGame.actor;

import com.company.cardGame.blackJack.Actor;
import com.company.cardGame.blackJack.Hand;
import com.company.cardGame.deck.Card;

public class Loser implements Actor {
    public final String name = "Loser";
    int balance = 1;

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
        hand.removeCard(0);
        hand.removeCard(0);

        hand.addCard(new Card(0, "∩"));
        
        System.out.println("I sure hope the dealer busts...");
        return STAND;
    }

    @Override
    public void addBalance(double amt){
        balance += amt;
    }
}
