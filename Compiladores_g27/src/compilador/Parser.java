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
   28,   28,   43,   43,   43,   43,   43,   43,   43,   44,
   44,   44,   44,   44,   44,   45,   40,   40,   40,   34,
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
    0,    0,    0,    0,   92,    0,  159,    0,    0,   61,
    0,    0,  134,  135,    0,    0,    0,   36,    0,   48,
   49,    0,    0,   62,   82,   86,   85,   81,   80,    0,
  106,   87,    0,    0,   96,    0,    0,    0,  156,  158,
    0,  142,   31,   35,    0,   47,    0,    0,   95,   88,
    0,    0,    0,    0,    0,    0,    0,    0,   30,   67,
    0,    0,    0,    0,    0,   94,   90,    0,    0,   65,
    0,  100,  103,    0,   60,  102,    0,  101,
};
final static short yydgoto[] = {                          4,
    5,    6,   21,   66,    7,   22,   23,   24,   25,   26,
   27,   28,   29,   30,   31,   32,   33,   34,  133,  237,
  238,  134,  239,  240,   35,   36,   37,   38,   39,   40,
   41,   42,   87,   99,   88,  100,   89,   90,   43,  101,
  188,  220,  159,  253,  254,  102,  103,  174,   61,   62,
  175,   63,  170,  114,  171,   44,   45,  229,
};
final static short yysindex[] = {                       -35,
    0,    0,    0,    0, -216,  130,    0,  207,  -36,    0,
 -153, -132,    0, -102,    0,    0,    0, -130,  116,  163,
   34,  207,  207,    0,    0,    0,    0,    0,   91, -101,
    0,    0,    0,  135,    0,    0,  141,  154,  157,  213,
  225,  -57,  -38,  -21,  -46,   34,    0,   23,   -6,   25,
  199,    0,    0,    0,  166,    0,  256,    0,   40,  -10,
  152,    0,    0,    0,   17,  353,  356,  207,    0,  248,
    0,    0,  146,  246,  327,    0,    0,    0,    0,    0,
   26,  370,  243,  368,  371,  359,  177,  310,    0,  377,
  -38,    0,    0,    0,    0,    0,    0,    2,   31,   71,
   90,    0,  116,    0,  -97,  366,  386,  116,  -10,   83,
  116,  -10,    0,    0,    0,  116,    0, -184, -184,  116,
  116,    0,    0,    0,    0,  248, -101,    0,    0,    0,
  176,  190,  407,    0,  -42,  416,    0,  274,    0,    0,
    0,  417,    0,    0,    0,  236,    0,   45,    0,  -42,
  116,  -27,    0,  392,    0,   97,  116,  126,  -69,  -10,
    0,  349,  335,    0,  303,  327,  209,  410,  -10,  424,
    0,  428,    0,  208,    0,  208,    0,    0,    0,    0,
    5,   49,  116,  429,    0,  -42,    0,  228,    0,    0,
  -10,    0,  116,  347,  435,  437,    0,  217,    0,  162,
    0,  223,    0,  454,  451,    0,    0,  116,  116,  116,
    0,  186,    0,  474,  116,   53,  122,    0,  239,  242,
  485,  180,  463,  116,    0,  320,    0,  458, -117,    0,
  253,  477,    0,    0,  476,  207,  388,    0,  162,    0,
    0,  491,  518,    0,    0,    0,    0,    0,    0,  492,
    0,    0,  259,  112,    0,  544,  498,  327,    0,    0,
  489,    0,    0,    0,  499,    0,  421,  512,    0,    0,
  522,  162,  116,  151,  514,  302,  524,  327,    0,    0,
  443,  116,  334,  571,  531,    0,    0,  455,  538,    0,
  586,    0,    0,  116,    0,    0,  592,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  321,    0,
  101,  541,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   32,  119,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  356,    0,  548,
    0,    0,    0,    0,    0,    0,  -30,    0,    0,  549,
    7,    0,    0,    0,    0,    0,    0,  137,    0,  181,
    0,    0,    0,    0,  557,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  284,  288,    0,    0,    0,  309,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  353,    0,
    0,  555,    0,    0,    0,  563,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  251,  321,    0,    0,    0,
    0,  564,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   58,    0,    0,    0,    0,    0,    0,  -13,    0,   62,
    0,    0,    0,    0,    0,  557,    0,    0,  567,    0,
    0,   -5,    0,   15,    0,   38,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   67,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  565,    0,    0,  563,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  486,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  357,    0,    0,    0,  557,    0,    0,
  576,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  360,    0,    0,    0,    0,    0,    0,  557,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   10,  616,  187,  100,    0,   81,  -16,   56,    0,    0,
    0,    0,  167,  -70, -176,    0,    0, -158, -139,    0,
  394,    0,    0,  389,  -22,    0,  444,    0,  450,  469,
    0,    0,  -45,  -18,  551,  -19,  -43,    0,  472,  545,
    0,    0,  483,    0,    0,  490,   54,  523,    0,  192,
    0,    0,  433,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=689;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         60,
   71,   98,   59,   19,  132,   68,   59,  259,    2,   49,
  137,  137,  137,  137,  137,   20,  137,   97,   49,   96,
   55,   97,  104,   96,   50,  227,  204,  161,  109,  137,
  112,  137,  118,   50,  119,  140,  140,  140,  140,  140,
    1,  140,  147,  228,  144,   71,   59,  130,  211,  130,
  130,  130,  260,    3,  140,  128,  140,  128,  128,  128,
  122,   97,  108,   96,  111,   83,  130,   59,  130,   59,
  228,  149,  172,  173,  128,   12,  128,   69,  129,  148,
  129,  129,  129,  160,  158,  189,   50,    3,  165,  213,
   97,  168,   96,  244,  144,  132,  169,  129,  113,  129,
  187,  105,  114,   70,   97,   52,   96,  112,   97,   93,
   96,   93,   97,  118,   96,  119,  182,  113,  277,  113,
   67,  114,  166,  114,   53,   69,  112,  212,  112,  157,
   97,  191,   96,  155,   72,   71,  157,  195,  289,  158,
  155,   64,  219,  167,  147,  107,   12,   56,  126,   73,
   19,  273,  150,  151,   54,   47,   12,   19,   64,  127,
   59,  147,   13,  214,  123,  246,   12,  216,  200,  197,
   14,  247,   19,  221,   75,   51,   15,   16,   17,   18,
   11,   69,   19,  226,   76,  162,   19,  132,  169,  241,
   19,  198,  199,  120,   46,  243,   74,   77,  121,   81,
   78,  150,   10,   71,  256,   11,   65,  132,   13,   82,
  106,   19,  156,  241,   57,   58,  241,   69,   57,   58,
   47,    1,   19,   19,   14,  147,   92,   93,   94,   95,
   92,   93,   94,   95,  272,  150,   48,  274,  137,  137,
  137,  137,  113,   13,   83,   48,   19,   93,   93,  209,
  110,   71,    3,  284,  210,  283,   79,   73,   57,   58,
   71,   11,  291,  140,  140,  140,  140,   19,   80,  150,
   92,   93,   94,   95,  297,  130,  130,  130,  130,   57,
   58,   57,   58,  128,  128,  128,  128,   64,   19,  129,
  115,   69,  236,   51,   15,  116,   19,  117,   48,   92,
   93,   94,   95,   19,   73,   14,  129,  129,  129,  129,
  235,  177,  178,   92,   93,   94,   95,   92,   93,   94,
   95,   92,   93,   94,   95,  257,  113,  113,  113,  113,
  114,  114,  114,  114,   19,  112,  112,  112,  112,   92,
   93,   94,   95,  203,   75,  118,  152,  119,   73,   10,
   83,  153,   11,  152,  154,   13,   10,  147,   83,   11,
  147,  154,   13,  147,   25,  147,  147,  137,  152,   77,
   19,   10,   57,   58,   11,   15,  271,   13,   81,   25,
   19,   10,  152,  245,   11,   10,    9,   13,   11,   10,
  196,   13,   11,   12,   19,   13,  124,   14,  185,  125,
  233,  234,  128,   15,   16,   17,   18,  152,   75,  135,
   10,  139,   73,   11,  140,  285,   13,  141,  152,    9,
  145,   10,   10,  163,   11,   11,   12,   13,   13,  164,
   14,  193,  179,   77,  143,  192,   15,   16,   17,   18,
  251,  252,    9,  142,   64,   10,  180,  181,   11,   12,
  206,   13,  118,   14,  119,  183,  186,  202,  292,   15,
   16,   17,   18,    9,  207,  205,   10,  208,  215,   11,
   12,  222,   13,  201,   14,  223,  224,  118,  225,  119,
   15,   16,   17,   18,   81,   84,   12,   10,  217,  218,
   11,   85,   81,   13,  230,   10,  231,  258,   11,   81,
  248,   13,   10,  249,  127,   11,  255,  136,   13,  261,
   86,   12,  265,   91,  242,   14,  118,  262,  119,  263,
  270,   15,   16,   17,   18,  250,   84,  118,  278,  119,
   81,   84,   85,   10,  267,  269,   11,   85,  184,   13,
   75,  276,  279,   75,   73,  280,   75,   73,   75,   75,
   73,   86,   73,   73,   91,  281,   86,  286,  268,   91,
  118,  282,  119,  287,  288,   77,   81,  290,   77,   10,
  294,   77,   11,   77,   77,   13,  152,  212,  295,   10,
   34,   84,   11,  130,  275,   13,  118,   85,  119,   84,
  152,  127,  126,   10,  131,   85,   11,   37,  123,   13,
   15,   16,   17,  148,   41,  127,   86,  150,   58,   91,
   46,  293,   12,  118,   86,  119,   14,   91,  105,   59,
    8,  104,   15,   16,   17,   18,  296,  266,  118,  264,
  119,   84,  298,  138,  118,  146,  119,   85,  194,  190,
  232,  176,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   86,    0,    0,   91,
   84,    0,    0,    0,    0,    0,   85,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   86,    0,    0,   91,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         19,
   23,   40,   45,   61,   75,   22,   45,  125,   44,   46,
   41,   42,   43,   44,   45,    6,   47,   60,   46,   62,
  123,   60,   44,   62,   61,  202,  166,  125,   48,   60,
   50,   62,   43,   61,   45,   41,   42,   43,   44,   45,
  257,   47,   41,  202,   88,   68,   45,   41,   44,   43,
   44,   45,  229,  123,   60,   41,   62,   43,   44,   45,
   44,   60,   40,   62,   40,  123,   60,   45,   62,   45,
  229,   41,  257,  258,   60,   44,   62,   22,   41,   98,
   43,   44,   45,  103,  101,   41,   61,  123,  108,   41,
   60,  111,   62,   41,  138,  166,  116,   60,   41,   62,
  146,  123,   41,   23,   60,  259,   62,   41,   60,  123,
   62,  125,   60,   43,   62,   45,  135,   60,  258,   62,
   21,   60,   40,   62,  257,   70,   60,  123,   62,   40,
   60,  151,   62,   44,   44,  158,   40,  157,  278,  156,
   44,  125,  188,   61,   44,   46,  264,  278,   68,   59,
   61,   40,   99,  100,  257,  257,  125,   61,  125,  257,
   45,   61,   44,  183,   65,   44,  264,  186,  159,   44,
  268,  217,   61,  193,   40,    9,  274,  275,  276,  277,
   44,  126,   61,  200,   44,  105,   61,  258,  208,  212,
   61,  261,  262,   42,    8,  215,   30,   44,   47,  257,
   44,  148,  260,  226,  224,  263,   20,  278,  266,  267,
  257,   61,  123,  236,  257,  258,  239,  162,  257,  258,
  257,  257,   61,   61,   44,  125,  269,  270,  271,  272,
  269,  270,  271,  272,  123,  182,  273,  254,  269,  270,
  271,  272,   44,  125,  123,  273,   61,  261,  262,   42,
  257,  274,  123,  273,   47,  272,   44,   59,  257,  258,
  283,  125,  282,  269,  270,  271,  272,   61,   44,  216,
  269,  270,  271,  272,  294,  269,  270,  271,  272,  257,
  258,  257,  258,  269,  270,  271,  272,  125,   61,   44,
  125,  236,  212,  127,   44,   40,   61,  258,  273,  269,
  270,  271,  272,   61,   59,  125,  269,  270,  271,  272,
  125,  120,  121,  269,  270,  271,  272,  269,  270,  271,
  272,  269,  270,  271,  272,  226,  269,  270,  271,  272,
  269,  270,  271,  272,   61,  269,  270,  271,  272,  269,
  270,  271,  272,   41,   61,   43,  257,   45,   61,  260,
  123,  262,  263,  257,  265,  266,  260,  257,  123,  263,
  260,  265,  266,  263,   44,  265,  266,  125,  257,   61,
   61,  260,  257,  258,  263,  125,  265,  266,  257,   59,
   61,  260,  257,  262,  263,  260,  257,  266,  263,  260,
  265,  266,  263,  264,   61,  266,   44,  268,  125,   44,
  209,  210,  257,  274,  275,  276,  277,  257,  125,   40,
  260,   44,  125,  263,   44,  265,  266,   59,  257,  257,
   44,  260,  260,   58,  263,  263,  264,  266,  266,   44,
  268,   40,  257,  125,  125,   44,  274,  275,  276,  277,
  261,  262,  257,  267,  125,  260,  257,   41,  263,  264,
   41,  266,   43,  268,   45,   40,   40,  123,  125,  274,
  275,  276,  277,  257,   41,  257,  260,   40,   40,  263,
  264,  125,  266,  125,  268,   41,   40,   43,  262,   45,
  274,  275,  276,  277,  257,   42,  264,  260,  261,  262,
  263,   42,  257,  266,   41,  260,   46,   40,  263,  257,
  262,  266,  260,  262,  257,  263,   44,  265,  266,  257,
   42,  264,  125,   42,   41,  268,   43,   41,   45,   44,
  262,  274,  275,  276,  277,   41,   83,   43,   40,   45,
  257,   88,   83,  260,   44,   44,  263,   88,  265,  266,
  257,   44,   44,  260,  257,  125,  263,  260,  265,  266,
  263,   83,  265,  266,   83,   44,   88,   44,   41,   88,
   43,   40,   45,  262,   41,  257,  257,  125,  260,  260,
   40,  263,  263,  265,  266,  266,  257,  123,   41,  260,
   40,  138,  263,  257,   41,  266,   43,  138,   45,  146,
  257,   44,   44,  260,  268,  146,  263,   41,   44,  266,
  274,  275,  276,   41,   41,  257,  138,   41,   44,  138,
  125,   41,  264,   43,  146,   45,  268,  146,  262,   44,
    5,  262,  274,  275,  276,  277,   41,  239,   43,  236,
   45,  188,   41,   83,   43,   91,   45,  188,  156,  150,
  208,  119,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  188,   -1,   -1,  188,
  217,   -1,   -1,   -1,   -1,   -1,  217,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  217,   -1,   -1,  217,
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
"seleccion_en_do : header_if condicion_salto_if if_seleccion_do END_IF",
"seleccion_en_do : header_if condicion_salto_if if_seleccion_do else_seleccion_do END_IF",
"seleccion_en_do : header_if condicion_salto_if if_seleccion_do bloque_sentencias_do END_IF",
"seleccion_en_do : header_if condicion_salto_if if_seleccion_do ELSE END_IF",
"header_if : IF",
"if_seleccion_do : bloque_sentencias_do",
"else_seleccion_do : ELSE bloque_sentencias_do",
"else_seleccion_do : ELSE ','",
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

//#line 422 "gramatica.y"
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



//#line 801 "Parser.java"
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
case 31:
//#line 95 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el cuerpo de la funcion");}
break;
case 33:
//#line 102 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
			TablaSimbolos.agregarAtributo(ptr_id, "tipo", "VOID_TYPE");
                    	TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de metodo");
                    	agregarEstructura(estructuras_sintacticas, "Declaracion de Funcion");}
