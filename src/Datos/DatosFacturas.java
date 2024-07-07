package Datos;

import java.sql.Date;

/**
 *
 * @author SwichBlade15
 */
public class DatosFacturas {

    private int id;
    private String boleta;
    private String mes;
    private Date vencimiento;
    private int atraso;
    private int conexion;
    private int medidor;
    private Date fechaInicio;
    private Date fechaCierre;
    private int estadoInicio;
    private int estadoCierre;
    private int consumoMinimo;
    private int excedente;
    private int total;
    private int idclientes;

    public DatosFacturas(int id, String boleta, String mes, Date vencimiento, int atraso, int conexion, int medidor, Date fechaInicio, Date fechaCierre, int estadoInicio, int estadoCierre, int consumoMinimo, int excedente, int total, int idclientes) {
        this.id = id;
        this.boleta = boleta;
        this.mes = mes;
        this.vencimiento = vencimiento;
        this.atraso = atraso;
        this.conexion = conexion;
        this.medidor = medidor;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
        this.estadoInicio = estadoInicio;
        this.estadoCierre = estadoCierre;
        this.consumoMinimo = consumoMinimo;
        this.excedente = excedente;
        this.total = total;
        this.idclientes = idclientes;
    }

    public DatosFacturas() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBoleta() {
        return boleta;
    }

    public void setBoleta(String boleta) {
        this.boleta = boleta;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(Date vencimiento) {
        this.vencimiento = vencimiento;
    }

    public int getAtraso() {
        return atraso;
    }

    public void setAtraso(int atraso) {
        this.atraso = atraso;
    }

    public int getConexion() {
        return conexion;
    }

    public void setConexion(int conexion) {
        this.conexion = conexion;
    }

    public int getMedidor() {
        return medidor;
    }

    public void setMedidor(int medidor) {
        this.medidor = medidor;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaCierre() {
        return fechaCierre;
    }

    public void setFechaCierre(Date fechaCierre) {
        this.fechaCierre = fechaCierre;
    }

    public int getEstadoInicio() {
        return estadoInicio;
    }

    public void setEstadoInicio(int estadoInicio) {
        this.estadoInicio = estadoInicio;
    }

    public int getEstadoCierre() {
        return estadoCierre;
    }

    public void setEstadoCierre(int estadoCierre) {
        this.estadoCierre = estadoCierre;
    }

    public int getConsumoMinimo() {
        return consumoMinimo;
    }

    public void setConsumoMinimo(int consumoMinimo) {
        this.consumoMinimo = consumoMinimo;
    }

    public int getExcedente() {
        return excedente;
    }

    public void setExcedente(int excedente) {
        this.excedente = excedente;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getIdclientes() {
        return idclientes;
    }

    public void setIdclientes(int idclientes) {
        this.idclientes = idclientes;
    }
}
