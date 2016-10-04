package org.yarb.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

public class RockPaperScissorsDb {

  public static final String DBNAME = "rocks_paper_scissors.db";
  public static final String TBLNAME = "games";

  public static final String COL_ID = "id";
  public static final String COL_SESHID = "seshid";
  public static final String COL_GAME_DATE = "game_date";
  public static final String COL_PLAYER_MOVE = "player_move";
  public static final String COL_COMPUTER_MOVE = "computer_move";
  public static final String COL_WON = "won";
  
  public static final String ROCK = "rock";
  public static final String PAPER = "paper";
  public static final String SCISSORS = "scissors";
  
  public static final int WON = 1;
  public static final int LOST = -1;
  public static final int TIED = 0;
  
  public static final String INSERT_GAME_SQL
      = "INSERT INTO " + TBLNAME + "("
      + COL_SESHID + ","
      + COL_GAME_DATE + ","
      + COL_PLAYER_MOVE + ","
      + COL_COMPUTER_MOVE + ","
      + COL_WON + ")"
      + " VALUES (?, ?, ?, ?, ?)";

  private Connection cxn;
  
  /**
   * Create the table.
   */
  public void createTable() {
    StringBuilder sqlSb = new StringBuilder();
    sqlSb.append("CREATE TABLE IF NOT EXISTS " + TBLNAME + "(");
    Hashtable<String, String> colTbl = new Hashtable<>();

    colTbl.put(COL_ID, "INT PRIMARY KEY");
    colTbl.put(COL_SESHID, "TEXT NOT NULL");
    colTbl.put(COL_GAME_DATE, "DATETIME NOT NULL");
    colTbl.put(COL_PLAYER_MOVE, "TEXT NOT NULL");
    colTbl.put(COL_COMPUTER_MOVE, "TEXT NOT NULL");
    colTbl.put(COL_WON, "INT NOT NULL");
    
    Iterator<String> keys = colTbl.keySet().iterator();
    LinkedList<String> lst = new LinkedList<>();
    
    while (keys.hasNext()) {
      String key = keys.next();
      String val = colTbl.get(key);
      
      sqlSb.append(key + " " + val);
      if (!keys.hasNext()) {
        sqlSb.append(",");
      }
    }
    
    sqlSb.append(")");
  }
  
  public RockPaperScissorsDb() throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    cxn = DriverManager.getConnection("jdbc:sqlite:" + DBNAME);
  }
  
  public Game createNewGame(String sid, java.util.Date dt, String pmove, String cmove, int wn) {
    return new Game(sid, dt, pmove, cmove, wn);
  }
  
  /**
   * Get games played during a given session.
   * @param seshid The ID of the session
   * @return An array of Game database objects
   */
  public Game[] getGamesForSession(String seshid) {
    try {
      Statement stmt = cxn.createStatement();
      String sql = "SELECT * FROM " 
          + TBLNAME 
          + " WHERE seshid = \"" 
          + seshid 
          + "\"";
      
      LinkedList<Game> lstGames = new LinkedList<Game>();
      
      ResultSet rs = stmt.executeQuery(sql);
      while (rs.next()) {
        lstGames.add(new Game(rs));
      }
      
      Game[] games = new Game[lstGames.size()];
      return lstGames.toArray(games);
      
    } catch (SQLException ex) {
      // TODO Auto-generated catch block
      ex.printStackTrace();
    }
    
    return new Game[0];
  }
  
  public class Game {
    protected int id;
    public String seshid;
    public Date gameDate;
    public String playerMove;
    public String computerMove;
    private int won;
    
    public int getId() {
      return id;
    }
    
    /**
     * Construct Game from DB result set.
     * @param rs java.sql.ResultSet containing Game properties
     * @throws SQLException If the result set is not properly structured
     */
    public Game(ResultSet rs) throws SQLException {
      id = rs.getInt(COL_ID);
      seshid = rs.getString(COL_SESHID);
      gameDate = rs.getDate(COL_GAME_DATE);
      playerMove = rs.getString(COL_PLAYER_MOVE);
      computerMove = rs.getString(COL_COMPUTER_MOVE);
      won = rs.getInt(COL_WON);
    }
    
    /**
     * Construct a game from individual data items.
     * @param sid session ID
     * @param dt the date the game was played
     * @param pmove the player move
     * @param cmove the computer move
     */
    public Game(String sid, java.util.Date dt, String pmove, String cmove, int wn) {
      seshid = sid;
      gameDate = new Date(dt.getTime());
      playerMove = pmove;
      computerMove = cmove;
      won = wn;
    }
    
    /**
     * Save this game to the database.
     */
    public void save() {
      try {
        PreparedStatement stmt = cxn.prepareStatement(INSERT_GAME_SQL);
        stmt.setString(1, seshid);
        stmt.setDate(2, gameDate);
        stmt.setString(3, playerMove);
        stmt.setString(4, computerMove);
        stmt.setInt(5, won);
        
        stmt.executeUpdate();
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
          id = rs.getInt(0);
        }
      } catch (SQLException ex) {
        // TODO Auto-generated catch block
        ex.printStackTrace();
      }
    }
  }
}
