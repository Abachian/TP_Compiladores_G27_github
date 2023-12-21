%{
package compilador;
import accionesSemanticas.AccionSemantica;

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.HashMap;
import compilador.Terceto;

%}

//declaracion de tokens a recibir del Analizador Lexico
%token ID cte cadena IF ELSE END_IF PRINT VOID RETURN DO
 UNTIL CLASS comp_menor_igual comp_mayor_igual comp_distinto comp_igual SUMA
 UINT DOUBLE SHORT IMPL FOR

%left '+' '-'
%left '*' '/'
%left '>' '<'

%start program

%%//declaracion de la gramatica del lenguaje

program: begin cuerpo_prog end ',' {metodo.add("ejecución"); agregarListaTercetos(metodo.get(metodo.size()-1), codigoIntermedio);}
	    | begin end ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaban sentencias de ejecucion");}
		| begin cuerpo_prog ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un '}' al final del programa");}
		| cuerpo_prog end ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un '{' antes de las sentencias");}
		| begin cuerpo_prog end {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un ',' al final del programa");}
		| ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un programa");}
;


begin: '{' {cambiarAmbito(":START"); Parser.declarando = false; tipo = TablaTipos.STRING_TYPE; cambiarAmbito("main");}
;

end: '}'
;

cuerpo_prog: declaraciones ejecucion
			| declaraciones
			| ejecucion
			| ejecucion declaraciones {agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
			| declaraciones ejecucion declaraciones {agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
;

//reglas de declaraciones y bloques de sentencias

declaraciones: declaraciones declaracion {Parser.declarando = false;}
			 | declaracion {Parser.declarando = false;}
;

declaracion: declaracion_variables
        	| declaracion_funcion
        	| declaracion_clase
        	| declaracion_variables_clase
;

declaracion_variables_clase: ID list_var ','
;

declaracion_variables: tipo list_var ','
					 | list_var ','
;

list_var: ID {					Parser.agregarSimbolo($1.sval, "variable ");
								int ptr_id = TablaSimbolos.obtenerSimbolo($1.sval);
                            	TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");

                                TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);}


        | list_var ';' ID {				Parser.agregarSimbolo($3.sval, "variable ");
                                          int ptr_id = TablaSimbolos.obtenerSimbolo($1.sval);
                                               TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                               TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");
                                               TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);}
;

declaracion_funcion: funcion
                	| funcion_sin_definir
              	    | declaracion_impl
;

funcion: header_funcion '(' list_de_parametros ')' '{' cuerpo_de_la_funcion '}' ',' {
											salirAmbito();
											Parser.declarando = true;
											agregarListaTercetos(metodo.get(metodo.size()-1), codigoIntermedio); metodo.remove(metodo.size()-1);}
		|  header_funcion '(' list_de_parametros ')' '{' '}' ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera el cuerpo de la funcion");}
;

funcion_sin_definir: header_funcion '(' list_de_parametros ')' ',' {salirAmbito(); agregarListaTercetos(metodo.get(metodo.size()-1), codigoIntermedio); metodo.remove(metodo.size()-1);}
;


header_funcion: VOID ID { Parser.agregarSimbolo($2.sval, "nombre de metodo ");
						int ptr_id = TablaSimbolos.obtenerSimbolo($2.sval);
						TablaSimbolos.agregarAtributo(ptr_id, "tipo", "VOID_TYPE");
                    	TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de metodo");
                    	TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);
                    	agregarEstructura(estructuras_sintacticas, "Declaracion de Funcion");
                        cambiarAmbito($2.sval);
                        ambitos.put($2.sval, ambito);
                        metodo.add($2.sval);
                        Parser.id_funcion = $2.sval;}
            | VOID {agregarError(errores_sintacticos, Parser.ERROR, "Se espera nombre de la funcion");}
;

cuerpo_de_la_funcion: declaraciones ejecucion_funcion
    | ejecucion_funcion
;