break;
case 34:
//#line 106 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera nombre de la funcion");}
break;
case 39:
//#line 118 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
	     	    TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                    TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");}
break;
case 40:
//#line 121 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el tipo del parametro");}
break;
case 41:
//#line 122 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el nombre del parametro");}
break;
case 42:
//#line 123 "gramatica.y"
{tipo = TablaTipos.CLASS_TYPE; int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval + Parser.ambito.toString());
                                                                                     TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                                                                     TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");}
break;
case 43:
//#line 129 "gramatica.y"
{tipo = TablaTipos.UINT_TYPE;}
break;
case 44:
//#line 130 "gramatica.y"
{tipo = TablaTipos.SHORT_TYPE;}
break;
case 45:
//#line 131 "gramatica.y"
{tipo = TablaTipos.DOUBLE_TYPE;}
break;
case 46:
//#line 135 "gramatica.y"
{}
break;
case 63:
//#line 168 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
break;
case 64:
//#line 171 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia DO_UNTIL ");}
break;
case 68:
//#line 177 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
break;
case 69:
//#line 178 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera un '{' "); }
break;
case 73:
//#line 186 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 75:
//#line 188 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 77:
//#line 190 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 81:
//#line 197 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 82:
//#line 198 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 83:
//#line 201 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");}
break;
case 86:
//#line 208 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 87:
//#line 211 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
																	{yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }}
