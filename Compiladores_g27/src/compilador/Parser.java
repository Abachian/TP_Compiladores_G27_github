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
    0,    0,    0,    0,    0,    0,    1,    3,    2,    2,
    2,    2,    2,    4,    4,    6,    6,    6,    6,   10,
    7,    7,   11,   11,    8,    8,    8,   13,   13,   14,
   16,   16,   18,   18,   17,   17,   20,   20,   20,   20,
   12,   12,   12,   19,   21,   21,   22,    5,    5,   23,
   24,   24,   24,   24,   24,   29,   29,   29,   29,   28,
   28,   30,   31,   31,   31,   31,   31,   33,   33,   35,
   35,   35,   35,   35,   35,   35,   36,   36,   36,   36,
   37,   39,   40,   40,   26,   26,   26,   26,   26,   26,
   42,   42,   42,   42,   42,   42,   42,   43,   43,   43,
   43,   43,   43,   41,   38,   38,   38,   32,   32,   44,
   44,   44,   45,   45,   45,   45,   45,   45,   25,   25,
   25,   25,   25,   25,   25,   34,   34,   34,   47,   47,
   47,   46,   46,   46,   48,   48,   48,   49,   49,   49,
   50,   50,   52,   27,   27,   51,   51,   53,    9,    9,
    9,    9,   54,   15,   55,   56,   56,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    3,    1,    1,    1,    2,    1,
    1,    2,    3,    2,    1,    1,    1,    1,    1,    3,
    3,    2,    1,    3,    1,    1,    1,    8,    7,    5,
    2,    1,    2,    1,    0,    1,    2,    1,    1,    2,
    1,    1,    1,    1,    2,    1,    1,    2,    1,    1,
    2,    2,    2,    2,    2,    5,    7,   10,    6,    6,
    5,    1,    8,    3,    7,    2,    2,    1,    2,    2,
    1,    2,    1,    2,    1,    2,    4,    5,    5,    5,
    1,    1,    2,    2,    6,    7,    4,    8,    3,    5,
    1,    6,    5,    4,    2,    1,    2,    4,    6,    5,
    4,    2,    1,    1,    3,    2,    2,    1,    3,    3,
    2,    2,    1,    1,    1,    1,    1,    1,    5,    5,
    3,    3,    2,    2,    2,    3,    3,    1,    3,    3,
    1,    3,    3,    1,    1,    1,    4,    1,    1,    4,
    1,    2,    0,    3,    1,    0,    1,    1,    4,    2,
    3,    3,    2,    5,    3,    2,    1,
};
final static short yydefred[] = {                         0,
    0,   81,    0,    0,   62,    0,   41,   43,   42,    0,
    6,    7,    0,    0,    0,    0,    0,    0,   15,   16,
   17,   18,   19,    0,    0,   25,   26,   27,    0,   49,
   50,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   23,    0,    0,    0,    0,  143,   31,  153,    0,    0,
    0,  141,    0,    0,    0,  131,  136,    8,    0,    0,
    0,    0,   14,    0,   48,   22,    0,    0,    0,   51,
   52,   53,   54,   55,    0,    0,    0,    0,    0,    0,
    0,    0,   68,    0,    0,  116,  115,  113,  114,  118,
  117,    0,    0,    0,    0,  108,    0,  150,    0,    0,
    0,    0,    0,    0,    0,   20,  144,  152,  155,    0,
  142,    0,    0,    0,    0,    3,    0,    2,    4,    0,
    0,   24,   21,   38,    0,    0,    0,   36,    0,    0,
   66,    0,   76,   72,   70,    0,   67,   69,   74,    0,
  107,    0,  106,    0,    0,    0,   89,    0,   96,    0,
    0,    0,    0,    0,  151,    0,    0,    0,    0,    0,
    0,    0,    0,  147,    0,  139,    0,  134,    0,  129,
  130,    1,   40,   37,    0,    0,    0,    0,   64,    0,
   82,    0,  105,  109,    0,   95,    0,    0,    0,    0,
   97,    0,   87,    0,  149,  157,    0,    0,  120,    0,
    0,  119,  137,    0,    0,    0,   30,    0,   61,    0,
    0,    0,    0,   77,    0,    0,    0,    0,    0,    0,
    0,   90,    0,    0,  154,  156,   59,    0,    0,  132,
  133,    0,    0,    0,   34,    0,   46,   47,    0,    0,
   60,   80,   79,   78,   84,   83,    0,  104,   85,    0,
    0,   94,    0,    0,    0,    0,  140,   29,   33,    0,
   45,    0,    0,   93,    0,    0,    0,    0,   86,    0,
    0,    0,    0,   28,   65,    0,    0,    0,    0,    0,
   92,   88,    0,   63,    0,   98,  101,    0,   58,  100,
    0,   99,
};
final static short yydgoto[] = {                         14,
   15,   16,   60,   17,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,   29,  127,  234,  235,  128,
  236,  237,   30,   31,   32,   33,   34,   35,   36,   37,
   81,   93,   82,   94,   83,   84,   38,   95,  182,  216,
  217,  153,  251,   96,   97,  167,   55,   56,  168,   57,
  163,  107,  164,   39,   40,  198,
};
final static short yysindex[] = {                       -41,
  -45,    0, -246, -233,    0, -112,    0,    0,    0, -243,
    0,    0,   -9,    0,  160,  -81,  213,  213,    0,    0,
    0,    0,    0,  -17, -171,    0,    0,    0,   53,    0,
    0,   86,  129,  133,  137,  142,  234,  -28,  127,  130,
    0,  -19,  -33,  -12,  -14,    0,    0,    0,   76,  -31,
  175,    0,    2,  124,  108,    0,    0,    0,  -22,  214,
  217,  213,    0,  373,    0,    0,    6,   11,  157,    0,
    0,    0,    0,    0,  -55,  232,  276,  236,  242,  248,
   26,  239,    0,  250,  -28,    0,    0,    0,    0,    0,
    0,   12,   27,   82,  114,    0,   -9,    0,  359,  183,
   -9,  124,   78,   -9,  124,    0,    0,    0,    0,   -9,
    0,  -62,  -62,   -9,   -9,    0,  266,    0,    0,  373,
 -171,    0,    0,    0,   65,   66,  308,    0,   89,  288,
    0,  287,    0,    0,    0,  310,    0,    0,    0,  322,
    0,   42,    0,   89,   -9,  -42,    0,   79,    0,  121,
   -9,  139, -105,  124,    0,  402,   98,  264,  157,  106,
  397,  124,  326,    0,  333,    0,  143,    0,  143,    0,
    0,    0,    0,    0,  136,   49,   -9,  350,    0,   89,
    0,  247,    0,    0,  124,    0,   -9,  281,  416,  369,
    0,  149,    0,  255,    0,    0,  375, -116,    0,  377,
  376,    0,    0,   -9,   -9,   -9,    0,  192,    0,  487,
   -9,   60,  159,    0,  165,  168,  109,  531,  -56,  395,
   -9,    0,  339,  157,    0,    0,    0,  184,  403,    0,
    0,  406,  213,  328,    0,  255,    0,    0,  407,  532,
    0,    0,    0,    0,    0,    0,  410,    0,    0,  128,
  185,    0,  548,  418,  422,  425,    0,    0,    0,  427,
    0,  349,  431,    0,  438,  255,   -9,  254,    0,  436,
  223,  363,  157,    0,    0,  367,   -9,  347,  574,  453,
    0,    0,  462,    0,  579,    0,    0,   -9,    0,    0,
  601,    0,
};
final static short yyrindex[] = {                         0,
  119,    0,  132,  476,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   16,   37,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  478,    0,  480,    0,    0,    0,    0,    0,    0,
   -4,    0,    0,  481,   18,    0,    0,    0,    0,    0,
    0,   62,    0,   72,    0,    0,    0,    0,  488,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  294,  304,
    0,    0,    0,  321,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  490,    0,    0,  493,    0,    0,    0,    0,  497,
    0,    0,    0,    0,    0,    0,  543,    0,    0,   77,
  119,    0,    0,    0,    0,  504,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   64,    0,    0,    0,    0,    0,
    0, -115,    0,   69,    0,    0,    0,    0,  488,    0,
    0,  505,    0,    0,    7,    0,   32,    0,   55,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   73,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  511,    0,    0,  497,    0,    0,    0,    0,    0,    0,
    0,    0,  150,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  488,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  424,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  296,
    0,    0,    0,    0,    0,  512,    0,    0,    0,    0,
    0,    0,    0,    0,  300,    0,    0,    0,    0,    0,
    0,    0,  488,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  439,  565,   48,  148,  -10,  135,    0,    0,    0,    0,
   22,  -64, -132,    0,    0,   15, -145,    0,  361,    0,
    0,  364,  -16,    0,  366,    0,  458,  486,    0,    0,
   -3,  -21,  518,  -13,  -67,    0,  489,  521,    0,    0,
  378,  459,    0,  464,  389,  498,    0,   -2,    0,    0,
  399,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=706;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
   43,   65,   11,   43,  126,   44,   62,   91,  225,   91,
   49,   92,   46,  200,  138,   44,   53,   12,   44,   13,
  101,  116,   45,   47,  196,   53,   66,  104,  102,  106,
  105,   91,   53,   90,   50,   53,  135,  135,  135,  135,
  135,   67,  135,   58,   67,   65,   68,  138,  138,  138,
  138,  138,  141,  138,  123,  135,   53,  135,  128,   10,
  128,  128,  128,   61,  138,  226,  138,  143,  138,   67,
  142,   91,  126,   90,  126,  126,  126,  128,  255,  128,
   11,   12,  183,  154,  152,   41,   91,  158,   90,  209,
  161,  126,   69,  126,  126,  127,  162,  127,  127,  127,
  241,   91,   58,   90,  111,    9,  117,  176,   91,  112,
   90,  170,  171,  110,  127,   12,  127,  159,  187,   91,
   13,   90,  186,  111,  112,  111,  113,  283,  112,   70,
  112,  185,  110,   53,  110,   65,  181,  189,  160,  152,
   10,   91,   45,   90,   48,   91,   91,    4,   91,  114,
   90,   63,  245,  151,  115,  192,  193,  149,  212,  126,
  151,   11,   23,  210,  149,   64,  112,  267,  113,   13,
   98,  197,   71,  218,   13,  145,   72,   23,  215,  207,
   73,   13,  191,  223,  205,   74,    9,  100,   13,  206,
  162,  238,  145,  104,  165,  166,   12,  240,   63,   13,
  108,   13,  230,  231,  248,  249,   65,  253,  126,  120,
  104,   41,  197,  246,  110,    1,  238,   42,    2,  238,
   13,    3,    4,  103,    5,  109,    6,   42,   51,   52,
   42,   77,    7,    8,    9,   10,  150,   51,   52,  268,
   86,   87,   88,   89,   51,   52,  156,   51,   52,   99,
  266,   65,   13,  279,   63,  278,  145,  118,  208,  111,
  119,   65,  122,  285,  135,  135,  135,  135,   51,   52,
  254,  129,  104,   13,  291,  138,  138,  138,  138,  133,
   86,   87,   88,   89,   58,  134,  128,  128,  128,  128,
   63,  135,  136,  139,   13,   86,   87,   88,   89,   13,
  126,  126,  126,  126,  199,  157,  112,   13,  113,  172,
   86,   87,   88,   89,   13,   13,  232,   86,   87,   88,
   89,  173,  174,  127,  127,  127,  127,  177,   86,   87,
   88,   89,  111,  111,  111,  111,   13,  112,  112,  112,
  112,  110,  110,  110,  110,   51,   52,   13,  175,  180,
   86,   87,   88,   89,   73,  233,   77,   86,   87,   88,
   89,    4,  201,  137,   71,   75,  203,   63,    2,   77,
  146,    3,  204,    2,    5,  147,    3,  146,  148,    5,
    2,   75,   13,    3,  146,  148,    5,    2,  145,  211,
    3,  145,  265,    5,  145,  146,  145,  145,    2,   13,
  131,    3,   78,  190,    5,  219,  104,   13,  221,  104,
  222,  179,  104,  124,  224,  104,    1,  227,   73,    2,
  242,  228,    3,    4,  125,    5,  243,    6,   71,  244,
    7,    8,    9,    7,    8,    9,   10,  202,  252,  112,
  256,  113,   78,  257,   77,   75,  269,   78,    1,  258,
  262,    2,  260,  264,    3,    4,  220,    5,  112,    6,
  113,  271,  272,   58,  273,    7,    8,    9,   10,    1,
  274,  286,    2,  275,  276,    3,    4,  277,    5,  281,
    6,  144,  145,  155,  282,  208,    7,    8,    9,   10,
   75,  284,  288,    2,   79,   75,    3,   78,    2,    5,
   76,    3,  289,   75,    5,   78,    2,  213,  214,    3,
  146,  146,    5,    2,    2,   32,    3,    3,  280,    5,
    5,  123,   80,  125,  124,   85,  195,  239,   35,  112,
  144,  113,   75,  122,   79,    2,  121,  146,    3,   79,
  130,    5,    5,   75,   39,  148,    2,   78,   44,    3,
   73,  178,    5,   73,   56,   57,   73,  103,   73,   73,
   71,  102,   80,   71,  144,   85,   71,   80,   71,   71,
   85,  247,  263,  112,  112,  113,  113,   75,   75,   59,
   75,    2,   78,   75,    3,   75,   75,    5,  270,   79,
  112,  194,  113,  259,  132,  146,  250,   79,    2,  261,
  144,    3,  229,  146,    5,  140,    2,  184,  188,    3,
  169,    0,    5,    0,  287,  121,  112,   80,  113,  290,
   85,  112,    4,  113,    0,   80,    6,    0,   85,  121,
    0,    0,    7,    8,    9,   10,    4,    0,    0,   79,
    6,  292,    0,  112,    0,  113,    7,    8,    9,   10,
    0,    0,    0,    0,    0,    0,    0,    0,  121,    0,
    0,    0,    0,    0,    0,    4,    0,   80,    0,    6,
   85,    0,    0,    0,   79,    7,    8,    9,   10,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,   80,    0,    0,   85,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         13,
   46,   18,   44,   46,   69,   61,   17,  123,  125,  125,
  123,   40,  259,  159,   82,   61,   45,  123,   61,   61,
   40,   44,    1,  257,  157,   45,   44,   40,   42,   44,
   44,   60,   45,   62,  278,   45,   41,   42,   43,   44,
   45,   59,   47,  125,   59,   62,   25,   41,   42,   43,
   44,   45,   41,   47,   44,   60,   45,   62,   41,   44,
   43,   44,   45,   16,  132,  198,   60,   41,   62,   59,
   92,   60,   41,   62,   43,   44,   45,   60,  224,   62,
   44,  123,   41,   97,   95,  257,   60,  101,   62,   41,
  104,   60,   40,   62,  159,   41,  110,   43,   44,   45,
   41,   60,  125,   62,   41,   44,   59,  129,   60,   41,
   62,  114,  115,   41,   60,   44,   62,   40,   40,   60,
   44,   62,   44,   60,   43,   62,   45,  273,   60,   44,
   62,  145,   60,   45,   62,  152,  140,  151,   61,  150,
  125,   60,  121,   62,  257,  261,  262,  264,   60,   42,
   62,   17,   44,   40,   47,  261,  262,   44,  180,  224,
   40,  125,   44,  177,   44,   18,   43,   40,   45,   61,
   44,  157,   44,  187,   61,   44,   44,   59,  182,   44,
   44,   61,   44,  194,   42,   44,  125,   58,   61,   47,
  204,  208,   61,   44,  257,  258,  125,  211,   64,   61,
  125,  125,  205,  206,  261,  262,  223,  221,  273,   62,
   61,  257,  198,  217,   40,  257,  233,  273,  260,  236,
   61,  263,  264,  257,  266,  257,  268,  273,  257,  258,
  273,  123,  274,  275,  276,  277,  123,  257,  258,  250,
  269,  270,  271,  272,  257,  258,   99,  257,  258,  123,
  123,  268,   61,  267,  120,  266,  125,   44,  123,  258,
   44,  278,  257,  277,  269,  270,  271,  272,  257,  258,
  223,   40,  123,   61,  288,  269,  270,  271,  272,   44,
  269,  270,  271,  272,  125,   44,  269,  270,  271,  272,
  156,   44,  267,   44,   61,  269,  270,  271,  272,   61,
  269,  270,  271,  272,   41,  123,   43,   61,   45,   44,
  269,  270,  271,  272,   61,   61,  125,  269,  270,  271,
  272,  257,  257,  269,  270,  271,  272,   40,  269,  270,
  271,  272,  269,  270,  271,  272,   61,  269,  270,  271,
  272,  269,  270,  271,  272,  257,  258,   61,   41,   40,
  269,  270,  271,  272,   61,  208,  123,  269,  270,  271,
  272,  264,  257,  125,   61,  257,   41,  233,  260,  123,
  257,  263,   40,  260,  266,  262,  263,  257,  265,  266,
  260,   61,   61,  263,  257,  265,  266,  260,  257,   40,
  263,  260,  265,  266,  263,  257,  265,  266,  260,   61,
  125,  263,   37,  265,  266,  125,  257,   61,   40,  260,
  262,  125,  263,  257,   40,  266,  257,   41,  125,  260,
  262,   46,  263,  264,  268,  266,  262,  268,  125,  262,
  274,  275,  276,  274,  275,  276,  277,   41,   44,   43,
  257,   45,   77,   41,  123,  125,  262,   82,  257,   44,
   44,  260,  125,   44,  263,  264,   41,  266,   43,  268,
   45,   44,   41,  125,   40,  274,  275,  276,  277,  257,
   44,  125,  260,  125,   44,  263,  264,   40,  266,   44,
  268,   93,   94,  125,  262,  123,  274,  275,  276,  277,
  257,  125,   40,  260,   37,  257,  263,  132,  260,  266,
  267,  263,   41,  257,  266,  140,  260,  261,  262,  263,
  257,  257,  266,  260,  260,   40,  263,  263,  265,  266,
  266,   44,   37,   44,   44,   37,  125,   41,   41,   43,
  142,   45,  257,   44,   77,  260,   44,   41,  263,   82,
  265,  266,    0,  257,   41,   41,  260,  182,  125,  263,
  257,  265,  266,  260,   44,   44,  263,  262,  265,  266,
  257,  262,   77,  260,  176,   77,  263,   82,  265,  266,
   82,   41,   41,   43,   43,   45,   45,  257,  257,   15,
  260,  260,  217,  263,  263,  265,  266,  266,   41,  132,
   43,  153,   45,  233,   77,  257,  219,  140,  260,  236,
  212,  263,  204,  257,  266,   85,  260,  144,  150,  263,
  113,   -1,  266,   -1,   41,  257,   43,  132,   45,   41,
  132,   43,  264,   45,   -1,  140,  268,   -1,  140,  257,
   -1,   -1,  274,  275,  276,  277,  264,   -1,   -1,  182,
  268,   41,   -1,   43,   -1,   45,  274,  275,  276,  277,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,   -1,
   -1,   -1,   -1,   -1,   -1,  264,   -1,  182,   -1,  268,
  182,   -1,   -1,   -1,  217,  274,  275,  276,  277,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  217,   -1,   -1,  217,
};
}
final static short YYFINAL=14;
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
"program : begin cuerpo_prog end ','",
"program : begin end ','",
"program : begin cuerpo_prog ','",
"program : cuerpo_prog end ','",
"program : begin cuerpo_prog end",
"program : ','",
"begin : '{'",
"end : '}'",
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
"declaracion_impl : header_impl ':' '{' funciones_impl '}'",
"header_impl : IMPL FOR ID",
"funciones_impl : funciones_impl funcion",
"funciones_impl : funcion",
};

