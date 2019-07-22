package codeGenerator;

import freemarker.template.TemplateExceptionHandler;
import io.swagger.annotations.ApiModelProperty;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代码生成器
 */
public class CodeGenerator {

    private static final String PACKAGE = "starter.temp";//生成代码的包名

    private static final String PACKAGE_PATH = packageConvertPath();//生成的代码存放路径

    private static final String PROJECT_PATH = System.getProperty("user.dir") + "/";//项目在硬盘上的基础路径

    private static final String TEMPLATE_FILE_PATH = PROJECT_PATH + "/src/test/resources";//模板位置

    private static final String JAVA_PATH = "/src/main/java"; //java文件路径

    private static final String AUTHOR = "zhyf";//@author

    private static final String DATE = new SimpleDateFormat("yyyy/MM/dd").format(new Date());//@date

    public static void main(String[] args) {
        //step1(Article.class, "xxxx");
        //step2(Article.class, "xxxx");
    }

    private static void step1(Class<?> clazz, String module) {
        String modelNameUpperCamel = clazz.getSimpleName();
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>();
            data.put("author", AUTHOR);
            data.put("date", DATE);
            data.put("module", module);
            data.put("pkg", PACKAGE);
            data.put("fields", getFieldsInfo(clazz));
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", toLowerCaseFirstOne(modelNameUpperCamel));

            File file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH + modelNameUpperCamel + "Request.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("request.ftl").process(data, new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Request.java 生成成功");

            file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH + modelNameUpperCamel + "PageRequest.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("pageRequest.ftl").process(data, new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Request.java 生成成功");

            file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH + modelNameUpperCamel + "Response.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("response.ftl").process(data, new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Response.java 生成成功");

            file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH + modelNameUpperCamel + "Repository.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("repository.ftl").process(data, new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Repository.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Repository失败", e);
        }
    }

    private static void step2(Class<?> clazz, String module) {
        String modelNameUpperCamel = clazz.getSimpleName();
        try {
            freemarker.template.Configuration cfg = getConfiguration();

            Map<String, Object> data = new HashMap<>();
            data.put("author", AUTHOR);
            data.put("date", DATE);
            data.put("module", module);
            data.put("pkg", PACKAGE);
            data.put("modelNameUpperCamel", modelNameUpperCamel);
            data.put("modelNameLowerCamel", toLowerCaseFirstOne(modelNameUpperCamel));

            File file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH + modelNameUpperCamel + "Service.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("service.ftl").process(data, new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Service.java 生成成功");

            file = new File(PROJECT_PATH + JAVA_PATH + PACKAGE_PATH + modelNameUpperCamel + "Ctrl.java");
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            cfg.getTemplate("ctrl.ftl").process(data, new FileWriter(file));
            System.out.println(modelNameUpperCamel + "Ctrl.java 生成成功");
        } catch (Exception e) {
            throw new RuntimeException("生成Ctrl失败", e);
        }
    }

    private static freemarker.template.Configuration getConfiguration() throws IOException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setDirectoryForTemplateLoading(new File(TEMPLATE_FILE_PATH));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return cfg;
    }

    private static List<ClassBean> getFieldsInfo(Class<?> clazz) {
        List<ClassBean> list = new LinkedList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field f : fields) {
            for (Annotation annotation : f.getAnnotations()) {
                if (annotation instanceof ApiModelProperty) {
                    String value = ((ApiModelProperty) annotation).value();
                    String field = f.getType().getSimpleName() + " " + f.getName();
                    list.add(new ClassBean(value, field));

                    System.out.println("@ApiModelProperty(value = \"" + value + "\")");
                    System.out.println("private " + f.getType().getSimpleName() + " " + f.getName() + ";");
                    System.out.println("---------------------------------------------");
                }
            }
        }
        return list;
    }

    /** 首字母转小写 */
    private static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        } else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }

    private static String packageConvertPath() {
        return String.format("/%s/", CodeGenerator.PACKAGE.replaceAll("\\.", "/"));
    }

}
