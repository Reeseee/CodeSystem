package com.hlc.codeanalyzesystem.ComplexityAlgorithm.Halstead;

import org.eclipse.jdt.core.dom.*;

import java.util.HashSet;
import java.util.Set;

public class HalsteadVisitor2 extends ASTVisitor{
    HashSet<String> names = new HashSet<String>();
    HashSet<String> snames = new HashSet<String>();
    int operators=0;
    int operands=0;
    public int getoperators()
    {
        return operators;
    }
    public int getoperands()
    {
        return operands;
    }
    public Set getNames()
    {
        return names;
    }
    public Set getSnames()
    {
        return snames;
    }
    //gets variables declared
    public boolean visit(VariableDeclarationFragment node) {
        if(node.getInitializer()!=null)
        {
            this.names.add(node.getName().toString());
            operands=operands+1;
        }
        return true;
    }
    //gets characters
    public boolean visit(CharacterLiteral node) {
        operands=operands+1;
        this.names.add("char");
        return true;
    }
    //gets strings
    public boolean visit(StringLiteral node) {
        operands=operands+1;
        this.names.add("String");
        return true;
    }
    //gets nulls
    public boolean visit(NullLiteral node) {
        operands=operands+1;
        this.names.add("null");
        return true;
    }
    //gets numbers
    public boolean visit(NumberLiteral node) {
        operands=operands+1;
        this.names.add("number");
        return true;
    }
    //gets boolean
    public boolean visit(BooleanLiteral node) {
        operands=operands+1;
        this.names.add("boolean");
        return true;
    }
    //gets assignment
    public boolean visit(Assignment node) {
        operators=operators+1;
        this.snames.add(node.getOperator().toString());
        return true;
    }
    //gets simple name
    public boolean visit(SimpleName node) {
        operands=operands+1;
        this.names.add(node.getIdentifier());
        return true;
    }
    //gets single variable
    public boolean visit(SingleVariableDeclaration node) {
        operands=operands+1;
        this.names.add(node.getName().toString());
        return true;
    }
    //gets primitive types
    public boolean visit(PrimitiveType node)
    {
        this.names.add(node.getPrimitiveTypeCode().toString());
        operands=operands+1;
        return true;
    }
    //gets for statements
    public boolean visit(ForStatement node) {
        this.snames.add("for");
        operators=operators+1;
        return true;
    }
    //gets prefix expressions
    public boolean visit(PrefixExpression node) {
        this.snames.add(node.getOperator().toString());
        operators=operators+1;
        this.names.add(node.getOperand().toString());
        operands=operands+1;
        return true;
    }
    //gets postfix expressions
    public boolean visit(PostfixExpression node) {

        this.snames.add(node.getOperator().toString());
        operators=operators+1;
        this.names.add(node.getOperand().toString());
        operands=operands+1;
        return true;
    }
    //gets infix expressions
    public boolean visit(InfixExpression node) {
        this.snames.add(node.getOperator().toString());
        operators=operators+1;
        this.names.add(node.getLeftOperand().toString());
        operands=operands+1;
        this.names.add(node.getRightOperand().toString());
        operands=operands+1;
        return true;
    }
    //gets if statements
    public boolean visit(IfStatement node) {
        this.snames.add("if");
        operators=operators+1;
        return true;
    }
    //gets while statements
    public boolean visit(WhileStatement node) {
        operators=operators+1;
        this.snames.add("while");
        return true;
    }

//    public boolean visit(MethodDeclaration node) {
//        node.accept(new ASTVisitor() {
//            //gets variables declared
//            public boolean visit(VariableDeclarationFragment node) {
//                if(node.getInitializer()!=null)
//                {
//                    names.add(node.getName().toString());
//                    operands=operands+1;
//                }
//                return true;
//            }
//            //gets characters
//            public boolean visit(CharacterLiteral node) {
//                operands=operands+1;
//                names.add("char");
//                return true;
//            }
//            //gets strings
//            public boolean visit(StringLiteral node) {
//                operands=operands+1;
//                names.add("String");
//                return true;
//            }
//            //gets nulls
//            public boolean visit(NullLiteral node) {
//                operands=operands+1;
//                names.add("null");
//                return true;
//            }
//            //gets numbers
//            public boolean visit(NumberLiteral node) {
//                operands=operands+1;
//                names.add("number");
//                return true;
//            }
//            //gets boolean
//            public boolean visit(BooleanLiteral node) {
//                operands=operands+1;
//                names.add("boolean");
//                return true;
//            }
//            //gets assignment
//            public boolean visit(Assignment node) {
//                operators=operators+1;
//                snames.add(node.getOperator().toString());
//                return true;
//            }
//            //gets simple name
//            public boolean visit(SimpleName node) {
//                operands=operands+1;
//                names.add(node.getIdentifier());
//                return true;
//            }
//            //gets single variable
//            public boolean visit(SingleVariableDeclaration node) {
//                operands=operands+1;
//                names.add(node.getName().toString());
//                return true;
//            }
//            //gets primitive types
//            public boolean visit(PrimitiveType node)
//            {
//                names.add(node.getPrimitiveTypeCode().toString());
//                operands=operands+1;
//                return true;
//            }
//            //gets for statements
//            public boolean visit(ForStatement node) {
//                snames.add("for");
//                operators=operators+1;
//                return true;
//            }
//            //gets prefix expressions
//            public boolean visit(PrefixExpression node) {
//                snames.add(node.getOperator().toString());
//                operators=operators+1;
//                names.add(node.getOperand().toString());
//                operands=operands+1;
//                return true;
//            }
//            //gets postfix expressions
//            public boolean visit(PostfixExpression node) {
//
//                snames.add(node.getOperator().toString());
//                operators=operators+1;
//                names.add(node.getOperand().toString());
//                operands=operands+1;
//                return true;
//            }
//            //gets infix expressions
//            public boolean visit(InfixExpression node) {
//                snames.add(node.getOperator().toString());
//                operators=operators+1;
//                names.add(node.getLeftOperand().toString());
//                operands=operands+1;
//                names.add(node.getRightOperand().toString());
//                operands=operands+1;
//                return true;
//            }
//            //gets if statements
//            public boolean visit(IfStatement node) {
//                snames.add("if");
//                operators=operators+1;
//                return true;
//            }
//            //gets while statements
//            public boolean visit(WhileStatement node) {
//                operators=operators+1;
//                snames.add("while");
//                return true;
//            }
//        });
//        return true;
//    }

}