//#line 460 "gramatica.y"
public static boolean declarando = true;

public static final String ERROR = "Error";
public static final String WARNING = "Warning";
public static final String NAME_MANGLING_CHAR = ".";

public static StringBuilder ambito = new StringBuilder();
public static final HashMap<String,StringBuilder> ambitos = new HashMap<String,StringBuilder>();

public static final List<String> errores_lexicos = new ArrayList<>();
public static final List<String> errores_sintacticos = new ArrayList<>();
public static final List<String> errores_semanticos = new ArrayList<>();
public static final List<String> estructuras_sintacticas = new ArrayList<>();
public static final List<HashMap<Integer,Terceto>> estructura_Tercetos = new ArrayList<>();
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

public StringBuilder obtenerAmbito(String id){
	if (!ambitos.containsKey(id)){
	System.out.println("El metodo a implementar no se encuentra o ya fue definido");}
	return ambitos.get(id);
}

public static void agregarListaTercetos(HashMap<Integer,Terceto> t){
	HashMap<Integer,Terceto> aux = new HashMap<Integer,Terceto>();
	aux = t;
	estructura_Tercetos.add(aux);
}

public static void imprimirListaTercetos() {
	// Imprimo la lista de Tercetos
	if (!estructura_Tercetos.isEmpty()) {
		for (HashMap<Integer,Terceto> hm : estructura_Tercetos){
                      imprimirTercetos(hm);
                      System.out.print("Terceto Nuevo");
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
        System.out.println("ambito" + " " + "");
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
                Parser.imprimirEstructuras(estructuras_sintacticas, "Estructuras");

        }



//#line 850 "Parser.java"
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
//#line 38 "gramatica.y"
{cambiarAmbito(":START"); Parser.declarando = true; tipo = TablaTipos.STRING_TYPE; cambiarAmbito("main");}
break;
case 10:
//#line 45 "gramatica.y"
{Parser.declarando = false;}
break;
case 12:
//#line 47 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
break;
case 13:
//#line 48 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
break;
case 23:
//#line 70 "gramatica.y"
{
                             int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
                             TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                             TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");
                             }
break;
case 24:
//#line 75 "gramatica.y"
{
                                          int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
                                          TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                          TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");
                                          }
break;
case 28:
//#line 87 "gramatica.y"
{salirAmbito(); agregarListaTercetos(codigoIntermedio);}
break;
case 29:
//#line 88 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el cuerpo de la funcion");}
break;
case 30:
//#line 91 "gramatica.y"
{salirAmbito(); agregarListaTercetos(codigoIntermedio);}
break;
case 31:
//#line 95 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
						TablaSimbolos.agregarAtributo(ptr_id, "tipo", "VOID_TYPE");
                    	TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de metodo");
                    	agregarEstructura(estructuras_sintacticas, "Declaracion de Funcion");
                        cambiarAmbito(val_peek(0).sval);
                        ambitos.put(val_peek(0).sval, ambito);}
break;
case 32:
//#line 101 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera nombre de la funcion");}
break;
case 37:
//#line 113 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
	     	        TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                    TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");}
break;
case 38:
//#line 116 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el tipo del parametro");}
break;
case 39:
//#line 117 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el nombre del parametro");}
break;
case 40:
//#line 118 "gramatica.y"
{tipo = TablaTipos.CLASS_TYPE; int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
                                                                                     TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                                                                     TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");}
break;
case 41:
//#line 124 "gramatica.y"
{tipo = TablaTipos.UINT_TYPE;}
break;
case 42:
//#line 125 "gramatica.y"
{tipo = TablaTipos.SHORT_TYPE;}
break;
case 43:
//#line 126 "gramatica.y"
{tipo = TablaTipos.DOUBLE_TYPE;}
break;
case 44:
//#line 130 "gramatica.y"
{}
break;
case 56:
//#line 155 "gramatica.y"
{int aux = generarTerceto(val_peek(1).sval,val_peek(4).sval+val_peek(3).sval+val_peek(2).sval,val_peek(0).sval);}
break;
case 57:
//#line 156 "gramatica.y"
{int aux = generarTerceto(val_peek(3).sval,val_peek(6).sval+val_peek(5).sval+val_peek(4).sval,val_peek(2).sval+val_peek(1).sval+val_peek(0).sval);}
break;
case 58:
//#line 157 "gramatica.y"
{int aux = generarTerceto(val_peek(6).sval,val_peek(9).sval+val_peek(5).sval+val_peek(4).sval,val_peek(3).sval+"("+")"+val_peek(1).sval);}
break;
case 60:
//#line 161 "gramatica.y"
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
case 61:
//#line 175 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
break;
case 62:
//#line 178 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("DO","-","-"))+ "]";
         agregarEstructura(estructuras_sintacticas, "Sentencia DO_UNTIL ");}
break;
case 66:
//#line 185 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
break;
case 67:
//#line 186 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera un '{' "); }
break;
case 71:
//#line 194 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 73:
//#line 196 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 75:
//#line 198 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 77:
//#line 203 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
                                                                      yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 78:
//#line 205 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
                                                                                 yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 79:
//#line 207 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 80:
//#line 208 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 81:
//#line 211 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");}
break;
case 82:
//#line 214 "gramatica.y"
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
case 83:
//#line 227 "gramatica.y"
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
case 84:
//#line 238 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 85:
//#line 241 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
																	yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 86:
//#line 243 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
											   yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 87:
//#line 245 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan llaves "); }
break;
case 88:
//#line 246 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 89:
//#line 247 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
break;
case 90:
//#line 248 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 91:
//#line 251 "gramatica.y"
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
case 92:
//#line 262 "gramatica.y"
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
case 93:
//#line 273 "gramatica.y"
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
//#line 284 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 95:
//#line 285 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 96:
//#line 286 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias luego de la condicion");}
break;
case 97:
//#line 287 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "No se espera ',' "); }
break;
case 98:
//#line 292 "gramatica.y"
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
case 99:
//#line 304 "gramatica.y"
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
case 100:
//#line 314 "gramatica.y"
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
case 101:
//#line 324 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 102:
//#line 325 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 103:
//#line 326 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 104:
//#line 329 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("BI","incompleto","-"))+ "]";  }
break;
case 105:
//#line 333 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("BF",val_peek(1).sval,"incompleto"))+ "]";  }
break;
case 106:
//#line 334 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera '(' al principio de la condicion");}
break;
case 107:
//#line 335 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera una condicion");}
break;
case 109:
//#line 340 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 110:
//#line 343 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,val_peek(2).sval,val_peek(0).sval))+ "]";}
break;
case 111:
//#line 345 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion luego del comparador");}
break;
case 112:
//#line 346 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion antes del comparador");}
break;
case 119:
//#line 357 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
									  int aux = generarTerceto(val_peek(3).sval,val_peek(4).sval,val_peek(1).sval);
						}
