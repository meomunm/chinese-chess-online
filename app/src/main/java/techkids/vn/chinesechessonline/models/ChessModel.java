package techkids.vn.chinesechessonline.models;

import techkids.vn.chinesechessonline.R;

public class ChessModel {
    private final int NOCHESS = 0;

    private final int B_KING = 1;
    private final int B_CAR = 2;
    private final int B_HORSE = 3;
    private final int B_CANON = 4;
    private final int B_BISHOP = 5;
    private final int B_ELEPHANT = 6;
    private final int B_PAWN = 7;

    private final int R_KING = 8;
    private final int R_CAR = 9;
    private final int R_HORSE = 10;
    private final int R_CANON = 11;
    private final int R_BISHOP = 12;
    private final int R_ELEPHANT = 13;
    private final int R_PAWN = 14;

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

    public int getNOCHESS() {
        return NOCHESS;
    }

    public int getB_KING() {
        return B_KING;
    }

    public int getB_CAR() {
        return B_CAR;
    }

    public int getB_HORSE() {
        return B_HORSE;
    }

    public int getB_CANON() {
        return B_CANON;
    }

    public int getB_BISHOP() {
        return B_BISHOP;
    }

    public int getB_ELEPHANT() {
        return B_ELEPHANT;
    }

    public int getB_PAWN() {
        return B_PAWN;
    }

    public int getR_KING() {
        return R_KING;
    }

    public int getR_CAR() {
        return R_CAR;
    }

    public int getR_HORSE() {
        return R_HORSE;
    }

    public int getR_CANON() {
        return R_CANON;
    }

    public int getR_BISHOP() {
        return R_BISHOP;
    }

    public int getR_ELEPHANT() {
        return R_ELEPHANT;
    }

    public int getR_PAWN() {
        return R_PAWN;
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
}
