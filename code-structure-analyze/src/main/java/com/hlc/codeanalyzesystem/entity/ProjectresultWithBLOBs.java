package com.hlc.codeanalyzesystem.entity;

public class ProjectresultWithBLOBs extends Projectresult {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.projectDir
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private String projectdir;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.projectPackage
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private String projectpackage;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.dependency
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private String dependency;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.fileCallRelation
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private String filecallrelation;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column projectresult.complexFile
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    private String complexfile;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.projectDir
     *
     * @return the value of projectresult.projectDir
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public String getProjectdir() {
        return projectdir;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.projectDir
     *
     * @param projectdir the value for projectresult.projectDir
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setProjectdir(String projectdir) {
        this.projectdir = projectdir == null ? null : projectdir.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.projectPackage
     *
     * @return the value of projectresult.projectPackage
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public String getProjectpackage() {
        return projectpackage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.projectPackage
     *
     * @param projectpackage the value for projectresult.projectPackage
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setProjectpackage(String projectpackage) {
        this.projectpackage = projectpackage == null ? null : projectpackage.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.dependency
     *
     * @return the value of projectresult.dependency
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public String getDependency() {
        return dependency;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.dependency
     *
     * @param dependency the value for projectresult.dependency
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setDependency(String dependency) {
        this.dependency = dependency == null ? null : dependency.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.fileCallRelation
     *
     * @return the value of projectresult.fileCallRelation
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public String getFilecallrelation() {
        return filecallrelation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.fileCallRelation
     *
     * @param filecallrelation the value for projectresult.fileCallRelation
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setFilecallrelation(String filecallrelation) {
        this.filecallrelation = filecallrelation == null ? null : filecallrelation.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column projectresult.complexFile
     *
     * @return the value of projectresult.complexFile
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public String getComplexfile() {
        return complexfile;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column projectresult.complexFile
     *
     * @param complexfile the value for projectresult.complexFile
     *
     * @mbg.generated Wed Mar 09 17:38:48 CST 2022
     */
    public void setComplexfile(String complexfile) {
        this.complexfile = complexfile == null ? null : complexfile.trim();
    }
}