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

import android.provider.BaseColumns;



public final class FeedReaderContract implements BaseColumns {
    //make construct private so nobody can instantiate the class
    private FeedReaderContract() {
    }

    public static class SecRing implements BaseColumns {
        public static final String TABLE_NAME = "secring";
        public static final String TB_COL2_EMAIL = "email";
        public static final String TB_COL3_SECKEY = "seckey";
    }

    public static class PubRing implements BaseColumns {
        public static final String TABLE_NAME = "pubring";
        public static final String TB_COL1_EMAIL = "email";
        public static final String TB_COL2_PUBKEY = "pubkey";
    }


    //SQL queries
    public static final String CREATE_TABLE_SECRING = "CREATE TABLE " + SecRing.TABLE_NAME + " ("
            + SecRing.TB_COL2_EMAIL + " TEXT,"
            + SecRing.TB_COL3_SECKEY + " BOLB" +
            " )";
    public static final String CREATE_TABLE_PUBRING = "CREATE TABLE " + PubRing.TABLE_NAME + "( " +
            PubRing.TB_COL1_EMAIL + " TEXT," +
            PubRing.TB_COL2_PUBKEY + " TEXT " +
            " )";


}
