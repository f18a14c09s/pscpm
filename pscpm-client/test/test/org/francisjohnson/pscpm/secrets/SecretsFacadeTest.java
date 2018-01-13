package test.org.francisjohnson.pscpm.secrets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.francisjohnson.pscpm.secrets.data.SecretsException;
import org.francisjohnson.pscpm.secrets.business.SecretsFacade;
import org.francisjohnson.pscpm.secrets.data.UserAccount;
import org.francisjohnson.pscpm.secrets.data.Server;
import org.francisjohnson.pscpm.secrets.data.ServerEnvironment;
import org.francisjohnson.pscpm.secrets.data.ServerSecret;
import org.francisjohnson.pscpm.security.business.SecurityFacade;
import org.francisjohnson.pscpm.security.data.PublicKeyEncryptedSecretKey;
import org.francisjohnson.pscpm.security.data.javacrypto.UserCredential;
import org.francisjohnson.pscpm.security.services.PasswordGenerator;
import org.francisjohnson.pscpm.security.services.javacrypto.IdentityKeyStoreAdapter;

import org.francisjohnson.pscpm.general.data.MapOfLists;
import org.francisjohnson.pscpm.general.services.ServiceFacade;

import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

import test.org.francisjohnson.pscpm.PSCPMTestCase;

public class SecretsFacadeTest extends PSCPMTestCase {

    private static final String SAMPLE_FIRST_INITIALS
            = "jrmwdtcapsgkebnlfhzvo";
    private static final String[] SAMPLE_LAST_NAMES
            = new String[]{"Name", "Smith", "Johnson", "Williams", "Jones",
                "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
                "Anderson", "Thomas", "Jackson", "White", "Harris",
                "Martin", "Thompson", "Garcia", "Martinez", "Robinson",
                "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall",
                "Allen", "Young", "Hernandez", "King", "Wright",
                "Lopez", "Hill", "Scott", "Green", "Adams", "Baker",
                "Gonzalez", "Nelson", "Carter", "Mitchell", "Perez",
                "Roberts", "Turner", "Phillips", "Campbell", "Parker",
                "Evans", "Edwards", "Collins", "Stewart", "Sanchez",
                "Morris", "Rogers", "Reed", "Cook", "Morgan", "Bell",
                "Murphy", "Bailey", "Rivera", "Cooper", "Richardson",
                "Cox", "Howard", "Ward", "Torres", "Peterson", "Gray",
                "Ramirez", "James", "Watson", "Brooks", "Kelly",
                "Sanders", "Price", "Bennett", "Wood", "Barnes", "Ross",
                "Henderson", "Coleman", "Jenkins", "Perry", "Powell",
                "Long", "Patterson", "Hughes", "Flores", "Washington",
                "Butler", "Simmons", "Foster", "Gonzales", "Bryant",
                "Alexander", "Russell", "Griffin", "Diaz", "Hayes"};
    private static final List<String> OSes
            = Arrays.asList("CENT OS 5", "CENT OS 6", "Oracle Linux 6",
                    "Oracle Linux 7", "RedHat EL 4", "RedHat EL 5",
                    "RedHat EL 6", "RedHat EL 7", "Solaris 10",
                    "Solaris 11");
    private Random rand = new Random();
    private PasswordGenerator passwdGen = new PasswordGenerator();
    private List<PublicKeyEncryptedSecretKey> keys;
    private UserCredential authCred;
    private SecretsFacade subject;
    private SecurityFacade securityFacade;

    public SecretsFacadeTest() {
    }

    public static void main(String[] args) {
        String[] args2 = {SecretsFacadeTest.class.getName()};
        JUnitCore.main(args2);
    }

    @Before
    public void setUp() throws Exception {
        setAuthCred(IdentityKeyStoreAdapter.getAdvancedSignatureCredential(IdentityKeyStoreAdapter.DEFAULT_KEY_ALIAS_FILTER));
        setSubject(SecretsFacade.startSession(ServiceFacade.DEFAULT_SERVER_URL,
                getAuthCred()));
        setSecurityFacade(new SecurityFacade(ServiceFacade.DEFAULT_SERVER_URL,
                getAuthCred()));
        setKeys(getSecurityFacade().findMySecretKeys());
    }

