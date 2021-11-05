//Adem VAROL - 200709078
package adem.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    byte[][] board = new byte[3][3];
    static final String PLAYER_1 = "X";
    static final String PLAYER_2 = "O";
    boolean player1Turn = true;
    int moveCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TableLayout tableLayout = findViewById(R.id.board);
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button btn = (Button) row.getChildAt(j);
                btn.setOnClickListener(new CellListener(i, j));
            }
        }

    }

    class CellListener implements View.OnClickListener {
        int row, col;

        public CellListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void onClick(View v) {
            if (isCellOccupied(row, col)) {
                Toast.makeText(MainActivity.this, "CELL IS ALREADY OCCUPIED", Toast.LENGTH_SHORT).show();
                return;
            }
            if (player1Turn) {
                board[row][col] = 1;
                ((Button) v).setText(PLAYER_1);
                moveCount++;
            } else {
                board[row][col] = 2;
                ((Button) v).setText(PLAYER_2);
                moveCount++;
            }

            int gameState = gameEnded(row, col);
            if (gameState == -1) {
                player1Turn = !player1Turn;
            } else if (gameState == 0) {
                Toast.makeText(MainActivity.this, "IT IS A DRAW", Toast.LENGTH_SHORT).show();
                findViewById(R.id.itemNG).callOnClick();
            } else {
                Toast.makeText(MainActivity.this, "PLAYER " + gameState + " WINS", Toast.LENGTH_SHORT).show();
                findViewById(R.id.itemNG).callOnClick();
            }

        }

        private int gameEnded(int row, int col) {
            int player = board[row][col];

            boolean win = true;
            for (int i = 0; i < 3; i++) {
                if (board[i][col] != player) {
                    win = false;
                    break;
                }
            }

            if (win)
                return player;

            win = true;
            for (int j = 0; j < 3; j++) {
                if (board[row][j] != player) {
                    win = false;
                    break;
                }
            }
            if (win)
                return player;


            // for the left to right diagonal of the board
            win = true;
            for (int i = 0; i < 3; i++) {
                if (board[i][i] != player) {
                    win = false;
                    break;
                }
            }
            if (win)
                return player;

            // for the right to left diagonal of the board
            win = true;
            for (int i = 0; i < 3; i++) {
                if (board[i][2 - i] != player) {
                    win = false;
                    break;
                }
            }
            if (win)
                return player;

            if (moveCount == 9)
                return 0;
            return -1;
        }

        private boolean isCellOccupied(int row, int col) {
            return board[row][col] != 0;
        }

    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean("player1Turn", player1Turn);
        outState.putInt("moveCount", moveCount);
        byte[] boardOneDimension = new byte[9];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                boardOneDimension[3 * i + j] = board[i][j];
            }
        }
        outState.putByteArray("board", boardOneDimension);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        player1Turn = savedInstanceState.getBoolean("player1Turn");
        moveCount = savedInstanceState.getInt("moveCount");
        byte[] boardOneDimension = savedInstanceState.getByteArray("board");

        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = boardOneDimension[i];
        }

        TableLayout tableLayout = findViewById(R.id.board);
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                Button btn = (Button) row.getChildAt(j);
                if (board[i][j] == 1)
                    btn.setText(PLAYER_1);
                else if (board[i][j] == 2)
                    btn.setText(PLAYER_2);
                else if (board[i][j] == 0)
                    btn.setText("");
            }
        }
    }

    //Add menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Menu item click listener for the new game
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        TableLayout tableLayout = findViewById(R.id.board);
        for (int i = 0; i < 3; i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0; // for the board empty
                Button btn = (Button) row.getChildAt(j); // for the button empty
                btn.setText("");
            }
        }
        moveCount = 0;
        Toast.makeText(MainActivity.this, "NEW GAME STARTED", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}
