/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package projecttest3;

import java.util.Scanner;

/**
 *
 * @author Admin
 */
public class Student {
    private String code;
    private String name;
    private String birthYear;
    private String address;

    public void Input(){
        Scanner sc = new Scanner(System.in);
        code = sc.nextLine();
        name = sc.nextLine();
        birthYear = sc.nextLine();
        address = sc.nextLine();
    }
    public void Output(){
        System.out.println("code :" + code + "\n"
                + "name: " + name + "\n"
                + "birthYear: " + birthYear + "\n"
                + "address: " + address    
                );
    }
    public Student(String code, String name, String birthYear, String address){
        this.code = code;
        this.name = name;
        this.birthYear = birthYear;
        this.address = address;
    }
}
