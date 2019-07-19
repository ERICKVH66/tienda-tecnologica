package dominio;

import dominio.repositorio.RepositorioProducto;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

public class Vendedor {

	public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
	public static final String EL_PRODUCTO_NO_TIENE_GARANTIA = "Este producto no cuenta con  garantia extendida";
	public static final String EL_PRODUCTO_NO_EXITE = "Este producto no existe";

	private RepositorioProducto repositorioProducto;
	private RepositorioGarantiaExtendida repositorioGarantia;

	public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
		this.repositorioProducto = repositorioProducto;
		this.repositorioGarantia = repositorioGarantia;

	}

	public void generarGarantia(String codigo, String nombreCliente) {

		Producto producto = buscarProducto(codigo);

		if (tieneGarantia(codigo))
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);

		contarNumeroVocales(codigo);
		Calendar fechaSolicitudGarantia = new GregorianCalendar();
		double precioGarantia = calcularPrecioGarantia(producto.getPrecio());
		Date fechaFinGarantia = calcularFechaFinalGarantiaExtendida(fechaSolicitudGarantia, producto.getPrecio());

		GarantiaExtendida garantia = new GarantiaExtendida(producto, codigo, producto.getPrecio(),
				fechaSolicitudGarantia, fechaFinGarantia, precioGarantia, nombreCliente);

		repositorioGarantia.agregar(garantia);

	}

	public boolean tieneGarantia(String codigo) {

		return repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo) != null;
	}

	public Producto buscarProducto(String codigo) {

		Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
		if (producto == null)
			throw new GarantiaExtendidaException(EL_PRODUCTO_NO_EXITE);

		return producto;
	}

	public void contarNumeroVocales(String codigo) {
		int contadorVocales = 0;
		char[] arregloCaracteres = codigo.toLowerCase().toCharArray();
		for (int i = 0; i < arregloCaracteres.length; i++) {
			if (arregloCaracteres[i] == 'a' || arregloCaracteres[i] == 'e' || arregloCaracteres[i] == 'i'
					|| arregloCaracteres[i] == 'o' || arregloCaracteres[i] == 'u') {
				contadorVocales++;
			}
		}

		if (contadorVocales == 3)
			throw new GarantiaExtendidaException(Vendedor.EL_PRODUCTO_NO_TIENE_GARANTIA);
	}

	public double calcularPrecioGarantia(double precio) {

		return (precio > 500000) ? precio * 0.2 : precio * 0.1;
	}

	public Date calcularFechaFinalGarantiaExtendida(Calendar fecha, double precio) {

		Calendar fechaNueva =  (Calendar) fecha.clone();
		int dias = 0;
		if (precio > 500000) {
			do {
				if (fechaNueva.getTime().getDay() == 2) {
					fechaNueva.add(Calendar.DATE, 2);
					dias++;
				} else {

					fechaNueva.add(Calendar.DATE, 1);
					dias++;
				}
			} while (dias <= 199);
			if (fechaNueva.getTime().getDay() == 1) {
				fechaNueva.add(Calendar.DATE, 1);
			}
		} else {
			fechaNueva.add(Calendar.DATE, 100);
		}

		return fechaNueva.getTime();
	}

}
