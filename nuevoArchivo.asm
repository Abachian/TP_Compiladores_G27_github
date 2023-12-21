.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
@aux2bytes dw ? 
@ERROR_OVERFLOW_SUMA_ENTEROS db "ERROR: Overflow en operacion de suma de enteros", 0
@ERROR_OVERFLOW_PRODUCTO db "ERROR: Resultado negativo en resta de enteros sin signo", 0
@ERROR_OVERFLOW_PRODUCTO db "ERROR: Overflow en operacion de producto", 0
_b dd ? 
_z dd ? 
_vv dd ? 
_l dd ? 
@2_ui dd 2_ui
@10_ui dd 10_ui
@8_ui dd 8_ui
@3_s dd 3_s
@25_ui dd 25_ui
.code
JE L2
 L-
 L2
invoke ExitProcess, 0
end START