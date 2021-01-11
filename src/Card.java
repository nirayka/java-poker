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
                case "T": return 10;    // for 10s and face cards,
                case "J": return 11;    // a number is associated
                case "Q": return 12;    // with each rank to make it easier
                case "K": return 13;    // to sort the cards.
                case "A": if (high){return 14;}else{return 1;}
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
        return this.getOrderedRank(this.getRank(), true)-compareCard;
    }
}