    @After
    public void tearDown() throws Exception {
    }

//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#startSession(String,org.francisjohnson.pscpm.security.domain.UserCredential)
//     */
//    @Test
//    public void testStartSession() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#main(java.lang.String[])
//     */
//    @Test
//    public void testMain() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#delete(org.francisjohnson.pscpm.secrets.domain.Server)
//     */
//    @Test
//    public void testDelete() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#delete(org.francisjohnson.pscpm.secrets.domain.ServerSecret)
//     */
//    @Test
//    public void testDelete1() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#save(org.francisjohnson.pscpm.secrets.domain.Server,org.francisjohnson.pscpm.security.domain.PublicKeyEncryptedSecretKey)
//     */
//    @Test
//    public void testSave() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#save(org.francisjohnson.pscpm.secrets.domain.Secret<org.francisjohnson.pscpm.secrets.domain.Server>)
//     */
//    @Test
//    public void testSave1() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#add(org.francisjohnson.pscpm.secrets.domain.Server,org.francisjohnson.pscpm.security.domain.PublicKeyEncryptedSecretKey)
//     */
//    @Test
//    public void testAdd() {
//        fail("Unimplemented");
//    }

//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#add(org.francisjohnson.pscpm.secrets.domain.Certificate,org.francisjohnson.pscpm.security.domain.PublicKeyEncryptedSecretKey)
//     */
//    @Test
//    public void testAdd2() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#refresh(org.francisjohnson.pscpm.secrets.domain.Secret<EntityClass>)
//     */
//    @Test
//    public void testRefresh() {
//        fail("Unimplemented");
//    }

//    /**
//     * @see org.francisjohnson.pscpm.secrets.business.SecretsFacade#getUser()
//     */
//    @Test
//    public void testGetUser() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#encryptAndDestroy(char[])
//     */
//    @Test
//    public void testEncryptAndDestroy() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#decryptPassword(org.francisjohnson.pscpm.security.domain.PublicKeyEncryptedKey)
//     */
//    @Test
//    public void testDecryptPassword() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#generatePassword()
//     */
//    @Test
//    public void testGeneratePassword() {
//        fail("Unimplemented");
//    }
//
//    /**
//     * @see
//     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#findMySecretKeys()
//     */
//    @Test
//    public void testFindMySecretKeys() {
//        fail("Unimplemented");
//    }

    /**
     * @see
     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#add(org.francisjohnson.pscpm.secrets.domain.Secret<org.francisjohnson.pscpm.secrets.domain.Server>)
     */
    @Test
    public void testAdd1() {
        try {
            addRollercoasterSecrets();
            addFictionalSpacecraftSecrets();
        } catch (SecretsException e) {
            failWithException(e);
        }
    }

    private List<ServerSecret> addRollercoasterSecrets() throws SecretsException {
        // Hard-validate the subject.
        getSubject().getClass();
        List<ServerSecret> retval = new ArrayList<ServerSecret>();
        List<ServerEnvironment> envs
                = Arrays.asList(ServerEnvironment.DEVELOPMENT, ServerEnvironment.TEST, ServerEnvironment.PRODUCTION);
        MapOfLists<String, PublicKeyEncryptedSecretKey> keysByPurpose
                = new MapOfLists<String, PublicKeyEncryptedSecretKey>();
        for (PublicKeyEncryptedSecretKey key : getKeys()) {
            String[] segs = key.getSecretKey().getAlias().split("\\s+");
            keysByPurpose.addToList(segs[0].toUpperCase() + ":"
                    + //                                    segs[1].toUpperCase()
                    "Shared", key);
        }
        for (String serverName : SAMPLE_ROLLERCOASTERS) {
            ServerEnvironment env = envs.get(getRand().nextInt(envs.size()));
            List<PublicKeyEncryptedSecretKey> sharedKeys
                    = keysByPurpose.get(env.name() + ":Shared");
            Server server
                    = new Server(serverName, env.name(), OSes.get(getRand().nextInt(OSes.size())),
                            "192.168." + (getRand().nextInt(20) + 5) + "."
                            + (getRand().nextInt(100) + 10), null);
            for (int tot = getRand().nextInt(10) + 5, i = 0; i < tot; i++) {
                server.addCredential(new UserAccount(org.francisjohnson.pscpm.secrets.data.UserAccount.Type.APPLICATION,
                        SAMPLE_FIRST_INITIALS.charAt(getRand().nextInt(SAMPLE_FIRST_INITIALS.length()))
                        + SAMPLE_LAST_NAMES[getRand().nextInt(SAMPLE_LAST_NAMES.length)]
                        + getRand().nextInt(30),
                        getPasswdGen().generatePassword(),
                        getPasswdGen().generatePassword(),
                        "DB"
                        + (rand.nextInt(20) + 1),
                        null));
            }
            for (int tot = getRand().nextInt(10) + 5, i = 0; i < tot; i++) {
                server.addCredential(new UserAccount(org.francisjohnson.pscpm.secrets.data.UserAccount.Type.ADMINISTRATIVE,
                        SAMPLE_FIRST_INITIALS.charAt(getRand().nextInt(SAMPLE_FIRST_INITIALS.length()))
                        + SAMPLE_LAST_NAMES[getRand().nextInt(SAMPLE_LAST_NAMES.length)]
                        + getRand().nextInt(30),
                        getPasswdGen().generatePassword(),
                        getPasswdGen().generatePassword(),
                        "DB"
                        + (rand.nextInt(20) + 1),
                        null));
            }
            ServerSecret secret
                    = getSubject().add(new ServerSecret(server, sharedKeys.get(getRand().nextInt(sharedKeys.size())).getSecretKey()));
            retval.add(secret);
            //            try {
            //                LocalCachePrototype.cache(secret);
            //            } catch (Exception e) {
            //                throwRuntimeException(e);
            //            }
        }
        return retval;
    }

