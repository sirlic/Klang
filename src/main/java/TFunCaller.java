import java.util.Stack;

public class TFunCaller implements IFunCaller{
    @Override
    public KValue invoke(String funName, Stack<KValue> params) {
        return new KValue(2);
    }

}
