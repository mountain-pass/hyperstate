package au.com.windyroad.hyperstate.core;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import au.com.windyroad.hyperstate.server.serialization.MessageSourceAwareSerializer;

abstract public class Labelled {

    private Set<String> natures = new HashSet<>();

    @Nullable
    String label = null;

    public Labelled() {
    }

    public Labelled(Labelled labelled) {
        this();
        this.label = labelled.label;
    }

    void setLabel(String template, String... args) {
        label = interpolate(template, args);
    }

    private String interpolate(String value, String... args) {
        if (args.length == 0) {
            return value;
        } else {
            Pattern patt = Pattern.compile("\\{(.*?)\\}");
            Matcher m = patt.matcher(value);
            StringBuffer sb = new StringBuffer(value.length());
            for (int i = 0; m.find(); ++i) {
                String code = m.group(1);
                m.appendReplacement(sb, Matcher.quoteReplacement(args[i]));
            }
            m.appendTail(sb);
            return sb.toString();
        }
    }

    public Labelled(String label, Set<String> natures) {
        this.natures = natures;
        this.label = label;
    }

    public Labelled(String label) {
        this.label = label;
    }

    /**
     * @return the natures
     */
    @JsonProperty("class")
    public Set<String> getNatures() {
        return natures;
    }

    /**
     * @param natures
     *            the natures to set
     */
    public void setNatures(Set<String> natures) {
        this.natures = natures;
    }

    /**
     * @return the label
     */
    @JsonSerialize(using = MessageSourceAwareSerializer.class)
    @JsonProperty("title")
    public String getLabel() {
        return label;
    }

    /**
     * @param title
     *            the label to set
     */
    public void setTitle(String title) {
        this.label = title;
    }

    public boolean hasNature(String nature) {
        return this.getNatures().contains(nature);
    }

}
