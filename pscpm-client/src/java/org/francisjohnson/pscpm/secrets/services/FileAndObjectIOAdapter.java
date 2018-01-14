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
package org.francisjohnson.pscpm.secrets.services;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.francisjohnson.pscpm.general.data.Identifiable;
import org.francisjohnson.pscpm.general.services.IOUtil;

/**
 * This class assumes that if confidentiality is required, then the data has
 * already been encrypted externally.
 */
public class FileAndObjectIOAdapter {

    private static final Logger _log = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    private static Logger getLog() {
        return _log;
    }
    public static final File DEFAULT_CACHE_DIRECTORY
            = new File(new File(System.getProperty("user.home")), ".pscpm");
    public static final File DEFAULT_CACHE_FILE
            = new File(DEFAULT_CACHE_DIRECTORY, "secrets.cache");

    public FileAndObjectIOAdapter() {
        super();
    }

    private static <SerIdent extends Serializable & Identifiable<?>> Map<Number, SerIdent> mapOntoIds(List<SerIdent> objectsToCache) {
        Map<Number, SerIdent> retval = new HashMap<Number, SerIdent>();
        for (SerIdent obj : objectsToCache) {
            if (obj == null || obj.getId() == null) {
                getLog().severe(FileAndObjectIOAdapter.class.getSimpleName()
                        + ".  Unable to map object onto its ID "
                        + (obj == null
                                ? "because the object itself is null"
                                : obj.getId() == null
                                ? "because the ID is null (obj=" + obj
                                + ")" : "for an unknown reason") + ".");
            } else {
                retval.put(obj.getId(), obj);
            }
        }
        return retval;
    }

