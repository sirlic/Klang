import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;


public class KLangInterpreter extends KlangBaseListener {
    private final String TAG = "KLangInterpreter";
    private Stack<KValue> mStack = new Stack<>();


    private IFunCaller mFunCaller;
    private VarResolver mVarResolver;

    public KLangInterpreter(IFunCaller caller,VarResolver varResolver) {
        mFunCaller = caller;
        mVarResolver = varResolver;
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
        mVarResolver.setValMap(key.getText(),value);
        Log.w(TAG,"exitAssignmentStatm "+key.getText()+"-"+value);
    }

    @Override
    public void exitAdd(KlangParser.AddContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();

        mStack.add(binaryOperatore("+",v2, v1));
        Log.w(TAG,"exitAdd " + ctx.getText());
    }

    private KValue binaryOperatore(String op,KValue left, KValue right) {
        if (right.isNumber() && left.isNumber()) {
            Double ret = op(op,right.asDouble() ,left.asDouble());
            return new KValue(ret);
        }
        if (right.isList() && left.isList()) {
            ArrayList<KValue> ret = new ArrayList<>();
            List<KValue> v1List = right.asList();
            List<KValue> v2List = left.asList();
            for (int i=0;i<v1List.size();i++) {
                ret.add(new KValue(op(op,v1List.get(i).asDouble() , v2List.get(i).asDouble())));
            }
            return  new KValue(ret);
        }
        Double d;
        List<KValue> list;
        if (right.isList()) {
            list = right.asList();
            d = left.asDouble();
        } else {
            d = right.asDouble();
            list = left.asList();
        }
        ArrayList<KValue> ret = new ArrayList<>();
        for (int i=0;i<list.size();i++) {
            ret.add(new KValue(op(op,list.get(i).asDouble() , d)));
        }
        return new KValue(ret);
    }

    private Double op(String op,Double left,Double right) {
        Double ret = null;
        switch (op) {
            case "+":
                ret = left + right;
                break;
            case "-":
                ret = left - right;
                break;
            case "*":
                ret = left * right;
                break;
            case "/":
                ret = left / right;
                break;
            default:
                break;
        }
        return ret;
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
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        if (!v1.isBoolean() || !v2.isBoolean()) {
            throwErr("");
        }
        boolean ret = v1.asBoolean() ^ v2.asBoolean();
        mStack.add(new KValue(ret));
    }

    @Override
    public void exitDiv(KlangParser.DivContext ctx) {
        KValue v2 = mStack.pop();
        KValue v1 = mStack.pop();
        mStack.add(binaryOperatore("/",v2, v1));
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
                KValue kValue = mVarResolver.resolve(value.asString());
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
        mStack.add(binaryOperatore("*",v2, v1));
    }

    @Override
    public void exitNegative(KlangParser.NegativeContext ctx) {
        KValue v = mStack.pop();
        if (!v.isNumber()) {
            throwErr("");
        }
        mStack.add(binaryOperatore("-",new KValue(0), v));
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
        KValue kValue = mVarResolver.resolve(value.asString());
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
        mStack.add(binaryOperatore("-",v2, v1));
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
