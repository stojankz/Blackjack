import java.util.Arrays;


public class Deck {
	
	private int[] cards;
	private int index;
	
	public Deck(){
		cards = new int[52];
		createDeck(cards);
		index = 0;
	}
	
	//Creates the deck by assigning values
		//to each card in the deck
		private void createDeck(int[] deck){
			int value = 1;
			for(int i = 0; i< deck.length; i++){
				if(i != 0 && i%4==0 && value < 10){
					value++;
					deck[i] = value;
				}else{
					deck[i] = value;
				}
			}
		}
		
	//TODO: figure out how to deal a card from 
	//the deck.
	public int dealCard(){
		if(index < 52){
			int card = cards[index];
			index++;
			return card;
		}
		return 0;
	}
	
	//returns the value of a card at a
	//specific index but does not 
	//take the card out of the deck.
	public int getCardValue(int i){
			return cards[i];
	}
	
	//Go through each position in the array 
	//and swap the card in that position with a 
	//another random card in the deck.
	public void shuffle(){
		for(int i = 0; i< cards.length; i++){
			int original = cards[i];
			int swapIndex = (int)(Math.random()*52);
			cards[i] = cards[swapIndex];
			cards[swapIndex] = original;
		}
		index = 0;
	}
	
	//returns a string of all the 
	//cards in the deck.
	public String toString(){
		return Arrays.toString(cards);
	}
}
