package quasic;

/**
 * @author  Jiacheng Liu
 * @version 1.2 (beta)
 * @since   1.0
 *
 * Quasic is a password generating program that produces an output in the form
 * of a quasi-hash/encrypted output
 *
 * The main idea of this program is to generate effective passwords from simply
 * inputing simple and memorable Strings(words) and converting them into encrypted
 * text.
 *
 * This program should not be used for any other purpose other than its intended
 * purpose. Mostly because I'm sure this thing is easy to break and vulnerable to
 * various attacks.
 */
public final class Quasic {

    // Generates a singleton of Quasic
    private static Quasic quasic = null;

    private Quasic() {
        System.out.println("Quasic initialized . . .");
    }

    static Quasic getInstance() {
        if (quasic == null) {
            quasic = new Quasic();
        }
        return quasic;
    }

    /**
     * Method "execute": takes in the user's String and chosen output length
     * @param  msg          user inputed String that is to be encrypted
     * @param  outputLength user prefered length of the encrypted output
     * @return              the encrypted output in a char[]
     */
    public char[] execute(String msg, int outputLength) {
        char[] output = new char[outputLength];
        int[] int_output;

        // converts String msg into char[]
        char[] input = msg.toCharArray();
        int[] ascii_input = charToInts(input);

        if(input.length < outputLength) {
            int_output = padToLength(ascii_input, outputLength);
        } else {
            int_output = ascii_input;
        }
        int_output = scramble(int_output);

        if(input.length > outputLength) {
            int_output = compressToLength(ascii_input, outputLength);
            int_output = scramble(int_output);
        }

        intToChar(int_output, output);
        return output;
    }

    /**
     * Method "charToInts": returns a new int[] of ascii values of the chars in
     * the passed char[]
     * @param  msgChars user inputed String converted to a char[]
     * @return          an int[] of asciiValues of msgChars
     */
    private int[] charToInts(char[] msgChars) {
        int[] asciiValues = new int[msgChars.length];
        for (int i = 0; i < msgChars.length; i++) {
            asciiValues[i] = msgChars[i];
        }
        return asciiValues;
    }

    /**
     * Method "padToLength": pads the msg to the chosen outputLength
     *
     * *Please Note*
     * The padded characters are determined by the characters in the user
     * inputed String msg
     *
     * @param  ascii_input  the int[] that contains the ascii values of msgChars
     * @param  outputLength user prefered length of the encrypted output
     * @return              the padded msg in int[] ascii form
     */
    private int[] padToLength(int[] ascii_input, int outputLength) {
        int[] paddedMsg = new int[outputLength];

        for(int i = 0; i < paddedMsg.length; i++) {
            if(i < ascii_input.length) {
                paddedMsg[i] = ascii_input[i];
            } else {
                paddedMsg[i] = pad(paddedMsg, i, ascii_input, outputLength);
            }
        }
        return paddedMsg;
    }

    /**
     * Method to return a number between 0 - 127 (ascii range); this number is
     * generated using a sequence of linearDeterNumGen() calls that even I do not
     * understand how it will play out. This is experimented to produce something
     * similar to a pseudo "one-way" function.
     *
     * @param  paddedMsg    the int[] that is to be the padded message
     * @param  i            a variable (that can be the current iteration)
     * @param  ascii_input  original user inputed message in int[] form
     * @param  outputLength the length that the user wishes the output to be
     * @return              a (pseudo) number that is to be the ascii equivalent
     */
    private int pad(int[] paddedMsg, int i, int[] ascii_input, int outputLength) {
        return linearDeterNumGen(paddedMsg[i - ascii_input.length] + outputLength, linearDeterNumGen(paddedMsg[i - 1] + outputLength, 7, 256), 128);
    }

    /**
     *
     * Recursive method to compress the int[] into the prefered output length
     *
     * @param  ascii_input  the int[] that contains the ascii values of msgChars
     * @param  outputLength user prefered length of the encrypted output
     * @return              the padded msg in int[] ascii form
     */
    private int[] compressToLength(int[] ascii_input, int outputLength) {

        if(ascii_input.length <= outputLength) {
            return padToLength(ascii_input, outputLength);
        }

        int[] compressedMsg = new int[(int)Math.ceil(ascii_input.length/2)];

        for(int i = 0; i < compressedMsg.length; i++) {
            if(i == 0) {
                compressedMsg[i] = ascii_input[i] ^ (ascii_input[i + 1] >> 2);
            } else {
                compressedMsg[i] = (ascii_input[i + 1] >> 1) ^ ascii_input[i + 2] ^ (compressedMsg[i - 1] >>> 3);
            }
        }
        return compressToLength(compressedMsg, outputLength);
    }

    /**
     * Method to scramble the padded/compressed message
     * @param msgToScramble message needed to scramble
     */
    private int[] scramble(int[] msgToScramble) {
        return finalize(round3(round2(round1(msgToScramble))));
    }

    /**
     * Round 1 operation of scrambling; scrambles the padded/compressed message
     * @param  msg original padded/compressed message
     * @return     round 1's scrambled message
     */
    private int[] round1(int[] msg) {
        for(int j = 0; j < 64; j++) {
            for(int i = 0; i < msg.length; i++) {

                if (i == msg.length - 1) {
                    msg[i] = ((msg[i] << 1) ^ msg[i - 1] - j) % 512;
                    break;
                }
                msg[i] = inner_round1(msg, i);
            }
        }
        return msg;
    }

