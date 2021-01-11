import java.util.*;
public class Player {
    private ArrayList<Card> hand;
    private long chips;
    public Player (long chips) {
        this.hand = new ArrayList<>();
        this.chips = chips;
    }

    public ArrayList <Card> getHand(){
        return hand;
    }
    public void takeCard(int index, Card card){
        hand.add(index, card);
    }
    public void takeCard(Card card){
        hand.add(card);
    }
    public void showHand(){
        String message = "\nYour hand: ";
        System.out.print(message);
        String a = "";
        for(int i = 0; i < getHand().size(); i ++){
            int index = i + 1;
            a += "["+ index +"]" + getHand().get(i) + "     ";
        }
        System.out.println(a+"\n");
    }
    public long getChips(){
        return chips;
    }
    public int checkHand(){
        int dupes = checkDupes();
        int seq = checkSequence();
        int flush = checkFlush();
        int royalflush = 0;
        int straightflush = 0;

        if(seq > 0 && flush > 0){
            straightflush = 50;
        }
        if(straightflush > 0){
            royalflush = checkRoyalFlush();
        }
        int round1 = Math.max(straightflush, flush);
        int round2 = Math.max(dupes, seq);
        int round3 = Math.max(round1, round2);
        int multiplier = Math.max(round3, royalflush);
        return multiplier;
    }
    public void setChips(long chips) {
        this.chips = chips;
    }
    public void emptyHand() {
        this.hand = new ArrayList<Card>();
    }
    public void removeCard(int cardIndex){
        hand.remove(cardIndex);
    }
    private int checkDupes(){
        int pair = 0;
        boolean trips = false;
        boolean quads = false;
        ArrayList<Integer> ranks = new ArrayList<>();
        HashMap<Integer, Integer> rankDupes = new HashMap<>();
        for(Card i : getHand()){
            ranks.add(i.getOrderedRank(i.getRank(), true));
        }
        for(int i = 0; i < ranks.size(); i ++){
            int seqLength = 1;
            int current = ranks.get(i);
            for(int j = i + 1; j < ranks.size(); j++){
                if(current == ranks.get(j)){
                    seqLength++;
                }
            }
            if(seqLength > 1 && !rankDupes.containsKey(current)){
                rankDupes.put(current, seqLength);
            }
        }

        for(int i : rankDupes.keySet()){
            if(rankDupes.get(i) == 2){
                pair++;
            }else if(rankDupes.get(i) == 3){
                trips = true;
            }else if(rankDupes.get(i) == 4){
                quads = true;
            }
        }
        if(quads){
            return 25;
        }else if(trips){
            if(pair > 0){
                return 15;
            }
            return 3;
        }else if(pair == 1){
            if(pairIsGreaterThanJack(rankDupes)){
                return 1;
            }
        }else if(pair == 2){
            return 2;
        }
        return 0;
    }
    private int checkSequence(){
        ArrayList<Integer> ranks = new ArrayList<>();
        int indexKing = -1;
        for(Card i : getHand()){
            if(i.getRank().equals("K")){
                indexKing = 1;
            }
        }

        boolean aceHigh = false;
        if(indexKing >= 0){
            aceHigh = true;
        }
        for(Card i : getHand()){
            ranks.add(i.getOrderedRank(i.getRank(), aceHigh));
        }
        Collections.sort(ranks);
        for(int i = 0; i < ranks.size() - 1; i++){
            if(ranks.get(i + 1) - 1 != ranks.get(i)){
                return 0;
            }
        }
        return 5;
    }
    private int checkFlush(){
        ArrayList<String> suits = new ArrayList<>();
        for(Card i : getHand()){
            suits.add(i.getSuit());
        }
        for(int i = 0; i < suits.size() - 1; i++){
            if(!suits.get(i+1).equals(suits.get(i))){
                return 0;
            }
        }
        return 10;
    }
    private int checkRoyalFlush(){
        int indexKing = -1;
        int indexAce = -1;
        for(Card i : getHand()){
            if(i.getRank().equals("K")){
                indexKing = 1;
            }else if (i.getRank().equals("A")){
                indexAce = 1;
            }
        }
        if(indexKing >= 0 && indexAce >= 1){
            return 100;
        }
        return 0;
    }
    private boolean pairIsGreaterThanJack(HashMap<Integer, Integer> vals){
        for(int i = 11; i < 15; i ++){
            if(vals.containsKey(i)){
                return true;
            }
        }
        return false;
    }

}