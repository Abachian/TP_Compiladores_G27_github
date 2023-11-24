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
import java.util.HashMap;
import compilador.Terceto;

//#line 30 "Parser.java"




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
   38,   38,   39,   41,   42,   42,   28,   28,   28,   28,
   28,   28,   44,   44,   44,   44,   44,   44,   44,   45,
   45,   45,   45,   45,   45,   43,   40,   40,   40,   34,
   34,   46,   46,   46,   47,   47,   47,   47,   47,   47,
   27,   27,   27,   27,   27,   27,   27,   36,   36,   36,
   49,   49,   49,   48,   48,   48,   50,   50,   50,   51,
   51,   51,   52,   52,   54,   29,   29,   53,   53,   55,
   11,   11,   11,   11,   56,   17,   57,   58,   58,
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
    5,    5,    1,    1,    2,    2,    6,    7,    4,    8,
    3,    5,    1,    6,    5,    4,    2,    1,    2,    4,
    6,    5,    4,    2,    1,    1,    3,    2,    2,    1,
    3,    3,    2,    2,    1,    1,    1,    1,    1,    1,
    5,    5,    3,    3,    2,    2,    2,    3,    3,    1,
    3,    3,    1,    3,    3,    1,    1,    1,    4,    1,
    1,    4,    1,    2,    0,    3,    1,    0,    1,    1,
    4,    2,    3,    3,    2,    6,    2,    2,    1,
};
final static short yydefred[] = {                         0,
   10,    6,    8,    0,    0,    0,    7,    0,    0,   83,
    0,    0,   64,    0,   43,   45,   44,    0,    0,    0,
    0,    0,    0,   17,   18,   19,   20,   21,    0,    0,
   27,   28,   29,    0,   51,   52,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   25,    0,    0,    0,
    0,  145,   33,  155,    0,  157,    0,  143,    0,    0,
    0,  133,  138,    9,    0,    0,    0,    0,   16,    0,
   50,   24,    0,    0,    0,   53,   54,   55,   56,   57,
    0,    0,    0,    0,    0,    0,    0,    0,   70,    0,
    0,  118,  117,  115,  116,  120,  119,    0,    0,    0,
    0,  110,    0,  152,    0,    0,    0,    0,    0,    0,
    0,    0,   22,  146,  154,    0,  144,    0,    0,    0,
    0,    3,    5,    2,    4,    0,    0,   26,   23,   40,
    0,    0,    0,   38,    0,    0,   68,    0,   78,   74,
   72,    0,   69,   71,   76,    0,  109,    0,  108,    0,
    0,    0,   91,    0,   98,    0,    0,    0,    0,    0,
  153,    0,    0,    1,    0,    0,    0,    0,    0,    0,
  149,    0,  141,    0,  136,    0,  131,  132,   42,   39,
    0,    0,    0,    0,   66,    0,   84,    0,  107,  111,
    0,   97,    0,    0,    0,    0,   99,    0,   89,    0,
  151,    0,  122,    0,    0,  121,  139,    0,    0,    0,
   32,    0,   63,    0,    0,    0,    0,   79,    0,    0,
    0,    0,    0,    0,    0,   92,    0,  159,    0,    0,
   61,    0,    0,  134,  135,    0,    0,    0,   36,    0,
   48,   49,    0,    0,   62,   82,   81,   80,   86,   85,
    0,  106,   87,    0,    0,   96,    0,    0,    0,  156,
  158,    0,  142,   31,   35,    0,   47,    0,    0,   95,
    0,    0,    0,    0,   88,    0,    0,    0,    0,   30,
   67,    0,    0,    0,    0,    0,   94,   90,    0,    0,
   65,    0,  100,  103,    0,   60,  102,    0,  101,
};
final static short yydgoto[] = {                          4,
    5,    6,   21,   66,    7,   22,   23,   24,   25,   26,
   27,   28,   29,   30,   31,   32,   33,   34,  133,  238,
  239,  134,  240,  241,   35,   36,   37,   38,   39,   40,
   41,   42,   87,   99,   88,  100,   89,   90,   43,  101,
  188,  220,  221,  159,  255,  102,  103,  174,   61,   62,
  175,   63,  170,  114,  171,   44,   45,  230,
};
final static short yysindex[] = {                       -21,
    0,    0,    0,    0, -144,  136,    0,  208,  -43,    0,
 -126, -116,    0, -104,    0,    0,    0, -129,  -41,  164,
   67,  208,  208,    0,    0,    0,    0,    0,  107,  -94,
    0,    0,    0,  161,    0,    0,  159,  167,  169,  179,
  199,  235,  -38,  -17,  -49,   67,    0,  -12,   -8,   37,
  114,    0,    0,    0,  128,    0,  217,    0,    5,  127,
   52,    0,    0,    0,   74,  214,  227,  208,    0,  386,
    0,    0,   26,  118,  390,    0,    0,    0,    0,    0,
  -52,  266,  281,  268,  269,  274,   78,  282,    0,  289,
  -38,    0,    0,    0,    0,    0,    0,    3,   21,   66,
   99,    0,  -41,    0,  -89,  276,  302,  -41,  127,   76,
  -41,  127,    0,    0,    0,  -41,    0,   24,   24,  -41,
  -41,    0,    0,    0,    0,  386,  -94,    0,    0,    0,
   91,  100,  319,    0,   82,  328,    0,  294,    0,    0,
    0,  339,    0,    0,    0,  325,    0,   33,    0,   82,
  -41,  -36,    0,  151,    0,  106,  -41,  117,  -81,  127,
    0,  365,  262,    0,  432,  390,  131,  468,  127,  356,
    0,  361,    0,   88,    0,   88,    0,    0,    0,    0,
   36,   38,  -41,  377,    0,   82,    0,  267,    0,    0,
  127,    0,  -41,  280,  475,  382,    0,  154,    0,  163,
    0,  130,    0,  393,  385,    0,    0,  -41,  -41,  -41,
    0,  186,    0,  491,  -41,   45,  174,    0,  175,  180,
  121,  534,  -34,  400,  -41,    0,  354,    0,  405, -117,
    0,  190,  410,    0,    0,  409,  208,  331,    0,  163,
    0,    0,  414,  591,    0,    0,    0,    0,    0,    0,
  426,    0,    0,   84,  219,    0,  608,  442,  390,    0,
    0,  447,    0,    0,    0,  444,    0,  368,  450,    0,
  456,  163,  -41,  240,    0,  455,  242,  467,  390,    0,
    0,  392,  -41,  364,  626,  479,    0,    0,  408,  482,
    0,  627,    0,    0,  -41,    0,    0,  632,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  139,    0,
  110,  495,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   75,   77,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  227,    0,  493,
    0,    0,    0,    0,    0,    0,  -30,    0,    0,  496,
    8,    0,    0,    0,    0,    0,    0,   79,    0,   85,
    0,    0,    0,    0,  502,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  330,  342,    0,    0,    0,  353,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  214,    0,
    0,  505,    0,    0,    0,  509,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  104,  139,    0,    0,    0,
    0,  511,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   50,    0,    0,    0,    0,    0,    0,  310,    0,   55,
    0,    0,    0,    0,    0,  502,    0,    0,  512,    0,
    0,   -4,    0,   16,    0,   28,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   60,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  514,    0,    0,  509,    0,    0,
    0,    0,    0,    0,    0,    0,  132,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  430,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  299,    0,    0,    0,    0,  502,    0,
    0,  519,    0,    0,    0,    0,    0,    0,    0,    0,
  306,    0,    0,    0,    0,    0,    0,    0,  502,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   10,  551,   27,   -1,    0,  -18,  -16,   86,    0,    0,
    0,    0,   25,   -9, -176,    0,    0,  -99, -145,    0,
  332,    0,    0,  334,  -22,    0,  376,    0,  424,  427,
    0,    0,   63,  -31,  497,  -19,  -13,    0,  438,  487,
    0,    0,  358,  433,    0,  436,  309,  464,    0,  446,
    0,    0,  384,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=677;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         60,
   71,   98,   49,   59,   70,   68,   59,  260,   50,   49,
  137,  137,  137,  137,  137,   20,  137,   50,   55,   67,
  204,   97,    2,   96,   50,  228,  104,  108,  109,  137,
  112,  137,   59,   51,   46,  161,  140,  140,  140,  140,
  140,    3,  140,  147,  107,   71,   65,   59,  130,  126,
  130,  130,  130,  261,   74,  140,  128,  140,  128,  128,
  128,  149,   97,  123,   96,  132,  148,  130,  129,  130,
  129,  129,  129,  189,  144,  128,  111,  128,  213,  211,
   97,   59,   96,  160,  158,  245,  162,  129,  165,  129,
  113,  168,   97,  120,   96,  114,  169,   97,  121,   96,
  112,    3,  229,  182,   97,  105,   96,   69,  118,  113,
  119,  113,    1,  278,  114,  166,  114,  122,   12,  112,
   13,  112,   11,  273,  144,   97,   59,   96,   14,  209,
  229,  191,   52,  290,  210,   71,  167,  195,  157,  158,
   53,   97,  155,   96,   19,  157,   12,   15,   56,  155,
   72,   51,   54,  147,  216,   69,  132,  113,  212,   19,
  197,  129,   47,  214,  249,   73,   19,  127,  200,  118,
  147,  119,   73,  222,   12,  106,   73,   19,   14,  198,
  199,   19,   25,  227,   15,   16,   17,   18,  169,  242,
  193,   64,  106,  237,  192,  244,   19,   25,   64,   12,
   75,   13,   76,   11,   71,  257,  272,  106,  187,   14,
   77,   69,   78,   47,  242,   57,   58,  242,   57,   58,
   48,  156,   79,   19,   19,  258,  252,  253,   15,   48,
   92,   93,   94,   95,  147,    1,   48,  274,  137,  137,
  137,  137,   80,   83,   57,   58,   19,   69,  110,  132,
  219,   71,  115,  285,  106,  284,  116,  124,    3,   57,
   58,   71,  117,  292,  140,  140,  140,  140,   19,  132,
  125,   92,   93,   94,   95,  298,  130,  130,  130,  130,
  172,  173,  128,  250,  128,  128,  128,  128,   64,   92,
   93,   94,   95,   57,   58,   19,  129,  129,  129,  129,
   19,   92,   93,   94,   95,  135,   92,   93,   94,   95,
  236,  139,  140,   92,   93,   94,   95,  141,  113,  113,
  113,  113,   69,  114,  114,  114,  114,   19,  112,  112,
  112,  112,  145,  163,   92,   93,   94,   95,   57,   58,
  152,   19,   19,   10,  142,  164,   11,  179,  271,   13,
   92,   93,   94,   95,   19,  152,  180,   83,   10,  181,
  153,   11,  152,  154,   13,   10,  147,  183,   11,  147,
  154,   13,  147,  152,  147,  147,   10,   81,  186,   11,
   10,  196,   13,   11,  202,   19,   13,  205,  106,   83,
   75,  106,    9,   12,  106,   10,  207,  106,   11,   12,
  208,   13,   73,   14,  223,  137,  143,  150,  151,   15,
   16,   17,   18,   77,   19,  226,  215,   84,  185,  152,
    9,  225,   10,   10,   19,   11,   11,   12,   13,   13,
  232,   14,   93,  231,   93,  246,  247,   15,   16,   17,
   18,  248,    9,  256,  259,   10,  262,   83,   11,   12,
  263,   13,  264,   14,   75,  266,  150,  268,   84,   15,
   16,   17,   18,   84,    9,   85,   73,   10,   86,  270,
   11,   12,  203,   13,  118,   14,  119,   77,   64,   91,
  275,   15,   16,   17,   18,  277,  279,  280,  293,  201,
  150,   81,  281,  282,   10,  283,  152,   11,  287,   10,
   13,   82,   11,  288,  286,   13,   85,  289,  206,   86,
  118,   85,  119,   84,   86,  224,  291,  118,  295,  119,
   91,   84,  296,   81,  150,   91,   10,  217,  218,   11,
  212,  243,   13,  118,   34,  119,  127,   81,   81,  126,
   10,   10,   37,   11,   11,  136,   13,   13,  123,  148,
   81,   41,  150,   10,   46,    8,   11,   58,  184,   13,
  105,   85,   59,   84,   86,  177,  178,  104,  265,   85,
   93,   93,   86,  267,  251,   91,  118,  146,  119,  138,
  254,   81,  176,   91,   10,  190,   75,   11,  194,   75,
   13,  233,   75,    0,   75,   75,   84,    0,   73,    0,
    0,   73,    0,    0,   73,    0,   73,   73,    0,   77,
  152,   85,   77,   10,   86,   77,   11,   77,   77,   13,
  152,  127,    0,   10,    0,   91,   11,    0,   12,   13,
    0,  269,   14,  118,    0,  119,    0,    0,   15,   16,
   17,   18,  127,    0,   85,    0,  130,   86,  276,   12,
  118,    0,  119,   14,  234,  235,    0,  131,   91,   15,
   16,   17,   18,   15,   16,   17,  294,  297,  118,  118,
  119,  119,  299,    0,  118,    0,  119,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         19,
   23,   40,   46,   45,   23,   22,   45,  125,   61,   46,
   41,   42,   43,   44,   45,    6,   47,   61,  123,   21,
  166,   60,   44,   62,   61,  202,   44,   40,   48,   60,
   50,   62,   45,    9,    8,  125,   41,   42,   43,   44,
   45,  123,   47,   41,   46,   68,   20,   45,   41,   68,
   43,   44,   45,  230,   30,   60,   41,   62,   43,   44,
   45,   41,   60,   65,   62,   75,   98,   60,   41,   62,
   43,   44,   45,   41,   88,   60,   40,   62,   41,   44,
   60,   45,   62,  103,  101,   41,  105,   60,  108,   62,
   41,  111,   60,   42,   62,   41,  116,   60,   47,   62,
   41,  123,  202,  135,   60,  123,   62,   22,   43,   60,
   45,   62,  257,  259,   60,   40,   62,   44,   44,   60,
   44,   62,   44,   40,  138,   60,   45,   62,   44,   42,
  230,  151,  259,  279,   47,  158,   61,  157,   40,  156,
  257,   60,   44,   62,   61,   40,  264,   44,  278,   44,
   44,  127,  257,   44,  186,   70,  166,   44,  123,   61,
   44,   44,  257,  183,   44,   59,   61,  257,  159,   43,
   61,   45,   59,  193,  264,   44,   59,   61,  268,  261,
  262,   61,   44,  200,  274,  275,  276,  277,  208,  212,
   40,  125,   61,  212,   44,  215,   61,   59,  125,  125,
   40,  125,   44,  125,  227,  225,  123,  257,  146,  125,
   44,  126,   44,  257,  237,  257,  258,  240,  257,  258,
  273,  123,   44,   61,   61,  227,  261,  262,  125,  273,
  269,  270,  271,  272,  125,  257,  273,  254,  269,  270,
  271,  272,   44,  123,  257,  258,   61,  162,  257,  259,
  188,  274,  125,  273,  123,  272,   40,   44,  123,  257,
  258,  284,  258,  283,  269,  270,  271,  272,   61,  279,
   44,  269,  270,  271,  272,  295,  269,  270,  271,  272,
  257,  258,  257,  221,  269,  270,  271,  272,  125,  269,
  270,  271,  272,  257,  258,   61,  269,  270,  271,  272,
   61,  269,  270,  271,  272,   40,  269,  270,  271,  272,
  125,   44,   44,  269,  270,  271,  272,   44,  269,  270,
  271,  272,  237,  269,  270,  271,  272,   61,  269,  270,
  271,  272,   44,   58,  269,  270,  271,  272,  257,  258,
  257,   61,   61,  260,  267,   44,  263,  257,  265,  266,
  269,  270,  271,  272,   61,  257,  257,  123,  260,   41,
  262,  263,  257,  265,  266,  260,  257,   40,  263,  260,
  265,  266,  263,  257,  265,  266,  260,  257,   40,  263,
  260,  265,  266,  263,  123,   61,  266,  257,  257,  123,
   61,  260,  257,  264,  263,  260,   41,  266,  263,  264,
   40,  266,   61,  268,  125,  125,  125,   99,  100,  274,
  275,  276,  277,   61,   61,  262,   40,   42,  125,  257,
  257,   40,  260,  260,   61,  263,  263,  264,  266,  266,
   46,  268,  123,   41,  125,  262,  262,  274,  275,  276,
  277,  262,  257,   44,   40,  260,  257,  123,  263,  264,
   41,  266,   44,  268,  125,  125,  148,   44,   83,  274,
  275,  276,  277,   88,  257,   42,  125,  260,   42,   44,
  263,  264,   41,  266,   43,  268,   45,  125,  125,   42,
  262,  274,  275,  276,  277,   44,   40,   44,  125,  125,
  182,  257,  125,   44,  260,   40,  257,  263,   44,  260,
  266,  267,  263,  262,  265,  266,   83,   41,   41,   83,
   43,   88,   45,  138,   88,   41,  125,   43,   40,   45,
   83,  146,   41,  257,  216,   88,  260,  261,  262,  263,
  123,   41,  266,   43,   40,   45,   44,  257,  257,   44,
  260,  260,   41,  263,  263,  265,  266,  266,   44,   41,
  257,   41,   41,  260,  125,    5,  263,   44,  265,  266,
  262,  138,   44,  188,  138,  120,  121,  262,  237,  146,
  261,  262,  146,  240,   41,  138,   43,   91,   45,   83,
  223,  257,  119,  146,  260,  150,  257,  263,  156,  260,
  266,  208,  263,   -1,  265,  266,  221,   -1,  257,   -1,
   -1,  260,   -1,   -1,  263,   -1,  265,  266,   -1,  257,
  257,  188,  260,  260,  188,  263,  263,  265,  266,  266,
  257,  257,   -1,  260,   -1,  188,  263,   -1,  264,  266,
   -1,   41,  268,   43,   -1,   45,   -1,   -1,  274,  275,
  276,  277,  257,   -1,  221,   -1,  257,  221,   41,  264,
   43,   -1,   45,  268,  209,  210,   -1,  268,  221,  274,
  275,  276,  277,  274,  275,  276,   41,   41,   43,   43,
   45,   45,   41,   -1,   43,   -1,   45,
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
"sentencia_do : DO_UNTIL ','",
"sentencia_do : DO_UNTIL",
"sentencia_do : impresion ','",
"sentencia_do : impresion",
"sentencia_do : seleccion_en_do ','",
"sentencia_do : seleccion_en_do",
"sentencia_do : asignacion ','",
"seleccion_en_do : header_if condicion_salto_if if_seleccion_do END_IF",
"seleccion_en_do : header_if condicion_salto_if if_seleccion_do else_seleccion_do END_IF",
"seleccion_en_do : header_if condicion_salto_if if_seleccion_do bloque_sentencias_do END_IF",
"seleccion_en_do : header_if condicion_salto_if if_seleccion_do ELSE END_IF",
"header_if : IF",
"if_seleccion_do : bloque_sentencias_do",
"else_seleccion_do : caso_else bloque_sentencias_do",
"else_seleccion_do : caso_else ','",
"seleccion : header_if condicion_salto_if '{' if_seleccion '}' END_IF",
"seleccion : header_if condicion_salto_if '{' if_seleccion '}' else_seleccion END_IF",
"seleccion : header_if condicion_salto_if if_seleccion END_IF",
"seleccion : header_if condicion_salto_if if_seleccion begin ejecucion end ',' END_IF",
"seleccion : header_if condicion_salto_if END_IF",
"seleccion : header_if condicion_salto_if if_seleccion ELSE END_IF",
"if_seleccion : ejecucion",
"if_seleccion : ejecucion RETURN '(' expresion ')' ','",
"if_seleccion : RETURN '(' expresion ')' ','",
"if_seleccion : '(' expresion ')' ','",
"if_seleccion : RETURN ','",
"if_seleccion : ','",
"if_seleccion : ejecucion ','",
"else_seleccion : caso_else '{' ejecucion '}'",
"else_seleccion : caso_else ejecucion RETURN '(' expresion ')'",
"else_seleccion : caso_else RETURN '(' expresion ')'",
"else_seleccion : caso_else '(' expresion ')'",
"else_seleccion : caso_else RETURN",
"else_seleccion : caso_else",
"caso_else : ELSE",
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
"declaracion_clase : header_clase '{' declaraciones '}'",
"declaracion_clase : header_clase ','",
"declaracion_clase : header_clase '{' '}'",
"declaracion_clase : CLASS '{' '}'",
"header_clase : CLASS ID",
"declaracion_impl : header_impl ID ':' '{' funciones_impl '}'",
"header_impl : IMPL FOR",
"funciones_impl : funciones_impl funcion",
"funciones_impl : funcion",
};

