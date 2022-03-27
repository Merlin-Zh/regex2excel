# 一个将正则表达式匹配结果输出为excel表格的工具

### 让数据拆分更简单

- 根据输入的正则表达式（必须进行分组命名）和传入的目标文件，进行正则匹配后将结果以excel表格输出
- 默认输出文件表头是分组名称，由于正则分组不可用中文，所以可以传入一个分组名称中英文映射map，来让输出表格表头为指定中文

### 使用方式：
1. 无分组名称映射，将使用正则表达式中的分组名称作为excel表格的表头输出

  ```java
import cc.ilooli.regex2excel.Regex2Excel;

import java.io.File;

class Test {
    public void test() {
        // 带分组名称的正则表达式
        String regex = "(?<series>(?:.*)?BM\\d+(?<E>E)?\\w*)-(?<inm>\\d+)(?<icu>(?:HU)?[CLMH])?(?<opeator>[ZP])?";
        // 需要进行拆分的目标数据，格式：String | InputStream | File
        String target = "BM30-125MZ\r\nBM3E-400HP\r\nBM30L-630P";
        // 输出的excel文件名
        String fileName = "test-excel.xlsx";
        // 调用方法执行拆分，获取excel文件。该方法会在指定目录以指定文件名生成文件，返回File可用于后续操作
        File file = Regex2Excel.init(regex).target(target).execute().get(fileName);
    }
}
```
生成的excel文件如下：

