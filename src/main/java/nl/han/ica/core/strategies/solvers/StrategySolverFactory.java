package nl.han.ica.core.strategies.solvers;

import nl.han.ica.core.strategies.ReplaceMagicNumber;
import nl.han.ica.core.strategies.Strategy;

public class StrategySolverFactory {

    private StrategySolverFactory() {}
    
    public static StrategySolver createStrategySolver(Strategy strategy){
        if(strategy instanceof ReplaceMagicNumber){
            return new ReplaceMagicNumberSolver();
        }
        return null;
    }
}
