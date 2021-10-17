module cognition.core {
    requires com.google.gson;

    exports core;
    exports json;

    opens core;
    opens core.tools;
    opens core.validators;
    opens json;
}