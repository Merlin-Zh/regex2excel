import cc.ilooli.regex2excel.Regex2Excel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        // 带分组名称的正则表达式
        String regex = "(?<series>(?:.*)?BM\\d+(?<E>E)?\\w*)-(?<inm>\\d+)(?<icu>(?:HU)?[CLMH])?(?<opeator>[ZP])?";
        // 分组名称映射，可进行部分名称映射
        Map<String, String> nameMap = new HashMap<>(16);
        nameMap.put("series", "系列");
        nameMap.put("E", "电子式");
        nameMap.put("icu", "分断等级");
        // 需要进行拆分的目标数据，格式：String | InputStream | File
        String target = "BM30-125OMZ\r\nBM3E-400HPH\r\nBM30L-630P";
        // 输出的excel文件名
        String fileName = "test-excel.xlsx";
        // 调用方法执行拆分，获取excel文件。该方法会在指定目录以指定文件名生成文件，返回File可用于后续操作
        File file = Regex2Excel.init(regex, nameMap).target(target).execute().get(fileName);
    }
}
