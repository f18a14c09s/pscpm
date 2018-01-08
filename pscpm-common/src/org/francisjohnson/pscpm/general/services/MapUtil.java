package org.francisjohnson.pscpm.general.services;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    private static final class MapEntry<KeyClass, ValueClass> implements Map.Entry<KeyClass, ValueClass> {
        private KeyClass key;
        private ValueClass value;

        public MapEntry(KeyClass key, ValueClass value) {
            setKey(key);
            setValue(value);
        }

        public void setKey(KeyClass key) {
            this.key = key;
        }

        public KeyClass getKey() {
            return key;
        }

        public ValueClass setValue(ValueClass value) {
            this.value = value;
            return getValue();
        }

        public ValueClass getValue() {
            return value;
        }
    }

    public static <KeyClass, ValueClass> Map.Entry<KeyClass, ValueClass> entry(KeyClass key,
                                                                               ValueClass value) {
        return new MapEntry<KeyClass, ValueClass>(key, value);
    }

    public static <KeyClass, ValueClass> Map<KeyClass, ValueClass> asMap(Map.Entry<KeyClass, ValueClass>... entries) {
        Map<KeyClass, ValueClass> retval = new HashMap<KeyClass, ValueClass>();
        if (entries != null) {
            for (Map.Entry<KeyClass, ValueClass> entry : entries) {
                retval.put(entry.getKey(), entry.getValue());
            }
        }
        return retval;
    }
}
