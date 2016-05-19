package util;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static final PolicyFactory SANITIZE_POLICY = new HtmlPolicyBuilder()
            .allowCommonBlockElements()
            .allowCommonInlineFormattingElements()
            .allowStandardUrlProtocols()
            .allowStyling()
            .allowAttributes("class").matching(Pattern.compile("[a-zA-Z0-9\\s,\\-_]+")).globally()
            .requireRelNofollowOnLinks()
            .toFactory();

    private ValidationUtil() {}

    public static String sanitizeHtml(String content) {
        return SANITIZE_POLICY.sanitize(content);
    }

    public static boolean isEmpty(String content) {
        return content.trim().matches("^((<p>)*\\s*(&nbsp;)*\\s*(</p>)*(\r|\n|\r\n)*)+$");
    }
}
