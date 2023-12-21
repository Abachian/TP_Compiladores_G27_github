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
   21,   21,   22,   22,   22,   12,   12,   12,   19,   23,
   23,   24,    5,    5,   25,   26,   26,   26,   26,   26,
   31,   31,   31,   30,   30,   32,   33,   33,   33,   33,
   33,   35,   35,   37,   37,   37,   37,   37,   37,   37,
   38,   38,   38,   38,   39,   41,   42,   42,   28,   28,
   28,   28,   28,   28,   44,   44,   44,   44,   44,   44,
   44,   45,   45,   45,   45,   45,   45,   43,   40,   40,
   40,   34,   34,   46,   46,   46,   47,   47,   47,   47,
   47,   47,   27,   27,   27,   27,   27,   27,   27,   36,
   36,   36,   49,   49,   49,   48,   48,   48,   50,   50,
   50,   51,   51,   51,   52,   52,   29,   29,   53,   53,
   54,    9,    9,    9,    9,   55,   15,   56,   57,   57,
};
final static short yylen[] = {                            2,
    4,    3,    3,    3,    3,    1,    1,    1,    2,    1,
    1,    2,    3,    2,    1,    1,    1,    1,    1,    3,
    3,    2,    1,    3,    1,    1,    1,    8,    7,    5,
    2,    1,    2,    1,    0,    1,    2,    1,    1,    2,
    0,    1,    2,    1,    1,    1,    1,    1,    1,    2,
    1,    1,    2,    1,    1,    2,    2,    2,    2,    2,
    5,    7,    6,    6,    5,    1,    8,    3,    7,    2,
    2,    1,    2,    2,    1,    2,    1,    2,    1,    2,
    4,    5,    5,    5,    1,    1,    2,    2,    6,    7,
    4,    8,    3,    5,    1,    6,    5,    4,    2,    1,
    2,    4,    6,    5,    4,    2,    1,    1,    3,    2,
    2,    1,    3,    3,    2,    2,    1,    1,    1,    1,
    1,    1,    5,    5,    3,    3,    2,    2,    2,    3,
    3,    1,    3,    3,    1,    3,    3,    1,    1,    1,
    4,    1,    1,    4,    1,    2,    2,    1,    0,    1,
    1,    4,    2,    3,    3,    2,    5,    3,    2,    1,
};
final static short yydefred[] = {                         0,
    0,   85,    0,    0,   66,    0,   46,   48,   47,    0,
    6,    7,    0,    0,    0,    0,    0,    0,   15,   16,
   17,   18,   19,    0,    0,   25,   26,   27,    0,   54,
   55,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   23,    0,    0,    0,    0,  147,   31,  156,    0,    0,
    0,  145,    0,    0,    0,  135,  140,    8,    0,    0,
    0,    0,   14,    0,   53,   22,    0,    0,    0,   56,
   57,   58,   59,   60,    0,    0,    0,    0,    0,    0,
    0,    0,   72,    0,    0,  120,  119,  117,  118,  122,
  121,    0,    0,    0,    0,  112,    0,  153,    0,    0,
    0,    0,    0,    0,    0,   20,  155,  158,    0,  146,
    0,    0,    0,    0,    3,    0,    2,    4,    0,    0,
   24,   21,   38,    0,    0,    0,   36,    0,    0,   70,
    0,   80,   76,   74,    0,   71,   73,   78,    0,  111,
    0,  110,    0,    0,    0,   93,    0,  100,    0,    0,
    0,    0,    0,  154,    0,    0,    0,    0,    0,    0,
    0,    0,  150,    0,  143,    0,  138,    0,  133,  134,
    1,   40,   37,    0,    0,    0,    0,   68,    0,   86,
    0,  109,  113,    0,   99,    0,    0,    0,    0,  101,
    0,   91,    0,  152,  160,    0,    0,  124,   44,    0,
    0,   42,    0,  123,  141,    0,    0,    0,   30,    0,
   65,    0,    0,    0,    0,   81,    0,    0,    0,    0,
    0,    0,    0,   94,    0,    0,  157,  159,   43,   63,
    0,    0,  136,  137,    0,    0,    0,   34,    0,   51,
   52,    0,    0,   64,   84,   83,   82,   88,   87,    0,
  108,   89,    0,    0,   98,    0,    0,    0,   62,  144,
   29,   33,    0,   50,    0,    0,   97,    0,    0,    0,
    0,   90,    0,    0,    0,   28,   69,    0,    0,    0,
    0,    0,   96,   92,   67,    0,  102,  105,    0,  104,
    0,  103,
};
final static short yydgoto[] = {                         14,
   15,   16,   60,   17,   18,   19,   20,   21,   22,   23,
   24,   25,   26,   27,   28,   29,  126,  237,  238,  127,
  201,  202,  239,  240,   30,   31,   32,   33,   34,   35,
   36,   37,   81,   93,   82,   94,   83,   84,   38,   95,
  181,  218,  219,  152,  254,   96,   97,  166,   55,   56,
  167,   57,  162,  163,   39,   40,  197,
};
final static short yysindex[] = {                       -36,
  -42,    0, -248, -243,    0, -113,    0,    0,    0, -251,
    0,    0,  -44,    0,  193,  -78,  151,  151,    0,    0,
    0,    0,    0,   99, -201,    0,    0,    0,   19,    0,
    0,   22,   28,   82,   92,  116,  247,  -24,   10,   31,
    0,   -8, -134,   24,  110,    0,    0,    0,   47,  -79,
  155,    0,  -71,  147,  -19,    0,    0,    0,   -9,  153,
  162,  151,    0,  369,    0,    0,  -31,  115,  336,    0,
    0,    0,    0,    0,  -56,  202,  252,  208,  227,  240,
   27,  318,    0,  251,  -24,    0,    0,    0,    0,    0,
    0,    3,   21,   85,  106,    0,  -44,    0,  339,  165,
  -44,  147,  101,  -44,  147,    0,    0,    0,  -44,    0,
 -224, -224,  -44,  -44,    0,  254,    0,    0,  369, -201,
    0,    0,    0,   53,   54,  268,    0,  104,  272,    0,
  274,    0,    0,    0,  300,    0,    0,    0,  269,    0,
   45,    0,  104,  -44,  -37,    0,  158,    0,  121,  -44,
  144, -106,  147,    0,  364,   96,  244,  298,  138,  375,
  147,  305,    0,  313,    0,  139,    0,  139,    0,    0,
    0,    0,    0,  133,   52,  -44,  327,    0,  104,    0,
  259,    0,    0,  147,    0,  -44,  264,  430,  356,    0,
  136,    0,  192,    0,    0,  362, -107,    0,    0,  166,
  383,    0,  384,    0,    0,  -44,  -44,  -44,    0,  222,
    0,  431,  -44,   62,  170,    0,  173,  186,  -41,  577,
    1,  394,  -44,    0,  319,  336,    0,    0,    0,    0,
  194,  419,    0,    0,  418,  151,  340,    0,  192,    0,
    0,  422,  582,    0,    0,    0,    0,    0,    0,  433,
    0,    0,  128,  216,    0,  608,  436,  440,    0,    0,
    0,    0,  439,    0,  359,  447,    0,  454,  192,  -44,
  176,    0,  451,  238,  379,    0,    0,  380,  -44,  329,
  611,  468,    0,    0,    0,  614,    0,    0,  -44,    0,
  617,    0,
};
final static short yyrindex[] = {                         0,
  132,    0,  140,  471,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   13,   50,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  479,    0,  480,    0,    0,    0,    0,    0,    0,
   -2,    0,    0,  484,   30,    0,    0,    0,    0,    0,
    0,   55,    0,   71,    0,    0,    0,    0,  489,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  287,  297,
    0,    0,    0,  304,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,  492,    0,    0,  494,    0,    0,    0,  501,    0,
    0,    0,    0,    0,    0,  533,    0,    0,   74,  132,
    0,    0,    0,    0,  504,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   67,    0,    0,    0,    0,    0,    0,
 -110,    0,   72,    0,    0,    0,    0,  515,    0,    0,
  517,    0,    0,    8,    0,   35,    0,   57,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   80,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  518,
    0,    0,  521,    0,    0,  501,    0,    0,    0,    0,
    0,    0,    0,    0,  174,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  489,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  443,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  309,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  315,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,
};
final static short yygindex[] = {                         0,
  428,  568,   61,   86,  -10,   89,    0,    0,    0,    0,
    5,  -47, -130,    0,    0,   14,  361,    0,  354,    0,
    0,    0,    0,  349,  -16,    0,  282,    0,  410,  466,
    0,    0,  164,   79,  522,  -13,  -70,    0,  469,  509,
    0,    0,  377,  452,    0,  459,  352,  497,    0,   -4,
    0,    0,  400,    0,    0,    0,    0,
};
final static int YYTABLESIZE=688;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         54,
   53,   65,  248,   43,   44,   45,   62,   11,   43,   49,
   46,  137,   95,   47,   95,   92,   12,  227,   44,   13,
   53,  125,  113,   44,   13,  195,   50,  114,  102,   68,
  105,  101,  164,  165,  115,   91,   53,   90,  139,  139,
  139,  139,  139,  140,  139,   65,   58,   53,  142,  142,
  142,  142,  142,   98,  142,   41,   10,  139,   69,  139,
  137,  142,   91,  104,   90,   70,  228,  142,   53,  142,
  132,   71,  132,  132,  132,  130,   61,  130,  130,  130,
   91,   77,   90,  153,  151,  182,   12,  157,  100,  132,
  160,  132,  211,   11,  130,  161,  130,  131,    9,  131,
  131,  131,  244,   64,   91,   63,   90,  115,  169,  170,
  200,   91,  116,   90,   12,   58,  131,   13,  131,  116,
  114,   91,  103,   90,   45,   72,  115,  111,  115,  112,
  184,  116,   99,  116,   65,   73,  188,   10,  151,  114,
  158,  114,   66,   48,   91,  150,   90,  119,   53,  148,
   95,   95,   63,  106,  191,  192,    4,   67,  122,   74,
  150,  159,  212,   91,  148,   90,   13,  270,   67,  196,
  141,  107,  220,   67,   11,   23,  209,  108,  125,    9,
  207,   13,  225,  148,  155,  208,  110,  190,   13,  111,
   23,  112,  161,  241,  109,   12,  117,  186,   13,  243,
  148,  185,  233,  234,   13,  118,  175,   63,   65,  256,
  196,   13,   51,   52,   41,   75,   42,  108,    2,  241,
    1,    3,  241,    2,    5,  121,    3,    4,  149,    5,
   42,    6,   51,   52,  108,   42,   13,    7,    8,    9,
   10,  128,  271,   63,   86,   87,   88,   89,   51,   52,
  269,  132,   13,   13,   65,  210,  281,  214,  280,   51,
   52,  251,  252,   65,  148,  286,  139,  139,  139,  139,
  133,   86,   87,   88,   89,  291,  142,  142,  142,  142,
   51,   52,   13,  134,  198,  257,  111,  156,  112,   86,
   87,   88,   89,  135,  138,  236,  108,  171,  132,  132,
  132,  132,  180,  130,  130,  130,  130,   13,  174,  172,
  173,  176,   13,   86,   87,   88,   89,   58,   78,   13,
   86,   87,   88,   89,   63,  131,  131,  131,  131,   13,
   86,   87,   88,   89,   13,  115,  115,  115,  115,  179,
  116,  116,  116,  116,  217,  205,  235,   77,  114,  114,
  114,  114,  206,   86,   87,   88,   89,   75,   78,    4,
   51,   52,  145,   78,   79,    2,  213,  146,    3,   77,
  147,    5,   86,   87,   88,   89,  130,  145,   13,   13,
    2,   77,  249,    3,  145,  147,    5,    2,  221,   13,
    3,   77,  268,    5,  203,  223,  148,  224,  178,  148,
  145,  226,  148,    2,  148,  148,    3,    1,  189,    5,
    2,   77,   78,    3,    4,  204,    5,  111,    6,  112,
   78,   75,  229,  230,    7,    8,    9,   10,   79,  231,
  108,  245,  145,  108,  246,    2,  108,  255,    3,  108,
  282,    5,  136,   58,  143,  144,   79,  247,  145,    1,
  259,    2,    2,  287,    3,    3,    4,    5,    5,  260,
    6,  261,   78,  154,  263,  265,    7,    8,    9,   10,
  222,  242,  111,  111,  112,  112,  267,  272,    1,  274,
  275,    2,  276,  277,    3,    4,   79,    5,  194,    6,
  278,   79,  143,  279,  283,    7,    8,    9,   10,  284,
   78,  210,   80,   75,  285,   85,    2,  289,   75,    3,
   32,    2,    5,   76,    3,   75,  129,    5,    2,  215,
  216,    3,  127,  129,    5,   75,  143,  128,    2,   35,
   75,    3,    5,    2,    5,  126,    3,  125,  177,    5,
   79,  149,   80,   77,   39,   85,   77,   80,   79,   77,
   85,   77,   77,   75,  199,   41,   75,  151,   45,   75,
   79,   75,   75,   79,   61,  143,   79,   49,   79,   79,
  107,    7,    8,    9,   75,  145,  106,    2,    2,  193,
    3,    3,   59,    5,    5,  145,  258,  264,    2,  262,
   79,    3,  123,  139,    5,  120,   80,  253,  131,   85,
  187,  183,    4,  124,   80,  232,    6,   85,  168,    7,
    8,    9,    7,    8,    9,   10,    0,  250,    0,  111,
  120,  112,  266,    0,  111,  120,  112,    4,   79,    0,
    0,    6,    4,    0,    0,    0,    6,    7,    8,    9,
   10,    0,    7,    8,    9,   10,   80,    0,  273,   85,
  111,  288,  112,  111,  290,  112,  111,  292,  112,  111,
    0,  112,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,   80,    0,    0,   85,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         13,
   45,   18,   44,   46,   61,    1,   17,   44,   46,  123,
  259,   82,  123,  257,  125,   40,  123,  125,   61,   61,
   45,   69,   42,   61,   61,  156,  278,   47,   42,   25,
   44,   40,  257,  258,   44,   60,   45,   62,   41,   42,
   43,   44,   45,   41,   47,   62,  125,   45,   41,   42,
   43,   44,   45,   44,   47,  257,   44,   60,   40,   62,
  131,   41,   60,   40,   62,   44,  197,   60,   45,   62,
   41,   44,   43,   44,   45,   41,   16,   43,   44,   45,
   60,  123,   62,   97,   95,   41,  123,  101,   58,   60,
  104,   62,   41,   44,   60,  109,   62,   41,   44,   43,
   44,   45,   41,   18,   60,   17,   62,   41,  113,  114,
  158,   60,   41,   62,   44,  125,   60,   44,   62,   59,
   41,   60,  257,   62,  120,   44,   60,   43,   62,   45,
  144,   60,  123,   62,  151,   44,  150,  125,  149,   60,
   40,   62,   44,  257,   60,   40,   62,   62,   45,   44,
  261,  262,   64,   44,  261,  262,  264,   59,   44,   44,
   40,   61,  176,   60,   44,   62,   61,   40,   59,  156,
   92,  125,  186,   59,  125,   44,   44,  257,  226,  125,
   42,   61,  193,   44,   99,   47,  258,   44,   61,   43,
   59,   45,  206,  210,   40,  125,   44,   40,  125,  213,
   61,   44,  207,  208,   61,   44,  128,  119,  225,  223,
  197,   61,  257,  258,  257,  257,  273,   44,  260,  236,
  257,  263,  239,  260,  266,  257,  263,  264,  123,  266,
  273,  268,  257,  258,   61,  273,   61,  274,  275,  276,
  277,   40,  253,  155,  269,  270,  271,  272,  257,  258,
  123,   44,   61,   61,  271,  123,  270,  179,  269,  257,
  258,  261,  262,  280,  125,  279,  269,  270,  271,  272,
   44,  269,  270,  271,  272,  289,  269,  270,  271,  272,
  257,  258,   61,   44,   41,  225,   43,  123,   45,  269,
  270,  271,  272,  267,   44,  210,  123,   44,  269,  270,
  271,  272,  139,  269,  270,  271,  272,   61,   41,  257,
  257,   40,   61,  269,  270,  271,  272,  125,   37,   61,
  269,  270,  271,  272,  236,  269,  270,  271,  272,   61,
  269,  270,  271,  272,   61,  269,  270,  271,  272,   40,
  269,  270,  271,  272,  181,   41,  125,   61,  269,  270,
  271,  272,   40,  269,  270,  271,  272,   61,   77,  264,
  257,  258,  257,   82,   61,  260,   40,  262,  263,  123,
  265,  266,  269,  270,  271,  272,  125,  257,   61,   61,
  260,  123,  219,  263,  257,  265,  266,  260,  125,   61,
  263,  123,  265,  266,  257,   40,  257,  262,  125,  260,
  257,   40,  263,  260,  265,  266,  263,  257,  265,  266,
  260,  125,  131,  263,  264,   41,  266,   43,  268,   45,
  139,  125,  257,   41,  274,  275,  276,  277,  125,   46,
  257,  262,  257,  260,  262,  260,  263,   44,  263,  266,
  265,  266,  125,  125,   93,   94,   37,  262,  257,  257,
  257,  260,  260,  125,  263,  263,  264,  266,  266,   41,
  268,   44,  181,  125,  125,   44,  274,  275,  276,  277,
   41,   41,   43,   43,   45,   45,   44,  262,  257,   44,
   41,  260,   44,  125,  263,  264,   77,  266,  125,  268,
   44,   82,  141,   40,   44,  274,  275,  276,  277,  262,
  219,  123,   37,  257,  125,   37,  260,   40,  257,  263,
   40,  260,  266,  267,  263,  257,  265,  266,  260,  261,
  262,  263,   44,   44,  266,  257,  175,   44,  260,   41,
  257,  263,    0,  260,  266,   44,  263,   44,  265,  266,
  131,   41,   77,  257,   41,   77,  260,   82,  139,  263,
   82,  265,  266,  257,  257,   41,  260,   41,   41,  263,
  257,  265,  266,  260,   44,  214,  263,  125,  265,  266,
  262,  274,  275,  276,  257,  257,  262,  260,  260,  152,
  263,  263,   15,  266,  266,  257,  226,  239,  260,  236,
  181,  263,  257,   85,  266,  257,  131,  221,   77,  131,
  149,  143,  264,  268,  139,  206,  268,  139,  112,  274,
  275,  276,  274,  275,  276,  277,   -1,   41,   -1,   43,
  257,   45,   41,   -1,   43,  257,   45,  264,  219,   -1,
   -1,  268,  264,   -1,   -1,   -1,  268,  274,  275,  276,
  277,   -1,  274,  275,  276,  277,  181,   -1,   41,  181,
   43,   41,   45,   43,   41,   45,   43,   41,   45,   43,
   -1,   45,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,  219,   -1,   -1,  219,
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
"list_de_parametros_clase :",
"list_de_parametros_clase : parametro_clase",
"parametro_clase : tipo ID",
"parametro_clase : ID",
"parametro_clase : tipo",
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
"referencia_clase : ID '.' ID '(' list_de_parametros_clase ')'",
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
"impresion : PRINT cadena",
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

