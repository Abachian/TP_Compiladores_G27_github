//package compilador;
//
//import java.util.*;
//
//public class IntermediateCode {
//
//    static String scope;
//    static boolean falloEnCompilacion;
//    static int cantProc;
//    static boolean shadowing;
//    static int ambitoAnonimo;
//    static String ambito_actual;
//    static List<String> list_variables;
//    static List<Integer> pila;
//    static Map<Integer, Terceto> list_tercetos;
//
//    // Tipos
//    public static void addVariable(String variable) {
//        list_variables.add(variable);
//    }
//
//    public static String asignarTipo(SymbolTable symbolTable, String op, String op2) {
//        if (op != null && op2 != null && checkType(symbolTable, op, op2)) {
//            return op;
//        }
//        return "error";
//    }
//
//    public static void declareVariableList(SymbolTable tablita, String type) {
//        tablita.clearTable();
//        for (String variable : list_variables) {
//            String key = variable + scope + ambito_actual;
//            checkShadowing(tablita, key);
//
//            if (tablita.existVariable(key) && tablita.getRegistry(key).getType().isEmpty()) {
//                tablita.addType(type, key, ambito_actual);
//            } else {
//                System.out.println("Error: Redeclaracion de variable");
//                falloEnCompilacion = true;
//            }
//        }
//        list_variables.clear();
//        tablita.clearTable();
//    }
//
//    // Other Checks
//    public static void check_scope(SymbolTable symbolTable, String key) {
//        symbolTable.clearTable();
//
//        String clave = key + scope;
//        String kaka = clave + ambito_actual;
//
//        if (symbolTable.existVariable(kaka)) {
//            return;
//        }
//
//        String aux;
//        int pos = kaka.length();
//
//        while (pos != 0) {
//            if (kaka.charAt(pos - 1) == scope.charAt(0)) {
//                aux = kaka.substring(0, pos);
//                if (aux.indexOf(scope.charAt(0)) == -1) {
//                    if (symbolTable.existVariable(aux)) {
//                        return;
//                    }
//                } else {
//                    String aux2 = aux;
//                    if (symbolTable.existVariable(aux2)) {
//                        return;
//                    }
//                }
//            }
//            pos--;
//        }
//
//        System.out.println("Error: variable no declarada.");
//    }
//
//    public static boolean check_scope2(SymbolTable symbolTable, String key) {
//        symbolTable.clearTable();
//
//        String clave = key + scope;
//        String kaka = clave + ambito_actual;
//
//        if (symbolTable.existVariable(kaka)) {
//            key = kaka;
//            return true;
//        }
//
//        String aux;
//        int pos = kaka.length();
//
//        while (pos != 0) {
//            if (kaka.charAt(pos - 1) == scope.charAt(0)) {
//                aux = kaka.substring(0, pos);
//                if (aux.indexOf(scope.charAt(0)) == -1) {
//                    if (symbolTable.existVariable(aux)) {
//                        key = aux;
//                        return true;
//                    }
//                } else {
//                    String aux2 = aux;
//                    if (symbolTable.existVariable(aux2)) {
//                        key = aux2;
//                        return true;
//                    }
//                }
//            }
//            pos--;
//        }
//
//        return false;
//    }
//
//    // Tercetos Operations
//    public static void insertar_terceto(String op, String op1, String op2) {
//        Terceto t = new Terceto(op, op1, op2);
//        list_tercetos.put(number, t);
//        number++;
//    }
//
//    public static void insertar_terceto(Terceto t) {
//        list_tercetos.put(number, t);
//        number++;
//    }
//
//    public static void modificar_terceto(int numeroTerceto, int numeroCompletar) {
//        Terceto t = removeTerceto(numeroTerceto);
//        int num = number + numeroCompletar;
//        String s = String.valueOf(num);
//        if (t.getOp().equals("BI")) {
//            t.setOp1(s);
//        } else {
//            t.setOp2(s);
//        }
//        list_tercetos.put(numeroTerceto, t);
//    }
//
//    public static void completar_operando3(Terceto t, String operando3) {
//        t.setOp2(operando3);
//        insertar_terceto(t);
//    }
//
//    public static Terceto removeTerceto(int pos) {
//        Terceto t = new Terceto();
//        t.setOp(list_tercetos.get(pos).getOp());
//        t.setOp1(list_tercetos.get(pos).getOp1());
//        t.setOp2(list_tercetos.get(pos).getOp2());
//        list_tercetos.remove(pos);
//        return t;
//    }
//
//    public static void apilar() {
//        pila.add(0, number);
//    }
//
//    public static int desapilar() {
//        int numerito = pila.get(0);
//        pila.remove(0);
//        return numerito;
//    }
//
//    // Other Methods...
//
//    // Auxiliary Functions...
//
//    public static void imprimirTercetos() {
//        for (Map.Entry<Integer, Terceto> entry : list_tercetos.entrySet()) {
//            int key = entry.getKey();
//            Terceto value = entry.getValue();
//            System.out.println(key + ".  (" + value.getOp() + " , " + value.getOp1() + " , " + value.getOp2() + " ) ");
//        }
//    }
//
//    public static void imprimirLista() {
//        for (String variable : list_variables) {
//            System.out.print(variable + ", ");
//        }
//        System.out.println();
//    }
//
//    // Other print methods...
//
//    // Rest of the methods...
//}
