package de.saxsys.examples;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * A Simple Particle that draws its self as a circle.
 */
public class Particle {
    private static final double GRAVITY = 0.06;
    // properties for animation
    // and colouring
    double alpha;
    final double easing;
    double fade;
    double posX;
    double posY;
    double velX;
    double velY;
    final double targetX;
    final double targetY;
    final Paint color;
    final int size;
    final boolean usePhysics;
    final boolean shouldExplodeChildren;
    final boolean hasTail;
    double lastPosX;
    double lastPosY;

    public Particle(final double posX, final double posY, final double velX, final double velY, final double targetX,
            final double targetY, final Paint color, final int size, final boolean usePhysics,
            final boolean shouldExplodeChildren, final boolean hasTail) {
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.targetX = targetX;
        this.targetY = targetY;
        this.color = color;
        this.size = size;
        this.usePhysics = usePhysics;
        this.shouldExplodeChildren = shouldExplodeChildren;
        this.hasTail = hasTail;
        this.alpha = 1;
        this.easing = Math.random() * 0.02;
        this.fade = Math.random() * 0.1;
    }

    public boolean update() {
        lastPosX = posX;
        lastPosY = posY;
        if (this.usePhysics) { // on way down
            velY += GRAVITY;
            posY += velY;
            this.alpha -= this.fade; // fade out particle
        } else { // on way up
            final double distance = (targetY - posY);
            // ease the position
            posY += distance * (0.03 + easing);
            // cap to 1
            alpha = Math.min(distance * distance * 0.00005, 1);
        }
        posX += velX;
        return alpha < 0.005;
    }

    public void draw(final GraphicsContext context) {
        final double x = Math.round(posX);
        final double y = Math.round(posY);
        final double xVel = (x - lastPosX) * -5;
        final double yVel = (y - lastPosY) * -5;
        // set the opacity for all drawing of this particle
        context.setGlobalAlpha(Math.random() * this.alpha);
        // draw particle
        context.setFill(color);
        context.fillOval(x - size, y - size, size + size, size + size);
        // draw the arrow triangle from where we were to where we are now
        if (hasTail) {
            context.setFill(Color.rgb(255, 255, 255, 0.3));
            context.fillPolygon(new double[] {posX + 1.5, posX + xVel, posX - 1.5 }, new double[] {posY, posY + yVel,
                    posY }, 3);
        }
    }
}
