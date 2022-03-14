package com.hlc.codeanalyzesystem.JDT;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hlc.codeanalyzesystem.Analyze.FileDirVo;
import com.hlc.codeanalyzesystem.ComplexityAlgorithm.Halstead.HalsteadMetrics;
import com.hlc.codeanalyzesystem.ComplexityAlgorithm.Halstead.HalsteadUtil;
import com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap.Ex_caculate_test;
import com.hlc.codeanalyzesystem.ComplexityAlgorithm.mindmap.Link;
import com.hlc.codeanalyzesystem.entity.*;
import com.hlc.codeanalyzesystem.util.PowFitter;
import com.hlc.codeanalyzesystem.util.TransformUtil;
import org.eclipse.jdt.core.dom.*;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;


@Component
public class JDTAnalyze {

    public static final String  resourcePath = "D://resource//";

    private Integer lineCount;

    private Integer fileCount;

    private Integer classCount;

    private Integer functionCount;

    private String longestFile;

    private String longestClass;

    private String longestFunction;

    private Integer longestFileLine;

    private Integer longestClassLine;

    private Integer longestFunctionLine;

    private List<FileDirNodeVo> projectDirVo;

    private Set<String> projectPackage;

    Map<Integer,String> idNameMapping;

    Map<String,Integer> nameIdMapping;

    Map<String,Set<String>> dependency;

    Map<String,Set<String>> call;

    Map<String,String> pathNameMap;  //全限定类名--文件绝对路径

    Map<String,Set<String>> packageMap;  //包--包目录下的文件的全限定类名

    //Map<String,Set<String>> internalImportMap; //每个文件内的导入类的最后一级名字

    Map<String,Map<String,String>> shortQualifiedNameMap;//每个文件内的导入类的最后一级名字和全限定名

    Map<String,String> filePackageMap; //全限定名及所对应的包

    Map<String,HalsteadMetrics> fileHalsteadMap;

    int nameId;

    int dirId;

    public void init(){
        lineCount = 0;
        fileCount = 0;
        classCount = 0;
        functionCount = 0;
        nameId = 0;
        dirId = 1;
        longestFile = "";
        longestFileLine = 0;
        longestClass = "";
        longestClassLine = 0;
        longestFunction = "";
        longestFunctionLine = 0;
        projectDirVo = new ArrayList<>();
        projectPackage = new HashSet<>();
        idNameMapping = new HashMap<>();
        nameIdMapping = new HashMap<>();
        dependency = new HashMap<>();
        call = new HashMap<>();
        pathNameMap = new HashMap<>();
        packageMap = new HashMap<>();
        //internalImportMap = new HashMap<>();
        shortQualifiedNameMap = new HashMap<>();
        filePackageMap = new HashMap<>();
        fileHalsteadMap = new HashMap<>();
    }

//    public static void main(String[] args) {
//        String path = "D:\\leetCodePractice\\src\\com\\hlc\\alibaba\\ali1.java";
////        CompilationUnit comp = JdtAstUtil.getCompilationUnit(path);
////        StructureVisitor structureVisitor = new StructureVisitor();
////        comp.accept(structureVisitor);
//        CompilationUnit result = JdtAstUtil.getCompilationUnit(path);
//
//        List importList = result.imports();// 获取导入的包
//        for(Object object : importList)
//        {
//            ASTNode astNode = (ASTNode) object;
//            //System.out.println(astNode.getStructuralProperty());
//        }
//
//        List ty = result.types();
//        //获取第一个类，返回的为list强转成TypeDeclaration类型，后续可以使用TypeDeclaration的方法进行处理，获得方法和返回值等
//        TypeDeclaration type = (TypeDeclaration) ty.get(0);
//        TypeDeclaration[] file=type.getTypes(); //获取全局变量
//        for(TypeDeclaration var : file)
//        {
//            System.out.println("var: " + var.getName());
//        }
//
//        //获取类中的方法
//        MethodDeclaration[] methods=type.getMethods();
//
//        //对类中方法进行遍历处理，操作方法
//        for (MethodDeclaration method:methods){
//            //获得方法的名字   method.getName()为SimpleName类型，不能强转成String可以通过toString转成String类型
//            String name=method.getName().toString();
//            System.out.println(name);
//
//            //获取方法的内容
//            Block method_block = method.getBody();
//
//            //获取方法的参数
//            List methodParamertes=method.parameters();
//
//
//            //获取方法的返回值   getReturnType2()为空则为构造函数
//            if (null!=method.getReturnType2()){
//                //加入返回值为泛型，如果只要类型不需要具体泛型可以处理（如果为Map<String,String>,只需要具体类型Map）
//                if (method.getReturnType2().isParameterizedType()){ //判断是否是泛型
//                    //获取类型强转成ParameterizedType，再获得具体类型
//                    //ParameterizedType  extends type 都可以通过tostring来转化成String类型
//                    String returnType2=((ParameterizedType)method.getReturnType2()).getType().toString();
//                }
//
//            }else {
//                System.out.println("为空则该方法为构造函数");
//            }
//
//            //获取javadoc，javadoc为方法前面的/**  **/ 单行注释不计入javadoc
//            Javadoc javadoc=method.getJavadoc();
//
//            //获取方法的位置,获取方法起始的位置，包括javadoc，位置的计算为字符为单位，不是以行为单位，如果要以行为单位，可以通过result进行转化
//            int startPositionByChar=method.getStartPosition(); //会计算出所有的资产，然后得出当前方法的日志的字符的位置
//
//            //通过ASTNode换算出行数
//            int realPosition=result.getLineNumber(startPositionByChar);  //行数所在的真正位置
//
//            //得出方法的总字符数，也可以通过ASTNode计算出行数来的出方法总共所占的行数
//            int methodLenth=method.getLength();
//            //方法总共占的行数
//            int methodAllLine=result.getLineNumber(startPositionByChar+methodLenth);
//
//
//            List statements = method_block.statements();// 获取方法内容的所有行,一个statement为一行计数而非真正的行数，一个if/else为一行
//            for (int i=0;i<statements.size();i++){
//                System.out.println("获取方法内容的所有行"+i+statements.get(i));
//
//            }
//
//        }
//
//    }

