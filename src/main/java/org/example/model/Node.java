package org.example.model;

import org.example.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Node{

    public String name;
    public Map<String,String> attributes;
    public List<Node> child = new ArrayList<>();
    public String text = null;

    public Node() {
    }

}