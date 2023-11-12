//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "gramatica.y"
package compilador;
import accionesSemanticas.AccionSemantica;

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
//#line 27 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short ID=257;
public final static short cte=258;
public final static short cadena=259;
public final static short IF=260;
public final static short ELSE=261;
public final static short END_IF=262;
public final static short PRINT=263;
public final static short VOID=264;
public final static short RETURN=265;
public final static short DO=266;
public final static short UNTIL=267;
public final static short CLASS=268;
public final static short comp_menor_igual=269;
public final static short comp_mayor_igual=270;
public final static short comp_distinto=271;
public final static short comp_igual=272;
public final static short SUMA=273;
public final static short UINT=274;
public final static short DOUBLE=275;
public final static short SHORT=276;
public final static short IMPL=277;
public final static short FOR=278;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    0,    0,    0,    0,    2,    1,    4,    5,
    3,    3,    3,    3,    3,    6,    6,    8,    8,    8,
    8,   12,    9,    9,   13,   13,   10,   10,   10,   15,
   15,   16,   18,   18,   20,   20,   19,   19,   22,   22,
   22,   22,   14,   14,   14,   21,   23,   23,   24,    7,
    7,   25,   26,   26,   26,   26,   26,   31,   31,   31,
   31,   30,   30,   32,   33,   33,   33,   33,   33,   35,
   35,   37,   37,   37,   37,   37,   37,   37,   38,   38,
   38,   38,   40,   41,   41,   28,   28,   28,   28,   28,
   28,   42,   42,   42,   42,   42,   42,   42,   43,   43,
   43,   43,   43,   43,   39,   39,   39,   34,   34,   44,
   44,   44,   45,   45,   45,   45,   45,   45,   27,   27,
   27,   27,   27,   27,   27,   36,   36,   36,   47,   47,
   47,   46,   46,   46,   48,   48,   48,   49,   49,   49,
   50,   50,   52,   29,   29,   51,   51,   53,   11,   11,
   11,   11,   17,   54,   54,
};
final static short yylen[] = {                            2,
    5,    4,    4,    4,    4,    1,    1,    1,    1,    1,
    2,    1,    1,    2,    3,    2,    1,    1,    1,    1,
    1,    3,    3,    2,    1,    3,    1,    1,    1,    8,
    7,    5,    2,    1,    2,    1,    0,    1,    2,    1,
    1,    2,    1,    1,    1,    1,    2,    1,    1,    2,
    1,    1,    2,    2,    2,    2,    2,    5,    7,   10,
    6,    6,    5,    1,    8,    3,    7,    2,    2,    1,
    2,    2,    1,    2,    1,    2,    1,    2,    4,    5,
    5,    5,    1,    2,    2,    6,    4,    7,    8,    3,
    5,    1,    6,    5,    4,    2,    1,    2,    4,    6,
    5,    4,    2,    1,    3,    2,    2,    1,    3,    3,
    2,    2,    1,    1,    1,    1,    1,    1,    5,    5,
    3,    3,    2,    2,    2,    3,    3,    1,    3,    3,
    1,    3,    3,    1,    1,    1,    4,    1,    1,    4,
    1,    2,    0,    3,    1,    0,    1,    1,    5,    3,
    4,    3,    7,    2,    1,
};
final static short yydefred[] = {                         0,
   10,    6,    8,    0,    0,    0,    7,    0,    0,    0,
    0,    0,   64,    0,   43,   45,   44,    0,    0,    0,
    0,    0,    0,   17,   18,   19,   20,   21,    0,    0,
   27,   28,   29,    0,   51,   52,    0,    0,    0,    0,
    0,    0,    0,   25,    0,    0,    0,    0,    0,  141,
  116,  115,  113,  114,    0,  118,  117,    0,    0,    0,
    0,  108,    0,    0,  131,  136,  143,   33,    0,    0,
    0,    0,    9,    0,    0,    0,    0,   16,    0,   50,
   24,    0,    0,    0,   53,   54,   55,   56,   57,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   70,    0,
    0,    0,    0,    0,    0,    0,   22,    0,  142,  107,
    0,  106,    0,    0,    0,    0,    0,   90,    0,   97,
    0,    0,    0,    0,    0,    0,    0,  144,  150,    0,
  152,    0,    3,    5,    2,    4,    0,    0,   26,   23,
   40,    0,    0,    0,   38,    0,    0,    0,   68,    0,
   78,   74,   72,    0,   69,   71,   76,    1,    0,    0,
    0,    0,    0,    0,  147,  105,  109,    0,  139,    0,
  134,    0,    0,   96,    0,    0,    0,    0,   98,    0,
   87,    0,  129,  130,  151,    0,    0,   42,   39,    0,
   83,    0,    0,    0,    0,   66,    0,  120,    0,    0,
  119,  137,    0,    0,    0,    0,    0,    0,    0,   91,
    0,  149,    0,   32,    0,    0,   79,    0,    0,   63,
    0,    0,    0,   61,    0,    0,  132,  133,    0,    0,
   86,    0,   95,    0,    0,  155,    0,    0,    0,    0,
    0,   36,    0,   48,   49,   82,   85,   84,   81,   80,
    0,    0,   62,    0,  140,   94,    0,    0,    0,    0,
   88,    0,    0,    0,  153,  154,   31,   35,    0,   47,
    0,    0,    0,    0,    0,    0,    0,   93,   89,    0,
   30,   67,    0,    0,    0,   99,  102,    0,    0,   65,
   60,  101,    0,  100,
};
final static short yydgoto[] = {                          4,
    5,    6,   21,   75,    7,   22,   23,   24,   25,   26,
   27,   28,   29,   30,   31,   32,   33,   34,  144,  241,
  242,  145,  243,  244,   35,   36,   37,   38,   39,   40,
   41,   42,   97,   59,   98,   60,   99,  100,   61,  192,
  219,  124,  232,   62,   63,  170,   64,   65,  171,   66,
  164,  128,  165,  238,
};
final static short yysindex[] = {                       -21,
    0,    0,    0,    0, -243,  138,    0,  218,  -34,  -38,
 -217, -211,    0, -103,    0,    0,    0, -201,  -42,  164,
  -17,  218,  218,    0,    0,    0,    0,    0,  111, -130,
    0,    0,    0,  104,    0,    0,  113,  130,  143,  144,
  153,  256,  -17,    0,   54,  -35,   73,  120,  190,    0,
    0,    0,    0,    0,   -7,    0,    0,    6,    2,   64,
   88,    0,  -42,  154,    0,    0,    0,    0,   37,  116,
   -1,  167,    0,  -33,  226,  236,  218,    0,  395,    0,
    0,    5,  123,  168,    0,    0,    0,    0,    0,  -56,
  -38,  243,  245,  246,  251,  237,   34,  321,    0,  279,
  294,  -42,  167,   74,  -42,  167,    0,  -42,    0,    0,
   22,    0,   71, -121, -121,  -42,  -36,    0,  169,    0,
   98,  -42,  134,  -85,  167,  -42,  -42,    0,    0,  360,
    0,  286,    0,    0,    0,    0,  395, -130,    0,    0,
    0,   90,   92,  311,    0,  296,   71,  326,    0,  271,
    0,    0,    0,  333,    0,    0,    0,    0,  207,  168,
  117,  388,  167,  335,    0,    0,    0,  340,    0,  182,
    0,  182,  167,    0,  -42,  260,  417,  348,    0,  147,
    0,  188,    0,    0,    0,  374,  266,    0,    0,   75,
    0,  285,   33,  -42,  352,    0,   71,    0,  364,  365,
    0,    0,  -42,  -42,  -42,  431,  -81,  366,  -42,    0,
  322,    0,  152,    0,  193,  124,    0,  155,  156,    0,
  446,  -42,   38,    0,  165,  382,    0,    0,  390,  112,
    0,  175,    0,  455,  405,    0,  386, -116,  408,  218,
  330,    0,  188,    0,    0,    0,    0,    0,    0,    0,
  419,  484,    0,  437,    0,    0,  439,  188,  -42,  332,
    0,  436,  221,  168,    0,    0,    0,    0,  444,    0,
  372,  457,  168,  -42,  346,  559,  450,    0,    0,  462,
    0,    0,  379,  465,  570,    0,    0,  -42,  384,    0,
    0,    0,  598,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  127,    0,
  102,  469,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    9,   26,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  477,    0,  480,    0,  -26,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -4,    0,    0,    0,    0,    0,    0,
    0,  482,    0,    0,    0,    0,   36,    0,   67,    0,
    0,    0,    0,  479,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  278,  295,    0,    0,    0,  310,
    0,    0,  486,    0,    0,  488,    0,  492,    0,    0,
    0,    0,    0,    0,    0,   44,    0,    0,    0,    0,
    0,    0,   20,    0,   50,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   77,  127,    0,    0,
    0,    0,  498,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  479,
    0,    0,  499,    0,    0,    0,    0,  -12,    0,   16,
    0,   28,   55,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  505,
    0,    0,  492,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  252,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  425,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  510,    0,    0,  302,    0,    0,    0,
    0,    0,    0,  479,    0,    0,    0,    0,    0,    0,
    0,    0,  479,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   -2,  552,  186,  126,    0,  -10,  -16,  -14,    0,    0,
    0,    0,   45,  200,  -88,    0,    0,  -73, -111,    0,
  325,    0,    0,  329,  -22,    0,  470,    0,  473,  476,
    0,    0,   12,   -6,  487,  -19,  -11,    0,  495,    0,
    0,  456,    0,  478,  406,  468,    0,    3,    0,    0,
  387,    0,    0,    0,
};
final static int YYTABLESIZE=692;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         72,
   80,   58,   55,   20,   47,   77,   55,   78,  265,   46,
  133,   46,   79,    1,  135,  135,  135,  135,  135,   70,
  135,   57,    2,   56,   47,  103,   47,  106,  138,  138,
  138,  138,  138,  135,  138,  135,  128,    3,  128,  128,
  128,   67,  112,  125,  123,   68,  110,  138,  199,  138,
   55,  111,   12,   48,   80,  128,  126,  128,  126,  126,
  126,   57,  166,   56,   78,   57,  137,   56,  127,   13,
  127,  127,  127,  220,   83,  126,   71,  126,  253,   11,
  129,   57,  159,   56,  111,  162,  156,  127,  163,  127,
  112,   73,   57,  102,   56,  110,  173,   57,   55,   56,
   80,    3,  177,  111,  123,  111,  114,   73,  115,  112,
   14,  112,  105,  160,  110,   55,  110,   55,  214,  186,
   15,  182,   78,   57,  236,   56,   44,  122,  183,  184,
   57,  120,   56,   12,  161,  168,  169,  122,  156,  237,
  193,  120,   92,   84,   92,  145,   76,   12,   19,  266,
   13,  259,  280,   69,   81,  206,   85,  191,   19,  130,
   11,  284,  145,  107,  237,  211,  140,  247,  101,   82,
   25,   78,   19,   86,  221,  180,  181,  179,   82,  230,
  231,   82,   48,  163,   19,   25,   87,   88,   80,  234,
  223,   14,  245,   43,   19,  126,   89,  215,   19,  134,
  127,   15,  252,  218,  240,   74,  227,  228,  175,  114,
  121,  115,  174,  260,   49,   50,   45,  245,   49,   50,
  245,  104,   44,  204,   19,   78,  145,  248,  205,  108,
   51,   52,   53,   54,  258,    1,   45,   80,   45,  276,
  131,  275,  135,  135,  135,  135,   93,  198,   19,  114,
  109,  115,   80,   19,  285,  132,  138,  138,  138,  138,
    3,  139,   49,   50,  128,  128,  128,  128,  293,  135,
   51,   52,   53,   54,   51,   52,   53,   54,   19,  136,
   92,   92,  147,  143,  126,  126,  126,  126,   73,  151,
   51,   52,   53,   54,  152,  153,  127,  127,  127,  127,
  154,   51,   52,   53,   54,   19,   51,   52,   53,   54,
   49,   50,  111,  111,  111,  111,   19,  239,  112,  112,
  112,  112,  157,  110,  110,  110,  110,   49,   50,   49,
   50,   19,   51,   52,   53,   54,  235,  158,   75,   51,
   52,   53,   54,  187,  117,   19,  188,   10,  189,  118,
   11,  190,  119,   13,  117,   73,   19,   10,  145,  143,
   11,  145,  119,   13,  145,  194,  145,  145,  117,  149,
   77,   10,  197,  200,   11,  202,  257,   13,   93,  203,
   90,   19,   19,   91,  207,  246,   11,  209,  213,   13,
  117,  222,   19,   10,    9,  196,   11,   10,  178,   13,
   11,   12,   75,   13,  224,   14,   19,   93,  210,  233,
  225,   15,   16,   17,   18,   12,  249,  250,   93,   73,
    9,  254,  255,   10,  141,  264,   11,   12,  201,   13,
  114,   14,  115,  256,   77,  142,  261,   15,   16,   17,
   18,   15,   16,   17,  117,  155,   73,   10,  263,    9,
   11,  267,   10,   13,  269,   11,   12,  208,   13,  114,
   14,  115,  271,  143,  113,  116,   15,   16,   17,   18,
  286,  229,  143,  114,    9,  115,  273,   10,  274,  278,
   11,   12,  279,   13,  185,   14,  251,  281,  114,  288,
  115,   15,   16,   17,   18,  262,  282,  114,  212,  115,
  283,   90,  289,  290,   91,  291,  215,   11,   34,  148,
   13,   94,   90,  104,   95,   91,  113,   96,   11,   37,
  123,   13,   92,  125,  272,  124,  114,   90,  115,  122,
   91,  121,  146,   11,   75,  195,   13,   75,   41,  148,
   75,   90,   75,   75,   91,  216,  217,   11,   58,   46,
   13,   73,   90,   59,   73,   91,    8,   73,   11,   73,
   73,   13,   94,  103,  268,   95,   77,   94,   96,   77,
   95,  270,   77,   96,   77,   77,  176,   90,  117,  150,
   91,   10,  172,   11,   11,  146,   13,   13,  117,  226,
  167,   10,    0,    0,   11,    0,  277,   13,  113,  287,
    0,  114,  117,  115,    0,   10,    0,    0,   11,    0,
  292,   13,  114,    0,  115,   94,  138,    0,   95,   94,
    0,   96,   95,   12,    0,   96,    0,   14,  113,    0,
  138,    0,    0,   15,   16,   17,   18,   12,  294,    0,
  114,   14,  115,    0,    0,    0,    0,   15,   16,   17,
   18,  138,    0,    0,    0,    0,    0,    0,   12,    0,
    0,   94,   14,    0,   95,    0,    0,   96,   15,   16,
   17,   18,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   94,    0,    0,   95,    0,
    0,   96,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         19,
   23,   40,   45,    6,   61,   22,   45,   22,  125,   46,
   44,   46,   23,  257,   41,   42,   43,   44,   45,  123,
   47,   60,   44,   62,   61,   45,   61,   47,   41,   42,
   43,   44,   45,   60,   47,   62,   41,  123,   43,   44,
   45,  259,   41,   63,   61,  257,   41,   60,  160,   62,
   45,   58,   44,    9,   77,   60,   41,   62,   43,   44,
   45,   60,   41,   62,   79,   60,   77,   62,   41,   44,
   43,   44,   45,   41,   30,   60,  278,   62,   41,   44,
   44,   60,  102,   62,   41,  105,   98,   60,  108,   62,
   41,  125,   60,   40,   62,   41,  116,   60,   45,   62,
  123,  123,  122,   60,  121,   62,   43,  125,   45,   60,
   44,   62,   40,   40,   60,   45,   62,   45,   44,  130,
   44,  124,  137,   60,  213,   62,  257,   40,  126,  127,
   60,   44,   62,  125,   61,  257,  258,   40,  150,  213,
  147,   44,  123,   40,  125,   44,   21,  264,   61,  238,
  125,   40,  264,  257,   44,  175,   44,  146,   61,  123,
  125,  273,   61,   44,  238,  182,   44,   44,   43,   59,
   44,  186,   61,   44,  194,  261,  262,   44,   59,  261,
  262,   59,  138,  203,   61,   59,   44,   44,  211,  209,
  197,  125,  215,    8,   61,   42,   44,  123,   61,   74,
   47,  125,  222,  192,  215,   20,  204,  205,   40,   43,
  123,   45,   44,  230,  257,  258,  273,  240,  257,  258,
  243,  257,  257,   42,   61,  240,  125,  216,   47,   40,
  269,  270,  271,  272,  123,  257,  273,  260,  273,  259,
  125,  258,  269,  270,  271,  272,  123,   41,   61,   43,
  258,   45,  275,   61,  274,  257,  269,  270,  271,  272,
  123,  257,  257,  258,  269,  270,  271,  272,  288,   44,
  269,  270,  271,  272,  269,  270,  271,  272,   61,   44,
  261,  262,   40,   84,  269,  270,  271,  272,  125,   44,
  269,  270,  271,  272,   44,   59,  269,  270,  271,  272,
  267,  269,  270,  271,  272,   61,  269,  270,  271,  272,
  257,  258,  269,  270,  271,  272,   61,  125,  269,  270,
  271,  272,   44,  269,  270,  271,  272,  257,  258,  257,
  258,   61,  269,  270,  271,  272,  211,   44,   61,  269,
  270,  271,  272,   58,  257,   61,  257,  260,  257,  262,
  263,   41,  265,  266,  257,   61,   61,  260,  257,  160,
  263,  260,  265,  266,  263,   40,  265,  266,  257,  125,
   61,  260,   40,  257,  263,   41,  265,  266,  123,   40,
  257,   61,   61,  260,  125,  262,  263,   40,  123,  266,
  257,   40,   61,  260,  257,  125,  263,  260,  265,  266,
  263,  264,  125,  266,   41,  268,   61,  123,  262,   44,
   46,  274,  275,  276,  277,  264,  262,  262,  123,  125,
  257,  257,   41,  260,  257,   40,  263,  264,   41,  266,
   43,  268,   45,   44,  125,  268,  262,  274,  275,  276,
  277,  274,  275,  276,  257,  125,  125,  260,   44,  257,
  263,   44,  260,  266,  125,  263,  264,   41,  266,   43,
  268,   45,   44,  264,   59,   60,  274,  275,  276,  277,
  125,   41,  273,   43,  257,   45,   40,  260,   40,   44,
  263,  264,  262,  266,  125,  268,   41,   44,   43,   40,
   45,  274,  275,  276,  277,   41,  125,   43,  125,   45,
   44,  257,   41,  125,  260,   41,  123,  263,   40,  265,
  266,   42,  257,  262,   42,  260,  111,   42,  263,   41,
   44,  266,  267,   44,   41,   44,   43,  257,   45,   44,
  260,   44,   41,  263,  257,  265,  266,  260,   41,   41,
  263,  257,  265,  266,  260,  261,  262,  263,   44,  125,
  266,  257,  257,   44,  260,  260,    5,  263,  263,  265,
  266,  266,   93,  262,  240,   93,  257,   98,   93,  260,
   98,  243,  263,   98,  265,  266,  121,  257,  257,   93,
  260,  260,  115,  263,  263,   91,  266,  266,  257,  203,
  113,  260,   -1,   -1,  263,   -1,  265,  266,  193,   41,
   -1,   43,  257,   45,   -1,  260,   -1,   -1,  263,   -1,
   41,  266,   43,   -1,   45,  146,  257,   -1,  146,  150,
   -1,  146,  150,  264,   -1,  150,   -1,  268,  223,   -1,
  257,   -1,   -1,  274,  275,  276,  277,  264,   41,   -1,
   43,  268,   45,   -1,   -1,   -1,   -1,  274,  275,  276,
  277,  257,   -1,   -1,   -1,   -1,   -1,   -1,  264,   -1,
   -1,  192,  268,   -1,  192,   -1,   -1,  192,  274,  275,
  276,  277,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  216,   -1,   -1,  216,   -1,
   -1,  216,
};
}
final static short YYFINAL=4;
final static short YYMAXTOKEN=278;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,"ID","cte","cadena","IF","ELSE","END_IF",
"PRINT","VOID","RETURN","DO","UNTIL","CLASS","comp_menor_igual",
"comp_mayor_igual","comp_distinto","comp_igual","SUMA","UINT","DOUBLE","SHORT",
"IMPL","FOR",
};
final static String yyrule[] = {
"$accept : program",
"program : begin header_program cuerpo_prog end ','",
"program : header_program begin end ','",
"program : header_program begin cuerpo_prog ','",
"program : header_program cuerpo_prog end ','",
"program : header_program begin cuerpo_prog end",
"program : ','",
"header_program : nombre_programa",
"begin : '{'",
"end : '}'",
"nombre_programa : ID",
"cuerpo_prog : declaraciones ejecucion",
"cuerpo_prog : declaraciones",
"cuerpo_prog : ejecucion",
"cuerpo_prog : ejecucion declaraciones",
"cuerpo_prog : declaraciones ejecucion declaraciones",
"declaraciones : declaraciones declaracion",
"declaraciones : declaracion",
"declaracion : declaracion_variables",
"declaracion : declaracion_funcion",
"declaracion : declaracion_clase",
"declaracion : declaracion_variables_clase",
"declaracion_variables_clase : ID list_var ','",
"declaracion_variables : tipo list_var ','",
"declaracion_variables : list_var ','",
"list_var : ID",
"list_var : list_var ';' ID",
"declaracion_funcion : funcion",
"declaracion_funcion : funcion_sin_definir",
"declaracion_funcion : declaracion_impl",
"funcion : header_funcion '(' list_de_parametros ')' '{' cuerpo_de_la_funcion '}' ','",
"funcion : header_funcion '(' list_de_parametros ')' '{' '}' ','",
"funcion_sin_definir : header_funcion '(' list_de_parametros ')' ','",
"header_funcion : VOID ID",
"header_funcion : VOID",
"cuerpo_de_la_funcion : declaraciones ejecucion_funcion",
"cuerpo_de_la_funcion : ejecucion_funcion",
"list_de_parametros :",
"list_de_parametros : parametro",
"parametro : tipo ID",
"parametro : ID",
"parametro : tipo",
"parametro : CLASS ID",
"tipo : UINT",
"tipo : SHORT",
"tipo : DOUBLE",
"ejecucion_funcion : bloque_funcion",
"bloque_funcion : bloque_funcion sentencia_funcion",
"bloque_funcion : sentencia_funcion",
"sentencia_funcion : sentencia",
"ejecucion : ejecucion sentencia",
"ejecucion : sentencia",
"sentencia : sentencia_ejecutable",
"sentencia_ejecutable : asignacion ','",
"sentencia_ejecutable : seleccion ','",
"sentencia_ejecutable : impresion ','",
"sentencia_ejecutable : DO_UNTIL ','",
"sentencia_ejecutable : referencia_clase ','",
"referencia_clase : ID '.' ID '=' ID",
"referencia_clase : ID '.' ID '=' ID '.' ID",
"referencia_clase : ID '.' ID '=' ID '.' ID '(' list_de_parametros ')'",
"referencia_clase : ID '.' ID '(' list_de_parametros ')'",
"DO_UNTIL : pdo bloque_sentencias_do UNTIL '(' condicion ')'",
"DO_UNTIL : pdo UNTIL '(' condicion ')'",
"pdo : DO",
"bloque_sentencias_do : '{' sentencia_ejecutable_do RETURN '(' expresion ')' ',' '}'",
"bloque_sentencias_do : '{' sentencia_ejecutable_do '}'",
"bloque_sentencias_do : '{' RETURN '(' expresion ')' ',' '}'",
"bloque_sentencias_do : '{' '}'",
"bloque_sentencias_do : sentencia_ejecutable_do '}'",
"sentencia_ejecutable_do : sentencia_do",
"sentencia_ejecutable_do : sentencia_ejecutable_do sentencia_do",
"sentencia_do : DO_UNTIL ';'",
"sentencia_do : DO_UNTIL",
"sentencia_do : impresion ','",
"sentencia_do : impresion",
"sentencia_do : seleccion_en_do ','",
"sentencia_do : seleccion_en_do",
"sentencia_do : asignacion ','",
"seleccion_en_do : IF condicion_salto_if if_seleccion_do END_IF",
"seleccion_en_do : IF condicion_salto_if if_seleccion_do else_seleccion_do END_IF",
"seleccion_en_do : IF condicion_salto_if if_seleccion_do bloque_sentencias_do END_IF",
"seleccion_en_do : IF condicion_salto_if if_seleccion_do ELSE END_IF",
"if_seleccion_do : bloque_sentencias_do",
"else_seleccion_do : ELSE bloque_sentencias_do",
"else_seleccion_do : ELSE ','",
"seleccion : IF condicion_salto_if '{' if_seleccion '}' END_IF",
"seleccion : IF condicion_salto_if if_seleccion END_IF",
"seleccion : IF condicion_salto_if '{' if_seleccion '}' else_seleccion END_IF",
"seleccion : IF condicion_salto_if if_seleccion begin ejecucion end ',' END_IF",
"seleccion : IF condicion_salto_if END_IF",
"seleccion : IF condicion_salto_if if_seleccion ELSE END_IF",
"if_seleccion : ejecucion",
"if_seleccion : ejecucion RETURN '(' expresion ')' ','",
"if_seleccion : RETURN '(' expresion ')' ','",
"if_seleccion : '(' expresion ')' ','",
"if_seleccion : RETURN ','",
"if_seleccion : ','",
"if_seleccion : ejecucion ','",
"else_seleccion : ELSE '{' ejecucion '}'",
"else_seleccion : ELSE ejecucion RETURN '(' expresion ')'",
"else_seleccion : ELSE RETURN '(' expresion ')'",
"else_seleccion : ELSE '(' expresion ')'",
"else_seleccion : ELSE RETURN",
"else_seleccion : ELSE",
"condicion_salto_if : '(' condicion ')'",
"condicion_salto_if : condicion ')'",
"condicion_salto_if : '(' ')'",
"condicion : expresion_bool",
"condicion : condicion comparador expresion_bool",
"expresion_bool : expresion comparador expresion",
"expresion_bool : expresion comparador",
"expresion_bool : comparador expresion",
"comparador : comp_distinto",
"comparador : comp_igual",
"comparador : comp_mayor_igual",
"comparador : comp_menor_igual",
"comparador : '<'",
"comparador : '>'",
"asignacion : ID '=' '(' expresion ')'",
"asignacion : ID SUMA '(' expresion ')'",
"asignacion : ID '=' expresion",
"asignacion : ID SUMA expresion",
"asignacion : ID SUMA",
"asignacion : '=' expresion",
"asignacion : ID '='",
"expresion : expresion '+' termino_positivo",
"expresion : expresion '-' termino_positivo",
"expresion : termino",
"termino : termino '*' factor",
"termino : termino '/' factor",
"termino : factor",
"termino_positivo : termino_positivo '*' factor",
"termino_positivo : termino_positivo '/' factor",
"termino_positivo : factor_positivo",
"factor : ID",
"factor : constante",
"factor : ID '(' list_parametros_reales ')'",
"factor_positivo : ID",
"factor_positivo : cte",
"factor_positivo : ID '(' list_parametros_reales ')'",
"constante : cte",
"constante : '-' cte",
"$$1 :",
"impresion : PRINT cadena $$1",
"impresion : PRINT",
"list_parametros_reales :",
"list_parametros_reales : parametro_real",
"parametro_real : expresion",
"declaracion_clase : CLASS ID '{' declaraciones '}'",
"declaracion_clase : CLASS ID ','",
"declaracion_clase : CLASS ID '{' '}'",
"declaracion_clase : CLASS '{' '}'",
"declaracion_impl : IMPL FOR ID ':' '{' funciones_impl '}'",
"funciones_impl : funciones_impl funcion",
"funciones_impl : funcion",
};