//#line 464 "gramatica.y"
public static boolean declarando = true;

public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = ".";

public static StringBuilder ambito = new StringBuilder();

public static final List<String> errores_lexicos = new ArrayList<>();
public static final List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> errores_semanticos = new ArrayList<>();
public static final List<String> estructuras_sintacticas = new ArrayList<>();
public static final HashMap<Integer,Terceto> codigoIntermedio = new HashMap<Integer,Terceto>();
public static final Stack pila = new Stack();

private static boolean errores_compilacion;
private static String tipo;
private int punteroTerceto = 1;

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

//--TERCETOS--//
public int generarTerceto(String op1, String op2, String op3){
	Terceto t = new Terceto(op1, op2, op3);
	codigoIntermedio.put(punteroTerceto,t);
	punteroTerceto = punteroTerceto + 1;
	return punteroTerceto -1;
}

public static void imprimirTercetos() {
  // Imprimo la lista de Tercetos
  if (!codigoIntermedio.isEmpty()) {
    System.out.println();
    int nroTerceto = 1;

    for (Terceto terceto : codigoIntermedio.values()) {
      System.out.print("Terceto " + nroTerceto + ": ");
      terceto.print();
      nroTerceto = nroTerceto + 1;
    }
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

				Parser.imprimirTercetos();
				TablaSimbolos.imprimirTabla();
                Parser.imprimirErrores(errores_lexicos, "Errores Lexicos");
                Parser.imprimirErrores(errores_sintacticos, "Errores Sintacticos");
                Parser.imprimirEstructuras(estructuras_sintacticas, "Estructuras");

        }



//#line 818 "Parser.java"
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
//#line 30 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaban sentencias de ejecucion");}
break;
case 3:
//#line 31 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un '}' al final del programa");}
break;
case 4:
//#line 32 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un '{' antes de las sentencias");}
break;
case 5:
//#line 33 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un ',' al final del programa");}
break;
case 6:
//#line 34 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperaba un programa");}
break;
case 7:
//#line 37 "gramatica.y"
{ Parser.declarando = true; tipo = TablaTipos.STRING_TYPE; int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                                                                                         TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                                                                         TablaSimbolos.agregarAtributo(ptr_id, "uso", "Nombre del programa");}
break;
case 8:
//#line 42 "gramatica.y"
{cambiarAmbito(":START");}
break;
case 10:
//#line 48 "gramatica.y"
{cambiarAmbito(val_peek(0).sval);}
break;
case 12:
//#line 52 "gramatica.y"
{Parser.declarando = false;}
break;
case 14:
//#line 54 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
break;
case 15:
//#line 55 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
break;
case 25:
//#line 77 "gramatica.y"
{
                             int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                             TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                             TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");
                             }
break;
case 26:
//#line 82 "gramatica.y"
{
                                          int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(2).sval + Parser.ambito.toString());
                                          TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                          TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");
                                          }
