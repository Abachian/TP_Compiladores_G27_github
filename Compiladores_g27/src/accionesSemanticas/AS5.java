package accionesSemanticas;

import compilador.AnalizadorLexico;
import compilador.Parser;
import compilador.TablaSimbolos;

import java.io.Reader;

public class AS5 implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {
        String simbolo = token.toString();
        //System.out.println("ESTE ES EL SIMBOLO"+ simbolo);
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

        /*
            Valores
            2.2250738585072014D-308 < x < 1.7976931348623157D+308 UNION
            -1.7976931348623157D+308 < x < -2.2250738585072014D-308 UNION 0.0

         */


        boolean firstRange = ((AnalizadorLexico.POSITIVE_DOUBLE_NEGATIVE_D < valor) &&
                (valor < AnalizadorLexico.POSITIVE_DOUBLE_POSITIVE_D));
        boolean secondRange = ((AnalizadorLexico.NEGATIVE_DOUBLE_POSITIVE_D < valor) && (valor <
                AnalizadorLexico.NEGATIVE_DOUBLE_NEGATIVE_D));
        boolean isZero = (valor == 0);

        return (firstRange || secondRange || isZero);







    }


}
