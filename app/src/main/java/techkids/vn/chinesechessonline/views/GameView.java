package techkids.vn.chinesechessonline.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import techkids.vn.chinesechessonline.R;
import techkids.vn.chinesechessonline.controllers.GameController;
import techkids.vn.chinesechessonline.controllers.GameSound;
import techkids.vn.chinesechessonline.controllers.Role;
import techkids.vn.chinesechessonline.models.ChessModel;
import techkids.vn.chinesechessonline.utils.DrawUtils;

public class GameView extends View {
    private final String TAG = GameView.class.toString();

    Context myContext;
    GameController gameController;
    ChessModel chessModel;

    private Resources res;
    private int chessEdge;

    Bitmap chessBoard;
    Bitmap chooseTarget;
    Bitmap choosedTarget;

    private Bitmap[] chessImage = new Bitmap[14];

    private boolean canDraw = false;
    private Paint paint;

    private int START_POINT_X;
    private int START_POINT_Y;
    private boolean firstchoice = true;

    //vị trí theo trục tọa độ x y
    private int first_y = -1;
    private int first_x = -1;

    //vị trí theo chỉ số mảng [i][j] -> tương ứng với vị trí chessModel -> initPosition
    int selectedPosition_i = 0;
    int selectedPosition_j = 0;

    int di;
    int dj;

    Rect chessRect;
    Role role; //Quản lý việc di chuyển theo lượt trong game, quân cờ thuộc vùng nào - có được di chuyển hay ko sẽ do Role quyết định

    private Matrix matrix;

    public GameView(Context context, GameController gameController) {
        super(context);

        chessModel = new ChessModel();
        this.gameController = gameController;
        this.myContext = context;

        this.paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.CENTER);

        this.res = context.getResources();
        int screenWidth = gameController.getScreenWidth();
        int screenHeight = gameController.getScreenHeight();

        //chessedge tỷ lệ của 1 ô cờ
        this.chessEdge = screenWidth / 9;

        paint.setTextSize(chessEdge / 2);

        //start_point_xy là thứ tự hiển thị dãy các quân cờ trong bàn cờ (khởi đầu từ vị trí x = 0; y =  1/2 chiều cao - 1/2 canvas cờ  - 1/2 quân cờ)
        START_POINT_X = 0;
        START_POINT_Y = (screenHeight / 2) - (screenWidth / 2) - (chessEdge / 2);

        //rect của bàn cờ
        chessRect = new Rect(0
                , (screenHeight / 2 - 5 * chessEdge)
                , (9 * chessEdge)
                , (screenHeight / 2 + 5 * chessEdge));

        //...something
        chessBoard = new DrawUtils(screenWidth, screenHeight).drawChessBoard();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        this.setBackgroundResource(R.drawable.yangpi);
        canvas.drawBitmap(chessBoard, 0, 0, paint);

