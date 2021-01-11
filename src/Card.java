public class Card implements Comparable {
    private String rank;
    private String suit;
    public Card (String rank, String suit){
        this.rank = rank;
        this.suit = suit;
    }
    public String getRank(){
        return rank;
    }
    public String getSuit(){
        return suit;
    }
    public int getOrderedRank(String rank, boolean high){
        try {
            return Integer.parseInt(rank);
        } catch (NumberFormatException e) {
            switch (rank) {
                case "T": return 10;    // for 10s, Jacks, Queens, Kings,
                case "J": return 11;    // and Aces, we need to apply a
                case "Q": return 12;    // numeric value to simplify the
                case "K": return 13;    // sorting of hands and books.
                case "A": if(high){return 14;}else{return 1;}
            }
        }
        return -1;
    }

    @Override
    public String toString() {
        if(rank == null || suit == null){
            return "Face Down Card";
        }else{
            return rank + suit;

        }
    }
    @Override
    public int compareTo(Object o) {
        Card card = (Card) o;
        int compareCard=card.getOrderedRank(card.getRank(), true);
        /* For Ascending order*/
        return this.getOrderedRank(this.getRank(), true)-compareCard;
    }
}