package compilador;

import java.util.HashMap;
import java.util.Stack;

public class GeneradorCodigo {
    public static StringBuilder codigo = new StringBuilder();

    private static String ultimaComparacion = "";

    private static int auxiliarDisponible = 0;


    public static String ultimaFuncionLlamada;

    private static String nombreAux2bytes = "@aux2bytes";


    private static final String ERROR_DIVISION_POR_CERO = "ERROR: Division por cero";   //strings de error constantes en el codigo
    private static final String ERROR_OVERFLOW_PRODUCTO = "ERROR: Overflow en operacion de producto";
    private static final String ERROR_INVOCACION = "ERROR: Invocacion de funcion a si misma no permitida";

    private static final String ERROR_OVERFLOW_SUMA_ENTEROS = "ERROR: Overflow en operacion de suma de enteros";

    private static final String ERROR_RESULTADO_NEGATIVO_RESTA_UINT = "ERROR: Resultado negativo en resta de enteros sin signo";

    private static HashMap<Integer,String> registrosDeTercetos;
    private static  int tercetoActual = 1;

    private static Stack<String> etiquetasBi;
    private static Stack<String> etiquetasBf;



    public static Terceto obtenerTerceto(Integer integer){
        return Parser.estructura_Tercetos.get("ejecución").get(integer);
    }
    public static HashMap<Integer,Terceto>  obtenerHashMapEjecucionTerceto(){

        return Parser.estructura_Tercetos.get("ejecución");
    }

    public static void generarCodigo() {

        HashMap<Integer,Terceto> ejecucionTerceto =  new HashMap<>(obtenerHashMapEjecucionTerceto());

        //funcion principal que genera el codigo del programa, utilizando los tokes de la pocala y simbolos de la respectiva tabla
        for (Terceto terceto : ejecucionTerceto.values()) {
            String operacion = terceto.getOp1();
            switch (operacion) {
                case "*":
                case "+":
                case "=":
                case "-":
                case "/":
                case ">=":
                case ">":
                case "<=":
                case "<":
                case "!!":
                case "+=":
                case "==":
                    generarOperador(terceto);
                    break;
                case "BI":
                    generarSaltoBI(terceto);
                    break;
                case "BF":
                    generarSaltoBF(terceto);
                    break;
                case "IfFin":
                    generarSaltoIF();
                    break;
                case "DO":
                    generarSaltoDo();
                case "UNTIL":
                    generarSaltoUntil(terceto);
                    break;
                default:
                    if (operacion.startsWith(Parser.STRING_CHAR)) { //encontramos una cadena
                        operacion = operacion.substring(1);
                        codigo.append("invoke MessageBox, NULL, addr ").append(operacion).append(", addr ").append(operacion).append(", MB_OK \n");
                    } else if (operacion.startsWith(":")) {   //entramos un label
                        codigo.append(operacion.substring(1)).append(":\n");
                    } else if (operacion.startsWith("!")) {   // Encontramos el comienzo de una funcion
                        generarCabeceraFuncion(operacion);
                    } else {
                    }

                    break;
            }

            ++tercetoActual;
            //Impresion por pantalla para debuggear el codigo
            //System.out.println("Se leyo el token: " + token + ", la pila actual es: " + pila_tokens);
        }

        codigo.append("invoke ExitProcess, 0\n")
              .append("end START");

        generarCabecera();
    }

    private static void generarCabecera() {
    //funcion encargada de la generacion de la cabecera del codigo
        StringBuilder cabecera = new StringBuilder();

        cabecera.append(".386\n")
            .append(".model flat, stdcall\n")
            .append("option casemap :none\n")
            .append("include \\masm32\\include\\windows.inc\n")
            .append("include \\masm32\\include\\kernel32.inc\n")
            .append("include \\masm32\\include\\user32.inc\n")
            .append("includelib \\masm32\\lib\\kernel32.lib\n")
            .append("includelib \\masm32\\lib\\user32.lib\n")
            .append(".data\n")
            .append(nombreAux2bytes).append(" dw ? \n")
            //agregamos las constantes de error
            .append("@ERROR_OVERFLOW_SUMA_ENTEROS db \"" + ERROR_OVERFLOW_SUMA_ENTEROS + "\", 0\n")
            .append("@ERROR_OVERFLOW_PRODUCTO db \"" + ERROR_RESULTADO_NEGATIVO_RESTA_UINT + "\", 0\n")
                .append("@ERROR_OVERFLOW_PRODUCTO db \"" + ERROR_OVERFLOW_PRODUCTO + "\", 0\n");

        generarCodigoDatos(cabecera);

        cabecera.append(".code\n");
        cabecera.append(codigo);
        codigo = cabecera;
    }