        if (canDraw) {
            int temp;
            for (int i = 0; i < 10; i++) {      //           y   |
                for (int j = 0; j < 9; j++) {   //           x   -
                    temp = chessModel.getInitPosition()[i][j];
                    if (temp != 0) {
                        canvas.drawBitmap(chessImage[temp - 1], j * chessEdge + START_POINT_X, i * chessEdge + START_POINT_Y, paint);
                    }
                }
            }

            if (first_y != -1) {
                canvas.drawBitmap(choosedTarget, first_x * chessEdge + START_POINT_X, first_y * chessEdge + START_POINT_Y, paint);
            }
        }
    }

    public void initChess() {
        chessModel.setInitPosition(chessModel.getStartPosition());
        postInvalidate();   //redraw the View
    }

    public void setFirstGo(boolean first) {
        renderChess(first);
    }

    private void renderChess(boolean isRed) {
        int size = BitmapFactory.decodeResource(res, R.drawable.bbishop).getWidth();

        matrix = new Matrix();
        float scale = ((float) chessEdge / (float) size); //tính toán scale sao cho tỷ lệ thu phóng sẽ vừa 1 ô chessEdge, lấy chessEdge/size ảnh = scale
        matrix.reset();                                   //(x, y) = (x * scale, y* scale)
        matrix.postScale(scale, scale);         //tỷ lệ ma trận (thu phóng) M' = M * S(sx, sy) = (xSx, ySy)

        chooseTarget = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.choice), 0, 0, size, size, matrix, true);
        choosedTarget = Bitmap.createBitmap(BitmapFactory.decodeResource(res, R.drawable.choice2), 0, 0, size, size, matrix, true);

        //tạo ảnh bitmap dựa trên drawable và gộp 2 mảng chessRed và chessBlack vào làm 1
        int[] chessRes = isRed ? chessModel.getRedChessRes() : chessModel.getBlackChessRes();
        for (int i = 0; i < chessRes.length; i++) {
            chessImage[i + 7] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, chessRes[i]), 0, 0, size, size, matrix, true);
        }

        //tạo ảnh bitmap của bên cờ đỏ xong tạo tiếp bên cờ đen
        chessRes = !isRed ? chessModel.getRedChessRes() : chessModel.getBlackChessRes();
        matrix.postRotate(180);
        for (int i = 0; i < chessRes.length; i++) {
            chessImage[i] = Bitmap.createBitmap(BitmapFactory.decodeResource(res, chessRes[i]), 0, 0, size, size, matrix, true);
        }

        if (isRed) {
            //Tao ra khu vực di chuyển trong game (những quân cờ thuộc khu vực này sẽ được di chuyển)
            role = new Role(7, 15);
            Log.d(TAG, "renderChess: create Red role");
        } else {
            //Red luôn được quyền đi trước
            role = new Role(0, 8);
            Log.d(TAG, "renderChess: create Black role");
        }

        canDraw = true;
        postInvalidate();
    }


    //xử lý sự kiện chạm vào quân cờ
    //handle event when touch chessman
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //what is class Rect???
            if (chessRect.contains((int) event.getX(), (int) event.getY())) {

                //tim x va y, vi tri nguoi dung click
                int x = ((int) event.getX() - START_POINT_X) / chessEdge;
                int y = ((int) event.getY() - START_POINT_Y) / chessEdge;

                clickAndMove(x, y);
                postInvalidate();
            }
        }
        return true;
    }

    private void clickAndMove(int x, int y) {
        /*  NOTE:
            để di chuyển 1 quân cờ thì cần 2 lần chạm
            lần 1: chọn quân cờ
            lần 2: di chuyển
            firstchoice là 1 flag để phân biệt lần 1 và lần 2
        */
        selectedPosition_i = y;
        selectedPosition_j = x;

        if (firstchoice) {
            //nếu người dùng touch vào ô trắng thì ko làm gì
            if (chessModel.getInitPosition()[y][x] == 0) {
                return;
            }
            firstchoice = false;
            first_x = x;     //j
            first_y = y;     //i

            Log.d(TAG, String.format("clickAndMove: if đầu nè!!!\ni = %s; j = %s\nx = %s; y = %s", selectedPosition_i, selectedPosition_j, first_x, first_y));
        }

        //khi nguời dùng click lại vào chính ô đó mà ko di chuyển
        else if (first_y == y && first_x == x) {
            firstchoice = true;
            Log.d(TAG, "clickAndMove: đoán xem!!!!");
        }

        //người dùng click di chuyển bất kì
        else {
            if (canMove(selectedPosition_i, selectedPosition_j)&& whoseTurn()) {
                //vị trí mới sẽ bằng giá trị của vị trí cũ
                //và vị trí cũ thì bằng 0 tức là ô trống trong bàn cờ
                gameController.playSoundGo();
                chessModel.setInItPositionItem(selectedPosition_i, selectedPosition_j, chessModel.getInitPosition()[first_y][first_x]);
                chessModel.setInItPositionItem(first_y, first_x, 0);
            }
            firstchoice = true;
        }
    }

    private boolean whoseTurn() {
        Log.d(TAG, "whoseTurn: " + chessModel.getInitPosition()[first_y][first_x]);

        if (role.canMove(chessModel.getInitPosition()[first_y][first_x])){
            role.changeRole();
            return true;
        }else {
            Toast.makeText(myContext, "Chờ đối phương di chuyển đã!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean canMove(int index_i, int index_j) {
        //lấy vị trí quân cờ đang được select
        int choice = chessModel.getInitPosition()[first_y][first_x];
        Log.d(TAG, String.format("canMove: choice = %s, x = %s, y =%s", choice, index_i, index_j));

//             what is this??
//        if (isTheSameSide(index_i, index_j)) {
//            return false;
//        }

        switch (choice) {
            case ChessModel.B_KING:
            case ChessModel.R_KING:
                return kingMove(index_i, index_j);

            case ChessModel.B_BISHOP:
            case ChessModel.R_BISHOP:
                return bishopMove(index_i, index_j);

            case ChessModel.B_ELEPHANT:
            case ChessModel.R_ELEPHANT:
                return elephantMove(index_i, index_j);

            case ChessModel.B_HORSE:
            case ChessModel.R_HORSE:
                return horseMove(index_i, index_j);

            case ChessModel.B_CAR:
            case ChessModel.R_CAR:
                return carMove(index_i, index_j);

            case ChessModel.B_CANON:
            case ChessModel.R_CANON:
                return canonMove(index_i, index_j);

            case ChessModel.B_PAWN:
            case ChessModel.R_PAWN:
                return pawnMove(index_i, index_j);

            default:
                Log.d(TAG, "canMove: default");
                return false;
        }
    }

    private boolean pawnMove(int index_i, int index_j) {
        //lọc xem quân tốt thuộc về black hay của red
        //tốt chỉ được phép di chuyển 1 ô về phía trước
        //và không được đi lùi hay sang trái, sang phải
        //tốt sang sông được di chuyển trái phải 1 ô
        if (chessModel.getInitPosition()[first_y][first_x] == ChessModel.B_PAWN) {
            if (index_i < 5) {   //chưa sang sông
                if (index_i == first_y + 1 && first_x == index_j) {
                    return true;
                }
            } else {              //sang sông
                if (index_i == first_y - 1 && first_x == index_j) {
                    return false;   //ngăn cản đi lùi của tốt trước khi thực hiện di chuyển isMoveNear()
                } else {
                    return isMoveNear(index_i, index_j);
                }
            }
        } else if (chessModel.getInitPosition()[first_y][first_x] == ChessModel.R_PAWN) {
            if (index_i > 4) {   //chưa sang sông
                if (index_i == first_y - 1 && first_x == index_j) {
                    return true;
                }
            } else {              //sang sông
                if (index_i == first_y + 1 && first_x == index_j) {
                    return false;
                } else {
                    return isMoveNear(index_i, index_j);
                }
            }
        }
        return false;
    }

    //áp dụng cho các ô có khả năng di chuyển cạnh bên như tướng và tốt
    private boolean isMoveNear(int index_i, int index_j) {
        //di chuyển lên xuống
        if (index_j == first_x && (index_i == first_y + 1 || index_i == first_y - 1)) {
            return true;
        } else  //di chuyển trái phải một đơn vị
            if (index_i == first_y && (index_j == first_x + 1 || index_j == first_x - 1)) {
            return true;
        }
        return false;
    }

    private boolean canonMove(int index_i, int index_j) {
        Log.d(TAG, "canonMove: ");
        return true;
    }

    private boolean carMove(int index_i, int index_j) {
        Log.d(TAG, "carMove: ");
        return true;
    }

    private boolean horseMove(int index_i, int index_j) {
        Log.d(TAG, "horseMove: ");
        return true;
    }

    private boolean elephantMove(int index_i, int index_j) {
        Log.d(TAG, "elephantMove: ");
        return true;
    }

    private boolean bishopMove(int index_i, int index_j) {
        Log.d(TAG, "bishopMove: ");
        return true;
    }


    //kiểm tra xem di chuyển có hợp lệ không?
    private boolean kingMove(int index_i, int index_j) {
        //tướng chỉ được phép di chuyển trong vùng ô vuông X
        if (chessModel.getInitPosition()[first_y][first_x] != 0){
            if(chessModel.getInitPosition()[first_y][first_x] == ChessModel.B_KING){
                //không được di chuyển
                if (index_i > 2 ||  index_i < 0 || index_j < 3 || index_j > 5){
                    return false;
                }else if(isMoveNear(index_i, index_j)){ //được di chuyển
                    //why have for in here?
//                    for (){
//
//                    }
                }
            }
        }

        return true;
    }

    private boolean isTheSameSide(int i, int j) {
        return false;
    }
}