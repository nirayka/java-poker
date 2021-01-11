import java.util.*;
public class Poker {

    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private final Player player;
    private final Player computer;
    private List<Card> deck;
    private final Scanner in;
    private int tradedCards = 0;
    public Poker(long n) {
        this.player = new Player(n);
        this.computer = new Player(Integer.MAX_VALUE);
        this.in = new Scanner(System.in);
    }
    public void play() {
        long wager;
        boolean startGame = true;
        while (true) {
            if (startGame) {
                if (player.getChips() == 0) {
                    long a = startGame();
                    player.setChips(a);
                }
                tradedCards = 0;
                initializeDeck();
                computer.emptyHand();
                player.emptyHand();
                shuffleAndDeal();
                startGame = false;
            }

            System.out.print("You have " + player.getChips() + " chips. How many chips would you like to wager? ");
            String input = in.nextLine();
            checkQuit(input);
            wager = checkValidInput(input);
            if (wager > 25){
                System.out.println("You can't wager more than 25 chips.");
            } else if (wager > player.getChips()) {
                System.out.println("You can't wager more chips than you currently have.");
            } else if (wager < 0) {
                System.out.println("You need to wager at least one chip.");
            } else {
                String msg = "";
                int multiplier = takeTurn();
                if (multiplier == 0) {
                    msg = "\nSorry, you lost. Better luck next time!";
                }else if (multiplier == 1){
                    msg = "\nNice job, you've won with a Pair!";
                }else if (multiplier == 2){
                    msg = "\nNice job, you've won with a Two Pair!";
                }else if (multiplier == 3){
                    msg = "\nNice job, you've won with a Three-of-a-kind!";
                }else if (multiplier == 5){
                    msg = "\nNice job, you've won with a Straight!";
                }else if (multiplier == 10){
                    msg = "\nNice job, you've won with a Flush";
                }else if (multiplier == 15){
                    msg = "\nNice job, you've won with a Full House!";
                }else if(multiplier == 25){
                    msg = "\nNice job, you've won with a Four-of-a-kind!";
                }else if (multiplier == 50){
                    msg = "\nNice job, you've won with a Straight Flush!";
                }else if(multiplier == 100) {
                    msg = "\nNice job, you've won with a Royal Flush!";
                }
                System.out.println(msg);
                if(multiplier > 0 ){
                    player.setChips(player.getChips() + wager * multiplier);
                }else {
                    player.setChips(player.getChips() - wager);
                }
                startGame = true;
            }
        }
    }

    public void shuffleAndDeal() {
        if(deck == null){
            initializeDeck();
        }
        Collections.shuffle(deck);
        while(player.getHand().size() < 5){
            player.takeCard(deck.remove(0));
            computer.takeCard(deck.remove(0));
        }
    }
    private void initializeDeck(){
        deck = new ArrayList<>(52);
        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));
            }
        }
    }
    private int takeTurn(){
        while (tradedCards < 3) {
            int tradesLeft = 3 - tradedCards;
            player.showHand();
            System.out.println("You have " + tradesLeft + " trades remaining. \nPick a card to trade in by entering the number next to it. If you don't want to trade anything in, enter \"F\" to finish your turn.");
            String cardIndex = in.nextLine().toUpperCase();
            if (cardIndex.equals("F")) {
                return player.checkHand();
            } else {
                try {
                    int card = Integer.parseInt(cardIndex);
                    if (card > 5){
                        System.out.println("Sorry, that's not a valid card choice.");
                    } else {
                        player.removeCard(card - 1);
                        player.takeCard( card - 1, deck.remove(0));
                        tradedCards++;
                    }
                } catch (NumberFormatException e){
                    System.out.println("Sorry, that's not a valid card choice.");
                }
            }
        }
        player.showHand();
        return player.checkHand();
    }
    public static void main(String[] args) {
        System.out.println("Let's play VIDEO POKER: the single-player rendition of classic five-card draw poker!");

        long chips = startGame();
        Poker game = new Poker(chips);
        game.play();
    }
    private static long startGame(){
        long a;
        while(true){
            Scanner init = new Scanner(System.in);
            System.out.print("How many chips would you like to buy? ");
            String input = init.nextLine();
            checkQuit(input);
            a = checkValidInput(input);

            if(a > 0){
                return a;
            }
        }

    }
    private static void checkQuit (String s){
        if (s.toUpperCase().equals("QUIT")) {
            System.exit(0);
        }
    }
    private static long checkValidInput (String input) {
        long a;
        try {
            a = Long.parseLong(input);
            if (a > 0) {
                return a;
            } else {
                return -1;
            }

        } catch (NumberFormatException e){
            return -1;
        }
    }
}