//#line 532 "gramatica.y"
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
                  GeneradorCodigo.generarCodigo();
                  FileHelper.writeProgram("nuevoArchivo.asm", GeneradorCodigo.codigo.toString());


}



//#line 872 "Parser.java"
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
case 1:
//#line 29 "gramatica.y"
{metodo.add("ejecución"); agregarListaTercetos(metodo.get(metodo.size()-1), codigoIntermedio);}
break;
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
{cambiarAmbito(":START"); Parser.declarando = false; tipo = TablaTipos.STRING_TYPE; cambiarAmbito("main");}
break;
case 12:
//#line 47 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
break;
case 13:
//#line 48 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "No se esperaban declaraciones luego de la ejecucion");}
break;
case 14:
//#line 53 "gramatica.y"
{Parser.declarando = false;}
break;
case 15:
//#line 54 "gramatica.y"
{Parser.declarando = false;}
break;
case 23:
//#line 70 "gramatica.y"
{					Parser.agregarSimbolo(val_peek(0).sval, "variable ");
								int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
                            	TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");

                                TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);}
break;
case 24:
//#line 78 "gramatica.y"
{				Parser.agregarSimbolo(val_peek(0).sval, "variable ");
                                          int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(2).sval);
                                               TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                               TablaSimbolos.agregarAtributo(ptr_id, "uso", "variable");
                                               TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);}
