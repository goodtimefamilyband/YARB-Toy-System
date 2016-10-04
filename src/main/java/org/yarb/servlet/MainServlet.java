package org.yarb.servlet;

import org.yarb.db.RockPaperScissorsDb;
import org.yarb.db.RockPaperScissorsDb.Game;

/** import the used packeges.*/

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

  RockPaperScissorsDb db;
  
  /**
   * Overrides HttpServlet.init
   */
  public void init() throws ServletException {
    super.init();
    try {
      db = new RockPaperScissorsDb();
    } catch (ClassNotFoundException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    } catch (SQLException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }
  
  protected void doGet(
          HttpServletRequest request, 
          HttpServletResponse response) 
          throws ServletException, IOException {
    response.setContentType("text/html");
    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().println("<h1>Hello Servlet</h1>");
    response.getWriter().println("session=" + request.getSession(true).getId());
    String playermove = request.getParameter("move");
    String userInput = playermove;
    if (isValid(userInput)) {
      String sessionId = request.getSession(true).getId();
      String resp = game(userInput, sessionId);
      response.getWriter().println(resp);
    } else {
      response.getWriter().println("<h1>Invalid user input!\nWrite rock, paper or scissors!</h1>");
    }
  }   


  public void print(String text) {
    System.out.println(text);
  }

  /**Check if the input from the user is valid. */
  public boolean isValid(String input) {
    if (input.equalsIgnoreCase("rock")) {
      return true;
    }
    if (input.equalsIgnoreCase("paper")) {
      return true;
    }
    if (input.equalsIgnoreCase("scissors")) {
      return true;
    }
    
    return false;
  }

  /**set the game function. */
  public String game(String user, String session) {
    String computer = computerResults();
    
    print(user + "vs" + computer + "\n");

    int won;
    String returnVal;
    if (user.equalsIgnoreCase(computer)) {
      won = RockPaperScissorsDb.TIED;
      returnVal = "<h1>Stalemate! No winners.</h1>";
    } else {
      if (checkWin(user, computer)) {
        won = RockPaperScissorsDb.WON;
        returnVal = "<h1>You won against the computer</h1>";
      } else {
        won = RockPaperScissorsDb.LOST;
        returnVal = "<h1>You lost against the computer!</h1>";
      }
    }
    
    Game game = db.createNewGame(
        session, 
        new Date(System.currentTimeMillis()), 
        user, 
        computer, 
        won
        );
    
    game.save();
    
    return returnVal;
  }

  /**create a random option from the computer. */
  public String computerResults() {
    String[] types = {"rock", "paper", "scissors"};
    Random rand = new Random();
    int computerChoice = rand.nextInt(3);
    
    return types[computerChoice];
  }

  /**check who is the winner. */
  public boolean checkWin(String user, String opponent) {
    if ((!isValid(user)) && (!isValid(opponent))) {
      return false;
    }

    String rock = "rock";
    String paper = "paper"; 
    String scissors = "scissors";
    
    if ((user.equalsIgnoreCase(rock)) && (opponent.equalsIgnoreCase(scissors))) {
      return true;
    }
    
    if ((user.equalsIgnoreCase(scissors)) && (opponent.equalsIgnoreCase(paper))) {
      return true;
    }
    
    if ((user.equalsIgnoreCase(paper)) && (opponent.equalsIgnoreCase(rock))) {
      return true;
    }
    
    return false;
    
  }
  
}