break;
case 120:
//#line 360 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
										int aux = generarTerceto("+",val_peek(4).sval,val_peek(1).sval);
										int aux2 = generarTerceto("=",val_peek(4).sval,"[" + aux +"]");
			}
break;
case 121:
//#line 364 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								int aux = generarTerceto(val_peek(1).sval,val_peek(2).sval,val_peek(0).sval);}
break;
case 122:
//#line 367 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								 int aux = generarTerceto("+",val_peek(2).sval,val_peek(0).sval);
                        		 int aux2 = generarTerceto("=",val_peek(2).sval,"[" + aux +"]");
			}
break;
case 123:
//#line 371 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 124:
//#line 372 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un identificador en el lado izquierdo de la asignacion");}
break;
case 125:
//#line 373 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 126:
//#line 376 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";
                                            }
break;
case 127:
//#line 378 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 129:
//#line 382 "gramatica.y"
{ yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 130:
//#line 383 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("/",yyval.sval,val_peek(0).sval))+ "]";}
break;
case 132:
//#line 387 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";  }
break;
case 133:
//#line 388 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("/",yyval.sval,val_peek(0).sval))+ "]";}
break;
case 137:
//#line 394 "gramatica.y"
{}
break;
case 140:
//#line 399 "gramatica.y"
{}
break;
case 141:
//#line 402 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo(val_peek(0).sval));}
break;
case 142:
//#line 405 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
                   		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
                   		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo(val_peek(0).sval));
                   		String lexema = negarConstante(val_peek(0).sval);}
