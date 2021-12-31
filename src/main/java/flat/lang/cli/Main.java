package flat.lang.cli;

import flat.lang.content.Key;

import java.util.ArrayList;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        String str = "&&";
        System.out.println(Key.readKey(str, 0, str.length()));
        /*for (int i = 0; i < 65535; i++) {
            for (int j = 0; j < 65535; j++) {
                String s = (char)i + "" + (char)j;
                Integer in = s.hashCode();
                if (s.hashCode() == 46) {
                    System.out.println(i+"-"+j);
                }
            }
        }*/

        System.out.println("\u0000\u002E".hashCode());
        //System.out.println(";{".hashCode());
        //System.out.println("<\\".hashCode());
        System.out.println(".".hashCode()); // 108960
    }
}
