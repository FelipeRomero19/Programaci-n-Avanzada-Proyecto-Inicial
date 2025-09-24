package sia;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        Menu menu = new Menu(sistema);

        menu.mostrarMenu();

        // guardar sistema cuando el usuario salga
        try {
            sistema.guardarSistema();
            System.out.println("Sistema guardado correctamente al salir.");
        } 
        catch (Exception e) {
            System.err.println("Error al guardar el sistema: " + e.getMessage());
        }
    }
}
