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
package org.francisjohnson.pscpm.security.impl.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.francisjohnson.pscpm.general.data.Identifiable;
import org.francisjohnson.pscpm.security.data.UserSecretKey;

/**
 * The purpose of this class is referential integrity. No secret information is
 * stored here.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "UserSecretKeyEntity.findAll",
            query = "select o from UserSecretKeyEntity o")})
@SequenceGenerator(name = "USER_SECRET_KEY_ID_S",
        sequenceName = "USER_SECRET_KEY_ID_S", allocationSize = 1,
        initialValue = 1)
@Table(name = "USER_SECRET_KEYS")
public class UserSecretKeyEntity implements Serializable, Identifiable<Long> {

    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    @Id
    @Column(name = "ID", nullable = false, precision = 38, scale = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "USER_SECRET_KEY_ID_S")
    private Long id;

    @Column(name = "ALIAS", length = 1000, nullable = false,
            unique = true)
    private String alias;

    /**
     * Identifies the cryptographic algorithm for which the symmetric key,
     * stored in ENCRYPTED_SYMMETRIC_KEY column, is used. Example: AES.
     */
    @Column(name = "SYMMETRIC_ALGORITHM",
            length = 100, nullable = false)
    private String symmetricAlgorithm;

    /**
     * This is the owner who decides with whom to share the secret key, as
     * implemented in the functional rules. TODO: The functional rules shouldn't
     * allow anyone but the owner or a administrator grantee to share the key.
     */
    @ManyToOne
    @JoinColumn(name = "OWNER_ID", referencedColumnName = "ID",
            nullable = false
    //    ,precision=38,scale=0
    )
    private UserEntity owner;

    public UserSecretKeyEntity() {
    }

    //    public UserSecretKey(User owner, String keyAlias) {
    //        setOwner(owner);
    //        setAlias(keyAlias);
    //    }
    public UserSecretKeyEntity(UserEntity owner, String keyAlias,
            String symmetricAlgorithm) {
        setOwner(owner);
        setAlias(keyAlias);
        setSymmetricAlgorithm(symmetricAlgorithm);
    }

    public UserSecretKeyEntity(UserSecretKey secretKey) {
        setAlias(
                secretKey.getAlias());
        setId(
                secretKey.getId());
        setOwner(
                secretKey.getOwner() == null ? null : new UserEntity(secretKey.getOwner()));
        setSymmetricAlgorithm(
                secretKey.getSymmetricAlgorithm());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public UserEntity getOwner() {
        return owner;
    }

    private void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    private void setSymmetricAlgorithm(String symmetricAlgorithm) {
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
        UserSecretKeyEntity rhs
                = rhsO instanceof UserSecretKeyEntity ? (UserSecretKeyEntity) rhsO : null;
        return rhs != null && rhs.getId() != null && getId() != null
                && getId().equals(rhs.getId());
    }

    public UserSecretKey toUserSecretKey() {
        UserSecretKey retval = new UserSecretKey();
        retval.setAlias(
                getAlias());
        retval.setId(
                getId());
        retval.setOwner(
                getOwner() == null ? null : getOwner().toUser());
        retval.setSymmetricAlgorithm(
                getSymmetricAlgorithm());
        return retval;
    }
}
