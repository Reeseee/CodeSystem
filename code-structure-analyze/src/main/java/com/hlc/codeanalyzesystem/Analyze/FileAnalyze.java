package com.hlc.codeanalyzesystem.Analyze;

import com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap.Ex_caculate_test;
import com.hlc.codeanalyzesystem.entity.Fileresult;
import com.hlc.codeanalyzesystem.util.PythonCallUtil;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class FileAnalyze {
    //类表
    Map<String,Integer> classMap = new HashMap<>();
    //方法表
    Map<String,Integer> functionMap = new HashMap<>();
    //变量表
    Map<String,String> variableMap = new HashMap<>();
    Map<Integer,VariableInfo> variableInfoMap = new HashMap<>();

    Stack<NamePosPair>  annotationStack = new Stack<>();
    Stack<NamePosPair> classStack = new Stack<>();
    Stack<NamePosPair> functionStack = new Stack<>();
    Stack<NamePosPair> cStack = new Stack<>();

    Set<String> internalImportSet = new HashSet<>();

    Set<String> internalSingleImportSet = new HashSet<>();

    Set<String> fileDependencySet = new HashSet<>();

    Set<String> callSet = new HashSet<>();

    String fullyQualifiedName = "";

    String packgeName = "";

    List<String> innerClass = new ArrayList<>();

    int variableIdCount = 0;

    int annotationCount = 0;

    //文件总行数
    int lineCount = 0;
    // 声明语句
    int statementCount = 0;
    // 执行语句
    int executeCount = 0;
    //空白行数
    int blankLine = 0;

    //是否在注释中
    int inAnnotation = 0;

    // 行数最多的类
    String longestClass;
    int longestClassLineCount = 0;

    // 行数最多的函数
    String longestFunction;
    int longestFunctionLineCount =0;

    Map<String,String> callClassMapping = new HashMap<>();

    public Set<String> getInternalSingleImportSet() {
        return internalSingleImportSet;
    }

    public void setInternalSingleImportSet(Set<String> internalSingleImportSet) {
        this.internalSingleImportSet = internalSingleImportSet;
    }

    public Set<String> getCallSet() {
        return callSet;
    }

    public void setCallSet(Set<String> callSet) {
        this.callSet = callSet;
    }

    public List<String> getInnerClass() {
        return innerClass;
    }

    public void setInnerClass(List<String> innerClass) {
        this.innerClass = innerClass;
    }

    public Map<String, String> getCallClassMapping() {
        return callClassMapping;
    }

    public void setCallClassMapping(Map<String, String> callClassMapping) {
        this.callClassMapping = callClassMapping;
    }

    public Map<Integer, VariableInfo> getVariableInfoMap() {
        return variableInfoMap;
    }

    public void setVariableInfoMap(Map<Integer, VariableInfo> variableInfoMap) {
        this.variableInfoMap = variableInfoMap;
    }

    public Set<String> getInternalImportSet() {
        return internalImportSet;
    }

    public void setInternalImportSet(Set<String> internalImportSet) {
        this.internalImportSet = internalImportSet;
    }

    public Set<String> getFileDependencySet() {
        return fileDependencySet;
    }

    public void setFileDependencySet(Set<String> fileDependencySet) {
        this.fileDependencySet = fileDependencySet;
    }

    public String getLongestClass() {
        return longestClass;
    }

    public void setLongestClass(String longestClass) {
        this.longestClass = longestClass;
    }

    public int getLongestClassLineCount() {
        return longestClassLineCount;
    }

    public void setLongestClassLineCount(int longestClassLineCount) {
        this.longestClassLineCount = longestClassLineCount;
    }

    public String getLongestFunction() {
        return longestFunction;
    }

    public void setLongestFunction(String longestFunction) {
        this.longestFunction = longestFunction;
    }

    public int getLongestFunctionLineCount() {
        return longestFunctionLineCount;
    }

    public void setLongestFunctionLineCount(int longestFunctionLineCount) {
        this.longestFunctionLineCount = longestFunctionLineCount;
    }

    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }

    public void setFullyQualifiedName(String fullyQualifiedName) {
        this.fullyQualifiedName = fullyQualifiedName;
    }

    public String getPackgeName() {
        return packgeName;
    }

    public void setPackgeName(String packgeName) {
        this.packgeName = packgeName;
    }

    public Map<String, Integer> getClassMap() {
        return classMap;
    }

    public void setClassMap(Map<String, Integer> classMap) {
        this.classMap = classMap;
    }

    public Map<String, Integer> getFunctionMap() {
        return functionMap;
    }

    public void setFunctionMap(Map<String, Integer> functionMap) {
        this.functionMap = functionMap;
    }

    public Map<String, String> getVariableMap() {
        return variableMap;
    }

    public void setVariableMap(Map<String, String> variableMap) {
        this.variableMap = variableMap;
    }

    public Stack<NamePosPair> getAnnotationStack() {
        return annotationStack;
    }

    public void setAnnotationStack(Stack<NamePosPair> annotationStack) {
        this.annotationStack = annotationStack;
    }

    public Stack<NamePosPair> getClassStack() {
        return classStack;
    }

    public void setClassStack(Stack<NamePosPair> classStack) {
        this.classStack = classStack;
    }

    public int getAnnotationCount() {
        return annotationCount;
    }

    public void setAnnotationCount(int annotationCount) {
        this.annotationCount = annotationCount;
    }

    public int getLineCount() {
        return lineCount;
    }

    public void setLineCount(int lineCount) {
        this.lineCount = lineCount;
    }

    public int getStatementCount() {
        return statementCount;
    }

    public void setStatementCount(int statementCount) {
        this.statementCount = statementCount;
    }

    public int getExecuteCount() {
        return executeCount;
    }

    public void setExecuteCount(int executeCount) {
        this.executeCount = executeCount;
    }

    public int getBlankLine() {
        return blankLine;
    }

    public void setBlankLine(int blankLine) {
        this.blankLine = blankLine;
    }

    public int getInAnnotation() {
        return inAnnotation;
    }

    public void setInAnnotation(int inAnnotation) {
        this.inAnnotation = inAnnotation;
    }


    public void init(){
        classMap = new HashMap<>();
        functionMap = new HashMap<>();
        variableMap = new HashMap<>();
        classStack = new Stack<>();
        lineCount = 0;
        statementCount = 0;
        executeCount = 0;
        blankLine = 0;
        fullyQualifiedName = "";
        fileDependencySet = new HashSet<>();
    }

    public Fileresult fileAnalyze(String path) throws Exception {
        File sourceFile = new File(path);
        BufferedReader in = new BufferedReader(new FileReader(sourceFile));
        String  classHeadRegex= "(public|private|protected){0,1}\\s{0,1}(class)\\s{0,}[\\w]{1,}";
        
        //旧版 已弃用
        //        String  functionHeadRegex= "(public|private|protected){0,1}\\s{0,1}[\\w]{1,}\\s[\\w]{1,}\\(";
        
        //from Quora 好像也能用 而且据回答者说实验中准度很高，但没有明白在传参括号后的那一段作用 暂时不用
        //String functionRegex = "(public|private|static|protected|abstract|native|synchronized)* +([a-zA-Z0-9<>._?, ]*) +([a-zA-Z0-9_]+) *\\([a-zA-Z0-9<>\\[\\]._?, \n]*\\) *([a-zA-Z0-9_ ,\n]*) *\\{";
        //from stackOverflow2
        //String functionHeadRegex = "(?:(?:public|private|protected|static|final|native|synchronized|abstract|transient)+\s+)+[$_\w<>\[\]\s]*\s+[\$_\w]+\([^\)]*\)?\s*\{?[^\}]*\}?";

        //from stackOverFlow                                                      这前面是限定符|    这一段是方法类型|    方法名|     括号传参   |   throws  Exception                         { 同时不匹配;
        String functionHeadRegex = "(public|protected|private|static|native|synchronized|\\s)+[\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^\\)]*\\) (throws\\s{1,}\\w{0,}Exception\\s{0,}){0,1} (\\{?|[^;])";
        //from stackOverFlow                                               这前面是限定符|        这一段是类型|               变量名|
        String variableHeadRegex = "(public|protected|private|static|synchronized|\\s)+[\\w\\[\\]\\.]{1,}\\s{1,}[\\w\\[\\]]{1,}\\s{0,}[\\;\\=\\,]";
        Pattern functionPattern = Pattern.compile(functionHeadRegex);
        Pattern classPattern = Pattern.compile(classHeadRegex);
        Pattern variablePattern = Pattern.compile(variableHeadRegex);

        boolean nearDeclarationHead = false;

        String line;
        while((line = in.readLine())!=null)
        {
            lineCount++;
            line.trim();
            if(line.length() == 0) {
                blankLine++;
            }
            //处理包名以及全限定类名
            else if(fullyQualifiedName.equals("")&&line.startsWith("package")){
                packgeName = getQualifiedNameFromLine(line);
                String sourceName = sourceFile.getName();
                String fileNameWithoutSuffix = sourceName.substring(0,sourceName.length()-5);
                fullyQualifiedName = packgeName + fileNameWithoutSuffix;
            }
            else{
                Matcher classMatcher = classPattern.matcher(line);
                //检查是否为类头
                if(classMatcher.find())
                {
                    String className = getClassNameFromLine(line);
                    classMap.put(className,-1);
                    classStack.push(new NamePosPair("class_"+className,lineCount));
                    cStack.push(new NamePosPair(className,lineCount));
                    if(cStack.size() > 1)
                    {
                        Iterator<NamePosPair> iterator = cStack.iterator();
                        String tempName = packgeName;
                        while (iterator.hasNext())
                        {
                            NamePosPair namePosPair = iterator.next();
                            tempName += namePosPair.getName();
                        }
                        innerClass.add(tempName);
                    }
                    statementCount++;
                    nearDeclarationHead = true;
                }
                else
                {
                    //检查是否为方法头
                    Matcher functionMatcher = functionPattern.matcher(line);
                    if(functionMatcher.matches())
                    {
                        String functionName = getFunctionNameFromLine2(line);
                        functionMap.put(functionName,-1);
                        classStack.push(new NamePosPair("function_"+functionName,lineCount));
                        functionStack.push(new NamePosPair(functionName,lineCount));
                        nearDeclarationHead = true;
                        statementCount++;
                    }

                    else {
                        //检查是否为变量声明
                        Matcher variableMatcher = variablePattern.matcher(line);
                        if(variableMatcher.find())
                        {
                            List<VariableInfo> variableInfos = parseVariableNameFromLine(line);
                            for(VariableInfo variableInfo : variableInfos)
                            {
                                String variableName = variableInfo.getName();
                                //栈中元素数量大于1，说明目前为局部变量
                                if(classStack.size() > 1)
                                {
                                    variableInfo.setScope(1);
                                    variableInfo.setBelongTo(functionStack.peek().getName());
                                    //variableMap.put(variableName,variableMap.getOrDefault(variableName,"")+"local ");
                                }
                                else
                                {
                                    variableInfo.setScope(0);
                                    variableInfo.setBelongTo(cStack.peek().getName());
                                    //variableMap.put(variableName,variableMap.getOrDefault(variableName,"")+"global ");
                                }
                                variableInfoMap.put(variableIdCount++,variableInfo);
                            }
                            statementCount++;
                        }
                        else
                        {
                            executeCount++;
                            //不需要操作
                        }
                    }
                }
                nearDeclarationHead = dealWithStack(line,lineCount,nearDeclarationHead);
            }

        }

        Fileresult fileresult = new Fileresult();
        fileresult.setLinecount(lineCount);
        fileresult.setClasscount(classMap.size());
        fileresult.setFunctioncount(functionMap.size());
        fileresult.setVariablecount(variableIdCount);
        fileresult.setClasslist(classMap.toString());
        fileresult.setFunctionlist(functionMap.toString());
        fileresult.setVariablelist(variableInfoMap.toString());
        fileresult.setLongestclass(longestClass);
        fileresult.setLongestfunction(longestFunction);
        return fileresult;
    }


    private boolean dealWithStack(String line,int lineCount,boolean nearDeclarationHead){

        int lineLen = line.length();
        for(int i = 0; i < lineLen; i++)
        {
            if(inAnnotation == 0)
            {
                if(line.charAt(i) == '{')
                {
                    if(nearDeclarationHead == false)
                        classStack.push(new NamePosPair("temp"+lineCount+"_"+i,lineCount));

                    else{
                        nearDeclarationHead = false;
                    }
                }
                else if(line.charAt(i) == '}')
                {
                    NamePosPair prevPair = classStack.pop();
                    String name = prevPair.getName();
                    if(name.startsWith("temp"))
                    {
                    }
                    else if(name.startsWith("class"))
                    {
                        String className = name.substring(5);
                        classMap.put(className,lineCount - prevPair.getPos());
                        if(lineCount - prevPair.getPos() > longestClassLineCount)
                        {
                            longestClassLineCount = lineCount - prevPair.getPos();
                            longestClass = className;
                        }
                        cStack.pop();
                    }
                    else if(name.startsWith("function"))
                    {
                        String functionName = name.substring(8);
                        functionMap.put(functionName,lineCount - prevPair.getPos());
                        if(lineCount - prevPair.getPos() > longestFunctionLineCount)
                        {
                            longestFunctionLineCount = lineCount - prevPair.getPos();
                            longestFunction = functionName;
                        }
                        functionStack.pop();
                    }
                }
            }

            else if(i < lineLen - 1 && line.charAt(i) == '/' && line.charAt(i) == '/')
            {
                annotationCount++;
                break;
            }

            else if(i < lineLen - 1 && line.charAt(i) == '/' && line.charAt(i) == '*')
            {
                annotationStack.push(new NamePosPair("annotation"+ lineCount,lineCount));
                inAnnotation++;
            }

            else if(i < lineLen - 1 && line.charAt(i) == '*' && line.charAt(i) == '/')
            {
                NamePosPair prev = annotationStack.pop();
                annotationCount += lineCount - prev.getPos();
                inAnnotation--;
            }
        }
        return nearDeclarationHead;
    }

    //通过import来构建依赖关系
    public Set<String> importAnalyze(Set<String> projectPackageSet,String path) throws Exception {
        File sourceFile = new File(path);
        BufferedReader in = new BufferedReader(new FileReader(sourceFile));
        String line;
        Set<String> externalPackage = new HashSet<>();
        while((line = in.readLine()) != null)
        {
            line.trim();
            if(line.startsWith("import"))
            {
                int startIndex = 7;
                while(line.charAt(startIndex) == ' ')
                {
                    startIndex++;
                }
                int endIndex = startIndex;
                while(line.charAt(endIndex) != ';')
                {
                    endIndex++;
                }
                String importPath = line.substring(startIndex,endIndex);
                if(projectPackageSet.contains(importPath))
                {
                    internalImportSet.add(importPath);
                }
                else
                {
                    externalPackage.add(importPath);
                }
            }
        }
        return internalImportSet;
    }

    //通过变量声明来构建依赖关系
    public Set<String> fileDependencyAnalyze(Set<String> projectPackageSet,String path) throws Exception{

        fileDependencySet = new HashSet<>();
        for(Map.Entry<Integer,VariableInfo> entry : variableInfoMap.entrySet())
        {
            String variableClass = entry.getValue().getType();

            String inCurrentPackageFind = packgeName +"." + variableClass;
            //在同一级包下查找
            if(projectPackageSet.contains(inCurrentPackageFind))
            {
                fileDependencySet.add(inCurrentPackageFind);
                continue;
            }

            //在导包路径中查找
            for(String importPath : internalImportSet)
            {
                if(importPath.endsWith(".*"))
                {
                    String realImport = importPath.substring(0,importPath.length()-1) + variableClass;
                    if(projectPackageSet.contains(realImport))
                    {
                        fileDependencySet.add(realImport);
                        break;
                    }
                }

                else
                {
                    String []paths = importPath.split(" ");
                    if(paths[paths.length-1].equals(variableClass))
                    {
                        fileDependencySet.add(importPath);
                        break;
                    }
                }
            }

        }

        return fileDependencySet;

    }

    public void  secondInitBeforeCallAnalyze(){
        lineCount = 0;
        lineCount = 0;
        statementCount = 0;
        executeCount = 0;
        blankLine = 0;
    }

    public Set<String> callAnalyze(Set<String> projectPackageSet,String path) throws Exception{
        File sourceFile = new File(path);
        BufferedReader in = new BufferedReader(new FileReader(sourceFile));
        String callHeadRegex = "([\\w\\[\\]]{1,})\\s{0,}[\\.]\\s{0,}[\\w\\[\\]]{1,}";
        Pattern callPattern = Pattern.compile(callHeadRegex);
        String  classHeadRegex= "(public|private|protected){0,1}\\s{0,1}(class)\\s{0,}[\\w]{1,}";
        //from stackOverFlow                                                  这前面是限定符|    这一段是方法类型|    方法名|     括号传参   |   { 同时不匹配;
        String functionHeadRegex = "(public|protected|private|static|native|synchronized|\\s)+[\\w\\<\\>\\[\\]]+\\s+(\\w+) *\\([^\\)]*\\) *(\\{?|[^;])";
        String variableHeadRegex = "(public|private|protected){0,1}\\s{0,1}[\\w\\[\\]\\.]{1,}\\s{1,}[\\w\\[\\]]{1,}\\s{0,}[\\;\\=\\,]";
        Pattern functionPattern = Pattern.compile(functionHeadRegex);
        Pattern classPattern = Pattern.compile(classHeadRegex);
        Pattern variablePattern = Pattern.compile(variableHeadRegex);

        Set<String> callRegexSet = new HashSet<>();
        //处理同包下同级单个文件，这个不需要import就能生效
        for(String cname : projectPackageSet)
        {
            if(cname.startsWith(packgeName)&&!cname.equals(".*"))
            {
                String []s1 = cname.split("\\.");
                String []s2 = packgeName.split("\\.");
                if(s2.length==s1.length)
                {
                    internalSingleImportSet.add(s1[s1.length-1]);
                }
            }
        }
        //处理同项目内导入
        for(String importPath : internalImportSet)
        {
            if(importPath.endsWith(".*"))
            {
                String prefix = importPath.substring(0,importPath.length()-1);
                for(String s : projectPackageSet)
                {
                    if(!s.endsWith(".*")&&s.startsWith(prefix))
                    {
                        String unQualifiedName = s.substring(prefix.length());
                        internalSingleImportSet.add(unQualifiedName);
                        callClassMapping.put(unQualifiedName,s);
                    }
                }
            }
            else
            {
                String []sp = importPath.split("\\.");
                internalSingleImportSet.add(sp[sp.length-1]);
                callClassMapping.put(sp[sp.length-1],importPath);
            }
        }

        //构建出这个文件可以产生调用的语句形式 弃用
//        for(String className : internalSingleImportSet)
//        {
//            callRegexSet.add(className+".");
//        }

        //放弃使用之前的结果，重新开始分析变量，弃用
//        for(Map.Entry<Integer,VariableInfo> entry : variableInfoMap.entrySet())
//        {
//            callRegexSet.add(entry.getValue().getName()+".");
//        }

        String line;
        Map<String,Stack<VariableInfo>> curVariableMap = new HashMap<>();

        boolean nearDeclarationHead = false;


        while((line = in.readLine())!=null)
        {
            lineCount++;
            line.trim();
            if(line.length() == 0) {
                blankLine++;
            }
            //处理包名以及全限定类名
            else if(fullyQualifiedName.equals("")&&line.startsWith("package")){
                packgeName = getQualifiedNameFromLine(line);
                String sourceName = sourceFile.getName();
                String fileNameWithoutSuffix = sourceName.substring(0,sourceName.length()-5);
                fullyQualifiedName = packgeName + fileNameWithoutSuffix;
            }
            else{
                Matcher classMatcher = classPattern.matcher(line);
                //检查是否为类头
                if(classMatcher.find())
                {
                    String className = getClassNameFromLine(line);
                    classMap.put(className,-1);
                    classStack.push(new NamePosPair("class_"+className,lineCount));
                    cStack.push(new NamePosPair(className,lineCount));
                    if(cStack.size() > 1)
                    {
                        Iterator<NamePosPair> iterator = cStack.iterator();
                        String tempName = packgeName;
                        while (iterator.hasNext())
                        {
                            NamePosPair namePosPair = iterator.next();
                            tempName += namePosPair.getName();
                        }
                        innerClass.add(tempName);
                    }
                    else
                    {
                        innerClass.add(packgeName+className);
                    }
                    statementCount++;
                    nearDeclarationHead = true;
                }
                else
                {
                    //检查是否为方法头
                    Matcher functionMatcher = functionPattern.matcher(line);
                    if(functionMatcher.find())
                    {
                        String functionName = getFunctionNameFromLine2(line);
                        functionMap.put(functionName,-1);
                        classStack.push(new NamePosPair("function_"+functionName,lineCount));
                        functionStack.push(new NamePosPair(functionName,lineCount));
                        nearDeclarationHead = true;
                        statementCount++;
                    }

                    else {
                        //检查是否为变量声明
                        Matcher variableMatcher = variablePattern.matcher(line);
                        if(variableMatcher.find())
                        {
                            List<VariableInfo> variableInfos = parseVariableNameFromLine(line);
                            for(VariableInfo variableInfo : variableInfos)
                            {
                                String variableName = variableInfo.getName();
                                //栈中元素数量大于1，说明目前为局部变量
                                if(classStack.size() > 1)
                                {
                                    variableInfo.setScope(1);
                                    variableInfo.setBelongTo(functionStack.peek().getName());
                                }
                                else
                                {
                                    variableInfo.setScope(0);
                                    variableInfo.setBelongTo(cStack.peek().getName());
                                }
                                variableInfoMap.put(variableIdCount++,variableInfo);
                                Stack<VariableInfo> stack = curVariableMap.getOrDefault(variableName,new Stack<>());
                                stack.push(variableInfo);
                                curVariableMap.put(variableName,stack);
                            }
                            statementCount++;
                        }
                        else
                        {
                            executeCount++;

                        }

                        //接下来开始处理调用
                        Matcher callMatcher = callPattern.matcher(line);
                        if(callMatcher.find())
                        {
                            String classNameOrVariableName = classMatcher.group(1);
                            //说明是静态调用
                            if(callClassMapping.containsKey(classNameOrVariableName))
                            {
                                callSet.add(callClassMapping.get(classNameOrVariableName));
                            }
                            //否则为普通调用
                            else
                            {
                                String className = curVariableMap.get(classNameOrVariableName).peek().getType();
                                if(callClassMapping.containsKey(className))
                                callSet.add(callClassMapping.get(className));
                            }
                        }
                    }
                }
                nearDeclarationHead = dealStack2(line,lineCount,nearDeclarationHead,curVariableMap);
            }

        }

        return callSet;
    }

    public boolean dealStack2(String line,int lineCount,boolean nearDeclarationHead,Map<String,Stack<VariableInfo>> curVariableMap){

        int lineLen = line.length();
        for(int i = 0; i < lineLen; i++)
        {
            if(inAnnotation == 0)
            {
                if(line.charAt(i) == '{')
                {
                    if(nearDeclarationHead == false)
                        classStack.push(new NamePosPair("temp"+lineCount+"_"+i,lineCount));

                    else{
                        nearDeclarationHead = false;
                    }
                }
                else if(line.charAt(i) == '}')
                {
                    NamePosPair prevPair = classStack.pop();
                    String name = prevPair.getName();
                    if(name.startsWith("temp"))
                    {
                    }
                    else if(name.startsWith("class"))
                    {
                        String className = name.substring(5);
                        classMap.put(className,lineCount - prevPair.getPos());
                        clearMapStack(className,curVariableMap);
                        if(lineCount - prevPair.getPos() > longestClassLineCount)
                        {
                            longestClassLineCount = lineCount - prevPair.getPos();
                            longestClass = className;
                        }
                        cStack.pop();
                    }
                    else if(name.startsWith("function"))
                    {
                        String functionName = name.substring(8);
                        functionMap.put(functionName,lineCount - prevPair.getPos());
                        clearMapStack(functionName,curVariableMap);
                        if(lineCount - prevPair.getPos() > longestFunctionLineCount)
                        {
                            longestFunctionLineCount = lineCount - prevPair.getPos();
                            longestFunction = functionName;
                        }
                        functionStack.pop();
                    }
                }
            }

            else if(i < lineLen - 1 && line.charAt(i) == '/' && line.charAt(i) == '/')
            {
                annotationCount++;
                break;
            }

            else if(i < lineLen - 1 && line.charAt(i) == '/' && line.charAt(i) == '*')
            {
                annotationStack.push(new NamePosPair("annotation"+ lineCount,lineCount));
                inAnnotation++;
            }

            else if(i < lineLen - 1 && line.charAt(i) == '*' && line.charAt(i) == '/')
            {
                NamePosPair prev = annotationStack.pop();
                annotationCount += lineCount - prev.getPos();
                inAnnotation--;
            }
        }
        return nearDeclarationHead;
    }

    public void clearMapStack(String belongName,Map<String,Stack<VariableInfo>> curVariableMap){
        for(Map.Entry<String,Stack<VariableInfo>> entry : curVariableMap.entrySet())
        {
            Stack<VariableInfo> stack = entry.getValue();
            while (!stack.isEmpty()&&stack.peek().getBelongTo().equals(belongName))
            {
                stack.pop();
            }
        }
    }

    private String getQualifiedNameFromLine(String line){
        int index = 8;
        while(line.charAt(index)==' ')
        {
            index++;
        }
        return line.substring(index,line.length()-1).trim();
    }

    //旧版本，已弃用，有问题
    private List<String> getVariableNameFromLine(String line){
        String []words = line.split(" ");
        int n = words.length;
        boolean hasLimited = false;
        List<String> variables = new ArrayList<>();
        int count = 0;
        for(int i=0;i<n;i++)
        {
            if(words[i].equals("static")) count--;

            if(hasLimited == false) {
                if(!words[i].equals(""))
                {
                    count++;
                }
                if(words[i].equals("protected")||words[i].equals("public")||words[i].equals("private"))
                {
                    hasLimited = true;
                }
                if(count == 1){
                    variables.add(words[i]);
                }
            }

            else {
                if(!words[i].equals(""))
                {
                    count++;
                }


            }
        }

        return variables;

    }

    private List<VariableInfo> parseVariableNameFromLine(String line){
        List<VariableInfo> variableList = new ArrayList<>();
        int n = line.length();
        int i = 0;
        boolean typeDefined = false;
        boolean inAssign = false;
        int inParentheses = 0;
        boolean canAdd = false;
        String className = "";
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
                    if(!word.equals("static") && !word.equals("public") && !word.equals("protected") && !word.equals("private") && !word.equals("Synchronized"))
                    {
                        if(inAssign == false && inParentheses == 0 && typeDefined == false)
                        {
                            typeDefined = true;
                            className = word;
                            canAdd = true;
                        }

                        else if(typeDefined == true && inAssign == false && inParentheses == 0 && canAdd == true)
                        {
                            if(!word.equals(""))
                            {
                                variableList.add(new VariableInfo(className,word));
                            }
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

    private String getFunctionNameFromLine2(String line) {
        String regex = "([a-zA-Z0-9_]+) *\\(";
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(line);
        if(matcher.find())
        {
            return matcher.group(1);
        }
        return "";
    }

    private String getFunctionNameFromLine(String line) {
        String []words = line.split(" ");
        int count = 0;
        int n = words.length;
        boolean hasLimited = false;
        for(int i=0;i<n;i++)
        {
            if(words[i].equals("static")) count--;

            if(hasLimited == false) {
                if(!words[i].equals(""))
                {
                    count++;
                }
                if(words[i].equals("protected")||words[i].equals("public")||words[i].equals("private"))
                {
                    hasLimited = true;
                }
                if(count == 2) return words[i];
            }

            else {
                if(!words[i].equals(""))
                {
                    count++;
                }

                if(count == 3) return words[i];
            }
        }

        return "";

    }

    private String getClassNameFromLine(String line) {
        String []words = line.split(" ");
        int count = 0;
        int n = words.length;
        boolean hasLimited = false;
        for(int i=0;i<n;i++)
        {
            if(words[i].equals("static")) count--;

            if(hasLimited == false) {
                if(!words[i].equals(""))
                {
                    count++;
                }
                if(words[i].equals("protected")||words[i].equals("public")||words[i].equals("private"))
                {
                    hasLimited = true;
                }
                if(count == 1) return words[i];
            }

            else {
                if(!words[i].equals(""))
                {
                    count++;
                }

                if(count == 2) return words[i];
            }
        }

        return "";
    }

    public int calculateFunctionComplexity(String path,int start,int end) throws Exception {
        File sourceFile = new File(path);
        BufferedReader in = new BufferedReader(new FileReader(sourceFile));
        String line;
        int pos = 0;
        String []sourceCodes = new String[end - start + 1];
        while((line = in.readLine()) != null)
        {
            if(pos  < start)
            {
                pos++;
                continue;
            }
            else if(pos > end)
            {
                break;
            }
            else
            {
                sourceCodes[pos - start] = line;
                pos++;
            }
        }
        return calculateComplexity(sourceCodes);
    }

    private int calculateComplexity(String []codes) {
        //todo 连接函数复杂度算法 暂时无
        return 0;
    }



    public List<String> generateAst(String filePath) throws Exception {
        String execPath = "D:\\pythonProject\\pythonProject\\astgenerator.py";
        String []arguments = new String[]{filePath};
        return PythonCallUtil.executePythonCode(execPath,arguments);
    }

    public static void main(String[] args) throws Exception {
        String path = "D:\\leetCodePractice\\src\\com\\hlc\\alibaba\\ali1.java";
        FileAnalyze fileAnalyze = new FileAnalyze();
        fileAnalyze.fileAnalyze(path);
        System.out.println("类表为");
        Map<String,Integer> classMap = fileAnalyze.getClassMap();
        for(Map.Entry<String,Integer> entry : classMap.entrySet())
        {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("方法表为");
        Map<String,Integer> functionMap = fileAnalyze.getFunctionMap();
        for(Map.Entry<String,Integer> entry : functionMap.entrySet())
        {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("变量表为");
        Map<String,String> varMap = fileAnalyze.getVariableMap();
        for(Map.Entry<String,String> entry : varMap.entrySet())
        {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        System.out.println("行数为");
        System.out.println(fileAnalyze.lineCount);
        System.out.println("最长的类");
        System.out.println(fileAnalyze.getLongestFunction() + " " + fileAnalyze.getLongestFunctionLineCount());
        System.out.println("最长的方法");
        System.out.println(fileAnalyze.getLongestClass() + " " + fileAnalyze.getLongestClassLineCount());
    }
}



