package MPP.constructive;


import MPP.structure.Instance;
import MPP.structure.Solution;

public class GreedyConstructive implements IConstructive {

    @Override
    public Solution construct(Instance instance) {
        Solution solution=new Solution(instance);
        for (int node : instance.getSupportNodes()){
            solution.addNode(node);
        }
        boolean stop=false;
        while(!stop){
            int selectedNode=solution.getBestNextNode();
            if(selectedNode==-1)
                stop=true;
            else
                solution.addNode(selectedNode);
        }
        return solution;
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }
}
