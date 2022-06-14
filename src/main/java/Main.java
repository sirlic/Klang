import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) {
        String len = "a:=0;IF(1,2,3)+If(if(1,2,3),2,3)*2;IF(1,2,3)+If(if(1,2,3),2,3)*2;";
        ANTLRInputStream input = new ANTLRInputStream(len);
        KlangLexer lexer = new KlangLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KlangParser parser = new KlangParser(tokens);
        KlangParser.ProgContext prog = parser.prog();
        KLangInterpreter kLangInterpreter = new KLangInterpreter() {
            @Override
            public void exitEveryRule(ParserRuleContext ctx) {
                super.exitEveryRule(ctx);

                System.out.println("exitEveryRule:"+ctx.getText()+parser.getRuleNames()[ctx.getRuleIndex()]);
            }
        };
        ParseTreeWalker.DEFAULT.walk(kLangInterpreter, prog);  //规则树遍历
        kLangInterpreter.value();
    }
}
