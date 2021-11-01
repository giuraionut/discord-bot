package com.discordbot.babybot.math;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

public class Node {
    private String head;
    private List<Node> childrens;

    public Node(String operation) {
        this.head = operation;
        this.childrens = new ArrayList<>();
    }

    public Node() {
        this.childrens = new ArrayList<>();
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void addChildren(Node child) {
        childrens.add(child);
    }

    public List<Node> getChildrens() {
        return childrens;
    }

    public static Node solve(Node node) {
        if (isOperation(node) && canSolve(node)) { // if is an operation and childrens are just numbers
            return operate(node); //operate the current node and replace it with the solution
        }
        if (isLeaf(node)) { //if the current node is a leaf, we move on
            return node;
        }
        node.getChildrens().forEach(c -> c = solve(c));
        return solve(node);
    }

    public static Node parse(String string) {
        Node tree = new Node();
        List<String> operations = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "^", "sqrt")); //operations
        if (operations.stream().anyMatch(string::contains)) { // if we have an operation means we have to parse the string
            // check to see what operation do we have in the current string, also we have to respect the order of operations.
            String operation = operations.stream().filter(string::contains).findFirst().get(); //get first match to respect the order of operations
            tree.setHead(operation); // set the operation as the head
            // split the current string by the found operation
            if (operation.equals("^"))
                operation = "\\^";
            final List<String> strings = Arrays.asList(string.split("[" + operation + "]")); //at this point we are done with one operation
//            strings.forEach(s -> tree.addChildren(parse(s))); // parse each substring again, these substrings can't contain the operation that already occurred
            for (String s : strings) {
                if (!s.isEmpty())
                    tree.addChildren(parse(s));
            }
            return tree;
        }
        tree.setHead(string); // if we don't have any operation, we got a number, so we return it
        return tree;
    }

    private static boolean canSolve(Node node) {
        return node.getChildrens().stream().noneMatch(Node::isOperation);
    }

    private static boolean isLeaf(Node node) {
        return node.getChildrens().size() == 0;
    }

    private static boolean isOperation(Node node) {
        String operations = "+-^/*sqrt";
        return operations.contains(node.getHead());
    }

    public static Node operate(Node node) {
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        final List<String> childrensHead = node.getChildrens().stream().map(Node::getHead).collect(Collectors.toList());
        List<Double> values = new ArrayList<>();
        for (String head : childrensHead) {
            try {
                final double e = format.parse(head).doubleValue();
                values.add(e);
            } catch (ParseException ex) {
                values.add(0.0);
            }
        }
        node.childrens = new ArrayList<>();
        switch (node.getHead()) {
            case "sqrt" -> {
                node.setHead(String.valueOf(Math.sqrt(values.get(0))));
                return node;
            }
            case "^" -> {
                double result = values.get(0);
                values.remove(0);
                for (Double i : values) {
                    result = Math.pow(result, i);
                }
                node.setHead(String.valueOf(result));
                return node;
            }
            case "/" -> {
                double result = values.get(0);
                values.remove(0);
                for (Double i : values) {
                    result = result / i;
                }
                node.setHead(String.valueOf(result));
                return node;
            }
            case "*" -> {
                final double result = values.stream().reduce(1.0, (a, b) -> a * b);
                node.setHead(String.valueOf(result));
                return node;
            }
            case "-" -> {
                double result = values.get(0);
                final int noOfValues = values.size();
                values.remove(0);
                for (Double i : values) {
                    result = result - i;
                }
                if (noOfValues == 1) {
                    result = -result;
                }
                node.setHead(String.valueOf(result));
                return node;
            }
            case "+" -> {
                final double result = values.stream().reduce(0.0, Double::sum);
                node.setHead(String.valueOf(result));
                return node;
            }
            default -> {
                node.setHead(String.valueOf(0.0));
                return new Node(String.valueOf(0.0));
            }
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        print(buffer, "", "");
        return buffer.toString();
    }

    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(head);
        buffer.append('\n');
        for (Iterator<Node> it = childrens.iterator(); it.hasNext(); ) {
            Node next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
}
