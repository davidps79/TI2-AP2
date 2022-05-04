package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

public class GameController {
	private int rows;
	private int columns;
	private int seeds;
	private int links;
	private Player rick;
	private Player morty;
	private Player currentPlayer;
	private char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	private LinkedList<Square> board;
	private ArrayList<Player> scoreRegistry;
	private LocalTime turnTime;
	
	@SuppressWarnings("unchecked")
	public GameController(int rows, int columns, int seeds, int links, String player1Name, String player2Name) {
		this.rows = rows;
		this.columns = columns;
		this.seeds = seeds;
		this.links = links;
		this.rick = new Player(player1Name, "Rick");
		this.morty = new Player(player2Name, "Morty");
		this.board = new LinkedList<>((int) Math.log10(columns * rows)+3);
		this.currentPlayer = rick;
		File file = new File(".\\files\\dataScore.obj");
		if(!file.exists()) {
			scoreRegistry = new ArrayList<Player>();
			saveScore();
		 }else {
			try {
				//System.out.println("Hola");
				FileInputStream fis = new FileInputStream(file);
				ObjectInputStream ois = new ObjectInputStream(fis); 
				this.scoreRegistry=(ArrayList<Player>) ois.readObject();
				ois.close();
				fis.close();
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		 }
		setupBoard();
	}
	
	public String getScoreboard() {
		String s = "Rick (" + rick.getName() + "): " + rick.getScore()
			   + "\nMorty (" + morty.getName() + "): " + morty.getScore();
		return s;
	}
	
	public void setupBoard() {
		ArrayList<Integer> auxArr = new ArrayList<>();
		ArrayList<Integer> auxArr2 = new ArrayList<>();
		
		for (int i=1; i<=rows*columns; i++) {
			board.add(new Square(i));
			auxArr.add(i);
			auxArr2.add(i);
		}
		
		// Posicionar jugadores
		
		int pos = randomInt(0, auxArr.size()-1);
		Node<Square> selectedPosition = board.get(auxArr.get(pos));
		rick.setPosition(selectedPosition);
		selectedPosition.getItem().addPlayer(rick);
		auxArr.remove(pos);
		auxArr2.remove(pos);
		
		pos = randomInt(0,auxArr.size()-1);
		selectedPosition = board.get(auxArr.get(pos));
		morty.setPosition(selectedPosition);
		selectedPosition.getItem().addPlayer(morty);
		auxArr.remove(pos);
		auxArr2.remove(pos);
		
		// Se excluyeron las casillas que ocupan los jugadores de portales y semillas
		
		// Posicionar semillas
		int seedsTemp = seeds;
		while (seedsTemp>0) {
			int selected = randomInt(0, auxArr.size()-1);
			//System.out.println("<!> Seed added at [" + board.get(auxArr.get(selected)).getItem().toString() + "] with position " + selected + " --> " + auxArr.get(selected) );
			Node<Square> temp = board.get(auxArr.get(selected));
			temp.getItem().setSeed(true);
			auxArr.remove(selected);
			seedsTemp--;
		}
		
		// Posicionar enlaces
		
		int i = 0;
		while (links>0) {		
			
			int selected = randomInt(0, auxArr2.size()-1);
			int obtained = auxArr2.get(selected);
			Node<Square> temp = board.get(obtained);
			auxArr2.remove(selected);
			
			int selected2 = randomInt(0, auxArr2.size()-1);
			int obtained2 = auxArr2.get(selected2);
			Node<Square> temp2 = board.get(obtained2);
			auxArr2.remove(selected2);			
			temp.getItem().setLinkMoves(obtained2-obtained);
			temp.getItem().setLinkId(alphabet[i]);
			temp2.getItem().setLinkMoves(obtained-obtained2);
			temp2.getItem().setLinkId(alphabet[i]);

			links--;
			i++;
		}
	}

	public int randomInt(int min, int max) {
        return (int) (Math.random() * (max + 1 - min)) + min;
    }
	
	public int throwDice() {
		return randomInt(1, 6);
	}
	
	public String movePlayer(int positions, int direction) {
		currentPlayer.getPosition().getItem().removePlayer(currentPlayer);
		if (direction == 1) currentPlayer.moveForward(positions);
		else currentPlayer.moveBackward(positions);
		
		String s = "<!> " + currentPlayer.getCharacterName() + " se ha movido a ["
		         + currentPlayer.getPosition().getItem().getNumber() + "]\n";
		
		if (currentPlayer.getPosition().getItem().hasSeed()) {
			s += "<!> " + currentPlayer.getCharacterName() + " recolecto una semilla en [" 
			  + currentPlayer.getPosition().getItem().getNumber() + "]\n"; 
			seeds--;
			
			currentPlayer.getPosition().getItem().setSeed(false);
			currentPlayer.addScore();
		} 
		
		int travel = currentPlayer.getPosition().getItem().getLinkMoves();
		if (travel != 0) {	
			// System.out.println(travel + " travel");
			currentPlayer.getPosition().getItem().removePlayer(currentPlayer);
			if (travel<0) currentPlayer.moveBackward(travel*-1);
			else currentPlayer.moveForward(travel);
			
			s += "<!> " + currentPlayer.getCharacterName() + " viajo en un portal hacia ["
					  + currentPlayer.getPosition().getItem().getNumber() + "]\n";	
			
			if (currentPlayer.getPosition().getItem().hasSeed()) {
				s += "<!> " + currentPlayer.getCharacterName() + " recolecto una semilla en [" 
				  + currentPlayer.getPosition().getItem().getNumber() + "]\n"; 
				seeds--;
				
				currentPlayer.getPosition().getItem().setSeed(false);
				currentPlayer.addScore();
			} 
		}
		
		return s;
	}

	public String getBoardSeedsData() {
		return board.toStringSeeds(columns);
	}
	
	public String getBoardLinksData() {
		return board.toStringLinks(columns);
	}
	
	public String getCurrentPlayerName() {
		return currentPlayer.getCharacterName() + " (" + currentPlayer.getName() + ")";
	}
	
	public void nextTurn() {
		if (currentPlayer==morty) currentPlayer = rick;
		else currentPlayer = morty;
	}

	public boolean getGameEnd() {
		return (seeds>0);
	}
	
	public String endGame() {
		morty.calculateFinalScore();
		rick.calculateFinalScore();
		Player winner= rick.getScore()>morty.getScore()? rick:morty;
		String s = "El ganador es: " + winner.getName() + " Puntaje: " + winner.getScore();
		boolean flag = true;
		for (int i =0; i<scoreRegistry.size();i++) {
			if (scoreRegistry.get(i).getName().equals(winner.getName())) {
				scoreRegistry.get(i).addScoreboard(winner.getScore());
				flag = false;
				s+="\n"+"Tu puntaje esta en el top 5";
				break;
			}
		}
		
		if (flag) {
			if (scoreRegistry.size()==5) {
				if (winner.getScore()>scoreRegistry.get(0).getScore()) {
					scoreRegistry.remove(0);
					scoreRegistry.add(winner);
					s+="\n"+"Tu puntaje esta en el top 5";
				}
			}else {
				scoreRegistry.add(winner);
				s+="\n"+"Tu puntaje esta en el top 5";
			}
		}
		
		Collections.sort(scoreRegistry);
		saveScore();
		return s;
		
	}
	
	public void saveScore() {
		
		 try {
			 	File file = new File(".\\files\\dataScore.obj");
	        	FileOutputStream fos = new FileOutputStream(file);
		        ObjectOutputStream oos = new ObjectOutputStream(fos);
		        oos.writeObject(scoreRegistry);
		        oos.close();
		        fos.close();
			    
		    }
		    catch (IOException ioe) {
		    	ioe.printStackTrace();
		    }
		
	}
	
	public String getScoreBoard() {
		String board = "Top 5: \n";
		int j =1;
		for (int i=scoreRegistry.size()-1; i>-1;i--) {
			board += j+ ". " + scoreRegistry.get(i).getName()+": "+scoreRegistry.get(i).getScore()+"\n";
			j++;
		}	
		return board;
	}

	public void startTime() {
		turnTime= LocalTime.now();
	}

	public void endTime() {
		LocalTime turnTime1= LocalTime.now();
		long finalTime=turnTime.until(turnTime1, ChronoUnit.SECONDS);
		currentPlayer.addTime((int) finalTime);
	}
	
	
	
}
