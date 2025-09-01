package sia;
import java.io.*;
import java.util.HashMap;

public class Menu {
    private Sistema sistema;
    private BufferedReader bf;

    public Menu(Sistema sistema) {
        this.sistema = sistema;
        this.bf = new BufferedReader(new InputStreamReader(System.in));
    }

    public void mostrarMenu() throws IOException {
        int opcion = -1;
        bienvenida();

        do {
            mostrarOpciones();
            opcion = leerOpcion();

            switch (opcion) {
                case 1:
                    agregarProyecto();
                    break;
                case 2:
                    agregarPublicacion();
                    break;
                case 3:
                    agregarInvestigador();
                    break;
                case 4:
                    listarProyectos();
                    break;
                case 5:
                    if (confirmarSalida()) {
                        despedida();
                        break;
                    } else {
                        opcion = -1; // para que no salga del menú
                        break;
                    }
                default:
                    System.out.println("⚠ Opción inválida. Por favor elija una opción del menú.");
                    mostrarAyuda();
            }
        } while (opcion != 5);
    }

    private void bienvenida() {
        System.out.println("==============================================");
        System.out.println("    ¡Bienvenido al Sistema de Proyectos!");
        System.out.println("==============================================");
    }

    private void despedida() {
        System.out.println("\n¡Gracias por usar el sistema! Hasta pronto.");
    }

    private void mostrarOpciones() {
        System.out.println("\n----------------------------------------------");
        System.out.println("**** MENÚ PRINCIPAL DEL SISTEMA DE PROYECTOS ****");
        System.out.println("1. Agregar Proyecto");
        System.out.println("2. Agregar publicación a un proyecto");
        System.out.println("3. Agregar investigador a un proyecto");
        System.out.println("4. Listar Proyectos");
        System.out.println("5. Salir");
        System.out.println("----------------------------------------------");
    }

    private void mostrarAyuda() {
        System.out.println("\nOpciones disponibles:");
        System.out.println("1: Agregar un nuevo proyecto");
        System.out.println("2: Agregar una publicación a un proyecto existente");
        System.out.println("3: Agregar un investigador a un proyecto existente");
        System.out.println("4: Listar todos los proyectos y sus detalles");
        System.out.println("5: Salir del sistema");
    }

    private int leerOpcion() throws IOException {
        int opcion = -1;
        boolean valido = false;
        while (!valido) {
            System.out.print("Elija una opción (1-5): ");
            String entrada = bf.readLine();
            if (entrada == null || entrada.trim().isEmpty()) {
                System.out.println("El campo no puede estar vacío. Ingrese un número entre 1 y 5, o escriba 'salir' para volver al menú.");
                continue;
            }
            if (entrada.equalsIgnoreCase("salir")) {
                return 5;
            }
            if (esEntero(entrada)) {
                opcion = Integer.parseInt(entrada);
                if (opcion >= 1 && opcion <= 5) {
                    valido = true;
                } else {
                    System.out.println("Debe ingresar un número entre 1 y 5.");
                }
            } else {
                System.out.println("Por favor ingrese un número válido.");
            }
        }
        return opcion;
    }

    private boolean confirmarSalida() throws IOException {
        System.out.print("¿Está seguro que desea salir? (s/n): ");
        String respuesta = bf.readLine();
        return respuesta.equalsIgnoreCase("s") || respuesta.equalsIgnoreCase("sí") || respuesta.equalsIgnoreCase("si");
    }

    // Función para pedir datos no vacíos, con opción de salir dentro del proceso
    private String pedirDatoNoVacio(String mensaje) throws IOException {
        String dato;
        while (true) {
            System.out.print(mensaje);
            dato = bf.readLine();
            if (dato == null || dato.trim().isEmpty()) {
                System.out.println("⚠ El campo no puede estar vacío. Intente de nuevo o escriba 'salir' para cancelar.");
                continue;
            }
            if (dato.equalsIgnoreCase("salir")) return null;
            return dato.trim();
        }
    }

