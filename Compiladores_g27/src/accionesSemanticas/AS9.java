package accionesSemanticas;

import compilador.TablaPalabrasReservadas;

import java.io.Reader;

public class AS9 implements AccionSemantica{
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        int identificador = -2;
        String simbolo = token.toString();
        int id_palabra_reservada = TablaPalabrasReservadas.obtenerIdentificador(simbolo);

        if (id_palabra_reservada != TablaPalabrasReservadas.PALABRA_NO_RESERVADA) {
            identificador = id_palabra_reservada;
            return identificador;
        }
        return identificador;


    }
}
