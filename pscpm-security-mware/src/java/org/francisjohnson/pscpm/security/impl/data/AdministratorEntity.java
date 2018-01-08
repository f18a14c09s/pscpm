package org.francisjohnson.pscpm.security.impl.data;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;


@Entity
@NamedQueries( { @NamedQuery(name = "AdministratorEntity.findAll",
                             query = "select o from AdministratorEntity o") })
@Inheritance
@DiscriminatorValue("ADMINISTRATOR")
public class AdministratorEntity extends UserEntity implements Serializable {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    public AdministratorEntity() {
    }
}
