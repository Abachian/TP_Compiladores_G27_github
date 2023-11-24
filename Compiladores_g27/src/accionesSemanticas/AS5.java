package accionesSemanticas;

import compilador.AnalizadorLexico;
import compilador.Parser;
import compilador.TablaSimbolos;

import java.io.Reader;

public class AS5 implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        String simbolo = token.toString();
        System.out.println("ESTE ES EL SIMBOLO"+ simbolo);
        String simbolo_aux = token.toString();;

        try {

            simbolo_aux = simbolo.replace('d', 'e').replace('D', 'E');
            double valor =  Double.parseDouble(simbolo_aux);

            if (!dentroRango(valor)) {

                Parser.agregarError(Parser.errores_lexicos, Parser.WARNING, "El numero double " + simbolo +
                                            " fue truncado ya que no se encuentra dentro del rango aceptado");

                if (valor < AnalizadorLexico.MIN_DOUBLE_VALUE) {
                    simbolo_aux = Double.toString(AnalizadorLexico.MIN_DOUBLE_VALUE);
                } else {
                    simbolo_aux = Double.toString(AnalizadorLexico.MAX_DOUBLE_VALUE);
                }
            }
        } catch (NumberFormatException excepcion) {
            excepcion.printStackTrace();
        }

        if (TablaSimbolos.obtenerSimbolo(simbolo) == TablaSimbolos.NO_ENCONTRADO) {
            TablaSimbolos.agregarSimbolo(simbolo);
        }

        return AnalizadorLexico.CONSTANTE;
    }

    private static boolean dentroRango(double valor) {
        /*El valor que esta recibiendo esta funcion tiene formato 2.2E7,
        por lo que nunca va a estar dentro de los valores de double
        que tienen de exponente la letra D, hay que cambiar eso
        */
        System.out.println("ESTE ES VALOR"+ valor);
        valor = 2.2D+6;
        return  (AnalizadorLexico.MIN_DOUBLE_VALUE <= valor && valor <= AnalizadorLexico.MAX_DOUBLE_VALUE);
    }
}
