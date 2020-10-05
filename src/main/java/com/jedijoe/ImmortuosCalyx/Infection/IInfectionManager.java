package com.jedijoe.ImmortuosCalyx.Infection;

public interface IInfectionManager {
     int getInfectionProgress();
     void setInfectionProgress(int infectionProgress);
     void addInfectionProgress(int infectionProgress);
     int getInfectionTimer();
     void addInfectionTimer(int Time);
}