    public static void testJdt(){
        String path = "D:\\leetCodePractice\\src\\com\\hlc\\alibaba\\ali1.java";
        CompilationUnit comp = JdtAstUtil.getCompilationUnit(path);
        StructureVisitor structureVisitor = new StructureVisitor();
        comp.accept(structureVisitor);
        List<TypeDeclaration> classes = structureVisitor.classList;
        List<MethodDeclaration> functions = structureVisitor.functionList;
        List<VariableDeclarationFragment> vars = structureVisitor.globalVarList;

        for (TypeDeclaration s : classes) {
            System.out.println("class : " + s.getName());
        }
        for (MethodDeclaration s : functions) {
            System.out.println("function : " + s.getName());
        }
        for (VariableDeclaration s : vars) {
            System.out.println("global_var : " + s.getName());
        }

        Map<MethodDeclaration,List<VariableDeclarationFragment>> localVarMap = structureVisitor.localVarMap;
        for (Map.Entry<MethodDeclaration,List<VariableDeclarationFragment>> entry : localVarMap.entrySet())
        {
            System.out.println(entry.getKey().getName());
            for(VariableDeclarationFragment v : entry.getValue())
            {
                System.out.print(JdtAstUtil.extractType(v) + " " + v.getName() + "  ");
            }
            System.out.println();
        }

        List<Object> importList = comp.imports();
        for(Object object : importList)
        {
            ImportDeclaration importDeclaration = (ImportDeclaration) object;
            System.out.println(importDeclaration.getName());
        }

        PackageDeclaration packageDeclaration = comp.getPackage();
        System.out.println(packageDeclaration.getName());

        System.out.println(comp.getLineNumber(comp.getLength()-1));


        System.out.println();
    }

    public static void testProjectAnalyze() throws Exception {
        ProjectresultWithBLOBs projectresultWithBLOBs = new ProjectresultWithBLOBs();
        JDTAnalyze jdtAnalyze = new JDTAnalyze();
        jdtAnalyze.init();
        projectresultWithBLOBs = jdtAnalyze.projectInitialAnalyze(1,projectresultWithBLOBs);
        System.out.println("DIR Structure");
        System.out.println(projectresultWithBLOBs.getProjectdir());
        ProjectResultVo projectResultVo = TransformUtil.transformProjectResultDoToVo(projectresultWithBLOBs);
//        System.out.println(projectResultVo.getProjectdir().get(0));
//        System.out.println("Complex File");
//        System.out.println(projectresultWithBLOBs.getComplexfile());
//        System.out.println("longestFile:" + projectresultWithBLOBs.getLongestfile());
//        System.out.println("longestClass:" + projectresultWithBLOBs.getLongestclass());
//        System.out.println(projectresultWithBLOBs.getProjectpackage());
//        System.out.println(projectresultWithBLOBs.getDependency());
//        System.out.println(projectresultWithBLOBs.getCall());
    }

