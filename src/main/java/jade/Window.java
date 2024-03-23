package jade;

import com.sun.org.apache.xerces.internal.impl.Version;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11C.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11C.glClearColor;

public class Window {

    private int width, height;
    String title;
    private static Window window =null;
    private long glfwindow;
    public float r,g,b,a;
    private boolean fadeToBlack =false;

    private static Scene currentScene ;




    private Window(){
        this.width=1920;
        this.height=1080;
        this.title = "Chmod";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
                currentScene = new LevelEditorScene();
                //currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Scene inconnue "+newScene+".";
                break;
        }

    }

    //singleton
    public static Window get(){

        if(Window.window == null){
            Window.window=new Window();
        }
        return Window.window;
    }

    public void run(){

        //System.out.println("Hello LWJGL " + Version.getVersion() + "!");
        init();
        loop();

        //gestion de la mémoire

        glfwFreeCallbacks(glfwindow);
        glfwDestroyWindow(glfwindow );

        gflwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void gflwTerminate() {
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();

        //init GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Impossible de d'initialiser GLFW.");
        }

        //config window

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE,GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //create the window

        glfwindow = glfwCreateWindow(this.width, this.height,this.title,NULL,NULL);

        if(glfwindow == NULL){

            throw new IllegalStateException("Impossible de créer l'objetc window");
        }

        glfwSetCursorPosCallback(glfwindow,MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwindow,MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwindow,KeyListener::keyCallback);
        glfwMakeContextCurrent(glfwindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwindow);

        GL.createCapabilities();

        Window.changeScene(0);


    }
    public void loop(){

        float beginTime = Time.getTime();
        float endTime;
        float dt =-1.0f;
        while(!glfwWindowShouldClose(glfwindow)){

            glfwPollEvents();

            glClearColor(r,g,b,a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(dt >= 0){

                currentScene.update(dt);

            }

            glfwSwapBuffers(glfwindow);

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        System.exit(0);
    }
}
