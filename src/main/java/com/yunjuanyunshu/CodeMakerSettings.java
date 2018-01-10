package com.yunjuanyunshu;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@State(name = "CodeGenerateSettings", storages = { @Storage(id = "app-default", file = "$APP_CONFIG$/CodeGenerate-settings.xml") })
public class CodeMakerSettings implements PersistentStateComponent<CodeMakerSettings> {

    public final static String MODEL  = "########################################################################################\n"
                                           + "##\n"
                                           + "## Common variables:\n"
                                           + "##  $YEAR - yyyy\n"
                                           + "##  $TIME - yyyy-MM-dd HH:mm:ss\n"
                                           + "##  $USER - user.name\n"
                                           + "##\n"
                                           + "## Available variables:\n"
                                           + "##  $class0 - the context class\n"
                                           + "##  $class1 - the selected class, like $class2, $class2\n"
                                           + "##  $ClassName - generate by the config of \"Class Name\", the generated class name\n"
                                           + "##\n"
                                           + "## Class Entry Structure:\n"
                                           + "##  $class0.className - the class Name\n"
                                           + "##  $class0.packageName - the packageName\n"
                                           + "##  $class0.importList - the list of imported classes name\n"
                                           + "##  $class0.fields - the list of the class fields\n"
                                           + "##          - type: the field type\n"
                                           + "##          - name: the field name\n"
                                           + "##          - modifier: the field modifier, like \"private\"\n"
                                           + "##  $class0.methods - the list of class methods\n"
                                           + "##          - name: the method name\n"
                                           + "##          - modifier: the method modifier, like \"private static\"\n"
                                           + "##          - returnType: the method returnType\n"
                                           + "##          - params: the method params, like \"(String name)\"\n"
                                           + "##\n"
                                           + "########################################################################################\n"
                                           + "package $class0.PackageName;\n"
                                           + "\n"
                                           + "#foreach($importer in $class0.ImportList)\n"
                                           + "import $importer;\n"
                                           + "#end\n"
                                           + "import lombok.Getter;\n"
                                           + "import lombok.Setter;\n"
                                           + "\n"
                                           + "/**\n"
                                           + " *\n"
                                           + " * @author $USER\n"
                                           + " * @version $Id: ${ClassName}.java, v 0.1 $TIME $USER Exp $$\n"
                                           + " */\n"
                                           + "class $ClassName {\n"
                                           + "\n"
                                           + "#foreach($field in $class0.Fields)\n"
                                           + "    /**\n"
                                           + "     *\n"
                                           + "     */\n"
                                           + "    @Getter\n"
                                           + "    @Setter\n"
                                           + "    private $field.Type $field.Name;\n"
                                           + "\n"
                                           + "#end\n" + "\n" + "}\n";
    public final static String example =
            "##  函数声明使用范例如下 \n"+
            "#macro (cap $strIn)$strIn.valueOf($strIn.charAt(0)).toUpperCase()$strIn.substring(1)#end\n" +
            "#macro (low $strIn)$strIn.valueOf($strIn.charAt(0)).toLowerCase()$strIn.substring(1)#end\n" +
            "\n" +
            "/**\n" +
            " * \n" +
            " * Copyright (c) 2004-$YEAR All Rights Reserved.\n" +
            " */\n" +
            "package $TableEntity.PackageStr;\n" +
            "\n" +
            "\n" +
            "/**\n" +
            " * ${TableEntity.tableJavaDesc}\n" +
            " * @author $USER\n" +
            " * @version ${TableEntity.tableJavaName}.java, v 0.1 $TIME $USER Exp $$\n" +
            " */\n" +
            "class ${TableEntity.tableJavaName} {\n" +
            "\n## 循环使用范例如下 \n"+
            "#foreach($field in $TableEntity.ColumnEntityList)\n" +
            "    /**\n" +
            "     * ${field.colJavaDesc}\n" +
            "     */\n##  判断语句使用范例如下 \n" +
            "  #if($field.colJavaNullAble)\n" +
            "     @CanNull(${field.colJavaNullAble})\n" +
            "  #end\n" +
            "  #if($field.colJavaType.equals(\"String\"))\n" +
            "     @Length(${field.colDBLength})\n" +
            "  #end\n## null 判断 直接使用变量判断即可 null 在此时可以理解位 false\n" +
            "  #if($field.colRangMax && ($field.colJavaType.equals(\"long\") || $field.colJavaType.equals(\"float\") || $field.colJavaType.equals(\"double\") || $field.colJavaType.equals(\"int\")))\n" +
            "     @Max(${field.colRangMax.doubleValue()})\n" +
            "  #end\n" +
            "  #if($field.colRangMin && ($field.colJavaType.equals(\"long\") || $field.colJavaType.equals(\"float\") || $field.colJavaType.equals(\"double\") || $field.colJavaType.equals(\"int\")))\n" +
            "     @Min(${field.colRangMin.doubleValue()})\n" +
            "  #end\n" +
            "     private ${field.colJavaType} ${field.colJavaName} ;\n" +
            "#end\n" +
            "\n" +
            "\n" +
            "#foreach($field in $TableEntity.ColumnEntityList)\n" +
            "    /**\n" +
            "     * 获取${field.colJavaDesc}\n" +
            "     * @return ${field.colJavaDesc}\n" +
            "     */\n" +
            "    private ${field.colJavaType} get#cap(${field.colJavaName})(){\n" +
            "        return ${field.colJavaName};\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "     * 设置${field.colJavaDesc}\n" +
            "     * @param ${field.colJavaName} ${field.colJavaDesc}\n" +
            "     */\n" +
            "    private void set#cap(${field.colJavaName})(${field.colJavaType} ${field.colJavaName}){\n" +
            "        this.${field.colJavaName} = ${field.colJavaName};\n" +
            "    }\n" +
            "\n" +
            "#end\n" +
            "\n" +
            "}";

