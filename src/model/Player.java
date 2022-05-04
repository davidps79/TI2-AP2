package model;

import java.io.Serializable;

public class Player implements Comparable<Player>, Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private String characterName;
	private int score;
	private Node<Square> position;
	private int time;
	
	public Player(String name, String characterName) {
		this.name = name;
		this.score = 0;
		this.characterName = characterName;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public char getBoardName() {
		return characterName.charAt(0);
	}
	
	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
	
	public Node<Square> getPosition() {
		return position;
	}

	public void setPosition(Node<Square> position) {
		this.position = position;
	}
	
	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void moveForward(int positions) {
		Node<Square> temp = position.movePlayerForward(positions);
		setPosition(temp);
		temp.getItem().addPlayer(this);
	}
	
	public void moveBackward(int positions) {
		Node<Square> temp = position.movePlayerBackward(positions);
		setPosition(temp);
		temp.getItem().addPlayer(this);
	}
	
	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public void addScore() {
		this.score++;
	}
	
	public void addScoreboard(int score) {
		this.score += score;
	}
	
	public void addTime(int time) {
		this.time+= time;
	}

	@Override
	public int compareTo(Player other) {
		return other.getScore()>this.getScore()?-1:1;
	}

	public void calculateFinalScore() {
		score=(score*120)-time;
	}
	
}