break;
case 28:
//#line 90 "gramatica.y"
{
											salirAmbito();
											Parser.declarando = true;
											agregarListaTercetos(metodo.get(metodo.size()-1), codigoIntermedio); metodo.remove(metodo.size()-1);}
break;
case 29:
//#line 94 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el cuerpo de la funcion");}
break;
case 30:
//#line 97 "gramatica.y"
{salirAmbito(); agregarListaTercetos(metodo.get(metodo.size()-1), codigoIntermedio); metodo.remove(metodo.size()-1);}
break;
case 31:
//#line 101 "gramatica.y"
{ Parser.agregarSimbolo(val_peek(0).sval, "nombre de metodo ");
						int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
						TablaSimbolos.agregarAtributo(ptr_id, "tipo", "VOID_TYPE");
                    	TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de metodo");
                    	TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);
                    	agregarEstructura(estructuras_sintacticas, "Declaracion de Funcion");
                        cambiarAmbito(val_peek(0).sval);
                        ambitos.put(val_peek(0).sval, ambito);
                        metodo.add(val_peek(0).sval);
                        Parser.id_funcion = val_peek(0).sval;}
break;
case 32:
//#line 111 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera nombre de la funcion");}
break;
case 35:
//#line 119 "gramatica.y"
{Parser.parametro_funcion = "NULL";
					agregarFuncion(id_funcion, TablaTipos.getTipo(parametro_funcion, ambito.toString()));}
