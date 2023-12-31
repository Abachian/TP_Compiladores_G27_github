package accionesSemanticas;

import compilador.AnalizadorLexico;
import compilador.Parser;
import compilador.TablaSimbolos;

import java.io.IOException;
import java.io.Reader;

public class AS4 implements AccionSemantica {
    @Override
    public int ejecutar(Reader lector, StringBuilder token) {

        try {
            token.append((char) lector.read()); // Concatena el caracter actual

        } catch (IOException excepcion) {
            excepcion.printStackTrace();
        }

        String simbolo = token.toString();
        String simbolo_aux=null;

        try {
           int indiceGuionBajo = simbolo.indexOf('_');

           if (indiceGuionBajo != -1) {
               simbolo_aux = simbolo.substring(0, indiceGuionBajo);

           }

            long valor_simbolo = Long.parseLong(simbolo_aux);
            char siguienteCaracter = simbolo.charAt(indiceGuionBajo + 1);
            switch (siguienteCaracter){
                case 'u' :
                    if (valor_simbolo > AnalizadorLexico.UNSIGNED_MAX_INT_VALUE) {
                        Parser.agregarError(Parser.errores_lexicos, Parser.WARNING, "El numero largo " + simbolo_aux +
                                " fue truncado al valor maximo, ya que supera a este mismo");
                        simbolo = Double.toString(AnalizadorLexico.UNSIGNED_MAX_INT_VALUE);
                    }
                    break;
                case 's' :
                    if (valor_simbolo > AnalizadorLexico.MAX_INT_VALUE) {
                        Parser.agregarError(Parser.errores_lexicos, Parser.WARNING, "El numero largo " + simbolo_aux +
                                " fue truncado al valor maximo, ya que supera a este mismo");
                        simbolo = Double.toString(AnalizadorLexico.MAX_INT_VALUE);
                    }else if (valor_simbolo < AnalizadorLexico.MIN_INT_VALUE){
                        Parser.agregarError(Parser.errores_lexicos, Parser.WARNING, "El numero largo " + simbolo_aux +
                                " fue truncado al valor minimo, ya que no supera a este mismo");
                        simbolo = Double.toString(AnalizadorLexico.MIN_INT_VALUE);
                    }
                    break;
            }

        } catch (NumberFormatException excepcion) {
            excepcion.printStackTrace();
        }

        if (TablaSimbolos.obtenerSimbolo(simbolo) == TablaSimbolos.NO_ENCONTRADO) {
            TablaSimbolos.agregarSimbolo(simbolo);
        }

        return AnalizadorLexico.CONSTANTE;
    }
}
