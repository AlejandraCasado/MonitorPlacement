package MPP.constructive;


import MPP.structure.Instance;
import MPP.structure.Solution;

import java.util.HashSet;
import java.util.Set;

public class GreedyDestructive implements IConstructive {

    @Override
    public Solution construct(Instance instance) {
        Solution solution=new Solution(instance);
        Set<Integer> copyUnselected=new HashSet<>(solution.getUnselectedNodes());
        for (int node: copyUnselected){
            solution.addNode(node);
        }
        boolean stop=false;
        while(!stop){
            int selectedNode=solution.getWorstNode();
            if(selectedNode==-1)
                stop=true;
            else
                solution.removeNode(selectedNode);
        }
        return solution;
    }


    public String toString() {
        return this.getClass().getSimpleName();
    }
}