break;
case 30:
//#line 94 "gramatica.y"
{salirAmbito();}
break;
case 31:
//#line 95 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el cuerpo de la funcion");}
break;
case 33:
//#line 102 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
			TablaSimbolos.agregarAtributo(ptr_id, "tipo", "VOID_TYPE");
                    	TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de metodo");
                    	agregarEstructura(estructuras_sintacticas, "Declaracion de Funcion");
                    	TablaSimbolos.agregarSimbolo("@ret@" + val_peek(0).sval + Parser.ambito.toString());
                        int ptr_ret = TablaSimbolos.obtenerSimbolo("@ret@" + val_peek(0).sval + Parser.ambito.toString());
                        cambiarAmbito(val_peek(0).sval);}
break;
case 34:
//#line 109 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera nombre de la funcion");}
break;
case 39:
//#line 121 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
	     	    TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                    TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");}
break;
case 40:
//#line 124 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el tipo del parametro");}
break;
case 41:
//#line 125 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el nombre del parametro");}
break;
case 42:
//#line 126 "gramatica.y"
{tipo = TablaTipos.CLASS_TYPE; int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                                                                                     TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                                                                     TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");}
break;
case 43:
//#line 132 "gramatica.y"
{tipo = TablaTipos.UINT_TYPE;}
break;
case 44:
//#line 133 "gramatica.y"
{tipo = TablaTipos.SHORT_TYPE;}
break;
case 45:
//#line 134 "gramatica.y"
{tipo = TablaTipos.DOUBLE_TYPE;}
break;
case 46:
//#line 138 "gramatica.y"
{}
break;
case 58:
//#line 164 "gramatica.y"
{int aux = generarTerceto(val_peek(1).sval,val_peek(4).sval+val_peek(3).sval+val_peek(2).sval,val_peek(0).sval);}
break;
case 59:
//#line 165 "gramatica.y"
{int aux = generarTerceto(val_peek(3).sval,val_peek(4).sval,val_peek(0).sval);}
break;
case 60:
//#line 166 "gramatica.y"
{int aux = generarTerceto("=",val_peek(7).sval,val_peek(5).sval);}
break;
case 62:
//#line 170 "gramatica.y"
{int i = codigoIntermedio.size();
                                                            boolean encontrado = false;
                                                            while (!encontrado && i > 0 )
                                                            {
                                                              if (codigoIntermedio.get(i).getOp1().equals("DO"))
                                                              {

                                                                  yyval.sval = "[" + Integer.toString(generarTerceto("UNTIL",Integer.toString(i + 1) ,Integer.toString(punteroTerceto + 1))) + "]";
                                                                  encontrado = true;
                                                               }
                                                              i = i-1;
                                                            }
                                                            }
break;
case 63:
//#line 184 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
break;
case 64:
//#line 187 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("DO","-","-"))+ "]";
         agregarEstructura(estructuras_sintacticas, "Sentencia DO_UNTIL ");}
