package com.java.interview.tdd.connect4;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Connect4 {
   private final int ROWS = 6;
   private final int COLUMNS = 7;
   private final String EMPTY = " ";
   private final int DISCS_TO_WIN = 4;

   private String[][] board = new String[ROWS][COLUMNS];
   private String currentPlayer = "R";
   private String winner = "";

   public Connect4() {
      for (String[] row :
         board) {
         Arrays.fill(row, EMPTY);
      }
   }

   public String displayBoard() {
      StringBuilder rows = new StringBuilder();
      for (int i = ROWS - 1; i >= 0; i--) {
         StringJoiner stringJoiner = new StringJoiner("|", "|", "|");
         for (int j = 0; j < COLUMNS; j++) {
            stringJoiner.add(board[i][j]);
         }
         rows.append(stringJoiner).append("\n");
      }

      rows.deleteCharAt(rows.lastIndexOf("\n"));
      return rows.toString();
   }

   private String playersTurn() {
      return String.format("%nPlayer %s turn. Enter the disc in column[1-%d]:", getCurrentPlayer(), COLUMNS);
   }

   public int getTotalNumberOfDiscs() {
      return IntStream.range(0, COLUMNS)
         .map(this::getNumberOfDiscsInAColumn)
         .sum();
   }

   private int getNumberOfDiscsInAColumn(int column) {
      return (int) IntStream.range(0, ROWS)
         .filter(row -> !EMPTY.equals(board[row][column]))
         .count();
   }

   private boolean isBoardFull() {
      return getTotalNumberOfDiscs() == (ROWS * COLUMNS);
   }

   private void switchPlayer() {
      if ("R".equals(currentPlayer)) currentPlayer = "G";
      else currentPlayer = "R";
   }

   public String getCurrentPlayer() {
      return currentPlayer;
   }

   public boolean isFinished() {
      return isBoardFull() || !winner.isEmpty();
   }

   public String getWinner() {
      return winner;
   }

   private boolean checkWinner(int row, int column) {
      String color = board[row][column];
      Pattern winPattern = Pattern.compile(".*" + color + "{" + DISCS_TO_WIN + "}.*"); // .*R{4}.*

      return (checkVerticalWinner(column, color, winPattern)
         || checkHorizontalWinner(board[row], color, winPattern)
         || checkDiagonalWinnerTopRightToBottomLeft(row, column, color, winPattern)
         || checkDiagonalWinnerTopLeftToBottomRight(row, column, color, winPattern));

   }

   private boolean checkHorizontalWinner(String[] values, String color, Pattern winPattern) {
      String horizontal = Stream.of(values)
         .reduce(String::concat).get();

      if (winPattern.matcher(horizontal).matches()) {
         winner = color;
         return true;
      }

      return false;
   }

   private boolean checkVerticalWinner(int column, String color, Pattern winPattern) {
      String vertical = IntStream.range(0, ROWS)
         .mapToObj(r -> board[r][column])
         .reduce(String::concat).get();

      if (winPattern.matcher(vertical).matches()) {
         winner = color;
         return true;
      }

      return false;
   }

   private boolean checkDiagonalWinnerTopLeftToBottomRight(int row, int column, String color, Pattern winPattern) {
      int startOffset = Math.min(column, ROWS - 1 - row);

      int myColumn = column - startOffset,
         myRow = row + startOffset;

      StringJoiner stringJoiner = new StringJoiner("");
      do {
         stringJoiner.add(board[myRow--][myColumn++]);
      } while (myColumn < COLUMNS && myRow >= 0);

      if (winPattern.matcher(stringJoiner.toString()).matches()) {
         winner = color;
         return true;
      }

      return false;
   }

   private boolean checkDiagonalWinnerTopRightToBottomLeft(int row, int column, String color, Pattern winPattern) {
      int startOffset = Math.min(column, row);

      int myColumn = column - startOffset,
         myRow = row - startOffset;

      StringJoiner stringJoiner = new StringJoiner("");
      do {
         stringJoiner.add(board[myRow++][myColumn++]);
      } while (myColumn < COLUMNS && myRow < ROWS);

      if (winPattern.matcher(stringJoiner.toString()).matches()) {
         winner = color;
         return true;
      }

      return false;
   }

   /**
    * runner of the game
    *
    * @param column
    */
   public void putDiscInColumn(int column) {
      if (column < 1 || column > COLUMNS) throw new IllegalArgumentException("Invalid column " + column);

      int row = getNumberOfDiscsInAColumn(--column);
      if (row >= ROWS) {
         System.out.println("Current column is full, try another column");
         return;
      }

      board[row][column] = currentPlayer;

      if (checkWinner(row, column)) {
         System.out.printf("%s%n%n============= CONGRATULATIONS!! PLAYER %s WINS!! =============%n", displayBoard(), currentPlayer);
         return;
      }

      if(isBoardFull()) {
         System.out.printf("%s%n%n============= IT'S A DRAW!! =============%n", displayBoard());
         return;
      }

      switchPlayer();
   }


   public static void main(String[] args) {
      Connect4 connect4 = new Connect4();
      try (Scanner scanner = new Scanner(System.in)) {
         while (!connect4.isFinished()) {
            System.out.println(connect4.displayBoard());
            System.out.println(connect4.playersTurn());

            int userInputColumn = -1;

            try {
               userInputColumn = scanner.nextInt();
            } catch (NoSuchElementException | IllegalStateException ne) {
               System.err.println("Should put correct input integer column in the range [1-7] !!");
            }

            connect4.putDiscInColumn(userInputColumn);
         }
      }
   }

}