![img.png](https://southeastasia1-mediap.svc.ms/transform/thumbnail?provider=spo&inputFormat=png&cs=fFNQTw&docid=https%3A%2F%2Filooli-my.sharepoint.com%3A443%2F_api%2Fv2.0%2Fdrives%2Fb!WEcHIbvBP0Cv5WFJov6-ch2GSYGqz0pDppYPwahMK1F3xJTRlChMQ45xMIWkakPY%2Fitems%2F017ULM5FCQC67KNDGYQJGKE5KG5SQRKTED%3Fversion%3DPublished&access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTBmZjEtY2UwMC0wMDAwMDAwMDAwMDAvaWxvb2xpLW15LnNoYXJlcG9pbnQuY29tQDY2OGNkNjA2LWUyODctNGNlZS1hZTBjLWVhNDQ0MDM5ODE1OSIsImlzcyI6IjAwMDAwMDAzLTAwMDAtMGZmMS1jZTAwLTAwMDAwMDAwMDAwMCIsIm5iZiI6IjE2NDgzNjA4MDAiLCJleHAiOiIxNjQ4MzgyNDAwIiwiZW5kcG9pbnR1cmwiOiJaN2pLcHRXd0FWeDd4eXdCSngzYk9PU0NmcHJONkc4UUxqRyt6WXpuTnRJPSIsImVuZHBvaW50dXJsTGVuZ3RoIjoiMTE2IiwiaXNsb29wYmFjayI6IlRydWUiLCJ2ZXIiOiJoYXNoZWRwcm9vZnRva2VuIiwic2l0ZWlkIjoiTWpFd056UTNOVGd0WXpGaVlpMDBNRE5tTFdGbVpUVXROakUwT1dFeVptVmlaVGN5IiwibmFtZWlkIjoiMCMuZnxtZW1iZXJzaGlwfHVybiUzYXNwbyUzYWFub24jYjgwNTg3YzBkNDcxYTE4YTJlMTcxOTdkYTUzYTFkZmJiNjE5ODljZTc0NWFmYjI2MWQ3MmU5NzRlNThlMGYzMCIsIm5paSI6Im1pY3Jvc29mdC5zaGFyZXBvaW50IiwiaXN1c2VyIjoidHJ1ZSIsImNhY2hla2V5IjoiMGguZnxtZW1iZXJzaGlwfHVybiUzYXNwbyUzYWFub24jYjgwNTg3YzBkNDcxYTE4YTJlMTcxOTdkYTUzYTFkZmJiNjE5ODljZTc0NWFmYjI2MWQ3MmU5NzRlNThlMGYzMCIsInNoYXJpbmdpZCI6InF1VlpTWm4wU1VDcStRZ2FTclE3bXciLCJ0dCI6IjAiLCJ1c2VQZXJzaXN0ZW50Q29va2llIjoiMiIsImlwYWRkciI6IjEyNC4xMjYuNi4xNDcifQ.WlV2WGZsc1RkUHloMmdqZlNhTjJDbS9DODFIa3UzYmZnYlFWYy9oQTROND0&cTag=%22c%3A%7BA6BE1750-D88C-4C82-A275-46ECA1154C83%7D%2C2%22&encodeFailures=1&width=2560&height=1277&srcWidth=&srcHeight=)

2. 使用分组名称映射，将使用分组名称的映射作为表头输出
```java
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
        String target = "BM30-125MZ\r\nBM3E-400HP\r\nBM30L-630P";
        // 输出的excel文件名
        String fileName = "test-excel.xlsx";
        // 调用方法执行拆分，获取excel文件。该方法会在指定目录以指定文件名生成文件，返回File可用于后续操作
        File file = Regex2Excel.init(regex, nameMap).target(target).execute().get(fileName);
    }
}
```
生成数据如图：

![img_1.png](https://southeastasia1-mediap.svc.ms/transform/thumbnail?provider=spo&inputFormat=png&cs=fFNQTw&docid=https%3A%2F%2Filooli-my.sharepoint.com%3A443%2F_api%2Fv2.0%2Fdrives%2Fb!WEcHIbvBP0Cv5WFJov6-ch2GSYGqz0pDppYPwahMK1F3xJTRlChMQ45xMIWkakPY%2Fitems%2F017ULM5FAZBEVJD4WXIBBZMRGXEOBBIMYV%3Fversion%3DPublished&access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTBmZjEtY2UwMC0wMDAwMDAwMDAwMDAvaWxvb2xpLW15LnNoYXJlcG9pbnQuY29tQDY2OGNkNjA2LWUyODctNGNlZS1hZTBjLWVhNDQ0MDM5ODE1OSIsImlzcyI6IjAwMDAwMDAzLTAwMDAtMGZmMS1jZTAwLTAwMDAwMDAwMDAwMCIsIm5iZiI6IjE2NDgzNjA4MDAiLCJleHAiOiIxNjQ4MzgyNDAwIiwiZW5kcG9pbnR1cmwiOiJaN2pLcHRXd0FWeDd4eXdCSngzYk9PU0NmcHJONkc4UUxqRyt6WXpuTnRJPSIsImVuZHBvaW50dXJsTGVuZ3RoIjoiMTE2IiwiaXNsb29wYmFjayI6IlRydWUiLCJ2ZXIiOiJoYXNoZWRwcm9vZnRva2VuIiwic2l0ZWlkIjoiTWpFd056UTNOVGd0WXpGaVlpMDBNRE5tTFdGbVpUVXROakUwT1dFeVptVmlaVGN5IiwibmFtZWlkIjoiMCMuZnxtZW1iZXJzaGlwfHVybiUzYXNwbyUzYWFub24jYjgwNTg3YzBkNDcxYTE4YTJlMTcxOTdkYTUzYTFkZmJiNjE5ODljZTc0NWFmYjI2MWQ3MmU5NzRlNThlMGYzMCIsIm5paSI6Im1pY3Jvc29mdC5zaGFyZXBvaW50IiwiaXN1c2VyIjoidHJ1ZSIsImNhY2hla2V5IjoiMGguZnxtZW1iZXJzaGlwfHVybiUzYXNwbyUzYWFub24jYjgwNTg3YzBkNDcxYTE4YTJlMTcxOTdkYTUzYTFkZmJiNjE5ODljZTc0NWFmYjI2MWQ3MmU5NzRlNThlMGYzMCIsInNoYXJpbmdpZCI6InF1VlpTWm4wU1VDcStRZ2FTclE3bXciLCJ0dCI6IjAiLCJ1c2VQZXJzaXN0ZW50Q29va2llIjoiMiIsImlwYWRkciI6IjEyNC4xMjYuNi4xNDcifQ.WlV2WGZsc1RkUHloMmdqZlNhTjJDbS9DODFIa3UzYmZnYlFWYy9oQTROND0&cTag=%22c%3A%7B912A0919-D7F2-4340-9644-D72382143315%7D%2C2%22&encodeFailures=1&width=693&height=150&srcWidth=693&srcHeight=150)

3. 若正则表达式的完全匹配结果与目标值不一致，则错误信息列会有信息提示，方便进行捷星结果审阅，如图：

![img_2.png](https://southeastasia1-mediap.svc.ms/transform/thumbnail?provider=spo&inputFormat=png&cs=fFNQTw&docid=https%3A%2F%2Filooli-my.sharepoint.com%3A443%2F_api%2Fv2.0%2Fdrives%2Fb!WEcHIbvBP0Cv5WFJov6-ch2GSYGqz0pDppYPwahMK1F3xJTRlChMQ45xMIWkakPY%2Fitems%2F017ULM5FEYIAXHP3M6AVEZFWRTGHEPLFXL%3Fversion%3DPublished&access_token=eyJ0eXAiOiJKV1QiLCJhbGciOiJub25lIn0.eyJhdWQiOiIwMDAwMDAwMy0wMDAwLTBmZjEtY2UwMC0wMDAwMDAwMDAwMDAvaWxvb2xpLW15LnNoYXJlcG9pbnQuY29tQDY2OGNkNjA2LWUyODctNGNlZS1hZTBjLWVhNDQ0MDM5ODE1OSIsImlzcyI6IjAwMDAwMDAzLTAwMDAtMGZmMS1jZTAwLTAwMDAwMDAwMDAwMCIsIm5iZiI6IjE2NDgzNjA4MDAiLCJleHAiOiIxNjQ4MzgyNDAwIiwiZW5kcG9pbnR1cmwiOiJaN2pLcHRXd0FWeDd4eXdCSngzYk9PU0NmcHJONkc4UUxqRyt6WXpuTnRJPSIsImVuZHBvaW50dXJsTGVuZ3RoIjoiMTE2IiwiaXNsb29wYmFjayI6IlRydWUiLCJ2ZXIiOiJoYXNoZWRwcm9vZnRva2VuIiwic2l0ZWlkIjoiTWpFd056UTNOVGd0WXpGaVlpMDBNRE5tTFdGbVpUVXROakUwT1dFeVptVmlaVGN5IiwibmFtZWlkIjoiMCMuZnxtZW1iZXJzaGlwfHVybiUzYXNwbyUzYWFub24jYjgwNTg3YzBkNDcxYTE4YTJlMTcxOTdkYTUzYTFkZmJiNjE5ODljZTc0NWFmYjI2MWQ3MmU5NzRlNThlMGYzMCIsIm5paSI6Im1pY3Jvc29mdC5zaGFyZXBvaW50IiwiaXN1c2VyIjoidHJ1ZSIsImNhY2hla2V5IjoiMGguZnxtZW1iZXJzaGlwfHVybiUzYXNwbyUzYWFub24jYjgwNTg3YzBkNDcxYTE4YTJlMTcxOTdkYTUzYTFkZmJiNjE5ODljZTc0NWFmYjI2MWQ3MmU5NzRlNThlMGYzMCIsInNoYXJpbmdpZCI6InF1VlpTWm4wU1VDcStRZ2FTclE3bXciLCJ0dCI6IjAiLCJ1c2VQZXJzaXN0ZW50Q29va2llIjoiMiIsImlwYWRkciI6IjEyNC4xMjYuNi4xNDcifQ.WlV2WGZsc1RkUHloMmdqZlNhTjJDbS9DODFIa3UzYmZnYlFWYy9oQTROND0&cTag=%22c%3A%7B772E4098-9EED-4905-92DA-3331C8F596EB%7D%2C2%22&encodeFailures=1&width=884&height=150&srcWidth=884&srcHeight=150)

### 项目依赖

jar包中未打包依赖，如运行报错请导入regexPlus和easyExcel依赖.

- Lombok：简化开发

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.22</version>
    <scope>provided</scope>
</dependency>
```

- regexplus：正则表达式增强，支持条件匹配等

```xml
<dependency>
    <groupId>info.codesaway</groupId>
    <artifactId>regexplus</artifactId>
    <version>2.0.0</version>
</dependency>
```

- easyExcel：将匹配结果输出为excel

```xml
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>easyexcel</artifactId>
    <version>3.0.5</version>
</dependency>
```


### 如果本项目对你有帮助，请给个Star吧