list_de_parametros: {Parser.parametro_funcion = "NULL";
					agregarFuncion(id_funcion, TablaTipos.getTipo(parametro_funcion, ambito.toString()));}
	           | parametro
;


parametro: tipo ID {Parser.agregarSimbolo($2.sval, "parametro ");
					int ptr_id = TablaSimbolos.obtenerSimbolo($2.sval);
	     	        TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                    TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");
                    TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);
                    Parser.parametro_funcion = $2.sval;
                    agregarFuncion(id_funcion, TablaTipos.getTipo(parametro_funcion, ambito.toString()));}
          | ID {agregarError(errores_sintacticos, Parser.ERROR, "Se espera el tipo del parametro");}
          | tipo {agregarError(errores_sintacticos, Parser.ERROR, "Se espera el nombre del parametro");}
	      | CLASS ID {tipo = TablaTipos.CLASS_TYPE; Parser.agregarSimbolo($2.sval, "parametro "); int ptr_id = TablaSimbolos.obtenerSimbolo($2.sval);
                                                                                     TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                                                                     TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");
                                                                                     TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);
                                                                                     Parser.parametro_funcion = $2.sval;
                                                                                     agregarFuncion(id_funcion, TablaTipos.getTipo(parametro_funcion, ambito.toString()));}
;

list_de_parametros_clase: {Parser.parametro_funcion = "NULL";}
	           | parametro_clase
;


parametro_clase: tipo ID {Parser.parametro_funcion = $2.sval;}
          | ID {Parser.parametro_funcion = $1.sval;}
          | tipo {agregarError(errores_sintacticos, Parser.ERROR, "Se espera el nombre del parametro");}
;


tipo: UINT {tipo = TablaTipos.UINT_TYPE;}
    | SHORT {tipo = TablaTipos.SHORT_TYPE;}
    | DOUBLE {tipo = TablaTipos.DOUBLE_TYPE;}

;

ejecucion_funcion:  bloque_funcion
;

bloque_funcion: bloque_funcion sentencia_funcion
        		| sentencia_funcion
;

sentencia_funcion: sentencia
;

ejecucion: ejecucion sentencia
			| sentencia
;

sentencia: sentencia_ejecutable

;

sentencia_ejecutable: asignacion ','
                | seleccion ','
                | impresion ','
                | DO_UNTIL ','
                | referencia_clase ','
;

referencia_clase: ID '.' ID '=' ID {int aux = generarTerceto($4.sval,$1.sval+$2.sval+$3.sval,$5.sval);
if (!Parser.mismoTipo($3.sval, $5.sval, ambito.toString())){
									agregarError(errores_semanticos, Parser.ERROR, "Se requieren operandos del mismo tipo");}}
		| ID '.' ID '=' ID '.' ID {int aux = generarTerceto($4.sval,$1.sval+$2.sval+$3.sval,$5.sval+$6.sval+$7.sval);
									if (!Parser.mismoTipo($3.sval, $7.sval, ambito.toString())){
									agregarError(errores_semanticos, Parser.ERROR, "Se requieren operandos del mismo tipo");}}
		| ID '.' ID '(' list_de_parametros_clase ')'  {int aux = generarTerceto("CALL",$1.sval+"."+$3.sval,Parser.parametro_funcion);
								if(Parser.funciones.containsKey($3.sval)){
								if (Parser.funciones.get($3.sval).equals(TablaTipos.getTipo(Parser.parametro_funcion, ambito.toString())) || !Parser.parametro_funcion.equals("NULL")){
												if (!Parser.mismoTipoFuncion($3.sval, Parser.parametro_funcion, ambito.toString())){

													agregarError(errores_semanticos, Parser.ERROR, "El tipo del parametro no corresponde con el de la funcion");
												}
								}
								else{
								agregarError(errores_semanticos, Parser.ERROR, "La cantidad de parametros no corresponde con el de la funcion");
								}
							}else{agregarError(errores_semanticos, Parser.ERROR, "Funcion no declarada");}}
;

