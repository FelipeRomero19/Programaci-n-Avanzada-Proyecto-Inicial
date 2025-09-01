package sia;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws NumberFormatException, IOException {
		Sistema sistema = new Sistema();
		Menu menu = new Menu(sistema);
		menu.mostrarMenu();
	}
	

}
