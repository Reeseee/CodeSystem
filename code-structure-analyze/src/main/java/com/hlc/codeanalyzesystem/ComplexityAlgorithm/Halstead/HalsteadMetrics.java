package com.hlc.codeanalyzesystem.ComplexityAlgorithm.Halstead;

public class HalsteadMetrics {

    public int DistOperators, DistOperands, TotOperators, TotOperands;
    private int Vocabulary=0;
    private int Proglen=0;
    private double CalcProgLen=0;
    private double Volume=0;
    private double Difficulty=0;
    private double Effort=0;
    private double TimeReqProg=0;
    private double TimeDelBugs=0;


    // Initialize the variables in the constructor
    public HalsteadMetrics() {
        DistOperators=0;
        DistOperands=0;
        TotOperators=0;
        TotOperands=0;
    }



    // set number of DistOperators, DistOperands, TotOperators, and TotOperands
    public void setParameters(int DistOprt, int DistOper, int TotOprt, int TotOper)
    {
        DistOperators=DistOprt;
        DistOperands=DistOper;
        TotOperators=TotOprt;
        TotOperands=TotOper;
        Vocabulary=DistOperators+DistOperands;
        Proglen=TotOperators+TotOperands;
        CalcProgLen = DistOperators*(Math.log(DistOperators) / Math.log(2)) + DistOperands*(Math.log(DistOperands) / Math.log(2));
        Volume=(TotOperators+TotOperands)*(Math.log(DistOperators+DistOperands)/Math.log(2));
        Difficulty=(DistOperators/2)*(TotOperands/(double)DistOperands);//
        Effort=((DistOperators/2)*(TotOperands/(double)DistOperands)) * ((TotOperators+TotOperands)*(Math.log(DistOperators+DistOperands)/Math.log(2)));
        TimeDelBugs = ((TotOperators+TotOperands)*(Math.log(DistOperators+DistOperands)/Math.log(2))) / 3000;
        TimeReqProg=(((DistOperators/2)*(TotOperands/(double)DistOperands)) * ((TotOperators+TotOperands)*(Math.log(DistOperators+DistOperands)/Math.log(2)))) /18;

    }

    public int getDistOperators() {
        return DistOperators;
    }

    public void setDistOperators(int distOperators) {
        DistOperators = distOperators;
    }

    public int getDistOperands() {
        return DistOperands;
    }

    public void setDistOperands(int distOperands) {
        DistOperands = distOperands;
    }

    public int getTotOperators() {
        return TotOperators;
    }

    public void setTotOperators(int totOperators) {
        TotOperators = totOperators;
    }

    public int getTotOperands() {
        return TotOperands;
    }

    public void setTotOperands(int totOperands) {
        TotOperands = totOperands;
    }

    public void setVocabulary(int vocabulary) {
        Vocabulary = vocabulary;
    }

    public void setProglen(int proglen) {
        Proglen = proglen;
    }

    public void setCalcProgLen(double calcProgLen) {
        CalcProgLen = calcProgLen;
    }

    public void setVolume(double volume) {
        Volume = volume;
    }

    public void setDifficulty(double difficulty) {
        Difficulty = difficulty;
    }

    public void setEffort(double effort) {
        Effort = effort;
    }

    public void setTimeReqProg(double timeReqProg) {
        TimeReqProg = timeReqProg;
    }

    public void setTimeDelBugs(double timeDelBugs) {
        TimeDelBugs = timeDelBugs;
    }

    // calculate the Program vocabulary
    public int getVocabulary()
    {
        return Vocabulary;
    }



    // calculate the Program length
    public int getProglen()
    {
        return Proglen;
    }



    // calculate the Calculated program length
    public double getCalcProgLen()
    {
         return CalcProgLen;
    }



    // calculate the Volume
    public double getVolume()
    {
        return Volume;
    }



    // calculate the Difficulty
    public double getDifficulty()
    {
        return Difficulty;
    }



    // calculate the Effort
    public double getEffort()
    {
        return Effort;
    }



    // calculate the Time required to program
    public double getTimeReqProg()
    {
        return TimeReqProg;
    }



    // calculate the Number of delivered bugs
    public double getTimeDelBugs()
    {
        return TimeDelBugs;
    }
}
