public class Log {
    private static boolean show = true;
    private static String TAG = "KLang";

    public static void w(String log) {
        w(TAG,log);
    }
    public static void w(String tag,String log) {
        if (show) {
            System.out.println(String.format("%s: %s",tag,log));
        }
    }
}
