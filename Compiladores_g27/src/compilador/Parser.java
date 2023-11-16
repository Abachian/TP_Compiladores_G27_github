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
   44,   44,   44,   44,   44,   40,   40,   40,   34,   34,
   45,   45,   45,   46,   46,   46,   46,   46,   46,   27,
   27,   27,   27,   27,   27,   27,   36,   36,   36,   48,
   48,   48,   47,   47,   47,   49,   49,   49,   50,   50,
   50,   51,   51,   53,   29,   29,   52,   52,   54,   11,
   11,   11,   11,   55,   17,   56,   57,   57,
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
    5,    5,    1,    1,    2,    2,    6,    4,    7,    8,
    3,    5,    1,    6,    5,    4,    2,    1,    2,    4,
    6,    5,    4,    2,    1,    3,    2,    2,    1,    3,
    3,    2,    2,    1,    1,    1,    1,    1,    1,    5,
    5,    3,    3,    2,    2,    2,    3,    3,    1,    3,
    3,    1,    3,    3,    1,    1,    1,    4,    1,    1,
    4,    1,    2,    0,    3,    1,    0,    1,    1,    4,
    2,    3,    3,    2,    6,    2,    2,    1,
};
final static short yydefred[] = {                         0,
   10,    6,    8,    0,    0,    0,    7,    0,    0,   83,
    0,    0,   64,    0,   43,   45,   44,    0,    0,    0,
    0,    0,    0,   17,   18,   19,   20,   21,    0,    0,
   27,   28,   29,    0,   51,   52,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   25,    0,    0,    0,
    0,  144,   33,  154,    0,  156,    0,  142,    0,    0,
    0,  132,  137,    9,    0,    0,    0,    0,   16,    0,
   50,   24,    0,    0,    0,   53,   54,   55,   56,   57,
    0,    0,    0,    0,    0,    0,    0,    0,   70,    0,
    0,  117,  116,  114,  115,  119,  118,    0,    0,    0,
    0,  109,    0,  151,    0,    0,    0,    0,    0,    0,
    0,    0,   22,  145,  153,    0,  143,    0,    0,    0,
    0,    3,    5,    2,    4,    0,    0,   26,   23,   40,
    0,    0,    0,   38,    0,    0,   68,    0,   78,   74,
   72,    0,   69,   71,   76,    0,  108,    0,  107,    0,
    0,    0,   91,    0,   98,    0,    0,    0,    0,    0,
  152,    0,    0,    1,    0,    0,    0,    0,    0,    0,
  148,    0,  140,    0,  135,    0,  130,  131,   42,   39,
    0,    0,    0,    0,   66,    0,   84,    0,  106,  110,
    0,   97,    0,    0,    0,    0,   99,    0,   88,    0,
  150,    0,  121,    0,    0,  120,  138,    0,    0,    0,
   32,    0,   63,    0,    0,    0,    0,   79,    0,    0,
    0,    0,    0,    0,   92,    0,  158,    0,    0,   61,
    0,    0,  133,  134,    0,    0,    0,   36,    0,   48,
   49,    0,    0,   62,   82,   86,   85,   81,   80,    0,
    0,   87,    0,   96,    0,    0,    0,  155,  157,    0,
  141,   31,   35,    0,   47,    0,    0,   95,    0,    0,
    0,    0,   89,    0,    0,    0,    0,   30,   67,    0,
    0,    0,    0,    0,   94,   90,    0,    0,   65,    0,
  100,  103,    0,   60,  102,    0,  101,
};
final static short yydgoto[] = {                          4,
    5,    6,   21,   66,    7,   22,   23,   24,   25,   26,
   27,   28,   29,   30,   31,   32,   33,   34,  133,  237,
  238,  134,  239,  240,   35,   36,   37,   38,   39,   40,
   41,   42,   87,   99,   88,  100,   89,   90,   43,  101,
  188,  220,  159,  253,  102,  103,  174,   61,   62,  175,
   63,  170,  114,  171,   44,   45,  229,
};
final static short yysindex[] = {                       -41,
    0,    0,    0,    0, -230,  126,    0,  181,   64,    0,
 -184, -174,    0, -107,    0,    0,    0, -149,   56,  147,
   43,  181,  181,    0,    0,    0,    0,    0,   93,  -98,
    0,    0,    0,  125,    0,    0,  133,  155,  163,  166,
  174,  238,  -38,   -4,  -82,   43,    0,    8,  -66,   34,
  110,    0,    0,    0,   72,    0,  183,    0,  -33,  104,
  109,    0,    0,    0,   -9,  184,  199,  181,    0,  341,
    0,    0,    1,  119,  346,    0,    0,    0,    0,    0,
  -47,  246,  280,  229,  300,  298,   96,  233,    0,  316,
  -38,    0,    0,    0,    0,    0,    0,   -2,   26,   62,
   83,    0,   56,    0,  355,  311,  328,   56,  104,   74,
   56,  104,    0,    0,    0,   56,    0,  -46,  -46,   56,
   56,    0,    0,    0,    0,  341,  -98,    0,    0,    0,
  116,  134,  352,    0,   81,  356,    0,  293,    0,    0,
    0,  357,    0,    0,    0,  275,    0,   31,    0,   81,
   56,  -20,    0,  158,    0,  -36,   56,  122,  -81,  104,
    0,  385,  276,    0,  218,  346,  149,  371,  104,  368,
    0,  377,    0,  153,    0,  153,    0,    0,    0,    0,
   65,   36,   56,  380,    0,   81,    0,  203,    0,    0,
  104,    0,   56,  301,  384,  388,    0,  169,    0,  258,
    0,  168,    0,  395,  396,    0,    0,   56,   56,   56,
    0,  210,    0,  394,   56,   40,  118,    0,  178,  189,
  405,   23,  399,   56,    0,  294,    0,  412,  -61,    0,
  197,  420,    0,    0,  418,  181,  343,    0,  258,    0,
    0,  427,  434,    0,    0,    0,    0,    0,    0,  445,
   99,    0,  221,    0,  471,  447,  346,    0,    0,  452,
    0,    0,    0,  450,    0,  372,  458,    0,  467,  258,
   56,  243,    0,  469,  249,  476,  346,    0,    0,  400,
   56,  347,  592,  479,    0,    0,  397,  485,    0,  598,
    0,    0,   56,    0,    0,  607,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,    0,  150,    0,
  111,  490,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   21,   32,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  184,    0,  487,
    0,    0,    0,    0,    0,    0,  -32,    0,    0,  199,
    6,    0,    0,    0,    0,    0,    0,   60,    0,   67,
    0,    0,    0,    0,  492,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  305,  309,    0,    0,    0,  334,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  495,    0,
    0,  503,    0,    0,    0,  507,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   68,  150,    0,    0,    0,
    0,  508,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   46,    0,    0,    0,    0,    0,    0,  -91,    0,   53,
    0,    0,    0,    0,    0,  492,    0,    0,  511,    0,
    0,  -24,    0,   11,    0,   18,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   58,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  517,    0,    0,  507,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  411,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
  282,    0,    0,    0,    0,    0,  492,    0,    0,  519,
    0,    0,    0,    0,    0,    0,    0,    0,  302,    0,
    0,    0,    0,    0,    0,    0,  492,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
   -1,  550,   49,  121,    0,  108,  -16,  131,    0,    0,
    0,    0,  152,  176, -112,    0,    0,  -99, -129,    0,
  331,    0,    0,  337,  -22,    0,  439,    0,  440,  446,
    0,    0,   27,  -65,  497,  -19,    7,    0,  500,  482,
    0,    0,  423,    0,  431,  496,  463,    0,  -76,    0,
    0,  379,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=717;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         60,
   71,   98,    2,  157,   20,   68,   59,  155,  136,  136,
  136,  136,  136,   50,  136,   55,  139,  139,  139,  139,
  139,   97,  139,   96,   19,   49,    1,  136,  109,  136,
  112,   93,  148,   93,  122,  139,  204,  139,  147,  104,
   50,    3,   59,  177,  178,   71,  129,  108,  129,  129,
  129,  127,   59,  127,  127,  127,   46,   97,  128,   96,
  128,  128,  128,  258,   12,  129,  149,  129,   65,  182,
  127,  189,  127,  111,   52,   13,  213,  128,   59,  128,
  244,    3,   53,  160,  158,   97,  112,   96,  165,  227,
   97,  168,   96,  113,  144,   97,  169,   96,  111,   97,
   59,   96,  228,   11,  118,  112,  119,  112,  211,   49,
   14,   15,  113,  166,  113,   64,  259,  111,  105,  111,
  216,   97,  157,   96,   50,   59,  155,  276,   56,  228,
   70,  191,  233,  234,  167,   71,   72,  195,  271,  158,
   97,   67,   96,   19,  144,   12,  118,  288,  119,   54,
  120,   73,   69,  113,  146,  121,   13,  200,   47,   19,
   51,  246,  129,  214,   75,  197,  107,   64,   73,   93,
   93,  146,  187,  221,  106,  126,   76,   73,   19,  198,
  199,   74,   19,  226,   11,  123,   19,  212,  169,  241,
  110,   14,   15,   25,  209,  243,  115,  193,   77,  210,
   69,  192,   12,   71,  255,  156,   78,   19,   25,   79,
  172,  173,  162,  241,  219,    1,  241,   80,   57,   58,
  152,  270,  116,   10,  117,   48,   11,  124,  154,   13,
   92,   93,   94,   95,  272,  146,  136,  136,  136,  136,
   83,   19,  125,  247,  139,  139,  139,  139,    3,   71,
  132,  283,   48,  282,   57,   58,   69,  128,  203,   71,
  118,  290,  119,   19,   57,   58,   92,   93,   94,   95,
   19,   64,  139,  296,  129,  129,  129,  129,   51,  127,
  127,  127,  127,  251,  252,  135,  128,  128,  128,  128,
   57,   58,   69,   19,   92,   93,   94,   95,   19,   92,
   93,   94,   95,   19,   92,   93,   94,   95,   92,   93,
   94,   95,   57,   58,  112,  112,  112,  112,   19,  236,
   47,  113,  113,  113,  113,   83,  111,  111,  111,  111,
   92,   93,   94,   95,  235,   19,   48,   57,   58,  152,
   19,  132,   10,  140,  153,   11,  256,  154,   13,   92,
   93,   94,   95,   19,   19,  152,  141,  143,   10,  145,
   83,   11,  142,  269,   13,   75,   69,  146,  163,   73,
  146,  164,  179,  146,   81,  146,  146,   10,  152,  245,
   11,   10,    9,   13,   11,   10,  196,   13,   11,   12,
  180,   13,  181,   14,   77,  183,  186,   83,  202,   15,
   16,   17,   18,    9,  137,  205,   10,   19,  207,   11,
   12,  206,   13,  118,   14,  119,  208,  185,   64,  215,
   15,   16,   17,   18,  223,  222,  118,  224,  119,   75,
  225,   12,  132,   73,  242,  230,  118,    9,  119,  248,
   10,  231,  254,   11,   12,  250,   13,  118,   14,  119,
  249,  257,  132,  260,   15,   16,   17,   18,   77,   81,
  261,  262,   10,  217,  218,   11,    9,  264,   13,   10,
  266,  291,   11,   12,  267,   13,  118,   14,  119,  161,
   84,   85,  273,   15,   16,   17,   18,   86,  268,   81,
  275,  277,   10,  278,   81,   11,  279,   10,   13,  152,
   11,  280,   10,   13,   82,   11,  281,  284,   13,  201,
  286,  274,  285,  118,  152,  119,  287,   10,  293,  212,
   11,   84,   85,   13,  289,  294,   84,   85,   86,   34,
  126,   81,   37,   86,   10,   46,   81,   11,  123,   10,
   13,   91,   11,  105,  136,   13,  122,  147,   41,   81,
  152,  149,   10,   10,    8,   11,   11,  184,   13,   13,
   58,   75,   59,  104,   75,   73,  263,   75,   73,   75,
   75,   73,  146,   73,   73,  265,   84,   85,  194,  138,
  190,  176,   91,   86,   84,   85,  232,   91,    0,    0,
   77,   86,    0,   77,  150,  151,   77,  127,   77,   77,
    0,    0,  130,  152,   12,    0,   10,    0,   14,   11,
    0,  127,   13,  131,   15,   16,   17,   18,   12,   15,
   16,   17,   14,    0,    0,    0,   84,   85,   15,   16,
   17,   18,  292,   86,  118,    0,  119,   91,  295,    0,
  118,  127,  119,  150,    0,   91,    0,  297,   12,  118,
    0,  119,   14,    0,    0,   84,   85,    0,   15,   16,
   17,   18,   86,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  150,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   91,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  150,    0,    0,    0,    0,   91,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         19,
   23,   40,   44,   40,    6,   22,   45,   44,   41,   42,
   43,   44,   45,   61,   47,  123,   41,   42,   43,   44,
   45,   60,   47,   62,   61,   46,  257,   60,   48,   62,
   50,  123,   98,  125,   44,   60,  166,   62,   41,   44,
   61,  123,   45,  120,  121,   68,   41,   40,   43,   44,
   45,   41,   45,   43,   44,   45,    8,   60,   41,   62,
   43,   44,   45,  125,   44,   60,   41,   62,   20,  135,
   60,   41,   62,   40,  259,   44,   41,   60,   45,   62,
   41,  123,  257,  103,  101,   60,   41,   62,  108,  202,
   60,  111,   62,   41,   88,   60,  116,   62,   41,   60,
   45,   62,  202,   44,   43,   60,   45,   62,   44,   46,
   44,   44,   60,   40,   62,  125,  229,   60,  123,   62,
  186,   60,   40,   62,   61,   45,   44,  257,  278,  229,
   23,  151,  209,  210,   61,  158,   44,  157,   40,  156,
   60,   21,   62,   61,  138,  125,   43,  277,   45,  257,
   42,   59,   22,   44,   44,   47,  125,  159,  257,   61,
    9,   44,   44,  183,   40,   44,   46,  125,   59,  261,
  262,   61,  146,  193,  257,   68,   44,   59,   61,  261,
  262,   30,   61,  200,  125,   65,   61,  123,  208,  212,
  257,  125,  125,   44,   42,  215,  125,   40,   44,   47,
   70,   44,  264,  226,  224,  123,   44,   61,   59,   44,
  257,  258,  105,  236,  188,  257,  239,   44,  257,  258,
  257,  123,   40,  260,  258,  273,  263,   44,  265,  266,
  269,  270,  271,  272,  251,  125,  269,  270,  271,  272,
  123,   61,   44,  217,  269,  270,  271,  272,  123,  272,
   75,  271,  273,  270,  257,  258,  126,  257,   41,  282,
   43,  281,   45,   61,  257,  258,  269,  270,  271,  272,
   61,  125,   44,  293,  269,  270,  271,  272,  127,  269,
  270,  271,  272,  261,  262,   40,  269,  270,  271,  272,
  257,  258,  162,   61,  269,  270,  271,  272,   61,  269,
  270,  271,  272,   61,  269,  270,  271,  272,  269,  270,
  271,  272,  257,  258,  269,  270,  271,  272,   61,  212,
  257,  269,  270,  271,  272,  123,  269,  270,  271,  272,
  269,  270,  271,  272,  125,   61,  273,  257,  258,  257,
   61,  166,  260,   44,  262,  263,  226,  265,  266,  269,
  270,  271,  272,   61,   61,  257,   59,  125,  260,   44,
  123,  263,  267,  265,  266,   61,  236,  257,   58,   61,
  260,   44,  257,  263,  257,  265,  266,  260,  257,  262,
  263,  260,  257,  266,  263,  260,  265,  266,  263,  264,
  257,  266,   41,  268,   61,   40,   40,  123,  123,  274,
  275,  276,  277,  257,  125,  257,  260,   61,   41,  263,
  264,   41,  266,   43,  268,   45,   40,  125,  125,   40,
  274,  275,  276,  277,   41,  125,   43,   40,   45,  125,
  262,  264,  257,  125,   41,   41,   43,  257,   45,  262,
  260,   46,   44,  263,  264,   41,  266,   43,  268,   45,
  262,   40,  277,  257,  274,  275,  276,  277,  125,  257,
   41,   44,  260,  261,  262,  263,  257,  125,  266,  260,
   44,  125,  263,  264,   41,  266,   43,  268,   45,  125,
   42,   42,  262,  274,  275,  276,  277,   42,   44,  257,
   44,   40,  260,   44,  257,  263,  125,  260,  266,  257,
  263,   44,  260,  266,  267,  263,   40,  265,  266,  125,
  262,   41,   44,   43,  257,   45,   41,  260,   40,  123,
  263,   83,   83,  266,  125,   41,   88,   88,   83,   40,
   44,  257,   41,   88,  260,  125,  257,  263,   44,  260,
  266,   42,  263,  262,  265,  266,   44,   41,   41,  257,
  257,   41,  260,  260,    5,  263,  263,  265,  266,  266,
   44,  257,   44,  262,  260,  257,  236,  263,  260,  265,
  266,  263,   91,  265,  266,  239,  138,  138,  156,   83,
  150,  119,   83,  138,  146,  146,  208,   88,   -1,   -1,
  257,  146,   -1,  260,   99,  100,  263,  257,  265,  266,
   -1,   -1,  257,  257,  264,   -1,  260,   -1,  268,  263,
   -1,  257,  266,  268,  274,  275,  276,  277,  264,  274,
  275,  276,  268,   -1,   -1,   -1,  188,  188,  274,  275,
  276,  277,   41,  188,   43,   -1,   45,  138,   41,   -1,
   43,  257,   45,  148,   -1,  146,   -1,   41,  264,   43,
   -1,   45,  268,   -1,   -1,  217,  217,   -1,  274,  275,
  276,  277,  217,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  182,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  188,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  216,   -1,   -1,   -1,   -1,  217,
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
"seleccion : header_if condicion_salto_if if_seleccion END_IF",
"seleccion : header_if condicion_salto_if '{' if_seleccion '}' else_seleccion END_IF",
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

//#line 360 "gramatica.y"
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
	t.print();
	return punteroTerceto -1;
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



//#line 795 "Parser.java"
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
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");}
break;
case 88:
//#line 212 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan llaves "); }
break;
case 90:
//#line 214 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 91:
//#line 215 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
break;
case 92:
//#line 216 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 96:
//#line 224 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 97:
//#line 226 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 98:
//#line 227 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias luego de la condicion");}
break;
case 99:
//#line 228 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "No se espera ',' "); }
break;
case 103:
//#line 236 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 104:
//#line 237 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 105:
//#line 238 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 107:
//#line 243 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera '(' al principio de la condicion");}
break;
case 108:
//#line 244 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera una condicion");}
break;
case 112:
//#line 253 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion luego del comparador");}
break;
case 113:
//#line 254 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion antes del comparador");}
break;
case 120:
//#line 265 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
									  int aux = generarTerceto(val_peek(3).sval,val_peek(4).sval,val_peek(1).sval);
						}
