package dominio.unitaria;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Test;

import dominio.Producto;
import dominio.Vendedor;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;
import dominio.repositorio.RepositorioProducto;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {
	
	private static final String CODIGO_PRODCUTO = "GT456577GHD";
	private static final double PRECIO_PRODUCTO = 200000;
	
	@Test
	public void productoYaTieneGarantiaTest() {
		
		// arrange
		Producto producto = new ProductoTestDataBuilder().build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		// act 
		boolean tieneGarantia = vendedor.tieneGarantia(producto.getCodigo());
		
		//assert
		assertTrue(tieneGarantia);
	}
	
	@Test
	public void productoNoTieneGarantiaTest() {
		
		// arrange
		Producto producto = new ProductoTestDataBuilder().build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		// act 
		boolean existeProducto =  vendedor.tieneGarantia(producto.getCodigo());
		
		//assert
		assertFalse(existeProducto);
	}
	
	@Test
	public void existeProductoTest() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);

		when(repositorioProducto.obtenerPorCodigo(producto.getCodigo())).thenReturn(producto);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		// act
		Producto productoEncontrado = vendedor.buscarProducto(producto.getCodigo());
		
		// assert
		Assert.assertEquals(producto.getNombre(), productoEncontrado.getNombre());
	}
	
	@Test
	public void noExisteProductoTest() {

		// arrange
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);

		when(repositorioProducto.obtenerPorCodigo(CODIGO_PRODCUTO)).thenReturn(null);

		try {
			// act
			vendedor.buscarProducto(CODIGO_PRODCUTO);
			fail();

		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.EL_PRODUCTO_NO_EXITE, e.getMessage());
		}

	}

	@Test
	public void productoConNombreConTresVocalesTest() {

		// arrange
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		try {
			// act
			vendedor.contarNumeroVocales(CODIGO_PRODCUTO);
		} catch (GarantiaExtendidaException e) {
			// assert
			Assert.assertEquals(Vendedor.EL_PRODUCTO_NO_TIENE_GARANTIA, e.getMessage());
		}
	}

	@Test
	public void calcularPrecioGarantiaMayorA500Test() {

		// arrange
		Producto producto = new ProductoTestDataBuilder().build();

		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		double precio = producto.getPrecio() * 0.2;

		// act
		double precioObtenido = vendedor.calcularPrecioGarantia(producto.getPrecio());

		// assert
		Assert.assertEquals(precioObtenido, precio, 0.0);

	}

	@Test
	public void calcularPrecioGarantiaMenorA500Test() {

		// arrange
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		double precio = PRECIO_PRODUCTO * 0.1;

		// act
		double precioObtenido = vendedor.calcularPrecioGarantia(PRECIO_PRODUCTO);

		// assert
		Assert.assertEquals(precioObtenido, precio, 0.0);

	}
	@Test
	public void calcularFechaFinalGarantiaExtendidaMenorTest(){
		
		// arrange
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		Calendar fechaInicial = new GregorianCalendar(2019, Calendar.JANUARY, 1);
		Calendar fechaEsperada = (Calendar) fechaInicial.clone();
		fechaEsperada.add(Calendar.DATE, 100);
		
		// act
		Date fechaObtenida = vendedor.calcularFechaFinalGarantiaExtendida(fechaInicial, PRECIO_PRODUCTO);
		
		//assert
		Assert.assertEquals(fechaEsperada.getTime(), fechaObtenida);
	}
	
	@Test
	public void calcularFechaFinalGarantiaExtendidaMayorTest(){
		
		// arrange
		Producto producto = new ProductoTestDataBuilder().build();
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		Calendar fechaInicial = new GregorianCalendar(2019, Calendar.JANUARY, 1);
		Calendar fechaEsperada = (Calendar) fechaInicial.clone();
		fechaEsperada.add(Calendar.DATE, 234);
		
		// act
		Date fechaObtenida = vendedor.calcularFechaFinalGarantiaExtendida(fechaInicial, producto.getPrecio());
		
		//assert
		Assert.assertEquals(fechaEsperada.getTime(), fechaObtenida);
	}
	
}
