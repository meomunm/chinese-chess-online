package techkids.vn.chinesechessonline.controllers;

import android.util.Log;

/*
* Class Role có nhiệm vụ  tạo ra 2 vùng range_start và range_end
* sau mỗi lần di chuyển thì sẽ change qua lại giữa 2 vùng
*           RED -> Black, Black -> Red....
*  quân cờ (ChessModel) nếu thuộc vùng nào thì được phép di chuyển khi đến turn của vùng đó
* */
public class Role {
    public Roler role;

    public Role(int range_start, int range_end) {
        role = new Reder(range_start, range_end);
    }

    public void changeRole() {
        role = role.changeRole();
    }

    public boolean canMove(int valueOfChess) {
        return role.canMove(valueOfChess);
    }
}

abstract class Roler {
    int range_start, range_end;

    public Roler(int range_start, int range_end) {
        this.range_start = range_start;
        this.range_end = range_end;
    }

    abstract public Roler changeRole();

    abstract public boolean canMove(int range);
}


class Reder extends Roler {
    private static final String TAG = Reder.class.toString();
    int range_start, range_end;

    public Reder(int range_start, int range_end) {
        super(range_start, range_end);
        this.range_start = range_start;
        this.range_end = range_end;
    }

    @Override
    public Roler changeRole() {
        return new Blacker(range_start, range_end);
    }

    @Override
    public boolean canMove(int range) {
        if (range > range_start && range < range_end){
            Log.d(TAG, String.format("canMove: red true  range = %s - s = %s - e = %s : ", range, range_start, range_end));
            return true;
        }
        Log.d(TAG, String.format("canMove: red false  range = %s - s = %s - e = %s : ",  range, range_start, range_end));
        return false;
    }
}

class Blacker extends Roler {
    private static final String TAG = Blacker.class.toString();

    int range_start, range_end;

    public Blacker(int range_start, int range_end) {
        super(range_start, range_end);
        this.range_start = range_start;
        this.range_end = range_end;
    }

    @Override
    public Roler changeRole() {
        return new Reder(range_start, range_end);
    }

    @Override
    public boolean canMove(int range) {
        if (range > range_start && range < range_end){
            Log.d(TAG, String.format("canMove: black true range = %s - s = %s - e = %s : ",  range, range_start, range_end));
            return false;
        }
        Log.d(TAG, String.format("canMove: black false  range = %s - s = %s - e = %s : ",  range, range_start, range_end));
        return true;
    }
}