    public CodeMakerSettings() {
        loadDefaultSettings();
    }

    public void loadDefaultSettings() {
        Map<String, CodeTemplate> codeTemplates = new HashMap<>();
        codeTemplates.put("Example", new CodeTemplate("Example",
            "${TableEntity.tableJavaName}.java",
                example, 1));
        this.codeTemplates = codeTemplates;
    }

    private Map<String, CodeTemplate> codeTemplates;

    @Nullable
    @Override
    public CodeMakerSettings getState() {
        return this;
    }

    @Override
    public void loadState(CodeMakerSettings codeMakerSettings) {
        XmlSerializerUtil.copyBean(codeMakerSettings, this);
    }

    public CodeTemplate getCodeTemplate(String template) {
        return codeTemplates.get(template);
    }

    public void removeCodeTemplate(String template) {
        codeTemplates.remove(template);
    }

    /**
     * Getter method for property <tt>codeTemplates</tt>.
     *
     * @return property value of codeTemplates
     */
    public Map<String, CodeTemplate> getCodeTemplates() {
        return codeTemplates;
    }

    /**
     * Getter method for property <tt>codeTemplates</tt>.
     *
     * @return property value of codeTemplates
     */
    public String[] getCodeTemplateNamesArray() {
        List<String> children = getCodeTemplateNamesList();
        if(children == null || children.size() == 0)
            return null;
        return children.toArray(new String[children.size()]);
    }

    /**
     * Getter method for property <tt>codeTemplates</tt>.
     *
     * @return property value of codeTemplates
     */
    public List<String> getCodeTemplateNamesList() {
        List<String> children = new ArrayList<>();
        Map<String, CodeTemplate> tmpMap = getCodeTemplates();
        tmpMap.forEach((key, value) -> children.add(key));
        return children;
    }

    /**
     * Setter method for property <tt>codeTemplates</tt>.
     *
     * @param codeTemplates value to be assigned to property codeTemplates
     */
    public void setCodeTemplates(Map<String, CodeTemplate> codeTemplates) {
        this.codeTemplates = codeTemplates;
    }
}
