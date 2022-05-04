package ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.concurrent.TimeUnit;
import model.GameController;


public class Main {
	private BufferedReader in;
	private BufferedWriter out;
	private GameController back;

	public Main() throws IOException {
		in = new BufferedReader(new InputStreamReader(System.in));
		out = new BufferedWriter(new OutputStreamWriter(System.out));
		println("  ._,-,_.          _    ________    _       ______    __\r\n"
				+ "  ||| |||         / \\  |__    __|  / \\     |   _  \\  |  |\r\n"
				+ "  ||| |||        / . \\    |  |    / . \\    |  |_) /  |  |\r\n"
				+ "  ;|| ||:       / /_\\ \\   |  |   / /_\\ \\   |     (   |  |\r\n"
				+ "./ /| |\\ \\.    /  ___  \\  |  |  /  ___  \\  |  |\\  \\  |  |\r\n"
				+ "|./ :_: \\.|   /__/   \\__\\ |__| /__/   \\__\\ |__| \\__\\ |__|\n"
				+ "\nRICK AND MORTY DELUXE EDITION -- A COMMANDLINE ADVENTURE\n"
				+ "\n (x) Start game\n"
				+ " (S) Change language\n"
				+ " (z) Install HD texturepack\n"
				+ " (c) Buy Ricks's Revenge DLC\n");
	}

	public static void main(String[] args) throws IOException, InterruptedException{
		Main main = new Main();
		main.ask();
	}
	
	public void ask() throws IOException, InterruptedException {
		switch(in.readLine()) {
		case "x":
			setup();
			break;
		default:
			println("...");
			TimeUnit.SECONDS.sleep(2);
			println(":(");
			TimeUnit.SECONDS.sleep(2);
			println("You live in a simulation\n\n");
			TimeUnit.SECONDS.sleep(3);
			setup();
		}
	}

	public void setup() throws IOException {
		int rows = readInteger("Digite el numero de filas:", 2);
		int columns = readInteger("Digite el numero de columnas:", 2);
		int condition=(columns*rows)/2;
		int seeds = readInteger("Digite la cantidad de semillas:", 0, condition);
		int links = readInteger("Digite la cantidad de enlaces:", 0, condition-2);
		println("Digite el nombre del jugador 1:");
		String player1Name = in.readLine();
		println("Digite el nombre del jugador 2:");
		String player2Name = in.readLine();
		back = new GameController(rows, columns, seeds, links, player1Name, player2Name);
		start();
	}

	private int readInteger(String message, int min) {
		println(message);
		boolean flag = true;
		int input=0;
		while (flag) {
			try {
				input = Integer.parseInt(in.readLine());
				while (input<min) {
					println("El numero debe ser como minimo " + min);
					input = Integer.parseInt(in.readLine());
				}
				flag = false;
			} catch (NumberFormatException e) {
				flag = true;
				println("Debes digitar un numero");
				//e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		return input;
	}

	private int readInteger(String message, int min, int max) {
		println(message);
		boolean flag = true;
		int input=0;
		while (flag) {
			try {
				input = Integer.parseInt(in.readLine());
				while (input<min || input>max) {
					println("El numero debe ser como minimo " + min + " y como maximo " + max);
					input = Integer.parseInt(in.readLine());
				}
				flag = false;
			} catch (NumberFormatException e) {
				flag = true;
				println("Debes digitar un numero");
				//e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}
		return input;
	}

	public void start() throws NumberFormatException, IOException{
		while (back.getGameEnd()) {
			showMenu();
		}
		println(back.endGame());
		String message = "Menu Final:\n" + "1. Mostrar top 5\n" + "2. Salir";
		int option = readInteger(message, 1, 2);
		switch (option) {
		case 1:
			println(back.getScoreBoard());
			break;
		case 2:
			break;
		}
	}

	public void showMenu() throws NumberFormatException, IOException{
		back.startTime();
		String message = "Es el turno de " + back.getCurrentPlayerName() + " ¿Que deseas hacer?\n"
		+ "1. Tirar dado (acabara tu turno)\n" + "2. Ver tablero\n" + "3. Ver enlaces\n" + "4. Ver Marcador";
		int option = readInteger(message, 1, 4);
		switch (option) {
		case 1:
			throwDice();
			back.nextTurn();
			break;
		case 2:
			showBoard();
			break;
		case 3:
			showLinks();
			break;
		case 4:
			showScoreboard();
			break;
		}

		println("");
	}

	public void throwDice(){
		int dice = back.throwDice();
		String message = "Sacaste un " + dice + "\nPuedes...\n1. Mover hacia adelante\n2. Mover hacia atras";
		int direction = readInteger(message, 1, 2);
		print(back.movePlayer(dice, direction));
		back.endTime();
	}

	public void println(String s){
		try {
			out.write(s + "\n");
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	public void print(String s){
		try {
			out.write(s);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showScoreboard(){
		println(back.getScoreboard());
	}

	public void showBoard() {
		println(back.getBoardSeedsData());
	}

	public void showLinks() {
		println(back.getBoardLinksData());
	}
}
