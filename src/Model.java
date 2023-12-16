
import java.util.*;

//Entity vs model: An entity is an in game model, actually applied and has properties.
//A model is like a class, there can be entities of the model class
//Similar to a solidworks assembly, where part studios are the models, and you can have parts with different positions etc.
//inside the assembly depending on how they are mated or moved.
public class Model {
    private final String id;
    private List<Entity> entitiesList;
    private List<Mesh> meshList;

    public Model(String id, List<Mesh> meshList) {
        this.id = id;
        this.meshList = meshList;
        entitiesList = new ArrayList<>();
    }
    public void cleanup(){
        meshList.forEach(Mesh::cleanup);
    }
    public List<Entity> getEntitiesList(){
        return entitiesList;
    }
    public String getId(){
        return id;
    }
    public List<Mesh> getMeshList(){
        return meshList;
    }
}
