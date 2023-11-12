package accionesSemanticas;

import java.io.Reader;

public class AS7 implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        char caracter = token.charAt(0);
        return caracter;
    }
}
