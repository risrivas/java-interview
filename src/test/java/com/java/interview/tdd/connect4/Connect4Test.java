package com.java.interview.tdd.connect4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class Connect4Test {

   private Connect4 connect4;

   @Rule
   public ExpectedException exception = ExpectedException.none();

   @Before
   public void setUp() {
      connect4 = new Connect4();
   }

   @Test
   public void displayEmptyBoardWith6RowsAnd7Columns() {
      assertThat(connect4.displayBoard(), is(
         "| | | | | | | |\n" +
            "| | | | | | | |\n" +
            "| | | | | | | |\n" +
            "| | | | | | | |\n" +
            "| | | | | | | |\n" +
            "| | | | | | | |"
      ));
   }

   @Test
   public void whenTheGameIsStartedTheBoardIsEmpty() {
      assertThat(connect4.getTotalNumberOfDiscs(), is(0));
   }

   @Test
   public void whenDiscOutsideBoardThenThrowException() {
      int column = -1;

      exception.expect(IllegalArgumentException.class);
      exception.expectMessage("Invalid column " + column);
      connect4.putDiscInColumn(column);
   }

   @Test
   public void whenFirstDiscInsertedInColumnThenNumberOfDiscIsOne() {
      connect4.putDiscInColumn(1);
      assertThat(connect4.getTotalNumberOfDiscs(), is(1));
   }

   @Test
   public void whenDiscInsertedThenNumberOfDiscsIncreases() {
      connect4.putDiscInColumn(1);
      connect4.putDiscInColumn(1);
      connect4.putDiscInColumn(2);
      assertThat(connect4.getTotalNumberOfDiscs(), is(3));
   }


   /*
   Should refactor the code now
    */


   @Test
   public void whenFirstPlayerPlaysThenDiscColorIsRed() {
      assertThat(connect4.getCurrentPlayer(), is("R"));
   }

   @Test
   public void whenSecondPlayerPlaysThenDiscColorIsGreen() {
      connect4.putDiscInColumn(2);
      assertThat(connect4.getCurrentPlayer(), is("G"));
   }


    /*
   Should refactor the code now
    */


   @Test
   public void whenTheGameStartsItIsNotFinished() {
      assertFalse("The game must not be finished as it is just started", connect4.isFinished());
   }

   @Test
   public void whenNoDiscCanBeIntroducedTheGamesIsFinished() {
      for (int i = 0; i < 6; i++) {
         for (int j = 1; j < 8; j++) {
            connect4.putDiscInColumn(j);
         }
      }
      assertTrue("The game must be finished now", connect4.isFinished());
   }


    /*
   Should refactor the code now
    */


   @Test
   public void when4VerticalDiscsAreConnectedThenPlayerWins() {
      for (int row = 0; row < 3; row++) {
         connect4.putDiscInColumn(1); // R
         connect4.putDiscInColumn(2); // G
      }
      assertThat(connect4.getWinner(), is(""));

      connect4.putDiscInColumn(1); // R
      assertThat(connect4.getWinner(), is("R"));
   }

   @Test
   public void when4HorizontalDiscsAreConnectedThenPlayerWins() {
      int column;
      for (column = 0; column < 3; column++) {
         connect4.putDiscInColumn(column); // R
         connect4.putDiscInColumn(column); // G
      }
      assertThat(connect4.getWinner(), is(""));

      connect4.putDiscInColumn(column); // R
      assertThat(connect4.getWinner(), is("R"));
   }
}
