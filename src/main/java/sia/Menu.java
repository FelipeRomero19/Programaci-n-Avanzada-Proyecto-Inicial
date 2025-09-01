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
		int opcion;

		do {
			System.out.println("\n**** SISTEMA DE PROYECTOS ****");
		    System.out.println("1. Agregar Proyecto");
		    System.out.println("2. Agregar publicación a un proyecto");
		    System.out.println("3. Agregar investigador a un proyecto");
		    System.out.println("4. Listar Proyectos");
		    System.out.println("5. Salir.......");
		    System.out.print("Elija una opción: ");
		    
		    opcion = Integer.parseInt(bf.readLine());

		    switch(opcion) {
		    case 1 : {
		    	agregarProyecto();
		    	break;
		    }
		    case 2 : {
		    	agregarPublicacion();
		    	break;
		    }
		    case 3 : {
		    	agregarInvestigador();
		    	break;
		    }
		    case 4 : {
		    	listarProyectos();
		    	break;
		    }
		    case 5 :  {
		    	System.out.println("Saliendo......");
		    	break;
		    }
		    default : System.out.println("Opción Inválida");
		    }
		} while(opcion != 5);
	}
	
	private void agregarProyecto() throws IOException {
		System.out.println("\n**** AGREGAR PROYECTO ****");
		System.out.print("ID del proyecto(Ej: P-1919): ");
		String id = bf.readLine();
		System.out.print("Nombre: ");
		String nombre = bf.readLine();
		System.out.print("Descripción: ");
		String descripcion = bf.readLine();
		System.out.print("Subrama de la carrera(Ej:Ciberseguridad): ");
		String subRama = bf.readLine();
		System.out.print("Fondos(ingresar valores númericos): ");
		double fondos = Double.parseDouble(bf.readLine());
		
		boolean agregado = sistema.agregarProyecto(id, nombre, descripcion, fondos, subRama);
		if(agregado) System.out.println("Proyecto agregado correctamente");
		else System.out.println("No se pudo agregar el proyecto");
		
	}

	private void agregarPublicacion() throws IOException {
		System.out.println("\n**** AGREGAR PUBLICACIÓN ****");
		System.out.print("ID del proyecto(Ej: P-1912): ");
		String idProyecto = bf.readLine();
		
		Proyecto proyecto = sistema.buscarProyecto(idProyecto);
		if(proyecto == null) {
			System.out.println("No existe un proyecto con ese ID.");
			return;
		}
		
		System.out.print("ID de la publicación(Ej: PUB-0019): ");
		String idPub = bf.readLine();
		System.out.print("Título: ");
		String titulo = bf.readLine();
		System.out.print("Año: ");
		int año = Integer.parseInt(bf.readLine());
		System.out.print("Tipo (artículo, tesis, etc): ");
		String tipo = bf.readLine();
		
		Publicacion pub = new Publicacion(idPub, titulo, año, tipo);
		
		if(proyecto.getInvestigadores().isEmpty()) {
			System.out.println("No puedes agregar publicaciones: este proyecto no tiene investigadores");
			return;
		}
		
		System.out.println("Investigadores disponibles en el proyecto");
		for(Investigador inv : proyecto.getInvestigadores()) {
			System.out.println(" - ID: " + inv.getIdInvestigador() + " | Nombre: " + inv.getNombre());
		}
		
		while(true) {
			System.out.print("Ingrese ID de un autor (o ´exit´ para terminar): ");
			String idAutor = bf.readLine();
			if(idAutor.equals("exit")) break;
			
			Investigador autor = proyecto.buscarInvestigador(idAutor);
			if(autor == null) {
				System.out.println("Ese investigador no pertenece al proyecto");
			}
			else {
				pub.agregarAutor(autor);
				System.out.println("Autor agregado exitosamente: " + autor.getNombre());
			}
		}
		
		boolean agregado = sistema.agregarPublicacion(idProyecto, pub);
		if(agregado) 
			System.out.println("Publicación agregada correctamente con sus autores");
		else 
			System.out.println("No se pudo agregar la publicación");
	}
	
	private void agregarInvestigador() throws IOException {
		System.out.println("\n**** AGREGAR INVESTIGADOR ****");
		System.out.print("ID del proyecto: ");
		String idProyecto = bf.readLine();
		if(sistema.buscarProyecto(idProyecto) == null) {
			System.out.println("No existe un proyecto con ese ID");
			return;
		}
		System.out.print("ID del investigador: ");
		String idInv = bf.readLine();
		System.out.print("Nombre y Apellido: ");
		String nombre = bf.readLine();
		System.out.print("Especialidad: ");
		String especialidad = bf.readLine();
		System.out.print("Carrera: ");
		String carrera = bf.readLine();
		
		boolean agregado = sistema.agregarInvestigador(idProyecto, idInv, nombre, especialidad, carrera);
		if(agregado) System.out.println("Investigador agregado correctamente");
		else System.out.println("No se pudo agregar al investigador");
	}
	
	public void listarProyectos() {
	    HashMap<String, Proyecto> proyectos = sistema.getProyectos();

	    if (proyectos.isEmpty()) {
	        System.out.println("No hay proyectos registrados.");
	        return;
	    }

	    for (Proyecto proyecto : proyectos.values()) {
	        System.out.println("*************************************");
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

	                // Mostrar autores de las publicaciones (investigadores del proyecto)
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
	        System.out.println("*************************************\n");
	    }
	}


}
