package compilador;

import accionesSemanticas.AccionSemantica;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {

    public static final char TABULACION = '\t';
    public static final char BLANCO = ' ';
    public static final char NUEVA_LINEA = '\n';
    public static final char RETORNO_CARRO = '\r';

    public static final int IDENTIFICADOR = 257;

    public static final int CONSTANTE = 258;

    public static final int CADENA = 259;

    public static final int LONGITUD_IDENTIFICADOR = 20;


    static String userDir = System.getProperty("user.dir");


    public static final int MIN_INT_VALUE = - (int) Math.pow(2, 7);
    public static final int MAX_INT_VALUE = (int) Math.pow(2, 7) - 1;
    public static final int UNSIGNED_MAX_INT_VALUE = (int) Math.pow(2, 16) - 1;



    public static final double MIN_DOUBLE_VALUE = 2.2250738585072014D-308;

    public static final double MAX_DOUBLE_VALUE = 1.7976931348623157D+308;

    public static Reader lector;

    private static final char DIGITO = '0';
    private static final char MINUSCULA = 'a';
    private static final char MAYUSCULA = 'A';
    private static final int CANTIDAD_ESTADOS = 22;
    private static final int CANTIDAD_CARACTERES = 30;




    private static final String ARCHIVO_MATRIZ_ESTADOS = userDir + "\\Compiladores_g27\\src\\matriz_de_Transicion_de_Estados.txt";
    private static final String ARCHIVO_MATRIZ_ACCIONES = userDir + "\\Compiladores_g27\\src\\matrizAccionesSemanticas.txt";
    private static final AccionSemantica[][]  acciones_semanticas  = FileHelper.readActionMatrixFile(ARCHIVO_MATRIZ_ACCIONES, CANTIDAD_ESTADOS, CANTIDAD_CARACTERES);
    private static final int[][] transicion_estados = FileHelper.readIntMatrixFile(ARCHIVO_MATRIZ_ESTADOS, CANTIDAD_ESTADOS, CANTIDAD_CARACTERES);

    public static final StringBuilder token_actual = new StringBuilder();

    private static final List<String> tokens_detectados = new ArrayList<>();

    public static int estado_actual = 0;
    private static int linea_actual = 1;

    public static int getLineaActual() {
        return linea_actual;
    }

    public static void setLineaActual(int numero_linea) {
        linea_actual = numero_linea;
    }

        private static void agregarTokenADetectados(int token) {
            String token_a_guardar;
            switch (token) {
                case 257:
                    token_a_guardar = "ID";
                    break;
                case 258:
                    token_a_guardar = "CTE";
                    break;
                case 259:
                    token_a_guardar = "cadena";
                    break;
                case 260:
                    token_a_guardar = "IF";
                    break;
                case 261:
                    token_a_guardar = "ELSE";
                    break;
                case 262:
                    token_a_guardar = "END_IF";
                    break;
                case 263:
                    token_a_guardar = "PRINT";
                    break;
                case 264:
                    token_a_guardar = "VOID";
                    break;
                case 265:
                    token_a_guardar = "RETURN";
                    break;
                case 266:
                    token_a_guardar = "DO";
                    break;
                case 267:
                    token_a_guardar = "UNTIL";
                    break;
                case 268:
                    token_a_guardar = "CLASS";
                    break;
                case 269:
                    token_a_guardar = "comp_menor_igual";
                    break;
                case 270:
                    token_a_guardar = "comp_mayor_igual";
                    break;
                case 271:
                    token_a_guardar = "comp_distinto";
                    break;
                case 272:
                    token_a_guardar = "comp_igual";
                    break;
                case 273:
                    token_a_guardar = "SUMA";
                    break;
                case 274:
                    token_a_guardar = "UINT";
                    break;
                case 275:
                    token_a_guardar = "DOUBLE";
                    break;
                case 276:
                    token_a_guardar = "SHORT";
                    break;
                case 277:
                    token_a_guardar = "IMPL";
                    break;
                case 278:
                    token_a_guardar = "FOR";
                    break;
                default:
                    return;

            }
            int linea = AnalizadorLexico.linea_actual;

        tokens_detectados.add("Token " + token_a_guardar);
           // System.out.println(token_a_guardar);

        }

    public static void imprimirTokensDetectados() {
        System.out.println("Tokens detectados: ");
        for (String token : tokens_detectados) {
            System.out.println(token);
        }
    }


    /**
     *
     * @param caracter
     * @return el caracter colapsado en caso de ser un digito, minuscula o mayuscula diferente a "D"
     */
    private static char obtenerTipoCaracter(char caracter) {
        if (Character.isDigit(caracter)) {
            return DIGITO;} else if (caracter != 'd' && caracter != 's' && caracter != 'u' && caracter != 'i' && Character.isLowerCase(caracter)) {
            return MINUSCULA;
        } else if (caracter != 'D' && Character.isUpperCase(caracter)) {return MAYUSCULA;
        } else {return caracter;
        }
    }

    // Se llama por cada caracter que se lee
    public static int cambiarEstado(Reader lector, char caracter) {
        int caracter_actual;
        switch (obtenerTipoCaracter(caracter)) {
            case BLANCO:
                caracter_actual = 0;
                break;
            case TABULACION:
                caracter_actual = 1;
                break;
            case NUEVA_LINEA:
            case RETORNO_CARRO:
                caracter_actual = 2;
                break;
            case MINUSCULA:
                caracter_actual = 3;
                break;
            case MAYUSCULA:
                caracter_actual = 4;
                break;
            case '_':
                caracter_actual = 5;
                break;
            case 'u':
                caracter_actual = 6;
                break;
            case 'i':
                caracter_actual = 7;
                break;
            case 's':
                caracter_actual = 8;
                break;
            case DIGITO:
                caracter_actual = 9;
                break;
            case '.':
                caracter_actual = 10;
                break;
            case ',':
                caracter_actual = 11;
                break;
            case 'd':
                caracter_actual = 12;
                break;
            case 'D':
                caracter_actual = 13;
                break;
            case ';':
                caracter_actual = 14;
                break;
            case '*':
                caracter_actual = 15;
                break;
            case '+':
                caracter_actual = 16;
                break;
            case '-':
                caracter_actual = 17;
                break;
            case '/':
                caracter_actual = 18;
                break;
            case '(':
                caracter_actual = 19;
                break;
            case ')':
                caracter_actual = 20;
                break;
            case '<':
                caracter_actual = 21;
                break;
            case '>':
                caracter_actual = 22;
                break;
            case '=':
                caracter_actual = 23;
                break;
            case '!':
                caracter_actual = 24;
                break;
            case '{':
                caracter_actual = 25;
                break;
            case '}':
                caracter_actual = 26;
                break;
            case '#':
                caracter_actual = 27;
                break;
            case ':':
                caracter_actual = 28;
                break;
            default:
                caracter_actual = 29;
                break;
        }




        AccionSemantica accion_a_ejecutar = acciones_semanticas[estado_actual][caracter_actual];
        int identificador_token = accion_a_ejecutar.ejecutar(lector, token_actual);
        agregarTokenADetectados(identificador_token);
        estado_actual = transicion_estados[estado_actual][caracter_actual];

        return identificador_token;

    }


} 



