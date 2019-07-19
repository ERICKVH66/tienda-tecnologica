package dominio.integracion;

import static org.junit.Assert.fail;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import dominio.Vendedor;
import dominio.GarantiaExtendida;
import dominio.Producto;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import persistencia.sistema.SistemaDePersistencia;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	private static final String NOMBRE_CLIENTE = "Carlos";
	private static final String FORMATO = "dd/MM/YYYY";
	private static final Calendar FECHA_FINAL = new GregorianCalendar();
	
	private SistemaDePersistencia sistemaPersistencia;
	
	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	@Before
	public void setUp() {
		
		sistemaPersistencia = new SistemaDePersistencia();
		
		repositorioProducto = sistemaPersistencia.obtenerRepositorioProductos();
		repositorioGarantia = sistemaPersistencia.obtenerRepositorioGarantia();
		
		sistemaPersistencia.iniciar();
	}
	

	@After
	public void tearDown() {
		sistemaPersistencia.terminar();
	}

	@Test
	public void generarGarantiaTest() {

		// arrange
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		Producto producto = new ProductoTestDataBuilder().build();
		repositorioProducto.agregar(producto);

		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
		GarantiaExtendida garantia = repositorioGarantia.obtener(producto.getCodigo());
		
		SimpleDateFormat format = new SimpleDateFormat(FORMATO);
		String fechaFinEsperada = format
				.format(vendedor.calcularFechaFinalGarantiaExtendida(FECHA_FINAL, producto.getPrecio()));
		String fechaFinObtenida = format.format(garantia.getFechaFinGarantia());
		double precioObtenido = vendedor.calcularPrecioGarantia(producto.getPrecio());

		// assert
		Assert.assertEquals(producto.getCodigo(), garantia.getProducto().getCodigo());
		Assert.assertEquals(garantia.getNombreCliente(), NOMBRE_CLIENTE);
		Assert.assertEquals(garantia.getPrecioGarantia(), precioObtenido, 0.0);
		Assert.assertEquals(fechaFinEsperada, fechaFinObtenida);

	}

	@Test
	public void productoYaTieneGarantiaTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().build();
		repositorioProducto.agregar(producto);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		// act
		vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
		try {
			
			vendedor.generarGarantia(producto.getCodigo(), NOMBRE_CLIENTE);
			fail();
			
		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.EL_PRODUCTO_TIENE_GARANTIA, e.getMessage());
		}
	}
}
