/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.network.pe;

import net.redstonelamp.network.itf.AdvancedNetworkInterface;

import java.net.SocketAddress;

/**
 * Extension from AdvancedNetworkInterface, has internal methods used by PEProtocol
 *
 * @author RedstoneLamp Team
 */
public interface PEInterface extends AdvancedNetworkInterface{
    void _internalClose(SocketAddress address, String reason);
}
