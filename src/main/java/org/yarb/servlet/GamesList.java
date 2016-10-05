package org.yarb.servlet;

import org.yarb.db.RockPaperScissorsDb;
import org.yarb.db.RockPaperScissorsDb.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GamesList extends HttpServlet {

  RockPaperScissorsDb db;
  
  /**
   * Overrides HttpServlet.init
   */
  public void init() throws ServletException {
    super.init();
    
    try {
      db = new RockPaperScissorsDb();
    } catch (ClassNotFoundException | SQLException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
  }
  
  protected void doGet(
      HttpServletRequest request, 
      HttpServletResponse response) 
      throws ServletException, IOException {
    
    PrintWriter writer = response.getWriter();
    writer.println("<h1>Your Results</h1>");
    
    Game[] games = db.getGamesForSession(request.getSession(true).getId());
    if (games.length == 0) {
      writer.println("You have no games yet...");
      return;
    }
    
    writer.println("<table><tr>");
    writer.println("<td>Date Played</td>");
    writer.println("<td>You threw:</td>");
    writer.println("<td>The Computer threw:</td>");
    writer.println("<td>The result:</td>");
    writer.println("</tr>");
    String row = "<td>%s</td>";
    for (int i = 0; i < games.length; i++) {
      writer.println("<tr>");
      Game gm = games[i];
      
      writer.println(row.replace("%s", gm.gameDate.toString()));
      writer.println(row.replace("%s", gm.playerMove));
      writer.println(row.replace("%s", gm.computerMove));    
      writer.println(row.replace("%s", gm.getResult()));
      writer.println("</tr>");
    }
    writer.println("</table>");
  }
  
}
