package sia;

import java.util.ArrayList;

// anidada
public class Publicacion {
	
	private String idPublicacion; // Numero publicacion por parte del investigador, si es 2 es la segunda publicacion
	private String titulo; 
	private int año; 
	private String tipo; // ej: articulo, tesis, etc
	private ArrayList<Investigador> autores; // Autores de la publicacion (autores dentro del proyecto)

	public Publicacion(String idPublicacion, String titulo, int año, String tipo) {
		this.idPublicacion = idPublicacion;
		this.titulo = titulo;
		this.año = año;
		this.tipo = tipo;
		this.autores = new ArrayList<>();
	}
	
	// Getters y setter
	public String getIdPublicacion() {
		return idPublicacion;
	}
	public void setIdPublicacion(String idPublicacion) {
		this.idPublicacion = idPublicacion;
	}
	
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public int getAño() {
		return año;
	}
	public void setAño(int año) {
		this.año = año;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public void agregarAutor(Investigador autor) {
		autores.add(autor);
	}
	public ArrayList<Investigador> getAutores() {
		return autores;
	}
}