    private void agregarProyecto() throws IOException {
        System.out.println("\n**** AGREGAR PROYECTO ****");
        String id = pedirDatoNoVacio("ID del proyecto (Ej: P-1919): ");
        if (id == null) return;
        String nombre = pedirDatoNoVacio("Nombre del proyecto (Ej: Sistema Solar): ");
        if (nombre == null) return;
        String descripcion = pedirDatoNoVacio("Descripción (Ej: Proyecto de astronomía para estudiar planetas): ");
        if (descripcion == null) return;
        String subRama = pedirDatoNoVacio("Subrama de la carrera (Ej: Ciberseguridad, Astronomía): ");
        if (subRama == null) return;
        Double fondos = leerDouble("Fondos (ingrese valores numéricos, Ej: 25000.50): ");
        if (fondos == null) return;

        boolean agregado = sistema.agregarProyecto(id, nombre, descripcion, fondos, subRama);
        if (agregado) System.out.println("✅ Proyecto agregado correctamente.");
        else System.out.println("❌ No se pudo agregar el proyecto. Verifique si el ID ya existe.");
    }

    private Double leerDouble(String mensaje) throws IOException {
        while (true) {
            System.out.print(mensaje);
            String entrada = bf.readLine();
            if (entrada == null || entrada.trim().isEmpty()) {
                System.out.println("⚠ El campo no puede estar vacío. Intente de nuevo o escriba 'salir' para cancelar.");
                continue;
            }
            if (entrada.equalsIgnoreCase("salir")) return null;
            if (esDouble(entrada)) {
                return Double.parseDouble(entrada);
            } else {
                System.out.println("⚠ Fondos inválidos. Debe ser un valor numérico, por ejemplo: 25000.50");
            }
        }
    }

    private void agregarPublicacion() throws IOException {
        System.out.println("\n**** AGREGAR PUBLICACIÓN ****");
        String idProyecto = pedirDatoNoVacio("ID del proyecto al que agregar la publicación (Ej: P-1912): ");
        if (idProyecto == null) return;

        Proyecto proyecto = sistema.buscarProyecto(idProyecto);
        if (proyecto == null) {
            System.out.println("❌ No existe un proyecto con ese ID.");
            return;
        }

        String idPub = pedirDatoNoVacio("ID de la publicación (Ej: PUB-0019): ");
        if (idPub == null) return;
        String titulo = pedirDatoNoVacio("Título de la publicación (Ej: Nuevos avances en IA): ");
        if (titulo == null) return;
        Integer año = leerInt("Año de publicación (Ej: 2025): ");
        if (año == null) return;
        String tipo = pedirDatoNoVacio("Tipo de publicación (Ej: artículo, tesis, conferencia): ");
        if (tipo == null) return;

        Publicacion pub = new Publicacion(idPub, titulo, año, tipo);

        if (proyecto.getInvestigadores().isEmpty()) {
            System.out.println("❌ No puedes agregar publicaciones: este proyecto no tiene investigadores.");
            return;
        }

        System.out.println("Investigadores disponibles en el proyecto:");
        for (Investigador inv : proyecto.getInvestigadores()) {
            System.out.println(" - ID: " + inv.getIdInvestigador() + " | Nombre: " + inv.getNombre());
        }
        System.out.println("Ingrese el ID de cada autor que participó en la publicación.");
        System.out.println("Escriba 'exit' cuando termine de agregar autores, o 'salir' para cancelar toda la operación.");

        while (true) {
            String idAutor = pedirDatoNoVacio("ID del autor (o 'exit' para terminar): ");
            if (idAutor == null) return;
            if (idAutor.equalsIgnoreCase("exit")) break;

            Investigador autor = proyecto.buscarInvestigador(idAutor);
            if (autor == null) {
                System.out.println("Ese investigador no pertenece al proyecto. Intente con otro ID o escriba 'exit' para terminar.");
            } else {
                pub.agregarAutor(autor);
                System.out.println("Autor agregado exitosamente: " + autor.getNombre());
            }
        }

        boolean agregado = sistema.agregarPublicacion(idProyecto, pub);
        if (agregado)
            System.out.println("✅ Publicación agregada correctamente con sus autores.");
        else
            System.out.println("❌ No se pudo agregar la publicación. Verifique si el ID ya existe.");
    }

    private Integer leerInt(String mensaje) throws IOException {
        while (true) {
            System.out.print(mensaje);
            String entrada = bf.readLine();
            if (entrada == null || entrada.trim().isEmpty()) {
                System.out.println("⚠ El campo no puede estar vacío. Intente de nuevo o escriba 'salir' para cancelar.");
                continue;
            }
            if (entrada.equalsIgnoreCase("salir")) return null;
            if (esEntero(entrada)) {
                return Integer.parseInt(entrada);
            } else {
                System.out.println("⚠ Dato inválido. Debe ser un número entero, por ejemplo: 2025");
            }
        }
    }