    public static void testPrintAst(){
        String filePath = "D:\\leetCodePractice\\src\\com\\hlc\\alibaba\\ali1.java";
        CompilationUnit comp = JdtAstUtil.getCompilationUnit(filePath);
        List<TreeNodeVo> list = TransformUtil.transformASTNodeToList(comp);
//        for(TreeNodeVo treeNodeVo : list)
//        {
//            System.out.println(treeNodeVo.getId() + " " + " " + treeNodeVo.getPid() + " " + treeNodeVo.getNodeMessage());
//        }
        List<TreeNodeVo> res = TransformUtil.buildTree(list,0);
        Map<String,Object> map = new HashMap<>();
        map.put("AST",res);
        System.out.println(JSONUtil.toJsonStr(map));
    }

    public static void testCall(){
        String path = "D:\\resource\\1\\com\\hlc\\alibaba\\ali2.java";
        CompilationUnit comp = JdtAstUtil.getCompilationUnit(path);
        StructureVisitor structureVisitor = new StructureVisitor();
        comp.accept(structureVisitor);
        List<String> calls = structureVisitor.calls;
        for(String s : calls)
            System.out.println(s);
        System.out.println();
    }

    public static void testCallGraph() throws Exception {
        ProjectresultWithBLOBs projectresultWithBLOBs = new ProjectresultWithBLOBs();
        JDTAnalyze jdtAnalyze = new JDTAnalyze();
        jdtAnalyze.init();
        projectresultWithBLOBs = jdtAnalyze.projectInitialAnalyze(1,projectresultWithBLOBs);
        System.out.println(projectresultWithBLOBs.getFilecallrelation());
    }

    public static void main(String[] args) throws Exception {
        testProjectAnalyze();
    }

    public ProjectresultWithBLOBs projectInitialAnalyze(Integer id,ProjectresultWithBLOBs projectresultWithBLOBs) throws Exception {
        init();
        String projectPath = resourcePath + id +"//";
        dfsFile(projectPath,0);
        int [][]g = new int [nameId][nameId];
        int [][]c = new int [nameId][nameId];
        for(Map.Entry<String,Set<String>> entry : dependency.entrySet())
        {
            String key = entry.getKey();
            int from = nameIdMapping.get(key);
            Set<String> set = entry.getValue();
            for(String dpy : set)
            {
                //处理单个导入
                if(projectPackage.contains(dpy))
                {
                    String toD = pathNameMap.get(dpy);
                    int to = nameIdMapping.get(toD);
                    g[from][to] = 1;
 //                   Set<String> fileShortImport = internalImportMap.getOrDefault(key,new HashSet<>());
                    Map<String,String> shortNameQualifiedNameMap = shortQualifiedNameMap.getOrDefault(key,new HashMap<>());
                    //仅加入最后一级名称
                    String shortName = dpy.substring(dpy.lastIndexOf(".") + 1);
 //                   fileShortImport.add(shortName);
                    shortNameQualifiedNameMap.put(shortName,dpy);
  //                  internalImportMap.put(key,fileShortImport);
                    shortQualifiedNameMap.put(key,shortNameQualifiedNameMap);
                }
                //处理.*
                else if(packageMap.containsKey(dpy))
                {
                    Set<String> lowLevel = packageMap.get(dpy);
                    for(String ss : lowLevel)
                    {
                        String toD = pathNameMap.get(ss);
                        int to = nameIdMapping.get(toD);
                        g[from][to] = 1;
 //                       Set<String> fileShortImport = internalImportMap.getOrDefault(key,new HashSet<>());
                        Map<String,String> shortNameQualifiedNameMap = shortQualifiedNameMap.getOrDefault(key,new HashMap<>());
                        //仅加入最后一级名称
                        String shortName = ss.substring(ss.lastIndexOf(".") + 1);
   //                     fileShortImport.add(shortName);
                        shortNameQualifiedNameMap.put(shortName,ss);
  //                      internalImportMap.put(key,fileShortImport);
                        shortQualifiedNameMap.put(key,shortNameQualifiedNameMap);
                    }
                }
            }
        }
        for(Map.Entry<String,Set<String>> entry : call.entrySet())
        {
            String fileAbsolutePath = entry.getKey();
            Set<String> fileCall = entry.getValue();
            int from = nameIdMapping.get(fileAbsolutePath);
  //          Set<String> shortNameSet = internalImportMap.getOrDefault(fileAbsolutePath,new HashSet<>());
            String fileBelongToPackage = filePackageMap.get(fileAbsolutePath);
            Set<String> samePackageFile = packageMap.getOrDefault(fileBelongToPackage,new HashSet<>());
            Map<String,String> shortNameQualifiedNameMap = shortQualifiedNameMap.getOrDefault(fileAbsolutePath,new HashMap<>());
            for(String f : samePackageFile)
            {
                String shortName = f.substring(f.lastIndexOf(".") + 1);
 //               shortNameSet.add(shortName);
                shortNameQualifiedNameMap.put(shortName,f);
            }
            for(String callTypeStr : fileCall)
            {
                String qualifiedName = shortNameQualifiedNameMap.getOrDefault(callTypeStr,"");
                String filePath = pathNameMap.getOrDefault(qualifiedName,"");
                if(!filePath.equals(""))
                {
                    int to = nameIdMapping.get(filePath);
                    c[from][to] = 1;
                }
            }
        }

        Map<String,Integer> nameIdM = new HashMap<>(nameIdMapping);
        Map<Integer,String> idNameM = new HashMap<>(idNameMapping);
        Graph dependencyGraph = new Graph(g,idNameM,nameIdM);
        Graph callGraph = new Graph(c,idNameM,nameIdM);
        projectresultWithBLOBs.setFilecount(fileCount);
        projectresultWithBLOBs.setClasscount(classCount);
        projectresultWithBLOBs.setFunctioncount(functionCount);
        projectresultWithBLOBs.setLinecount(lineCount);
        projectresultWithBLOBs.setLongestclass(longestClass);
        projectresultWithBLOBs.setLongestfile(longestFile);
        projectresultWithBLOBs.setLongestfunction(longestFunction);
        projectresultWithBLOBs.setProjectpackage(JSON.toJSONString(projectPackage));
        projectresultWithBLOBs.setDependency(JSON.toJSONString(dependencyGraph));
        projectresultWithBLOBs.setFilecallrelation(JSON.toJSONString(callGraph));
        projectresultWithBLOBs.setProjectdir(JSON.toJSONString(projectDirVo));

//        List<FileDirNodeVo> fileDirNodeVoList = TransformUtil.buildDirTree(projectDirVo,0);
//       projectresultWithBLOBs.setProjectdir("[" + JSON.toJSONString(fileDirNodeVoList.get(0)) + "]");

        projectresultWithBLOBs.setComplexfile(JSON.toJSONString(fileHalsteadMap));

        String projectComplexity = ProjectComplexityUtil.calProjectComplex(dependencyGraph);
        projectresultWithBLOBs.setProjectcomplexity(projectComplexity);
        return projectresultWithBLOBs;
    }

