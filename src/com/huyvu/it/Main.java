package com.huyvu.it;

import java.util.Scanner;

public class Main {
public static void main(String[] args) {
	System.out.print("Nhập số lượng đĩa: ");
	int n = new Scanner(System.in).nextInt();
	Game game = new Game(n);
	game.run();
	
}
}
