package org.francisjohnson.pscpm.secrets.data;

import java.io.Serializable;

import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedKey;


public class UserAccount implements Serializable {
    /**
     * This value must not change, or else the data in the database will become
     * invalid.
     */
    private static final long serialVersionUID = 1;

    public static enum Type {
        APPLICATION,
        ADMINISTRATIVE,
        INDIVIDUAL;

        private String getLabel() {
            return Character.toUpperCase(name().charAt(0)) +
                name().substring(1).toLowerCase();
        }

        public String toString() {
            return getLabel();
        }
    }
    private Type type;
    private String userId;
    //    private byte[] osPassword;
    //    private byte[] dbPassword;
    //    private PublicKeyEncryptedSecretKey osPassword;
    //    private PublicKeyEncryptedSecretKey dbPassword;

    /**
     * @deprecated
     * If the password is encrypted with the public key, then it can't be shared.
     */
    @Deprecated
    private PublicKeyEncryptedKey deprecatedOsPassword;

    /**
     * @deprecated
     * If the password is encrypted with the public key, then it can't be shared.
     */
    @Deprecated
    private PublicKeyEncryptedKey deprecatedDbPassword;
    private char[] osPassword;
    private char[] dbPassword;
    private String database;
    private String comments;

    public UserAccount() {
        super();
    }

    public UserAccount(Type type, String userId,
                      PublicKeyEncryptedKey osPassword,
                      PublicKeyEncryptedKey dbPassword, String database,
                      String comments) {
        super();
        setType(type);
        setUserId(userId);
        setDeprecatedOsPassword(osPassword);
        setDeprecatedDbPassword(dbPassword);
        setDatabase(database);
        setComments(comments);
    }

    public UserAccount(Type type, String userId,
                      char[] osPassword,
                      char[] dbPassword, String database,
                      String comments) {
        super();
        setType(type);
        setUserId(userId);
        setOsPassword(osPassword);
        setDbPassword(dbPassword);
        setDatabase(database);
        setComments(comments);
    }

    public void setType(UserAccount.Type type) {
        this.type = type;
    }

    public UserAccount.Type getType() {
        return type;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatabase() {
        return database;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setOsPassword(char[] osPassword) {
        this.osPassword = osPassword;
    }

    public char[] getOsPassword() {
        return osPassword;
    }

    public void setDbPassword(char[] dbPassword) {
        this.dbPassword = dbPassword;
    }

    public char[] getDbPassword() {
        return dbPassword;
    }

    public void setDeprecatedOsPassword(PublicKeyEncryptedKey osPassword) {
        this.deprecatedOsPassword = osPassword;
    }

    public PublicKeyEncryptedKey getDeprecatedOsPassword() {
        return deprecatedOsPassword;
    }

    public void setDeprecatedDbPassword(PublicKeyEncryptedKey dbPassword) {
        this.deprecatedDbPassword = dbPassword;
    }

    public PublicKeyEncryptedKey getDeprecatedDbPassword() {
        return deprecatedDbPassword;
    }
}
