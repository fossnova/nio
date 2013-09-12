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
 * An <code>NullReadableByteChannel</code> does nothing. It is always at the EOF position. It
 * never throws <code>IOException</code>.
 * <p>
 * This class is thread safe.
 * </p>
 *
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
public final class NullReadableByteChannel implements ReadableByteChannel {

    private static final NullReadableByteChannel INSTANCE = new NullReadableByteChannel();

    private NullReadableByteChannel() {
    }

    /**
     * Returns <code>NullReadableByteChannel</code> singleton instance.
     */
    public static NullReadableByteChannel getInstance() {
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
     * Does nothing.
     */
    @Override
    public void close() throws IOException {
    }

    /**
     * Does nothing.
     */
    @Override
    public int read( final ByteBuffer buffer ) throws IOException {
        return -1;
    }
}
