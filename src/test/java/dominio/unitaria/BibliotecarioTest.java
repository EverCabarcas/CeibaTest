package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import testdatabuilder.LibroTestDataBuilder;

public class BibliotecarioTest {

	@Test
	public void esPrestadoTest() {
		
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();
		
		Libro libro = libroTestDataBuilder.build(); 
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(libro);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act 
		boolean esPrestado =  bibliotecario.esPrestado(libro.getIsbn());
		
		//assert
		assertTrue(esPrestado);
	}
	
	@Test
	public void libroNoPrestadoTest() {
		
		// arrange
		LibroTestDataBuilder libroTestDataBuilder = new LibroTestDataBuilder();
		
		Libro libro = libroTestDataBuilder.build(); 
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		
		when(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn())).thenReturn(null);
		
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo);
		
		// act 
		boolean esPrestado =  bibliotecario.esPrestado(libro.getIsbn());
		
		//assert
		assertFalse(esPrestado);
	}
	
	@Test
	public void esPalindromoTest(){

		// arrange
		String isbn = "1E3BB3E1";
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo); 
		
		// act
		boolean esPalindromo = bibliotecario.esPalindromo(isbn);
		
		//assert
		assertTrue(esPalindromo);
		
	}
	
	@Test
	public void noesPalindromoTest(){

		// arrange
		String isbn = "ABCDEFG1";
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo); 
		
		// act
		boolean esPalindromo = bibliotecario.esPalindromo(isbn);
		
		//assert
		assertFalse(esPalindromo);
		
	}
	
	@Test
	public void domingosEntreFechasTest(){

		// arrange
		LocalDate fechainicial = LocalDate.of(2018, 6, 22);
		LocalDate fechafinal   = LocalDate.of(2018, 7, 7);
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo); 
		
		// act
		long domingos = bibliotecario.domingosEntreFechas(fechainicial,fechafinal);
		
		//assert
		assertEquals(2, domingos);
		
	}
	@Test
	public void calcularFechaEntregaTest(){

		// arrange
		
		LocalDate fechaLocalDate = LocalDate.of(2018, 6, 22);
		
		Date fechaDate = Date.from(fechaLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		String isbn = "A874B69Q";
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo); 
		
		// act
		Date fechaEntrega = bibliotecario.calcularFechaEntrega(isbn, 10, fechaDate);
		
		//assert
		
        LocalDate fechaEsperadaLocalDate = LocalDate.of(2018, 7, 3);
		Date fechaEsperadaDate = Date.from(fechaEsperadaLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		assertEquals(fechaEsperadaDate, fechaEntrega);
		
	}
	
	@Test
	public void sinFechaEntregaTest(){

		// arrange
		
		LocalDate fechaLocalDate = LocalDate.of(2018, 6, 22);
		
		Date fechaDate = Date.from(fechaLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		String isbn = "A74B69Q";
		
		RepositorioPrestamo repositorioPrestamo = mock(RepositorioPrestamo.class);
		RepositorioLibro repositorioLibro = mock(RepositorioLibro.class);
		Bibliotecario bibliotecario = new Bibliotecario(repositorioLibro, repositorioPrestamo); 
		
		// act
		Date fechaEntrega = bibliotecario.calcularFechaEntrega(isbn, 10, fechaDate);
		
		//assert
		
        LocalDate fechaEsperadaLocalDate = LocalDate.of(2018, 7, 3);
		Date fechaEsperadaDate = Date.from(fechaEsperadaLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		assertEquals(null, fechaEntrega);
		
	}
}