    private List<ServerSecret> addFictionalSpacecraftSecrets() throws SecretsException {
        // Hard-validate the subject
        getSubject().getClass();
        List<ServerSecret> retval = new ArrayList<ServerSecret>();
        List<ServerEnvironment> envs
                = Arrays.asList(ServerEnvironment.DEVELOPMENT, ServerEnvironment.TEST, ServerEnvironment.PRODUCTION);
        MapOfLists<String, PublicKeyEncryptedSecretKey> keysByPurpose
                = new MapOfLists<String, PublicKeyEncryptedSecretKey>();
        for (PublicKeyEncryptedSecretKey key : getKeys()) {
            String[] segs = key.getSecretKey().getAlias().split("\\s+");
            keysByPurpose.addToList(segs[0].toUpperCase() + ":"
                    + //                                    segs[1].toUpperCase()
                    "Shared", key);
        }
        for (String serverName : FICTIONAL_SPACECRAFT) {
            switch (getRand().nextInt(3)) {
                case 0: {
                    serverName = serverName.toUpperCase();
                    break;
                }
                case 1: {
                    serverName = serverName.toLowerCase();
                    break;
                }
                default: {
                    // Keep the case intact.
                    serverName = serverName;
                    break;
                }
            }
            ServerEnvironment env = envs.get(getRand().nextInt(envs.size()));
            List<PublicKeyEncryptedSecretKey> sharedKeys
                    = keysByPurpose.get(env.name() + ":Shared");
            Server server
                    = new Server(serverName, env.name(), OSes.get(getRand().nextInt(OSes.size())),
                            "192.168." + (getRand().nextInt(20) + 5) + "."
                            + (getRand().nextInt(100) + 10), null);
            for (int tot = getRand().nextInt(20) + 5, i = 0; i < tot; i++) {
                server.addCredential(new UserAccount(org.francisjohnson.pscpm.secrets.data.UserAccount.Type.APPLICATION,
                        SAMPLE_FIRST_INITIALS.charAt(getRand().nextInt(SAMPLE_FIRST_INITIALS.length()))
                        + SAMPLE_LAST_NAMES[getRand().nextInt(SAMPLE_LAST_NAMES.length)]
                        + getRand().nextInt(30),
                        getPasswdGen().generatePassword(),
                        getPasswdGen().generatePassword(),
                        "DB"
                        + (rand.nextInt(20) + 1),
                        null));
            }
            for (int tot = getRand().nextInt(20) + 5, i = 0; i < tot; i++) {
                server.addCredential(new UserAccount(org.francisjohnson.pscpm.secrets.data.UserAccount.Type.ADMINISTRATIVE,
                        SAMPLE_FIRST_INITIALS.charAt(getRand().nextInt(SAMPLE_FIRST_INITIALS.length()))
                        + SAMPLE_LAST_NAMES[getRand().nextInt(SAMPLE_LAST_NAMES.length)]
                        + getRand().nextInt(30),
                        getPasswdGen().generatePassword(),
                        getPasswdGen().generatePassword(),
                        "DB"
                        + (rand.nextInt(20) + 1),
                        null));
            }
            ServerSecret secret
                    = getSubject().add(new ServerSecret(server, sharedKeys.get(getRand().nextInt(sharedKeys.size())).getSecretKey()));
            retval.add(secret);
            //            try {
            //                LocalCachePrototype.cache(secret);
            //            } catch (Exception e) {
            //                throwRuntimeException(e);
            //            }
        }
        return retval;
    }

