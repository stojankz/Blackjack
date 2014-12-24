//Klementina Stojanovska
//11/16/14

//import java.util.ArrayList;
//import java.util.List;
import java.util.Random;


public class Blackjack {
	
	private static char[] s = new char[90];
	private static double[] probability = new double[90];
	public static final int REPEAT = 100000;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Deck d = new Deck();
		Random rand = new Random();
	
		//In order to make the code more efficient I thought
		//I would try multithreading. The results were not
		//accurate though and my best guess as to why is that
		//because the threads run concurrently, when 
		//referencing back to previous larger numbers in the 
		//table, it was giving inaccurate result because the
		//threads may not have been run yet.
		
//		ThreadGroup group = new ThreadGroup("main");
//		int availableCpu = Runtime.getRuntime().availableProcessors();
//		int i;
//		
//		List<Multithread> simulations = new ArrayList<Multithread>();
//		
//		int playerInitialVal = 20;
//		for(i = 0; i<90; i++){
//			simulations.add(new Multithread(d,i,playerInitialVal,s, probability,"thread"+i,group));
//		}
//		
//		i = 0;
//		int priority = 10;
//		while(i< simulations.size()){
//			if(group.activeCount() < availableCpu){
//				Multithread thread = simulations.get(i);
//				thread.setPriority(priority);
//				thread.run();
//				priority--;
//				if(priority < 1) priority = 10;
//				i++;
//			} else{
//				try{Thread.sleep(100);}
//				catch(InterruptedException e){ e.printStackTrace();}
//			}
//		}
//		
//		while(group.activeCount() > 0){
//			try{Thread.sleep(100);}
//			catch (InterruptedException e){ e.printStackTrace();}
//		}
		