    private void agregarInvestigador() throws IOException {
        System.out.println("\n**** AGREGAR INVESTIGADOR ****");
        String idProyecto = pedirDatoNoVacio("ID del proyecto al que agregar el investigador (Ej: P-1912): ");
        if (idProyecto == null) return;
        if (sistema.buscarProyecto(idProyecto) == null) {
            System.out.println("❌ No existe un proyecto con ese ID.");
            return;
        }
        String idInv = pedirDatoNoVacio("ID del investigador (Ej: INV-0001): ");
        if (idInv == null) return;
        String nombre = pedirDatoNoVacio("Nombre y Apellido (Ej: Juan Pérez): ");
        if (nombre == null) return;
        String especialidad = pedirDatoNoVacio("Especialidad (Ej: Machine Learning, Astrofísica): ");
        if (especialidad == null) return;
        String carrera = pedirDatoNoVacio("Carrera (Ej: Ingeniería en Sistemas): ");
        if (carrera == null) return;

        boolean agregado = sistema.agregarInvestigador(idProyecto, idInv, nombre, especialidad, carrera);
        if (agregado) System.out.println("✅ Investigador agregado correctamente.");
        else System.out.println("❌ No se pudo agregar al investigador. Verifique si el ID ya existe o si el proyecto es válido.");
    }

    public void listarProyectos() {
        HashMap<String, Proyecto> proyectos = sistema.getProyectos();

        if (proyectos.isEmpty()) {
            System.out.println("❌ No hay proyectos registrados en el sistema.");
            return;
        }

        System.out.println("\n==== Listado de proyectos registrados (" + proyectos.size() + ") ====");
        for (Proyecto proyecto : proyectos.values()) {
            System.out.println("----------------------------------------------");
            System.out.println("ID Proyecto: " + proyecto.getIdProyecto());
            System.out.println("Nombre: " + proyecto.getNombre());
            System.out.println("Descripción: " + proyecto.getDescripcion());
            System.out.println("SubRama: " + proyecto.getSubRamaCarrera());
            System.out.println("Fondos: $" + proyecto.getFondos());

            // Investigadores
            System.out.println("\nInvestigadores del Proyecto:");
            if (proyecto.getInvestigadores().isEmpty()) {
                System.out.println("   * Ningún investigador asignado.");
            } else {
                for (Investigador inv : proyecto.getInvestigadores()) {
                    System.out.println("   - " + inv.getNombre() + " (" + inv.getEspecialidad() + ")");
                }
            }

            // Publicaciones
            System.out.println("\nPublicaciones del Proyecto:");
            if (proyecto.getPublicaciones().isEmpty()) {
                System.out.println("   * Ninguna publicación registrada.");
            } else {
                for (Publicacion pub : proyecto.getPublicaciones()) {
                    System.out.println("   - Título: " + pub.getTitulo() + " (" + pub.getAño() + ")");
                    System.out.println("     Tipo: " + pub.getTipo());

                    // Mostrar autores de las publicaciones
                    System.out.println("     Autores:");
                    if (pub.getAutores().isEmpty()) {
                        System.out.println("        * Sin autores asignados.");
                    } else {
                        for (Investigador autor : pub.getAutores()) {
                            if (proyecto.getInvestigadores().contains(autor)) {
                                System.out.println("        - " + autor.getNombre());
                            } else {
                                System.out.println("        - " + autor.getNombre() + " (⚠ no pertenece al proyecto)");
                            }
                        }
                    }
                }
            }
            System.out.println("----------------------------------------------\n");
        }
    }

    // Métodos para validación sencilla

    private boolean esEntero(String texto) {
        if (texto == null || texto.isEmpty()) return false;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (i == 0 && (c == '-' || c == '+')) {
                if (texto.length() == 1) return false;
                else continue;
            }
            if (c < '0' || c > '9') return false;
        }
        return true;
    }

    private boolean esDouble(String texto) {
        if (texto == null || texto.isEmpty()) return false;
        boolean punto = false;
        int start = 0;
        if (texto.charAt(0) == '-' || texto.charAt(0) == '+') start = 1;
        for (int i = start; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == '.') {
                if (punto) return false;
                punto = true;
            } else if (c < '0' || c > '9') {
                return false;
            }
        }
        return start < texto.length(); // debe haber al menos un dígito
    }
}