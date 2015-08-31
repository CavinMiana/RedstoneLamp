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
package net.redstonelamp.network.pc;

/**
 * This "interface" contains the MCPC network constants.
 *
 * @author RedstoneLamp Team
 */
public interface PCNetworkConst{
    /**
     * The Minecraft version this software implements.
     */
    public static final String MC_VERSION = "15w35e";

    /**
     * The Minecraft network protocol version this software implements.
     */
    public static final int MC_PROTOCOL = 66;

    //Handshake packets
    public static final int HANDSHAKE_HANDSHAKE = 0x00;

    //Status packets
    public static final int STATUS_REQUEST = 0x00;
    public static final int STATUS_RESPONSE = 0x00;
    public static final int STATUS_PING = 0x01;
    public static final int STATUS_PONG = 0x01;

    //Login packets
    public static final int LOGIN_LOGIN_START = 0x00;
    public static final int LOGIN_ENCRYPTION_REQUEST = 0x01;
    public static final int LOGIN_ENCRYPTION_RESPONSE = 0x01;
    public static final int LOGIN_DISCONNECT = 0x00;
    public static final int LOGIN_LOGIN_SUCCESS = 0x02;
    public static final int LOGIN_SET_COMPRESSION = 0x03;

    //Play packets
    public static final int PLAY_KEEP_ALIVE = 0x00;
    public static final int PLAY_JOIN_GAME = 0x01;
    public static final int PLAY_SERVERBOUND_CHAT_MESSAGE = 0x01;
    public static final int PLAY_CLIENTBOUND_CHAT_MESSAGE = 0x02;
    public static final int PLAY_TIME_UPDATE = 0x03;
    public static final int PLAY_ENTITY_EQUIPMENT = 0x04;
    public static final int PLAY_SPAWN_POSITION = 0x05;
    public static final int PLAY_UPDATE_HEALTH = 0x06;
    public static final int PLAY_SERVERBOUND_PLAYER_POSITION_LOOK = 0x06;
    public static final int PLAY_RESPAWN = 0x07;
    public static final int PLAY_CLIENTBOUND_PLAYER_POSITION_LOOK = 0x08;

    public static final int PLAY_SERVERBOUND_PLAYER_ABILITIES = 0x14;

    public static final int PLAY_CHUNK_DATA = 0x21;

    public static final int PLAY_CLIENTBOUND_PLAYER_ABILITIES = 0x39;
}
