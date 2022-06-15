import java.util.HashMap;
import java.util.Map;

public class VarResolver {
    private Map<String,KValue> mValMap = new HashMap<>();

    public void setValMap(String val,KValue value) {
        mValMap.put(val,value);
    }

    public KValue resolve(String val) {
        return mValMap.get(val);
    }
}
