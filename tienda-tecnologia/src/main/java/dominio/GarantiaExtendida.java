package dominio;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class GarantiaExtendida {

	private Producto producto;
    private String codigoProducto;
    private double precioProducto;
    private Calendar fechaSolicitudGarantia;
    private Date fechaFinGarantia;
    private double precioGarantia;
    private String nombreCliente;

    public GarantiaExtendida(Producto producto) {
        this.fechaSolicitudGarantia = new GregorianCalendar();
        this.producto = producto;
    }

    public GarantiaExtendida(Producto producto, String codigoProducto, double precioProducto, Calendar fechaSolicitudGarantia, Date fechaFinGarantia,
            double precioGarantia, String nombreCliente) {

        this.producto = producto;
        this.codigoProducto = codigoProducto;
        this.precioProducto = precioProducto;
        this.fechaSolicitudGarantia = fechaSolicitudGarantia;
        this.fechaFinGarantia = fechaFinGarantia;
        this.precioGarantia = precioGarantia;
        this.nombreCliente = nombreCliente;
    }

    public Producto getProducto() {
        return producto;
    }
    public String getCodigoProducto() {
		return codigoProducto;
	}
    
    public double getPrecioProducto() {
		return precioProducto;
	}

    public Calendar getFechaSolicitudGarantia() {
        return fechaSolicitudGarantia;
    }

    public Date getFechaFinGarantia() {
        return fechaFinGarantia;
    }

    public double getPrecioGarantia() {
        return precioGarantia;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

}
