package techkids.vn.chinesechessonline.models;

import techkids.vn.chinesechessonline.R;

public class ChessModel {
    private final int NOCHESS = 0;

    public static final int B_KING = 1;
    public static final int B_CAR = 2;
    public static final int B_HORSE = 3;
    public static final int B_CANON = 4;
    public static final int B_BISHOP = 5;
    public static final int B_ELEPHANT = 6;
    public static final int B_PAWN = 7;

    public static final int R_KING = 8;
    public static final int R_CAR = 9;
    public static final int R_HORSE = 10;
    public static final int R_CANON = 11;
    public static final int R_BISHOP = 12;
    public static final int R_ELEPHANT = 13;
    public static final int R_PAWN = 14;

    private int[][] initPosition = {
            {B_CAR, B_HORSE, B_ELEPHANT, B_BISHOP, B_KING, B_BISHOP, B_ELEPHANT, B_HORSE, B_CAR},
            {NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS},
            {NOCHESS, B_CANON, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, B_CANON, NOCHESS},
            {B_PAWN, NOCHESS, B_PAWN, NOCHESS, B_PAWN, NOCHESS, B_PAWN, NOCHESS, B_PAWN},
            {NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS},

            {NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS},
            {R_PAWN, NOCHESS, R_PAWN, NOCHESS, R_PAWN, NOCHESS, R_PAWN, NOCHESS, R_PAWN},
            {NOCHESS, R_CANON, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, R_CANON, NOCHESS},
            {NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS},
            {R_CAR, R_HORSE, R_ELEPHANT, R_BISHOP, R_KING, R_BISHOP, R_ELEPHANT, R_HORSE, R_CAR}};

    private final int[][] startPosition = {
            {B_CAR, B_HORSE, B_ELEPHANT, B_BISHOP, B_KING, B_BISHOP, B_ELEPHANT, B_HORSE, B_CAR},
            {NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS},
            {NOCHESS, B_CANON, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, B_CANON, NOCHESS},
            {B_PAWN, NOCHESS, B_PAWN, NOCHESS, B_PAWN, NOCHESS, B_PAWN, NOCHESS, B_PAWN},
            {NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS},

            {NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS},
            {R_PAWN, NOCHESS, R_PAWN, NOCHESS, R_PAWN, NOCHESS, R_PAWN, NOCHESS, R_PAWN},
            {NOCHESS, R_CANON, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, R_CANON, NOCHESS},
            {NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS, NOCHESS},
            {R_CAR, R_HORSE, R_ELEPHANT, R_BISHOP, R_KING, R_BISHOP, R_ELEPHANT, R_HORSE, R_CAR}};

    private int[] redChessRes;
    private int[] blackChessRes;

    public ChessModel() {
        redChessRes = new int[]{
                R.drawable.rking,
                R.drawable.rcar,
                R.drawable.rhorse,
                R.drawable.rcanon,
                R.drawable.rbishop,
                R.drawable.relephant,
                R.drawable.rpawn
        };

        blackChessRes = new int[]{
                R.drawable.bking,
                R.drawable.bcar,
                R.drawable.bhorse,
                R.drawable.bcanon,
                R.drawable.bbishop,
                R.drawable.belephant,
                R.drawable.bpawn
        };
    }


    public int[][] getInitPosition() {
        return initPosition;
    }

    public void setInitPosition(int[][] initPosition) {
        this.initPosition = initPosition;
    }

    public int[][] getStartPosition() {
        return startPosition;
    }

    public int[] getRedChessRes() {
        return redChessRes;
    }

    public int[] getBlackChessRes() {
        return blackChessRes;
    }

    public void setInItPositionItem(int i, int j, int value){
        initPosition[i][j] = value;
    }
}
