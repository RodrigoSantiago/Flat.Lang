package flat.lang.content;

import java.util.HashMap;

/**
 * Key is the container class for all available keywords and special cases
 */
public class Key {
    public static final Key None = new Key("");
    public static final Key Invalid = new Key("");
    public static final Key InvalidOp = new Key("");

    public static final Key Word = new Key("");
    public static final Key String = new Key("");
    public static final Key Number = new Key("");
    public static final Key True = new Key("true");
    public static final Key False = new Key("false");
    public static final Key Null = new Key("null");

    public static final Key Param = new Key("(");
    public static final Key Brace = new Key("{");
    public static final Key Index = new Key("[");
    public static final Key Generic = new Key("<");

    public static final Key CParam = new Key(")");
    public static final Key CBrace = new Key("}");
    public static final Key CIndex = new Key("]");
    public static final Key CGeneric = new Key(">");

    public static final Key Namespace = new Key("namespace");
    public static final Key Using = new Key("using");
    public static final Key Class = new Key("class");
    public static final Key Struct = new Key("struct");
    public static final Key Interface = new Key("interface");
    public static final Key Enum = new Key("enum");

    public static final Key Public = new Key("public");
    public static final Key Private = new Key("private");
    public static final Key Static = new Key("static");
    public static final Key Final = new Key("final");
    public static final Key Abstract = new Key("abstract");
    public static final Key Operator = new Key("operator");

    public static final Key If = new Key("if");
    public static final Key Else = new Key("else");
    public static final Key For = new Key("for");
    public static final Key While = new Key("while");
    public static final Key Do = new Key("do");
    public static final Key Switch = new Key("switch");
    public static final Key Native = new Key("native");

    public static final Key Break = new Key("break");
    public static final Key Case = new Key("case");
    public static final Key Continue = new Key("continue");
    public static final Key Default = new Key("default");
    public static final Key Let = new Key("let");
    public static final Key New = new Key("new");
    public static final Key Return = new Key("return");
    public static final Key Super = new Key("super");
    public static final Key This = new Key("this");
    public static final Key Var = new Key("var");
    public static final Key Void = new Key("void");
    public static final Key Yield = new Key("yield");

    public static final Key Semicolon = new Key(";");
    public static final Key Colon = new Key(":");
    public static final Key Dot = new Key(".");
    public static final Key Comma = new Key(",");
    public static final Key Quest = new Key("?");
    public static final Key Lambda = new Key("->");
    public static final Key Mul = new Key("*");
    public static final Key Div = new Key("/");
    public static final Key Mod = new Key("%");
    public static final Key Add = new Key("+");
    public static final Key Sub = new Key("-");
    public static final Key Inc = new Key("++");
    public static final Key Dec = new Key("--");
    public static final Key RShift = new Key(">>");
    public static final Key LShift = new Key("<<");
    public static final Key More = new Key(">");
    public static final Key Less = new Key("<");
    public static final Key EMore = new Key(">=");
    public static final Key ELess = new Key("<=");
    public static final Key Equal = new Key("==");
    public static final Key Dif = new Key("!=");
    public static final Key BitAnd = new Key("&");
    public static final Key BitXor = new Key("^");
    public static final Key BitOr = new Key("|");
    public static final Key BitNot = new Key("~");
    public static final Key And = new Key("&&");
    public static final Key Or = new Key("||");
    public static final Key Not = new Key("!");
    public static final Key SetVal = new Key("=");
    public static final Key SetAdd = new Key("+=");
    public static final Key SetSub = new Key("-=");
    public static final Key SetMul = new Key("*=");
    public static final Key SetDiv = new Key("/=");
    public static final Key SetMod = new Key("%=");
    public static final Key SetRShift = new Key(">>=");
    public static final Key SetLShift = new Key("<<=");
    public static final Key SetAnd = new Key("&=");
    public static final Key SetOr = new Key("|=");
    public static final Key SetNot = new Key("~=");
    public static final Key SetXor = new Key("^=");

    public static final Key Is = new Key("is");
    public static final Key IsNot = new Key("!is");
    public static final Key Auto = new Key("auto");

