
import org.joml.*;


import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements IAppLogic {
    //Constants
    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.002f;
    private static final int NUM_CHUNKS = 4;
    private static Entity carEntity;
    private Entity track;
    private Entity[][] terrainEntities;


    public static void main(String[] args) {
        Main main = new Main();
        Engine gameEng = new Engine("chapter-11", new Window.WindowOptions(), main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
        // Nothing to be done yet
    }
    @Override
    public void init(Window window, Scene scene, Render render) {
        Model carModel = ModelLoader.loadModel("car-model", "src/resources/models/bluecar/0.25sizecar.obj",
                scene.getTextureCache());
        scene.addModel(carModel);
        Model trackModel = ModelLoader.loadModel("track-model", "src/resources/models/track/track6.obj",
                scene.getTextureCache());
        scene.addModel(trackModel);

        carEntity = new Entity("car-entity", carModel.getId());
        carEntity.setScale(0.25f);
        carEntity.setPosition(0f, 0.095f, 0f);
        carEntity.updateModelMatrix();
        scene.addEntity(carEntity);
        track = new Entity("track-entity", trackModel.getId());
        track.setScale(0.25f);
        track.setPosition(0, 0.01f, 1.25f);
        //track.setRotation(1, 0, 0, (float)(Math.PI));
        track.updateModelMatrix();
        scene.addEntity(track);
        carEntity.setRotation(0,1,0,(float)Math.PI);

        String terrainModelId = "terrain";
        Model terrainModel = ModelLoader.loadModel(terrainModelId, "src/resources/models/terrain/terrain.obj",
                scene.getTextureCache());
        scene.addModel(terrainModel);
        Entity terrainEntity = new Entity("terrainEntity", terrainModelId);
        terrainEntity.setScale(100.0f);
        terrainEntity.updateModelMatrix();
        scene.addEntity(terrainEntity);


        SkyBox skyBox = new SkyBox("src/resources/models/skybox/skybox.obj", scene.getTextureCache());
        skyBox.getSkyBoxEntity().setScale(50f);
        scene.setSkyBox(skyBox);

        scene.setFog(new Fog(true, new Vector3f(0.2f, 0.2f, 0.2f), 0.1f));

        //scene.getCamera().moveUp(0.1f);

    }

    public static Entity getCarEntity() {
        return carEntity;
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis) {
        //move represents the difference in time since last run around
        float move = diffTimeMillis * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();

        //Car
        if (window.isKeyPressed(GLFW_KEY_W)) {
            carEntity.accelerate(move);
            //camera.accelerate(move);
        }
        else if (window.isKeyPressed(GLFW_KEY_S)) {
            carEntity.reverse(move);
            //camera.reverse(move);
        }
        if(carEntity.getSpeed() > 0) {
            if (window.isKeyPressed(GLFW_KEY_A)) {
                carEntity.steerLeft(move);
                //camera.steerLeft(move);
            } else if (window.isKeyPressed(GLFW_KEY_D)) {
                carEntity.steerRight(move);
                //camera.steerRight(move);
            }
        }
        else if(carEntity.getSpeed() < 0){
            if (window.isKeyPressed(GLFW_KEY_A)) {
                carEntity.steerLeft(-move);
                //camera.steerLeft(-move);
            } else if (window.isKeyPressed(GLFW_KEY_D)) {
                carEntity.steerRight(-move);
                //camera.steerRight(-move);
            }
        }
        carEntity.moveForward(move);
        camera.moveForward(move);
        if (window.isKeyPressed(GLFW_KEY_I)) {
            track.rotatex(move);
        }
        else if (window.isKeyPressed(GLFW_KEY_O)) {
            track.rotatey(move);
        }
        else if (window.isKeyPressed(GLFW_KEY_P)) {
            track.rotatez(move);
        }
        //Camera
        if (window.isKeyPressed(GLFW_KEY_UP)) {
            camera.moveForward2(move);
        }
        else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
            camera.moveBackwards(move);
        }
        if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
            camera.moveRight(move);
        }
        else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
            camera.moveLeft(move);
        }


        carEntity.updateModelMatrix();
        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY), (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
        }
    }

    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        // updateTerrain(scene);
    }

    public void updateTerrain(Scene scene) {
        int cellSize = 10;
        Camera camera = scene.getCamera();
        Vector3f cameraPos = camera.getPosition();
        int cellCol = (int) (cameraPos.x / cellSize);
        int cellRow = (int) (cameraPos.z / cellSize);

        int numRows = NUM_CHUNKS * 2 + 1;
        int numCols = numRows;
        int zOffset = -NUM_CHUNKS;
        float scale = cellSize / 2.0f;
        for (int j = 0; j < numRows; j++) {
            int xOffset = -NUM_CHUNKS;
            for (int i = 0; i < numCols; i++) {
                Entity entity = terrainEntities[j][i];
                entity.setScale(scale);
                entity.setPosition((cellCol + xOffset) * 2.0f, 0, (cellRow + zOffset) * 2.0f);
                entity.getModelMatrix().identity().scale(scale).translate(entity.getPosition());
                xOffset++;
            }
            zOffset++;
        }
    }
//    @Override
//    public boolean handleGuiInput(Scene scene, Window window) {
//        ImGuiIO imGuiIO = ImGui.getIO();
//        MouseInput mouseInput = window.getMouseInput();
//        Vector2f mousePos = mouseInput.getCurrentPos();
//        imGuiIO.setMousePos(mousePos.x, mousePos.y);
//        imGuiIO.setMouseDown(0, mouseInput.isLeftButtonPressed());
//        imGuiIO.setMouseDown(1, mouseInput.isRightButtonPressed());
//
//        return imGuiIO.getWantCaptureMouse() || imGuiIO.getWantCaptureKeyboard();
//    }

//    @Override
//    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {
//        if (inputConsumed) {
//            return;
//        }
//        float move = diffTimeMillis * MOVEMENT_SPEED;
//        Camera camera = scene.getCamera();
//        if (window.isKeyPressed(GLFW_KEY_W)) {
//            camera.moveForward(move);
//        } else if (window.isKeyPressed(GLFW_KEY_S)) {
//            camera.moveBackwards(move);
//        }
//        if (window.isKeyPressed(GLFW_KEY_A)) {
//            camera.moveLeft(move);
//        } else if (window.isKeyPressed(GLFW_KEY_D)) {
//            camera.moveRight(move);
//        }
//        if (window.isKeyPressed(GLFW_KEY_UP)) {
//            camera.moveUp(move);
//        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
//            camera.moveDown(move);
//        }
//
//        MouseInput mouseInput = window.getMouseInput();
//        if (mouseInput.isRightButtonPressed()) {
//            Vector2f displVec = mouseInput.getDisplVec();
//            camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY),
//                    (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
//        }
//    }

//    @Override
//    public void update(Window window, Scene scene, long diffTimeMillis) {
//        //rotation += 1.5;
//        if (rotation > 360) {
//            rotation = 0;
//        }
//        carEntity.setRotation(1, 1, 1, (float) Math.toRadians(rotation));
//        carEntity.updateModelMatrix();
//    }
//    @Override
//    public void drawGui() {
//        ImGui.newFrame();
//        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
//        ImGui.showDemoWindow();
//        ImGui.endFrame();
//        ImGui.render();
//    }
}
