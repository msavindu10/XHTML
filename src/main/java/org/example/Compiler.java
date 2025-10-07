package org.example;

import org.example.model.Node;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compiler {

    private static Compiler instance = null;

    private Compiler(){

    }

    public String compile(String filePath){

        //Read file
        String value;
        try{
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine()) != null){
                stringBuilder.append("\n").append(line);
            }
            bufferedReader.close();
            fileInputStream.close();

            value = stringBuilder.toString();

        }catch(Exception e){
            System.out.println("Error - "+e.getMessage());
            return null;
        }

        List<Node> nodeTree = parse(value);
        String data = prepare(nodeTree);;

        return data;
    }

    public String prepare(List<Node> nodeList){

        String value = "";
        for (Node node : nodeList) {

            String attributesValue = "";

            if(node.attributes != null){
                for (Map.Entry<String, String> entry : node.attributes.entrySet()) {
                    attributesValue = attributesValue + " " + entry.getKey() + "=" + entry.getValue();
                }
            }

            if(!attributesValue.isEmpty()){
                attributesValue = " " + attributesValue.trim();
            }

            if(node.text != null){
                value = value + node.text;
            }else{
                value = value
                        +"<"+node.name+attributesValue+">"
                        +prepare(node.child)
                        +"</"+node.name+">";
            }

        }

        return value;
    }

    public List<Node> parse(String value){

        List<Node> nodeList = new ArrayList<>();
        Map<String,String> attributes = null;

        String text = "";
        boolean isExtractText = true;
        String attributeName = "";
        String attributeValue = "";
        boolean isExtractAttribute = false;
        boolean isExtractAttributeValue = false;
        String childContent = "";
        boolean isExtractChildContent = false;

        int openCount = 0;

        for (int i = 0; i < value.length(); i++) {
            String x = String.valueOf(value.charAt(i));

            //check tag name is end
            if(!isExtractChildContent && x.equals("[")){

                //check have child text
                text = text.trim();
                int tagStartPosition = text.lastIndexOf(" ");
                if(tagStartPosition == -1){
                    tagStartPosition = text.lastIndexOf("\n");
                }

                if(tagStartPosition > -1){
                    String textChildText = text.substring(0,tagStartPosition).trim();
                    text = text.substring(tagStartPosition+1);

                    Node node = new Node();
                    node.name = null;
                    node.attributes = null;
                    node.text = textChildText;
                    nodeList.add(node);

                }

                isExtractText = false;
                isExtractAttribute = true;

                continue;
            }

            if(isExtractAttribute){

                if(attributes == null){
                    attributes = new HashMap<>();
                }

                if(x.equals(",") || x.equals("]")){

                    if(!attributeName.trim().isEmpty()){
                        attributes.put(attributeName.trim(),attributeValue.trim());
                    }

                    attributeName = "";
                    attributeValue = "";

                    if(x.equals(",")){
                        isExtractAttributeValue = false;
                    }

                    if(x.equals("]")){
                        isExtractAttribute = false;
                        isExtractAttributeValue = false;
                    }

                    continue;
                }

                if(x.equals("=")){
                    isExtractAttributeValue = true;
                    continue;
                }

                if(isExtractAttributeValue){
                    attributeValue = attributeValue + x;
                }else{
                    attributeName = attributeName + x;
                }

            }

            if(isExtractText){
                text = text + x;
            }

            if(x.equals("{")){
                openCount++;
                isExtractChildContent = true;

                if(openCount == 1){
                    continue;
                }

            }

            if(x.equals("}")){
                openCount--;

                if(openCount == 0){

                    Node node = new Node();
                    node.name = text.trim();
                    node.attributes = attributes;
                    node.child = parse(childContent);

                    nodeList.add(node);

                    text = "";
                    attributes = null;
                    childContent = "";
                    isExtractChildContent = false;
                    isExtractText = true;

                    continue;
                }

            }

            if(isExtractChildContent){
                childContent = childContent + x;
            }

            //last
            if(i == value.length()-1){
                Node node = new Node();
                node.name = null;
                node.attributes = null;
                node.text = text.trim();
                nodeList.add(node);
            }

        }

        return nodeList;
    }

    public static Compiler getInstance(){
        if(instance == null){
            instance = new Compiler();
        }
        return instance;
    }

}
