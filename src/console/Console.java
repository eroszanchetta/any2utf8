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

package console;

import core.About;
import core.Converter;
import core.ErrorCode;
import core.Utils;
import java.io.File;

/**
 *
 * @author Eros Zanchetta <eros@sslmit.unibo.it>
 */
public class Console {

    private static final boolean ADDBOM = false;
    
    public static void main(String[] args) {
        Console console = new Console();
        console.go(args);
    }

    private void go(String[] args) {
        Converter converter = new Converter();

        if (args.length == 1 && (args[0].equals("-help") || args[0].equals("--help"))) {
            printExtendedInfo();
            System.exit(0);
        }

        try {
            File input  = new File(args[0]);
            File output = new File(args[1]);

            if (input.isDirectory() && output.isDirectory()) {
                if (!converter.convertDir(input, output, ADDBOM).equals(ErrorCode.OK)) {
                    System.out.print(converter.getLog());
                    System.out.print(converter.getWarningLog());
                    System.out.print(converter.getErrorLog());
                    System.exit(1);
                }                   
            }

            else if(input.isDirectory() && output.isFile()) {
                System.err.println("Error: if you specify a directory as input you must also specify a " +
                    "directory as output (the output you specified appears to be a file).");
                System.exit(1);
            }

            // in this case create a file with the same name in destination directory
            else if(input.isFile() && output.isDirectory()) {
                File outputFile = new File(output + File.separator + input.getName());
                converter.convertFile(input, outputFile, ADDBOM);
            }

            else if(input.isFile() && output.isFile()) {
                System.err.println("Error: " + output + " already exists.");
                System.exit(1);
            }

            else if(input.isFile() && !output.isFile()) {
                converter.convertFile(input, output, ADDBOM);
            }

            else {
                System.err.println("Error: something is wrong with the parameters you specified.");
                Console.printSyntax();
            }
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            System.err.println("Error: missing parameters, try running the program with -help");
            System.err.println();
            Console.printSyntax();
            System.exit(1);
        }
        
        System.out.print(converter.getLog());
        System.err.print(converter.getWarningLog());
        System.err.print(converter.getErrorLog());
        System.out.println("Conversion completed successfully.");
    }

    public static void printSyntax() {
        About.Platform platform;
        
        // try to determine platform
        String os = System.getProperty("os.name").toLowerCase();

        if (os.startsWith("windows")) platform = About.Platform.WINDOWS;
        else if (os.startsWith("mac")) platform = About.Platform.MAC;
        else if (os.startsWith("linux")) platform = About.Platform.LINUX;
        else platform = About.Platform.UNKNOWN;

        String commandString = null;

        switch (platform) {
            default:
            case LINUX:
            case MAC:
            case UNKNOWN:
            case WINDOWS:
                commandString = "java -jar " + About.getProgramNameCli() + ".jar";
                break;
        }

        System.err.println(About.getProgramNameCli() + " version " + About.getVersion());
        System.err.println();
        System.err.println(Utils.wordWrap("This tool automatically converts text files in any encoding to UTF8"));
        System.err.println();
        System.err.println("Usage: " + commandString + " inputFile outputFile");
        System.err.println("  or   " + commandString + " inputFile outputDir");
        System.err.println("  or   " + commandString + " inputDir outputDir");
    }

    public static void printExtendedInfo() {
        printSyntax();
        
        System.err.println();
        System.err.println(Utils.wordWrap("You can specify either files or directories as " +
            "input/output parameters. If you specify a directory as input, all files contained " +
            "in it and its subdirectories will be converted to UTF-8 and written to the output " +
            "directory (the directory tree of input directory will be re-created). " +
            "Unrecognised files (e.g. binary files or text files using unrecognised encodings) " +
            "will be skipped and warnings will be printed to stderr."));
        System.err.println();
        System.err.println(Utils.wordWrap("The program will not overwrite existing files and will "+
            "refuse to work if a non-empty outputDir is specified."));
		System.err.println();
        System.err.println(Utils.wordWrap("The detected character encoding for each file is " +
            "printed to stdout."));
        System.err.println();
        System.err.println("Copyright (C) " + About.getCopyrightFirst() + "-" +
                About.getCopyrightLast() + " by " + About.getAuthor() + ".");
        System.err.println();
        System.err.println(Utils.wordWrap("This program is free software: you can redistribute " +
            "it and/or modify it under the terms of the GNU General Public License as published by " +
            "the Free Software Foundation, either version 3 of the License, or (at your option) " +
            "any later version."));

    }

}
