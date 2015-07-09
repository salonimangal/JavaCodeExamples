/* Copyright (c) 2001 by McKesson. All rights reserved.
 * Digital Focus, Inc. retains the right to use this
 * software in applications and derivative works, so long
 * as such use retains this copyright notice.
 *
 * DIGITAL FOCUS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT
 * THE SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. DIGITAL FOCUS SHALL NOT BE LIABLE FOR
 * ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 * MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

package com.mckesson.smora.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The Class SimpleCryptor.
 */
public class SimpleCryptor
        implements Serializable {
	/**
	 * The CLASSNAME.
	 */
	private final String CLASSNAME = "SimpleCryptor";

	/**
	 * The Constant CRYPTKEY.
	 */
	private static final String CRYPTKEY = "WindowsNTApplication";
	/**
	 * The Constant SEPARATOR.
	 */
	private static final String SEPARATOR = "\u00a1\u00a2\u00a3";
	//Separator used by the infolink application
	/**
	 * The Constant SEPARATOR_SIMPLE.
	 */
	private static final String SEPARATOR_SIMPLE = "LKJHG";
        /**
         * The Constant ENCODING.
         */
        private static final String ENCODING = "ISO8859_1";

	/**
	 * Crypt.
	 * 
	 * @param key the key
	 * @param inputBytes the input bytes
	 * 
	 * @return the byte[]
	 */
	public static byte[] crypt(byte[] inputBytes, byte[] key) {

		int maxLoop;
		int inputByteLength = inputBytes.length;
		byte[] outputBytes = new byte[inputByteLength];
		int keyLength = key.length;
		int offset = 0;

		while (offset < inputByteLength) {
			if (inputByteLength - offset > keyLength) {
				maxLoop = keyLength;
			} else {
				maxLoop = inputByteLength - offset;
			}

			for (int i = 0; i < maxLoop; i++) {
				outputBytes[offset + i] = (byte) (inputBytes[offset + i] ^ key[i]);
			}
			offset += maxLoop;
		}
		return outputBytes;
	}

	/**
	 * Encrypt.
	 * 
	 * @param key the key
	 * @param inputString the input string
	 * 
	 * @return the string
	 */
	public static String encrypt(String inputString, String key) {
		String outputString;
        byte[] outputBytes = null;
        try {
            outputBytes = crypt(inputString.getBytes(ENCODING), key.getBytes(ENCODING));
        } catch(Exception ex) {
            outputBytes = crypt(inputString.getBytes(), key.getBytes());
        }
		outputString = uuencode(outputBytes);
		return outputString;
	}

	/**
	 * Decrypt.
	 * 
	 * @param key the key
	 * @param inputString the input string
	 * 
	 * @return the string
	 */
	public static String decrypt(String inputString, String key) {
		String outputString = null;

		byte[] outputBytes = uudecode(inputString);
        try {
            outputString = new String(crypt(outputBytes, key.getBytes(ENCODING)), ENCODING);
        } catch(Exception ex) {
            outputString = new String(crypt(outputBytes, key.getBytes()));
        }
		return outputString;
	}

	/**
	 * Uuencode.
	 * 
	 * @param source the source
	 * 
	 * @return the string
	 */
	public static String uuencode(byte[] source) {
                int temp;
                int nibble[]=new int[2];
                StringBuffer outStringBuff = new StringBuffer();

                for (int i = 0; i < source.length; i++) {
                        temp = source[i];
                        // get lower nibble
                        nibble[1] = temp & 0x0F;
                        // get upper nibble
                        nibble[0] = (temp >>> 4) & 0x0F;

                        // write nibbles
                        for(int j = 0; j < 2; j++) {
                        	outStringBuff.append(Character.forDigit(nibble[j], 16));
                        }
                }
                return outStringBuff.toString();
	}

	/**
	 * Uudecode.
	 * 
	 * @param source the source
	 * 
	 * @return the byte[]
	 */
	public static byte[] uudecode(String source) {
                int temp, tempDigit;
                int nibble[] = new int[2];
                byte outputBytes[] = new byte[source.length() / 2];

                for (int i = 0; i < outputBytes.length; i++) {
                        for(int j = 0; j < 2; j++) {
                        	tempDigit = Character.digit(source.charAt(2 * i + j), 16);
                        	if (tempDigit < 0) {
                                    // Error in MCryptor.uudecode. Character is not a hex digit!

					tempDigit = 0;
                        	}
                        	nibble[j] = tempDigit;
                        }
                        temp = (nibble[0] & 0x0F) << 4;
                        temp = (nibble[1] & 0x0F) | temp;
                        outputBytes[i] = (byte) temp;
                }
                return outputBytes;
	}

	/**
	 * This method encrypts the pass word simple.
	 * 
	 * @param userName the user name
	 * @param passWord the pass word
	 * 
	 * @return the string
	 */
	public static final String encryptPassWordSimple(String userName, String passWord) {
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat( "MM/dd/yyyy" );
		String encryptedPassWord = SimpleCryptor.encrypt( userName +
				SEPARATOR_SIMPLE + passWord + SEPARATOR_SIMPLE +
				formatter.format(now), CRYPTKEY );
		return encryptedPassWord;
	}

	/**
	 * This method encrypts the pass word.
	 * 
	 * @param userName the user name
	 * @param passWord the pass word
	 * 
	 * @return the string
	 */
	public static final String encryptPassWord(String userName, String passWord) {
		Date now = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat( "MM/dd/yyyy" );
		String encryptedPassWord = SimpleCryptor.encrypt( userName +
			SEPARATOR + passWord + SEPARATOR +
			formatter.format(now), CRYPTKEY );
		return encryptedPassWord;
	}
	/**
	 * Encrypt.
	 * 
	 * @param orig the orig
	 * 
	 * @return the string
	 * 
	 * @throws Exception the exception
	 */
	public static final String encrypt(String orig) throws Exception    {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(orig.getBytes());
        byte[] cByte = md.digest();
        StringBuffer h = new StringBuffer();
        for (int x =0; x<cByte.length; x++) {
            String hex = Integer.toHexString(cByte[x]);
            if ( hex.length() < 2)
                h.append("0"+hex);
            else
                h.append( hex.substring(hex.length()-2) );
        }
        return h.toString();
    }


    /**
     * The main method.
     * 
     * @param args the args
     */
    public static void main(String[] args){
                String userName = "AESTUUN1";
                String password = "infocraze";
                /*
                System.out.println("Before encryption: \tuser:"+userName
                        +" password:"+password);
            String en = encryptPassWord(userName, password);
            System.out.println("Encrypted line: \t"+en);
                String plaintext = SimpleCryptor.encryptPassWord(en, CRYPTKEY);
                System.out.println("Decrypted line: \t"+plaintext);
                int end = plaintext.indexOf(SEPARATOR);
                userName = null;
                password = null;
                if (end != -1) {
                        userName = plaintext.substring(0, end);
                        int start = end + SEPARATOR.length();
                        end = plaintext.indexOf(SEPARATOR, start);
                        if (end != -1) {
                                password = plaintext.substring(start, end);
                        }
                }
                */
                /*

                360a1d0056074beff6e2021f1f0a0c04d5cbcc5f66465f544045437e61

               
                System.out.println("SEPARATOR: >" + SEPARATOR + "<" );

                if ( "1".equals(args[0]) )
                {
					System.out.println("userid: >" + args[1] + "<" );
					System.out.println("password: >" + args[2] + "<" );


					String plain =
						SimpleCryptor.encryptPassWord(args[1],args[2]);
					System.out.println("Encrypted Text: >" + plain + "<");
				}
				else
				{
					System.out.println("Data: >" + args[1] + "<" );


					String plain =
						SimpleCryptor.decrypt(args[1],
						CRYPTKEY);
					System.out.println("Plain Text: >" + plain + "<");
					int index1 = plain.indexOf(SEPARATOR);
					System.out.println("SepIdx: >" + index1 + "<");
					if (index1 > -1)
					{
					  System.out.println("USerID: >"+ plain.substring(0, index1) + "<");
					  int index2 = plain.indexOf(SEPARATOR, index1 + 1);
					  if (index2 > -1)
					  {
						System.out.println("Password: >" + plain.substring(index1 + SEPARATOR.length(), index2) + "<");
						String timeStamp = plain.substring(index2 + SEPARATOR.length());
						System.out.println("Date: >" + timeStamp + "<");
						SimpleDateFormat format =
						new SimpleDateFormat("MM/dd/yyyy");
						Date today = new Date();
						Date token = format.parse(timeStamp, new ParsePosition(0));
						}
					  }
			}
*/    
             /*   String plain =
					SimpleCryptor.encryptPassWord(userName,password);
				System.out.println("Encrypted Text: >" + plain + "<");*/
                String plain = null;
                try{
                plain =
					SimpleCryptor.encrypt(password);
                }
                catch(Exception e)
                {
                	e.printStackTrace();
                	                
                }
    }

}