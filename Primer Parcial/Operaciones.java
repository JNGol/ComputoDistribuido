/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package calc;

/**
 *
 * @author juani
 */
import java.lang.Math.*;
public class Operaciones {
    double num1;
    float num;
    float num2;
    float num3;
    float resultado;
    
    Operaciones(String valor1, String valor2){
        num2 = Float.parseFloat(valor1);
        num3 = Float.parseFloat(valor2);
    }
    
    float suma(){
        num=num2+num3;
        return num;
    }
    float resta(){
        num=num2-num3;
        return num;
    }
    float multiplicacion(){
        num=num2*num3;
        return num;
    }
    float division(){
        num=num2/num3;
        return num;
    }
    
}
