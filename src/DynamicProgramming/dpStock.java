package DynamicProgramming;

import java.util.*;
import java.lang.*;
import java.io.*;

public class dpStock {
	
	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		
		System.out.print("Enter your BUDGET: ");
		int budget = input.nextInt();
		
		int maxStockQuantity = 100;
		File file = new File("src/stocks.txt");
		ArrayList<Stock> stocks = new ArrayList<>();
		
		try {
			Scanner in = new Scanner(file);
			
			String line;
			while(in.hasNext()) {
				line = in.nextLine();
				String[] content = line.split(",");
				if(content.length == 4) {
					String name = content[0];
					int cost = Integer.parseInt(content[1]);
					int expectedReturn  = Integer.parseInt(content[2]);
					int maxQuantity  = Integer.parseInt(content[3]);
					stocks.add(new Stock(name,cost,expectedReturn,maxQuantity));
				}
			}
			in.close();
			
			//Sort the stocks by expected return
			Collections.sort(stocks, Stock.comparatorER);
			
			int[] weights = new int[stocks.size()];
			int[] values = new int[stocks.size()];
			
			for (int i = 0; i < stocks.size(); i++) {
				weights[i] = stocks.get(i).getCost();
	            values[i] = stocks.get(i).getExpectedReturn();
	            }
			
			
			
			int[][] dp = knapsack(budget, weights, values, stocks.size(), maxStockQuantity);
			int maxProfit = dp[stocks.size()][budget];
			System.out.println("Maximum Profit: " + maxProfit);
			
			
			System.out.println("Suggested Stocks to buy: ");
			printSelectedStocks(budget, weights, values, stocks, dp, maxStockQuantity);
			
			
		}catch (Exception ex) {
			System.err.println(ex);
		}
	}

	static int[][] knapsack(int budget, int cost[], int expectedReturn[], int n, int maxStockQuantity) {
			
			//2D dimensions array, n = number of stocks
			int dp[][] = new int[n + 1][budget + 1];
			
			for(int i = 0; i <= n; i++) {
				for(int j = 0; j <= budget; j++) {
					if(i == 0 || j == 0) {
						dp[i][j] = 0;
					}
					else {
						//[i][j] is the maximum expected return achievable with i stocks and j budget
						dp[i][j] = dp[i - 1][j];
						int maxQuantity = Math.min(maxStockQuantity, j / cost[i - 1]);
						for(int k = 1; k <= maxQuantity; k++) {
							dp[i][j] = Math.max(dp[i][j], expectedReturn[i - 1] * k + dp[i - 1][j - cost[i - 1] * k]);
						}
					}
				}
			}
			
			return dp;
		}

	static void printSelectedStocks(int budget, int cost[], int expectedReturn[], ArrayList<Stock> stocks, int[][] dp, int maxStockQuantity) {
	    int n = cost.length;
	    int i = n, j = budget;
	    int totalShares = 0;

	    while (i > 0 && j > 0) {
	        if (dp[i][j] != dp[i - 1][j]) {
	            int maxQuantity = Math.min(maxStockQuantity, j / cost[i - 1]);

	            // Find the optimal quantity to buy for this stock
	            int optimalQuantity = 0;
	            for (int k = 1; k <= maxQuantity; k++) {
	                if (dp[i][j] == expectedReturn[i - 1] * k + dp[i - 1][j - cost[i - 1] * k]) {
	                    optimalQuantity = k;
	                }
	            }

	            if (optimalQuantity > 0) {
	                int sharesToBuy = optimalQuantity;
	                int costOfShares = cost[i - 1] * sharesToBuy;
	                totalShares += sharesToBuy;
	                j -= costOfShares;
	                i--;

	                System.out.println("Buy " + sharesToBuy + " shares of " + stocks.get(i).getName()
	                        + " (Cost: RM" + costOfShares + ", Returns: RM" + expectedReturn[i] * sharesToBuy + ")");
	            }
	        } else {
	            i--;
	        }
	    }

	    // Check if the budget is fully utilized
	    if (totalShares == maxStockQuantity) {
	        System.out.println("Budget fully utilized. No funds remaining.");
	    } else {
	        System.out.println("Remaining budget: RM" + j);
	    }
	}


}

 class Stock{

	private String name;
	private int cost;
	private int expectedReturn;
	private int maxQuantity;
	
	public Stock(String name, int cost, int expectedReturn, int maxQuantity) {
		this.name = name;
		this.cost = cost;
		this.expectedReturn = expectedReturn;
		this.maxQuantity = maxQuantity;
	}

	public int getCost() {
		return cost;
	}
	
	public int getExpectedReturn() {
		return expectedReturn;
	}
	
	public String getName() {
		return name;
	}
	
	public static Comparator<Stock> comparatorER = new Comparator<Stock>() {
		@Override
		public int compare(Stock s1, Stock s2) {
			return s2.getExpectedReturn() - s1.getExpectedReturn();
		}
	};
}
