import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by makjdrn on 2015-11-01.
 */
public class RC4 {
    public static String cryptogram = "11100000 00000011 11111101 11001110 10111001 01001001 01000001 00001000 00101001 11110001 10010010 11011111 01110011 01101110 00101011 11101110 01010001 00010010 11001011 00110101 11100101 00100111 00011100 10000001 11000000 10100110 11000101 11100000 11010010 10001111 11011011 01100011 01000000 11001011 00001111 11010100 11100110 11000110 00000001 00111101 00111100 01100101 11100110 10010000 01011101 01100100 00000000 01110111 11001111 00100111 10101010 10011011 11100111 01110100 10010001 10100101 11110100 00111001 00001110 10010011 01001100 00011010 10100101 01001001 10000110 10010100 11111110 11010110 11001110 00110010 10110100 10000010 00001110 01000101 00101110 00100000 00000011 11000000 00100111 00010010 01001011 01000100 11011011 10001110 01000011 00101000 00000011 10100001 11000100 11100011 10101001 10111010 01010000 01110001 10010001 01011101 00011010 00000001 10000100 00001010";
    public static void main (String [] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        byte[] key = "5b7c959d92e969e9".getBytes("ASCII");

        cryptogram = cryptogram.replaceAll("\\s+", "");
        String ciphertext = toAscii(cryptogram);
        BigInteger op = new BigInteger("4294967280");
        BigInteger i = new BigInteger("1");
        BigInteger ii = new BigInteger("1");
        String zero = "00000000";
        String halfkey = "8e9d019d";
        String s;
        String strsbin;
        String result;
        int asciiresult = 0;
        //64
        while(i.compareTo(op) == -1) {
            StringBuilder hex2String = new StringBuilder();
            String nexthex = i.toString(16);

            if (nexthex.length() < 8) hex2String.append(zero.substring(0,zero.length() - nexthex.length()));
            hex2String.append(nexthex);
            s = hex2String.toString() + halfkey;
            strsbin = new BigInteger(s, 16).toString(2);
            //System.out.println("Hex2 format : " + s+ " " + strsbin);
            byte[] strbyte = s.getBytes("ASCII");
            //System.out.println(strbyte);
            result = RC4method(strbyte, ciphertext);
            //System.out.println(result);
            asciiresult = CheckifCorrect(result);
            if(asciiresult >= 20 && result.length() == 64)
                System.out.println("Klucz: " + s + " plaintext: " + result);
            //if(result.length() % 8 == 0)
            //    asciiresult = toAscii(result);
            //if(asciiresult != null)
             //       System.out.println(asciiresult);

            i = i.add(ii);
        }
    }

    private static int CheckifCorrect(String result) {
        //System.out.println(result);
        char c;
        int good = 0;
        int i = 0;
        int length = result.length();
        while(i < length ) {
            //System.out.println(good);
            c = result.charAt(i);
            if ((c >= 65 && c <= 90) || (c >= 97 && c <= 122))// || c == 32 || c == 44 || c == 46 || c == 40 || c == 41 || c == 63 || (c >= 48 && c <=57) || c == 33 || c == 34))
                good++;
            i++;
        }
        return good;
    }

    private static String RC4method(byte[] strbyte, String cryptog) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("RC4");
        SecretKeySpec rc4key = new SecretKeySpec(strbyte, "RC4");
        cipher.init(Cipher.DECRYPT_MODE, rc4key);
        byte[] cleartext = cipher.update(cryptog.getBytes(StandardCharsets.US_ASCII));
        //System.out.println(new String(cleartext, StandardCharsets.US_ASCII));
        return new String(cleartext, StandardCharsets.US_ASCII);
    }

    private static String toAscii(String result) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < result.length(); i+=8)
            sb.append((char)Integer.parseInt(result.substring(i, i + 8), 2));
        String s;
        s = sb.toString();
        return s;
    }
}