//#line 321 "gramatica.y"
public static boolean declarando = true;

public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = ".";

public static StringBuilder ambito = new StringBuilder();

public static final List<String> errores_lexicos = new ArrayList<>();
public static final List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> errores_semanticos = new ArrayList<>();
public static final List<String> estructuras_sintacticas = new ArrayList<>();


private static boolean errores_compilacion;
private static String tipo;

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
	AnalizadorLexico.imprimirTokensDetectados();
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


public static void agregarError(List<String> errores,String errorType , String error) {

        int linea_actual = AnalizadorLexico.getLineaActual();
        errores_compilacion = true;
        errores.add(errorType + " (Linea " + linea_actual + "): " + error);
}

public static void agregarErrorSemantico(int linea, String error){
        errores_compilacion = true;
        errores_semanticos.add(Parser.ERROR + " (Linea " + linea + "): " + error);
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

public static boolean chequearParametro(String parametro, String funcion) {
        //esta funcion chequea si el tipo de un parametro es valido para una funcion
        int puntero_parametro = TablaSimbolos.obtenerSimboloAmbito(parametro);
        int puntero_funcion = TablaSimbolos.obtenerSimboloAmbito(funcion);

        String tipoParametro = TablaSimbolos.obtenerAtributo(puntero_parametro, "tipo");
        String tipoFuncion = TablaSimbolos.obtenerAtributo(puntero_funcion, "tipo_parametro");

        return tipoParametro == tipoFuncion;
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

				TablaSimbolos.imprimirTabla();
                Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
                Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
                Parser.imprimirEstructuras(estructuras_sintacticas, "Estructuras");

        }
//#line 768 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 2:
//#line 27 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaban sentencias de ejecucion");}
break;
case 3:
//#line 28 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un '}' al final del programa");}
break;
case 4:
//#line 29 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un '{' antes de las sentencias");}
break;
case 5:
//#line 30 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un ',' al final del programa");}
break;
case 6:
//#line 31 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un programa");}
break;
case 7:
//#line 34 "gramatica.y"
{ Parser.declarando = true;}
break;
case 12:
//#line 47 "gramatica.y"
{Parser.declarando = false;}
break;
case 14:
//#line 49 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
break;
case 15:
//#line 50 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
break;
case 19:
//#line 60 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Declaracion de Funcion");}
break;
case 20:
//#line 61 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Declaracion de Clase");}
break;
case 25:
//#line 72 "gramatica.y"
{
                             int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                             TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                             TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");
                             }
break;
case 26:
//#line 77 "gramatica.y"
{
                                          int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(2).sval + Parser.ambito.toString());
                                          TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                          TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");
                                          }
