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
 * <p>
 * A <code>DelegatingReadableByteChannel</code> overrides all methods of
 * <code>ReadableByteChannel</code> and delegates their execution to the wrapped
 * <code>ReadableByteChannel</code>. The wrapped <code>ReadableByteChannel</code>
 * is always obtained via {@link #getDelegate()} method.
 * </p>
 * <p>
 * This class is not thread safe.
 * </p>
 *
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
public class DelegatingReadableByteChannel extends DelegatingChannel implements ReadableByteChannel {

    /**
     * Creates a <code>DelegatingReadableByteChannel</code> that wraps passed readable byte channel.
     *
     * @param delegate the readable byte channel to be wrapped
     * @throws <code>IllegalArgumentException</code> if parameter is null
     */
    public DelegatingReadableByteChannel( final ReadableByteChannel delegate ) {
        super( delegate );
    }

    /**
     * Returns wrapped channel.
     */
    @Override
    protected ReadableByteChannel getDelegate() {
        return ( ReadableByteChannel ) super.getDelegate();
    }

    /**
     * Delegates the call to the wrapped channel.
     */
    @Override
    public int read( final ByteBuffer dst ) throws IOException {
        return getDelegate().read( dst );
    }
}
