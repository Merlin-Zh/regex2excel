package cc.ilooli.util;

import lombok.*;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * 正则工具类
 * @author Merlin
 * @date 2022/03/24
 */
// @Builder
@Accessors(chain = true)
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class RegexExecutor {

    /** 正则表达式 */
    private String regex;
    /** 目标列表 */
    private List<String> targetList;
    /** 名称映射 */
    private Map<String, String> nameMap;

    @Builder
    public RegexExecutor(String regex, Map<String, String> nameMap) {
        this.regex = regex;
        this.nameMap = nameMap;
    }

    /**
     * 获取目标正则表达式中的分组名称集合
     * @return {@code Set<String>}
     */
    public Set<String> getGroupNameListFromRegex(){
        Set<String> nameSet = new HashSet<>();
        final Matcher matcher = compile("<(\\w+)>", Pattern.MULTILINE).matcher(this.regex);

        // 遍历匹配结果，将目标正则表达式中的组名存入nameSet
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                nameSet.add(matcher.group(i));
            }
        }
        return nameSet;
    }

    /**
     * 从输入流中获取文件内容并转换为字符列表
     * @param inputStream 输入流
     * @return {@code List<String>}
     */
    @SneakyThrows
    public List<String> loadFileFromInputStream(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        try (inputStream) {
            int n;
            byte[] buffer = new byte[4096];
            while ((n = inputStream.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(builder.toString().split("\r\n"));
    }

    /**
     * 从字符串加载文件并转换为列表
     * @param string 字符串
     * @return {@code List<String>} 字符串列表
     */
    public List<String> loadFileFromString(String string) {
        return Arrays.asList(string.split("\r\n"));
    }

    public void execute(){

    }

}