    private static void generarCodigoDatos(StringBuilder cabecera) {
        //funcion utilizada para generar el codigo necesario para todos los datos del programa, presentes en la tabla de simbolos
        for (int simbolo : TablaSimbolos.obtenerConjuntoPunteros()) {
            //tomamos el atributo 'uso' del simbolo actual, desde la tabla de simbolos
            String uso = TablaSimbolos.obtenerAtributo(simbolo, "uso");

            if (!uso.equals(TablaSimbolos.NO_ENCONTRADO_S) && uso.equals("funcion")) continue;

            String tipo_actual = TablaSimbolos.obtenerAtributo(simbolo, "tipo");
            String lexema_actual = TablaSimbolos.obtenerAtributo(simbolo, "lexema");

            if (tipo_actual.equals(TablaSimbolos.NO_ENCONTRADO_S)) continue;

            switch (tipo_actual) {
                case TablaTipos.STRING_TYPE:
                    //tomo el valor de la tabla de simbolos
                    String valor_actual = TablaSimbolos.obtenerAtributo(simbolo, "valor");
                    cabecera.append(lexema_actual.substring(1)).append(" db \"").append(valor_actual).append("\", 0\n");
                    break;

                case TablaTipos.UINT_TYPE:
                case TablaTipos.VOID_TYPE:
                case TablaTipos.SHORT_TYPE:
                    if (uso.equals("constante")) {
                        String lexema = lexema_actual;
                        lexema_actual = "@" + lexema_actual;
                        cabecera.append(lexema_actual).append(" dd ").append(lexema).append("\n");
                    } else {
                        if (!lexema_actual.startsWith("@")) {
                            cabecera.append("_");
                        }

                        cabecera.append(lexema_actual).append(" dd ? \n");
                    }

                    break;

                case TablaTipos.DOUBLE_TYPE:        //en caso que el simbolo de tipo double y sea una constante
                    if (uso.equals("constante")) {
                        String lexema = lexema_actual;

                        if (lexema_actual.charAt(0) == '.')
                            lexema = "0" + lexema;

                        lexema_actual = "@" + lexema_actual.replace('.', '@').replace('-', '@').replace('+', '@');  //cambiamos el punto por una @
                        cabecera.append(lexema_actual).append(" REAL4 ").append(lexema).append("\n");   //y agregamos el simbolo a la cabecera con REAL4
                    } else {
                        if (! lexema_actual.startsWith("@")) {
                            cabecera.append("_");
                        }
                        cabecera.append(lexema_actual).append(" dq ?\n");
                    }

                    break;
            }
        }
    }

    private static void generarCabeceraFuncion(String token) {
        codigo.append(token.substring(1)).append(" PROC\n");
        // codigo.append("MOV EAX, ").append(token.substring(1)).append("\n");
        // codigo.append("MOV @FUNCION_ACTUAL, EAX\n"); // en la variable @FUNCION_ACTUAL guardamos el nombre de la funcion actual
    }

    public static void generarOperador(Terceto t) {
        String op1 = t.getOp1();
        String op2 = t.getOp2();
        String op3 = t.getOp3();

        String tipo = TablaTipos.getTipo(op1, TablaTipos.getTipo(op1, TablaSimbolos.obtenerSimboloAmbito(op1)));
        switch (tipo) {
            case TablaTipos.SHORT_TYPE:
                generarOperacionEnterosCortos(op1,op2, op3);
                break;
            case TablaTipos.UINT_TYPE:
                generarOperacionEnterosSinSigno(op1, op2, op3);
                break;
            case TablaTipos.DOUBLE_TYPE:
                generarOperacionFlotantes(op1, op2, op3);
                break;
            case TablaTipos.VOID_TYPE:
                generarOperacionVoid(op2, op3);
                break;
            default:
                System.out.println("Algo esta mal");
                TablaSimbolos.imprimirTabla();
        }
    }

