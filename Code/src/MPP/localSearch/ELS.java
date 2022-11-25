package MPP.localSearch;

import MPP.structure.Instance;
import MPP.structure.RandomManager;
import MPP.structure.Solution;

import java.util.*;

public class ELS implements ILocalSearch{

    @Override
    public Solution execute(Solution sol, Instance instance) {

        boolean improve=true;
        while(improve)
        {
            improve=loop(sol,instance);
        }

        return sol;
    }

    private boolean loop(Solution sol, Instance instance){

        List<Integer> selectedNotSupportList=new ArrayList<Integer>(sol.getSelectedNotSupportNodes());
        Collections.shuffle(selectedNotSupportList, RandomManager.getRandom());
        List<Integer> unselectedList=new ArrayList<Integer>(sol.getUnselectedNodes());
        Collections.shuffle(unselectedList,RandomManager.getRandom());
        for(int removed1 : selectedNotSupportList){
            sol.removeNode(removed1);
            for(int removed2 : selectedNotSupportList){
                sol.removeNode(removed2);
                for(int unselected : unselectedList){
                    sol.addNode(unselected);
                    sol.incrementNumExchanges();
                    if(sol.getPenalty()==0){
                        return true;
                    }
                    else sol.removeNode(unselected);
                }
                sol.addNode(removed2);
            }
            sol.addNode(removed1);
        }
        return false;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