		//This loop runs the hit strategy and stand strategy simulations
		//for each option in the table then assigns values to
		//the arrays.
		int playerInitialVal = 20;
		for(int j = 0; j< 90; j++){
				int sWins = standStrategyWins(rand,d,(j%10)+1, playerInitialVal);
				int hWins = hitStrategyWins(rand,d,(j%10)+1,playerInitialVal);
				if(hWins > sWins){
					s[j] = 'H';
					probability[j] = (double)hWins/REPEAT;
				}else{
					s[j] = 'S';
					probability[j] = (double)sWins/REPEAT;
				}
			if(j%10 == 0) playerInitialVal--;
		}
		
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Total execution time: " + totalTime + " milliseconds.");
		drawStrategyTable();
		System.out.println("");
		drawProbabilityTable();

	}
	
	
	//This method runs a constant number, REPEAT, amount of simulations in
	//which the player Hits. It keeps track of the amount of times
	//that the player wins by hitting.
	public static int hitStrategyWins(Random rand, Deck gameDeck, int dealerUpCard, int testValue){
		int wins = 0;
		
		for(int i = 0; i< REPEAT; i++){
			boolean nextStrategy = true;
			//because the call to shuffle is so expensive,
			//I realized that the deck is not fully used for each
			//simulation and decided that I should instead
			//limit the amount of times shuffle is called.
			if(i%10 == 0){
			gameDeck.shuffle();
			}
			
			int initialCard = gameDeck.getCardValue(rand.nextInt(52));
			int dealerTotal = dealerUpCard + initialCard;
			int playerTotal = testValue;
			
			//checks whether the player should keep
			//hitting.
			while(nextStrategy == true){
				playerTotal += gameDeck.dealCard();
				if(playerTotal > 21 || playerTotal == 21){
					break;
				}
				if(s[playerTotal%20 + (dealerUpCard-1)] == 'S'){
					nextStrategy = false;
				}
			}
			
			//Deals cards to the dealer
			//while his value is less than 17
			while(dealerTotal < 17){
				dealerTotal += gameDeck.dealCard();
			}
			
			//checks whether the player beat the dealer
			if(playerTotal < 22 && playerTotal > dealerTotal || 
					playerTotal < 22 && dealerTotal > 21){
				wins++;
			}	
		}
		return wins;
	}
	
	//This method runs a constant number, REPEAT, amount of simulations in
	//which the player always stands. It keeps track of the number of times
	//the player wins while using the stand strategy.
	public static int standStrategyWins(Random rand, Deck gameDeck, int dealerUpCard, int testValue) {
		int wins = 0;
		
		for (int i = 0; i < REPEAT; i++) {
			//Again I limited the amount of times
			//shuffle is called in order to improve
			//the runtime of the program
			if(i%10 == 0){
			gameDeck.shuffle();
			}
			
			int card = gameDeck.getCardValue(rand.nextInt(52));
			int dealerTotal = dealerUpCard + card;
			int playerTotal = testValue;
			
			//Deals cards to the dealer while
			//the dealers value is less than 17
			while (dealerTotal < 17) {
				dealerTotal += gameDeck.dealCard();
			}
			
			//Checks whether the player beat the dealer.
			//If so, increases the number of wins by 1.
			if( dealerTotal>21 || playerTotal > dealerTotal){
				wins++;
			}
		}
		return wins;
	}
	
	//Prints the Hit/Stand strategy table
	public static void drawStrategyTable(){
		System.out.printf("%2s%4s%4s%4s%4s%4s%4s%4s%4s%4s%5s"," ","A", "2", "3", "4", "5", "6", "7", "8", "9"," 10\n");
		System.out.println("-------------------------------------------");
		System.out.printf("%2s%4s%4s%4s%4s%4s%4s%4s%4s%4s%5s", "20", s[0],s[1],s[2],s[3],s[4],s[5],s[6],s[7],s[8],s[9]+"\n");
		System.out.printf("%2s%4s%4s%4s%4s%4s%4s%4s%4s%4s%5s", "19", s[10],s[11],s[12],s[13],s[14],s[15],s[16],s[17],s[18],s[19]+"\n");
		System.out.printf("%2s%4s%4s%4s%4s%4s%4s%4s%4s%4s%5s", "18", s[20],s[21],s[22],s[23],s[24],s[25],s[26],s[27],s[28],s[29]+"\n");
		System.out.printf("%2s%4s%4s%4s%4s%4s%4s%4s%4s%4s%5s", "17", s[30],s[31],s[32],s[33],s[34],s[35],s[36],s[37],s[38],s[39]+"\n");
		System.out.printf("%2s%4s%4s%4s%4s%4s%4s%4s%4s%4s%5s", "16", s[40],s[41],s[42],s[43],s[44],s[45],s[46],s[47],s[48],s[49]+"\n");
		System.out.printf("%2s%4s%4s%4s%4s%4s%4s%4s%4s%4s%5s", "15", s[50],s[51],s[52],s[53],s[54],s[55],s[56],s[57],s[58],s[59]+"\n");
		System.out.printf("%2s%4s%4s%4s%4s%4s%4s%4s%4s%4s%5s", "14", s[60],s[61],s[62],s[63],s[64],s[65],s[66],s[67],s[68],s[69]+"\n");
		System.out.printf("%2s%4s%4s%4s%4s%4s%4s%4s%4s%4s%5s", "13", s[70],s[71],s[72],s[73],s[74],s[75],s[76],s[77],s[78],s[79]+"\n");
		System.out.printf("%2s%4s%4s%4s%4s%4s%4s%4s%4s%4s%5s", "12", s[80],s[81],s[82],s[83],s[84],s[85],s[86],s[87],s[88],s[89]+"\n");
	}
	
	//Prints the probability table for winning using
	//the corresponding strategy from the hit/stand
	//table.
	public static void drawProbabilityTable(){
		System.out.printf("%4s%4s%8s%8s%8s%8s%8s%8s%8s%8s%8s"," ","A", "2", "3", "4", "5", "6", "7", "8", "9"," 10\n");
		System.out.println("----------------------------------------------------------------------------------");
		System.out.printf("%-4s%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-2s", "20", probability[0], probability[1], probability[2],probability[3],
				probability[4],probability[5],probability[6],probability[7],probability[8],probability[9]," " + "\n");
		System.out.printf("%-4s%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-2s", "19", probability[10],probability[11],probability[12],probability[13],
				probability[14],probability[15],probability[16],probability[17],probability[18],probability[19]," " + "\n");
		System.out.printf("%-4s%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-2s", "18", probability[20],probability[21],probability[22],probability[23],
				probability[24],probability[25],probability[26],probability[27],probability[28],probability[29]," " + "\n");
		System.out.printf("%-4s%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-2s", "17", probability[30],probability[31],probability[32],probability[33],
				probability[34],probability[35],probability[36],probability[37],probability[38],probability[39]," " + "\n");
		System.out.printf("%-4s%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-2s", "16", probability[40],probability[41],probability[42],probability[43],
				probability[44],probability[45],probability[46],probability[47],probability[48],probability[49], " " + "\n");
		System.out.printf("%-4s%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-2s", "15", probability[50],probability[51],probability[52],probability[53],
				probability[54],probability[55],probability[56],probability[57],probability[58],probability[59], " " + "\n");
		System.out.printf("%-4s%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-2s", "14", probability[60],probability[61],probability[62],probability[63],
				probability[64],probability[65],probability[66],probability[67],probability[68],probability[69], " " + "\n");
		System.out.printf("%-4s%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-2s", "13", probability[70],probability[71],probability[72],probability[73],
				probability[74],probability[75],probability[76],probability[77],probability[78],probability[79], " " + "\n");
		System.out.printf("%-4s%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-8.3f%-2s", "12", probability[80],probability[81],probability[82],probability[83],
				probability[84],probability[85],probability[86],probability[87],probability[88],probability[89], " " + "\n");
	}
}
