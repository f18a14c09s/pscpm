/* 
 * Copyright (C) 2018 Francis Johnson
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package f18a14c09s.pscpm.general.services;

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
