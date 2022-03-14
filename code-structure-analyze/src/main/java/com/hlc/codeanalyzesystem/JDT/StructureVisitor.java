package com.hlc.codeanalyzesystem.JDT;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StructureVisitor extends ASTVisitor {

    List<TypeDeclaration> classList = new ArrayList<>();
    List<MethodDeclaration> functionList = new ArrayList<>();
    List<VariableDeclarationFragment> globalVarList = new ArrayList<>();
    Map<MethodDeclaration,List<VariableDeclarationFragment>> localVarMap = new HashMap<>();
    List<String> calls = new ArrayList<>();

    @Override
    public boolean visit(FieldDeclaration node) {
        for (Object obj: node.fragments()) {
            VariableDeclarationFragment v = (VariableDeclarationFragment)obj;
            globalVarList.add(v);
        }
        return true;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        functionList.add(node);
        node.accept(new ASTVisitor() {
            public boolean visit(VariableDeclarationFragment fd) {
                List<VariableDeclarationFragment> list = localVarMap.getOrDefault(node,new ArrayList<>());
                list.add(fd);
                localVarMap.put(node,list);
                return true;
            }

        });
        return true;
    }

    @Override
    public boolean visit(TypeDeclaration node) {
        classList.add(node);
        return true;
    }

    @Override
    public boolean visit(MethodInvocation node){
        //calls.add("MethodInvocation:\t" + node.toString());
        //calls.add("\tExpression: " + node.getExpression());
        Expression expression = node.getExpression();
        if(expression != null) {
            ITypeBinding  typeBinding = expression.resolveTypeBinding();
            if (typeBinding != null) {
                calls.add(typeBinding.getName());
                //calls.add("\tType: " + typeBinding.getQualifiedName());
            }
        }
        //calls.add("\tName: " + node.getName());
        return true;
    }
}

