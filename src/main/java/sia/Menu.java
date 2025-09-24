package sia;

import java.io.*;
import java.util.*;
import sia.exceptions.DuplicatedIdException;
import sia.exceptions.ProyectoNotFoundException;

public class Menu {
    private final Sistema sistema;
    private final BufferedReader bf;

    public Menu(Sistema sistema) {
        this.sistema = sistema;
        this.bf = new BufferedReader(new InputStreamReader(System.in));
    }

    public void mostrarMenu() {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n=== SISTEMA DE PROYECTOS (SIA) ===");
            System.out.println("1) Agregar Proyecto");
            System.out.println("2) Editar Proyecto");
            System.out.println("3) Eliminar Proyecto");
            System.out.println("4) Listar Proyectos");
            System.out.println("5) Ver Detalle de Proyecto (investigadores / publicaciones)");
            System.out.println("6) Agregar Investigador a Proyecto");
            System.out.println("7) Agregar Publicación a Proyecto");
            System.out.println("8) Eliminar Investigador de Proyecto");
            System.out.println("9) Eliminar Publicación de Proyecto");
            System.out.println("10) Buscar Publicación (global)");
            System.out.println("11) Buscar Investigador (global)");
            System.out.println("12) Filtrar Proyectos por SubRama");
            System.out.println("13) Filtrar Publicaciones por rango de años");
            System.out.println("0) Salir y guardar");
            System.out.print("Elija una opción: ");

            try {
            	String opt = bf.readLine().trim();
                switch (opt) {
                    case "1": opcionAgregarProyecto(); break;
                    case "2": opcionEditarProyecto(); break;
                    case "3": opcionEliminarProyecto(); break;
                    case "4": opcionListarProyectos(); break;
                    case "5": opcionDetalleProyecto(); break;
                    case "6": opcionAgregarInvestigador(); break;
                    case "7": opcionAgregarPublicacion(); break;
                    case "8": opcionEliminarInvestigador(); break;
                    case "9": opcionEliminarPublicacion(); break;
                    case "10": opcionBuscarPublicacionGlobal(); break;
                    case "11": opcionBuscarInvestigadorGlobal(); break;
                    case "12": opcionFiltrarProyectos(); break;
                    case "13": opcionFiltrarPublicaciones(); break;
                    case "0":
                        opcionSalir();
                        salir = true;
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } 
            catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
    
    // Metodos de opciones
    private String leerLinea(String mensaje) throws IOException {
        System.out.print(mensaje);
        return bf.readLine().trim();
    }

    private double leerDouble() {
        try {
            String s = bf.readLine().trim();
            return Double.parseDouble(s);
        } 
        catch (Exception e) {
            System.out.println("Entrada inválida. Se asigna 0.");
            return 0.0;
        }
    }

    private int leerEntero() {
        try {
            String s = bf.readLine().trim();
            return Integer.parseInt(s);
        } 
        catch (Exception e) {
            System.out.println("Entrada inválida. Se asigna 0.");
            return 0;
        }
    }

    private void opcionAgregarProyecto() throws IOException {
    	String id = leerLinea("ID proyecto: ");
        String nombre = leerLinea("Nombre: ");
        String desc = leerLinea("Descripción: ");
        String sub = leerLinea("SubRama: ");
        System.out.print("Fondos (número): ");
        double fondos = leerDouble();
        try {
            boolean ok = sistema.agregarProyecto(id, nombre, desc, fondos, sub);
            System.out.println(ok ? "Proyecto agregado." : "No se pudo agregar proyecto.");
        } 
        catch (DuplicatedIdException e) {
            System.out.println("No se pudo agregar: " + e.getMessage());
        }
    }

    private void opcionEditarProyecto() throws IOException {
    	String id = leerLinea("ID proyecto a editar: ");
        Proyecto p = sistema.buscarProyecto(id);
        if (p == null) {
            System.out.println("Proyecto no encontrado.");
            return;
        }
        String nombre = leerLinea("Nuevo nombre (enter para mantener): ");
        if (nombre.isBlank()) nombre = p.getNombre();
        String desc = leerLinea("Nueva descripción (enter para mantener): ");
        if (desc.isBlank()) desc = p.getDescripcion();
        System.out.print("Nuevos fondos (ingrese número o enter para mantener): ");
        String fondosStr = bf.readLine().trim();
        double fondos = p.getFondos();
        if (!fondosStr.isBlank()) {
            try { 
            	fondos = Double.parseDouble(fondosStr); 
            } 
            catch (NumberFormatException ex) { 
            	System.out.println("Fondos inválidos, se mantiene el anterior."); 
            }
        }
        String sub = leerLinea("Nueva SubRama (enter para mantener): ");
        if (sub.isBlank()) sub = p.getSubRamaCarrera();
        try {
            sistema.editarProyecto(id, nombre, desc, fondos, sub);
            System.out.println("Proyecto actualizado.");
        } 
        catch (ProyectoNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void opcionEliminarProyecto() throws IOException {
        String id = leerLinea("ID proyecto a eliminar: ");
        try {
            sistema.eliminarProyecto(id);
            System.out.println("Proyecto eliminado.");
        } 
        catch (ProyectoNotFoundException e) {
            System.out.println("No se pudo eliminar: " + e.getMessage());
        }
    }

    private void opcionListarProyectos() {
        Map<String, Proyecto> proys = sistema.getProyectos();
        if (proys.isEmpty()) {
            System.out.println("No hay proyectos registrados.");
            return;
        }
        System.out.println("Proyectos:");
        for (Proyecto p : proys.values()) {
            System.out.println(" - " + p);
        }
    }

    private void opcionDetalleProyecto() throws IOException {
        String id = leerLinea("ID proyecto: ");
        Proyecto p = sistema.buscarProyecto(id);
        if (p == null) {
            System.out.println("Proyecto no encontrado.");
            return;
        }
        System.out.println("Proyecto: " + p);
        System.out.println("Investigadores:");
        for (Investigador inv : p.getInvestigadores()) {
            System.out.println("  - " + inv);
        }
        System.out.println("Publicaciones:");
        for (Publicacion pub : p.getPublicaciones()) {
            System.out.println("  - " + pub + " Autores: " + listarIdsAutores(pub));
        }
    }

    private String listarIdsAutores(Publicacion pub) {
        StringJoiner sj = new StringJoiner(", ");
        for (Investigador a : pub.getAutores()) sj.add(a.getIdInvestigador());
        return sj.toString();
    }

    private void opcionAgregarInvestigador() throws IOException {
        String idProj = leerLinea("ID proyecto: ");
        String idInv = leerLinea("ID investigador: ");
        String nombre = leerLinea("Nombre: ");
        String esp = leerLinea("Especialidad: ");
        String carrera = leerLinea("Carrera: ");

        try {
            boolean ok = sistema.agregarInvestigador(idProj, idInv, nombre, esp, carrera);
            System.out.println(ok ? "Investigador agregado." : "No se agregó investigador.");
        } 
        catch (ProyectoNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void opcionAgregarPublicacion() throws IOException {
    	String idProj = leerLinea("ID proyecto: ");
        String idPub = leerLinea("ID publicación: ");
        String titulo = leerLinea("Título: ");
        System.out.print("Año: ");
        int año = leerEntero();
        String tipo = leerLinea("Tipo: ");

        try {
            boolean ok = sistema.agregarPublicacion(idProj, idPub, titulo, año, tipo);
            System.out.println(ok ? "Publicación agregada." : "No se pudo agregar publicación.");
        } 
        catch (ProyectoNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void opcionEliminarInvestigador() throws IOException {
        String idProj = leerLinea("ID proyecto: ");
        String idInv = leerLinea("ID investigador a eliminar: ");
        try {
            boolean ok = sistema.eliminarInvestigador(idProj, idInv);
            System.out.println(ok ? "Investigador eliminado." : "No se pudo eliminar investigador.");
        } 
        catch (ProyectoNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void opcionEliminarPublicacion() throws IOException {
    	String idProj = leerLinea("ID proyecto: ");
        String idPub = leerLinea("ID publicación a eliminar: ");
        try {
            boolean ok = sistema.eliminarPublicacion(idProj, idPub);
            System.out.println(ok ? "Publicación eliminada." : "No se pudo eliminar publicación.");
        } catch (ProyectoNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private void opcionBuscarPublicacionGlobal() throws IOException {
        String idPub = leerLinea("ID publicación a buscar: ");
        Map.Entry<Proyecto, Publicacion> res = sistema.buscarPublicacionGlobal(idPub);
        if (res == null) {
            System.out.println("No se encontró la publicación.");
        } 
        else {
            System.out.println("Encontrada en proyecto: " + res.getKey().getIdProyecto());
            System.out.println("Publicación: " + res.getValue());
        }
    }

    private void opcionBuscarInvestigadorGlobal() throws IOException {
        String idInv = leerLinea("ID investigador a buscar: ");
        Map.Entry<Proyecto, Investigador> res = sistema.buscarInvestigadorGlobal(idInv);
        if (res == null) {
            System.out.println("No se encontró el investigador.");
        } 
        else {
            System.out.println("Encontrado en proyecto: " + res.getKey().getIdProyecto());
            System.out.println("Investigador: " + res.getValue());
        }
    }

    private void opcionFiltrarProyectos() throws IOException {
        String sub = leerLinea("SubRama a filtrar: ");
        List<Proyecto> out = sistema.filtrarProyectosPorSubRama(sub);
        if (out.isEmpty()) System.out.println("No hay proyectos para esa subRama.");
        else {
            System.out.println("Proyectos encontrados:");
            out.forEach(p -> System.out.println(" - " + p));
        }
    }

    private void opcionFiltrarPublicaciones() throws IOException {
        System.out.print("Desde año: ");
        int desde = leerEntero();
        System.out.print("Hasta año: ");
        int hasta = leerEntero();
        List<Publicacion> out = sistema.filtrarPublicacionesPorRangoAños(desde, hasta);
        if (out.isEmpty()) System.out.println("No hay publicaciones en ese rango.");
        else {
            System.out.println("Publicaciones encontradas:");
            out.forEach(pub -> System.out.println(" - " + pub));
        }
    }

    private void opcionSalir() {
        try {
            sistema.guardarSistema();
            System.out.println("Sistema guardado. Saliendo...");
        } 
        catch (IOException e) {
            System.err.println("Error al guardar el sistema: " + e.getMessage());
        }
    }
}
