package util;

public class ValidationUtil {

    private ValidationUtil() {}

    public static boolean containsScriptTag(String content) {
        return content == null || content.contains("<script>") || content.contains("</script>");
    }

    public static boolean isEmpty(String content) {
        return content == null || content.trim().isEmpty() || content.trim().matches("^(<p>(\\s*&nbsp;\\s*)*</p>(\n|\r|\r\n)*)+$");
    }
}
