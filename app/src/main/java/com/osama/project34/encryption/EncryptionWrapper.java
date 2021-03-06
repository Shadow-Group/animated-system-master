/*
 * Copyright (c) 2017. slownet5
 *  This file is part of RootPGPExplorer also known as CryptoFM
 *
 *       RootPGPExplorer a is free software: you can redistribute it and/or modify
 *       it under the terms of the GNU General Public License as published by
 *       the Free Software Foundation, either version 3 of the License, or
 *       (at your option) any later version.
 *
 *       RootPGPExplorer is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with RootPGPExplorer.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.osama.project34.encryption;

import android.util.Log;

import java.io.File;
import java.io.InputStream;



public class EncryptionWrapper {
    private static final int TWO_MB = 2097152;

    public static boolean encryptFile(File inputFile,
                                      File outputFile,
                                      File keyFile,
                                      Boolean integrityCheck)
            throws Exception {
        //check file size
        Log.d("bullhead", "encryptFile: size/length is: " + inputFile.length());
        if (inputFile.length() > TWO_MB) {
            Log.d("bullhead", "encryptFile: file size is larger than 2MB using large file handler ");
            EncryptionManagement enc = new EncryptionManagement();
            return enc.encryptFile(outputFile, inputFile, keyFile, integrityCheck);
        } else {
            Log.d("bullhead", "encryptFile: ");
            EncryptionSmallFileProcessor enc = new EncryptionSmallFileProcessor();
            return enc.encryptFile(inputFile, outputFile, keyFile, integrityCheck);
        }

    }

    public static boolean decryptFile(File inputFile,
                                      File outputFile,
                                      File pubKey,
                                      InputStream secKeyFile,
                                      char[] pass
    ) throws Exception {
        Log.d("wrapper", "decryptFile: size/length is: " + inputFile.length());
        if (inputFile.length() > TWO_MB) {
            EncryptionManagement dec = new EncryptionManagement();
            return dec.decryptFile(inputFile, outputFile, pubKey, secKeyFile, pass);
        } else {
            EncryptionSmallFileProcessor dec = new EncryptionSmallFileProcessor();
            return dec.decryptFile(inputFile, outputFile, pubKey, secKeyFile, pass);
        }
    }
}
