package org.example;

import org.example.model.Node;

import java.io.*;

public class Main {

    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("Please parse source file.");
            return;
        }

        String path = args[0];
        Compiler compiler = Compiler.getInstance();

        //Write result
        File file = new File(path);
        File outFile = new File(file.getPath(),file.getName()+".html");
        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outFile));
            outputStream.write(compiler.compile(path).getBytes());
            outputStream.close();

            System.out.println("Your file compiled successfully.");
        } catch (IOException e) {
            System.out.println("Failed to compile.");
        }

    }

}