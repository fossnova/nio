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
import java.nio.channels.Channel;

/**
 * <p>
 * A <code>DelegatingChannel</code> overrides all methods of
 * <code>Channel</code> and delegates their execution to the wrapped
 * <code>Channel</code>. The wrapped <code>Channel</code>
 * is always obtained via {@link #getDelegate()} method.
 * </p>
 * <p>
 * This class is not thread safe.
 * </p>
 *
 * @author <a href="mailto:opalka dot richard at gmail dot com">Richard Opalka</a>
 */
class DelegatingChannel implements Channel {

    private final Channel delegate;

    /**
     * Creates a <code>DelegatingChannel</code> that wraps passed channel.
     *
     * @param delegate the channel to be wrapped
     * @throws <code>IllegalArgumentException</code> if parameter is null
     */
    public DelegatingChannel( final Channel delegate ) {
        if ( delegate == null ) {
            throw new IllegalArgumentException( "Channel cannot be null" );
        }
        this.delegate = delegate;
    }

    /**
     * Returns wrapped channel.
     */
    protected Channel getDelegate() {
        return delegate;
    }

    /**
     * Delegates the call to the wrapped channel.
     */
    @Override
    public boolean isOpen() {
        return getDelegate().isOpen();
    }

    /**
     * Delegates the call to the wrapped channel.
     */
    @Override
    public void close() throws IOException {
        getDelegate().close();
    }
}
