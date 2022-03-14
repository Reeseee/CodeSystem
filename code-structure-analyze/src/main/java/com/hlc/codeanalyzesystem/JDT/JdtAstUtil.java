package com.hlc.codeanalyzesystem.JDT;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.jdt.core.dom.*;

public class JdtAstUtil {
    /**
     * get compilation unit of source code
     * @param javaFilePath
     * @return CompilationUnit
     */
    public static CompilationUnit getCompilationUnit(String javaFilePath){
        byte[] input = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(javaFilePath));
            input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound Exception");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }


        ASTParser astParser = ASTParser.newParser(AST.JLS4);
        astParser.setSource(new String(input).toCharArray());
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        astParser.setResolveBindings(true);
        astParser.setBindingsRecovery(true);
        astParser.setEnvironment(new String[]{"C:\\Program Files\\Java\\jdk1.8.0_221\\bin"}, null, null, true);
        astParser.setUnitName("any_name");
        CompilationUnit result = (CompilationUnit) (astParser.createAST(null));

        return result;
    }



    public static Type extractType(VariableDeclaration variableDeclaration) {
        Type returnedVariableType = null;
        if(variableDeclaration instanceof SingleVariableDeclaration) {
            SingleVariableDeclaration singleVariableDeclaration = (SingleVariableDeclaration)variableDeclaration;
            returnedVariableType = singleVariableDeclaration.getType();
        }
        else if(variableDeclaration instanceof VariableDeclarationFragment) {
            VariableDeclarationFragment fragment = (VariableDeclarationFragment)variableDeclaration;
            if(fragment.getParent() instanceof VariableDeclarationStatement) {
                VariableDeclarationStatement variableDeclarationStatement = (VariableDeclarationStatement)fragment.getParent();
                returnedVariableType = variableDeclarationStatement.getType();
            }
            else if(fragment.getParent() instanceof VariableDeclarationExpression) {
                VariableDeclarationExpression variableDeclarationExpression = (VariableDeclarationExpression)fragment.getParent();
                returnedVariableType = variableDeclarationExpression.getType();
            }
            else if(fragment.getParent() instanceof FieldDeclaration) {
                FieldDeclaration fieldDeclaration = (FieldDeclaration)fragment.getParent();
                returnedVariableType = fieldDeclaration.getType();
            }
        }
        return returnedVariableType;
    }
}