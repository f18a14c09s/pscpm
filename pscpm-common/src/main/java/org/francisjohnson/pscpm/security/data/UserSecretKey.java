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
package org.francisjohnson.pscpm.security.data;

import java.io.Serializable;


import org.francisjohnson.pscpm.general.data.Identifiable;


/**
 * The purpose of this class is referential integrity.  No secret information is
 * stored here.
 */
public class UserSecretKey implements Serializable, Identifiable<Long> {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    private Long id;

    private String alias;

    /**
     * Identifies the cryptographic algorithm for which the symmetric key,
     * stored in ENCRYPTED_SYMMETRIC_KEY column, is used.  Example: AES.
     */
    private String symmetricAlgorithm;

    /**
     * This is the owner who decides with whom to share the secret key, as
     * implemented in the functional rules.
     * TODO: The functional rules shouldn't allow anyone but the owner or a
     * administrator grantee to share the key.
     */
    private User owner;

    public UserSecretKey() {
    }

    //    public UserSecretKey(User owner, String keyAlias) {
    //        setOwner(owner);
    //        setAlias(keyAlias);
    //    }

    public UserSecretKey(User owner, String keyAlias,
                         String symmetricAlgorithm) {
        setOwner(owner);
        setAlias(keyAlias);
        setSymmetricAlgorithm(symmetricAlgorithm);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public void setSymmetricAlgorithm(String symmetricAlgorithm) {
        this.symmetricAlgorithm = symmetricAlgorithm;
    }

    public String getSymmetricAlgorithm() {
        return symmetricAlgorithm;
    }

    @Override
    public String toString() {
        return getAlias() + " (ID=" + getId() + ")";
    }

    @Override
    public int hashCode() {
        return getId() == null ? 0 : getId().hashCode();
    }

    @Override
    public boolean equals(Object rhsO) {
        UserSecretKey rhs =
            rhsO instanceof UserSecretKey ? (UserSecretKey)rhsO : null;
        return rhs != null && rhs.getId() != null && getId() != null &&
            getId().equals(rhs.getId());
    }
}
