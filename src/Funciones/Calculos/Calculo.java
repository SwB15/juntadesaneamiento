package Funciones.Calculos;

/**
 *
 * @author SwichBlade15
 */
public class Calculo {

    private final int consumoMinimo = 10;
    private final int importeMinimo = 10000;
    private int consumo = 0;
    int consumoExcedente = 0;
    int importeExcedente = 0;
    int importeTotal = 0;
    int iva = 0;

    public Calculo() {
    }

    public int calculoConsumo(int medidorInicio, int medidorCierre) {
        consumo = medidorCierre - medidorInicio;
        if (consumo > 10) {
            consumoExcedente = consumo - 10;
        }

        return consumoExcedente;
    }

    public int importeExcedente(int consumoExcedente, int atrasos, int conexion, int medidor) {
        if (consumoExcedente != 0) {
            importeExcedente = consumoExcedente * 2000;
        }
        
        return importeExcedente;
    }

    public int importeTotal(int importeExcedente, int importeAtrasos, int importeConexion, int importeMedidor) {
        importeTotal = importeMinimo + importeExcedente + importeAtrasos + importeConexion + importeMedidor;
        return importeTotal;
    }

    public int importeIva(int importeTotal) {
        iva = importeTotal / 10;
        return iva;
    }

}
