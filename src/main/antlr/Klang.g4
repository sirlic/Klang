grammar Klang;

prog: stat*;
stat
    : assignmentStatm SEMI
    | expr SEMI
    ;
expr
    : LPAREN expr RPAREN #skip
    | ID LPAREN exprList? RPAREN #fun
    | expr LBRACK expr RBRACK #skip
    | SUB expr #negative
    | BANG expr #bang
    | <assoc=right> expr CARET expr#caret
    | expr (MUL) expr #mul
    | expr (DIV) expr #div
    | expr (ADD) expr #add
    | expr (SUB) expr #sub
    | expr (LT) expr #lt
    | expr (LE) expr #le
    | expr (GT) expr #gt
    | expr (GE) expr #ge
    | expr (ASSIGN) expr #equal
    | expr (NOTEQUAL) expr #notequal
    | expr (AND) expr #and
    | expr (OR) expr #or
    | SCIENTIFIC_NUMBER #number
    | Bool #bool
    | ID #id
    ;

AssignmentOperator
	:	':'
	|	':='
	;
assignmentStatm
    : ID AssignmentOperator expr (COMMA ID)*
    ;

exprList
    : expr (COMMA expr)*
    ;

ID  :   LETTER (LETTER | [0-9])* ;

fragment
LETTER : [a-zA-Z_] ;

ASSIGN : '=';
GT : '>';
LT : '<';
BANG : '!';
TILDE : '~';
COLON : ':';
EQUAL : '==';
LE : '<=';
GE : '>=';
NOTEQUAL : '!=';
AND : '&&';
OR : '||';
ADD : '+';
SUB : '-';
MUL : '*';
DIV : '/';
CARET : '^';

LPAREN : '(';
RPAREN : ')';
LBRACE : '{';
RBRACE : '}';
LBRACK : '[';
RBRACK : ']';
SEMI : ';';
COMMA : ',';
DOT : '.';

fragment Bool
 : 'true'
 | 'false'
 ;

SCIENTIFIC_NUMBER
   : NUMBER ((E1 | E2) SIGN? NUMBER)?
   ;


fragment NUMBER
   : ('0' .. '9') + ('.' ('0' .. '9') +)?
   ;
fragment E1
   : 'E'
   ;


fragment E2
   : 'e'
   ;
fragment SIGN
   : (ADD | SUB)
   ;
WS: [ \t\r\n]* -> skip;
COMMENT
    :   LBRACE .*? RBRACE -> skip
    ;