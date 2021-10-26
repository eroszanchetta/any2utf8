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

import com.ibm.icu.text.CharsetDetector;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eros Zanchetta <eros@sslmit.unibo.it>
 */
public class Converter {
    
    private final ArrayList<WarningCode> warning = new ArrayList<WarningCode>();
    
    private String log;
    private String errorLog;
    private String warningLog;
    
    public Converter() {
        log          = "";
        errorLog     = "";
        warningLog   = "";        
    }
    
    public ErrorCode convertDir(File inputDir, File outputDir, boolean addBom) {
        ErrorCode errorCode = dirPathVerifier(inputDir, outputDir);
        
        if (!errorCode.equals(ErrorCode.OK)) return errorCode;

        for (String fileName : inputDir.list()) {
            File input  = new File(inputDir + File.separator + fileName);
            File output = new File(outputDir + File.separator + fileName);

            if (input.isDirectory()) {
                output.mkdir();
                convertDir(input, output, addBom);
                continue;
            }

            convertFile(input, output, addBom);
        }
        
        return errorCode;
    }

    public String getErrorLog() {
        return errorLog;
    }

    public String getLog() {
        return log;
    }

    public String getWarningLog() {
        return warningLog;
    }
    
    public ArrayList<WarningCode> getWarning() {
        return warning;
    }

    private void logError(String s) {
        errorLog += "Error: " + s + System.getProperty("line.separator");
    }

    private void logWarning(String s) {
        warningLog += "Warning: " + s + System.getProperty("line.separator");
    }
    
    private void logMessage(String s) {
        log += s + System.getProperty("line.separator");
    }    
    
    /**
     * This method verifies that all parameters for a directory conversion are correct
     * @param inputDir
     * @param outputDir
     * @return
     */
    public ErrorCode dirPathVerifier(File inputDir, File outputDir) {
        // see if input dir exists and is readable
        if (!inputDir.exists() || !inputDir.canRead()) {
            logError("input directory \"" + inputDir + "\" does not exist or is not readable.");
            return ErrorCode.INVALID_INPUT_DIR;
        }

        // see if output dir exists and is writeable
        if (!outputDir.exists() || !outputDir.canWrite()) {
            logError("output directory \"" + outputDir + "\" does not exist or is not writeable.");
            return ErrorCode.INVALID_OUTPUT_DIR;
        }

        // output directory must be empty
        if (outputDir.list().length > 0) {
            logError("output directory \"" + outputDir + "\" is not empty, the output directory MUST be empty.");
            return ErrorCode.NON_EMPTY_OUTPUT_DIR;
        }

        // issue a warning if directory is empty (this is not an error since
        // it can happen if the program is performing a recursive conversion)
        if (inputDir.list().length == 0) {
            logWarning("input directory \"" + inputDir + "\" is empty, skipping it.");
            warning.add(WarningCode.EMPTY_INPUT_DIR);
        }
            
        return ErrorCode.OK;
    }

    /**
     * This method verifies that all parameters for a file conversion are correct
     * @param inputFile
     * @param outputFile
     * @return
     */
    public ErrorCode filePathVerifier(File inputFile, File outputFile) {        
        // see if input file exists and is readable
        if (!inputFile.exists() || !inputFile.canRead()) {
            logError("input file " + inputFile + " does not exist or is not readable.");
            return ErrorCode.INVALID_INPUT_FILE;
        }

        // see if output file already exists
        if (outputFile.exists()) {
            logError("output file " + outputFile + " already exists.");
            return ErrorCode.OUTPUT_FILE_EXISTS;
        }

        // see if directory that is to contain output file exists and is writeable
        if (!outputFile.getParentFile().isDirectory() || !outputFile.getParentFile().canWrite()) {
            logError("output directory " + outputFile.getParentFile() + " does not exist or is not writeable.");
            return ErrorCode.INVALID_OUTPUT_DIR;
        }

        try {
            // see if output file can be created
            if (!outputFile.createNewFile()) {
                logError("output file " + outputFile + " could not be created.");
                return ErrorCode.INVALID_OUTPUT_FILE;
            }
            outputFile.delete();
        }
        catch (IOException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }

        return ErrorCode.OK;
    }

