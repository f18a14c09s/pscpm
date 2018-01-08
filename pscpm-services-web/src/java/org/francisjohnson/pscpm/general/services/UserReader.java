/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
