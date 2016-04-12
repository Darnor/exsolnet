package helper;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.core.SubstringMatcher;

import java.util.regex.Pattern;

/**
 * Created by tourn on 12.4.16.
 */
public class RegexMatcher extends SubstringMatcher{
    private final Pattern pattern;

    protected RegexMatcher(String substring) {
        super(substring);
        this.pattern = Pattern.compile(substring, Pattern.MULTILINE | Pattern.DOTALL);
    }

    @Override
    protected boolean evalSubstringOf(String string) {
        return pattern.matcher(string).find();
    }

    @Override
    protected String relationship() {
        return "matching regex";
    }

    @Factory
    public static Matcher<String> matches(String substring) {
        return new RegexMatcher(substring);
    }
}
