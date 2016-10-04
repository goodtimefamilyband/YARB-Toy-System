package org.yarb.servlet;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("<h1>Hello Servlet</h1>");
        response.getWriter().println("session=" + request.getSession(true).getId());
        String player_move = request.getParameter("move");

        String userInput = player_move;
		if (isValid(userInput)){
			String resp = game(userInput);
			response.getWriter().println(resp);
		}else{
//			print("Invalid user input!\nWrite rock, paper or scissors!");
	        response.getWriter().println("<h1>Invalid user input!\nWrite rock, paper or scissors!</h1>");
		}
        
    }
	
	
	
	
	public static void print(String text){
		System.out.println(text);
	}
	
	public static boolean isValid(String input){
		if (input.equalsIgnoreCase("rock")){
			return true;
		}
		if (input.equalsIgnoreCase("paper")){
			return true;
		}
		if (input.equalsIgnoreCase("scissors")){
			return true;
		}
		
		return false;
	}
	
//	public static void game(String user){
	public static String game(String user){
		String computer = computerResults();
		
		print(user+ "vs"+computer+"\n");
		
		if (user.equalsIgnoreCase(computer)){
			//print("Stalemate! No winners.");
			return "<h1>Stalemate! No winners.</h1>";
		}else{
			if (checkWin(user, computer)){
				//print("You won against the computer!");
				return "<h1>You won against the computer</h1>";
			}else {
				//print("You lost against the computer!");
				return "<h1>You lost against the computer!</h1>";
			}
		}
	}
	
	
	public static String computerResults(){
		String types[] = {"rock", "paper", "scissors"};
		Random rand = new Random();
		int computerChoice = rand.nextInt(3);
		
		return types[computerChoice];
	}
	
	public static boolean checkWin(String user, String opponent){
		if ((!isValid(user)) && (!isValid(opponent))){
			return false;
		}
		
		String rock = "rock", paper = "paper", scissors = "scissors";
		
		if ((user.equalsIgnoreCase(rock)) && (opponent.equalsIgnoreCase(scissors))){
			return true;
		}
		
		if ((user.equalsIgnoreCase(scissors)) && (opponent.equalsIgnoreCase(paper))){
			return true;
		}
		
		if ((user.equalsIgnoreCase(paper)) && (opponent.equalsIgnoreCase(rock))){
			return true;
		}
		
		return false;
		
	}
	
}
