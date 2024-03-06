/*
 *  Copyright (C) 2011 Eros Zanchetta <eros@sslmit.unibo.it>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package core;

/**
 *
 * @author Eros Zanchetta <eros@sslmit.unibo.it>
 */
public class About {

    private static final Double   VERSION           = 1.0;
    private static final String   AUTHOR            = "Eros Zanchetta";
    private static final String   PROGRAM_NAME      = "any2utf8";
    private static final String   PROGRAM_NAME_CLI  = "any2utf8-cli";
    private static final String   PROGRAM_URL        = "http://bootcat.dipintra.it/redirects/redirect.php?targetPage=any2utf8_home";
    private static final String   HELP_URL           = "http://bootcat.dipintra.it/redirects/redirect.php?targetPage=any2utf8_manual";
    private static final int      COPYRIGHT_FIRST    = 2011;
    private static final int      COPYRIGHT_LATEST   = 2024;

    public enum Platform {
        LINUX,
        MAC,
        WINDOWS,
        UNKNOWN
    }

    public static String getHelpUrl() {
        return HELP_URL;
    }

    public static String getAuthor() {
        return AUTHOR;
    }

    public static String getProgramUrl() {
        return PROGRAM_URL;
    }

    public static int getCopyrightFirst() {
        return COPYRIGHT_FIRST;
    }

    public static int getCopyrightLast() {
        return COPYRIGHT_LATEST;
    }

    public static String getProgramName() {
        return PROGRAM_NAME;
    }

    public static String getProgramNameCli() {
        return PROGRAM_NAME_CLI;
    }

    public static Double getVersion() {
        return VERSION;
    }
}