break;
case 143:
//#line 411 "gramatica.y"
{ String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                          TablaSimbolos.agregarSimbolo(nombre);
                          int puntero = TablaSimbolos.obtenerSimbolo(nombre);
                          }
break;
case 144:
//#line 414 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Comentario");}
break;
case 145:
//#line 415 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un mensaje luego del PRINT ");}
break;
case 146:
//#line 419 "gramatica.y"
{}
break;
case 147:
//#line 420 "gramatica.y"
{}
break;
case 148:
//#line 423 "gramatica.y"
{}
break;
case 149:
//#line 427 "gramatica.y"
{ salirAmbito();
 						tipo = TablaTipos.CLASS_TYPE;
						int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(2).sval + Parser.ambito.toString());
						TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de clase");
						TablaSimbolos.agregarAtributo(ptr_id, "tipo", "CLASS_TYPE");}
break;
case 151:
//#line 434 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una declaracion dentro de las llaves");}
break;
case 152:
//#line 435 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una ID previo a las llaves");}
break;
case 153:
//#line 438 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Declaracion de Clase");
						cambiarAmbito(val_peek(0).sval);
                        int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                        TablaSimbolos.agregarAtributo(ptr_id, "tipo", "CLASS_TYPE");
                        TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de clase");}
break;
case 154:
//#line 445 "gramatica.y"
{salirAmbito();}
break;
case 155:
//#line 448 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de IMPL para el void " + val_peek(0).sval);
						 ambito.setLength(0);
						 ambito = obtenerAmbito(val_peek(0).sval);
						 cambiarAmbito(val_peek(0).sval);}
break;
//#line 1536 "Parser.java"
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