break;
case 37:
//#line 125 "gramatica.y"
{Parser.agregarSimbolo(val_peek(0).sval, "parametro ");
					int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
	     	        TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                    TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");
                    TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);
                    Parser.parametro_funcion = val_peek(0).sval;
                    agregarFuncion(id_funcion, TablaTipos.getTipo(parametro_funcion, ambito.toString()));}
break;
case 38:
//#line 132 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el tipo del parametro");}
break;
case 39:
//#line 133 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el nombre del parametro");}
break;
case 40:
//#line 134 "gramatica.y"
{tipo = TablaTipos.CLASS_TYPE; Parser.agregarSimbolo(val_peek(0).sval, "parametro "); int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
                                                                                     TablaSimbolos.agregarAtributo(ptr_id, "tipo", tipo);
                                                                                     TablaSimbolos.agregarAtributo(ptr_id, "uso", "parametro");
                                                                                     TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);
                                                                                     Parser.parametro_funcion = val_peek(0).sval;
                                                                                     agregarFuncion(id_funcion, TablaTipos.getTipo(parametro_funcion, ambito.toString()));}
break;
case 41:
//#line 142 "gramatica.y"
{Parser.parametro_funcion = "NULL";}
break;
case 43:
//#line 147 "gramatica.y"
{Parser.parametro_funcion = val_peek(0).sval;}
break;
case 44:
//#line 148 "gramatica.y"
{Parser.parametro_funcion = val_peek(0).sval;}
break;
case 45:
//#line 149 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera el nombre del parametro");}
break;
case 46:
//#line 153 "gramatica.y"
{tipo = TablaTipos.UINT_TYPE;}
break;
case 47:
//#line 154 "gramatica.y"
{tipo = TablaTipos.SHORT_TYPE;}
break;
case 48:
//#line 155 "gramatica.y"
{tipo = TablaTipos.DOUBLE_TYPE;}
break;
case 61:
//#line 184 "gramatica.y"
{int aux = generarTerceto(val_peek(1).sval,val_peek(4).sval+val_peek(3).sval+val_peek(2).sval,val_peek(0).sval);
if (!Parser.mismoTipo(val_peek(2).sval, val_peek(0).sval, ambito.toString())){
									agregarError(errores_semanticos, Parser.ERROR, "Se requieren operandos del mismo tipo");}}
