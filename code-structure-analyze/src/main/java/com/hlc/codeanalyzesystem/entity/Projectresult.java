package com.hlc.codeanalyzesystem.entity;

public class Projectresult {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.id
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.lineCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private Integer linecount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.fileCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private Integer filecount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.classCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private Integer classcount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.functionCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private Integer functioncount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.longestFile
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private String longestfile;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.longestClass
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private String longestclass;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.longestFunction
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private String longestfunction;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.projectComplexity
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private String projectcomplexity;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.id
     *
     * @return the value of projectresult.id
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.id
     *
     * @param id the value for projectresult.id
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.lineCount
     *
     * @return the value of projectresult.lineCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public Integer getLinecount() {
        return linecount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.lineCount
     *
     * @param linecount the value for projectresult.lineCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setLinecount(Integer linecount) {
        this.linecount = linecount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.fileCount
     *
     * @return the value of projectresult.fileCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public Integer getFilecount() {
        return filecount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.fileCount
     *
     * @param filecount the value for projectresult.fileCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setFilecount(Integer filecount) {
        this.filecount = filecount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.classCount
     *
     * @return the value of projectresult.classCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public Integer getClasscount() {
        return classcount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.classCount
     *
     * @param classcount the value for projectresult.classCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setClasscount(Integer classcount) {
        this.classcount = classcount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.functionCount
     *
     * @return the value of projectresult.functionCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public Integer getFunctioncount() {
        return functioncount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.functionCount
     *
     * @param functioncount the value for projectresult.functionCount
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setFunctioncount(Integer functioncount) {
        this.functioncount = functioncount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.longestFile
     *
     * @return the value of projectresult.longestFile
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public String getLongestfile() {
        return longestfile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.longestFile
     *
     * @param longestfile the value for projectresult.longestFile
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setLongestfile(String longestfile) {
        this.longestfile = longestfile == null ? null : longestfile.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.longestClass
     *
     * @return the value of projectresult.longestClass
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public String getLongestclass() {
        return longestclass;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.longestClass
     *
     * @param longestclass the value for projectresult.longestClass
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setLongestclass(String longestclass) {
        this.longestclass = longestclass == null ? null : longestclass.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.longestFunction
     *
     * @return the value of projectresult.longestFunction
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public String getLongestfunction() {
        return longestfunction;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.longestFunction
     *
     * @param longestfunction the value for projectresult.longestFunction
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setLongestfunction(String longestfunction) {
        this.longestfunction = longestfunction == null ? null : longestfunction.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.projectComplexity
     *
     * @return the value of projectresult.projectComplexity
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public String getProjectcomplexity() {
        return projectcomplexity;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.projectComplexity
     *
     * @param projectcomplexity the value for projectresult.projectComplexity
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setProjectcomplexity(String projectcomplexity) {
        this.projectcomplexity = projectcomplexity == null ? null : projectcomplexity.trim();
    }
}