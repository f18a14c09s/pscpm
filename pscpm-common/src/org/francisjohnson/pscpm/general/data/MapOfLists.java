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
