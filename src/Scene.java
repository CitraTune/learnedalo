import java.util.*;

public class Scene {

    private Map<String, Model> modelMap;
    private Projection projection;

    public Scene(int width, int height) {
        modelMap = new HashMap<>();
        projection = new Projection(width, height);
    }
    public void addEntity(Entity entity){
        //Organization stuff
        //Get the model id from the entity which will tell you
        String modelId = entity.getModelId();
        //Find which model that entity has from the modelId
        Model model = modelMap.get(modelId);
        //Confirmation that there actually is a model associated with the id
        if(model == null){
            throw new RuntimeException("Could not find model[" + modelId + "]");
        }
        //Add that entity to the list associated with the model we found the entity saying it has
        model.getEntitiesList().add(entity);
    }
    public void addModel(Model model){
        //Adds the model and its respective id to a map to grab from when creating an entity
        modelMap.put(model.getId(), model);
    }


    public Projection getProjection() {
        return projection;
    }

    public void resize(int width, int height) {
        projection.updateProjMatrix(width, height);
    }

    public void cleanup() {
        modelMap.values().forEach(Model::cleanup);
    }

    public Map<String, Model> getModelMap() {
        return modelMap;
    }

}