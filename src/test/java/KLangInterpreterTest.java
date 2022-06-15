import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.BitSet;

public class KLangInterpreterTest {

    public void testVar() {
        String len = "a:=true;IF(1,2,3)+If(if(1,2,3),2,3)*2;IF(1,2,3)+If(if(1,2,3),2,3)*2;";
        test(len);
    }
    public void test(String str) {

        ANTLRInputStream input = new ANTLRInputStream(str);
        KlangLexer lexer = new KlangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KlangParser parser = new KlangParser(tokens);
        KlangParser.ProgContext prog = parser.prog();
        KLangInterpreter kLangInterpreter = new KLangInterpreter(new TFunCaller());
        ParseTreeWalker.DEFAULT.walk(kLangInterpreter, prog);  //规则树遍历
        kLangInterpreter.value();
    }
}
