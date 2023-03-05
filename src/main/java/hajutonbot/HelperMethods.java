//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package hajutonbot;

import java.util.ArrayList;
import java.util.List;

public class HelperMethods {
    public HelperMethods() {
    }

    public static void main(String[] args) throws Exception {
        String moi = "moitesti";
        String subi = moi.substring(3);
        System.out.println(subi);
    }

    public static String HeadOrTails() {
        int decider = (int)Math.floor(Math.random() * 2.0D);
        return decider == 1 ? "tails" : "heads";
    }

    public static String enCrypt(String inputText) {
        String endResult = "";
        char[] charArray = inputText.toCharArray();
        int cipherNumber = 3;
        char[] var4 = charArray;
        int var5 = charArray.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            char charas = var4[var6];
            endResult = endResult + String.valueOf(Character.toChars(charas + cipherNumber));
        }

        return endResult;
    }

    public static String deCrypt(String inputText) {
        String endResult = "";
        char[] charArray = inputText.toCharArray();
        int cipherNumber = 3;
        char[] var4 = charArray;
        int var5 = charArray.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            char charas = var4[var6];
            endResult = endResult + String.valueOf(Character.toChars(charas - cipherNumber));
        }

        return endResult;
    }
}
