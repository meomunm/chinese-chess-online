package techkids.vn.chinesechessonline.views;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import techkids.vn.chinesechessonline.R;
import techkids.vn.chinesechessonline.controllers.GameController;
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
//    private int first_i = -1;
//    private int first_j = -1;


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
        this.chessEdge = screenWidth / 9;

        paint.setTextSize(chessEdge / 2);

        START_POINT_X = (screenWidth - 9 * chessEdge) / 2;
//        START_POINT_Y = ((screenHeight - 9 * chessEdge) / 2) - chessEdge / 2;
        START_POINT_Y = (screenHeight / 4) - chessEdge;

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
                        canvas.drawBitmap(chessImage[temp - 1], j * chessEdge, i * chessEdge + START_POINT_Y, paint);
                    }
                }
            }
//
//            if (first_i != -1)
//                canvas.drawBitmap(choosedTarget, first_j * chessEdge, first_i * chessEdge + START_POINT_Y, paint);
        }
        Log.d(TAG, "debug -> onDraw: ");
    }

    public void initChess() {
        chessModel.setInitPosition(chessModel.getStartPosition());
        postInvalidate();   //redraw the View
        Log.d(TAG, "debug -> initChess: ");
    }

    public void  setFirstGo(boolean first) {
        renderChess(first);
    }

    private void renderChess(boolean isRed) {
        int size = BitmapFactory.decodeResource(res, R.drawable.bbishop).getWidth();

        matrix = new Matrix();
        float scale = ((float) chessEdge / (float) size);
        matrix.reset();
        matrix.postScale(scale, scale); //ty le hien thi cac o co


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

//        if (isRed) {
//            role = new Role(7, 15);
//        } else {
//            role = new Role(0, 8);
//        }

        //...something
        canDraw = true;
        postInvalidate();
        Log.d(TAG, "renderChess: ");
    }


    //xử lý sự kiện chạm vào quân cờ
    //handle event when touch chessman
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){

            Log.d(TAG, "onTouchEvent: clicked clicked");
        }
        return true;
    }
}