break;
case 68:
//#line 194 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
break;
case 69:
//#line 195 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera un '{' "); }
break;
case 73:
//#line 203 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 75:
//#line 205 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 77:
//#line 207 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 79:
//#line 212 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
                                                                      yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 80:
//#line 214 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
                                                                                 yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 81:
//#line 216 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 82:
//#line 217 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 83:
//#line 220 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");}
break;
case 84:
//#line 223 "gramatica.y"
{int i = codigoIntermedio.size();
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
break;
case 85:
//#line 236 "gramatica.y"
{int i = codigoIntermedio.size();
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
break;
case 86:
//#line 247 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 87:
//#line 250 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
																	yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 88:
//#line 252 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
											   yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 89:
//#line 254 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan llaves "); }
break;
case 90:
//#line 255 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 91:
//#line 256 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
break;
case 92:
//#line 257 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 93:
//#line 260 "gramatica.y"
{  int i = codigoIntermedio.size();
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
break;
case 94:
//#line 271 "gramatica.y"
{  int i = codigoIntermedio.size();
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
break;
case 95:
//#line 282 "gramatica.y"
{  int i = codigoIntermedio.size();
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
break;
case 96:
//#line 293 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 97:
//#line 294 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 98:
//#line 295 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias luego de la condicion");}
break;
case 99:
//#line 296 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "No se espera ',' "); }
break;
case 100:
//#line 301 "gramatica.y"
{int i = codigoIntermedio.size();
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
break;
case 101:
//#line 313 "gramatica.y"
{int i = codigoIntermedio.size();
                                                               					     boolean encontrado = false;
                                                               					     while (!encontrado && i > 0 ){
                                                               					     	if (codigoIntermedio.get(i).getOp2().equals("incompleto")) {
                                                               						       Terceto t = codigoIntermedio.get(i);
                                                               						       t.setOp2(Integer.toString(punteroTerceto));
                                                               						       encontrado = true;
                                                               					     	}
                                                               					     	i = i-1;
                                                               					     }}
break;
case 102:
//#line 323 "gramatica.y"
{int i = codigoIntermedio.size();
                                             					     boolean encontrado = false;
                                             					     while (!encontrado && i > 0 ){
                                             					     	if (codigoIntermedio.get(i).getOp2().equals("incompleto")) {
                                             						       Terceto t = codigoIntermedio.get(i);
                                             						       t.setOp2(Integer.toString(punteroTerceto));
                                             						       encontrado = true;
                                             					     	}
                                             					     	i = i-1;
                                             					     }}
break;
case 103:
//#line 333 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 104:
//#line 334 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 105:
//#line 335 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 106:
//#line 338 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("BI","incompleto","-"))+ "]";  }
break;
case 107:
//#line 342 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("BF",val_peek(1).sval,"incompleto"))+ "]";  }
break;
case 108:
//#line 343 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera '(' al principio de la condicion");}
break;
case 109:
//#line 344 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera una condicion");}
break;
case 111:
//#line 349 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 112:
//#line 352 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,val_peek(2).sval,val_peek(0).sval))+ "]";}
break;
case 113:
//#line 354 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion luego del comparador");}
break;
case 114:
//#line 355 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion antes del comparador");}
break;
case 121:
//#line 366 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
									  int aux = generarTerceto(val_peek(3).sval,val_peek(4).sval,val_peek(1).sval);
						}
break;
case 122:
//#line 369 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
										int aux = generarTerceto("+",val_peek(4).sval,val_peek(1).sval);
										int aux2 = generarTerceto("=",val_peek(4).sval,"[" + aux +"]");
			}
break;
case 123:
//#line 373 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								int aux = generarTerceto(val_peek(1).sval,val_peek(2).sval,val_peek(0).sval);}
break;
case 124:
//#line 376 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								 int aux = generarTerceto("+",val_peek(2).sval,val_peek(0).sval);
                        		 int aux2 = generarTerceto("=",val_peek(2).sval,"[" + aux +"]");
			}
break;
case 125:
//#line 380 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 126:
//#line 381 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un identificador en el lado izquierdo de la asignacion");}
break;
case 127:
//#line 382 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 128:
//#line 385 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";
                                            }
break;
case 129:
//#line 387 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 131:
//#line 391 "gramatica.y"
{ yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 132:
//#line 392 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("/",yyval.sval,val_peek(0).sval))+ "]";}
break;
case 134:
//#line 396 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";  }
break;
case 135:
//#line 397 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("/",yyval.sval,val_peek(0).sval))+ "]";}
break;
case 139:
//#line 403 "gramatica.y"
{}
break;
case 142:
//#line 408 "gramatica.y"
{}
break;
case 143:
//#line 411 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo(val_peek(0).sval));}
break;
case 144:
//#line 414 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
                   		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
                   		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo(val_peek(0).sval));
                   		String lexema = negarConstante(val_peek(0).sval);}
break;
case 145:
//#line 420 "gramatica.y"
{ String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                          TablaSimbolos.agregarSimbolo(nombre);
                          int puntero = TablaSimbolos.obtenerSimbolo(nombre);
                          }
break;
case 146:
//#line 423 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Comentario");}
break;
case 147:
//#line 424 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un mensaje luego del PRINT ");}
break;
case 148:
//#line 428 "gramatica.y"
{}
break;
case 149:
//#line 429 "gramatica.y"
{}
break;
case 150:
//#line 432 "gramatica.y"
{}
break;
case 151:
//#line 436 "gramatica.y"
{  tipo = TablaTipos.CLASS_TYPE;
						int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(2).sval + Parser.ambito.toString());
						TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de clase");
						TablaSimbolos.agregarAtributo(ptr_id, "tipo", "CLASS_TYPE");}
break;
case 153:
//#line 442 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una declaracion dentro de las llaves");}
break;
case 154:
//#line 443 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una ID previo a las llaves");}
break;
case 155:
//#line 446 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Declaracion de Clase");
                        int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                        TablaSimbolos.agregarAtributo(ptr_id, "tipo", "CLASS_TYPE");
                        TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de clase");}
break;
case 157:
//#line 455 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de IMPL");}
break;
//#line 1502 "Parser.java"
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
