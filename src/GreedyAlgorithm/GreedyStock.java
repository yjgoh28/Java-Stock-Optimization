package GreedyAlgorithm;
import java.util.*;
import java.io.*;

public class GreedyStock {
    
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
            
            //Sort the stocks by return per cost ratio
            Collections.sort(stocks, Stock.comparatorRPC);
            
            // Use a HashMap to store the selected stocks and their quantities
            Map<String, Integer> selectedStocks = new HashMap<>();
            
            int totalShares = 0;
            int totalReturn = 0; // Add this line to keep track of total return
            for (Stock stock : stocks) {
                int maxShares = Math.min(maxStockQuantity, budget / stock.getCost());
                if (maxShares > 0) {
                    int costOfShares = stock.getCost() * maxShares;
                    totalShares += maxShares;
                    budget -= costOfShares;
                    totalReturn += stock.getExpectedReturn() * maxShares; // Update total return
                    
                    // Add the selected stock and its quantity to the HashMap
                    selectedStocks.put(stock.getName(), maxShares);
                    
                    // Print the selected stock information
                    System.out.println("Buy " + maxShares + " shares of " + stock.getName()
                            + " (Cost: RM" + costOfShares + ", Returns: RM" + stock.getExpectedReturn() * maxShares + ")");
                }
            }
            
            // Print the maximum profit and remaining budget
            System.out.println("Maximum Profit: RM" + totalReturn);
            
            // Check if the budget is fully utilized
            if (totalShares == maxStockQuantity) {
                System.out.println("Remaining budget: RM0");
            } else {
                System.out.println("Remaining budget: RM" + budget);
            }
            
        } catch (Exception ex) {
            System.err.println(ex);
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
    
    public static Comparator<Stock> comparatorRPC = new Comparator<Stock>() {
        @Override
        public int compare(Stock s1, Stock s2) {
            return Double.compare((double)s2.getExpectedReturn() / s2.getCost(), (double)s1.getExpectedReturn() / s1.getCost());
        }
    };
}
