import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class ConnectFourMain {
    public static final String ANSI_RESET = "";//"\u001B[0m";
    public static final String ANSI_BLACK = "";
    //"\u001B[30m";
    public static final String ANSI_RED = "";//"\u001B[31m";
    public static final String ANSI_GREEN = "";//"\u001B[32m";
    public static final String ANSI_YELLOW = "";//"\u001B[33m";
    public static final String ANSI_BLUE = "";//"\u001B[34m";
    public static final String ANSI_PURPLE = "";//"\u001B[35m";
    public static final String ANSI_CYAN = "";//"";
    //"\u001B[36m";
    public static final String ANSI_WHITE = "";//"\u001B[37m";
    public static void main(String args[]){
        /*FileWriter splits;
        try{
            File split = new File("Splits_1000.txt");
            splits = new FileWriter(split);
        }catch (Exception e){
            System.out.println("Error");
            splits = null;
        }*/
        byte gameState[][] = new byte[7][6];//0:nobody, 1:user, 2:computer
        Scanner kb = new Scanner(System.in);
        int draws = 0;
        int randomWins = 0;
        int smartWins = 0;
        Random rd = new Random();
        long lastTime = System.currentTimeMillis();
        int hundredIndex = 0;
        int resigns = 0;
        byte lastHundred[] = new byte[10000]; 
        for (byte i = 0; i < 100; i++) lastHundred[i] = 0;    
        byte playsMade;
        double record = 0;
        ArrayList<ArrayList<ArrayList<ArrayList<ConnectFourGameBoardData>>>> allStates = new ArrayList<ArrayList<ArrayList<ArrayList<ConnectFourGameBoardData>>>>();
        for (int uh = 0; uh < 2187; uh++){
            allStates.add(new ArrayList<ArrayList<ArrayList<ConnectFourGameBoardData>>>());
            for (int i = 0; i < Integer.parseInt("22212", 3) + 1; i++){
                allStates.get(uh).add(new ArrayList<ArrayList<ConnectFourGameBoardData>>());
                for (int oh = 0; oh < 21; oh++){
                    allStates.get(uh).get(i).add(new ArrayList<ConnectFourGameBoardData>());
                }
            }
        }
        allStates.get(0).get(0).get(0).add(new ConnectFourGameBoardData(new byte[7][6], (byte)3));
        int lastResigns = 0;
		double lastPct = 0;
		double lastLund = 0;
		double lrecord = 0;
        long cpuTime = 0;
        long stupidTime = 0;
        long searchTime = 0;
		long startTime = System.currentTimeMillis();
		long skipStart = 0;
		long skipTime = 0;
		long skips = 0;
		long lastSkips = 0;
        int lastAmt = 0;
        int totalSolved = 0;
        int lastSolved = 0;
        long totalMoves = 0;
        int totalPlays = 0;
        int leftBoardId = 0;
        int rightBoardId = 0;
        int mirrorLeftBoardId = 0;
        int mirrorRightBoardId = 0;
        System.out.println("Begin");
        do{
            for (int i = 0; i < 7; i++)
                for (int j = 0; j < 6; j++)
                    gameState[i][j] = (byte)0;
            ArrayList<ConnectFourPointer> usedStates = new ArrayList<ConnectFourPointer>();
            playsMade = 0;
            long cpuStart;
            long stupidStart;
            long searchStart;
            do{
                /*for (int i = 5; i >= 0; i--){
                    for (int j = 0; j < 7; j++){
                        System.out.print(gameState[j][i] + " ");
                    }
                    System.out.println();
                }*/
                /*
                System.out.println("Your turn to enter a row (1-7)");
                int playerMove = kb.nextInt() - 1;*/

                //ConnectFourHelper.SysoutBoard(gameState);
                //System.out.println();
                cpuStart = System.currentTimeMillis();
                skipStart = System.currentTimeMillis();
                int col = ConnectFourHelper.findWinningColumn(gameState, (byte)2);
                if (col != -1){
                    gameState[col][ConnectFourHelper.firstGoodRow(col, gameState)] = (byte)2;
					usedStates.add(null);
					skipTime += System.currentTimeMillis() - skipStart;
                    break;
                }
                boolean ezMove = false;
                col = ConnectFourHelper.findWinningColumn(gameState, (byte)1);
                if (col != -1){
                    gameState[col][ConnectFourHelper.firstGoodRow(col, gameState)] = (byte)2;
                    usedStates.add(null);
                    ezMove = true;
				}
				if (!ezMove && playsMade >= 18 && ConnectFourHelper.oneLegalMove(gameState)){
					for (int i = 0; i < 7; i++){
						if (ConnectFourHelper.firstGoodRow(i, gameState) != -1){
							gameState[i][ConnectFourHelper.firstGoodRow(i, gameState)] = (byte)2;
							usedStates.add(null);
                   		    ezMove = true;
						}
					}
				}
				if (!ezMove && playsMade > 3) {
					col = ConnectFourHelper.reasonableMove(gameState, (byte)2);
					if (col != -1){
						gameState[col][ConnectFourHelper.firstGoodRow(col, gameState)] = (byte)2;
						usedStates.add(null);
						ezMove = true;
					}
				}
				skipTime += System.currentTimeMillis() - skipStart;
                if (!ezMove){
					searchStart = System.currentTimeMillis();
                    leftBoardId = 0;
					rightBoardId = 0;
					mirrorLeftBoardId = 0;
                    mirrorRightBoardId = 0;
                    for (int i = 0; i < 7; i++){
                        leftBoardId += Math.pow(3, i) * gameState[i][0];
						mirrorLeftBoardId += Math.pow(3, i) * gameState[6 - i][0];
                    }
                    for (int i = 0; i < 5; i++){
                        mirrorRightBoardId += Math.pow(3, i) * gameState[5 - i][1];
                        rightBoardId += Math.pow(3, i) * gameState[i + 1][1];
                    }
                    boolean foundBoardData = false;
                    boolean foundAndFlip = false;
                    int allStatesSize = allStates.get(leftBoardId).get(rightBoardId).get(playsMade).size();
                    for (int index = 0; index < allStatesSize; index++){
						ConnectFourGameBoardData boards = allStates.get(leftBoardId).get(rightBoardId).get(playsMade).get(index);
                        byte[][] theirBoard = boards.getState();
                        boolean go = true;
                        boolean goat = true;
                        for (int j = 1; j < 6 && go; j++){
                            for (int i = 0; i < 7 && go; i++){
                                if (go && theirBoard[i][j] != gameState[i][j]) go = false;
                                //if (theirBoard[i][j] == 0) break;
                            }
                        }
                        if (go){
                            usedStates.add(new ConnectFourPointer(index, leftBoardId, rightBoardId));
                            foundBoardData = true;
                            break;
                        }
					}
					if (!foundBoardData){
						allStatesSize = allStates.get(mirrorLeftBoardId).get(mirrorRightBoardId).get(playsMade).size();
						for (int index = 0; index < allStatesSize; index++){
							ConnectFourGameBoardData boards = allStates.get(mirrorLeftBoardId).get(mirrorRightBoardId).get(playsMade).get(index);
							byte[][] theirBoard = boards.getState();
							boolean go = true;
							for (int i = 0; i < 7 && go; i++){
								for (int j = 1; j < 6 && go; j++){
									if (go && theirBoard[6 - i][j] != gameState[i][j]) go = false;
									if (go && theirBoard[i][j] == 0) break;
								}
							}
							if (go){
								leftBoardId = mirrorLeftBoardId;
								rightBoardId = mirrorRightBoardId;
								usedStates.add(new ConnectFourPointer(index, leftBoardId, rightBoardId));
								foundAndFlip = true;
								break;
							}
						}
						if (!foundAndFlip){
							ConnectFourGameBoardData data = new ConnectFourGameBoardData(gameState);
							allStates.get(leftBoardId).get(rightBoardId).get(playsMade).add(data);
							usedStates.add(new ConnectFourPointer(allStates.get(leftBoardId).get(rightBoardId).get(playsMade).size() - 1, leftBoardId, rightBoardId));
						}
					}
					if (!(ConnectFourHelper.gameOver(gameState) == 0)) 
					//&& (usedStates.get(playsMade) != null && allStates.get(leftBoardId).get(rightBoardId).get(playsMade).get(usedStates.get(playsMade).getMyIndex()).resign())))
						break;
					searchTime += System.currentTimeMillis() - searchStart;
                    cpuStart = System.currentTimeMillis();
                    if (foundAndFlip){
                        byte[][] flipStates = allStates.get(leftBoardId).get(rightBoardId).get(playsMade).get(usedStates.get(playsMade).getMyIndex()).makeAMove(rd);
                        byte[][] holder = new byte[7][6];
                        for (int i = 0; i < 7; i++){
                            for (int j = 0; j < 6; j++){
                                gameState[i][j] = flipStates[6 - i][j];
                            }
                        }
                    }
                    else
                    gameState = allStates.get(leftBoardId).get(rightBoardId).get(playsMade).get(usedStates.get(playsMade).getMyIndex()).makeAMove(rd);
                    if (gameState == null){
                        System.out.println("Null");
                        break;
                    }
                    
				}else skips++;
				if (ConnectFourHelper.gameOver(gameState) != 0 || (usedStates.get(playsMade) != null && allStates.get(leftBoardId).get(rightBoardId).get(playsMade).get(usedStates.get(playsMade).getMyIndex()).resign())) break;
                cpuTime += System.currentTimeMillis() - cpuStart;
                stupidStart = System.currentTimeMillis();
                int playerMove;
                if (randomWins + smartWins + draws != 0 
                && ((randomWins + smartWins + draws) / 9999995) >= 1
                ){                    
					ConnectFourHelper.SysoutBoard(gameState);
					if (usedStates.get(usedStates.size() - 1) == null)
					System.out.println();
					else
                    System.out.println(allStates.get(leftBoardId).get(rightBoardId).get(playsMade).get(usedStates.get(playsMade).getMyIndex()).getTimesUsed() + " times " + allStates.get(leftBoardId).get(rightBoardId).get(playsMade).get(usedStates.get(playsMade).getMyIndex()).solved());
                    playerMove = 
                    kb.nextInt();
                }else{
                    playerMove = 
                    //kb.nextInt();
                    ConnectFourHelper.getCompletelyRandomGo(gameState, rd);
                }
                for (int i = 0; i < 6; i++){
                    if (gameState[playerMove][i] == 0){
                        gameState[playerMove][i] = 1;
                        break;
                    }
                }
                stupidTime += System.currentTimeMillis() - stupidStart;
                playsMade++;

            }while (ConnectFourHelper.gameOver(gameState) == 0 || (usedStates.get(usedStates.size() - 1) != null && allStates.get(leftBoardId).get(rightBoardId).get(usedStates.size() - 1).get(usedStates.get(usedStates.size() - 1).getMyIndex()).resign()));
            boolean resignation = usedStates.get((usedStates.size() - 1)) != null && allStates.get(usedStates.get(usedStates.size() - 1)
                .getID()).get(usedStates.get(usedStates.size() - 1)
                .getRID()).get(usedStates.size() - 1)
                .get(usedStates
                .get(usedStates.size() - 1)
                .getMyIndex())
				.resign();
				//randomWins + smartWins + draws == 0 || 
			if (randomWins + smartWins + draws != 0 && ((randomWins + smartWins + draws) / 9999995) >= 1){
				String oot = ANSI_RED + "E" + ANSI_YELLOW + "RR" + ANSI_GREEN + "O" + ANSI_BLUE + "R" + ANSI_RESET;
				if (ConnectFourHelper.gameOver(gameState) == 1) oot = ANSI_GREEN + "PLAYER WON" + ANSI_RESET;
				if (ConnectFourHelper.gameOver(gameState) == 2) oot = ANSI_RED + "CPU WON" + ANSI_RESET;
				if (ConnectFourHelper.gameOver(gameState) == 3) oot = ANSI_YELLOW + "GAME DRAWN" + ANSI_RESET;
				if (resignation) oot = "Computer resigned";
				System.out.println(oot);
			}
            lastHundred[hundredIndex] = resignation ? 0 :
            (byte)ConnectFourHelper.gameOver(gameState);
            hundredIndex++;
            for (int i = 0; i < usedStates.size(); i++){
                if (usedStates.get(i) == null) continue;
                allStates.get(usedStates.get(i).getID()).get(usedStates.get(i).getRID()).get(i).get(usedStates.get(i).getMyIndex()).updateOdds((i >= (usedStates.size() - 2)), 
                    ConnectFourHelper.gameOver(gameState), Math.pow(1.2, (2.0 * i / 3.0)) - 0.999D);
            }
            if (resignation) resigns++;
            if (hundredIndex == 10000) {
                try{
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                }catch (Exception e){
                    System.out.println("CLS failed");
                }
                System.out.println("Time spent on each function (ms)");
                System.out.println("Stupid: " + stupidTime);
                System.out.println("Smart: " + cpuTime);
				System.out.println("Search: " + searchTime);
				System.out.println("Skip: " + skipTime);

                //System.out.println("Resignations: " + String.format("%.5f", ((resigns - lastResigns) * 100.0D / ((randomWins + smartWins + draws) + 1))));
				/*System.out.println("Total seaches saved: " + skips);
                System.out.println("New seaches saved: " + (skips - lastSkips));*/
                lastResigns = resigns;
				lastSkips = skips;
                int size = 0;
                /*for (int i = 0; i < 42; i++){
                    size += allStates.get(i).size();
                }*/
                if ((((randomWins + smartWins + draws) + 1) % 100000) == 0){
                    long sortStart = System.currentTimeMillis();
                    totalSolved = 0;
                    for (int uh = 0; uh < 2187; uh++){
                        for (int oh = 0; oh  < Integer.parseInt("22212", 3) + 1; oh++){
                            for (int i = 0; i < 21; i++){
                                //Collections.sort(allStates.get(uh).get(oh).get(i));
                                size += allStates.get(uh).get(oh).get(i).size();
                                for (ConnectFourGameBoardData boread : allStates.get(uh).get(oh).get(i)){
                                    if (boread.solved()){
                                        totalSolved++;
                                    }
                                    totalMoves += boread.getTimesUsed();
                                    //totalPlays += boread.getTimesUsed();
                                }
                            }
                        }
                    }
                    System.out.println("Sort: " + (System.currentTimeMillis() - sortStart));
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                    System.out.println("Sorted: " + size);
                    System.out.println("New Sorts: " + (size - lastAmt));
                    System.out.println("Solves: " + totalSolved);
                    System.out.println("New Solves: " + (totalSolved - lastSolved));
					System.out.println("Average Moves: " + String.format("%.5f", 2 * ((double)totalMoves / (draws + smartWins + randomWins))));/**/
					lastSolved = totalSolved;
                    lastAmt = size;
                }else System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                System.out.println("Total Games: " + (randomWins + smartWins + draws));
                totalMoves = 0;
                totalPlays = 0;
				cpuTime = 0;
				skipTime = 0;
                stupidTime = 0;
                searchTime = 0;
                hundredIndex = 0;
                /*for (int i = 5; i >= 0; i--){
                    for (int j = 0; j < 7; j++){
                        if (gameState[j][i] == 0)
                        System.out.print(ANSI_BLACK + gameState[j][i] + " " + ANSI_RESET);
                        if (gameState[j][i] == 1)
                        System.out.print(ANSI_YELLOW + gameState[j][i] + " " + ANSI_RESET);
                        if (gameState[j][i] == 2)
                        System.out.print(ANSI_RED + gameState[j][i] + " " + ANSI_RESET);
                    }
                    System.out.println();
                }*/
                /*String out = "";
                switch (ConnectFourHelper.gameOver(gameState)){
                    case 1:
                        out = "Dumb Luck";
                        break;
                    case 2:
                        out = "Smart Skill";
                        break;
                    case 3: 
                        out = "Draw";
                        break;
                }*/
                System.out.println("W%  |chng W%  |rec   t");
				double hundPct = 0;
				double lundPct = 0;
                for (byte bit : lastHundred){
					hundPct += bit == 2 ? (byte)1 : (byte)0;
					lundPct += bit != 1 && bit != 0 ? (byte)1 : (byte)0;
                }
				hundPct /= 10000.0D;
				lundPct /= 10000.0D;
				//System.out.println(tp);
                if (hundPct > record){
					System.out.println(ANSI_GREEN + String.format("%.3f", hundPct) + " " + String.format("%.3f", hundPct - lastPct) + ANSI_RESET + "   " + (String.format("%.3f", record)) + " " + (System.currentTimeMillis() - lastTime) + "ms");
					record = hundPct;
                }
                else 
                System.out.println(ANSI_RED + String.format("%.3f", hundPct) + " " + (lastPct < hundPct ? ANSI_GREEN : lastPct == hundPct ? ANSI_YELLOW : "") + String.format("%.3f", hundPct - lastPct) + ANSI_RESET + "   " + (String.format("%.3f", record)) + " " + (System.currentTimeMillis() - lastTime) + "ms");
                System.out.println("W+D%|chng W+D%|rec   t");
                if (lundPct > lrecord){
					System.out.println(ANSI_GREEN + String.format("%.3f", lundPct) + " " + String.format("%.3f", lundPct - lastLund) + ANSI_RESET + "   " + (String.format("%.3f", lrecord)) + " " + (System.currentTimeMillis() - lastTime) + "ms");
					lrecord = lundPct;
                }
                else 
                System.out.println(ANSI_RED + String.format("%.3f", lundPct) + " " + (lastLund < lundPct ? ANSI_GREEN : lastLund == lundPct ? ANSI_YELLOW : "") + String.format("%.3f", lundPct - lastLund) + ANSI_RESET + "     " + (String.format("%.3f", lrecord)) + " " + (System.currentTimeMillis() - lastTime) + "ms");
				lastLund = lundPct;
				lastPct = hundPct;
                //splits.write(hundPct + " " + (randomWins + smartWins + draws) + " " + (System.currentTimeMillis() - lastTime) + "ms");
                //splits.write('\n');
                lastTime = System.currentTimeMillis();
                if (randomWins + smartWins + draws >= 100000000) break;
            }
            if (ConnectFourHelper.gameOver(gameState) == 1 || ConnectFourHelper.gameOver(gameState) == 0) randomWins++;
            if (ConnectFourHelper.gameOver(gameState) == 2) smartWins++;
            if (ConnectFourHelper.gameOver(gameState) == 3) draws++;
        }while (true);
        System.out.println("************************************************************");
        System.out.println("Done in: " + (System.currentTimeMillis() - startTime) + "ms with " + lastAmt + " positions");
        System.out.println("Stupid Luck: " + randomWins);
        System.out.println("Smart skill: " + smartWins);
        System.out.println("Indifferent: " + draws);
        System.out.println("--------------------");
        System.out.println("Total Games: " + (randomWins + smartWins + draws));
        System.out.println("Smart w pct: " + ((double)smartWins / (randomWins + smartWins + draws)));
        //splits.close();
    }
}