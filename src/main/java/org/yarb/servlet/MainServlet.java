package org.yarb.servlet;

/** import the used packeges.*/
import java.io.IOException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

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
      String resp = game(userInput);
      response.getWriter().println(resp);
    } else {
      response.getWriter().println("<h1>Invalid user input!\nWrite rock, paper or scissors!</h1>");
    }
  }   


  public static void print(String text) {
    System.out.println(text);
  }

  /**Check if the input from the user is valid. */
  public static boolean isValid(String input) {
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
  public static String game(String user) {
    String computer = computerResults();
    
    print(user + "vs" + computer + "\n");
    
    if (user.equalsIgnoreCase(computer)) {
      return "<h1>Stalemate! No winners.</h1>";
    } else {
      if (checkWin(user, computer)) {
        return "<h1>You won against the computer</h1>";
      } else {
        return "<h1>You lost against the computer!</h1>";
      }
    }
  }

  /**create a random option from the computer. */
  public static String computerResults() {
    String[] types = {"rock", "paper", "scissors"};
    Random rand = new Random();
    int computerChoice = rand.nextInt(3);
    
    return types[computerChoice];
  }

  /**check who is the winner. */
  public static boolean checkWin(String user, String opponent) {
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
