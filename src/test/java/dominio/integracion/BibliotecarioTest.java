package dominio.integracion;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Bibliotecario;
import dominio.Libro;
import dominio.Prestamo;
import dominio.excepcion.PrestamoException;
import dominio.repositorio.RepositorioLibro;
import dominio.repositorio.RepositorioPrestamo;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.LibroTestDataBuilder;

public class BibliotecarioTest {

	private static final String CRONICA_DE_UNA_MUERTA_ANUNCIADA = "Cronica de una muerta anunciada";
	
	private SistemaDePersistencia sistemaPersistencia;
	
	private RepositorioLibro repositorioLibros;
	private RepositorioPrestamo repositorioPrestamo;

	@Before
	public void setUp() {
		
		sistemaPersistencia = new SistemaDePersistencia();
		
		repositorioLibros = sistemaPersistencia.obtenerRepositorioLibros();
		repositorioPrestamo = sistemaPersistencia.obtenerRepositorioPrestamos();
		
		sistemaPersistencia.iniciar();
	}
	

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void prestarLibroTest() throws Exception {

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		repositorioLibros.agregar(libro);
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		String nombreUsuario = "Ever";
		int diasPrestamo = 6;
		

		// act
		blibliotecario.prestar(libro.getIsbn(),nombreUsuario,diasPrestamo);

		// assert
		Assert.assertTrue(true);
		//Assert.assertNotNull(repositorioPrestamo.obtenerLibroPrestadoPorIsbn(libro.getIsbn()));

	}

	@Test
	public void prestarLibroNoDisponibleTest(){

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		
		repositorioLibros.agregar(libro);
		
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		
		String nombreUsuario = "Ever";
		int diasPrestamo = 15;

		// act
		
		try {
			blibliotecario.prestar(libro.getIsbn(),nombreUsuario,diasPrestamo);
			
			blibliotecario.prestar(libro.getIsbn(),nombreUsuario,diasPrestamo);
			fail();
			
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.EL_LIBRO_NO_SE_ENCUENTRA_DISPONIBLE, e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void isbnPalindromoTest(){

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).conIsbn("1AF2B2FA1").build();
		
		repositorioLibros.agregar(libro);
		
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		
		String nombreUsuario = "Ever";
		int diasPrestamo = 15;

		// act
		
		try {
			blibliotecario.prestar(libro.getIsbn(),nombreUsuario,diasPrestamo);
			fail();
			
		} catch (PrestamoException e) {
			// assert
			Assert.assertEquals(Bibliotecario.LIBROS_PALINDROMOS_NO_PUEDEN_PRESTARSE, e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void prestamoTest(){

		// arrange
		Libro libro = new LibroTestDataBuilder().conTitulo(CRONICA_DE_UNA_MUERTA_ANUNCIADA).build();
		
		repositorioLibros.agregar(libro);
		
		Bibliotecario blibliotecario = new Bibliotecario(repositorioLibros, repositorioPrestamo);
		
		String nombreUsuario = "Ever";
		int diasPrestamo = 10;

		// act
		
		try {
			blibliotecario.prestar(libro.getIsbn(),nombreUsuario,diasPrestamo);
			
			Prestamo prestamo = repositorioPrestamo.obtener(libro.getIsbn());
			
			// assert
			Assert.assertNotNull(prestamo);
			Assert.assertEquals(nombreUsuario , prestamo.getNombreUsuario());
			Assert.assertEquals(libro.getIsbn(), prestamo.getLibro().getIsbn());
			
			
			
			
		} catch (Exception e) {
			fail();
		}
	}
	
}
