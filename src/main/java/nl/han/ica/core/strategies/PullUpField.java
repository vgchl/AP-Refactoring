package nl.han.ica.core.strategies;

/**
 * Created with IntelliJ IDEA.
 * User: Niek
 * Date: 9-11-12
 * Time: 14:15
 * To change this template use File | Settings | File Templates.
 */
public class PullUpField implements Strategy {

    private static final String STRATEGY_NAME = "Pull up duplicate fields.";
    private static final String STRATEGY_DESCRIPTION = "Avoid duplicating fields when it can be placed in the superclass.";

    @Override
    public String getName() {
        return STRATEGY_NAME;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getDescription() {
        return STRATEGY_DESCRIPTION;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