break;
case 62:
//#line 187 "gramatica.y"
{int aux = generarTerceto(val_peek(3).sval,val_peek(6).sval+val_peek(5).sval+val_peek(4).sval,val_peek(2).sval+val_peek(1).sval+val_peek(0).sval);
									if (!Parser.mismoTipo(val_peek(4).sval, val_peek(0).sval, ambito.toString())){
									agregarError(errores_semanticos, Parser.ERROR, "Se requieren operandos del mismo tipo");}}
break;
case 63:
//#line 190 "gramatica.y"
{int aux = generarTerceto("CALL",val_peek(5).sval+"."+val_peek(3).sval,Parser.parametro_funcion);
								if(Parser.funciones.containsKey(val_peek(3).sval)){
								if (Parser.funciones.get(val_peek(3).sval).equals(TablaTipos.getTipo(Parser.parametro_funcion, ambito.toString())) || !Parser.parametro_funcion.equals("NULL")){
												if (!Parser.mismoTipoFuncion(val_peek(3).sval, Parser.parametro_funcion, ambito.toString())){

													agregarError(errores_semanticos, Parser.ERROR, "El tipo del parametro no corresponde con el de la funcion");
												}
								}
								else{
								agregarError(errores_semanticos, Parser.ERROR, "La cantidad de parametros no corresponde con el de la funcion");
								}
							}else{agregarError(errores_semanticos, Parser.ERROR, "Funcion no declarada");}}
