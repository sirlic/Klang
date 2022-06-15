import java.util.Stack;

public interface IFunCaller {
    public KValue invoke(String funName, Stack<KValue> params);
}
