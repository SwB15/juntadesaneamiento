package Datos;

/**
 *
 * @author SwichBlade15
 */
public class DatosClientes {

    private int id;
    private int numerocliente;
    private String Nombre;
    private String Apellido;
    private String Direccion;
    private int Medidor;

    public DatosClientes(int id, int numerocliente, String Nombre, String Apellido, String Direccion, int Medidor) {
        this.id = id;
        this.numerocliente = numerocliente;
        this.Nombre = Nombre;
        this.Apellido = Apellido;
        this.Direccion = Direccion;
        this.Medidor = Medidor;
    }

    public DatosClientes() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getnumerocliente() {
        return numerocliente;
    }

    public void setnumerocliente(int numerocliente) {
        this.numerocliente = numerocliente;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getApellido() {
        return Apellido;
    }

    public void setApellido(String Apellido) {
        this.Apellido = Apellido;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String Direccion) {
        this.Direccion = Direccion;
    }

    public int getMedidor() {
        return Medidor;
    }

    public void setMedidor(int Medidor) {
        this.Medidor = Medidor;
    }

}
