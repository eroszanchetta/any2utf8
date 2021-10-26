/*
 * Copyright (C) 2011 Eros Zanchetta <eros@sslmit.unibo.it>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package core;

/**
 *
 * @author Eros Zanchetta <eros@sslmit.unibo.it>
 */
public enum ErrorCode {
    INVALID_INPUT_DIR,
    INVALID_INPUT_FILE,
    INVALID_OUTPUT_DIR,
    INVALID_OUTPUT_FILE,
    NON_EMPTY_OUTPUT_DIR,
    OUTPUT_FILE_EXISTS,
    INCOMPLETE_FILE_READ,
    FILE_TOO_LARGE,
    FILE_IS_EMPTY,
    OK,
}
