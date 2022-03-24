package cc.ilooli;

import cc.ilooli.util.RegexExecutor;

/**
 * @author Merlin
 * @date 2022/03/24
 */
public class Application {

    public static void main(String[] args) {
        RegexExecutor shs = RegexExecutor.builder().regex("shs").build();
        System.out.println("shs = " + shs);
    }

}
