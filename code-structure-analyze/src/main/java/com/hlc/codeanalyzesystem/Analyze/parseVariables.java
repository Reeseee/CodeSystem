package com.hlc.codeanalyzesystem.Analyze;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class parseVariables {

    private List<String> getVariableNameFromLine(String line){
        List<String> variableList = new ArrayList<>();
        int n = line.length();
        int i = 0;
        boolean typeDefined = false;
        boolean inAssign = false;
        int inParentheses = 0;
        boolean canAdd = false;
        while (i < n)
        {
            if(inAssign == false)
            {
                if(line.charAt(i) == ' ')
                {
                    i++;
                }

                else if(line.charAt(i) == '=')
                {
                    inAssign = true;
                    i++;
                }
                if(line.charAt(i) == '('||line.charAt(i) == '{')
                {
                    inParentheses++;
                    i++;
                }
                else if(line.charAt(i) == ')'||line.charAt(i) == '}')
                {
                    inParentheses--;
                    i++;
                }
                else if(line.charAt(i) == ',')
                {
                    if(inParentheses == 0)
                        inAssign = false;
                    i++;
                }
                else
                {
                    int l = i;
                    while (i < n)
                    {
                        if(Character.isLetterOrDigit(line.charAt(i))||line.charAt(i)=='_')
                        {
                            i++;
                        }
                        else
                        {
                            break;
                        }
                    }
                    String word = line.substring(l,i);
                    if(!word.equals("static") && !word.equals("public") && !word.equals("protected") && !word.equals("private"))
                    {
                        if(inAssign == false && inParentheses == 0 && typeDefined == false)
                        {
                            typeDefined = true;
                            canAdd = true;
                        }

                        else if(typeDefined == true && inAssign == false && inParentheses == 0 && canAdd == true)
                        {
                            if(!word.equals(""));
                            variableList.add(word);
                        }
                    }
                    i++;
                }
            }
            else
            {
                if(line.charAt(i) == '('||line.charAt(i) == '{')
                {
                    inParentheses++;
                    i++;
                }
                else if(line.charAt(i) == ')'||line.charAt(i) == '}')
                {
                    inParentheses--;
                    i++;
                }
                else if(line.charAt(i) == ',')
                {
                    if(inParentheses == 0)
                    {
                        inAssign = false;
                        canAdd = true;
                    }

                    i++;
                }
                else
                {
                    i++;
                }
            }

        }


        return variableList;

    }


    public static void main(String[] args) {
        //                                                   public        static              returnType                    name           (int a,int b)
        String variableHeadRegex = "(public|private|protected){0,1}\\s{0,}(static){0,1}\\s{0,}([\\w\\<\\>\\[\\]]{1,})\\s{1,}[\\w]{1,}\\s{0,}\\(";
        //from stackOverFlow                                                  这前面是限定符|    这一段是方法类型|    方法名|     括号传参   |   { 同时不匹配;
        String functionRegex = "(public|protected|private|static|native|synchronized|\\s)+[\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^\\)]*\\) *(\\{?|[^;])";
        //from Quora
        //String functionRegex = "(public|private|static|protected|abstract|native|synchronized)* +([a-zA-Z0-9<>._?, ]*) +([a-zA-Z0-9_]+) *\\([a-zA-Z0-9<>\\[\\]._?, \n]*\\) *([a-zA-Z0-9_ ,\n]*) *\\{";
        Pattern variablePattern = Pattern.compile(functionRegex);
        Matcher variableMatcher = variablePattern.matcher("Service ser = new Service()");
        System.out.println(variableMatcher.matches());
    }

}
