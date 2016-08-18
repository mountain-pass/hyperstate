package au.com.mountainpass.hyperstate.core;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

abstract public class Titled {

    @Nullable
    String title = null;

    private Set<String> natures = new HashSet<>();

    public Titled() {
    }

    public Titled(final String title, final String... natures) {
        this.title = title;
        if (natures != null) {
            this.natures.addAll(Arrays.asList(natures));
        }
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