break;
case 88:
//#line 213 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
											   yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 89:
//#line 215 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan llaves "); }
break;
case 90:
//#line 216 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 91:
//#line 217 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
break;
case 92:
//#line 218 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 93:
//#line 221 "gramatica.y"
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
//#line 232 "gramatica.y"
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
//#line 243 "gramatica.y"
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
//#line 254 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 97:
//#line 255 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 98:
//#line 256 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias luego de la condicion");}
break;
case 99:
//#line 257 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "No se espera ',' "); }
break;
case 100:
//#line 262 "gramatica.y"
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
//#line 274 "gramatica.y"
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
//#line 284 "gramatica.y"
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
//#line 294 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 104:
//#line 295 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 105:
//#line 296 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 106:
//#line 299 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("BI","incompleto","-"))+ "]";  }
break;
case 107:
//#line 303 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("BF",val_peek(1).sval,"incompleto"))+ "]";  }
break;
case 108:
//#line 304 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera '(' al principio de la condicion");}
break;
case 109:
//#line 305 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera una condicion");}
break;
case 111:
//#line 310 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 112:
//#line 313 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,val_peek(2).sval,val_peek(0).sval))+ "]";}
break;
case 113:
//#line 315 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion luego del comparador");}
break;
case 114:
//#line 316 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion antes del comparador");}
break;
case 121:
//#line 327 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
									  int aux = generarTerceto(val_peek(3).sval,val_peek(4).sval,val_peek(1).sval);
						}