break;
case 31:
//#line 90 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el cuerpo de la funcion");}
break;
case 33:
//#line 97 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);}
break;
case 34:
//#line 98 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera nombre de la funcion");}
break;
case 39:
//#line 110 "gramatica.y"
{}
break;
case 40:
//#line 111 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el tipo del parametro");}
break;
case 41:
//#line 112 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el nombre del parametro");}
break;
case 43:
//#line 117 "gramatica.y"
{tipo = TablaTipos.UINT_TYPE;}
break;
case 44:
//#line 118 "gramatica.y"
{tipo = TablaTipos.SHORT_TYPE;}
break;
case 45:
//#line 119 "gramatica.y"
{tipo = TablaTipos.DOUBLE_TYPE;}
break;
case 46:
//#line 122 "gramatica.y"
{}
break;
case 62:
//#line 154 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia DO_UNTIL ");}
break;
case 63:
//#line 155 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
break;
case 68:
//#line 164 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
break;
case 69:
//#line 165 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera un '{' "); }
break;
case 73:
//#line 173 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 75:
//#line 175 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 77:
//#line 177 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 79:
//#line 182 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF en DO");}
break;
case 80:
//#line 183 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");}
break;
case 81:
//#line 184 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 82:
//#line 185 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 85:
//#line 194 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 86:
//#line 197 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");}
break;
case 87:
//#line 198 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan llaves "); }
break;
case 89:
//#line 200 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 90:
//#line 201 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
break;
case 91:
//#line 202 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 95:
//#line 210 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 96:
//#line 212 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 97:
//#line 213 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias luego de la condicion");}
break;
case 98:
//#line 214 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "No se espera ',' "); }
break;
case 102:
//#line 222 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 103:
//#line 223 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 104:
//#line 224 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 106:
//#line 229 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera '(' al principio de la condicion");}
break;
case 107:
//#line 230 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera una condicion");}
break;
case 111:
//#line 239 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion luego del comparador");}
break;
case 112:
//#line 240 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion antes del comparador");}
break;
case 119:
//#line 251 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");}
break;
case 120:
//#line 252 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");}
break;
case 121:
//#line 253 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");}
break;
case 122:
//#line 254 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");}
break;
case 123:
//#line 255 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 124:
//#line 256 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un identificador en el lado izquierdo de la asignacion");}
break;
case 125:
//#line 257 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 137:
//#line 277 "gramatica.y"
{}
break;
case 140:
//#line 282 "gramatica.y"
{}
break;
case 141:
//#line 285 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);}
break;
case 142:
//#line 286 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);

		  String lexema = negarConstante(val_peek(0).sval);}
break;
case 143:
//#line 291 "gramatica.y"
{ String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                          TablaSimbolos.agregarSimbolo(nombre);
                          int puntero = TablaSimbolos.obtenerSimbolo(nombre);
                          }
break;
case 144:
//#line 294 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Comentario");}
break;
case 145:
//#line 295 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un mensaje luego del PRINT ");}
break;
case 146:
//#line 299 "gramatica.y"
{}
break;
case 147:
//#line 300 "gramatica.y"
{}
break;
case 148:
//#line 303 "gramatica.y"
{}
break;
case 149:
//#line 307 "gramatica.y"
{}
break;
case 151:
//#line 309 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una declaracion dentro de las llaves");}
break;
case 152:
//#line 310 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una ID previo a las llaves");}
break;
case 153:
//#line 313 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de IMPL");}
break;
//#line 1218 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