break;
case 64:
//#line 204 "gramatica.y"
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
case 65:
//#line 218 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
break;
case 66:
//#line 221 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("DO","-","-"))+ "]";
         agregarEstructura(estructuras_sintacticas, "Sentencia DO_UNTIL ");}
break;
case 70:
//#line 228 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias entre DO y UNTIL"); }
break;
case 71:
//#line 229 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera un '{' "); }
break;
case 75:
//#line 237 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 77:
//#line 239 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 79:
//#line 241 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ','"); }
break;
case 81:
//#line 246 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
                                                                      yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 82:
//#line 248 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
                                                                                 yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 83:
//#line 250 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 84:
//#line 251 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 85:
//#line 254 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");}
break;
case 86:
//#line 257 "gramatica.y"
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
case 87:
//#line 270 "gramatica.y"
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
case 88:
//#line 281 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 89:
//#line 284 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
																	yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 90:
//#line 286 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia IF");
											   yyval.sval = "[" + Integer.toString(generarTerceto("IfFin","-","-"))+ "]";  }
break;
case 91:
//#line 288 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan llaves "); }
break;
case 92:
//#line 289 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera ELSE"); }
break;
case 93:
//#line 290 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias luego del Then"); }
break;
case 94:
//#line 291 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera bloque de sentencias despues del ELSE"); }
break;
case 95:
//#line 294 "gramatica.y"
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
//#line 305 "gramatica.y"
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
case 97:
//#line 316 "gramatica.y"
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
case 98:
//#line 327 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 99:
//#line 328 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 100:
//#line 329 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias luego de la condicion");}
break;
case 101:
//#line 330 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "No se espera ',' "); }
break;
case 102:
//#line 335 "gramatica.y"
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
case 103:
//#line 347 "gramatica.y"
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
case 104:
//#line 357 "gramatica.y"
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
case 105:
//#line 367 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera la palabra reservada RETURN");}
break;
case 106:
//#line 368 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera expresión de retorno");}
break;
case 107:
//#line 369 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se esperan sentencias dentro del cuerpo del ELSE ");}
break;
case 108:
//#line 372 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("BI","incompleto","-"))+ "]";  }
break;
case 109:
//#line 376 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("BF",val_peek(1).sval,"incompleto"))+ "]";  }
break;
case 110:
//#line 377 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera '(' al principio de la condicion");}
break;
case 111:
//#line 378 "gramatica.y"
{ agregarError(errores_sintacticos, Parser.ERROR, "Se espera una condicion");}
break;
case 113:
//#line 383 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 114:
//#line 386 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,val_peek(2).sval,val_peek(0).sval))+ "]";
if(!mismoTipo(val_peek(2).sval,val_peek(0).sval, ambito.toString())){agregarError(errores_semanticos, Parser.ERROR, "La operación requiere operandos del mismo tipo");}}
break;
case 115:
//#line 389 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion luego del comparador");}
break;
case 116:
//#line 390 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion antes del comparador");}
break;
case 123:
//#line 401 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
									  int aux = generarTerceto(val_peek(3).sval,val_peek(4).sval,val_peek(1).sval);
									  if(!TablaSimbolos.isVariableDeclarada(val_peek(4).sval)){
									  agregarError(errores_semanticos, Parser.ERROR, val_peek(4).sval + " variable no declarada");
									  if(!mismoTipo(val_peek(4).sval,val_peek(2).sval, ambito.toString())){
                                      agregarError(errores_semanticos, Parser.ERROR, "La operación requiere operandos del mismo tipo");
                                      }
							 }
						}
