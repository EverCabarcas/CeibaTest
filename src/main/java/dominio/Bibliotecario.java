package dominio;

import java.util.Date;

import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

public class Bibliotecario {

	public static final String EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE = "El libro no se encuentra disponible";
	public static final String LIBROS_PALINDROMOS_NO_PUEDEN_PRESTARSE = "los libros palíndromos solo se pueden utilizar en la biblioteca";

	private RepositorioLibro repositorioLibro;
	private RepositorioPrestamo repositorioPrestamo;

	public Bibliotecario(RepositorioLibro repositorioLibro, RepositorioPrestamo repositorioPrestamo) {
		this.repositorioLibro = repositorioLibro;
		this.repositorioPrestamo = repositorioPrestamo;

	}

	public void prestar(String isbn, String nombreUsuario, int diasPrestamo ) throws Exception {
		
    //Un ISBN no se puede prestar más de una vez
		
		if(esPrestado(isbn)){
			throw new PrestamoException(EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE);
		}
			
			//Comprobamos que no sean palindromos
			
			if(esPalindromo(isbn))
				 throw new PrestamoException(LIBROS_PALINDROMOS_NO_PUEDEN_PRESTARSE);
		
			//Búscamos el libro
			 
			 Libro libro;
			 libro = repositorioLibro.obtenerPorIsbn(isbn);
			 
			 if(libro == null)
				 throw new Exception("El libro no se ha encontrado");
			 
			//Creamos el prestamo
			 Date fecha = new Date();
			 
			 Prestamo prestamo = new Prestamo(
					 fecha,
					 libro,
					 calcularFechaEntrega(isbn,diasPrestamo, fecha),
					 nombreUsuario
					 );
			 
			 //Agregamos el prestamos al repositorio
			 repositorioPrestamo.agregar(prestamo);
		

	}

	public boolean esPrestado(String isbn) {
		
		Libro libro = this.repositorioPrestamo.obtenerLibroPrestadoPorIsbn(isbn);

		if(libro == null){
			return false;
		}
		return true;
		
	}
	
	public boolean esPalindromo(String isbn){
		//Creamos la variable que tendrá el ISBN invertido
		StringBuilder isbnInvert = new StringBuilder(isbn);
		
		//Ahora lo invertimos en la misma variable
		isbnInvert.reverse();
		
		//Comprobamos si es palidroma
		if(isbn.equals(isbnInvert.toString()))
			return true;
		
		return false;
	}
	
	public Date calcularFechaEntrega(String isbn, int diasPrestamo, Date fecha){
		
		String [] caracteres = isbn.split("");
		int suma = 0;
		
			for(int i = 0; i< caracteres.length; i++){
				
				if( Character.isDigit(caracteres[i].charAt(0))){
					suma += Integer.parseInt(caracteres[i]);
				}
			}
			
			LocalDate fechaActual = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();;
			LocalDate fechaInicial = fechaActual;
			LocalDate fechaFinal = fechaInicial.plusDays(diasPrestamo);
			 
			long numeroDomingos;
			long diasExtra = 0;
			
			//Cuando la suma de los digitos de ISBN se ha mayor a 30
			if(suma <= 30 || diasPrestamo > 15)
				return null;
			
			do{
					
				numeroDomingos = domingosEntreFechas(fechaInicial, fechaFinal);
					
				fechaInicial = fechaFinal;
				fechaFinal =  fechaInicial.plusDays(numeroDomingos);
					
			}while(numeroDomingos!=0);		
				
			//Cálculamos la fecha de entrega
			LocalDate fechaentrega = fechaFinal.minusDays(1);
				
			return Date.from(fechaentrega.atStartOfDay(ZoneId.systemDefault()).toInstant());	
			
				
	}
	
	public long domingosEntreFechas(LocalDate first, LocalDate last) {
	    if (last.isBefore(first)) {
	        throw new IllegalArgumentException("Fecha inicial " + first + " Esta antes de la final " + last);
	    }
	    // Encuentra el primer domingo del intervalo
	    LocalDate firstSunday = first.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
	    // Encuentra el último domingo
	    LocalDate lastSunday = last.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY));
	    
	    long number = ChronoUnit.WEEKS.between(firstSunday, lastSunday);
	    // cuenta los domingos encontrados
	    return number + 1;
	}

}
