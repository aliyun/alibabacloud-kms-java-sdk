package com.aliyun.kms.kms20160120.utils;

public interface Constants {
    int GCM_IV_LENGTH = 12;
    int EKT_ID_LENGTH = 36;
    String DIGEST_MESSAGE_TYPE = "DIGEST";
    String KMS_KEY_PAIR_AES_256 = "AES_256";
    String KMS_KEY_PAIR_AES_128 = "AES_128";
    String MIGRATION_KEY_VERSION_ID_KEY = "x-kms-migrationkeyversionid";
    int NUMBER_OF_BYTES_AES_256 = 32;
    int NUMBER_OF_BYTES_AES_128 = 16;
    byte MAGIC_NUM = '$';
    int MAGIC_NUM_LENGTH = 1;
    int CIPHER_VER_AND_PADDING_MODE_LENGTH = 1;
    int ALGORITHM_LENGTH = 1;
    byte CIPHER_VER = 0;
    byte ALG_AES_GCM = 2;
}
