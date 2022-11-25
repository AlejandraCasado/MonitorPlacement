package MPP.localSearch;

import MPP.structure.Instance;
import MPP.structure.RandomManager;
import MPP.structure.Solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ILS implements ILocalSearch{

    @Override
    public Solution execute(Solution sol, Instance instance) {

        boolean improve=true;
        while(improve)
        {
            improve=efficientLoop(sol,instance);
        }

        return sol;
    }

    private boolean efficientLoop(Solution sol, Instance instance){

        List<Integer> selectedNotSupportList=new ArrayList<>(sol.getSelectedNotSupportNodes());
        Collections.shuffle(selectedNotSupportList, RandomManager.getRandom());

        int freeNode1;
        int freeNode2;

        for (int removed1 : selectedNotSupportList) {
            freeNode1 = checkFreeNode(removed1, instance, sol);

            if (freeNode1 == -2) continue;
            else if (freeNode1 == -1) {
                sol.incrementNumExchanges();
                sol.removeNode(removed1);
                return true;
            }

            for (int node : instance.getAdjacencyList()[freeNode1]) {
                if (sol.getSelectedNotSupportNodes().contains(node)) {

                    freeNode2 = checkFreeNode(node, instance, sol);
                    if (freeNode2 == freeNode1 && node != removed1 && !instance.getAdjacencyList()[removed1].contains(node)) {
                        sol.removeNode(removed1);
                        sol.removeNode(node);
                        sol.addNode(freeNode1);
                        sol.incrementNumExchanges();
                        return true;
                    }
                }
            }


        }
        return false;
    }

    private int checkFreeNode(int node, Instance instance, Solution sol){

        int freeNode=-1;
        int countFreeNodes=0;

        for(int neighbour : instance.getAdjacencyList()[node]){
            //if(instance.getUnSelectedNodes().contains(neighbour)){
            if (sol.getUnselectedNodes().contains(neighbour)) {
                freeNode=neighbour;
                countFreeNodes++;
                if(countFreeNodes>1){
                    return -2;
                }
            }
        }

        return freeNode;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
