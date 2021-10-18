package com.company.cardGame.actor;

import com.company.cardGame.blackJack.Actor;
import com.company.cardGame.blackJack.Hand;
import com.company.cardGame.deck.Card;

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
        return balance / 20;
    }

    @Override
    public byte getAction(Hand hand, Hand dealer){
        // Victory Royale (⌐▨◡▨)
//        if(dealer.getValue() == 21){
//            dealer.addCard(new Card(-69, " ໒( 0◡0)っ✂╰⋃╯"));
//            dealer.addCard(new Card(117, " (ノಠ益ಠ)ノ彡┻━┻"));
//            return STAND;
//        }
//        if(dealer.getValue() == 20){
//            dealer.addCard(new Card(2, " ಠ_ಠ"));
//            return STAND;
//        }

        int score;
        do{
            score = hand.getValue();

            if(hand.size() > 2 || hands > 1){
                System.out.println(dealer.getName() + " " + dealer.displayHand());
                System.out.println(name);
            }
            System.out.println(hand.displayHand());
            System.out.println(score);

            if(hand.canSplit() && ++hands <= 4)
                return SPLIT;
            if((dealer.getValue() >= 17 && dealer.getValue() < score) || score >= 17)
                return STAND;

            hand.addCard(new Card((int)(Math.random() * (21 - score) + 1),
                    SUITS[(int)(Math.random() * SUITS.length)]));
            System.out.println("Hit");
        }while(true);
    }

    @Override
    public void addBalance(double amt) {
        balance += amt;
    }
}