    public static void generarOperacionVoid(String op1, String op2) {
        int punt_op2 = TablaSimbolos.obtenerSimbolo(op2);
        String uso = TablaSimbolos.obtenerAtributo(punt_op2, "uso");

        op1 = renombre(op1);

        //si el uso es una variable, renombramos el operando
        if (uso.equals("variable"))
            op2 = renombre(op2);

        codigo.append("MOV EAX, ").append(op2).append("\n");
        codigo.append("MOV ").append(op1).append(", EAX\n");
    }

    private static void generarErrorOverflowSumaEnteros(String aux){
        // genera el codigo necesario ante un error de overflow de suma de enteros
        codigo.append("JNO ").append(aux.substring(1)).append("\n");
        codigo.append("invoke MessageBox, NULL, addr @ERROR_OVERFLOW_SUMA_ENTEROS, addr @ERROR_OVERFLOW_SUMA_ENTEROS, MB_OK\n");
        codigo.append("invoke ExitProcess, 0\n");
        codigo.append(aux.substring(1)).append(":\n"); //declaro una label
    }

    public static void generarErrorResultadoNegativo(String aux){
        // genera el codigo necesario ante un error de resultado negativo de resta de enteros sin signo
        codigo.append("JNO ").append(aux.substring(1)).append("\n");
        codigo.append("invoke MessageBox, NULL, addr @ERROR_RESULTADO_NEGATIVO_RESTA_UINT, addr @ERROR_RESULTADO_NEGATIVO_RESTA_UINT, MB_OK\n");
        codigo.append("invoke ExitProcess, 0\n");
        codigo.append(aux.substring(1)).append(":\n"); //declaro una label
    }


    private static void generarErrorDivCero(String aux){
        // genera el codigo necesario ante un error de division por cero
        codigo.append("JNE ").append(aux.substring(1)).append("\n");
        codigo.append("invoke MessageBox, NULL, addr @ERROR_DIVISION_POR_CERO, addr @ERROR_DIVISION_POR_CERO, MB_OK\n");
        codigo.append("invoke ExitProcess, 0\n");
        codigo.append(aux.substring(1)).append(":\n"); //declaro una label
    }

    private static void generarErrorOverflow(String aux){
        //genera el codigo necesario ante un error de overflow de una operacion de producto de enteros
        //utilizamos el flag de overflow para indicar que se ha producido un overflow

        codigo.append("JNO ").append(aux.substring(1)).append("\n");
        codigo.append("invoke MessageBox, NULL, addr @ERROR_OVERFLOW_PRODUCTO, addr @ERROR_OVERFLOW_PRODUCTO, MB_OK\n");
        codigo.append("invoke ExitProcess, 0\n");
        codigo.append(aux.substring(1)).append(":\n"); //declaro una label
    }


