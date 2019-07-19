package testdatabuilder;

import dominio.Producto;

public class ProductoTestDataBuilder {

	private static final String CODIGO_PRODUCTO= "F01TSA0150";
	private static final String NOMBRE_PRODUCTO = "Computador Lenovo";
	private static final double PRECIO_PRODUCTO = 780000;
	
	private String codigo;
	private String nombre;
	private double precio;
	
	public ProductoTestDataBuilder() {
		this.codigo = CODIGO_PRODUCTO;
		this.nombre = NOMBRE_PRODUCTO;
		this.precio = PRECIO_PRODUCTO;
		
	}

	public ProductoTestDataBuilder conCodigo(String cedula) {
		this.codigo=cedula;
		return this;
	}

	public ProductoTestDataBuilder conNombre(String nombre) {
		this.nombre=nombre;
		return this;
	}

	public ProductoTestDataBuilder conPrecio(double costo) {
		this.precio = costo;
		return this;
	}
	
	
	public Producto build() {
		return new Producto(this.codigo, this.nombre,this.precio);
	}
}
