/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package projecttest3;

import java.util.Scanner;

/**
 *
 * @author Admin
 */
public class ProjectTest3 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc = new Scanner(System.in);
        Student s = new Student(sc.nextLine(), sc.nextLine(), sc.nextLine(), sc.nextLine());
        s.Output();
        Student s2 = new Student();
        s.Input();
        s.Output();
    }
    
}
