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
import java.nio.channels.ReadableByteChannel;

/**
 * <P>
 * A <code>PushbackReadableByteChannel</code> allows one or more bytes to be pushed back to the channel.
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
public final class PushbackReadableByteChannel extends DelegatingReadableByteChannel {

    private final byte[] pushBuffer;

    private int pushPosition;

    private boolean closed;

    /**
     * Creates a <code>PushBackReadableByteChannel</code> that wraps passed
     * readable channel with a one-byte pushback buffer size.
     * 
     * @param delegate readable channel to operate upon
     */
    public PushbackReadableByteChannel( final ReadableByteChannel delegate ) {
        this( delegate, 1 );
    }

    /**
     * Creates a <code>PushBackReadableByteChannel</code> that wraps passed
     * readable channel.
     * 
     * @param delegate readable channel to operate upon
     * @param size fixed push back buffer size
     */
    public PushbackReadableByteChannel( final ReadableByteChannel delegate, final int size ) {
        // ensure preconditions
        super( delegate );
        if ( size <= 0 ) {
            throw new IllegalArgumentException( "Push back buffer size must be positive" );
        }
        // initialize
        pushBuffer = new byte[ size ];
        pushPosition = pushBuffer.length;
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
        if ( pushPosition == 0 ) {
            throw new IOException( "Push back buffer is full" );
        }
        // the implementation
        pushBuffer[ --pushPosition ] = ( byte ) b;
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
        if ( buffer == null ) {
            throw new IllegalArgumentException( "buffer cannot be null" );
        }
        // the implementation
        unread( buffer, 0, buffer.length );
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
        if ( buffer == null ) {
            throw new IllegalArgumentException( "buffer cannot be null" );
        }
        // the implementation
        final int length = buffer.limit();
        if ( length == 0 ) {
            return;
        }
        if ( length > pushPosition ) {
            throw new IOException( "Push back buffer is full" );
        }
        pushPosition -= length;
        buffer.get( pushBuffer, pushPosition, length );
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
        if ( buffer == null ) {
            throw new IllegalArgumentException( "buffer cannot be null" );
        }
        if ( offset < 0 ) {
            throw new IllegalArgumentException( "offset must be positive" );
        }
        if ( length < 0 ) {
            throw new IllegalArgumentException( "length must be positive" );
        }
        if ( length > ( buffer.length - offset ) ) {
            throw new IllegalArgumentException( "length must be less or equal to free space available in the buffer" );
        }
        // method implementation
        if ( length == 0 ) {
            return;
        }
        if ( length > pushPosition ) {
            throw new IOException( "Push back buffer is full" );
        }
        pushPosition -= length;
        System.arraycopy( buffer, offset, pushBuffer, pushPosition, length );
    }

    /**
     * Delegates the call to the wrapped channel.
     */
    @Override
    public int read( final ByteBuffer buffer ) throws IOException {
        // ensure preconditions
        ensureOpen();
        if ( buffer == null ) {
            throw new IllegalArgumentException( "buffer cannot be null" );
        }
        // method implementation
        if ( buffer.limit() == 0 ) {
            return 0;
        }
        int returnValue = 0;
        // process pushBuffer first
        if ( !isPushBackBufferEmpty() ) {
            final int count = Math.min( buffer.limit(), getPushBackBufferSize() );
            buffer.put( pushBuffer, pushPosition, count );
            // update variables accordingly
            pushPosition += count;
            returnValue = count;
        }
        if ( buffer.limit() == 0 ) {
            // pushBuffer served method request completely
            return returnValue;
        }
        // process delegate last
        final int count = super.read( buffer );
        if ( count == -1 ) {
            return ( returnValue == 0 ) ? -1 : returnValue;
        } else {
            return returnValue + count;
        }
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

    private boolean isPushBackBufferEmpty() {
        return pushPosition == pushBuffer.length;
    }

    private int getPushBackBufferSize() {
        return pushBuffer.length - pushPosition;
    }
}
