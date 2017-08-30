/*
 * Copyright (c) 2012-2017, FOSS Nova Software foundation (FNSF),
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
import java.nio.channels.WritableByteChannel;

/**
 * A <code>BrokenWritableByteChannel</code> always throws <code>IOException</code>.
 * <p>
 * This class is thread safe.
 * </p>
 *
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
public final class BrokenWritableByteChannel implements WritableByteChannel {

    private static final BrokenWritableByteChannel INSTANCE = new BrokenWritableByteChannel();

    private final IOException exception;

    private BrokenWritableByteChannel() {
        exception = new IOException( "Broken writable channel" );
    }

    /**
     * Returns <code>BrokenWritableByteChannel</code> singleton instance.
     */
    public static BrokenWritableByteChannel getInstance() {
        return INSTANCE;
    }

    /**
     * Always returns true.
     */
    @Override
    public boolean isOpen() {
        return true;
    }

    /**
     * Throws IOException.
     */
    @Override
    public void close() throws IOException {
        throw exception;
    }

    /**
     * Throws IOException.
     */
    @Override
    public int write( final ByteBuffer buffer ) throws IOException {
        throw exception;
    }
}