break;
case 121:
//#line 268 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
										int aux = generarTerceto("+",val_peek(4).sval,val_peek(1).sval);
										int aux2 = generarTerceto("=",val_peek(4).sval,"[" + aux +"]");
			}
break;
case 122:
//#line 272 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								int aux = generarTerceto(val_peek(1).sval,val_peek(2).sval,val_peek(0).sval);}
break;
case 123:
//#line 275 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								 int aux = generarTerceto("+",val_peek(2).sval,val_peek(0).sval);
                        		 int aux2 = generarTerceto("=",val_peek(2).sval,"[" + aux +"]");
			}
break;
case 124:
//#line 279 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 125:
//#line 280 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un identificador en el lado izquierdo de la asignacion");}
break;
case 126:
//#line 281 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 127:
//#line 284 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";
                                            }
break;
case 128:
//#line 286 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 130:
//#line 290 "gramatica.y"
{ yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 131:
//#line 291 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("/",yyval.sval,val_peek(0).sval))+ "]";}
break;
case 133:
//#line 295 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";  }
break;
case 134:
//#line 296 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("/",yyval.sval,val_peek(0).sval))+ "]";}
break;
case 138:
//#line 302 "gramatica.y"
{}
break;
case 141:
//#line 307 "gramatica.y"
{}
break;
case 142:
//#line 310 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo(val_peek(0).sval));}
break;
case 143:
//#line 313 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
                   		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
                   		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo(val_peek(0).sval));
                   		String lexema = negarConstante(val_peek(0).sval);}
break;
case 144:
//#line 319 "gramatica.y"
{ String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                          TablaSimbolos.agregarSimbolo(nombre);
                          int puntero = TablaSimbolos.obtenerSimbolo(nombre);
                          }
break;
case 145:
//#line 322 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Comentario");}
break;
case 146:
//#line 323 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un mensaje luego del PRINT ");}
break;
case 147:
//#line 327 "gramatica.y"
{}
break;
case 148:
//#line 328 "gramatica.y"
{}
break;
case 149:
//#line 331 "gramatica.y"
{}
break;
case 150:
//#line 335 "gramatica.y"
{  tipo = TablaTipos.CLASS_TYPE;
						int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(2).sval + Parser.ambito.toString());
						TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de clase");
						TablaSimbolos.agregarAtributo(ptr_id, "tipo", "CLASS_TYPE");}
break;
case 152:
//#line 341 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una declaracion dentro de las llaves");}
break;
case 153:
//#line 342 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una ID previo a las llaves");}
break;
case 154:
//#line 345 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Declaracion de Clase");}
break;
case 156:
//#line 351 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de IMPL");}
break;
//#line 1290 "Parser.java"
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
