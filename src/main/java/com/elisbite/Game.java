package com.elisbite;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {

    private long window;
    private World world = new World(); // The world contains all voxel information
    private Player player = new Player(); // The player
    private UI ui = new UI(); // The UI
    private Debugger debugger = new Debugger(); // Contains all debug functionality
    private int seed = 100;
    private Vector2i windowSize = new Vector2i(800, 600);

    /**
     * Handles initialization, game loop, and closing game
     */
    public void run() {
        System.out.println("Starting the game...");
        init();
        loop();
        glfwDestroyWindow(window);
        glfwTerminate();
    }

    /**
     * Sets up GL including window, camera, rendering, cursor
     */
    private void init() {
        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure the window properties
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // Create the window
        window = glfwCreateWindow(windowSize.x, windowSize.y, "Anyacraft", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create the GLFW window");
        }

        // Center the window
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidMode.width() - windowSize.x) / 2, (vidMode.height() - windowSize.y) / 2);

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1); // Enable V-Sync
        glfwShowWindow(window);

        // Create OpenGL capabilities
        GL.createCapabilities();

        // Enable depth testing for 3D rendering
        glEnable(GL_DEPTH_TEST);

        // Set up the projection matrix for perspective
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        float aspect = windowSize.x / windowSize.y;
        glFrustum(-aspect, aspect, -1.0, 1.0, 1.0, 100.0);

        // Enable face culling
        GL11.glEnable(GL11.GL_CULL_FACE);
        // Specify to cull back faces
        GL11.glCullFace(GL11.GL_BACK);

        // Hide cursor and capture it
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        glfwSetCursorPos(window, windowSize.x/2, windowSize.y/2); // Center the cursor

        // Switch back to model view matrix
        glMatrixMode(GL_MODELVIEW);

        player.init(ui);
        ui.init(debugger);
        debugger.init(player);
    }

    /**
     * The main game loop: swapping buffers, moving camera
     */
    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            // Clear the color and depth buffers
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            // Reset the model-view matrix
            glLoadIdentity();

            // Listen to Player inputs
            player.handleKeyboardInputs(window);
            player.handleMouseInputs(window);
            
            player.tick();

            // Render 3D World
            glEnable(GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_CULL_FACE);
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            float aspect = windowSize.x / windowSize.y;
            glFrustum(-aspect, aspect, -1.0, 1.0, 1.0, 100.0);
            world.renderWorld( player.getPlayerPos().x, player.getPlayerPos().z, seed );
            glMatrixMode(GL_MODELVIEW);

            // Render UI
            ui.tick(windowSize);
            
            // Swap the buffers to display the frame
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Game().run();
    }
}
