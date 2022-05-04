package model;

import java.io.Serializable;
import java.util.ArrayList;

public class Square implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int number;
	private ArrayList<Player> players;
	private boolean hasSeed;
	private char linkId;
	private int linkMoves;
	
	public Square(int number) {
		this.number = number;
		this.linkMoves = 0;
		this.players = new ArrayList<Player>();
		this.linkId = ' ';
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int number) {
		this.number = number;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public void addPlayer(Player player) {
		players.add(player);
	}
	
	public void removePlayer(Player player) {
		players.remove(player);
	}
	
	public boolean hasSeed() {
		return hasSeed;
	}
	
	public void setSeed(boolean hasSeed) {
		this.hasSeed = hasSeed;
	}
	
	public char getLinkID() {
		return linkId;
	}
	
	public void setLinkId(char linkId) {
		this.linkId = linkId;
	}
	
	public String toStringSeeds() {
		String s = "";
		if (players.size()>0) {
			for (Player p : players) {
				s+=p.getBoardName();
			}
			return s;
		}
		else if (hasSeed) return "*";
		else {
			s = number + "";
			return (s);
		}
	}
	
	public String toStringLinks() {
		return linkId + "";
	}
	
	public int getLinkMoves() {
		return linkMoves;
	}

	public void setLinkMoves(int linkMoves) {
		this.linkMoves = linkMoves;
	}
}