    public ErrorCode convertFile(File inputFile, File outputFile, boolean addBom) {

        ErrorCode errorCode;
        
        // verify correctness of file parameters
        errorCode = filePathVerifier(inputFile, outputFile);
        if (!errorCode.equals(ErrorCode.OK)) return errorCode;

        try {        
            // build a BufferedInputStream to read file
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inputFile));

            // detect encoding
            CharsetDetector detector = new CharsetDetector();
            detector.setText(bis);

            Charset sourceCharset = null;

            try {
                if (detector.detect() == null) {
                    logWarning("unable to detect source file encoding, skipping " + inputFile);
                    warning.add(WarningCode.ENCODING_NOT_DETECTED);
                    return ErrorCode.OK;
                }

                sourceCharset = Charset.forName(detector.detect().getName());
            }
            catch (UnsupportedCharsetException ex) {
                logWarning("\"" + inputFile + "\" uses an unsupported encoding, skipping.");
                warning.add(WarningCode.ENCODING_NOT_SUPPORTED);
                return ErrorCode.OK;
            }

            logMessage("Encoding of file " + inputFile + " appears to be " + sourceCharset.displayName() + ".");

            // get file length so we can check a few things and create a
            // byte array large enough to accomodate its content
            long length = inputFile.length();

            // file is empty
            if (length == 0) {
                logWarning("file " + inputFile.getName() + " is empty, skipping it.");
                return ErrorCode.FILE_IS_EMPTY;
            }            
            
            // file is too large
            if (length > Integer.MAX_VALUE) {
                logError("file " + inputFile.getName() + " is too large.");
                return ErrorCode.FILE_TOO_LARGE;
            }
            
            // Create the byte array to hold the data
            byte[] bytes = new byte[(int)length];

            // reset inputstream
            bis.reset();

            // Read in the bytes
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length && (numRead=bis.read(bytes, offset, bytes.length-offset)) >= 0) {
                offset += numRead;
            }

            // make sure all the bytes have been read in
            if (offset < bytes.length) {
                logError("could not completely read file " + inputFile.getName());
                return ErrorCode.INCOMPLETE_FILE_READ;
            }

            // Close the input stream
            bis.close();

            // create a decoder
            CharsetDecoder decoder = sourceCharset.newDecoder();

            // create a bytestream from the bytes read above and decode it
            CharBuffer internalCbuf = decoder.decode(ByteBuffer.wrap(bytes));

            // since the conversion mechanism apparently adds null characters at the end of the array,
            // I had to add a bit of messy code to remove trailing nulls, here goes:

            // first convert the bytestream into an ArrayList
            ArrayList<Character> charArrayList = new ArrayList<Character>();

            // read the array into the arraylist
            for (char c : internalCbuf.array()) charArrayList.add(c);

            // if first character is the byte-order-mark, strip it (we'll add it back later)
            if (charArrayList.get(0).equals('\ufeff')) charArrayList.remove(0);

            // now starting at the end of the ArrayList, work your way backwards and remove
            // all null characters you encounter until you find a non-null one
            while (charArrayList.get(charArrayList.size()-1) == 0)
                charArrayList.remove(charArrayList.size()-1);

            // create an OutPutStream
            FileOutputStream fos = new FileOutputStream(outputFile);
            Writer out = new OutputStreamWriter(fos, "UTF8");

            // write BOM
            if (addBom) {
                out.write('\ufeff');
            }

            // write all chars in the array to output stream
            for (char c : charArrayList) out.write(c);

            // flush and close output stream
            out.close();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (Exception ex) {
            Logger.getLogger(Converter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ErrorCode.OK;
    }
}
