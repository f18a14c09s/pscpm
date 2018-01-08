package org.francisjohnson.pscpm.general.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The purpose of this class is to automatically create lists as new values are
 * added.
 * @param <KeyClass>
 * @param <ListElementClass>
 */
public class MapOfLists<KeyClass, ListElementClass> extends HashMap<KeyClass, List<ListElementClass>> {
    public void addToList(KeyClass listKey, ListElementClass element) {
        if (!containsKey(listKey)) {
            put(listKey, new ArrayList<ListElementClass>());
        }
        get(listKey).add(element);
    }
}
