package com.yunjuanyunshu.util;

import com.intellij.ide.util.TreeClassChooser;
import com.intellij.ide.util.TreeClassChooserFactory;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.yunjuanyunshu.CodeTemplate;
import com.yunjuanyunshu.Entity.ClassEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hansong.xhs
 * @version $Id: CodeMakerUtil.java, v 0.1 2017-01-20 10:15 hansong.xhs Exp $$
 */
public class CodeMakerUtil {

    public static Logger getLogger(Class clazz) {
        return Logger.getInstance(clazz);
    }

    public static PsiClass chooseClass(Project project, PsiClass defaultClass) {
        TreeClassChooser chooser = TreeClassChooserFactory.getInstance(project)
            .createProjectScopeChooser("Select a class", defaultClass);

        chooser.showDialog();

        return chooser.getSelected();
    }

    public static String getSourcePath(PsiClass clazz) {
        String classPath = clazz.getContainingFile().getVirtualFile().getPath();
        return classPath.substring(0, classPath.lastIndexOf('/'));
    }

    public static String getSourceBasePath(Project project) {

        return  project.getBasePath();
        //String classPath = project.getProjectFilePath();
        //return classPath.substring(0, classPath.lastIndexOf('/'));
    }

    public static String generateClassPath(String sourcePath, String className) {
        return sourcePath + "/" + className + ".java";
    }

    public static String generateClassPath(Project project,String packagePath, String className) {
        return project.getBasePath() + "/src/" + packagePath.replace(".","/") +"/" + className +".java";
    }
    public static String generateClassPath(Project project,String packagePath, String className,CodeTemplate codeTemplate) {
        return project.getBasePath() + "/src/" + packagePath.replace(".","/") +"/" + className+codeTemplate.getClassNameVm() +".java";
    }

    /**
     * Gets the javafile that's currently selected in the editor. Returns null if it's not a java file.
     *
     * @param dataContext data context.
     * @return The current javafile. Null if not a javafile.
     */
    public static PsiJavaFile getSelectedJavaFile(DataContext dataContext) {
        final PsiFile psiFile = (PsiFile) dataContext.getData("psi.File");

        if (!(psiFile instanceof PsiJavaFile)) {
            return null;
        } else {
            return (PsiJavaFile) psiFile;
        }
    }

    public static List<String> getImportList(PsiJavaFile javaFile) {
        PsiImportList importList = javaFile.getImportList();
        if (importList == null) {
            return new ArrayList<>();
        }
        return Arrays.stream(importList.getImportStatements())
            .map(PsiImportStatement::getQualifiedName).collect(Collectors.toList());
    }

    public static List<ClassEntry.Field> getFields(PsiClass psiClass) {
        return Arrays
            .stream(psiClass.getFields())
            .map(
                psiField -> new ClassEntry.Field(psiField.getType().getPresentableText(), psiField
                    .getName(), psiField.getModifierList() == null ? "" : psiField
                    .getModifierList().getText())).collect(Collectors.toList());
    }

    public static List<ClassEntry.Method> getMethods(PsiClass psiClass) {
        return Arrays
            .stream(psiClass.getMethods())
            .map(
                psiMethod -> {
                    String returnType = psiMethod.getReturnType() == null ? "" : psiMethod
                        .getReturnType().getPresentableText();
                    return new ClassEntry.Method(psiMethod.getName(), psiMethod.getModifierList()
                        .getText(), returnType, psiMethod.getParameterList().getText());
                }).collect(Collectors.toList());
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }

        int length = str.length();

        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
