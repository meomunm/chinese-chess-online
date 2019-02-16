package techkids.vn.chinesechessonline.views;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.luolc.emojirain.EmojiRainLayout;

import techkids.vn.chinesechessonline.R;
import techkids.vn.chinesechessonline.activities.EndGameFragment;
import techkids.vn.chinesechessonline.controllers.GameController;
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
            if (canMove(selectedPosition_i, selectedPosition_j) && whoseTurn()) {
                //vị trí mới sẽ bằng giá trị của vị trí cũ
                //và vị trí cũ thì bằng 0 tức là ô trống trong bàn cờ
                gameController.playSoundGo();

                //ván cờ kết thúc khi tướng của 1 trong 2 bên bị mất
                if (chessModel.getInitPosition()[selectedPosition_i][selectedPosition_j] == 1
                        || chessModel.getInitPosition()[selectedPosition_i][selectedPosition_j] == 8) {

                    chessModel.setInItPositionItem(selectedPosition_i, selectedPosition_j, chessModel.getInitPosition()[first_y][first_x]);
                    chessModel.setInItPositionItem(first_y, first_x, 0);
                    postInvalidate();
                    showDiaglogFragmentEndGame();
                }

                //di chuyển quân cờ đến vị trí mới
                chessModel.setInItPositionItem(selectedPosition_i, selectedPosition_j, chessModel.getInitPosition()[first_y][first_x]);
                chessModel.setInItPositionItem(first_y, first_x, 0);
            }
            firstchoice = true;
        }
    }

    //show dialogFragment
    private void showDiaglogFragmentEndGame() {
        FragmentManager fm = ((AppCompatActivity) myContext).getFragmentManager();
        EndGameFragment ef = new EndGameFragment();
        ef.show(fm, "null");
    }

    private boolean whoseTurn() {
        Log.d(TAG, "whoseTurn: " + chessModel.getInitPosition()[first_y][first_x]);

        if (role.canMove(chessModel.getInitPosition()[first_y][first_x])) {
            role.changeRole();
            return true;
        } else {
            Toast.makeText(myContext, "Chờ đối phương di chuyển đã!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean canMove(int index_i, int index_j) {
        //  lấy vị trí quân cờ đang được select
        int choice = chessModel.getInitPosition()[first_y][first_x];
        Log.d(TAG, String.format("canMove: choice = %s, x = %s, y =%s", choice, index_i, index_j));

        //ngăn cản việc đi đè lên quân mình
        //quân mình ăn quân mình
        if (isTheSameSide(index_i, index_j)) {
            Toast.makeText(myContext, "Không thể di chuyển, bị vướng đồng minh", Toast.LENGTH_SHORT).show();
            return false;
        }

        switch (choice) {
            case ChessModel.B_KING:
            case ChessModel.R_KING:
                return kingMove(index_i, index_j);

            case ChessModel.B_BISHOP:
            case ChessModel.R_BISHOP:
                //quân sĩ, di chuyển chéo 2 đơn vị trong cung tướng, ko đc phép đi ra ngoài cung tướng
                return bishopMove(index_i, index_j);

            case ChessModel.B_ELEPHANT:
            case ChessModel.R_ELEPHANT:
                //Quân Tượng cùng với Sỹ là hai quân phòng thủ
                //nó di chuyển chéo mỗi lần 2 ô nếu không bị cản và không được qua Sông.
                return elephantMove(index_i, index_j);

            case ChessModel.B_HORSE:
            case ChessModel.R_HORSE:
                //Mã di chuyển theo hình chữ L
                //nếu không bị cản
                return horseMove(index_i, index_j);

            case ChessModel.B_CAR:
            case ChessModel.R_CAR:
                //xe đi theo hình dấu cộng nếu không bị cản
                return carMove(index_i, index_j);

            case ChessModel.B_CANON:
            case ChessModel.R_CANON:
                //pháo di chuyển như xe nhưng nếu muốn ăn 1 quân khác màu
                // thì phải thông qua 1 quân trên bàn cờ
                return canonMove(index_i, index_j);

            case ChessModel.B_PAWN:
            case ChessModel.R_PAWN:
                //Quân Tốt khi chưa qua Sông thì nó di chuyển dọc
                //sau khi đã qua Sông thì Tốt có thể di chuyển theo chiều ngang và dọc
                // Nó chỉ di chuyển mỗi lần 1 ô và chỉ tiến lên không được lùi lại.
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
        Toast.makeText(myContext, "Nước di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(myContext, "Không thể di chuyển chéo!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean canonMove(int index_i, int index_j) {
        int count = 0;      //đê
        if (index_j == first_x) {
            //pháo đi lên
            if (first_y > index_i) {
                for (int i = first_y - 1; i > index_i; i--) {
                    //kiểm tra xem có gặp vật cản theo hàng dọc hay không
                    if (chessModel.getInitPosition()[i][index_j] != 0) {
                        count++;
                    }
                }
            } else if (first_y < index_i) { //pháo đi xuống, theo hàng dọc
                for (int i = first_y + 1; i < index_i; i++) {
                    //kiểm tra xem có gặp vật cản theo hàng dọc hay không
                    if (chessModel.getInitPosition()[i][index_j] != 0) {
                        count++;
                    }
                }
            }
            if (count == 0) {
                //trên đường di chuyển không gặp vật cản
                return true;
            } else if (count == 1) {
                if (chessModel.getInitPosition()[index_i][index_j] != 0) {
                    return true;
                } else {
                    Toast.makeText(myContext, "Di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        } else if (index_i == first_y) {   //di chuyển theo hàng ngang
            //ngang sang trái   <-
            if (index_j < first_x) {
                for (int j = first_x - 1; j > index_j; j--) {
                    if (chessModel.getInitPosition()[index_i][j] != 0) {
                        count++;
                    }
                }
                return true;
            } else if (index_j > first_x) {
                for (int j = first_x + 1; j < index_j; j++) {
                    if (chessModel.getInitPosition()[index_i][j] != 0) {
                        count++;
                    }
                }
            }
            if (count == 0) {
                //trên đường di chuyển không gặp vật cản
                return true;
            } else if (count == 1) {
                if (chessModel.getInitPosition()[index_i][index_j] != 0) {
                    return true;
                } else {
                    Toast.makeText(myContext, "Di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }

        Toast.makeText(myContext, "Di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean carMove(int index_i, int index_j) {
        //di chuyển theo hàng dọc
        if (index_j == first_x) {
            //xe đi lên
            if (first_y > index_i) {
                for (int i = first_y - 1; i > index_i; i--) {
                    Log.d(TAG, String.format("carMove: fi = %s; i = %s", first_y, index_i));
                    //kiểm tra xem có gặp vật cản theo hàng dọc hay không
                    if (chessModel.getInitPosition()[i][index_j] != 0) {
                        Toast.makeText(myContext, "Bị vướng vật cản không thể di chuyển! dọc lên", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return true;
            } else if (first_y < index_i) { //xe đi xuống, theo hàng dọc
                for (int i = first_y + 1; i < index_i; i++) {
                    Log.d(TAG, String.format("carMove: fi = %s; i = %s", first_y, index_i));

                    //kiểm tra xem có gặp vật cản theo hàng dọc hay không
                    if (chessModel.getInitPosition()[i][index_j] != 0) {
                        Toast.makeText(myContext, "Bị vướng vật cản không thể di chuyển! dọc xuống", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return true;
            }
        } else if (index_i == first_y) {   //di chuyển theo hàng ngang
            //ngang sang trái   <-
            if (index_j < first_x) {
                for (int j = first_x - 1; j > index_j; j--) {
                    if (chessModel.getInitPosition()[index_i][j] != 0) {
                        Toast.makeText(myContext, "Bị vướng vật cản không thể di chuyển! ngang trái", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return true;
            } else if (index_j > first_x) {
                for (int j = first_x + 1; j < index_j; j++) {
                    if (chessModel.getInitPosition()[index_i][j] != 0) {
                        Toast.makeText(myContext, "Bị vướng vật cản không thể di chuyển! ngang phải", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
                return true;
            }
        }
        Toast.makeText(myContext, "Di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean horseMove(int index_i, int index_j) {
        //di chuyển lên xuống
        if (((first_y - index_i == 2 || first_y - index_i == -2)
                && (first_x - index_j == 1 || first_x - index_j == -1))
                // di chuyển trái phải
                || ((first_y - index_i == 1 || first_y - index_i == -1)
                && (first_x - index_j == 2 || first_x - index_j == -2))) {
            //mã di chuyển lên và không bị vướng vật cản
            if (first_y - index_i == 2 && chessModel.getInitPosition()[first_y - 1][first_x] == 0) {
                return true;
            }
            //mã di chuyển xuống và không bị vướng vật cản
            if (first_y - index_i == -2 && chessModel.getInitPosition()[first_y + 1][first_x] == 0) {
                return true;
            }
            //di chuyển sang bên trái và không bị vướng ngại vật
            if (first_x - index_j == 2 && chessModel.getInitPosition()[first_y][first_x - 1] == 0) {
                return true;
            }
            //di chuyển sang bên phải và không bị vướng chướng ngại vật
            if (first_x - index_j == -2 && chessModel.getInitPosition()[first_y][first_x + 1] == 0) {
                return true;
            }
        }
        Toast.makeText(myContext, "Di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean elephantMove(int index_i, int index_j) {
        //ngăn cản tượng đen qua sông
        if (chessModel.getInitPosition()[first_y][first_x] == ChessModel.B_ELEPHANT && index_i > 4) {
            Toast.makeText(myContext, "Tượng không thể sang sông!", Toast.LENGTH_SHORT).show();
            return false;
        } else
            //ngăn cản tượng đỏ qua sông
            if (chessModel.getInitPosition()[first_y][first_x] == ChessModel.R_ELEPHANT && index_i < 5) {
                Toast.makeText(myContext, "Tượng không thể sang sông!", Toast.LENGTH_SHORT).show();
                return false;
            } else
                //cho phép di chuyển theo hình chéo
                if (first_y - index_i == 2 || first_y - index_i == -2 && first_x - index_j == 2 || first_x - index_j == -2) {

                    //kiểm tra xem có bị cản bởi quân cờ nào, trong nước di chuyển hay không?
                    if (chessModel.getInitPosition()[(first_y + index_i) / 2][(first_x + index_j) / 2] == 0) {
                        return true;
                    } else {
                        Toast.makeText(myContext, "đang bị cản bởi quân cờ khác!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else
                    Toast.makeText(myContext, "Nước di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean bishopMove(int index_i, int index_j) {
        if (chessModel.getInitPosition()[first_y][first_x] == ChessModel.B_BISHOP) {
            Log.d(TAG, "bishopMove: ");

            if (index_i > 2 || index_i < 0 || index_j < 3 || index_j > 5) {      //không được di chuyển ra ngoài cung tướng
                Toast.makeText(myContext, "Không thể đi ra ngoài cung tướng!", Toast.LENGTH_SHORT).show();
                return false;
            } else { //được phép di chuyển
                if (first_y == 1 && first_x == 4) { //quân sĩ đang ở trung tâm của cung tướng thì được phép di chuyển theo hình X
                    if ((index_i == 0 && index_j == 3) || (index_i == 0 && index_j == 5)
                            || (index_i == 2 && index_j == 3) || (index_i == 2 && index_j == 5)) {
                        return true;
                    } else
                        Toast.makeText(myContext, "Nước di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
                    return false;
                } else {
                    return index_i == 1 && index_j == 4;
                }
            }
        } else if (chessModel.getInitPosition()[first_y][first_x] == ChessModel.R_BISHOP) {
            if (index_i < 7 || index_i > 9 || index_j < 3 || index_j > 5) {
                Toast.makeText(myContext, "Không thể đi ra ngoài cung tướng!", Toast.LENGTH_SHORT).show();
                return false;
            } else {
                if (first_y == 8 && first_x == 4) { //đang ở trong cung tướng
                    if ((index_i == 9 && index_j == 3) || (index_i == 9 && index_j == 5)
                            || (index_i == 7 && index_j == 3) || (index_i == 7 && index_j == 5)) {
                        return true;
                    } else {
                        Toast.makeText(myContext, "Nước di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    return index_i == 8 && index_j == 4;
                }
            }
        }
        Toast.makeText(myContext, "Nước di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
        return false;
    }


    // kiểm tra xem di chuyển có hợp lệ không?
    private boolean kingMove(int index_i, int index_j) {
        // tướng chỉ được phép di chuyển trong vùng ô vuông X
        //giới hạn di chuyển của tướng trong một cung tướng
        if (chessModel.getInitPosition()[first_y][first_x] == ChessModel.B_KING) {  //di chuyển của Black King
            //không được di chuyển
            if (index_i > 2 || index_i < 0 || index_j < 3 || index_j > 5) {
                Toast.makeText(myContext, "Không thể đi ra ngoài cung tướng!", Toast.LENGTH_SHORT).show();
                return false;
            } else  //được di chuyển
                return isMoveNear(index_i, index_j);
        } else if (chessModel.getInitPosition()[first_y][first_x] == ChessModel.R_KING) { //di chuyển của Red King
            //giới hạn di chuyển của tướng trong 1 cung tướng
            if (index_i < 7 || index_i > 9 || index_j < 3 || index_j > 5) {
                Toast.makeText(myContext, "Không thể đi ra ngoài cung tướng!", Toast.LENGTH_SHORT).show();
                return false;
            } else  // được di chuyển
                return isMoveNear(index_i, index_j);
        }
        Toast.makeText(myContext, "Nước di chuyển không hợp lệ!", Toast.LENGTH_SHORT).show();
        return false;
    }

    //nếu vị trí mới và vị trí cũ cùng 1 team thì return true
    private boolean isTheSameSide(int index_i, int index_j) {
        if ((chessModel.getInitPosition()[first_y][first_x] < 8
                && chessModel.getInitPosition()[index_i][index_j] < 8
                && chessModel.getInitPosition()[index_i][index_j] > 0)
                || (chessModel.getInitPosition()[index_i][index_j] > 7
                && chessModel.getInitPosition()[first_y][first_x] > 7
                && chessModel.getInitPosition()[index_i][index_j] < 15)) {
            return true;
        }
        return false;
    }
}