package edu.brown.cs.student.main;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

/**
 * Class representing a single bloom filter with a given false positive rate
 * and number of hashes.
 */
public class BloomFilter {
  /** number of hashing functions used on new entries to the set. */
  private final int numHashes;

  /** set of bits representing the bloom filter set. */
  private final BitSet filter;

  /** size of the bloom filter or the number of bits used. */
  private final int size;


  /**
   * Constructor for a BloomFilter object. Uses the given parameters to
   * calculate the length of the filter's bitset and the number of hashes to use
   * @param fpRate desired false positive rate of this bloom filter
   * @param maxElts maximum number of elements to be inserted
   * @throws IllegalArgumentException if an error is encountered that prevents
   * proper creation of the filter.
   */
  BloomFilter(double fpRate, int maxElts) throws IllegalArgumentException {
    try {
      assert fpRate > 0 && fpRate <= 1;

      long numHash = Math.round(Math.ceil(-1 * Math.log(fpRate) / Math.log(2)));
      long numBits = Math.round(Math.ceil((numHash * maxElts) / Math.log(2)));

      numHashes = Math.toIntExact(numHash);
      size = Math.toIntExact(numBits);
      filter = new BitSet(size);
    } catch (ArithmeticException e) {
      System.err.println("ERROR: calculated filter size or hash number too "
          + "large. Try a different value of <r> or <n>.");
      throw new IllegalArgumentException();
    } catch (AssertionError e) {
      System.err.println("ERROR: false positive rate must be between 0 and 1");
      throw new IllegalArgumentException();
    }
  }

  /**
   * Generates hashes based on the contents of an array of bytes, converts the result into
   * BigIntegers, and stores them in an array. The hash function is called until the required number
   * of BigIntegers are produced.
   * For each call to the hash function a salt is prepended to the data. The salt is increased by 1
   * for each call.
   *
   * @param data      input data.
   * @param numHashes number of hashes/BigIntegers to produce.
   * @return array of BigInteger hashes
   */
  private static BigInteger[] createHashes(byte[] data, int numHashes) {
    BigInteger[] result = new BigInteger[numHashes];

    int k = 0;
    BigInteger salt = BigInteger.valueOf(0);
    while (k < numHashes) {
      try {
        MessageDigest hashFunction = MessageDigest.getInstance("SHA-1");
        hashFunction.update(salt.toByteArray());
        salt = salt.add(BigInteger.valueOf(1));
        byte[] hash = hashFunction.digest(data);
        hashFunction.reset();

        // convert hash byte array to hex string, then to BigInteger
        String hexHash = bytesToHex(hash);
        result[k] = new BigInteger(hexHash, 16);
        k++;
      } catch (NoSuchAlgorithmException ex) {
        System.err.println("ERROR: SHA-1 Algorithm not found.");
      }
    }
    return result;
  }

  /**
   * Converts a byte array to a hex string.
   * Source: https://stackoverflow.com/a/9855338
   *
   * @param bytes the byte array to convert
   * @return the hex string
   */
  private static String bytesToHex(byte[] bytes) {
    byte[] hexArray = "0123456789ABCDEF".getBytes(StandardCharsets.UTF_8);
    byte[] hexChars = new byte[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = hexArray[v >>> 4];
      hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars, StandardCharsets.UTF_8);
  }

  public void insert(Object newElt) {
    String hashString = newElt.toString();
    byte[] byteArray = hashString.getBytes(StandardCharsets.UTF_8);

    BigInteger[] hashArray = createHashes(byteArray, numHashes);

    for (BigInteger hash : hashArray) {
      BigInteger bigIndex = hash.mod(BigInteger.valueOf(size));
      int index = bigIndex.intValue(); // no overflow because size < MAXINT
      filter.set(index);
    }
  }

  @Override
  public String toString() {
    StringBuilder bitString = new StringBuilder();
    for (int i = 0; i < size; i++) {
      String toAppend = "0";
      if (filter.get(i)) {
        toAppend = "1";
      }

      bitString.append(toAppend);
    }

    return bitString.toString();
  }
}