break;
case 124:
//#line 411 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
										int aux = generarTerceto("+",val_peek(4).sval,val_peek(1).sval);
										int aux2 = generarTerceto("=",val_peek(4).sval,"[" + aux +"]");
										if(!TablaSimbolos.isVariableDeclarada(val_peek(4).sval)){
										agregarError(errores_semanticos, Parser.ERROR, val_peek(4).sval + " variable no declarada");
										if(!mismoTipo(val_peek(4).sval,val_peek(2).sval, ambito.toString())){agregarError(errores_semanticos, Parser.ERROR, "La operación requiere operandos del mismo tipo");

}}
			}
break;
case 125:
//#line 420 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								int aux = generarTerceto(val_peek(1).sval,val_peek(2).sval,val_peek(0).sval);
								if(!TablaSimbolos.isVariableDeclarada(val_peek(2).sval)){
                                agregarError(errores_semanticos, Parser.ERROR, val_peek(2).sval + " variable no declarada");}
                                if(!mismoTipo(val_peek(2).sval,val_peek(0).sval, ambito.toString())){
                                agregarError(errores_semanticos, Parser.ERROR, "La operación requiere operandos del mismo tipo");
                                System.out.println(TablaSimbolos.getTipo(val_peek(1).sval));
                                  System.out.println(TablaSimbolos.getTipo(val_peek(3).sval));
}}
break;
case 126:
//#line 428 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de asignacion");
								 int aux = generarTerceto("+",val_peek(2).sval,val_peek(0).sval);
                        		 int aux2 = generarTerceto("=",val_peek(2).sval,"[" + aux +"]");
                        		 if(!TablaSimbolos.isVariableDeclarada(val_peek(2).sval)){
                                 agregarError(errores_semanticos, Parser.ERROR, val_peek(2).sval + " variable no declarada");
                                 if(!mismoTipo(val_peek(2).sval,val_peek(0).sval, ambito.toString())){agregarError(errores_semanticos, Parser.ERROR, "La operación requiere operandos del mismo tipo");
}}
			}
