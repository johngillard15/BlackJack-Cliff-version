package com.company.cardGame.blackJack;

import com.company.Utils.Console;
import com.company.cardGame.actor.Dealer;
import com.company.cardGame.actor.John;
import com.company.cardGame.actor.Player;
import com.company.cardGame.deck.Deck;
import com.company.cardGame.deck.RiggedDeck;
import com.company.cardGame.deck.StandardDeck;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private final List<Hand> hands = new ArrayList<>();
    // TODO: more comfortable -> try to accomplish without the players list.
    private final List<Actor> players = new ArrayList<>();
    private final Hand dealer = new Hand(new Dealer());
    private Deck deck;
    private static final int BUST_VALUE = 21;

    public Table(){
        Actor john = new John();
        players.add(john);
        hands.add(new Hand(john));
    }

    private void gatherPlayers(){
        do{
            System.out.println("\nAny new players? (y/n)");

            String choice;
            do{
                choice = Console.getString("new players? ", true);
            }while (!(choice.equals("y") || choice.equals("n")));

            if(choice.equals("y")){
                players.add(new Player(Console.getString("Enter your name: ", true), 1_000));
                hands.add(new Hand(players.get(players.size() - 1)));
            }
            else
                break;
        }while(true);
    }

    private void grabBets(){
        Actor lastPlayer = null;
        for(int i = 0; i < hands.size(); i++){
            Hand hand = hands.get(i);

            if(hand.getHolder() == lastPlayer) {
                hands.remove(hand);
                --i;
                continue;
            }

            System.out.println("\n" + hand.getName());
            System.out.printf("$%,d\n", hand.getBalance());

            String choice;
            if(hand.getBalance() >= 1){
                do{
                    choice = Console.getString("(b)et or leave?", true);
                }while (!(choice.equals("b") || choice.equals("l")));
            }
            else{
                System.out.printf("%s is off to remortgage their house.\n", hand.getName());
                Console.pause();

                choice = "l";
            }

            switch (choice) {
                case "b" -> hand.placeBet();
                case "l" -> {
                    System.out.printf("%s has left the table with $%,d.\n", hand.getName(), hand.getBalance());
                    players.remove(hand.getHolder());
                    hands.remove(hand);
                    --i;
                }
            }

            lastPlayer = hand.getHolder();
        }
    }

    public void setup(){
        deck = new StandardDeck();
        deck.shuffle();

        for(Hand hand : hands)
            hand.clear();
        dealer.clear();

        gatherPlayers();
        grabBets();
    }

    public void play(){
        do{
            setup();

            if(players.size() > 0)
                playRound();
            else
                break;
        }while(true);
    }

    private void initialDeal() {
        for (int count = 0; count < 2; count++) {
            // list of hands
            dealer.addCard(deck.draw());
            for(Hand hand : hands)
                hand.addCard(deck.draw());
        }
    }

    private void displayTable() {
        StringBuilder output = new StringBuilder();
        output.append(dealer.getName()).append(" -> ").append(dealer.displayHand()).append("\n");
        output.append("value: ").append(dealer.getValue()).append("\n");
        for(Hand hand : hands) {
            output.append(hand.getName()).append(" -> ").append(hand.displayHand()).append("\n");
            output.append("value: ").append(hand.getValue()).append("\n");
        }
        System.out.println("\nTable:\n" + output);
    }

    private void playRound(){
        initialDeal();
        displayTable();

        for(int i = 0; i < hands.size(); i++){
            Hand hand = hands.get(i);

            while(turn(hand));

            System.out.println(hand.displayHand());
            System.out.println("value: " + hand.getValue());
            Console.pause();
        }
        while(turn(dealer));

        System.out.println("\n\n- Round Results -");
        displayTable();
        for(Hand hand : hands){
            determineWinner(hand);
            System.out.printf("Balance: $%,d\n", hand.getBalance());
        }

        Console.pause();
    }

    private boolean turn(Hand activeHand){
        System.out.printf("\n%s -> %s\n", dealer.getName(), dealer.displayHand());
        System.out.println("value: " + dealer.getValue());

        System.out.printf("%s's turn\n", activeHand.getName());
        byte action = activeHand.getAction(dealer);
        return switch (action) {
            case Actor.QUIT -> {
                System.out.println("Surrender ;(");
                yield false;
            }
            case Actor.HIT -> hit(activeHand);
            case Actor.STAND -> stand(activeHand);
            case Actor.DOUBLE -> doubleDown(activeHand);
            case Actor.SPLIT -> split(activeHand);
            default -> false;
        };
    }

    private boolean hit(Hand activeHand) {
        activeHand.addCard(deck.draw());
        System.out.println("Hit");
        if (activeHand.getValue() > BUST_VALUE){
            System.out.println("Busted");
            return false;
        }
        return true;
    }

    private boolean stand(Hand activeHand) {
        System.out.println("Stand");
        return false;
    }

    private boolean doubleDown(Hand activeHand) {
        activeHand.doubleBet();
        System.out.println("Bet Doubled");
        hit(activeHand);
        return false;
    }

    private boolean split(Hand activeHand){
        int splitIndex = hands.indexOf(activeHand) + 1;
        hands.add(splitIndex, activeHand.splitHand());

        activeHand.addCard(deck.draw());
        hands.get(splitIndex).addCard(deck.draw());

        return true;
    }

    private void determineWinner(Hand hand) {
        if (hand.getValue() > BUST_VALUE) {
            System.out.printf("%s Busted\n", hand.getName());
            return;
        }
        if (hand.getValue() > dealer.getValue() || dealer.getValue() > BUST_VALUE) {
            System.out.printf("%s Wins\n", hand.getName());
            hand.payout(Hand.NORMALPAY);
            return;
        }
        if (hand.getValue() == dealer.getValue()) {
            System.out.printf("%s Pushed\n", hand.getName());
            hand.payout(Hand.PUSHPAY);
            return;
        }
        System.out.printf("%s Loses\n", hand.getName());
    }
}
