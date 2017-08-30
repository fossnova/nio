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
import java.nio.channels.ByteChannel;

/**
 * <p>
 * A <code>DelegatingByteChannel</code> overrides all methods of
 * <code>ByteChannel</code> and delegates their execution to the wrapped
 * <code>ByteChannel</code>. The wrapped <code>ByteChannel</code>
 * is always obtained via {@link #getDelegate()} method.
 * </p>
 * <p>
 * This class is not thread safe.
 * </p>
 *
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
public class DelegatingByteChannel extends DelegatingChannel implements ByteChannel {

    /**
     * Creates a <code>DelegatingByteChannel</code> that wraps passed byte channel.
     *
     * @param delegate the byte channel to be wrapped
     * @throws <code>IllegalArgumentException</code> if parameter is null
     */
    public DelegatingByteChannel( final ByteChannel delegate ) {
        super( delegate );
    }

    /**
     * Returns wrapped channel.
     */
    @Override
    protected ByteChannel getDelegate() {
        return ( ByteChannel ) super.getDelegate();
    }

    /**
     * Delegates the call to the wrapped channel.
     */
    @Override
    public int read( final ByteBuffer dst ) throws IOException {
        return getDelegate().read( dst );
    }

    /**
     * Delegates the call to the wrapped channel.
     */
    @Override
    public int write( final ByteBuffer src ) throws IOException {
        return getDelegate().write( src );
    }
}
