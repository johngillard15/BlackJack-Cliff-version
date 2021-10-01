package com.company.cardGame.actor;

import com.company.cardGame.blackJack.Actor;
import com.company.cardGame.blackJack.Hand;

public class John implements Actor {
    public final String name = "\uD83D\uDCFB♪♬ ᕕ(⌐■_■)ᕗ";
    int balance = 69_000_000;

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
        return 0;
    }

    @Override
    public byte getAction(Hand hand) {
        return 0;
    }

    @Override
    public void addBalance(int amt) {
        balance += amt;
    }
}
