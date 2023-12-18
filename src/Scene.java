

import java.util.*;

public class Scene {

    private Camera camera;
    private Fog fog;
    //private IGuiInstance guiInstance;
    private Map<String, Model> modelMap;
    private Projection projection;
    //  private SceneLights sceneLights;
    private TextureCache textureCache;
    private SkyBox skyBox;

    public Scene(int width, int height) {
        modelMap = new HashMap<>();
        projection = new Projection(width, height);
        textureCache = new TextureCache();
        camera = new Camera();
        camera.setPosition(0,0.25f,1f);
        fog = new Fog();

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


    public SkyBox getSkyBox() {
        return skyBox;
    }
    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }
    public void cleanup() {
        modelMap.values().forEach(Model::cleanup);
    }

    public Camera getCamera() {
        return camera;
    }
    public Fog getFog() {
        return fog;
    }
    public void setFog(Fog fog) {
        this.fog = fog;
    }

//    public IGuiInstance getGuiInstance() {
//        return guiInstance;
//    }

    public Map<String, Model> getModelMap() {
        return modelMap;
    }


    public Projection getProjection() {
        return projection;
    }

//    public SceneLights getSceneLights() {
//        return sceneLights;
//    }

    public TextureCache getTextureCache() {
        return textureCache;
    }

    public void resize(int width, int height) {
        projection.updateProjMatrix(width, height);
    }

//    public void setGuiInstance(IGuiInstance guiInstance) {
//        this.guiInstance = guiInstance;
//    }

//    public void setSceneLights(SceneLights sceneLights) {
//        this.sceneLights = sceneLights;
//    }

}