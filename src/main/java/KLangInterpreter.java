import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;


public class KLangInterpreter extends KlangBaseListener {
    private final String TAG = "KLangInterpreter";
    private Map<String,KValue> mValMap = new HashMap<>();
    private Stack<KValue> mStack = new Stack<>();

    public void setValMap(String val,KValue value) {
        mValMap.put(val,value);
    }

    public KValue getValue(String val) {
        return mValMap.get(val);
    }

    private IFunCaller mFunCaller;

    public KLangInterpreter(IFunCaller caller) {
        mFunCaller = caller;
    }
    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        super.exitEveryRule(ctx);

    }

    @Override
    public void exitProg(KlangParser.ProgContext ctx) {
        super.exitProg(ctx);
    }

    @Override
    public void exitStat(KlangParser.StatContext ctx) {
        super.exitStat(ctx);
        if (ctx.expr() != null) {
            KValue value = mStack.pop();
            Log.w(TAG,"exitStat "+ctx.getText() +"--"+value);
        }
    }

    @Override
    public void exitAssignmentStatm(KlangParser.AssignmentStatmContext ctx) {
        KValue value = mStack.pop();
        TerminalNode key = ctx.ID(0);
        mValMap.put(key.getText(),value);
        Log.w(TAG,"exitAssignmentStatm "+key.getText()+"-"+value);
    }

    @Override
    public void exitAdd(KlangParser.AddContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isNumber() || !v2.isNumber()) {
            throwErr("");
        }
        Double ret = v1.asDouble() + v2.asDouble();
        mStack.add(new KValue(ret));
        Log.w(TAG,"exitAdd " + ctx.getText());
    }

    @Override
    public void exitAnd(KlangParser.AndContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isBoolean() || !v2.isBoolean()) {
            throwErr("");
        }
        mStack.add(new KValue(v1.asBoolean() && v2.asBoolean()));
    }

    @Override
    public void exitBang(KlangParser.BangContext ctx) {
        super.exitBang(ctx);
        KValue value = mStack.pop();
        if (!value.isBoolean()) {
            throwErr("");
        }
        mStack.add(new KValue(!value.asBoolean()));
    }

    @Override
    public void exitCaret(KlangParser.CaretContext ctx) {
        super.exitCaret(ctx);
    }

    @Override
    public void exitDiv(KlangParser.DivContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isNumber() || !v2.isNumber()) {
            throwErr("");
        }
        Double ret = v1.asDouble() / v2.asDouble();
        mStack.add(new KValue(ret));
    }

    @Override
    public void exitEqual(KlangParser.EqualContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isNumber() || !v2.isNumber()) {
            throwErr("");
        }
        boolean ret = v1.asDouble() == v2.asDouble();
        mStack.add(new KValue(ret));
    }

    @Override
    public void exitExprList(KlangParser.ExprListContext ctx) {

    }

    @Override
    public void exitFun(KlangParser.FunContext ctx) {
        TerminalNode funName = ctx.ID();
        KlangParser.ExprListContext exprListContext = ctx.exprList();
        List<KlangParser.ExprContext> expr = exprListContext.expr();
        Stack<KValue> values = new Stack<>();
        for (int i = 0; i < expr.size(); i++) {
            KValue value = mStack.pop();
            if (value.isString()) {
                KValue kValue = mValMap.get(value.asString());
                if (kValue != null) {
                    value = kValue;
                }
            }
            values.add(value);
        }
        if (mFunCaller != null) {
            KValue value = mFunCaller.invoke(funName.getText(), values);
            mStack.add(value);
        }
        Log.w(TAG,"exitFun:"+ctx.getText()+"----");
    }

    @Override
    public void exitGe(KlangParser.GeContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isNumber() || !v2.isNumber()) {
            throwErr("");
        }
        boolean ret = v1.asDouble() >= v2.asDouble();
        mStack.add(new KValue(ret));
    }

    @Override
    public void exitGt(KlangParser.GtContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isNumber() || v2.isNumber()) {
            throwErr("");
        }
        boolean ret = v1.asDouble() > v2.asDouble();
        mStack.add(new KValue( ret));
    }

    @Override
    public void exitLe(KlangParser.LeContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isNumber() || !v2.isNumber()) {
            throwErr("");
        }
        boolean ret = v1.asDouble() <= (Double) v2.asDouble();
        mStack.add(new KValue( ret));
    }

    @Override
    public void exitLt(KlangParser.LtContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isNumber() || !v2.isNumber()) {
            throwErr("");
        }
        boolean ret = v1.asDouble() < v2.asDouble();
        mStack.add(new KValue(ret));
    }

    @Override
    public void exitMul(KlangParser.MulContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isNumber() || !v2.isNumber()) {
            throwErr("");
        }
        Double ret = v1.asDouble() * v2.asDouble();
        mStack.add(new KValue(ret));
    }

    @Override
    public void exitNegative(KlangParser.NegativeContext ctx) {
        KValue v = mStack.pop();
        if (!v.isNumber()) {
            throwErr("");
        }
        Double ret = 0 -v.asDouble();
        mStack.add(new KValue(ret));
    }

    @Override
    public void exitNotequal(KlangParser.NotequalContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isNumber() || !v2.isNumber()) {
            throwErr("");
        }
        boolean ret = v1.asDouble() != v2.asDouble();
        mStack.add(new KValue(ret));
    }

    @Override
    public void exitVariable(KlangParser.VariableContext ctx) {
        KValue value = new KValue(String.valueOf(ctx.getText()));
        KValue kValue = mValMap.get(value.asString());
        if (kValue != null) {
            value = kValue;
        }
        mStack.add(value);
        Log.w(TAG,"exitVariable "+ctx.getText());
    }

    @Override
    public void exitString(KlangParser.StringContext ctx) {
        mStack.add(new KValue(ctx.getText()));
        Log.w(TAG,"exitString "+ctx.getText());
    }

    //    @Override
//    public void exitBool(KlangParser.BoolContext ctx) {
//        mStack.add(new KValue(Boolean.valueOf(ctx.getText())));
//        Log.w(TAG,"exitBool");
//    }

    @Override
    public void exitNumber(KlangParser.NumberContext ctx) {
        mStack.add(new KValue(Double.valueOf(ctx.getText())));
    }

    @Override
    public void exitOr(KlangParser.OrContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();

        mStack.add(new KValue(v1.asBoolean() || v2.asBoolean()));
    }

    @Override
    public void exitSkip(KlangParser.SkipContext ctx) {

    }

    @Override
    public void exitSub(KlangParser.SubContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isNumber() || !v2.isNumber()) {
            throwErr("");
        }
        Double ret = v1.asDouble() - v2.asDouble();
        mStack.add(new KValue(ret));
    }

    public void value() {
        for (int i = 0; i < mStack.size(); i++) {
            KValue value = mStack.pop();
            System.out.println(value.toString());
            System.out.println(mStack.size());
        }
    }


    private void throwErr(String s) {

    }
}
