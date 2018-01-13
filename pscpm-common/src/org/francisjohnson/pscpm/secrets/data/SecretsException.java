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
package org.francisjohnson.pscpm.secrets.data;

public class SecretsException extends Exception {
    public SecretsException(String string, Throwable throwable, boolean b,
                            boolean b1) {
        super(string, throwable, b, b1);
    }

    public SecretsException(Throwable throwable) {
        super(throwable);
    }

    public SecretsException(String string, Throwable throwable) {
        super(string, throwable);
    }

    public SecretsException(String string) {
        super(string);
    }

    public SecretsException() {
        super();
    }
}
