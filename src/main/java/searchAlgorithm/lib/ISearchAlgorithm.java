package searchAlgorithm.lib;

import java.io.FileNotFoundException;

public interface ISearchAlgorithm<A extends Action>{
    A getAction(GameState<A> root, User user) throws FileNotFoundException;
}
