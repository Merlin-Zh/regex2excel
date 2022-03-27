package cc.ilooli.regex2excel.executor;

import info.codesaway.util.regex.Matcher;
import info.codesaway.util.regex.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import static info.codesaway.util.regex.Pattern.compile;

/**
 * 正则表达式执行器
 * @author Merlin
 * @date 2022/03/25
 */
public class RegexExecutor {
    /** 正则表达式 */
    @Setter
    private String regex;

    /** 需要进行正则拆分的列表（用于保存校对） */
    @Getter
    private volatile List<String> targetList;

    /** 名称映射 */
    @Setter
    @Getter
    private volatile Map<String, String> nameMap;

    /** 正则表达式中的分组名称集合 */
    @Getter
    private volatile List<String> nameList;

    /** 结果列表 */
    @Getter
    private final List<Map<String, String>> resultList = new CopyOnWriteArrayList<>();

    /**
     * 拆分传入的字符串以获取需要拆分的目标列表
     * @param target 目标
     * @return {@link RegexExecutor} this
     */
    public RegexExecutor target(String target) {
        this.targetList = Arrays.asList(target.split("\r\n"));
        return this;
    }

    /**
     * 从传入的输入流获取需要拆分的字符串列表
     * @param inputStream 输入流
     * @return {@link RegexExecutor} this
     */
    public RegexExecutor target(InputStream inputStream) {
        StringBuilder builder = new StringBuilder();
        try(inputStream){
            int n;
            byte[] buffer = new byte[4096];
            while ((n = inputStream.read(buffer)) != -1) {
                builder.append(new String(buffer, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.targetList = Arrays.asList(builder.toString().split("\r\n"));
        return this;
    }

    /**
     * 从传入的文件获取需要拆分的目标字符串列表
     * @param file 文件
     * @return {@link RegexExecutor} this
     */
    @SneakyThrows
    public RegexExecutor target(File file) {
        InputStream inputStream = new FileInputStream(file);
        return this.target(inputStream);
    }

    /**
     * 从正则表达式中提取分组名称
     * 分组名称将用于输出表格的表头
     */
    private void getGroupNameListFromRegex(){
        List<String> temp = new ArrayList<>();
        final Matcher matcher = compile("<(\\w+)>", Pattern.MULTILINE).matcher(this.regex);
        // 遍历匹配结果，将目标正则表达式中的组名存入nameSet
        while (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                temp.add(matcher.group(i));
            }
        }
        this.nameList = temp;
    }

    /**
     * 执行目标字符列表的正则拆分
     * @return {@link ExcelExecutor} ecxel执行器
     */
    public ExcelExecutor execute(){
        this.getGroupNameListFromRegex();
        this.doExecuteLoop();
        return new ExcelExecutor(this);
    }

    /**
     * 以遍历目标字符串列表的方式拆分字符串
     */
    private void doExecuteLoop(){
        // 预编译正则
        final Pattern pattern = compile(this.regex);
        // 设置了分组名称映射（提前判断再循环，避免循环内再判断）
        if (this.nameMap != null && !this.nameMap.isEmpty()){
            this.getListWhitNameMap(pattern);
        }else {
            // 未设置分组名称映射
            this.getListWithoutNameMap(pattern);
        }
    }

    /**
     * 未设置分组名称映射的目标列表拆分
     * @param pattern    模式
     */
    private void getListWithoutNameMap( Pattern pattern) {
        this.targetList.parallelStream().forEach(target -> {
            Map<String, String> map = new HashMap<>(16);
            map.put("目标", target);
            final Matcher matcher = pattern.matcher(target);
            while (matcher.find()){
                String fullGroup = matcher.group(0);
                map.put("完整匹配", fullGroup);
                if (!target.equals(fullGroup)){
                    map.put("错误信息", "目标值与完整匹配不一致，请检查");
                }
                this.nameList.forEach(name -> map.put(name, matcher.group(name)));
            }
            this.resultList.add(map);
        });
    }

    /**
     * 设置分组名称映射的目标列表拆分
     * @param pattern    模式
     */
    private void getListWhitNameMap( Pattern pattern) {
        this.targetList.forEach(target -> {
            Map<String, String> map = new HashMap<>(16);
            map.put("目标", target);
            final Matcher matcher = pattern.matcher(target);
            while (matcher.find()) {
                String fullGroup = matcher.group(0);
                map.put("完整匹配", fullGroup);
                if (!target.equals(fullGroup)){
                    map.put("错误信息", "目标值与完整匹配不一致，请检查");
                }
                // 遍历分组名称集合，获取分组名称映射及匹配值存入map
                this.nameList.forEach(name -> map.put(this.nameMap.get(name) == null ? name : this.nameMap.get(name), matcher.group(name)));
            }
            this.resultList.add(map);
        });
    }

}
