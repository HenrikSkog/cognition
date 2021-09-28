module cognition.core {
    requires transitive com.fasterxml.jackson.databind;

    exports core;
    exports json;

    opens core;
    opens json;
}