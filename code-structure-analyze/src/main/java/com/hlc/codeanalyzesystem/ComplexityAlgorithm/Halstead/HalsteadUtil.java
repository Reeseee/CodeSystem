package com.hlc.codeanalyzesystem.ComplexityAlgorithm.Halstead;

import com.alibaba.fastjson.JSON;
import com.hlc.codeanalyzesystem.entity.TreeNodeVo;
import com.hlc.codeanalyzesystem.util.TransformUtil;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HalsteadUtil {
    public static HalsteadVisitor2 parse(char[] str) {
        ASTParser parser = ASTParser.newParser(AST.JLS4);
        parser.setSource(str);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        parser.setEnvironment(new String[]{"C:\\Program Files\\Java\\jdk1.8.0_221\\bin"}, null, null, true);
        parser.setUnitName("any_name");
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

        // Check for compilationUnits problems in the provided file
//        IProblem[] problems = cu.getProblems();
//        for(IProblem problem : problems) {
//            // Ignore some error because of the different versions.
//            if (problem.getID() == 1610613332) 		 // 1610613332 = Syntax error, annotations are only available if source level is 5.0
//                continue;
//            else if (problem.getID() == 1610613329) // 1610613329 = Syntax error, parameterized types are only available if source level is 5.0
//                continue;
//            else if (problem.getID() == 1610613328) // 1610613328 = Syntax error, 'for each' statements are only available if source level is 5.0
//                continue;
//            else
//            {
//                // quit compilation if
//                System.out.println("CompilationUnit problem Message " + problem.getMessage() + " \t At line= "+problem.getSourceLineNumber() + "\t Problem ID="+ problem.getID());
//
//                System.out.println("The program will quit now!");
//                System.exit(1);
//            }
//        }

        // visit nodes of the constructed AST
        HalsteadVisitor2 visitor= new HalsteadVisitor2();
        cu.accept(visitor);

        return visitor;
    }

    public static HalsteadMetrics calculateFileHalstead(String filepath) throws IOException {
        char []codes = ReadFileToCharArray(filepath);
        HalsteadVisitor2 halsteadVisitor = parse(codes);
//        int OperatorCount=0;
//        int OperandCount=0;
//
//        OperatorCount=0;
//        for (int f : halsteadVisitor.oprt.values()) {
//            OperatorCount+= f;
//        }
//        OperandCount=0;
//        for (int f : halsteadVisitor.names.values()) {
//            OperandCount += f;
//        }
        for (String s : halsteadVisitor.names){
            System.out.println(s);
        }
        for (String s : halsteadVisitor.snames){
            System.out.println(s);
        }

        HalsteadMetrics hal = new HalsteadMetrics();
//        hal.setParameters(halsteadVisitor.oprt.size(), halsteadVisitor.names.size(), OperatorCount, OperandCount);
        hal.setParameters(halsteadVisitor.snames.size(), halsteadVisitor.names.size(), halsteadVisitor.operators, halsteadVisitor.operands);

        return hal;
    }

    public static HalsteadMetrics calculateFunctionHalstead(String filepath,int start,int end) throws IOException {
        String functionStr = ReadFunctionToCharArray(filepath,start,end);
        String addHeader = "public class A{ \n" + functionStr + "\n}\n";
        //System.out.println(addHeader);
        char []codes = addHeader.toCharArray();
        HalsteadVisitor2 halsteadVisitor = parse(codes);

        HalsteadMetrics hal = new HalsteadMetrics();
        hal.setParameters(halsteadVisitor.snames.size(), halsteadVisitor.names.size(), halsteadVisitor.operators, halsteadVisitor.operands);
        return hal;
    }

    // parse file in char array
    public static char[] ReadFileToCharArray(String filePath) throws IOException {
        byte[] input = null;
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
            input = new byte[bufferedInputStream.available()];
            bufferedInputStream.read(input);
            bufferedInputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFound Exception");
        } catch (IOException e) {
            System.out.println("IO Exception");
        }

        return  new String(input).toCharArray();
    }


    // parse file in char array
    public static String ReadFunctionToCharArray(String filePath,int start,int end) throws IOException {
        StringBuilder fileData = new StringBuilder(100000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int linePos = 0;
        while ((line = reader.readLine()) != null)
        {
            linePos ++;
            if (linePos >= start && linePos <= end)
            {
                fileData.append(line).append("\n");
            }
            else if (linePos > end)
            {
                break;
            }
        }
        reader.close();
        return  fileData.toString();
    }



    // parse files in a directory to list of char array
    public static List<char[]> ParseFilesInDir(List<String> JavaFiles) throws IOException{
        if(JavaFiles.isEmpty())
        {
            System.out.println("There is no java source code in the provided directory");
            System.exit(0);
        }

        List<char[]> FilesRead= new ArrayList<char []>();

        for(int i=0; i<JavaFiles.size(); i++)
        {
            System.out.println("Now, reading: "+ JavaFiles.get(i));
            FilesRead.add(ReadFileToCharArray(JavaFiles.get(i)));
        }

        return FilesRead;
    }



    // retrieve all .java files in the directory and subdirectories.
    public static List<String> retrieveFiles(String directory) {
        List<String> Files = new ArrayList<String>();
        File dir = new File(directory);

        if (!dir.isDirectory())
        {
            System.out.println("The provided path is not a valid directory");
            System.exit(1);
        }

        for (File file : dir.listFiles()) {
            if(file.isDirectory())
            {
                Files.addAll(retrieveFiles(file.getAbsolutePath()));
            }
            if (file.getName().endsWith((".java")))
            {
                Files.add(file.getAbsolutePath());
            }
        }

        return Files;
    }

    public static void main(String[] args) throws IOException {
        HalsteadMetrics halsteadMetrics = calculateFileHalstead("D:\\resource\\1\\com\\hlc\\alibaba\\ali3.java");
        //HalsteadMetrics halsteadMetrics= calculateFunctionHalstead("D:\\resource\\1\\com\\hlc\\alibaba\\ali1.java",29,51);
        System.out.println(JSON.toJSONString(halsteadMetrics));
    }

//    public static void main(String[] args) throws IOException {
//        // get the Directory name from the user
//        String DirName=null;
//        Scanner user_input = new Scanner( System.in );
//        System.out.print("Enter Directory Name: ");
//        DirName = user_input.next( );
//        user_input.close();
//        System.out.println("Directory Name is: " + DirName);
//
//        // retrieve all .java files in the directory and subdirectories.
//        List<String> JavaFiles=retrieveFiles(DirName);
//
//        // parse files in a directory to list of char array
//        List<char[]> FilesRead=ParseFilesInDir(JavaFiles);
//
//        HalsteadVisitor ASTVisitorFile;
//        int DistinctOperators=0;
//        int DistinctOperands=0;
//        int TotalOperators=0;
//        int TotalOperands=0;
//        int OperatorCount=0;
//        int OperandCount=0;
//
//        // Construct the AST of each java file. visit different nodes to get the number of operors and operands
//        // Retrieve the number of distinct operators, distinct operands,
//        // total operators, and total operands for each .java file in the directory.
//        // Sum each parameter from different files together to be used in Halstead Complexity metrics.
//        for(int i=0; i<FilesRead.size(); i++)
//        {
//
//            System.out.println("Now, AST parsing for : "+ JavaFiles.get(i));
//            ASTVisitorFile=parse(FilesRead.get(i));
//            DistinctOperators+=ASTVisitorFile.oprt.size();
//            DistinctOperands+=ASTVisitorFile.names.size();
//
//            OperatorCount=0;
//            for (int f : ASTVisitorFile.oprt.values()) {
//                OperatorCount+= f;
//            }
//            TotalOperators+=OperatorCount;
//
//            OperandCount=0;
//            for (int f : ASTVisitorFile.names.values()) {
//                OperandCount += f;
//            }
//            TotalOperands+=OperandCount;
//
//            System.out.println("Distinct Operators in this .java file = "+ ASTVisitorFile.oprt.size());
//            System.out.println("Distinct Operands in this .java file = "+ ASTVisitorFile.names.size());
//            System.out.println("Total Operators in this .java file = "+ OperatorCount);
//            System.out.println("Total Operands in this .java file = "+ OperandCount);
//            System.out.println("\n");
//        }
//
//        System.out.println("\n");
//        System.out.println("Overall Distinct Operators in the directory= "+ DistinctOperators);
//        System.out.println("Overall Distinct Operands in the directory= "+ DistinctOperands);
//        System.out.println("Overall Total Operators in the directory= "+ TotalOperators);
//        System.out.println("Overall Total Operands in the directory= "+ TotalOperands);
//
//        // calculate Halstead Complexity Metrics
//        System.out.println("\n");
//        System.out.println("###### Halstead Complexity Metrics ######");
//        HalsteadMetrics hal = new HalsteadMetrics();
//
//        hal.setParameters(DistinctOperators, DistinctOperands, TotalOperators, TotalOperands);
//        hal.getVocabulary();
//        hal.getProglen();
//        hal.getCalcProgLen();
//        hal.getVolume();
//        hal.getDifficulty();
//        hal.getEffort();
//        hal.getTimeReqProg();
//        hal.getTimeDelBugs();
//    }
}