    /**
     * An inner scrambling method for method 1
     * @param  msg   original padded/compressed message
     * @param  index current index/iteration number
     * @return       a number within the certain index of new scrambled message
     */
    private int inner_round1(int[] msg, int index) {
        for(int i = 4; i < msg.length; i++) {
            msg[i] = Integer.rotateRight(msg[i] - msg[i - 4], 7) ^ Integer.rotateLeft(msg[i] - msg[i - 4], 11) ^ msg[i] >>> 3;
        }

        for(int j = 0; j < msg.length; j++) {
            if(j == msg.length - 1) {
                msg[j] = Integer.rotateLeft(msg[j/2], 11) ^ msg[0] >> 2;
                break;
            }
            msg[j] = msg[j] ^ msg[j + 1] >>> 3;
        }
        return msg[index];
    }

    /**
     * Round 2 operation of scrambling; scrambles the output of round1
     * @param msg round 1's scrambled message
     * @return     round 2's scrambled message
     */
    private int[] round2(int[] msg) {
        int temp;
        for(int i = 0; i < msg.length; i++) {
            if(i == msg.length - 1) {
                temp = msg[i] ^ (msg[i] >> i) & (msg[i] % msg.length);
                msg[i] = Math.abs(msg[i] ^ temp ^ (temp << i));
                break;
            }
            temp = msg[i] ^ (msg[i] >> i) & (msg[i] % msg.length);
            msg[i] = Math.abs(msg[i] ^ temp ^ (temp << i));
        }
        return msg;
    }

    /**
     * Round 3 operation of scrambling; scrambles the output of round 2
     * @param  msg message to be scrambled
     * @return     round 3's scrambled message
     */
    private int[] round3(int[] msg) {
        boolean swap = true;
        for(int j = 0; j < 64; j++) {
            for(int i = 0; i < msg.length; i++) {
                if(swap) {
                    msg[linearDeterNumGen(i + j, i, msg.length - 1)] = Math.abs(msg[i] ^ msg[linearDeterNumGen((i >> 2 )^ j, i, msg.length - 1)] ^ Integer.rotateRight(msg[i] - 64, 5) ^ i >>> 3);
                    swap = !swap;
                } else {
                    if(i + 1 > msg.length - 1) {
                        msg[i] = Math.abs(msg[i]);
                        break;
                    }
                    msg[linearDeterNumGen(j + msg.length, i, msg.length - 1)] = Math.abs(msg[i + 1] + i ^ msg[i % 3]);
                    swap = !swap;
                }
            }
        }
        return msg;
    }

    /**
     * Message that compress all values to within the 33 - 126 ascii value range
     * @param  msg final uncompressed scrambled message
     * @return     the int[] output of the scrambled message in compressed form
     */
    private int[] finalize(int[] msg) {
        for(int i = 0; i < msg.length; i++) {

            msg[i] = msg[i] % 126;
            if (msg[i] < 33) {
                msg[i] = reroll(msg[i], i, 33, 64);
            }
        }
        return msg;
    }

    /**
     * Method to make sure that the number in the index is above 32.
     * This uses the linearDeterNumGen() method to generate numbers that will
     * add to variable num in-order for it to be above 32.
     *
     * There are no displayable ascii characters below 32.
     *
     * @param  num the initial number needed to be above 32
     * @param  i   the current index value
     * @param  min the least number that num has to pass (32)
     * @param  lim the limit of the range of numbers for linearDeterNumGen() to
     *             choose from
     * @return     the final compressed int[] output
     */
    private int reroll(int num, int i, int min, int lim) {

        int multiplier = num * i - min;
        while(num < min) {
            num = num + linearDeterNumGen(num, multiplier, min) + 1;
        }

        return num;
    }

    /**
     * A really, really bad pseudo-random number generator that I call:
     * "Linear Deterministic Number Generator" because its deterministic
     *
     * @param  seed       initial starting value
     * @param  multiplier some extra variable
     * @param  modulus    the max number the generator can generate
     * @return            the generated pseudo-random number
     */
    private int linearDeterNumGen(int seed, int multiplier, int modulus) {

        int limit = seed * multiplier >> 1 ;
        for(int i = 0; i < limit; i++) {
            int temp = Integer.rotateRight(multiplier, i) ^ Integer.rotateLeft(limit, multiplier);
            seed = Math.abs((((multiplier & i) * seed + i) >> 2 ^ temp) % 2147483647);
        }
        seed = seed % modulus;
        return seed;
    }

    /**
     * Method used for debugging; it prints out the contents of the passed int[]
     */
    private void debug_printArr(int[] arr) {
        int index = 0;
        for(int i: arr) {
            System.out.println("i: " + index + " v: " + i);
            index++;
        }
    }

    /**
     * Method used for debugging; it prints out the contents of the passed char[]
     */
    private void debug_printArr(char[] arr) {
        for(char c: arr) {
            System.out.println(c);
        }
    }

    /**
     * Method used to convert an int[] to char[]
     * @param i_arr int array to convert
     * @param c_arr char array to convert to
     */
    private void intToChar(int[] i_arr, char[] c_arr) {
        for(int i = 0; i < i_arr.length; i++) {
            c_arr[i] = (char)i_arr[i];
        }
    }
}