    public static <SerIdent extends Serializable & Identifiable<?>> void cache(SerIdent objectToCache) throws IOException,
            ClassNotFoundException {
        if (objectToCache == null) {
            // TODO: Throw an exception and handle it on the other side.
            getLog().severe(FileAndObjectIOAdapter.class.getSimpleName()
                    + ".  Ignoring null input on add.");
        } else {
            boolean dirCreated = false;
            if (!DEFAULT_CACHE_DIRECTORY.exists()) {
                dirCreated = DEFAULT_CACHE_DIRECTORY.mkdir();
            }
            if (DEFAULT_CACHE_DIRECTORY.exists()) {
                if (DEFAULT_CACHE_DIRECTORY.isDirectory()) {
                    getLog().info("Default cache directory found"
                            + (dirCreated
                                    ? ", after system created it" : "")
                            + ".");
                } else {
                    throw new IOException("Default cache path exists but is not a directory.  Path: "
                            + DEFAULT_CACHE_DIRECTORY.getAbsolutePath()
                            + ".");
                }
            } else {
                throw new IOException("Default cache directory not found, and system failed to create it.  Path: "
                        + DEFAULT_CACHE_DIRECTORY.getAbsolutePath()
                        + ".");
            }
            if (DEFAULT_CACHE_FILE.exists()
                    && DEFAULT_CACHE_FILE.length() >= 1) {
                File backupFile
                        = File.createTempFile(DEFAULT_CACHE_FILE.getName(),
                                ".backup", DEFAULT_CACHE_DIRECTORY);
                backupFile.delete();
                DEFAULT_CACHE_FILE.renameTo(backupFile);
                FileInputStream fis = null;
                ObjectInputStream ois = null;
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    boolean objectWasUpdated = false;
                    fis = new FileInputStream(backupFile);
                    ois = new ObjectInputStream(fis);
                    fos = new FileOutputStream(DEFAULT_CACHE_FILE);
                    oos = new ObjectOutputStream(fos);
                    while (true) {
                        Object obj = null;
                        try {
                            obj = ois.readObject();
                        } catch (EOFException e) {
                            getLog().info("End of cache file encountered.  This is normal behavior.");
                            break;
                        }
                        SerIdent curSecret
                                = obj != null && objectToCache.getClass().equals(obj.getClass())
                                ? (SerIdent) obj : null;
                        if (curSecret != null
                                && curSecret.getId().equals(objectToCache.getId())) {
                            oos.writeObject(objectToCache);
                            objectWasUpdated = true;
                        } else {
                            oos.writeObject(obj);
                        }
                    }
                    if (!objectWasUpdated) {
                        oos.writeObject(objectToCache);
                    }
                } finally {
                    IOUtil.closeSafely(ois, fis, oos, fos);
                }
                backupFile.delete();
            } else {
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    fos = new FileOutputStream(DEFAULT_CACHE_FILE);
                    oos = new ObjectOutputStream(fos);
                    oos.writeObject(objectToCache);
                } finally {
                    IOUtil.closeSafely(oos, fos);
                }
            }
        }
    }

    public static <SerIdent extends Serializable & Identifiable<?>> void cache(List<SerIdent> objectsToCache) throws IOException,
            ClassNotFoundException {
        if (objectsToCache == null || objectsToCache.isEmpty()) {
            // TODO: Throw an exception and handle it on the other side.
            getLog().severe(FileAndObjectIOAdapter.class.getSimpleName()
                    + ".  Ignoring "
                    + (objectsToCache == null ? "null"
                            : objectsToCache.isEmpty() ? "empty"
                            : "unknown error with") + " list on bulk add.");
        } else {
            boolean dirCreated = false;
            if (!DEFAULT_CACHE_DIRECTORY.exists()) {
                dirCreated = DEFAULT_CACHE_DIRECTORY.mkdir();
            }
            if (DEFAULT_CACHE_DIRECTORY.exists()) {
                if (DEFAULT_CACHE_DIRECTORY.isDirectory()) {
                    getLog().info("Default cache directory found"
                            + (dirCreated
                                    ? ", after system created it" : "")
                            + ".");
                } else {
                    throw new IOException("Default cache path exists but is not a directory.  Path: "
                            + DEFAULT_CACHE_DIRECTORY.getAbsolutePath()
                            + ".");
                }
            } else {
                throw new IOException("Default cache directory not found, and system failed to create it.  Path: "
                        + DEFAULT_CACHE_DIRECTORY.getAbsolutePath()
                        + ".");
            }
            if (DEFAULT_CACHE_FILE.exists()
                    && DEFAULT_CACHE_FILE.length() >= 1) {
                Map<Number, SerIdent> idMap = mapOntoIds(objectsToCache);
                Set<Number> updateMap = new HashSet<Number>();
                File backupFile
                        = File.createTempFile(DEFAULT_CACHE_FILE.getName(),
                                ".backup", DEFAULT_CACHE_DIRECTORY);
                backupFile.delete();
                DEFAULT_CACHE_FILE.renameTo(backupFile);
                FileInputStream fis = null;
                ObjectInputStream ois = null;
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    fis = new FileInputStream(backupFile);
                    ois = new ObjectInputStream(fis);
                    fos = new FileOutputStream(DEFAULT_CACHE_FILE);
                    oos = new ObjectOutputStream(fos);
                    int classCastExceptions = 0;
                    while (true) {
                        Object obj = null;
                        try {
                            obj = ois.readObject();
                        } catch (EOFException e) {
                            getLog().info("End of cache file encountered.  This is normal behavior.");
                            break;
                        }
                        SerIdent curSecret = null;
                        try {
                            curSecret = (SerIdent) obj;
                        } catch (ClassCastException e) {
                            classCastExceptions++;
                        }
                        if (curSecret != null && curSecret.getId() != null
                                && idMap.get(curSecret.getId()) != null) {
                            oos.writeObject(idMap.get(curSecret.getId()));
                            updateMap.add(curSecret.getId());
                        } else {
                            oos.writeObject(obj);
                        }
                    }
                    if (classCastExceptions > 0) {
                        getLog().info(FileAndObjectIOAdapter.class.getSimpleName()
                                + ".  Total of "
                                + classCastExceptions
                                + " encountered during bulk cache update."
                                + "  This is likely normal behavior, since the cache contains multiple object classes.");
                    }
                    for (Map.Entry<Number, SerIdent> entry
                            : idMap.entrySet()) {
                        if (!updateMap.contains(entry.getKey())) {
                            oos.writeObject(entry.getValue());
                        }
                    }
                } finally {
                    IOUtil.closeSafely(ois, fis, oos, fos);
                }
                backupFile.delete();
            } else {
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    fos = new FileOutputStream(DEFAULT_CACHE_FILE);
                    oos = new ObjectOutputStream(fos);
                    for (SerIdent obj : objectsToCache) {
                        oos.writeObject(obj);
                    }
                } finally {
                    IOUtil.closeSafely(oos, fos);
                }
            }
        }
    }

    /**
     * @param <SerIdent>
     * @param objectToDelete
     * @return boolean. True means the object does not reside in the cache, and
     * false means that deletion failed.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <SerIdent extends Serializable & Identifiable<?>> boolean delete(SerIdent objectToDelete) throws IOException,
            ClassNotFoundException {
        if (objectToDelete == null) {
            getLog().severe(FileAndObjectIOAdapter.class.getSimpleName()
                    + ".  Ignoring null input on delete.  Returning false.");
            return false;
        } else {
            boolean dirCreated = false;
            if (!DEFAULT_CACHE_DIRECTORY.exists()) {
                dirCreated = DEFAULT_CACHE_DIRECTORY.mkdir();
            }
            if (DEFAULT_CACHE_DIRECTORY.exists()) {
                if (DEFAULT_CACHE_DIRECTORY.isDirectory()) {
                    getLog().info("Default cache directory found"
                            + (dirCreated
                                    ? ", after system created it" : "")
                            + ".");
                } else {
                    throw new IOException("Default cache path exists but is not a directory.  Path: "
                            + DEFAULT_CACHE_DIRECTORY.getAbsolutePath()
                            + ".");
                }
            } else {
                throw new IOException("Default cache directory not found, and system failed to create it.  Path: "
                        + DEFAULT_CACHE_DIRECTORY.getAbsolutePath()
                        + ".");
            }
            if (DEFAULT_CACHE_FILE.exists()
                    && DEFAULT_CACHE_FILE.length() >= 1) {
                File backupFile
                        = File.createTempFile(DEFAULT_CACHE_FILE.getName(),
                                ".backup", DEFAULT_CACHE_DIRECTORY);
                backupFile.delete();
                DEFAULT_CACHE_FILE.renameTo(backupFile);
                FileInputStream fis = null;
                ObjectInputStream ois = null;
                FileOutputStream fos = null;
                ObjectOutputStream oos = null;
                try {
                    boolean objectWasDeleted = false;
                    fis = new FileInputStream(backupFile);
                    ois = new ObjectInputStream(fis);
                    fos = new FileOutputStream(DEFAULT_CACHE_FILE);
                    oos = new ObjectOutputStream(fos);
                    while (true) {
                        Object obj = null;
                        try {
                            obj = ois.readObject();
                        } catch (EOFException e) {
                            getLog().info("End of cache file encountered.  This is normal behavior.");
                            break;
                        }
                        SerIdent curSecret
                                = obj != null && objectToDelete.getClass().equals(obj.getClass())
                                ? (SerIdent) obj : null;
                        if (curSecret != null
                                && curSecret.getId().equals(objectToDelete.getId())) {
                            // Omitting the object constitutes deletion.
                            objectWasDeleted = true;
                        } else {
                            oos.writeObject(obj);
                        }
                    }
                } finally {
                    IOUtil.closeSafely(ois, fis, oos, fos);
                }
                backupFile.delete();
            }
            return true;
        }
    }

    /**
     * @param <SerIdent>
     * @param iface Class. Required.
     * @return List of all records from the cache that implement or inherit the
     * specified interface.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <SerIdent extends Serializable & Identifiable<?>> List<SerIdent> loadAllByInterface(Class<SerIdent> iface) throws IOException,
            ClassNotFoundException {
        List<SerIdent> retval = new ArrayList<SerIdent>();
        if (DEFAULT_CACHE_FILE.exists() && DEFAULT_CACHE_FILE.length() >= 1) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(DEFAULT_CACHE_FILE);
                ois = new ObjectInputStream(fis);
                while (true) {
                    Object obj = null;
                    try {
                        obj = ois.readObject();
                    } catch (EOFException e) {
                        getLog().info("End of server secrets file encountered.  This is normal behavior.");
                        break;
                    }
                    if (obj != null
                            && iface.isAssignableFrom(obj.getClass())) {
                        retval.add((SerIdent) obj);
                    }
                }
            } finally {
                IOUtil.closeSafely(ois, fis);
            }
            getLog().info("Returning " + retval.size()
                    + " instances of " + iface.getSimpleName() + ".");
        } else {
            getLog().severe("Cache file not found or is empty: "
                    + DEFAULT_CACHE_FILE.getAbsolutePath()
                    + " interface.");
        }
        return retval;
    }

    /**
     * @param <SerIdent>
     * @param clazz Class. Required.
     * @return List of all records from the cache whose class exactly matches
     * specified class.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <SerIdent extends Serializable & Identifiable<?>> List<SerIdent> loadAllByClass(Class<SerIdent> clazz) throws IOException,
            ClassNotFoundException {
        List<SerIdent> retval = new ArrayList<SerIdent>();
        if (DEFAULT_CACHE_FILE.exists() && DEFAULT_CACHE_FILE.length() >= 1) {
            FileInputStream fis = null;
            ObjectInputStream ois = null;
            try {
                fis = new FileInputStream(DEFAULT_CACHE_FILE);
                ois = new ObjectInputStream(fis);
                while (true) {
                    Object obj = null;
                    try {
                        obj = ois.readObject();
                    } catch (EOFException e) {
                        getLog().info("End of server secrets file encountered.  This is normal behavior.");
                        break;
                    }
                    if (obj != null && clazz.equals(obj.getClass())) {
                        retval.add((SerIdent) obj);
                    }
                }
            } finally {
                IOUtil.closeSafely(ois, fis);
            }
            getLog().info("Returning " + retval.size()
                    + " instances of " + clazz.getSimpleName() + ".");
        } else {
            getLog().severe("Cache file not found or is empty: "
                    + DEFAULT_CACHE_FILE.getAbsolutePath() + ".");
        }
        return retval;
    }
}
