package techkids.vn.chinesechessonline.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

public class DrawUtils {
    private final String TAG = DrawUtils.class.toString();
    static final int LINES = 10, COLUMNS = 9;
    private final int DIMENSION_HORIZONTAL = 8;
    private final int DIMENSION_VERTICAL = 9;


    private Paint paint;
    private int screenWidth;
    private int screenHeight;
    private Bitmap chessBoard;
    private Canvas canvas;
    private int chessSize;

    public DrawUtils(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        chessSize = getChessSize();
        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setColor(Color.BLACK);
        chessBoard = Bitmap.createBitmap(this.screenWidth, this.screenHeight, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(chessBoard);
    }

    public Bitmap drawChessBoard() {
        drawLines();        //draw | -
        draw2X();             //draw X
        Log.d(TAG, "debug -> drawChessBoard: ");
        return chessBoard;
    }

    private void drawLines() {
        int boardX = getBoardStartPoint().x;
        int boardY = getBoardStartPoint().y;

        //draw columns
        for (int i = 0, x = boardX; i < COLUMNS; i++, x += chessSize) {
            if (i == 0 || i == COLUMNS - 1) {
                //vẽ 2 đường dọc, ngoài cùng trái và phải
                int DESY = boardY + DIMENSION_VERTICAL * chessSize;   //độ dài của dòng kẻ
                canvas.drawLine(x, boardY, x, DESY, paint);
            } else {
                //vẽ các đường kẻ dọc với chiều dọc ngắn hơn
                int DESY = boardY + (DIMENSION_HORIZONTAL / 2) * chessSize;
                canvas.drawLine(x, boardY, x, DESY, paint);
                canvas.drawLine(x, DESY + chessSize, x, DESY + 5 * chessSize, paint);
            }
        }

        //vẽ dòng kẻ ngang
        for (int i = 0, y = boardY, DESX = boardX + DIMENSION_HORIZONTAL * chessSize; i < LINES; y += chessSize, i++) {
            canvas.drawLine(boardX, y, DESX, y, paint);
        }
    }

    private void draw2X() {
        /*          (x;y)
            start           end
            (0;3)          (2;5)    \  /
            (2;3)          (0;5)     \/
            (7;3)          (9;5)     /\
            (9;3)          (7;5)    /  \
         */
        int pointxy[] = new int[]{0, 3, 2, 5, 2, 3, 0, 5, 7, 3, 9, 5, 9, 3, 7, 5};
        for (int i = 0; i < pointxy.length; i += 4) {
            Point start = getBoardPoint(pointxy[i], pointxy[i + 1]);
            Point end = getBoardPoint(pointxy[i + 2], pointxy[i + 3]);
            canvas.drawLine(start.x, start.y, end.x, end.y, paint);
        }
    }

    private Point getBoardStartPoint() {
        Point point = new Point();
        int chessSize = getChessSize();
        point.x = (screenWidth - DIMENSION_HORIZONTAL * chessSize) / 2;
        point.y = (screenHeight - DIMENSION_VERTICAL * chessSize) / 2;
        return point;
    }

    public Point getBoardPoint(int row, int col) {
        Point start = getBoardStartPoint();
        int chessSize = getChessSize();
        Point point = new Point();
        point.x = col * chessSize + start.x;
        point.y = row * chessSize + start.y;
        return point;
    }

    public int getChessSize() {
        final int CHESS_IN_ROW = 9;
        return screenWidth / CHESS_IN_ROW;
    }
}
