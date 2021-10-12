package ui.controllers.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Custom annotation to suppress SpotBugs warnings.
 * It must have this specific name to be considered by SpotBugs.
 * Inspired by the IT1901 staff.
 */
@Retention(RetentionPolicy.CLASS)
public @interface SuppressFBWarnings {

    /**
     * The array of FindBugs warnings that are to be suppressed in
     * annotated element. The value can be a bug category, kind or pattern.
     */
    String[] value() default {
            "UI_INHERITANCE_UNSAFE_GETRESOURCE",
            "NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD"
    };

    /**
     * Optional documentation of the reason why the warning is suppressed.
     */
    String justification() default "" +
            "UI_INHERITANCE_UNSAFE_GETRESOURCE: The UI Controller classes will not be used outside the designated package. \n " +
            "NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD: The FXML nodes are initialized via a mechanism not seen by the FindBugs analysis.";
}