DO_UNTIL: pdo bloque_sentencias_do UNTIL '(' condicion ')' {int i = codigoIntermedio.size();
                                                            boolean encontrado = false;
                                                            while (!encontrado && i > 0 )
                                                            {
                                                              if (codigoIntermedio.get(i).getOp1().equals("DO"))
                                                              {

                                                                  $$.sval = "[" + Integer.toString(generarTerceto("UNTIL",Integer.toString(i + 1) ,Integer.toString(punteroTerceto + 1))) + "]";
                                                                  encontrado = true;
                                                               }
                                                              i = i-1;
                                                            }
                                                            }

		| pdo UNTIL '(' condicion ')' { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
;

pdo: DO {$$.sval = "[" + Integer.toString(generarTerceto("DO","-","-"))+ "]";
         agregarEstructura(estructuras_sintacticas, "Sentencia DO_UNTIL ");}
;

bloque_sentencias_do: '{' sentencia_ejecutable_do RETURN '(' expresion ')' ',' '}'
					| '{' sentencia_ejecutable_do '}'
					| '{' RETURN '(' expresion ')' ',' '}'
					| '{' '}' { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
                    | sentencia_ejecutable_do '}' { agregarError(errores_sintacticos, Parser.ERROR, "Se espera un '{' "); }
;

sentencia_ejecutable_do: sentencia_do
                        | sentencia_ejecutable_do sentencia_do
;

sentencia_do:    DO_UNTIL ','
				| DO_UNTIL { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
                | impresion ','
                | impresion { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
                | seleccion_en_do ','
                | seleccion_en_do { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
                | asignacion ','

;

seleccion_en_do: header_if condicion_salto_if if_seleccion_do END_IF {agregarEstructura(estructuras_sintacticas, "Sentencia IF");
                                                                      $$.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
        | header_if condicion_salto_if if_seleccion_do else_seleccion_do END_IF {agregarEstructura(estructuras_sintacticas, "Sentencia IF");
                                                                                 $$.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
        | header_if condicion_salto_if if_seleccion_do bloque_sentencias_do END_IF { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
        | header_if condicion_salto_if if_seleccion_do ELSE END_IF { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
;

header_if: IF {agregarEstructura(estructuras_sintacticas, "Sentencia IF");}
;

if_seleccion_do: bloque_sentencias_do {int i = codigoIntermedio.size();
                                       			   boolean encontrado = false;
                                       			   while (!encontrado && i > 0 ){
                                       			     if (codigoIntermedio.get(i).getOp3().equals("incompleto")) {
                                       			       Terceto t = codigoIntermedio.get(i);
                                       			       t.setOp3(Integer.toString(punteroTerceto+1));
                                       			       encontrado = true;
                                       			     }
                                       			     i = i-1;
                                       			   }
                                       }
 ;

else_seleccion_do: caso_else bloque_sentencias_do {int i = codigoIntermedio.size();
                                                   					     boolean encontrado = false;
                                                   					     while (!encontrado && i > 0 ){
                                                   					     	if (codigoIntermedio.get(i).getOp2().equals("incompleto")) {
                                                   						       Terceto t = codigoIntermedio.get(i);
                                                   						       t.setOp2(Integer.toString(punteroTerceto));
                                                   						       encontrado = true;
                                                   					     	}
                                                   					     	i = i-1;
                                                   					     }
                                                   					    }
				| caso_else ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
;
 
seleccion: header_if condicion_salto_if '{' if_seleccion '}' END_IF {agregarEstructura(estructuras_sintacticas, "Sentencia IF");
																	$$.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
		| header_if condicion_salto_if '{' if_seleccion '}' else_seleccion END_IF {agregarEstructura(estructuras_sintacticas, "Sentencia IF");
											   $$.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
		| header_if condicion_salto_if  if_seleccion END_IF { agregarError(errores_sintacticos, Parser.ERROR, "Se esperan llaves "); }
        | header_if condicion_salto_if if_seleccion begin ejecucion end ',' END_IF { agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
        | header_if condicion_salto_if END_IF { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
        | header_if condicion_salto_if if_seleccion ELSE END_IF { agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
;

if_seleccion: ejecucion {  int i = codigoIntermedio.size();
			   boolean encontrado = false;
			   while (!encontrado && i > 0 ){
			     if (codigoIntermedio.get(i).getOp3().equals("incompleto")) {
			       Terceto t = codigoIntermedio.get(i);
			       t.setOp3(Integer.toString(punteroTerceto+1));
			       encontrado = true;
			     }
			     i = i-1;
			   }
                           }
		| ejecucion RETURN '(' expresion ')' ',' {  int i = codigoIntermedio.size();
                                                         			   boolean encontrado = false;
                                                         			   while (!encontrado && i > 0 ){
                                                         			     if (codigoIntermedio.get(i).getOp3().equals("incompleto")) {
                                                         			       Terceto t = codigoIntermedio.get(i);
                                                         			       t.setOp3(Integer.toString(punteroTerceto+1));
                                                         			       encontrado = true;
                                                         			     }
                                                         			     i = i-1;
                                                         			   }
                                                                                    }
		| RETURN '(' expresion ')' ',' {  int i = codigoIntermedio.size();
                                               			   boolean encontrado = false;
                                               			   while (!encontrado && i > 0 ){
                                               			     if (codigoIntermedio.get(i).getOp3().equals("incompleto")) {
                                               			       Terceto t = codigoIntermedio.get(i);
                                               			       t.setOp3(Integer.toString(punteroTerceto+1));
                                               			       encontrado = true;
                                               			     }
                                               			     i = i-1;
                                               			   }
                                                                          }
		| '(' expresion ')' ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
		| RETURN ',' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
		| ',' { agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias luego de la condicion");}
		| ejecucion ',' { agregarError(errores_sintacticos, Parser.ERROR, "No se espera ',' "); }
;

//TODO agregar error se espera cuerpo dentro llaves else

else_seleccion: caso_else '{' ejecucion '}' {int i = codigoIntermedio.size();
					     boolean encontrado = false;
					     while (!encontrado && i > 0 ){
					     	if (codigoIntermedio.get(i).getOp2().equals("incompleto")) {
						       Terceto t = codigoIntermedio.get(i);
						       t.setOp2(Integer.toString(punteroTerceto));
						       encontrado = true;
					     	}
					     	i = i-1;
					     }
					}

		| caso_else ejecucion RETURN '(' expresion ')'{int i = codigoIntermedio.size();
                                                               					     boolean encontrado = false;
                                                               					     while (!encontrado && i > 0 ){
                                                               					     	if (codigoIntermedio.get(i).getOp2().equals("incompleto")) {
                                                               						       Terceto t = codigoIntermedio.get(i);
                                                               						       t.setOp2(Integer.toString(punteroTerceto));
                                                               						       encontrado = true;
                                                               					     	}
                                                               					     	i = i-1;
                                                               					     }}
        | caso_else RETURN '(' expresion ')'{int i = codigoIntermedio.size();
                                             					     boolean encontrado = false;
                                             					     while (!encontrado && i > 0 ){
                                             					     	if (codigoIntermedio.get(i).getOp2().equals("incompleto")) {
                                             						       Terceto t = codigoIntermedio.get(i);
                                             						       t.setOp2(Integer.toString(punteroTerceto));
                                             						       encontrado = true;
                                             					     	}
                                             					     	i = i-1;
                                             					     }}
        | caso_else '(' expresion ')'  {agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
        | caso_else RETURN  {agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
		| caso_else  {agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
;

caso_else: ELSE {$$.sval = "[" + Integer.toString(generarTerceto("BI","incompleto","-"))+ "]";  }
;


condicion_salto_if: '(' condicion ')' {$$.sval = "[" + Integer.toString(generarTerceto("BF",$2.sval,"incompleto"))+ "]";  }
		| condicion ')'{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera '(' al principio de la condicion");}
		| '(' ')'      { agregarError(errores_sintacticos, Parser.ERROR, "Se espera una condicion");}
;


condicion: expresion_bool
        | condicion comparador expresion_bool {$$.sval = "[" + Integer.toString(generarTerceto($2.sval,$$.sval,$3.sval))+ "]";}
;

expresion_bool: expresion comparador expresion {$$.sval = "[" + Integer.toString(generarTerceto($2.sval,$1.sval,$3.sval))+ "]";
if(!mismoTipo($1.sval,$3.sval, ambito.toString())){agregarError(errores_semanticos, Parser.ERROR, "La operación requiere operandos del mismo tipo");}}

		| expresion comparador {agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion luego del comparador");}
        | comparador expresion {agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion antes del comparador");}
;

comparador: comp_distinto
        | comp_igual
        | comp_mayor_igual
        | comp_menor_igual
        | '<'
        | '>'
;

asignacion: ID '=' '(' expresion ')' {agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
									  int aux = generarTerceto($2.sval,$1.sval,$4.sval);
									  if(!TablaSimbolos.isVariableDeclarada($1.sval)){
									  agregarError(errores_semanticos, Parser.ERROR, $1.sval + " variable no declarada");
									  if(!mismoTipo($1.sval,$3.sval, ambito.toString())){
                                      agregarError(errores_semanticos, Parser.ERROR, "La operación requiere operandos del mismo tipo");
                                      }
							 }
						}

			| ID SUMA '(' expresion ')'{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
										int aux = generarTerceto("+",$1.sval,$4.sval);
										int aux2 = generarTerceto("=",$1.sval,"[" + aux +"]");
										if(!TablaSimbolos.isVariableDeclarada($1.sval)){
										agregarError(errores_semanticos, Parser.ERROR, $1.sval + " variable no declarada");
										if(!mismoTipo($1.sval,$3.sval, ambito.toString())){agregarError(errores_semanticos, Parser.ERROR, "La operación requiere operandos del mismo tipo");

}}
			}
			| ID '=' expresion {agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								int aux = generarTerceto($2.sval,$1.sval,$3.sval);
								if(!TablaSimbolos.isVariableDeclarada($1.sval)){
                                agregarError(errores_semanticos, Parser.ERROR, $1.sval + " variable no declarada");}
                                if(!mismoTipo($1.sval,$3.sval, ambito.toString())){
                                agregarError(errores_semanticos, Parser.ERROR, "La operación requiere operandos del mismo tipo");
}}

			| ID SUMA expresion {agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								 int aux = generarTerceto("+",$1.sval,$3.sval);
                        		 int aux2 = generarTerceto("=",$1.sval,"[" + aux +"]");
                        		 if(!TablaSimbolos.isVariableDeclarada($1.sval)){
                                 agregarError(errores_semanticos, Parser.ERROR, $1.sval + " variable no declarada");
                                 if(!mismoTipo($1.sval,$3.sval, ambito.toString())){agregarError(errores_semanticos, Parser.ERROR, "La operación requiere operandos del mismo tipo");
}}
			}

			| ID SUMA {agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
			| '=' expresion {agregarError(errores_sintacticos, Parser.ERROR, "Se espera un identificador en el lado izquierdo de la asignacion");}
            | ID '='  {agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
;

expresion: expresion '+' termino_positivo  {$$.sval = "[" + Integer.toString(generarTerceto($2.sval,$$.sval,$3.sval))+ "]";}
        | expresion '-' termino_positivo {$$.sval = "[" + Integer.toString(generarTerceto($2.sval,$$.sval,$3.sval))+ "]";}
        | termino
;

termino: termino '*' factor { $$.sval = "[" + Integer.toString(generarTerceto($2.sval,$$.sval,$3.sval))+ "]";}
        | termino '/' factor {$$.sval = "[" + Integer.toString(generarTerceto("/",$$.sval,$3.sval))+ "]";}
		| factor
;

termino_positivo: termino_positivo '*' factor {$$.sval = "[" + Integer.toString(generarTerceto($2.sval,$$.sval,$3.sval))+ "]";}
            | termino_positivo '/' factor {$$.sval = "[" + Integer.toString(generarTerceto("/",$$.sval,$3.sval))+ "]";}
            | factor_positivo
;

factor: ID
        | constante
        | ID '('list_parametros_reales')'{}
;

factor_positivo: ID
                | cte
                | ID '('list_parametros_reales')'{}
;

constante: cte {int ptr_id = TablaSimbolos.obtenerSimbolo($1.sval);
		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo($1.sval));
		TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);}
        | '-' cte {int ptr_id = TablaSimbolos.obtenerSimbolo($2.sval);
                   		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
                   		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo($2.sval));
                   		TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);
                   		String lexema = negarConstante($2.sval);}
;

impresion: PRINT  cadena { String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                          agregarSimbolo(nombre, "cadena ");
                          int puntero = TablaSimbolos.obtenerSimbolo(nombre);
                          int aux = generarTerceto("PRINT", $2.sval, "-");
                          agregarEstructura(estructuras_sintacticas, "Comentario");}
		| PRINT  {agregarError(errores_sintacticos, Parser.ERROR, "Se espera un mensaje luego del PRINT ");}

;

list_parametros_reales: {}
					| parametro_real {}
;

parametro_real: expresion {}
;

// TODO agregar ejecucion al cuerpo?
declaracion_clase: header_clase '{' declaraciones '}' ',' { salirAmbito();
 						tipo = TablaTipos.CLASS_TYPE;
						int ptr_id = TablaSimbolos.obtenerSimbolo($1.sval + Parser.ambito.toString());
						TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de clase");
						TablaSimbolos.agregarAtributo(ptr_id, "tipo", "CLASS_TYPE");
						TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);}

				 | header_clase ','
				 | header_clase '{' '}' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera una declaracion dentro de las llaves");}
				 | CLASS '{' '}' {agregarError(errores_sintacticos, Parser.ERROR, "Se espera una ID previo a las llaves");}
;

header_clase: CLASS ID {agregarEstructura(estructuras_sintacticas, "Declaracion de Clase");
						cambiarAmbito($2.sval);
						Parser.agregarSimbolo($2.sval, "nombre de clase ");
                        int ptr_id = TablaSimbolos.obtenerSimbolo($2.sval);
                        TablaSimbolos.agregarAtributo(ptr_id, "tipo", "CLASS_TYPE");
                        TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de clase");
                        TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);
                         }
;

declaracion_impl: header_impl ':' '{' funciones_impl '}' {salirAmbito();}
;

header_impl: IMPL FOR ID {agregarEstructura(estructuras_sintacticas, "Sentencia de IMPL para el void " + $3.sval);

						 ambito = obtenerAmbito($3.sval);
						 cambiarAmbito($3.sval);}

;

funciones_impl: funciones_impl funcion
			  | funcion
;

%%
public static boolean declarando = true;

public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = ":";
public static String id_funcion;
public static String parametro_funcion;
public static String tipo_var;
public static StringBuilder ambito = new StringBuilder();
public static final HashMap<String,StringBuilder> ambitos = new HashMap<String,StringBuilder>();

public static final List<String> errores_lexicos = new ArrayList<>();
public static final List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> errores_semanticos = new ArrayList<>();
public static final List<String> estructuras_sintacticas = new ArrayList<>();
public static final HashMap<String,HashMap<Integer,Terceto>> estructura_Tercetos = new HashMap<>();
public static final HashMap<Integer,Terceto> codigoIntermedio = new HashMap<>();
public static final HashMap<String,String> funciones = new HashMap<>();
public static final Stack pila = new Stack();
public static int intDeclarando = 0;
private static boolean errores_compilacion;
private static String tipo;
private static List<String> metodo = new ArrayList<>();
private static int punteroTerceto = 1;

private static int contador_cadenas = 0;
public static final String STRING_CHAR = "‘";
void yyerror(String mensaje) {
        // funcion utilizada para imprimir errores que produce yacc
        System.out.println("Error yacc: " + mensaje);
}

int yylex() {
        int identificador_token = 0;
        Reader lector = AnalizadorLexico.lector;
        AnalizadorLexico.estado_actual = 0;

        // Leo hasta que el archivo termine
        while (true) {
                try {
                        if (FileHelper.endOfFile(lector)) {
                                break;
                        }

                        char caracter = FileHelper.getNextCharWithoutAdvancing(lector);
                        identificador_token = AnalizadorLexico.cambiarEstado(lector, caracter);

                        // Si llego a un estado final
                        if (identificador_token != AccionSemantica.TOKEN_ACTIVO) {
                                yylval = new ParserVal(AnalizadorLexico.token_actual.toString());
                                AnalizadorLexico.token_actual.delete(0, AnalizadorLexico.token_actual.length());
                                return identificador_token;
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
return identificador_token;
}

public String negarConstante(String constante) {
        // Si la constante es un numero DOUBLE, la negamos antes de que se agrege a la tabla de simbolos
        int puntero = TablaSimbolos.obtenerSimbolo(constante);
        String nuevo_lexema;

        if (constante.contains(".")) {
                nuevo_lexema = '-' + constante;
        } else {
                agregarError(errores_sintacticos, Parser.WARNING, "El numero largo -" + constante +
                                " fue truncado al valor minimo, ya que es menor que este mismo");
                nuevo_lexema = "0";
        }


        return nuevo_lexema;
}

public static boolean mismoTipo (String str1, String str2, String ambito){
	return TablaTipos.getTipo(str1, ambito).equals(TablaTipos.getTipo(str2, ambito));
}
public static void agregarError(List<String> errores,String errorType , String error) {

        int linea_actual = AnalizadorLexico.getLineaActual();
        errores_compilacion = true;
        errores.add(errorType + " (Linea " + linea_actual + "): " + error);
}

public static void agregarErrorSemantico(int linea, String error){
        errores_compilacion = true;
        errores_semanticos.add(Parser.ERROR + " (Linea " + linea + "): " + error);
}

public static boolean mismoTipoFuncion(String str1, String str2, String ambito){
 return (funciones.get(str1).equals(TablaTipos.getTipo(str2, ambito)));
}


public static void agregarEstructura(List<String> estructuras, String estructura) {

        int linea_actual = AnalizadorLexico.getLineaActual();
        estructuras.add(" (Linea " + linea_actual + "): " + estructura);
}


public static boolean pertenece(String simbolo) {
        // funcion recursiva para controlar si un simbolo se encuentra en la tabla de simbolos
        if (!simbolo.contains(NAME_MANGLING_CHAR)) {
                return false;
        } else if (TablaSimbolos.obtenerSimbolo(simbolo) != TablaSimbolos.NO_ENCONTRADO) {
                return true;
        } else {
                int index = simbolo.lastIndexOf(NAME_MANGLING_CHAR);
                simbolo = simbolo.substring(0, index);
                return pertenece(simbolo);
        }
}

public static void agregarSimbolo(String str, String tipo) {
                    	if (TablaSimbolos.obtenerSimbolo(str) == -1){
				TablaSimbolos.agregarSimbolo(str);

                    	}
                    	else {
                    		String ambitoLexema = TablaSimbolos.obtenerSimboloAmbito(str);
				if (ambito.toString().contains(ambitoLexema)){
					agregarError(errores_semanticos, Parser.ERROR, tipo + str + " ya declarada");
				}
				else{
					TablaSimbolos.agregarSimbolo(str);

				}
                    	}


}

public static void agregarFuncion (String str1, String str2){
 funciones.put(str1, str2);
}

//--FUNCIONES DE IMPRESION Y MAIN--//

public static void imprimirErrores(List<String> errores, String cabecera) {
        // Imprimo los errores encontrados en el programa
        if (!errores.isEmpty()) {
                System.out.println();
                System.out.println(cabecera + ":");

                for (String error: errores) {
                        System.out.println(error);
                }
        }
}

public static void imprimirEstructuras(List<String> estructuras, String cabecera) {
        // Imprimo las estructuras encontradas en el programa
		if (!estructuras.isEmpty()) {
                        System.out.println();
                        System.out.println(cabecera + ":");

                        for (String estructura: estructuras) {
                                System.out.println(estructura);
                        }
		}
}

//--TERCETOS--//
public int generarTerceto(String op1, String op2, String op3){
	Terceto t = new Terceto(op1, op2, op3);
	codigoIntermedio.put(punteroTerceto,t);
	punteroTerceto = punteroTerceto + 1;
	return punteroTerceto -1;
}

public StringBuilder obtenerAmbito(String id){
	if (!ambitos.containsKey(id)){
	System.out.println("El metodo a implementar no se encuentra o ya fue definido");}
	return ambitos.get(id);
}

public static void agregarListaTercetos(String s, HashMap<Integer,Terceto> t){
	HashMap<Integer,Terceto> aux = new HashMap<Integer,Terceto>(t);
	estructura_Tercetos.put(s, aux);
	t.clear();
	punteroTerceto = 1;
}

public static void imprimirListaTercetos() {
	// Imprimo la lista de Tercetos
	if (!estructura_Tercetos.isEmpty()) {
        for (String s:  estructura_Tercetos.keySet()) {
          HashMap<Integer,Terceto> hm = estructura_Tercetos.get(s);
          System.out.print("Llave :" + s);
          imprimirTercetos(hm);
        }
    }
}

public static void imprimirTercetos(HashMap<Integer,Terceto> hm) {
  // Imprimo los Tercetos
  if (!hm.isEmpty()) {
    System.out.println();
    int nroTerceto = 1;

    for (Terceto terceto : hm.values()) {
      System.out.print("Terceto " + nroTerceto + ": ");
      terceto.print();
      nroTerceto = nroTerceto + 1;
    }
  }
  else {
      System.out.println("Terceto Vacio " + hm);
  }
}

private static void cambiarAmbito(String nuevo_ambito) {
        //recibe el ID de una funcion, y lo concantenac con ambito
        ambito.append(NAME_MANGLING_CHAR).append(nuevo_ambito);
}

private static void salirAmbito() {
        //la funcion salirAmbito modifica el atributo ambito, quitandole todos los caracteres hasta el ':'
        int index = ambito.lastIndexOf(NAME_MANGLING_CHAR);
        ambito.delete(index, ambito.length());
}


private static String nombreFuncion() {
        // Ultimo name mangling char
        int ultimo_nmc = ambito.lastIndexOf(NAME_MANGLING_CHAR);
        String nombre_funcion = ambito.substring(ultimo_nmc + 1);
        return nombre_funcion + ambito.substring(0, ultimo_nmc);
}

public static void main(String[] args) {

	 	Scanner scanner = new Scanner(System.in);
	  	System.out.print("Ingrese el nombre completo del archivo con la extension .txt a compilar: ");
		String fileName = scanner.nextLine();
		String userDir = System.getProperty("user.dir");
		String archivo_a_leer = userDir + "\\Compiladores_g27\\src\\compilador\\" + fileName;
                System.out.println("Se esta compilando el siguiente archivo: " + archivo_a_leer);

                try {
                        AnalizadorLexico.lector = new BufferedReader(new FileReader(archivo_a_leer));
                        Parser parser = new Parser();
                        parser.run();
                } catch (IOException excepcion) {
                        excepcion.printStackTrace();
                }

				Parser.imprimirListaTercetos();
				TablaSimbolos.imprimirTabla();
                Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
                Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
                Parser.imprimirErrores(errores_semanticos, "Errores Semanticos");
                Parser.imprimirEstructuras(estructuras_sintacticas, "Estructuras");

        }



