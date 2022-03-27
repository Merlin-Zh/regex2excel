package cc.ilooli.regex2excel.executor;

import com.alibaba.excel.EasyExcel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * excel执行器
 * @author Merlin
 * @date 2022/03/26
 */
public class ExcelExecutor {

    private final RegexExecutor regexExecutor;
    private final Properties properties = new Properties();
    private final static String TEMPLATE_PATH = "app.temp-file-path";
    private final static String DELETE_TEMPLATE = "app.temp-file-path.delete";
    private final static String EXCEL_PATH = "app.excel-file-path";

    public ExcelExecutor(RegexExecutor regexExecutor) {
        this.regexExecutor = regexExecutor;
        this.initProperties();
    }

    /**
     * 初始化properties，若未从application.properties获取到，则使用默认值
     */
    private void initProperties(){
        // 优先获取项目jar包根目录的application.properties，失败则获取项目内的application.properties，再失败则使用默认
        try{
            this.properties.load(this.getClass().getResourceAsStream("./application.properties"));
        }catch (Exception e1) {
            try {
                this.properties.load(this.getClass().getResourceAsStream("/application.properties"));
            } catch (Exception e2) {
                this.properties.put(TEMPLATE_PATH, "./excel/");
                this.properties.put(DELETE_TEMPLATE, "true");
                this.properties.put(EXCEL_PATH, "./excel/");
            }
        }
    }

    /**
     * 获取excel文件
     * @param fileName 文件名称
     */
    public File get(String fileName){
        new File(this.properties.getProperty(EXCEL_PATH)).mkdirs();
        String tempFileName = this.properties.getProperty(EXCEL_PATH) + fileName;
        File templateFile = this.creatTemplate(fileName);
        EasyExcel.write(tempFileName).withTemplate(templateFile).sheet().doFill(regexExecutor.getResultList());
        if (this.properties.containsKey(DELETE_TEMPLATE) && Boolean.TRUE.toString().equals(this.properties.getProperty(DELETE_TEMPLATE))){
            templateFile.delete();
        }
        return new File(tempFileName);
    }

    /**
     * 获取表格的表头列表
     * @return {@link List}<{@link List}<{@link String}>> excel的表头
     */
    private List<List<String>> getHead(){
        List<String> nameSet = regexExecutor.getNameList();
        Map<String, String> nameMap = regexExecutor.getNameMap();
        List<List<String>> list = new ArrayList<>();
        list.add(List.of("目标"));
        list.add(List.of("完整匹配"));
        list.add(List.of("错误信息"));

        if(nameMap != null && !nameMap.isEmpty()){
            nameSet.forEach(name->{
                List<String> head = new ArrayList<>();
                head.add(nameMap.get(name) == null ? name : nameMap.get(name));
                list.add(head);
            });
        return list;
        }

        nameSet.forEach(name->{
            List<String> head = new ArrayList<>();
            head.add(name);
            list.add(head);
        });
        return list;
    }

    /**
     * 创建一个用于填充数据的excel模板
     * @param fileName 文件名称
     * @return {@link File} 模板文件
     */
    private File creatTemplate(String fileName){
        new File(this.properties.getProperty(TEMPLATE_PATH)).mkdirs();
        String tempFileName = this.properties.getProperty(TEMPLATE_PATH) + "temp_" + fileName;
        List<List<String>> templateData = new ArrayList<>();
        List<List<String>> heads = this.getHead();
        List<String> data = new ArrayList<>();
        heads.forEach(head-> data.add("{." + head.get(0) + "}"));
        templateData.add(data);
        EasyExcel.write(tempFileName).head(heads).sheet("result").doWrite(templateData);
        return new File(tempFileName);
    }
}
