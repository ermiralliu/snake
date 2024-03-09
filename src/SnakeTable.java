import java.awt.Color;
import javax.swing.*;
import java.awt.GridLayout;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeTable implements KeyListener{
	private int headX;
	private int headY;
	private int directionX;
	private int directionY;
	
	private JFrame frame = new JFrame("Snake Game");
	private Color defaultColor = new Color(20,5,10);
	private Color snakeColor = new Color(255,255,0);
	private Color foodColor = Color.RED;
	private  ArrayList<JPanel> Snake;
	private static Random rand = new Random();
	private int currentHead;
	private JPanel[][] panel = new JPanel[40][40];
	
	boolean foodTakenLastTurn;
	private int score;
	private boolean lose =false;
	
	public SnakeTable() {
		frame.setLayout(new GridLayout());
		frame.setSize(815, 838);
		frame.setResizable(false);
		frame.setLayout(new GridLayout(40, 40));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		for(int i=0; i<40; i++)
			for(int j=0; j<40; j++)
				panel[i][j]= new JPanel();
		initialize();
		frame.setVisible(true);
		frame.addKeyListener(this);
		update();
	}
	
	public void initialize() {
		Snake = new ArrayList<>();
		JPanel[] tailPanel =new JPanel[4];
		foodTakenLastTurn = false;
		currentHead = 4;
		score = 0;
		for(int i=0; i<40; i++) {
			for(int j=0; j<40; j++) {
				panel[i][j].setBackground(defaultColor);
				frame.add(panel[i][j]);
			}
		}
		headX= 5 + rand.nextInt(31);
		headY= 5 + rand.nextInt(31);
		panel[headY][headX].setBackground(snakeColor);
		int tailX = rand.nextInt(3)-1;
		directionX = -tailX;
		if(tailX ==0) {
			int tailY = rand.nextInt(2);
			if(tailY ==0) {
				for(int i=0;i<4;i++)
					tailPanel[i] = panel[headY-i-1][headX];
				
				directionY= 1;
			}		
			else {
				for(int i=0;i<4;i++)
					tailPanel[i] = panel[headY+i+1][headX];
				directionY= -1;
			}
				
		}
		else {
			for(int i=0;i<4;i++)
				tailPanel[i] = panel[headY][headX+(i)*tailX];
			directionY=0;
		}
		for(int i=0;i<4;i++) {
			tailPanel[i].setBackground(snakeColor);
			Snake.add(tailPanel[i]);
		}
		Snake.add(panel[headY][headX]);
	}
	
	public void update(){
		while(true) {
			try {
				Thread.sleep(80);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}
			foodGen();
			if(!foodTakenLastTurn)
				normalTurn();
			else
				sheAte();
			if(lose) {
				lose();
				return;
			}
		}
	}
	
	public void lose() {
		System.out.println("Final Score:"+  score);
		lose = false;
		JFrame frame2 = new JFrame();
		frame2.setSize(600, 300);
		frame2.add(new JLabel("Your Score: " + score, 0));
		frame2.setVisible(true);
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		frame2.dispose();
		initialize();
		update();
	}
	
	public void sheAte() {
		foodTakenLastTurn = false;
		if(headY + directionY >= 40)
			headY = -2 + directionY;
		else if(headY + directionY <= -1)
			headY = 41+ directionY;
		if(headX + directionX >= 40)
			headX = -2 + directionX;
		else if(headX + directionX <= -1)
			headX = 41 +directionX;
		if(panel[headY+=directionY][headX+=directionX].getBackground() == foodColor) {
			score ++;
			foodTakenLastTurn = true;
		}
		else if(panel[headY][headX].getBackground() == snakeColor) {
			lose = true;
		}
		panel[headY][headX].setBackground(snakeColor);
		Snake.add(currentHead+1, panel[headY][headX]);
	}
	
	public void normalTurn() {
		if( --currentHead< 0)
			currentHead = Snake.size()-1;
		Snake.get(currentHead).setBackground(defaultColor);
		if(headY + directionY >= 40)
			headY = -2 + directionY;
		else if(headY + directionY <= -1)
			headY = 41+ directionY;
		if(headX + directionX >= 40)
			headX = -2 + directionX;
		else if(headX + directionX <= -1)
			headX = 41 +directionX;
		if(panel[headY += directionY][headX += directionX].getBackground() == foodColor) {
			score ++;
			foodTakenLastTurn = true;
		}
		else if(panel[headY][headX].getBackground() == snakeColor) {
			lose = true; 
		}
			
		panel[headY][headX].setBackground(snakeColor);
		Snake.set(currentHead, panel[headY][headX]);
	}
	
	public void foodGen() {
			int a = rand.nextInt(40);
			int b = rand.nextInt(40);
			if(panel[a][b].getBackground() == defaultColor && rand.nextInt(40) <= 3)
				panel[a][b].setBackground(foodColor);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		switch(e.getKeyChar()) {
		case 'a':
			if(directionX!=1) {
				directionX=-1;
				directionY=0;
			}
			break;
		case 's':
			if(directionY!=-1) {
				directionX=0;
				directionY=1;
			}
			break;
		case 'd':
			if(directionX!=-1) {
				directionX=1;
				directionY=0;
			}
			break;
		case 'w':
			if(directionY!=1) {
				directionX=0;
				directionY=-1;
			}
			break;
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {}
}
