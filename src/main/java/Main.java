import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.*;

import java.util.BitSet;

public class Main {
    public static void main(String[] args) {
        String len = "a:=1;IaaF(a,2,3)+If(if(1,2,3),2,3)*a;IF(1,2,3)+If(if(1,2,3),2,3)*2;";
        ANTLRInputStream input = new ANTLRInputStream(len);
        KlangLexer lexer = new KlangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KlangParser parser = new KlangParser(tokens);
        parser.addErrorListener(new ANTLRErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
                System.out.println("syntaxError");
            }

            @Override
            public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
                System.out.println("reportAmbiguity");
            }

            @Override
            public void reportAttemptingFullContext(Parser recognizer, DFA dfa, int startIndex, int stopIndex, BitSet conflictingAlts, ATNConfigSet configs) {
                System.out.println("reportAttemptingFullContext");
            }

            @Override
            public void reportContextSensitivity(Parser recognizer, DFA dfa, int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
                System.out.println("reportContextSensitivity");
            }
        });

        KlangParser.ProgContext prog = parser.prog();
        KLangInterpreter kLangInterpreter = new KLangInterpreter(new TFunCaller()) {
            @Override
            public void exitEveryRule(ParserRuleContext ctx) {
                super.exitEveryRule(ctx);

//                System.out.println("exitEveryRule:"+ctx.getText()+parser.getRuleNames()[ctx.getRuleIndex()]);
            }
        };
        ParseTreeWalker.DEFAULT.walk(kLangInterpreter, prog);  //规则树遍历
        kLangInterpreter.value();
    }
}
