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
package f18a14c09s.pscpm.general.data;

public enum BreadAction {
    BROWSE("Browse"),
    READ("Read"),
    EDIT("Edit"),
    ADD("Add"),
    DELETE("Delete");
    private String label;

    private void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    private BreadAction(String label) {
        setLabel(label);
    }
}
