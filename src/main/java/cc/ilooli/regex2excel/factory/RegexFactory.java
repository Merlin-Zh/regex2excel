package cc.ilooli.regex2excel.factory;

import cc.ilooli.regex2excel.executor.RegexExecutor;

import java.util.Map;

/**
 * 正则表达式工厂
 * @author Merlin
 * @date 2022/03/25
 */
public class RegexFactory {

    /**
     * 初始化执行器
     * @param regex 正则表达式
     * @return {@link RegexExecutor} 执行器
     */
    public static RegexExecutor init(String regex){
        return init(regex, null);
    }

    /**
     * 初始化执行器
     * @param regex   正则表达式
     * @param nameMap 分组名称映射
     * @return {@link RegexExecutor} 执行器
     */
    public static RegexExecutor init(String regex, Map<String, String> nameMap){
        RegexExecutor executor = new RegexExecutor();
        executor.setRegex(regex);
        executor.setNameMap(nameMap);
        return executor;
    }
}