break;
case 122:
//#line 330 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
										int aux = generarTerceto("+",val_peek(4).sval,val_peek(1).sval);
										int aux2 = generarTerceto("=",val_peek(4).sval,"[" + aux +"]");
			}
break;
case 123:
//#line 334 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								int aux = generarTerceto(val_peek(1).sval,val_peek(2).sval,val_peek(0).sval);}
break;
case 124:
//#line 337 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								 int aux = generarTerceto("+",val_peek(2).sval,val_peek(0).sval);
                        		 int aux2 = generarTerceto("=",val_peek(2).sval,"[" + aux +"]");
			}
break;
case 125:
//#line 341 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 126:
//#line 342 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un identificador en el lado izquierdo de la asignacion");}
break;
case 127:
//#line 343 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 128:
//#line 346 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";
                                            }
break;
case 129:
//#line 348 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 131:
//#line 352 "gramatica.y"
{ yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 132:
//#line 353 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("/",yyval.sval,val_peek(0).sval))+ "]";}
break;
case 134:
//#line 357 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";  }
break;
case 135:
//#line 358 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("/",yyval.sval,val_peek(0).sval))+ "]";}
break;
case 139:
//#line 364 "gramatica.y"
{}
break;
case 142:
//#line 369 "gramatica.y"
{}
break;
case 143:
//#line 372 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo(val_peek(0).sval));}
break;
case 144:
//#line 375 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
                   		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
                   		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo(val_peek(0).sval));
                   		String lexema = negarConstante(val_peek(0).sval);}
break;
case 145:
//#line 381 "gramatica.y"
{ String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                          TablaSimbolos.agregarSimbolo(nombre);
                          int puntero = TablaSimbolos.obtenerSimbolo(nombre);
                          }
break;
case 146:
//#line 384 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Comentario");}
break;
case 147:
//#line 385 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un mensaje luego del PRINT ");}
break;
case 148:
//#line 389 "gramatica.y"
{}
break;
case 149:
//#line 390 "gramatica.y"
{}
break;
case 150:
//#line 393 "gramatica.y"
{}
break;
case 151:
//#line 397 "gramatica.y"
{  tipo = TablaTipos.CLASS_TYPE;
						int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(2).sval + Parser.ambito.toString());
						TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de clase");
						TablaSimbolos.agregarAtributo(ptr_id, "tipo", "CLASS_TYPE");}
break;
case 153:
//#line 403 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una declaracion dentro de las llaves");}
break;
case 154:
//#line 404 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una ID previo a las llaves");}
break;
case 155:
//#line 407 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Declaracion de Clase");}
break;
case 157:
//#line 413 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de IMPL");}
break;
//#line 1400 "Parser.java"
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
