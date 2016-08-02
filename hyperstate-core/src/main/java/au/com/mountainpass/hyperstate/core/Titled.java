package au.com.mountainpass.hyperstate.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract public class Titled {

    @Nullable
    String title = null;

    private Set<String> natures = new HashSet<>();

    public Titled() {
    }

    public Titled(final Titled titled) {
        this();
        this.title = titled.title;
        this.natures = titled.natures;
    }

    public Titled(final String title, final Set<String> natures) {
        this.title = title;
        this.natures = natures;
    }

    public Titled(final String title, final String... natures) {
        this.title = title;
        this.natures.addAll(Arrays.asList(natures));
    }

    /**
     * @return the title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * @return the natures
     */
    @JsonProperty("class")
    public Set<String> getClasses() {
        return natures;
    }

    public boolean hasNature(final String nature) {
        return this.getClasses().contains(nature);
    }

    private String interpolate(final String value, final String... args) {
        if (args.length == 0) {
            return value;
        } else {
            final Pattern patt = Pattern.compile("\\{(.*?)\\}");
            final Matcher m = patt.matcher(value);
            final StringBuffer sb = new StringBuffer(value.length());
            for (int i = 0; m.find(); ++i) {
                final String code = m.group(1);
                m.appendReplacement(sb, Matcher.quoteReplacement(args[i]));
            }
            m.appendTail(sb);
            return sb.toString();
        }
    }

    void setTitle(final String template, final String... args) {
        title = interpolate(template, args);
    }

    /**
     * @param natures
     *            the natures to set
     */
    @JsonProperty("class")
    public void setClasses(final Set<String> natures) {
        this.natures = natures;
    }

    /**
     * @param title
     *            the title to set
     */
    public void setTitle(final String title) {
        this.title = title;
    }

}