break;
case 127:
//#line 437 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 128:
//#line 438 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un identificador en el lado izquierdo de la asignacion");}
break;
case 129:
//#line 439 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una expresion del lado derecho de la asignacion");}
break;
case 130:
//#line 442 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 131:
//#line 443 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 133:
//#line 447 "gramatica.y"
{ yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 134:
//#line 448 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("/",yyval.sval,val_peek(0).sval))+ "]";}
break;
case 136:
//#line 452 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto(val_peek(1).sval,yyval.sval,val_peek(0).sval))+ "]";}
break;
case 137:
//#line 453 "gramatica.y"
{yyval.sval = "[" + Integer.toString(generarTerceto("/",yyval.sval,val_peek(0).sval))+ "]";}
break;
case 141:
//#line 459 "gramatica.y"
{}
break;
case 144:
//#line 464 "gramatica.y"
{}
break;
case 145:
//#line 467 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo(val_peek(0).sval));
		TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);}
break;
case 146:
//#line 471 "gramatica.y"
{int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
                   		TablaSimbolos.agregarAtributo(ptr_id, "uso", "constante");
                   		TablaSimbolos.agregarAtributo(ptr_id, "tipo", TablaSimbolos.getTipo(val_peek(0).sval));
                   		TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);
                   		String lexema = negarConstante(val_peek(0).sval);}
break;
case 147:
//#line 478 "gramatica.y"
{ String nombre = STRING_CHAR + "cadena" + String.valueOf(contador_cadenas);
                          agregarSimbolo(nombre, "cadena ");
                          int puntero = TablaSimbolos.obtenerSimbolo(nombre);
                          int aux = generarTerceto("PRINT", val_peek(0).sval, "-");
                          agregarEstructura(estructuras_sintacticas, "Comentario");}
break;
case 148:
//#line 483 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera un mensaje luego del PRINT ");}
break;
case 149:
//#line 487 "gramatica.y"
{}
break;
case 150:
//#line 488 "gramatica.y"
{}
break;
case 151:
//#line 491 "gramatica.y"
{}
break;
case 152:
//#line 495 "gramatica.y"
{ salirAmbito();
 						tipo = TablaTipos.CLASS_TYPE;
						int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(3).sval + Parser.ambito.toString());
						TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de clase");
						TablaSimbolos.agregarAtributo(ptr_id, "tipo", "CLASS_TYPE");
						TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);}
break;
case 154:
//#line 503 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una declaracion dentro de las llaves");}
break;
case 155:
//#line 504 "gramatica.y"
{agregarError(errores_sintacticos, Parser.ERROR, "Se espera una ID previo a las llaves");}
break;
case 156:
//#line 507 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Declaracion de Clase");
						cambiarAmbito(val_peek(0).sval);
						Parser.agregarSimbolo(val_peek(0).sval, "nombre de clase ");
                        int ptr_id = TablaSimbolos.obtenerSimbolo(val_peek(0).sval);
                        TablaSimbolos.agregarAtributo(ptr_id, "tipo", "CLASS_TYPE");
                        TablaSimbolos.agregarAtributo(ptr_id, "uso", "nombre de clase");
                        TablaSimbolos.agregarAtributoConAmbito(ptr_id, "ambito", ambito);
                         }
break;
case 157:
//#line 517 "gramatica.y"
{salirAmbito();}
break;
case 158:
//#line 520 "gramatica.y"
{agregarEstructura(estructuras_sintacticas, "Sentencia de IMPL para el void " + val_peek(0).sval);

						 ambito = obtenerAmbito(val_peek(0).sval);
						 cambiarAmbito(val_peek(0).sval);}
break;
//#line 1636 "Parser.java"
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
