package com.elisbite;

public class Debugger {
    Player playerRef;
    Game gameRef;

    public void init(Player player, Game game) {
        playerRef = player;
        gameRef = game;
    }

    public String getDebugScreenTextDisplay() {
        String textDisplay = "";

        // FPS
        textDisplay += gameRef.getCurrentFPS() + " FPS" + "\n";

        // Player XYZ Position
        textDisplay += "XYZ: " + (int)playerRef.getPlayerPos().x + "/" + (int)playerRef.getPlayerPos().y + "/" + (int)playerRef.getPlayerPos().z + "\n";

        // Player Direction
        float yawAngle = playerRef.getPlayerYaw(true);
        String cardinalDirection = "";
        String coordinateDirection = "";
        if (yawAngle < 45 && yawAngle > -45) {
            cardinalDirection = "NORTH";
            coordinateDirection = "NEGATIVE Z";
        } else if (yawAngle < 135 && yawAngle > 45) {
            cardinalDirection = "EAST";
            coordinateDirection = "POSITIVE X";
        } else if (yawAngle < -135 || yawAngle > 135) {
            cardinalDirection = "SOUTH";
            coordinateDirection = "POSITIVE Z";
        } else if (yawAngle < -45 && yawAngle > -135) {
            cardinalDirection = "WEST";
            coordinateDirection = "NEGATIVE X";
        }
        textDisplay += "FACING: " + cardinalDirection + "(TOWARDS " + coordinateDirection + ")" + "(" + yawAngle + ")";
        
        return textDisplay;
    }
}