    public void dfsFile(String filePath,int pid){
        File f = new File(filePath);
        for(File file : f.listFiles())
        {
            if(file.isDirectory())
            {
                projectDirVo.add(new FileDirNodeVo(dirId,pid,file.getName(),file.getAbsolutePath()));
                int tempId = dirId;
                dirId++;
                dfsFile(file.getAbsolutePath(),tempId);
            }
            else if(file.getName().endsWith(".java"))
            {
                projectDirVo.add(new FileDirNodeVo(dirId,pid,file.getName(),file.getAbsolutePath()));
                dirId++;
                fileCount++;
                try {
                    FileJDTResultModel fileJDTResultModel = fileJDTAnalyze(file.getAbsolutePath());
                    lineCount += fileJDTResultModel.getLineCount();
                    classCount += fileJDTResultModel.getClassCount();
                    functionCount += fileJDTResultModel.getFunctionCount();
                    idNameMapping.put(nameId,file.getAbsolutePath());
                    nameIdMapping.put(file.getAbsolutePath(),nameId);
                    nameId++;
                    String packageName = fileJDTResultModel.getPackageName();
                    if(fileJDTResultModel.getLineCount() > longestFileLine)
                    {
                        longestFileLine = fileJDTResultModel.getLineCount();
                        longestFile = file.getAbsolutePath();
                    }
                    if(fileJDTResultModel.getLongestClassLine() > longestClassLine)
                    {
                        longestClassLine = fileJDTResultModel.getLongestClassLine();
                        longestClass = fileJDTResultModel.getLongestClass();
                    }
                    if(fileJDTResultModel.getLongestFunctionLine() > longestFunctionLine)
                    {
                        longestFunctionLine = fileJDTResultModel.getLongestFunctionLine();
                        longestFunction = fileJDTResultModel.getLongestFunction();
                    }
                    for (TypeDeclaration typeDeclaration : fileJDTResultModel.getClasses())
                    {
                        projectPackage.add(packageName + "." + typeDeclaration.getName().toString());
                        Set<String> set = packageMap.getOrDefault(packageName,new HashSet<>());
                        set.add(packageName + "." + typeDeclaration.getName().toString());
                        packageMap.put(packageName,set);
                        pathNameMap.put(packageName + "." + typeDeclaration.getName().toString(),file.getAbsolutePath());
                    }
                    filePackageMap.put(file.getAbsolutePath(),packageName);

                    List<Object> importList = fileJDTResultModel.getImports();
                    for(Object object : importList)
                    {
                        ImportDeclaration importDeclaration = (ImportDeclaration) object;
                        Set<String> importSet = dependency.getOrDefault(file.getAbsolutePath(),new HashSet<>());
                        importSet.add(importDeclaration.getName().toString());
                        dependency.put(file.getAbsolutePath(),importSet);
                    }

                    List<String> callList = fileJDTResultModel.getCalls();
                    Set<String> fileCallSet = call.getOrDefault(file.getAbsolutePath(),new HashSet<>());
                    fileCallSet.addAll(callList);
                    call.put(file.getAbsolutePath(),fileCallSet);

                    fileHalsteadMap.put(file.getAbsolutePath(),fileJDTResultModel.getHalsteadMetrics());

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("FileAnalyzeException:"+file.getAbsolutePath() + " ");
                }
            }
        }
    }

