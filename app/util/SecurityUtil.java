package util;

import org.apache.xerces.impl.dv.util.Base64;
import org.mindrot.jbcrypt.BCrypt;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

public class SecurityUtil {

    private static final PolicyFactory SANITIZE_POLICY = new HtmlPolicyBuilder()
            .allowCommonBlockElements()
            .allowCommonInlineFormattingElements()
            .allowStandardUrlProtocols()
            .allowStyling()
            .allowAttributes("class").matching(Pattern.compile("[a-zA-Z0-9\\s,\\-_]+")).globally()
            .requireRelNofollowOnLinks()
            .toFactory()
            .and(Sanitizers.LINKS)
            .and(Sanitizers.TABLES)
            .and(Sanitizers.IMAGES);

    private SecurityUtil() {}

    public static String sanitizeHtml(String content) {
        return SANITIZE_POLICY.sanitize(content);
    }

    public static boolean isEmpty(String content) {
        return content.trim().matches("^((<p>)*\\s*(&nbsp;)*\\s*(</p>)*(\r|\n|\r\n)*)+$");
    }

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String candidate, String hashed) {
        return BCrypt.checkpw(candidate, hashed);
    }

    public static String generateVerificationCode() throws UnsupportedEncodingException {
        Random ran = new SecureRandom();
        byte [] salt = new byte[32];
        ran.nextBytes(salt);
        return Base64.encode(salt).replaceAll("[^a-zA-Z0-9]", "");
    }
}