    private static void generarOperacionEnterosSinSigno(String operador, String op1, String op2) {
        op1 = renombre(op1);
        op2 = renombre(op2);

        String aux;

        switch (operador) {
            case "+":
                codigo.append("MOV ECX, ").append(op1).append("\n"); //muevo siempre al registro ECX ya que al usar auxiliares nunca voy a gastar mas de 1 registro, ademas este registro no es usado por las divisiones
                codigo.append("ADD ECX, ").append(op2).append("\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", ECX\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;
            case "-":
                codigo.append("MOV ECX, ").append(op1).append("\n"); //muevo siempre al registro ECX ya que al usar auxiliares nunca voy a gastar mas de 1 registro, ademas este registro no es usado por las divisiones
                codigo.append("SUB ECX, ").append(op2).append("\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                generarErrorResultadoNegativo(aux);
                codigo.append("MOV ").append(aux).append(", ECX\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;
            case "*":
                codigo.append("MOV EAX, ").append(op1).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("MUL ").append(op2).append("\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", EAX\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;
            case "=":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("MOV ").append(op1).append(", ECX\n");

                break;
            case "/":
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                //String operando2 = ocuparAuxiliar(TablaTipos.ULONG_TYPE); //Esto se hace ya que no se puede ahcer una comparacion entre 2 inmediatos y no peude hacer un DIV con un inmediato
                //codigo.append("MOV ").append(operando2).append(", ").append(op2).append("\n");
                codigo.append("CMP ").append(op2).append(", 00h\n");
                generarErrorDivCero(aux);
                codigo.append("MOV EAX, ").append(op1).append("\n"); //el dividendo debe estar en EAX
                codigo.append("DIV ").append(op2).append("\n");
                codigo.append("MOV ").append(aux).append(", EAX\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;

            case "==":
                codigo.append("MOV ECX, ").append(op2).append("\n");
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n");
                codigo.append("JE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n");
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                ultimaComparacion = "JNE";
                break;
            case "!!":
                codigo.append("MOV ECX, ").append(op2).append("\n");
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n");
                codigo.append("JNE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n");
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                ultimaComparacion = "JE";
                break;
            case ">=":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JAE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                ultimaComparacion = "JB";
                break;

            case ">":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JA ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera

                ultimaComparacion = "JBE";
                break;

            case "<=":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JBE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                ultimaComparacion = "JA";
                break;

            case "<":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JB " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1) + ":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                ultimaComparacion = "JAE";
                break;

            case "+=":
                codigo.append("MOV ECX, ").append(op1).append("\n"); // Muevo el valor de op1 a ECX
                codigo.append("ADD ECX, ").append(op2).append("\n"); // Sumo el valor de op2 a ECX
                codigo.append("MOV ").append(op1).append(", ECX\n"); // Guardo el resultado de la suma de vuelta en op1
                break;

            default:
                codigo.append("ERROR, se entro a default en operacion de enteros").append("\n");
                break;
        }
    }

    private static void generarOperacionEnterosCortos(String operador, String op1, String op2) {
        op1 = renombre(op1);
        op2 = renombre(op2);

        String aux;

        switch (operador) {
            case "+":
                codigo.append("MOV ECX, ").append(op1).append("\n"); //muevo siempre al registro ECX ya que al usar auxiliares nunca voy a gastar mas de 1 registro, ademas este registro no es usado por las divisiones
                codigo.append("ADD ECX, ").append(op2).append("\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                generarErrorOverflowSumaEnteros(aux);
                codigo.append("MOV ").append(aux).append(", ECX\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;
            case "-":
                codigo.append("MOV ECX, ").append(op1).append("\n"); //muevo siempre al registro ECX ya que al usar auxiliares nunca voy a gastar mas de 1 registro, ademas este registro no es usado por las divisiones
                codigo.append("SUB ECX, ").append(op2).append("\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", ECX\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;
            case "*":
                codigo.append("MOV EAX, ").append(op1).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("MUL ").append(op2).append("\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                registrosDeTercetos.put(tercetoActual,aux);
                codigo.append("MOV ").append(aux).append(", EAX\n");
                break;
            case "=":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("MOV ").append(op1).append(", ECX\n");
                break;
            case "/":
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                //String operando2 = ocuparAuxiliar(TablaTipos.ULONG_TYPE); //Esto se hace ya que no se puede ahcer una comparacion entre 2 inmediatos y no peude hacer un DIV con un inmediato
                //codigo.append("MOV ").append(operando2).append(", ").append(op2).append("\n");
                codigo.append("CMP ").append(op2).append(", 00h\n");
                generarErrorDivCero(aux);
                codigo.append("MOV EAX, ").append(op1).append("\n"); //el dividendo debe estar en EAX
                codigo.append("DIV ").append(op2).append("\n");
                codigo.append("MOV ").append(aux).append(", EAX\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;

            case "==":
                codigo.append("MOV ECX, ").append(op2).append("\n");
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n");
                codigo.append("JE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n");
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                ultimaComparacion = "JNE";
                break;
            case "!!":
                codigo.append("MOV ECX, ").append(op2).append("\n");
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n");
                codigo.append("JNE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n");
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                ultimaComparacion = "JE";
                break;
            case ">=":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JAE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                ultimaComparacion = "JB";
                break;

            case ">":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JA ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                ultimaComparacion = "JBE";
                break;

            case "<=":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(aux).append(", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JBE ").append(aux.substring(1)).append("\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV ").append(aux).append(", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1)).append(":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                ultimaComparacion = "JA";
                break;

            case "<":
                codigo.append("MOV ECX, ").append(op2).append("\n"); //muevo al registro EAX ya que esto es lo que dice la filmina, que siempre en las MULT tengo que usar este registro
                codigo.append("CMP ").append(op1).append(", ECX\n");
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n"); //REVISAR pongo el aux en todos 1
                codigo.append("JB " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n"); //REVISAR pongo el aux en todos 0
                codigo.append(aux.substring(1) + ":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                ultimaComparacion = "JAE";
                break;

            case "+=":
                codigo.append("MOV ECX, ").append(op1).append("\n"); // Muevo el valor de op1 a ECX
                codigo.append("ADD ECX, ").append(op2).append("\n"); // Sumo el valor de op2 a ECX
                codigo.append("MOV ").append(op1).append(", ECX\n"); // Guardo el resultado de la suma de vuelta en op1
                break;

            default:
                codigo.append("ERROR, se entro a default en operacion de enteros").append("\n");
                break;
        }
    }

    private static void generarOperacionFlotantes(String operador, String op1, String op2) {
        op1 = renombre(op1);
        op2 = renombre(op2);

        String aux;


        switch (operador) {
            //nunca  va a llegar una operacion AND o OR entre doubles ya que al finalizar cada condicion guardo un ULONG con el resultado de la condicion.
            case "+":
                codigo.append("FLD ").append(op2).append("\n"); //apilo primero el op2 ya que quiero que me quede como el segundo que agarro para las operaciones que no son conmutativas
                codigo.append("FLD ").append(op1).append("\n");
                codigo.append("FADD\n");
                aux = ocuparAuxiliar(TablaTipos.DOUBLE_TYPE);
                codigo.append("FSTP ").append(aux).append("\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;

            case "-":
                codigo.append("FLD ").append(op2).append("\n"); //apilo primero el op2 ya que quiero que me quede como el segundo que agarro para las operaciones que no son conmutativas
                codigo.append("FLD ").append(op1).append("\n");
                codigo.append("FSUB\n");
                aux = ocuparAuxiliar(TablaTipos.DOUBLE_TYPE);
                codigo.append("FSTP ").append(aux).append("\n");
                registrosDeTercetos.put(tercetoActual,aux);

                break;

            case "*":
                codigo.append("FLD ").append(op2).append("\n"); //apilo primero el op2 ya que quiero que me quede como el segundo que agarro para las operaciones que no son conmutativas
                codigo.append("FLD ").append(op1).append("\n");

                codigo.append("FMUL\n");
                aux = ocuparAuxiliar(TablaTipos.DOUBLE_TYPE);
                generarErrorOverflow(aux);
                codigo.append("FSTP ").append(aux).append("\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;

            case "=":
                codigo.append("FLD ").append(op2).append("\n");
                codigo.append("FSTP ").append(op1).append("\n");
                break;

            case "/":
                aux = ocuparAuxiliar(TablaTipos.DOUBLE_TYPE);
                codigo.append("FLD ").append(op2).append("\n"); //cargo el operando dos para luego compararlo con cero
                //guardar 00h en una variable auxiliar
                String _cero = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV ").append(_cero).append(", 00h\n");
                codigo.append("FCOM " + _cero + "\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH
                generarErrorDivCero(aux);
                codigo.append("FLD ").append(op2).append("\n"); //apilo primero el op2 ya que quiero que me quede como el segundo que agarro para las operaciones que no son conmutativas
                codigo.append("FLD ").append(op1).append("\n");
                codigo.append("FDIV\n");
                codigo.append("FSTP ").append(aux).append("\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;

            case "==":
                codigo.append("FLD ").append(op1).append("\n");
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JE " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n");
                codigo.append(aux.substring(1) + ":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                break;
            case "!!":
                codigo.append("FLD ").append(op1).append("\n");
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JNE " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n");
                codigo.append(aux.substring(1) + ":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                break;

            case ">=":
                codigo.append("FLD ").append(op1).append("\n");
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JAE " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n");
                codigo.append(aux.substring(1) + ":\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;

            case ">":
                codigo.append("FLD ").append(op1).append("\n");
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH
                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JA " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n");
                codigo.append(aux.substring(1) + ":\n"); //creo una label para que salte y se saltee la instruccion de poner aux en cero en caso de que sea verdadera
                registrosDeTercetos.put(tercetoActual,aux);
                break;

            case "<=":
                codigo.append("FLD ").append(op1).append("\n");
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH

                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JBE " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n");
                codigo.append(aux.substring(1) + ":\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;

            case "<":
                codigo.append("FLD ").append(op1).append("\n");
                codigo.append("FCOM ").append(op2).append("\n");
                codigo.append("FSTSW ").append(nombreAux2bytes).append("\n");// cargo la palabra de estado en la memoria
                codigo.append("MOV AX, ").append(nombreAux2bytes).append("\n"); //copia el contenido en el registro AX
                codigo.append("SAHF").append("\n"); //Almacena en los 8 bits menos significativos del regisro de indicadores el valor del registro AH

                aux = ocuparAuxiliar(TablaTipos.UINT_TYPE);
                codigo.append("MOV " + aux + ", 0FFh\n");
                codigo.append("JB " + aux.substring(1) + "\n"); // si llega a ser verdadero salto y sigo con la ejecucion. En caso contrario tengo que poner el valor de aux en 0
                codigo.append("MOV " + aux + ", 00h\n");
                codigo.append(aux.substring(1) + ":\n");
                registrosDeTercetos.put(tercetoActual,aux);
                break;
            case "+=":
                codigo.append("MOV ECX, ").append(op1).append("\n"); // Muevo el valor de op1 a ECX
                codigo.append("ADD ECX, ").append(op2).append("\n"); // Sumo el valor de op2 a ECX
                codigo.append("MOV ").append(op1).append(", ECX\n"); // Guardo el resultado de la suma de vuelta en op1
                break;

            default:
                codigo.append("ERROR se entro a default al generar codigo para una operacion de flotantes\n");
                break;
        }
    }


    private static void generarSaltoBF(Terceto terceto) {
        codigo.append("JE L").append(terceto.getOp3()).append("\n");
        etiquetasBf.push(terceto.getOp3());

    }

    private static void generarSaltoBI(Terceto terceto) {
        codigo.append("JMP L").append(terceto.getOp2()).append("\n");
        etiquetasBi.push(terceto.getOp3());
        String direccion = etiquetasBi.pop();
        codigo.append("L").append(direccion).append("\n");
    }

    private static void generarSaltoIF() {
        String direccion = etiquetasBi.pop();
        codigo.append("L").append(direccion).append("\n");
    }

    private static void generarSaltoDo() {
        codigo.append("JE L").append(tercetoActual+1).append("\n");

    }

    private static void generarSaltoUntil(Terceto terceto){
        codigo.append(ultimaComparacion + " L").append(terceto.getOp2()).append("\n");
    }

    private static String renombre(String token) {
        char caracter = token.charAt(0);
        int puntToken = TablaSimbolos.obtenerSimbolo(token);

        // Si es una constante, le cambio de nombre al cual fue declarada
        if (TablaSimbolos.obtenerAtributo(puntToken, "uso").equals("constante")) {
            return "@" + token.replace('.', '@').replace('-', '@').replace('+', '@');
        } else if (Character.isLowerCase(caracter) || Character.isUpperCase(caracter) || caracter == '_') {
            return "_" + token;
        } else {
            return token;
        }
    }

    private static String ocuparAuxiliar(String tipo) {
        String retorno = "@aux" + auxiliarDisponible;
        ++auxiliarDisponible;
        //agrego a la tabla de simbolos la auxiliar.
        TablaSimbolos.agregarSimbolo(retorno);
        TablaSimbolos.agregarAtributo(TablaSimbolos.obtenerSimbolo(retorno), "tipo", tipo);
        return retorno;
    }

}