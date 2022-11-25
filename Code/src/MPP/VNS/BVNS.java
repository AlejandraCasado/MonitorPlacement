package MPP.VNS;

import MPP.localSearch.ILocalSearch;
import MPP.structure.Instance;
import MPP.structure.Solution;

public class BVNS extends VNS{

    private ILocalSearch localSearch;

    public BVNS(ILocalSearch localSearch){
        this.localSearch=localSearch;
    }

    protected void procedure(int k, Solution solution, Instance instance){
        shakeExchangeNodesOfSameEdge(k,solution,instance);
        solution.removeRedundant();
        if (localSearch != null) {
            localSearch.execute(solution, instance);
        }
    }

    public ILocalSearch getLocalSearch() {
        return localSearch;
    }

    public String toString() {
        String localSearchName=localSearch!=null? ", "+localSearch.toString():"";
        return getClass().getSimpleName()+"(Iter"+iterations+", kMax"+ kMaxPCT+", kStep"+ kStepPCT+localSearchName+", "+shakeName+")";
    }
}
