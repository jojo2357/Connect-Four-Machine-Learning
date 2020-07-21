import java.util.Random;

public class ConnectFourHelper {
    public static final String ANSI_RESET = "";//"\u001B[0m";
    public static final String ANSI_BLACK = "";//"\u001B[30m";
    public static final String ANSI_RED = "";//"\u001B[31m";
    public static final String ANSI_GREEN = "";//"\u001B[32m";
    public static final String ANSI_YELLOW = "";//"\u001B[33m";
    public static final String ANSI_BLUE = "";//"\u001B[34m";
    public static final String ANSI_PURPLE = "";//"\u001B[35m";
    public static final String ANSI_CYAN = "";//"\u001B[36m";
    public static final String ANSI_WHITE = "";//"\u001B[37m";
    public static int gameOver(byte[][] board){
        //assert(board.length == 7 && board.length == 6);
        for (int i = 0;  i < 7; i++){
            //if (board[i][3] != 0 && board[i][2] != 0)
            for (int j = 0; j < 3; j++){
                if (board[i][j] == board[i][j + 1] && board[i][j + 1] == board[i][j + 2] && board[i][j + 2] == board[i][j + 3] && board[i][j + 1] != 0) return board[i][j + 1];
            }
        }
        for (int j = 0; j < 6; j++){
            //if (board[3][j] != 0 && board[j][3] != 0)
            for (int i = 0; i < 4; i++){
                if (board[i][j] == board[i + 1][j] && board[i][j] == board[i + 2][j] && board[i][j] == board[i + 3][j] && board[i + 3][j] != 0) return board[i + 3][j];
            }
        }
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 3; j++){
                if (board[i][j] == board[i + 1][j + 1] && board[i][j] == board[i + 2][j + 2] && board[i][j] == board[i + 3][j + 3] && board[i + 3][j + 3] != 0) return board[i + 3][j + 3];
                if (board[i + 3][j] == board[i + 2][j + 1] && board[i + 3][j] == board[i + 1][j + 2] && board[i + 3][j] == board[i][j + 3] && board[i][j + 3] != 0) return board[i + 3][j];
            }
        }
        boolean draw = true;
        for (int i = 0; i < 7 && draw; i++){
            for (int j = 0; j < 6 && draw; j++){
                if (board[i][j] == 0) draw = false;
            }
        }
        if (draw) return 3;
        return 0;
    }

    public static boolean winningMove(byte[][] board, byte player){
        for (int i = 0; i < 7; i++){
            if (ConnectFourHelper.privateWinningMove(board, player, i)) 
                return true;
        }
        return false;
    }

    private static boolean privateWinningMove(byte[][] board, byte player, int column){
        for (int i = 0; i < 6; i++){
            if (board[column][i] == 0){
                board[column][i] = player;
                boolean bool = ConnectFourHelper.gameOver(board) == player;
                board[column][i] = 0;
                return bool;
            }
        }
        return false;
    }

    public static byte[][] findWinningMove(byte[][] board, byte player){
        int row = 0;
        for (int i = 0; i < 6; i++){
            if (board[ConnectFourHelper.findWinningColumn(board, player)][i] == 0){
                board[ConnectFourHelper.findWinningColumn(board, player)][i] = player;
                return board;
            }
        }
        return null;
    }

    public static int privateFindWinningMove(byte[][] board, byte player, int column){
        for (int i = 0; i < 6; i++){
            if (board[column][i] == 0){
                board[column][i] = player;
                if (i >= 3){
                    if (board[column][i - 3] == player && board[column][i - 1] == board[column][i - 2] && board[column][i - 2] == board[column][i - 3]){
                        board[column][i] = 0;
                        return i;
                    }
                    if (column <= 3) 
                        if (board[column + 3][i - 3] == player && board[column + 1][i - 1] == board[column + 2][i - 2] && board[column + 2][i - 2] == board[column + 3][i - 3]){
                            board[column][i] = 0;
                            return i;
                        }
                    if (column >= 3)
                        if (board[column - 3][i - 3] == player && board[column - 1][i - 1] == board[column - 2][i - 2] && board[column - 2][i - 2] == board[column - 3][i - 3]){
                            board[column][i] = 0;
                            return i;
                        }
                }
                if (i < 3){
                    if (column <= 3) 
                        if (board[column + 3][i + 3] == player && board[column + 1][i + 1] == board[column + 2][i + 2] && board[column + 2][i + 2] == board[column + 3][i + 3]){
                            board[column][i] = 0;
                            return i;
                        }
                    if (column >= 3)
                        if (board[column - 3][i + 3] == player && board[column - 1][i + 1] == board[column - 2][i + 2] && board[column - 2][i + 2] == board[column - 3][i + 3]){
                            board[column][i] = 0;
                            return i;
                        }
                }
                for (int k = 0; k < 4; k++){
                    if (player == board[k][i] && board[k][i] == board[k + 1][i] && board[k + 1][i] == board[k + 2][i] && board[k + 3][i] == board[k + 1][i] && (k + 3 >= column || k <= column)){
                        board[column][i] = 0;
                        return i;
                    }
                }
                if (ConnectFourHelper.gameOver(board) == player) {
                    board[column][i] = 0;
                    return i;
                }
                board[column][i] = 0;
                break;
            }
        }
        return -1;
    }

    public static int findWinningColumn(byte[][] board, byte player){
        for (int i = 0; i < 7; i++){
            if (ConnectFourHelper.privateFindWinningMove(board, player, i) != -1) {
                return i;
            }
        }
        return -1;
    }

    public static int firstGoodRow(int row, byte[][] board){
        for (int i = 0; i < 6; i++){
            if (board[row][i] == 0) return i;
        }
        return -1;
    }

    public static int getCompletelyRandomGo(byte[][] board, Random rd){
        int winning = ConnectFourHelper.findWinningColumn(board, (byte)1);
        if (winning != -1) return winning;
        winning = ConnectFourHelper.findWinningColumn(board, (byte)2);
        if (winning != -1) return winning;
        if (ConnectFourHelper.reasonableMove(board, (byte)1) != -1) return ConnectFourHelper.reasonableMove(board, (byte)1);
        do{
            int rand = ((int)Math.round(rd.nextGaussian() * 1.5) + 3);
            if (rand < 7 && rand >= 0)
                if (board[rand][5] == 0) return rand;
        }while (true);
    }

    public static void SysoutBoard(byte[][] board){
        for (int i = 5; i >= 0; i--){
            for (int j = 0; j < 7; j++){
                if (board[j][i] == 0)
                System.out.print(ANSI_BLACK + board[j][i] + " " + ANSI_RESET);
                if (board[j][i] == 1)
                System.out.print(ANSI_YELLOW + board[j][i] + " " + ANSI_RESET);
                if (board[j][i] == 2)
                System.out.print(ANSI_RED + board[j][i] + " " + ANSI_RESET);
            }
            System.out.println();
        }
    }

    public static boolean oneLegalMove(byte[][] board){
        byte good = 2;
        for (byte i = 0; i < (byte) 7; i++){
            if (ConnectFourHelper.firstGoodRow(i, board) != -1)
                good--;
        }
        return good == 1;
    }

    public static int reasonableMove(byte[][] board, byte player){
        byte found = 2;
        byte row = -1;
        for (int i = 0; i < 7; i++){
            byte fgr = (byte)ConnectFourHelper.firstGoodRow(i, board);
            if (fgr != -1){
                board[i][fgr] = player;
                if (!ConnectFourHelper.winningMove(board, ConnectFourHelper.invertP(player))){
                    found--;
                    row = (byte)i;
                }
                board[i][fgr] = 0;
            }
        }
        if (found != 1){
            return -1;
        }
        return row;
    }

    private static byte invertP(byte P){
        return P == (byte) 2 ? (byte)1 : (byte)2;
    }

    public static boolean[] compressBoard(byte[] row){
        byte far = -1;
        for (byte i = 5; i >= 0; i--){
            if (row[i] == 0){
                far = i;
            }
        }
        if (far == -1)
            far = 6;
        boolean out[] = new boolean[far];
        for (int i = 0; i < far; i++){
            out[i] = row[i] == 1;
        }
        return out;
    }

    public static byte[] expandBoard(boolean[] row){
        byte[] out = new byte[6];
        for (int i = 0; i < 6; i++){
            if (row.length > i)
            out[i] = row[i] ? (byte)1 : (byte)2;
            else 
            out[i] = 0;
        }
        return out;
    }
}