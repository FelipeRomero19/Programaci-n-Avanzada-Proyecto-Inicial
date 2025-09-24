package sia.persistence;

import sia.Proyecto;
import sia.Investigador;
import sia.Publicacion;
import sia.Sistema;
import sia.exceptions.DuplicatedIdException;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PersistenceCSV {

    private static final String DIR = "data";
    private static final String PROYECTOS_FILE = DIR + "/proyectos.csv";
    private static final String INVESTIGADORES_FILE = DIR + "/investigadores.csv";
    private static final String PUBLICACIONES_FILE = DIR + "/publicaciones.csv";
    
    // Se encarga de revisar si el directorio existe
    private static void ensureDir() throws IOException {
        Path d = Paths.get(DIR);
        if (!Files.exists(d)) Files.createDirectories(d);
    }

    public static void guardarTodo(Sistema sistema) throws IOException {
        ensureDir();
        Map<String, Proyecto> proyectos = sistema.getProyectos();
        guardarProyectos(proyectos);
        guardarInvestigadores(proyectos);
        guardarPublicaciones(proyectos);
        generarReporteTxt(proyectos);
    }

    private static void guardarProyectos(Map<String, Proyecto> proyectos) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PROYECTOS_FILE))) {
        	// Cabecera
            pw.println("idProyecto,nombre,descripcion,subRama,fondos");
            for (Proyecto p : proyectos.values()) {
                pw.printf("%s,%s,%s,%s,%.2f%n",
                        escape(p.getIdProyecto()),
                        escape(p.getNombre()),
                        escape(p.getDescripcion()),
                        escape(p.getSubRamaCarrera()),
                        p.getFondos());
            }
        }
    }

    private static void guardarInvestigadores(Map<String, Proyecto> proyectos) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(INVESTIGADORES_FILE))) {
        	// Cabecera
            pw.println("idInvestigador,nombre,especialidad,carrera,idProyecto");
            for (Proyecto p : proyectos.values()) {
                for (Investigador inv : p.getInvestigadores()) {
                    pw.printf("%s,%s,%s,%s,%s%n",
                            escape(inv.getIdInvestigador()),
                            escape(inv.getNombre()),
                            escape(inv.getEspecialidad()),
                            escape(inv.getCarrera()),
                            escape(p.getIdProyecto()));
                }
            }
        }
    }

    private static void guardarPublicaciones(Map<String, Proyecto> proyectos) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PUBLICACIONES_FILE))) {
        	// Cabecera
            pw.println("idPublicacion,titulo,año,tipo,idProyecto,autores");
            for (Proyecto p : proyectos.values()) {
                for (Publicacion pub : p.getPublicaciones()) {
                    StringJoiner sj = new StringJoiner(";");
                    for (Investigador a : pub.getAutores()) sj.add(a.getIdInvestigador());
                    pw.printf("%s,%s,%d,%s,%s,%s%n",
                            escape(pub.getIdPublicacion()),
                            escape(pub.getTitulo()),
                            pub.getAño(),
                            escape(pub.getTipo()),
                            escape(p.getIdProyecto()),
                            escape(sj.toString()));
                }
            }
        }
    }

    // Carga todo en el sistema -> si no hay archivos, no hace nada (pero no borra inicializarSistema)
    public static void cargarTodo(Sistema sistema) throws IOException, DuplicatedIdException {
        ensureDir();
        Path pProy = Paths.get(PROYECTOS_FILE);
        if (!Files.exists(pProy)) return; // no hay datos guardados
        
        // se cargan los proyectos vacios
        Map<String, Proyecto> proyectosTemp = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PROYECTOS_FILE))) {
            String line = br.readLine(); // cabecera
            while ((line = br.readLine()) != null) {
                String[] parts = splitCSV(line, 5);
                String id = unescape(parts[0]);
                String nombre = unescape(parts[1]);
                String descripcion = unescape(parts[2]);
                String subRama = unescape(parts[3]);
                double fondos = 0.0;
                try {
                	fondos = Double.parseDouble(parts[4]);
                }
                catch (NumberFormatException ex){
                	fondos = 0.0;
                }
                Proyecto p = new Proyecto(id, nombre, descripcion, fondos, subRama);
                proyectosTemp.put(id, p);
            }
        }
        
        // cargar investigadores y asociar a proyectos
        if (Files.exists(Paths.get(INVESTIGADORES_FILE))) {
            try (BufferedReader br = new BufferedReader(new FileReader(INVESTIGADORES_FILE))) {
                String line = br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] parts = splitCSV(line, 5);
                    String idInv = unescape(parts[0]);
                    String nombre = unescape(parts[1]);
                    String especialidad = unescape(parts[2]);
                    String carrera = unescape(parts[3]);
                    String idProyecto = unescape(parts[4]);

                    Proyecto p = proyectosTemp.get(idProyecto);
                    if (p != null) {
                        Investigador inv = new Investigador(idInv, nombre, especialidad, carrera);
                        p.agregarInvestigador(inv);
                    } // si no existe proyecto se ignora la fila
                }
            }
        }
        
        // cargar publicaciones y autores
        if (Files.exists(Paths.get(PUBLICACIONES_FILE))) {
            try (BufferedReader br = new BufferedReader(new FileReader(PUBLICACIONES_FILE))) {
                String line = br.readLine(); // cabecera
                while ((line = br.readLine()) != null) {
                    String[] parts = splitCSV(line, 6);
                    String idPub = unescape(parts[0]);
                    String titulo = unescape(parts[1]);
                    int año = 0;
                    try {
                    	año = Integer.parseInt(parts[2]);
                    }
                    catch (NumberFormatException ex) {
                    	año = 0;
                    }
                    String tipo = unescape(parts[3]);
                    String idProyecto = unescape(parts[4]);
                    String autoresRaw = unescape(parts[5]);

                    Proyecto p = proyectosTemp.get(idProyecto);
                    if (p != null) {
                        Publicacion pub = new Publicacion(idPub, titulo, año, tipo);
                        if (!autoresRaw.isBlank()) {
                            String[] autorIds = autoresRaw.split(";");
                            for (String aid : autorIds) {
                                Investigador inv = p.buscarInvestigador(aid);
                                if (inv != null) pub.agregarAutor(inv);
                            }
                        }
                        p.agregarPublicacion(pub);
                    }
                }
            }
        }

        sistema.reemplazaAll(proyectosTemp);      
    }

    private static void generarReporteTxt(Map<String, Proyecto> proyectos) throws IOException {
        String file = DIR + "/reporte_proyectos.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("REPORTE DE PROYECTOS");
            pw.println("=====================");
            for (Proyecto p : proyectos.values()) {
                pw.println("ID: " + p.getIdProyecto());
                pw.println("Nombre: " + p.getNombre());
                pw.println("SubRama: " + p.getSubRamaCarrera());
                pw.printf("Fondos: %.2f%n", p.getFondos());
                pw.println("Investigadores:");
                for (Investigador inv : p.getInvestigadores()) {
                    pw.println(" - " + inv.getIdInvestigador() + " | " + inv.getNombre() + " | " + inv.getEspecialidad());
                }
                pw.println("Publicaciones:");
                for (Publicacion pub : p.getPublicaciones()) {
                    pw.println(" - " + pub.getIdPublicacion() + " | " + pub.getTitulo() + " (" + pub.getAño() + ") Tipo: " + pub.getTipo());
                }
                pw.println("-----------------------------------");
            }
        }
    }

    private static String escape(String s) {
        if (s == null) return "";
        return s.replace("\"", "\"\"");
    }
    
    private static String unescape(String s) {
        if (s == null) return "";
        return s.replace("\"\"", "\"");
    }

    private static String[] splitCSV(String line, int expected) {
        String[] parts = line.split(",", expected);
        if (parts.length < expected) {
            String[] fixed = new String[expected];
            System.arraycopy(parts, 0, fixed, 0, parts.length);
            for (int i = parts.length; i < expected; i++) fixed[i] = "";
            return fixed;
        }
        return parts;
    }
}
