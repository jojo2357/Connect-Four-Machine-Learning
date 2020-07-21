import java.util.Random;

public class ConnectFourGameBoardData 
//implements Comparable<ConnectFourGameBoardData>
{
    
    private boolean gameState[][] = new boolean[7][0];// [1, 42) bits
    private float odds[] = new float[7];// 28 Bytes
    private byte lastChoice;// 1 Byte
    private boolean solved = false; // 1 bit
    private byte solvedMove;// 1 byte
    private int timesUsed = 0;// 4 bytes

    //40 Bytes

    public ConnectFourGameBoardData(final byte[][] board) {
        for (int i = 0; i < 7; i++){
            gameState[i] = ConnectFourHelper.compressBoard(board[i]);
        }
        //this.gameState = board;
        for (int i = 0; i < 7; i++)
            odds[i] = 3.0F;
    }

    public ConnectFourGameBoardData(final byte[][] board, final byte solve) {
        this.gameState = new boolean[7][0];
        this.solved = true;
        this.solvedMove = solve;
        for (int i = 0; i < 7; i++)
            odds[i] = i == solve ? 2.0F : 0;
    }

    public byte[][] getState() {
        byte[][] out = new byte[7][6];
        for (int i = 0; i < 7; i++)
            out[i] = ConnectFourHelper.expandBoard(gameState[i]);
        return out;
    }

    public byte[][] makeAMove(final Random rand) {
        float oddsTotal = updateTotalOdds();
        final byte move = 0;
        final byte[][] gameBoard = new byte[7][6];
        for (int i = 0; i < 7; i++) {
            gameBoard[i] = ConnectFourHelper.expandBoard(gameState[i]);
        }
        if (solved) {
            for (int j = 0; j < 6; j++) {
                if (gameBoard[solvedMove][j] == 0) {
                    gameBoard[solvedMove][j] = 2;
                    return gameBoard;
                }
            }
        }
        for (int i = 0; i < 7 && !solved; i++) {
            boolean tried = false;
            for (int j = 0; j < 6 && !solved; j++) {
                if (gameBoard[i][j] == 0) {
                    tried = true;
                    /*
                     * if (ConnectFourHelper.winningMove(gameBoard, (byte)2)){ int win =
                     * ConnectFourHelper.findWinningColumn(gameBoard, (byte)2); for (int k = 0; k <
                     * 7; k++) odds[k] = k == win ? 1 : 0; solved = true; solvedMove = (byte)win;
                     * break; }
                     */
                    gameBoard[i][j] = (byte) 2;
                    if (ConnectFourHelper.winningMove(gameBoard, (byte) 1)) {
                        odds[i] = 0;
                    }
                    if (1.05 * oddsTotal > odds[i] && 0.95 * oddsTotal < odds[i]) {
                        solved = true;
                        solvedMove = (byte) i;
                        odds = null;
                    }
                    oddsTotal = updateTotalOdds();
                    gameBoard[i][j] = 0;
                    break;
                }
            }
            if (!tried) {
                odds[i] = 0;
                if (1.05 * oddsTotal > odds[i] && 0.95 * oddsTotal < odds[i]) {
                    solved = true;
                    solvedMove = (byte) i;
                    odds = null;
                }
            }
        }
        if (solved) {
            for (int j = 0; j < 6; j++) {
                if (gameBoard[solvedMove][j] == 0) {
                    gameBoard[solvedMove][j] = 2;
                    return gameBoard;
                }
            }
        }
        /*
         * int in = ConnectFourHelper.findWinningColumn(gameBoard, (byte)2); if (in !=
         * -1){ /*for (int i = 0; i < 7; i++){ if
         * (ConnectFourHelper.findWinningColumn(gameBoard, (byte)1) != i) odds[i] = 0; }
         * gameBoard[in][ConnectFourHelper.firstGoodRow(in, gameBoard)] = 2; return
         * gameBoard; } in = ConnectFourHelper.findWinningColumn(gameBoard, (byte)1); if
         * (in != -1){ /*for (int i = 0; i < 7; i++){ if
         * (ConnectFourHelper.findWinningColumn(gameBoard, (byte)1) != i) odds[i] = 0; }
         * gameBoard[in][ConnectFourHelper.firstGoodRow(in, gameBoard)] = 2; return
         * gameBoard; }
         */
        do {
            this.updateTotalOdds();
            final double num = rand.nextDouble() * oddsTotal;
            double oddSum = 0;
            for (byte i = 0; i < 7; i++) {
                if (num <= oddSum + odds[i]) {
                    for (int j = 0; j < 6; j++) {
                        if (gameBoard[i][j] == 0) {
                            gameBoard[i][j] = (byte) 2;
                            // if (!ConnectFourHelper.winningMove(gameBoard, (byte)1)){
                            this.lastChoice = i;
                            return gameBoard;
                            // }
                            /*
                             * if (oddsTotal == odds[i]){ this.lastChoice = i; return gameBoard; } foundBad
                             * = true; gameBoard[i][j] = 0; odds[i] = 0;
                             */
                        }
                    }
                } else {
                    oddSum += odds[i];
                }
            }
        } while (!this.resign());
        //ConnectFourHelper.SysoutBoard(gameBoard);
        return gameBoard;
    }

    private float updateTotalOdds() {
        if (this.odds == null) return 0.1F;
        float out = 0;
        for (int i = 0; i < 7; i++) {
            if (odds[i] < 0)
                odds[i] = 0;
            if (this.gameState[i].length == 6 && !this.gameState[i][5]) {
                odds[i] = 0;
            } else
                out += odds[i];
        }
        return out;
    }

    public void updateOdds(final boolean finalMove, final int gameOutcome, final double downstreamDist) {// 1: lost, 3:
                                                                                                         // drawn, 2:
                                                                                                         // won
        timesUsed++;
        if (solved) return;
        if (finalMove) {
            if (gameOutcome == 1 || gameOutcome == 0) {
                if (odds == null)
                ;
                this.odds[this.lastChoice] = 0;
            }
            if (gameOutcome == 2) {
                for (int i = 0; i < 7; i++)
                    if (this.lastChoice == i)
                        odds[i] = 1;
                    else
                        odds[i] = 0;
            }
        } else {
            switch (gameOutcome) {
                case 0:
                case 1:
                    odds[this.lastChoice] -= 0.35F * downstreamDist;
                    return;
                case 3:
                    odds[this.lastChoice] -= 0.15F * downstreamDist;
                    return;
                case 2:
                    odds[this.lastChoice] += 0.1F * downstreamDist;
                    return;
            }
        }
    }

    public boolean resign() {
        return this.updateTotalOdds() == 0;
    }

    public long getTimesUsed() {
        return this.timesUsed;
    }

    public boolean solved() {
        return this.solved;
    }

    /*@Override
    public int compareTo(final ConnectFourGameBoardData o) {
        return Long.compare(this.getTimesUsed(), o.getTimesUsed());
    }*/
}