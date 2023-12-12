package compilador;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TablaTipos {
    public static final int SHORT = 0;
    public static final int UINT = 1;
    public static final int DOUBLE = 2;
    public static final int CLASS = 3;
    public static final int VOID = 4;
    public static final int STRING = 5;

    public static final String SHORT_TYPE = "_s";
    public static final String UINT_TYPE = "_ui";
    public static final String DOUBLE_TYPE = "DOUBLE";
    public static final String CLASS_TYPE = "CLASS";
    public static final String VOID_TYPE = "VOID";
    public static final String STRING_TYPE = "string";
    public static final String ERROR_TYPE = "error";

    public static String getTipo(String lexema, String ambito) {
        String resultado = "NULL";
        for (Map.Entry<Integer, Map<String, String>> entrada : TablaSimbolos.simbolos.entrySet()) {
            if (Objects.equals(entrada.getValue().get("lexema"), lexema) && Objects.equals(entrada.getValue().get("ambito"), ambito)) {
                resultado = entrada.getValue().get("tipo");
            }

        }

        return resultado;
    }

    private static int getNumeroTipo(String tipo) {
        if (tipo.equals(UINT_TYPE)) return UINT;
        else if (tipo.equals(SHORT_TYPE)) return SHORT;
        else if (tipo.equals(CLASS_TYPE)) return CLASS;
        else if (tipo.equals(STRING_TYPE)) return STRING;
        else if (tipo.equals(VOID_TYPE)) return VOID;
        else return DOUBLE;
    }
    }