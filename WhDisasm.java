import java.io.*;
import java.util.*;

public class WhDisasm {

    private static final Node parseTree;

    private static final Reader stdin = new InputStreamReader(System.in);

    private static Node current;

    public static void main(String[] args) {
        current = parseTree;
        Command test = new Command("blub");
        while(true) current.process();
    }

    private static char read() {
        int i=0;
        char c;

        do {
            // read next input character
            try {
                i = stdin.read();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-42);
            }

            // end of stream?
            if (i==-1) {
                System.exit(0);
            }

            c = (char)i;
        } while(c!=' ' && c!='\n' && c!='\t');

        return c;
    }

    static {
        // init parse tree

        // stack manipulation 
        Command push =      new Command("push       ", true);
        Command duplicate = new Command("duplicate");
        Command copyn =     new Command("copyn      ", true);
        Command swap =      new Command("swap");
        Command discard =   new Command("discard");
        Command sliden =    new Command("sliden     ", true);

        // arithmetic
        Command add = new Command("add");
        Command sub = new Command("sub");
        Command mult = new Command("mult");
        Command div = new Command("div");
        Command mod = new Command("mod");

        // heap access
        Command store =     new Command("store");
        Command retrieve =  new Command("retrieve");

        // flow control
        Command mark =      new Command("mark       ", true);
        Command call =      new Command("call       ", true);
        Command jump =      new Command("jump       ", true);
        Command branchz =   new Command("branchz    ", true);
        Command branchltz = new Command("branchltz  ", true);
        Command ret = new Command("return");
        Command end = new Command("end");

        // I/O
        Command printchar = new Command("printchar");
        Command printnum = new Command("printnum");
        Command readchar = new Command("readchar");
        Command readnum = new Command("readnum");

        // the tree
        // stack manipulation
        HashMap<Character, Node> stackLfMap = new HashMap<Character, Node>();
        stackLfMap.put(' ', duplicate);
        stackLfMap.put('\t', swap);
        stackLfMap.put('\n', discard);
        Part stackLf = new Part(stackLfMap);

        HashMap<Character, Node> stackTabMap = new HashMap<Character, Node>();
        stackTabMap.put(' ', copyn);
        stackTabMap.put('\n', sliden);
        Part stackTab = new Part(stackTabMap);

        HashMap<Character, Node> stackMap = new HashMap<Character, Node>();
        stackMap.put(' ', push);
        stackMap.put('\n', stackLf);
        stackMap.put('\t', stackTab);
        Part stack = new Part(stackMap);

        // arithmetic
        HashMap<Character, Node> arithmeticSpaceMap = new HashMap<Character, Node>();
        arithmeticSpaceMap.put(' ', add);
        arithmeticSpaceMap.put('\t', sub);
        arithmeticSpaceMap.put('\n', mult);
        Part arithmeticSpace = new Part(arithmeticSpaceMap);

        HashMap<Character, Node> arithmeticTabMap = new HashMap<Character, Node>();
        arithmeticTabMap.put(' ', div);
        arithmeticTabMap.put('\t', mod);
        Part arithmeticTab = new Part(arithmeticTabMap);

        HashMap<Character, Node> arithmeticMap = new HashMap<Character, Node>();
        arithmeticMap.put(' ', arithmeticSpace);
        arithmeticMap.put('\t', arithmeticTab);
        Part arithmetic = new Part(arithmeticMap);

        // heap access
        HashMap<Character, Node> heapMap = new HashMap<Character, Node>();
        heapMap.put(' ', store);
        heapMap.put('\t', retrieve);
        Part heap = new Part(heapMap);

        // flow control
        HashMap<Character, Node> flowSpaceMap = new HashMap<Character, Node>();
        flowSpaceMap.put(' ', mark);
        flowSpaceMap.put('\t', call);
        flowSpaceMap.put('\n', jump);
        Part flowSpace = new Part(flowSpaceMap);

        HashMap<Character, Node> flowTabMap = new HashMap<Character, Node>();
        flowTabMap.put(' ', branchz);
        flowTabMap.put('\t', branchltz);
        flowTabMap.put('\n', ret);
        Part flowTab = new Part(flowTabMap);

        HashMap<Character, Node> flowLfMap = new HashMap<Character, Node>();
        flowLfMap.put('\n', end);
        Part flowLf = new Part(flowLfMap);

        HashMap<Character, Node> flowMap = new HashMap<Character, Node>();
        flowMap.put(' ', flowSpace);
        flowMap.put('\t', flowTab);
        flowMap.put('\n', flowLf);
        Part flow = new Part(flowMap);

        // I/O
        HashMap<Character, Node> ioSpaceMap = new HashMap<Character, Node>();
        ioSpaceMap.put(' ', printchar);
        ioSpaceMap.put('\t', printnum);
        Part ioSpace = new Part(ioSpaceMap);

        HashMap<Character, Node> ioTabMap = new HashMap<Character, Node>();
        ioTabMap.put(' ', readchar);
        ioTabMap.put('\t', readnum);
        Part ioTab = new Part(ioTabMap);

        HashMap<Character, Node> ioMap = new HashMap<Character, Node>();
        ioMap.put(' ', ioSpace);
        ioMap.put('\t', ioTab);
        Part io = new Part(ioMap);

        // the tree root
        HashMap<Character, Node> rootTabMap = new HashMap<Character, Node>();
        rootTabMap.put(' ', arithmetic);
        rootTabMap.put('\t', heap);
        rootTabMap.put('\n', io);
        Part rootTab = new Part(rootTabMap);

        HashMap<Character, Node> rootMap = new HashMap<Character, Node>();
        rootMap.put(' ', stack);
        rootMap.put('\t', rootTab);
        rootMap.put('\n', flow);
        parseTree = new Part(rootMap);
    }

    private static void log(String msg) {
        System.err.println(msg);
    }

    abstract static class Node {
        abstract void process();
    }

    static class Part extends Node {

        private HashMap<Character, Node> children;

        Part(HashMap<Character, Node> children) {
            // TODO: this should be a copy
            this.children = children;
        }

        void process() {
            current = children.get(read());
        }
    }

    static class Command extends Node {

        private String name;
        private boolean param;

        Command(String name) {
            this(name, false);
        }

        Command(String name, boolean param) {
            this.name = name;
            this.param = param;
        }

        void process() {

            System.out.print(this.name + " ");

            char c;

            if (this.param) {
                c = read();
                while(c != '\n') {
                    System.out.print((c==' ') ? 0 : 1);
                    c = read();
                }
            }

            System.out.println();
            current = parseTree;
        }

    }

}