    /**
     * @see
     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#decrypt(org.francisjohnson.pscpm.secrets.domain.Secret<EntityClass>)
     */
    @Test
    public void testDecrypt() {
        List<ServerSecret> servers = null;
        try {
            servers = addRollercoasterSecrets();
        } catch (SecretsException e) {
            throwRuntimeException(e);
        }
        try {
            for (ServerSecret secret : servers) {
                secret.setData(getSubject().decrypt(secret));
                assertNotNull(secret.getData());
            }
        } catch (SecretsException e) {
            failWithException(e);
        } finally {
            try {
                for (ServerSecret secret : servers) {
                    getSubject().delete(secret);
                }
            } catch (Exception e) {
                // Error with cleanup
                e.printStackTrace(System.out);
            }
        }
    }

    /**
     * @see
     * org.francisjohnson.pscpm.secrets.business.SecretsFacade#findAvailableServers()
     */
    @Test
    public void testFindAvailableServers() {
        System.out.println("Browsing the list of available servers:");
        try {
            for (ServerSecret server : getSubject().findAvailableServers()) {
                System.out.println("\t" + server.getData().getName());
                System.out.println("\t\tDescription: "
                        + server.getData().getDescription());
                System.out.println("\t\tEnvironment: "
                        + server.getData().getEnvironment());
                System.out.println("\t\tOS Version:  "
                        + server.getData().getOsVersion());
                System.out.println("\t\tIP Address:  "
                        + server.getData().getIpAddress());
            }
        } catch (SecretsException e) {
            failWithException(e);
        }
    }

    private void setAuthCred(UserCredential authCred) {
        this.authCred = authCred;
    }

    private UserCredential getAuthCred() {
        return authCred;
    }

    private void setSubject(SecretsFacade facade) {
        this.subject = facade;
    }

    private SecretsFacade getSubject() {
        return subject;
    }

    private void setSecurityFacade(SecurityFacade security) {
        this.securityFacade = security;
    }

    private SecurityFacade getSecurityFacade() {
        return securityFacade;
    }

    private void setRand(Random rand) {
        this.rand = rand;
    }

    private Random getRand() {
        return rand;
    }

    private void setPasswdGen(PasswordGenerator gen) {
        this.passwdGen = gen;
    }

    private PasswordGenerator getPasswdGen() {
        return passwdGen;
    }

    private void setKeys(List<PublicKeyEncryptedSecretKey> keys) {
        this.keys = keys;
    }

    private List<PublicKeyEncryptedSecretKey> getKeys() {
        return keys;
    }

    private static final List<String> FICTIONAL_SPACECRAFT
            = Arrays.asList("Ark", "C57", "EAS", "EEV", "EVA", "Gay", "ISA", "Ori",
                    "TIE", "USS", "V19", "Von", "Buck", "Chig", "Dora",
                    "Halo", "Jedi", "NSEA", "RF42", "SA43", "SDF1", "SDF3",
                    "Star", "TFNS", "UNSC", "Betty", "Cylon", "Death",
                    "Droid", "Eagle", "Event", "Hawks", "Lemon", "Naboo",
                    "Orbit", "Orion", "Other", "Rebel", "SA23E", "SA23J",
                    "Space", "Terra", "Titan", "Vwing", "ARC170", "Escape",
                    "Needle", "Planet", "Raider", "Shadow", "Sulaco",
                    "TARDIS", "Vorlon", "Wraith", "X30199", "Yamato",
                    "Babylon", "Basroil", "Destiny", "Elysium", "F302100",
                    "Frigate", "Javelin", "Jupiter", "Minbari", "Nemesis",
                    "Nirvana", "Argonaut", "Awing109", "Bwing110",
                    "Colonial", "Galactic", "Hispania", "Hyperion",
                    "Libertad", "Megazone", "Normandy", "Nostromo",
                    "Personal", "Serenity", "Stargate", "Valkyrie",
                    "Xwing111", "Ywing112", "Amaterasu", "Andromeda",
                    "Basestars", "BatRocket", "Deucalion", "Draconian",
                    "EarthStar", "Endurance", "Geonosian", "Liberator",
                    "Narcissus", "Nialclass", "Nostalgia", "ShangriLa",
                    "Battlestar", "Fraziclass", "Heighliner", "Millennium",
                    "Prometheus", "TauriEarth", "Technowing", "Bellerophon",
                    "Confederacy", "Sentriclass", "Conquistador",
                    "Swordbreaker", "HunterGratzner");
    private static final List<String> SAMPLE_ROLLERCOASTERS
            = Arrays.asList("DisasterTransport", "MeanStreak", "WildCat",
                    "BlueStreak", "CedarMineRide", "Corkscrew", "GateKeeper",
                    "Gemini", "IronDragon", "MagnumXL200", "Maverick",
                    "MillenniumForce", "Raptor", "TopThrillDragster",
                    "WickedTwister");
}
