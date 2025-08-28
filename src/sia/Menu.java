package sia;
import java.io.*;

public class Menu {
	private int opcion;
	private Sistema sistema ;
	private BufferedReader bf ;
	
	public Menu(Sistema sistema) {
		this.sistema = sistema ;	
	}
	
	public int getOpcion() {
		return opcion;
	}
	
	public void setOpcion(int opcion) {
		if(opcion != 0) {
			this.opcion = 0 ;
		}
		else {
			this.opcion = opcion;
		}
	}
	
	public void mostrarMenu() throws NumberFormatException, IOException {
		bf = new BufferedReader(new InputStreamReader(System.in));
		int opcion;

		do {
		    System.out.println("1. Gestión de Investigadores");
		    System.out.println("2. Gestión de Proyectos");
		    System.out.println("3. Gestión de Publicaciones");
		    System.out.println("4. Salir");
		    System.out.print("Elija una opción: ");
		    opcion = Integer.parseInt(bf.readLine()) ;

		    switch(opcion) {
		        case 1:
		        	System.out.println("Ingresaste opcion 1");
		            break;
		        case 2:
		        	System.out.println("Ingresaste opcion 2");
		            break;
		        case 3:
		        	System.out.println("Ingresaste opcion 3");
		            break;
		        case 4:
		            System.out.println("Saliendo...");
		            break;
		        default:
		            System.out.println("Opción inválida.");
		    }
		} while(opcion != 4);
	}

	

}
