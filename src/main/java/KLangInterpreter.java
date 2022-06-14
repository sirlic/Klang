import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;


public class KLangInterpreter extends KlangBaseListener {
    private Map<String,Value> mValMap = new HashMap<>();
    private Stack<Value> mStack = new Stack<>();

    public void setValMap(String val,Value value) {
        mValMap.put(val,value);
    }

    public Value getValue(String val) {
        return mValMap.get(val);
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

        }
    }

    @Override
    public void exitAssignmentStatm(KlangParser.AssignmentStatmContext ctx) {
        List<TerminalNode> id = ctx.ID();
        TerminalNode operator = ctx.AssignmentOperator();
    }

    @Override
    public void exitAdd(KlangParser.AddContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type != Value.NUMBER || v2.type != Value.NUMBER) {
            throwErr("");
        }
        Double ret = ((Double)v1.v) + ((Double)v2.v);
        System.out.println(ret+"ad");
        mStack.add(new Value(Value.NUMBER,ret));
    }

    @Override
    public void exitAnd(KlangParser.AndContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type == Value.STRING || v2.type == Value.STRING) {
            throwErr("");
        }
        mStack.add(new Value(Value.BOOLEAN,(getBoolean(v1) && getBoolean(v2))));
    }

    private boolean getBoolean(Value value) {
        boolean ret = true;
        if (value.type == Value.NUMBER) {
            ret = (Double) value.v > 0 ? true : false;
        } else if (value.type == Value.BOOLEAN) {
            ret = (boolean) value.v;
        }
        return ret;
    }

    @Override
    public void exitBang(KlangParser.BangContext ctx) {
        super.exitBang(ctx);
    }

    @Override
    public void exitCaret(KlangParser.CaretContext ctx) {
        super.exitCaret(ctx);
    }

    @Override
    public void exitDiv(KlangParser.DivContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type != Value.NUMBER || v2.type != Value.NUMBER) {
            throwErr("");
        }
        Double ret = ((Double)v1.v) / ((Double)v2.v);
        mStack.add(new Value(Value.NUMBER,ret));
    }

    @Override
    public void exitEqual(KlangParser.EqualContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type != Value.NUMBER || v2.type != Value.NUMBER) {
            throwErr("");
        }
        boolean ret = (Double)v1.v == (Double) v2.v;
        mStack.add(new Value(Value.BOOLEAN, ret));
    }

    @Override
    public void exitExprList(KlangParser.ExprListContext ctx) {

    }

    @Override
    public void exitFun(KlangParser.FunContext ctx) {
        TerminalNode funName = ctx.ID();
        KlangParser.ExprListContext exprListContext = ctx.exprList();
        List<KlangParser.ExprContext> expr = exprListContext.expr();
        Stack<Value> values = new Stack<>();
        for (int i = 0; i < expr.size(); i++) {
            values.add(mStack.pop());
        }
        if ("if".equals(funName.getText().toLowerCase())) {
            funIf(values.pop(),values.pop(),values.pop());
        }
        System.out.println("exitEveryRule:"+ctx.getText()+"----");
    }

    private void funIf(Value pop, Value pop1, Value pop2) {
        mStack.add(new Value(Value.NUMBER, 1d));

        System.out.println("fun"+"ad");
    }

    @Override
    public void exitGe(KlangParser.GeContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type != Value.NUMBER || v2.type != Value.NUMBER) {
            throwErr("");
        }
        boolean ret = (Double)v1.v >= (Double) v2.v;
        mStack.add(new Value(Value.BOOLEAN, ret));
    }

    @Override
    public void exitGt(KlangParser.GtContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type != Value.NUMBER || v2.type != Value.NUMBER) {
            throwErr("");
        }
        boolean ret = (Double)v1.v > (Double) v2.v;
        mStack.add(new Value(Value.BOOLEAN, ret));
    }

    @Override
    public void exitId(KlangParser.IdContext ctx) {

    }

    @Override
    public void exitLe(KlangParser.LeContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type != Value.NUMBER || v2.type != Value.NUMBER) {
            throwErr("");
        }
        boolean ret = (Double)v1.v <= (Double) v2.v;
        mStack.add(new Value(Value.BOOLEAN, ret));
    }

    @Override
    public void exitLt(KlangParser.LtContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type != Value.NUMBER || v2.type != Value.NUMBER) {
            throwErr("");
        }
        boolean ret = (Double)v1.v < (Double) v2.v;
        mStack.add(new Value(Value.BOOLEAN, ret));
    }

    @Override
    public void exitMul(KlangParser.MulContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type != Value.NUMBER || v2.type != Value.NUMBER) {
            throwErr("");
        }
        Double ret = ((Double)v1.v) * ((Double)v2.v);
        mStack.add(new Value(Value.NUMBER,ret));
    }

    @Override
    public void exitNegative(KlangParser.NegativeContext ctx) {
        Value v = mStack.pop();
        if (v.type != Value.NUMBER) {
            throwErr("");
        }
        Double ret = 0 - ((Double)v.v);
        mStack.add(new Value(Value.NUMBER,ret));
    }

    @Override
    public void exitNotequal(KlangParser.NotequalContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type != Value.NUMBER || v2.type != Value.NUMBER) {
            throwErr("");
        }
        boolean ret = (Double)v1.v != (Double) v2.v;
        mStack.add(new Value(Value.BOOLEAN, ret));
    }

    @Override
    public void exitNumber(KlangParser.NumberContext ctx) {
        mStack.add(new Value(Value.NUMBER,Double.parseDouble(ctx.getText())));
    }

    @Override
    public void exitOr(KlangParser.OrContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type == Value.STRING || v2.type == Value.STRING) {
            throwErr("");
        }
        mStack.add(new Value(Value.BOOLEAN,(getBoolean(v1) || getBoolean(v2))));
    }

    @Override
    public void exitSkip(KlangParser.SkipContext ctx) {

    }

    @Override
    public void exitSub(KlangParser.SubContext ctx) {
        Value v2 = mStack.pop();
        Value v1 = mStack.pop();
        if (v1.type != Value.NUMBER || v2.type != Value.NUMBER) {
            throwErr("");
        }
        Double ret = ((Double)v1.v) - ((Double)v2.v);
        mStack.add(new Value(Value.NUMBER,ret));
    }

    public Object value() {
        Value value = mStack.pop();
        System.out.println(value.v);
        System.out.println(mStack.size());
        return value.v;
    }


    private void throwErr(String s) {

    }

    public static class Value {
        public static final int NUMBER = 0;
        public static final int STRING = 1;
        public static final int BOOLEAN = 2;
        public int type;
        public Object v;

        public Value(int type, Object v) {
            this.type = type;
            this.v = v;
        }
    }
}
