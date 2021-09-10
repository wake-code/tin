package io.github.wakecode.engine;

import io.github.wakecode.engine.listeners.MouseListener;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width;
    private int height;
    private String title;
    private long glfwWindow;


    private static Window window = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Tin Engine";
    }

    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("[TIN DEBUG] LWJGL " + Version.getVersion());

        init();
        loop();

        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init() {
        GLFWErrorCallback.createPrint(System.err).set(); // error callback

        if (!glfwInit()) {
            throw new IllegalStateException("[TIN ERROR] Failed to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // default: true
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // default: true
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE); // default: false

        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL); // window

        if (glfwWindow == NULL) {
            throw new IllegalStateException("[TIN ERROR] Failed to create a window");
        }

        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);

        glfwMakeContextCurrent(glfwWindow); // context current creation
        glfwSwapInterval(1); // vsync
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
    }

    public void loop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            glfwPollEvents(); // poll events

            glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            glfwSwapBuffers(glfwWindow);
        }
    }
}
