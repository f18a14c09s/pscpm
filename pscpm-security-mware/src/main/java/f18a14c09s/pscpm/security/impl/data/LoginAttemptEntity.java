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
package f18a14c09s.pscpm.security.impl.data;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import f18a14c09s.pscpm.general.data.Identifiable;


@Entity
@NamedQueries( { @NamedQuery(name = "LoginAttemptEntity.findAll",
                             query = "select o from LoginAttemptEntity o") })
@SequenceGenerator(name = "LOGIN_ATTEMPT_ID_S",
                   sequenceName = "LOGIN_ATTEMPT_ID_S", allocationSize = 1,
                   initialValue = 1)
@Table(name = "LOGIN_ATTEMPTS")
public class LoginAttemptEntity implements Serializable, Identifiable<Long> {
    /**
     * This value must not change, or else the data in the cache will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    public static enum LoginResult {
        SUCCESSFUL,
        REJECTED;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "LOGIN_ATTEMPT_ID_S")
    private Long id;

    @Column(name = "DATE_ATTEMPTED", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateAttempted;

    @OneToOne
    @JoinColumn(name = "PRINCIPAL_ID", referencedColumnName = "ID",
                nullable=false
//            ,precision=38,scale=0
    )
    private SecurityPrincipalEntity principal;

    @Column(name = "LOGIN_RESULT",
            length=1000, nullable=false)
    private LoginResult loginResult;

    public LoginAttemptEntity() {
    }

    public LoginAttemptEntity(SecurityPrincipalEntity principal, LoginResult loginResult) {
        setPrincipal(principal);
        setLoginResult(loginResult);
        setDateAttempted(new Date());
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    private void setPrincipal(SecurityPrincipalEntity principal) {
        this.principal = principal;
    }

    public SecurityPrincipalEntity getPrincipal() {
        return principal;
    }

    private void setDateAttempted(Date dateAttempted) {
        this.dateAttempted = dateAttempted;
    }

    public Date getDateAttempted() {
        return dateAttempted;
    }

    private void setLoginResult(LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    public LoginResult getLoginResult() {
        return loginResult;
    }
}
