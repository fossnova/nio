/*
 * Copyright (c) 2013, FOSS Nova Software foundation (FNSF),
 * and individual contributors as indicated by the @author tags.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.fossnova.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

/**
 * <P>
 * A <code>PushbackByteChannel</code> allows one or more bytes to be pushed back to the channel.
 * If there are some pushed back bytes in the channel, these are returned
 * first when {@link #read(ByteBuffer)} method is called.
 * If there are no pushed back bytes then {@link #read(ByteBuffer)} method
 * call is delegated to wrapped channel.
 * </P>
 * <P>
 * The push back buffer has fixed length. Any attempt to push back more bytes
 * than buffer length will cause <B>java.io.IOException</B>.
 * </P>
 * <p>
 * This class is not thread safe.
 * </p>
 *
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
public final class PushbackByteChannel extends DelegatingByteChannel implements ByteChannel {

    private final PushbackReadableByteChannel readDelegate;

    private boolean closed;

    /**
     * Creates a <code>PushBackByteChannel</code> that wraps passed
     * channel with a one-byte pushback buffer size.
     * 
     * @param delegate readable channel to operate upon
     */
    public PushbackByteChannel( final ByteChannel delegate ) {
        this( delegate, 1 );
    }

    /**
     * Creates a <code>PushBackByteChannel</code> that wraps passed channel.
     * 
     * @param delegate channel to operate upon
     * @param size fixed push back buffer size
     */
    public PushbackByteChannel( final ByteChannel delegate, final int size ) {
        // ensure preconditions
        super( delegate );
        // initialize
        readDelegate = new PushbackReadableByteChannel( delegate, size );
    }

    /**
     * Push back one byte so it is visible to next read attempts.
     *
     * @param b byte to be pushed back
     * @throws IOException if some I/O error occurs
     */
    public void unread( final int b ) throws IOException {
        // ensure preconditions
        ensureOpen();
        // the implementation
        readDelegate.unread( b );
    }

    /**
     * Push back all bytes from the buffer so these are visible to next read attempts.
     *
     * @param buffer bytes to be pushed back
     * @throws IOException if some I/O error occurs
     */
    public void unread( final byte[] buffer ) throws IOException {
        // ensure preconditions
        ensureOpen();
        // the implementation
        readDelegate.unread( buffer );
    }

    /**
     * Push back all bytes from the buffer so these are visible to next read attempts.
     *
     * @param buffer bytes to be pushed back
     * @throws IOException if some I/O error occurs
     */
    public void unread( final ByteBuffer buffer ) throws IOException {
        // ensure preconditions
        ensureOpen();
        // the implementation
        readDelegate.unread( buffer );
    }

    /**
     * Push back <B>length</B> bytes from this buffer starting from specified <B>offset</B> position
     * so these are visible to next read attempts.
     *
     * @param buffer holding bytes to be pushed back
     * @param offset to start copy from
     * @param length count of bytes to process
     * @throws IOException if some I/O error occurs
     */
    public void unread( final byte[] buffer, final int offset, final int length ) throws IOException {
        // ensure preconditions
        ensureOpen();
        // the implementation
        readDelegate.unread( buffer, offset, length );
    }

    /**
     * Delegates the call to the wrapped channel.
     */
    @Override
    public int read( final ByteBuffer buffer ) throws IOException {
        // ensure preconditions
        ensureOpen();
        // the implementation
        return readDelegate.read( buffer );
    }

    /**
     * Delegates the call to the wrapped channel.
     */
    @Override
    public int write( final ByteBuffer src ) throws IOException {
        // ensure preconditions
        ensureOpen();
        // the implementation
        return getDelegate().write( src );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if ( !closed ) {
            closed = true;
            super.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen() {
        return !closed;
    }

    void ensureOpen() {
        if ( closed ) {
            throw new IllegalStateException( "Channel is closed" );
        }
    }
}
