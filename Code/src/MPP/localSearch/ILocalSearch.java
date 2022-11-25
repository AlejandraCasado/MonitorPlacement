package MPP.localSearch;

import MPP.structure.Instance;
import MPP.structure.Solution;

public interface ILocalSearch {

    Solution execute(Solution sol, Instance instance);
    String toString();

}
