package com.java.interview.tdd.connect4;

import java.util.Arrays;
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

      System.out.println(displayBoard());
      System.out.println(gameStatus());
   }

   private void runGame() {

   }

   private String gameStatus() {
      if (isBoardFull()) {
         return ("Draw!!");
      }

      return String.format("%nPlayer %s turn.%nEnter the disc in column[1-%d]:", getCurrentPlayer(), COLUMNS);
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

   /**
    * main runner of the game
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
      checkWinner(row, column);

      switchPlayer();

      System.out.println(displayBoard());
      System.out.println(gameStatus());
   }

   private void switchPlayer() {
      if ("R".equals(currentPlayer)) currentPlayer = "G";
      else currentPlayer = "R";
   }

   public String getCurrentPlayer() {
      return currentPlayer;
   }

   public static void main(String[] args) {
      Connect4 connect4 = new Connect4();
      while (!connect4.isBoardFull()) {

      }
   }

   public boolean isFinished() {
      return isBoardFull();
   }

   public String getWinner() {
      return winner;
   }

   private void checkWinner(int row, int column) {
      if (winner.isEmpty()) {
         String color = board[row][column];

         Pattern winPattern = Pattern.compile(".*" + color + "{" + DISCS_TO_WIN + "}.*"); // .*R{4}.*

         String vertical = IntStream.range(0, ROWS)
            .mapToObj(r -> board[r][column])
            .reduce(String::concat).get();

         String horizontal = Stream.of(board[row])
            .reduce(String::concat).get();

         if (winPattern.matcher(vertical).matches()
            || winPattern.matcher(horizontal).matches()) winner = color;
      }
   }
}
