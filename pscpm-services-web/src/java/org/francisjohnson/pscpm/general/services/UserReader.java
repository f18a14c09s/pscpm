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
package org.francisjohnson.pscpm.general.services;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import org.francisjohnson.pscpm.security.data.User;

/**
 *
 * @author fjohnson
 */
//@Consumes("multipart/form-data")
public class UserReader
//        implements MessageBodyReader<User>
{

    /**
     * Ascertain if the MessageBodyReader can produce an instance of a
     * particular type.
     *
     * @param type
     * @param genericType
     * @param annotations
     * @param mediaType
     * @return
     */
//    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return false;
    }

    /**
     * Read a type from the InputStream.
     *
     * @param type
     * @param genericType
     * @param annotations
     * @param mediaType
     * @param httpHeaders
     * @param entityStream
     * @return
     */
//    @Override
    public User readFrom(Class<User> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) {
        return null;
    }
}