    private static final HashMap<Integer, Key> keys = new HashMap<>();
    static {
        keys.put("true".hashCode(), True);
        keys.put("false".hashCode(), False);
        keys.put("null".hashCode(), Null);
        keys.put("(".hashCode(), Param);
        keys.put("{".hashCode(), Brace);
        keys.put("[".hashCode(), Index);
        keys.put("<".hashCode(), Generic);
        keys.put(")".hashCode(), CParam);
        keys.put("}".hashCode(), CBrace);
        keys.put("]".hashCode(), CIndex);
        keys.put(">".hashCode(), CGeneric);
        keys.put("namespace".hashCode(), Namespace);
        keys.put("using".hashCode(), Using);
        keys.put("class".hashCode(), Class);
        keys.put("struct".hashCode(), Struct);
        keys.put("interface".hashCode(), Interface);
        keys.put("enum".hashCode(), Enum);
        keys.put("public".hashCode(), Public);
        keys.put("private".hashCode(), Private);
        keys.put("static".hashCode(), Static);
        keys.put("final".hashCode(), Final);
        keys.put("abstract".hashCode(), Abstract);
        keys.put("operator".hashCode(), Operator);
        keys.put("if".hashCode(), If);
        keys.put("else".hashCode(), Else);
        keys.put("for".hashCode(), For);
        keys.put("while".hashCode(), While);
        keys.put("do".hashCode(), Do);
        keys.put("switch".hashCode(), Switch);
        keys.put("native".hashCode(), Native);
        keys.put("break".hashCode(), Break);
        keys.put("case".hashCode(), Case);
        keys.put("continue".hashCode(), Continue);
        keys.put("default".hashCode(), Default);
        keys.put("let".hashCode(), Let);
        keys.put("new".hashCode(), New);
        keys.put("return".hashCode(), Return);
        keys.put("super".hashCode(), Super);
        keys.put("this".hashCode(), This);
        keys.put("var".hashCode(), Var);
        keys.put("void".hashCode(), Void);
        keys.put("yield".hashCode(), Yield);
        keys.put(";".hashCode(), Semicolon);
        keys.put(":".hashCode(), Colon);
        keys.put(".".hashCode(), Dot);
        keys.put(",".hashCode(), Comma);
        keys.put("?".hashCode(), Quest);
        keys.put("->".hashCode(), Lambda);
        keys.put("*".hashCode(), Mul);
        keys.put("/".hashCode(), Div);
        keys.put("%".hashCode(), Mod);
        keys.put("+".hashCode(), Add);
        keys.put("-".hashCode(), Sub);
        keys.put("++".hashCode(), Inc);
        keys.put("--".hashCode(), Dec);
        keys.put(">>".hashCode(), RShift);
        keys.put("<<".hashCode(), LShift);
        //keys.put(">".hashCode(), More);
        //keys.put("<".hashCode(), Less);
        keys.put(">=".hashCode(), EMore);
        keys.put("<=".hashCode(), ELess);
        keys.put("==".hashCode(), Equal);
        keys.put("!=".hashCode(), Dif);
        keys.put("&".hashCode(), BitAnd);
        keys.put("^".hashCode(), BitXor);
        keys.put("|".hashCode(), BitOr);
        keys.put("~".hashCode(), BitNot);
        keys.put("&&".hashCode(), And);
        keys.put("||".hashCode(), Or);
        keys.put("!".hashCode(), Not);
        keys.put("=".hashCode(), SetVal);
        keys.put("+=".hashCode(), SetAdd);
        keys.put("-=".hashCode(), SetSub);
        keys.put("*=".hashCode(), SetMul);
        keys.put("/=".hashCode(), SetDiv);
        keys.put("%=".hashCode(), SetMod);
        keys.put(">>=".hashCode(), SetRShift);
        keys.put("<<=".hashCode(), SetLShift);
        keys.put("&=".hashCode(), SetAnd);
        keys.put("|=".hashCode(), SetOr);
        keys.put("~=".hashCode(), SetNot);
        keys.put("^=".hashCode(), SetXor);
        keys.put("is".hashCode(), Is);
        keys.put("!is".hashCode(), IsNot);
        keys.put("auto".hashCode(), Auto);
    }

    public final String name;
    private Key(String name) {
        this.name = name;
    }

    /**
     * Try to find a key from a source.
     *
     * @param source String source
     * @param start Start index offset
     * @param end End index offset
     * @return The Key found, or null
     */
    public static Key readKey(String source, int start, int end) {
        int h = 0;
        int length = end - start;
        if (length <= 0 || length > 9) return null;

        for (int i = 0; i < length; i++) {
            h = 31 * h + source.charAt(i);
        }
        Key key = keys.get(h);
        return key != null && isEqual(key, source, start, end) ? key : null;
    }

    /**
     * Total keywords count
     * @return Total keywords count
     */
    public static int keywordsCount() {
        return keys.size();
    }

    private static boolean isEqual(Key key, String source, int start, int end) {
        int len = end - start;
        if (len == key.name.length()) {
            for (int i = 0; i < len; i++) {
                if (source.charAt(start + i) != key.name.charAt(i)) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Key[" + name + "]";
    }
}