    public FileJDTResultModel fileJDTAnalyze(String filePath) throws IOException {
        CompilationUnit comp = JdtAstUtil.getCompilationUnit(filePath);
        StructureVisitor structureVisitor = new StructureVisitor();
        comp.accept(structureVisitor);
        FileJDTResultModel fileJDTResultModel = new FileJDTResultModel();
        fileJDTResultModel.setClasses(structureVisitor.classList);
        fileJDTResultModel.setClassCount(structureVisitor.classList.size());
        fileJDTResultModel.setFunctions(structureVisitor.functionList);
        fileJDTResultModel.setFunctionCount(structureVisitor.functionList.size());
        fileJDTResultModel.setVars(structureVisitor.globalVarList);
        fileJDTResultModel.setVariableCount(structureVisitor.globalVarList.size());
        fileJDTResultModel.setLocalVarMap(structureVisitor.localVarMap);
        fileJDTResultModel.setLineCount(comp.getLineNumber(comp.getLength()-1));

        for (TypeDeclaration typeDeclaration : fileJDTResultModel.getClasses()) {
            int startLine = comp.getLineNumber(typeDeclaration.getStartPosition());
            int classLength = typeDeclaration.getLength();
            int endLine = comp.getLineNumber(typeDeclaration.getStartPosition() + classLength);
            if(fileJDTResultModel.getLongestClassLine() == null || endLine - startLine > fileJDTResultModel.getLongestClassLine())
            {
                fileJDTResultModel.setLongestClassLine(endLine - startLine);
                fileJDTResultModel.setLongestClass(typeDeclaration.getName().toString());
            }
        }

        for (MethodDeclaration methodDeclaration : fileJDTResultModel.getFunctions()) {
            int startLine = comp.getLineNumber(methodDeclaration.getStartPosition());
            int classLength = methodDeclaration.getLength();
            int endLine = comp.getLineNumber(methodDeclaration.getStartPosition() + classLength);
            if(fileJDTResultModel.getLongestFunctionLine() == null || endLine - startLine > fileJDTResultModel.getLongestFunctionLine())
            {
                fileJDTResultModel.setLongestFunctionLine(endLine - startLine);
                fileJDTResultModel.setLongestFunction(methodDeclaration.getName().toString());
            }
            HalsteadMetrics halsteadMetrics = HalsteadUtil.calculateFunctionHalstead(filePath,startLine,endLine);
            //System.out.println(methodDeclaration.getName() + " " + JSON.toJSONString(halsteadMetrics));
            fileJDTResultModel.getMethodComplexityMap().put(methodDeclaration,halsteadMetrics);
        }

        PackageDeclaration packageDeclaration = comp.getPackage();
        fileJDTResultModel.setPackageName(packageDeclaration.getName().toString());

        List<Object> importList = comp.imports();
        fileJDTResultModel.setImports(importList);
        fileJDTResultModel.setCalls(structureVisitor.calls);
        fileJDTResultModel.setHalsteadMetrics(HalsteadUtil.calculateFileHalstead(filePath));

        return fileJDTResultModel;
    }

}
