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
        while (true){
            if(startGame){
                if(player.getChips() == 0){
                    long a = startGame();
                    player.setChips(a);
                }
                tradedCards = 0;
                initializeDeck();
                computer.emptyHand();
                player.emptyHand();
                shuffleAndDeal();
                startGame=false;
            }

            System.out.println("Chip Balance: $" + player.getChips());
            System.out.print("How much would you like to wager? $");
            String input = in.nextLine();
            checkQuit(input);
            wager = checkValidInput(input);
            if(wager > player.getChips() || wager < 0){
                System.out.println("Invalid number of chips");
            }else{
                String msg = "";
                int multiplier = takeTurn();
                if (multiplier == 0) {
                    msg = "You lost. Better luck next time!";
                }else if(multiplier == 1){
                    msg = "Pair!";
                }else if (multiplier == 2){
                    msg = "Two Pair!";
                }else if (multiplier == 3){
                    msg = "Three-of-a-kind!";
                }else if(multiplier == 5){
                    msg = "Straight!";
                }else if(multiplier == 10){
                    msg = "Flush";
                }else if (multiplier == 15){
                    msg = "Full House!";
                }else if(multiplier == 25){
                    msg = "Four-of-a-kind!";
                }else if (multiplier == 50){
                    msg = "Straight Flush!";
                }else if(multiplier == 100) {
                    msg = "Royal Flush!";
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

    public void shuffleAndDeal(){
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
        for(String suit : SUITS){
            for(String rank : RANKS){
                deck.add(new Card(rank, suit));
            }
        }
    }
    private int takeTurn(){
        while(tradedCards < 3){
            int tradesLeft = 3 - tradedCards;
            player.showHand();
            System.out.println("Pick which card to trade in. Press \"P\" to pass. You have " + tradesLeft + " trades left");
            String cardIndex = in.nextLine().toUpperCase();
            if(cardIndex.equals("PASS") || cardIndex.equals("P")){
                return player.checkHand();
            }else{
                try{
                    int card = Integer.parseInt(cardIndex);
                    if(card > 5){
                        System.out.println("Not a valid number");
                    }else{
                        player.removeCard(card - 1);
                        player.takeCard( card - 1, deck.remove(0));
                        tradedCards++;
                    }
                }catch (NumberFormatException e){
                    System.out.println("Not a valid number");
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
        while (true) {
            Scanner init = new Scanner(System.in);
            System.out.print("Enter the number of chips you would like to buy: ");
            String input = init.nextLine();
            checkQuit(input);
            a = checkValidInput(input);

            if (a > 0) {
                return a;
            }
        }

    }
    private static void checkQuit (String s){
        if (s.toUpperCase().equals("QUIT") || s.toUpperCase().equals("Q")) {
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