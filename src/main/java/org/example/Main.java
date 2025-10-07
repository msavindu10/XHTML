package org.example;

import org.example.model.Node;

import java.io.*;

public class Main {

    public static void main(String[] args) {

        String path = "C:\\Users\\Savindu\\Desktop\\Compiler\\TestProject\\main.txt";//Replace args[0] for your source file location
        Compiler compiler = Compiler.getInstance();

        //Write result
        File file = new File(path);
        File outFile = new File(file.getParent(),file.getName()+".html");

        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outFile));
            outputStream.write(compiler.compile(path).getBytes());
            outputStream.close();

            System.out.println("Your file compiled successfully.");
        } catch (IOException e) {
            System.out.println("Error - "+e.getMessage());
            System.out.println("Failed to compile.");
        }

    }

}