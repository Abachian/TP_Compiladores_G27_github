package compilador;

import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    public static final int NO_ENCONTRADO = -1;

    public static final String NO_ENCONTRADO_S = "No encontrado";

    public static final String LEXEMA = "lexema";

    public static final Map<Integer, Map<String, String>> simbolos = new HashMap<>();

    public static  StringBuilder ambito = new StringBuilder();
                           
    private static int identificador_siguiente = 1;

    public static Map<String,Integer> aparicionesLexema = new HashMap<>();

    public static void agregarSimbolo(String simbolo_nuevo) {
        Map<String, String> atributos = new HashMap<>();
        atributos.put(LEXEMA, simbolo_nuevo);
        agregarAparicionLexema(simbolo_nuevo);
        simbolos.put(identificador_siguiente, atributos);
        ++identificador_siguiente;
    }

    public static int obtenerSimbolo(String lexema) {
        Integer cantApariciones = aparicionesLexema.get(lexema);
        for (Map.Entry<Integer, Map<String, String>> entrada: simbolos.entrySet()) {
            String lexema_actual = entrada.getValue().get(LEXEMA);

            if (lexema_actual.equals(lexema)) {
                if (cantApariciones == 1)
                {
                    return entrada.getKey();
                }
                cantApariciones = cantApariciones-1;


            }
        }

        return NO_ENCONTRADO;
    }

    public static boolean isVariableDeclarada(String lexema) {
        return  aparicionesLexema.containsKey(lexema);

    }








    public static String obtenerSimboloAmbito(String lexema) {
        int puntero = TablaSimbolos.obtenerSimbolo(lexema);
        if(puntero == -1){
            return NO_ENCONTRADO_S;
        }
        else{
            Map<String,String> mapa = simbolos.get(puntero);
            return mapa.get("ambito");
        }



    }

    public static int obtenerParametro(String funcion) {

        int punt_funcion = obtenerSimbolo(funcion);
        String uso = obtenerAtributo(punt_funcion, "uso");

        if (uso.equals("variable")) {
            funcion = obtenerAtributo(punt_funcion, "funcion_asignada");
        }

        int primer_nmc = funcion.indexOf(Parser.NAME_MANGLING_CHAR);
        String ambito = funcion.substring(primer_nmc + 1) + Parser.NAME_MANGLING_CHAR + funcion.substring(0, primer_nmc);

        for (Map.Entry<Integer, Map<String, String>> entrada_i: simbolos.entrySet()) {
            Map<String, String> parametros_i = entrada_i.getValue();

            if (!parametros_i.containsKey("uso")) continue;

            if (parametros_i.get("uso").equals("parametro") && parametros_i.get("lexema").endsWith(ambito)) {
                return entrada_i.getKey();
            }
        }

        return NO_ENCONTRADO;
    }

    public static void eliminarSimbolo(int clave) {
        simbolos.remove(clave);
    }

    public static String obtenerUso(String lexema){
        int id = obtenerSimbolo(lexema);
        return obtenerAtributo(id, "uso");
    }

    private static void agregarAparicionLexema(String lexema){
        if (aparicionesLexema.containsKey(lexema)){
            int nuevoValor = aparicionesLexema.get(lexema);
            aparicionesLexema.put(lexema, nuevoValor + 1);
        }
        else {
            aparicionesLexema.put(lexema,1);
        }


    }

    // Agregar un simbolo en simbolos dado su clave, un string 'atributo', un string 'valor', si es que pertenece al mapa
    // Si el atributo ya esta dentro del mapa de atributos del simbolo, su valor se sobreescribira
    public static void agregarAtributo(int clave, String atributo, String valor) {
        if (simbolos.containsKey(clave)) {
            Map<String, String> atributos = simbolos.get(clave);
            atributos.put(atributo, valor);
        }
    }

    public static void agregarAtributoConAmbito(int clave, String atributo, StringBuilder nuevoAmbito) {
        if (simbolos.containsKey(clave)) {
            Map<String, String> atributos = simbolos.get(clave);
            StringBuilder aux = new StringBuilder(nuevoAmbito);
            ambito = aux;
            String ambitoFormatted = ambito.toString();
            atributos.put(atributo,ambitoFormatted);
        }
    }

    public static String getTipo(String valor){
        if (valor.contains("_ui")){
            return TablaTipos.UINT_TYPE;
        } else if (valor.contains("_s")) {
            return TablaTipos.SHORT_TYPE;
        }
        return TablaTipos.DOUBLE_TYPE;
    }

    //obtengo un atributo de un simbolo, dado su clave y un string 'atributo', si es que pertenece al mapa
    public static String obtenerAtributo(int clave, String atributo) {
        if (simbolos.containsKey(clave)) {
            Map<String, String> atributos = simbolos.get(clave);

            if (atributos.containsKey(atributo)) {
                return atributos.get(atributo);
            }
        }
        return NO_ENCONTRADO_S;
    }


    //imprimo simbolos entera
    public static void imprimirTabla() {
        System.out.println("\nTablaSimbolos:");

        for (Map.Entry<Integer, Map<String, String>> entrada: simbolos.entrySet()) {
            Map<String, String> atributos = entrada.getValue();
            System.out.print(entrada.getKey() + ": ");

            for (Map.Entry<String, String> atributo: atributos.entrySet()) {
                System.out.print("(" + atributo.getKey() + ": " + atributo.getValue() + ") ");
            }

            System.out.println();
        }
    }
}
